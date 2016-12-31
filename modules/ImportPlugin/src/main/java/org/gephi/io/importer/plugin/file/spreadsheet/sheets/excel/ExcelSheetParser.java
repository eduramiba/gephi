package org.gephi.io.importer.plugin.file.spreadsheet.sheets.excel;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.gephi.io.importer.plugin.file.spreadsheet.sheet.SheetParser;
import org.gephi.io.importer.plugin.file.spreadsheet.sheet.SheetRow;

/**
 *
 * @author Eduardo Ramos
 */
public class ExcelSheetParser implements SheetParser {

    private final Sheet sheet;
    private final Map<String, Integer> headerMap = new HashMap<>();
    private ExcelIterator iterator;

    public ExcelSheetParser(Sheet sheet) {
        this.sheet = sheet;
        initHeaderInfo();
    }

    private void initHeaderInfo() {
        Row firstRow = sheet.getRow(sheet.getFirstRowNum());

        if (firstRow != null) {
            for (Cell cell : firstRow) {
                int index = cell.getColumnIndex();
                headerMap.put(ExcelSheetRow.getRowCellAsString(cell, index), index);
            }
        }
    }

    @Override
    public Map<String, Integer> getHeaderMap() {
        return new HashMap<>(headerMap);
    }

    @Override
    public long getCurrentRecordNumber() {
        return iterator != null ? iterator.getRowNum() : 0;
    }

    @Override
    public Iterator<SheetRow> iterator() {
        return iterator = new ExcelIterator();
    }

    @Override
    public void close() throws IOException {
        sheet.getWorkbook().close();
    }

    private class ExcelIterator implements Iterator<SheetRow> {

        private final Iterator<Row> iterator;
        private Row currentRow = null;

        public ExcelIterator() {
            iterator = sheet.iterator();
            if (iterator.hasNext()) {
                iterator.next();//Skip headers row
            }
        }

        @Override
        public boolean hasNext() {
            try {
                return iterator.hasNext();
            } catch (Exception e) {
                Logger.getLogger("").severe(e.getMessage());
                return false;//In case of malformed excel
            }
        }

        @Override
        public SheetRow next() {
            currentRow = iterator.next();
            return new ExcelSheetRow(currentRow);
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }

        private long getRowNum() {
            if (currentRow != null) {
                return currentRow.getRowNum();
            } else {
                return 0;
            }
        }
    }
}
