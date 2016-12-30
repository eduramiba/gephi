/*
 Copyright 2008-2010 Gephi
 Authors : Mathieu Bastian <mathieu.bastian@gephi.org>, Sebastien Heymann <sebastien.heymann@gephi.org>
 Website : http://www.gephi.org

 This file is part of Gephi.

 DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.

 Copyright 2011 Gephi Consortium. All rights reserved.

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

 Portions Copyrighted 2011 Gephi Consortium.
 */
package org.gephi.io.importer.plugin.file.spreadsheet;

import java.io.IOException;
import java.io.Reader;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;
import org.apache.commons.csv.CSVParser;
import org.gephi.graph.api.types.IntervalSet;
import org.gephi.io.importer.api.ContainerLoader;
import org.gephi.io.importer.api.Report;
import org.gephi.io.importer.plugin.file.spreadsheet.sheets.SheetParser;
import org.gephi.io.importer.plugin.file.spreadsheet.sheets.csv.CSVSheetParser;
import org.gephi.io.importer.spi.FileImporter;
import org.gephi.utils.longtask.spi.LongTask;
import org.gephi.utils.progress.ProgressTicket;
import org.openide.util.Exceptions;

/**
 *
 * @author Eduardo Ramos
 */
public class ImporterSpreadsheet implements FileImporter, LongTask {

    private Reader reader;
    private ContainerLoader container;
    private Report report;
    private ProgressTicket progressTicket;
    private boolean cancel = false;

    private AbstractImport importer = null;

    @Override
    public boolean execute(ContainerLoader container) {
        this.container = container;
        this.report = new Report();

        Map<String, Class<?>> columnsClasses = new HashMap<>();
        columnsClasses.put("timeset", IntervalSet.class);//DEBUG

        try (SheetParser parser = buildCSVParser()) {
            importNodes(parser, columnsClasses, false);
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }

        //TODO: manage time representation, timezone...
        return !cancel;
    }

    private SheetParser buildCSVParser() throws IOException {
        CSVParser csvParser = SpreadsheetUtils.configureCSVParser(reader, ','); //TODO separator
        return new CSVSheetParser(csvParser);
    }

    public void importNodes(SheetParser parser, Map<String, Class<?>> columnTypes, boolean assignNewNodeIds) throws IOException {
        SpreadsheetNodesConfiguration config = new SpreadsheetNodesConfiguration(columnTypes, assignNewNodeIds);
        ImportNodes nodesImporter = new ImportNodes(config, parser, container, progressTicket);
        importer = nodesImporter;
        
        nodesImporter.execute();
        importer = null;
        report.append(nodesImporter.getReport());
    }

    @Override
    public void setReader(Reader reader) {
        this.reader = reader;
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
}
