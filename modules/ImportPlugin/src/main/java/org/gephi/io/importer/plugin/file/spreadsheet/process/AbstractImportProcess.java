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

import java.io.Closeable;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.gephi.io.importer.api.ContainerLoader;
import org.gephi.io.importer.api.Report;
import org.gephi.io.importer.plugin.file.spreadsheet.SpreadsheetUtils;
import org.gephi.io.importer.plugin.file.spreadsheet.sheet.SheetParser;
import org.gephi.utils.progress.ProgressTicket;

/**
 *
 * @author Eduardo Ramos
 */
public abstract class AbstractImportProcess implements Closeable {

    protected final ContainerLoader container;
    protected final Report report;
    protected final ProgressTicket progressTicket;
    protected final SheetParser parser;
    protected boolean cancel = false;

    protected final Map<String, Integer> specialColumnsIndexMap = new HashMap<>();
    protected final Map<String, Integer> headersIndexMap = new HashMap<>();
    protected final Map<String, Class<?>> headersClassMap = new HashMap<>();

    public AbstractImportProcess(ContainerLoader container, ProgressTicket progressTicket, SheetParser parser) {
        this.container = container;
        this.progressTicket = progressTicket;
        this.parser = parser;

        this.report = new Report();
    }

    public abstract boolean execute();

    protected void setupColumnsIndexesAndFindSpecialColumns(List<String> specialColumnNames, Map<String, Class<?>> columnsClasses) {
        Map<String, Integer> headerMap = parser.getHeaderMap();
        for (Map.Entry<String, Integer> entry : headerMap.entrySet()) {
            String headerName = entry.getKey();
            int currentIndex = entry.getValue();
            boolean isSpecialColumn = false;

            //First check for special columns:
            for (String specialColumnName : specialColumnNames) {
                if (headerName.trim().equalsIgnoreCase(specialColumnName)) {
                    if (specialColumnsIndexMap.containsKey(specialColumnName)) {
                        logWarning("Repeated special column " + specialColumnName + ". Using only first occurrence");
                    } else {
                        specialColumnsIndexMap.put(specialColumnName, currentIndex);
                    }

                    isSpecialColumn = true;
                    break;
                }
            }

            if (isSpecialColumn) {
                continue;
            }

            Class<?> clazz = columnsClasses.containsKey(headerName) ? columnsClasses.get(headerName) : String.class;
            headersClassMap.put(headerName, clazz);

            if (isEdgesImport()) {
                container.addEdgeColumn(headerName, clazz);
            } else {
                container.addNodeColumn(headerName, clazz);
            }
            headersIndexMap.put(headerName, currentIndex);
        }
    }

    public boolean cancel() {
        return cancel = true;
    }

    protected void logInfo(String message) {
        SpreadsheetUtils.logInfo(report, message, parser);
    }

    protected void logWarning(String message) {
        SpreadsheetUtils.logWarning(report, message, parser);
    }

    protected void logError(String message) {
        SpreadsheetUtils.logError(report, message, parser);
    }

    @Override
    public void close() throws IOException {
        if (parser != null) {
            parser.close();
        }
    }

    public Report getReport() {
        return report;
    }

    public abstract boolean isEdgesImport();
}
