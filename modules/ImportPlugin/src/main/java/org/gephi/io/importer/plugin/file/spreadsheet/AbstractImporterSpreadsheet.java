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
package org.gephi.io.importer.plugin.file.spreadsheet;

import java.io.File;
import org.gephi.io.importer.plugin.file.spreadsheet.process.SpreadsheetNodesConfiguration;
import org.gephi.io.importer.plugin.file.spreadsheet.process.ImportNodesProcess;
import org.gephi.io.importer.plugin.file.spreadsheet.process.AbstractImportProcess;
import java.io.IOException;
import java.io.Reader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.gephi.graph.api.TimeRepresentation;
import org.gephi.graph.api.types.IntervalSet;
import org.gephi.io.importer.api.ContainerLoader;
import org.gephi.io.importer.api.Report;
import org.gephi.io.importer.plugin.file.spreadsheet.sheet.SheetParser;
import org.gephi.io.importer.spi.FileImporter;
import org.gephi.utils.longtask.spi.LongTask;
import org.gephi.utils.progress.ProgressTicket;
import org.joda.time.DateTimeZone;
import org.openide.util.Exceptions;

/**
 *
 * @author Eduardo Ramos
 */
public abstract class AbstractImporterSpreadsheet implements FileImporter, FileImporter.FileAware, LongTask {

    protected ContainerLoader container;
    protected Report report;
    protected ProgressTicket progressTicket;
    protected boolean cancel = false;

    protected AbstractImportProcess importer = null;

    protected File file;

    //General configuration:
    protected Table table = Table.NODES;
    protected TimeRepresentation timeRepresentation = TimeRepresentation.INTERVAL;
    protected DateTimeZone timeZone = DateTimeZone.UTC;

    public enum Table {
        NODES,
        EDGES;
    }

    @Override
    public boolean execute(ContainerLoader container) {
        this.container = container;
        this.report = new Report();

        this.container.setTimeRepresentation(timeRepresentation);
        this.container.setTimeZone(timeZone);

        if (table == Table.EDGES) {

        } else {
            Map<String, Class<?>> columnsClasses = new HashMap<>();
            columnsClasses.put("timeset", IntervalSet.class);//DEBUG

            try (SheetParser parser = createParser()) {
                importNodes(parser, columnsClasses, false);
            } catch (IOException ex) {
                Exceptions.printStackTrace(ex);
            }
        }

        return !cancel;
    }

    public abstract SheetParser createParser() throws IOException;

    public Map<String, Integer> getHeadersMap() throws IOException {
        try (SheetParser parser = createParser()) {
            return parser.getHeaderMap();
        }
    }

    public void importNodes(SheetParser parser, Map<String, Class<?>> columnTypes, boolean assignNewNodeIds) throws IOException {
        SpreadsheetNodesConfiguration config = new SpreadsheetNodesConfiguration(columnTypes, assignNewNodeIds);
        ImportNodesProcess nodesImporter = new ImportNodesProcess(config, parser, container, progressTicket);
        importer = nodesImporter;

        nodesImporter.execute();
        importer = null;
        report.append(nodesImporter.getReport());
    }

    private void autoDetectTable() {
        try {
            Set<String> headersLowerCase = new HashSet<>();

            for (String header : getHeadersMap().keySet()) {
                headersLowerCase.add(header.trim().toLowerCase());
            }

            if (headersLowerCase.contains("source") || headersLowerCase.contains("target") || headersLowerCase.contains("weight")) {
                table = Table.EDGES;
            } else {
                table = Table.NODES;
            }
        } catch (IOException ex) {
        }
    }

    @Override
    public void setReader(Reader reader) {
        //We can't use a reader since we might need to read the file many times (get the headers first, then read again...)
        //See setFile(File file)
    }

    @Override
    public void setFile(File file) {
        this.file = file;

        autoDetectTable();
    }

    public File getFile() {
        return file;
    }

    @Override
    public ContainerLoader getContainer() {
        return container;
    }

    @Override
    public Report getReport() {
        return report;
    }

    @Override
    public boolean cancel() {
        if (importer != null) {
            importer.cancel();
            importer = null;
        }
        return cancel = true;
    }

    @Override
    public void setProgressTicket(ProgressTicket progressTicket) {
        this.progressTicket = progressTicket;
    }

    public Table getTable() {
        return table;
    }

    public void setTable(Table table) {
        this.table = table;
    }

    public TimeRepresentation getTimeRepresentation() {
        return timeRepresentation;
    }

    public void setTimeRepresentation(TimeRepresentation timeRepresentation) {
        this.timeRepresentation = timeRepresentation;
    }

    public DateTimeZone getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(DateTimeZone timeZone) {
        this.timeZone = timeZone;
    }
}
