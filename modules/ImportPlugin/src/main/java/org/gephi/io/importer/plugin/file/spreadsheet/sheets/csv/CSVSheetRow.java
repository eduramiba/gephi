package org.gephi.io.importer.plugin.file.spreadsheet.sheets.csv;

import org.apache.commons.csv.CSVRecord;
import org.gephi.io.importer.plugin.file.spreadsheet.sheet.SheetRow;

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
    public int size() {
        return record.size();
    }

    @Override
    public String get(int index) {
        return record.get(index);
    }
}
