package org.gephi.io.importer.plugin.file.spreadsheet.sheets.csv;

import org.apache.commons.csv.CSVRecord;
import org.gephi.io.importer.plugin.file.spreadsheet.sheets.SheetRow;

/**
 *
 * @author Eduardo Ramos
 */
public class CSVSheetRow implements SheetRow {
    
    private final CSVRecord record;

    public CSVSheetRow(CSVRecord record) {
        this.record = record;
    }

    @Override
    public boolean isConsistent() {
        return record.isConsistent();
    }

    @Override
    public String get(int index) {
        return record.get(index);
    }
}
