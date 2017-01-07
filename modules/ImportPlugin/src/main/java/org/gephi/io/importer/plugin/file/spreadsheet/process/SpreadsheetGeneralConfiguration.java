package org.gephi.io.importer.plugin.file.spreadsheet.process;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.gephi.graph.api.TimeRepresentation;
import org.joda.time.DateTimeZone;

/**
 *
 * @author Eduardo Ramos
 */
public class SpreadsheetGeneralConfiguration {

    public enum Table {
        NODES(Arrays.asList(
                ImportNodesProcess.NODE_ID,
                ImportNodesProcess.NODE_LABEL
        )),
        EDGES(Arrays.asList(
                ImportEdgesProcess.EDGE_ID,
                ImportEdgesProcess.EDGE_KIND,
                ImportEdgesProcess.EDGE_LABEL,
                ImportEdgesProcess.EDGE_SOURCE,
                ImportEdgesProcess.EDGE_TARGET,
                ImportEdgesProcess.EDGE_TYPE,
                ImportEdgesProcess.EDGE_WEIGHT
        ));

        private final Set<String> specialColumnNames;

        private Table(List<String> specialColumnNames) {
            this.specialColumnNames = Collections.unmodifiableSet(new HashSet<>(specialColumnNames));
        }

        public Set<String> getSpecialColumnNames() {
            return specialColumnNames;
        }

        public boolean isSpecialColumn(String column) {
            return specialColumnNames.contains(column.trim().toLowerCase());
        }
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
