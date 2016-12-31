package org.gephi.io.importer.plugin.file.spreadsheet.sheets.excel;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.gephi.io.importer.plugin.file.spreadsheet.sheet.SheetRow;

/**
 *
 * @author Eduardo Ramos
 */
public class ExcelSheetRow implements SheetRow {

    private final Row row;

    public ExcelSheetRow(Row row) {
        this.row = row;
    }

    @Override
    public boolean isConsistent() {
        return true;
    }

    @Override
    public String get(int index) {
        Cell cell = row.getCell(index, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);

        String value = getRowCellAsString(cell, index);
        if (value != null) {
            value = value.trim();

            if (value.isEmpty()) {
                value = null;
            }
        }

        return value;
    }

    @Override
    public int size() {
        return row.getLastCellNum();
    }

    public static String getRowCellAsString(Cell cell, int index) {
        if (cell == null) {
            return null;
        }

        switch (cell.getCellTypeEnum()) {
            case NUMERIC:
                return String.valueOf(cell.getNumericCellValue());
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            default:
                return cell.getStringCellValue();
        }
    }
}
