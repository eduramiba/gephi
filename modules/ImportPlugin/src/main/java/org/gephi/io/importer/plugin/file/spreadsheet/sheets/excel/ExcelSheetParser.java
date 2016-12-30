package org.gephi.io.importer.plugin.file.spreadsheet.sheets.excel;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.gephi.io.importer.plugin.file.spreadsheet.sheets.SheetParser;
import org.gephi.io.importer.plugin.file.spreadsheet.sheets.SheetRow;

/**
 *
 * @author Eduardo Ramos
 */
public class ExcelSheetParser implements SheetParser {

    private final XSSFSheet parser;
    private final Map<String, Integer> headerMap = new HashMap<>();
    private ExcelIterator iterator;

    public ExcelSheetParser(XSSFSheet parser) {
        this.parser = parser;
        initHeaderInfo();
    }

    private void initHeaderInfo() {
        XSSFRow firstRow = parser.getRow(parser.getFirstRowNum());

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
        parser.getWorkbook().close();
    }

    private class ExcelIterator implements Iterator<SheetRow> {

        private final Iterator<Row> iterator;
        private Row currentRow = null;

        public ExcelIterator() {
            iterator = parser.iterator();
        }

        @Override
        public boolean hasNext() {
            return iterator.hasNext();
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
