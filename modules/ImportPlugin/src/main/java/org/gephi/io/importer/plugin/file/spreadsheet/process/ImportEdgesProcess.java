/*
Copyright 2008-2016 Gephi
Authors : Eduardo Ramos <eduardo.ramos@gephi.org>
Website : http://www.gephi.org

This file is part of Gephi.

DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.

Copyright 2016 Gephi Consortium. All rights reserved.

The contents of this file are subject to the terms of either the GNU
General Public License Version 3 only ("GPL") or the Common
Development and Distribution License("CDDL") (collectively, the
"License"). You may not use this file except in compliance with the
License. You can obtain a copy of the License at
http://gephi.org/about/legal/license-notice/
or /cddl-1.0.txt and /gpl-3.0.txt. See the License for the
specific language governing permissions and limitations under the
License.  When distributing the software, include this License Header
Notice in each file and include the License files at
/cddl-1.0.txt and /gpl-3.0.txt. If applicable, add the following below the
License Header, with the fields enclosed by brackets [] replaced by
your own identifying information:
"Portions Copyrighted [year] [name of copyright owner]"

If you wish your version of this file to be governed by only the CDDL
or only the GPL Version 3, indicate your decision by adding
"[Contributor] elects to include this software in this distribution
under the [CDDL or GPL Version 3] license." If you do not indicate a
single choice of license, a recipient has the option to distribute
your version of this file under either the CDDL, the GPL Version 3 or
to extend the choice of license to its licensees as provided above.
However, if you add GPL Version 3 code and therefore, elected the GPL
Version 3 license, then the option applies only if the new code is
made subject to such option by the copyright holder.

Contributor(s):

Portions Copyrighted 2016 Gephi Consortium.
 */
package org.gephi.io.importer.plugin.file.spreadsheet.process;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;
import org.gephi.graph.api.AttributeUtils;
import org.gephi.io.importer.api.ContainerLoader;
import org.gephi.io.importer.api.EdgeDirection;
import org.gephi.io.importer.api.EdgeDirectionDefault;
import org.gephi.io.importer.api.EdgeDraft;
import org.gephi.io.importer.api.EdgeWeightMergeStrategy;
import org.gephi.io.importer.plugin.file.spreadsheet.sheet.SheetParser;
import org.gephi.io.importer.plugin.file.spreadsheet.sheet.SheetRow;
import org.gephi.utils.progress.ProgressTicket;

/**
 *
 * @author Eduardo Ramos
 */
public class ImportEdgesProcess extends AbstractImportProcess {

    private static final String EDGE_SOURCE = "source";
    private static final String EDGE_TARGET = "target";
    private static final String EDGE_TYPE = "type";
    private static final String EDGE_KIND = "kind";

    private static final String EDGE_ID = "id";
    private static final String EDGE_LABEL = "label";
    private static final String EDGE_WEIGHT = "weight";

    private final SpreadsheetEdgesConfiguration config;

    public ImportEdgesProcess(SpreadsheetGeneralConfiguration generalConfig, SpreadsheetEdgesConfiguration config, SheetParser parser, ContainerLoader container, ProgressTicket progressTicket) throws IOException {
        super(generalConfig, container, progressTicket, parser);
        this.config = config;

        init();
    }

    private void init() {
        //Make sure default container configuration is correct for importing edges:
        container.setAllowParallelEdge(true);
        container.setAllowSelfLoop(true);
        container.setEdgeDefault(EdgeDirectionDefault.MIXED);
        container.setEdgesMergeStrategy(EdgeWeightMergeStrategy.SUM);
    }

