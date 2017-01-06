package org.gephi.io.importer.plugin.file.spreadsheet.process;

import java.util.LinkedHashMap;
import java.util.Map;
import org.gephi.graph.api.TimeRepresentation;
import org.joda.time.DateTimeZone;

/**
 *
 * @author Eduardo Ramos
 */
public class SpreadsheetGeneralConfiguration {
    
    public enum Table {
        NODES,
        EDGES;
    }

    protected final Map<String, Class> columnsClasses = new LinkedHashMap<>();

    protected Table table = Table.NODES;
    protected TimeRepresentation timeRepresentation = TimeRepresentation.INTERVAL;
    protected DateTimeZone timeZone = DateTimeZone.UTC;

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

    public Map<String, Class> getColumnsClasses() {
        return new LinkedHashMap<>(columnsClasses);
    }

    public void setColumnsClasses(Map<String, Class> columnsClasses) {
        this.columnsClasses.clear();
        if (columnsClasses != null) {
            for (String column : columnsClasses.keySet()) {
                setColumnClass(column, columnsClasses.get(column));
            }
        }
    }

    public Class getColumnClass(String column) {
        return columnsClasses.get(column);
    }

    public void setColumnClass(String column, Class clazz) {
        columnsClasses.put(column.trim(), clazz);
    }
}
