package org.gephi.io.importer.plugin.file.spreadsheet.sheets;

import java.io.Closeable;
import java.util.Map;

/**
 *
 * @author Eduardo Ramos
 */
public interface SheetParser extends Closeable, Iterable<SheetRow> {
    Map<String, Integer> getHeaderMap();
    
    long getCurrentRecordNumber();
}
