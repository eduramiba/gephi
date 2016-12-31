package org.gephi.io.importer.plugin.file.spreadsheet.process;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Eduardo Ramos
 */
public class SpreadsheetGeneralConfiguration {

    private final Map<String, Class<?>> columnsClasses;

    public SpreadsheetGeneralConfiguration(Map<String, Class<?>> columnsClasses) {
        if (columnsClasses == null) {
            columnsClasses = new HashMap<>();
        }
        this.columnsClasses = columnsClasses;
    }

    public Map<String, Class<?>> getColumnsClasses() {
        return columnsClasses;
    }
}
