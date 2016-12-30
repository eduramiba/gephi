package org.gephi.io.importer.plugin.file.spreadsheet.sheets;

/**
 *
 * @author Eduardo Ramos
 */
public interface SheetRow {
    boolean isConsistent();
    
    String get(int index);
}
