package org.gephi.io.importer.plugin.file.spreadsheet;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Eduardo Ramos
 */
public class SpreadsheetNodesConfiguration {

    private final Map<String, Class<?>> columnsClasses;
    private final boolean assignNewNodeIds;

    public SpreadsheetNodesConfiguration(Map<String, Class<?>> columnsClasses, boolean assignNewNodeIds) {
        if (columnsClasses == null) {
            columnsClasses = new HashMap<>();
        }

        this.columnsClasses = columnsClasses;
        this.assignNewNodeIds = assignNewNodeIds;
    }

    public Map<String, Class<?>> getColumnsClasses() {
        return new HashMap<>(columnsClasses);
    }

    public boolean isAssignNewNodeIds() {
        return assignNewNodeIds;
    }
}
