package org.gephi.io.importer.plugin.file.spreadsheet.sheet;

/**
 *
 * @author Eduardo Ramos
 */
public interface SheetRow {
    boolean isConsistent();
    
    int size();
    
    String get(int index);
}
