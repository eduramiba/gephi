package org.gephi.io.importer.plugin.file.spreadsheet;

import java.io.Closeable;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.gephi.io.importer.api.ContainerLoader;
import org.gephi.io.importer.api.Report;
import org.gephi.io.importer.plugin.file.spreadsheet.sheets.SheetParser;
import org.gephi.utils.progress.ProgressTicket;

/**
 *
 * @author Eduardo Ramos
 */
public abstract class AbstractImport implements Closeable {

    protected final ContainerLoader container;
    protected final Report report;
    protected final ProgressTicket progressTicket;
    protected final SheetParser parser;
    protected boolean cancel = false;

    protected final Map<String, Integer> specialColumnsIndexMap = new HashMap<>();
    protected final Map<String, Integer> headersIndexMap = new HashMap<>();
    protected final Map<String, Class<?>> headersClassMap = new HashMap<>();

    public AbstractImport(ContainerLoader container, ProgressTicket progressTicket, SheetParser parser) {
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
