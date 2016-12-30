package org.gephi.io.importer.plugin.file.spreadsheet.sheets.csv;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.gephi.io.importer.plugin.file.spreadsheet.sheets.SheetParser;
import org.gephi.io.importer.plugin.file.spreadsheet.sheets.SheetRow;

/**
 *
 * @author Eduardo Ramos
 */
public class CSVSheetParser implements SheetParser {

    private final CSVParser parser;

    public CSVSheetParser(CSVParser parser) {
        this.parser = parser;
    }
    
    @Override
    public Map<String, Integer> getHeaderMap() {
        return parser.getHeaderMap();
    }

    @Override
    public long getCurrentRecordNumber() {
        return parser.getRecordNumber();
    }

    @Override
    public Iterator<SheetRow> iterator() {
        return new CSVIterator();
    }

    @Override
    public void close() throws IOException {
        parser.close();
    }
    
    private class CSVIterator implements Iterator<SheetRow> {

        private final Iterator<CSVRecord> iterator;

        public CSVIterator() {
            iterator = parser.iterator();
        }

        @Override
        public boolean hasNext() {
            return iterator.hasNext();
        }

        @Override
        public SheetRow next() {
            return new CSVSheetRow(iterator.next());
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }
}
