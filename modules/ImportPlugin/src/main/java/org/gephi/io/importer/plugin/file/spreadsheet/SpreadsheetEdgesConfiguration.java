package org.gephi.io.importer.plugin.file.spreadsheet;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Eduardo Ramos
 */
public class SpreadsheetEdgesConfiguration {

    private final Map<String, Class<?>> columnsClasses;
    private final boolean createMissingNodes;

    public SpreadsheetEdgesConfiguration(Map<String, Class<?>> columnsClasses, boolean createMissingNodes) {
        if (columnsClasses == null) {
            columnsClasses = new HashMap<>();
        }
        
        this.columnsClasses = columnsClasses;
        this.createMissingNodes = createMissingNodes;
    }

    public Map<String, Class<?>> getColumnsClasses() {
        return new HashMap<>(columnsClasses);
    }

    public boolean isCreateMissingNodes() {
        return createMissingNodes;
    }
}