    @Override
    public boolean execute() {
        final boolean createMissingNodes = config.isCreateMissingNodes();

        setupColumnsIndexesAndFindSpecialColumns(Arrays.asList(EDGE_SOURCE, EDGE_TARGET, EDGE_TYPE, EDGE_KIND, EDGE_ID, EDGE_LABEL, EDGE_WEIGHT), generalConfig.getColumnsClasses());

        Integer sourceColumnIndex = specialColumnsIndexMap.get(EDGE_SOURCE);
        Integer targetColumnIndex = specialColumnsIndexMap.get(EDGE_TARGET);
        Integer typeColumnIndex = specialColumnsIndexMap.get(EDGE_TYPE);//Direction
        Integer kindColumnIndex = specialColumnsIndexMap.get(EDGE_KIND);//Kind for parallel edges

        Integer idColumnIndex = specialColumnsIndexMap.get(EDGE_ID);
        Integer labelColumnIndex = specialColumnsIndexMap.get(EDGE_LABEL);
        Integer weightColumnIndex = specialColumnsIndexMap.get(EDGE_WEIGHT);

        progressTicket.start();
        for (SheetRow row : parser) {
            if (cancel) {
                break;
            }

            if (!row.isConsistent()) {
                logError("The record is inconsistent. Skipping record");
                continue;
            }

            String source = null;
            String target = null;
            String id = null;
            String label = null;
            double weight = 1;
            EdgeDirection direction = EdgeDirection.DIRECTED;
            String kind = null;

            if (sourceColumnIndex != null) {
                source = row.get(sourceColumnIndex);
            }
            if (targetColumnIndex != null) {
                target = row.get(targetColumnIndex);
            }
            if (typeColumnIndex != null) {
                String type = row.get(typeColumnIndex);
                if ("undirected".equalsIgnoreCase(type)) {
                    direction = EdgeDirection.UNDIRECTED;
                }
            }
            if (kindColumnIndex != null) {
                kind = row.get(kindColumnIndex);
            }

            if (idColumnIndex != null) {
                id = row.get(idColumnIndex);
            }
            if (labelColumnIndex != null) {
                label = row.get(labelColumnIndex);
            }
            if (weightColumnIndex != null) {
                String weightStr = row.get(weightColumnIndex);
                if(weightStr != null){
                    try {
                        weight = Double.parseDouble(weightStr);
                    } catch (Exception ex) {
                        logError(String.format("Error parsing weight '%s' as double", weightStr));
                    }
                }
            }

            if (id != null && container.edgeExists(id)) {
                logError("Edge with id '" + id + "' already exists. Skipping record");
                continue;
            }

            EdgeDraft edge = id != null ? container.factory().newEdgeDraft(id) : container.factory().newEdgeDraft();

            if (label != null) {
                edge.setLabel(label);
            }

            if (source == null || target == null) {
                logError("Ignoring edge due to empty source and/or target node ids");
                continue;
            }

            if (!container.nodeExists(source) && !createMissingNodes) {
                logWarning(String.format("Missing source node '%s' and create missing nodes is disabled. Skipping record", source));
                continue;
            }

            if (!container.nodeExists(target) && !createMissingNodes) {
                logWarning(String.format("Missing target node '%s' and create missing nodes is disabled. Skipping record", target));
                continue;
            }

            edge.setSource(container.getNode(source));
            edge.setTarget(container.getNode(target));
            edge.setDirection(direction);
            edge.setWeight(weight);

            if (kind != null) {
                edge.setType(kind);
            }

            for (Map.Entry<String, Integer> columnEntry : headersIndexMap.entrySet()) {
                String column = columnEntry.getKey();
                Integer index = columnEntry.getValue();
                Class type = headersClassMap.get(column);

                if (type == null) {
                    continue;
                }

                Object value = row.get(index);
                if (value != null) {
                    try {
                        value = AttributeUtils.parse((String) value, type);
                    } catch (Exception e) {
                        logError(String.format("Error when parsing value '%s' as a '%s'", value, type));
                        value = null;
                    }

                    if (value != null) {
                        edge.setValue(column, value);
                    }
                }
            }

            container.addEdge(edge);
        }

        progressTicket.finish();

        return !cancel;
    }

    @Override
    protected void addColumn(String name, Class type) {
        container.addEdgeColumn(name, type);
    }
}
