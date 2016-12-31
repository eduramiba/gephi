package org.gephi.io.importer.plugin.file.spreadsheet.sheet;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 *
 * @author Eduardo Ramos
 */
public final class ErrorSheet implements SheetParser {

    private static final Map<String, Integer> ERROR_HEADER = new HashMap<String, Integer>();

    static {
        ERROR_HEADER.put("error", 0);
    }
    private final SheetRow errorRow;

    public ErrorSheet(final String error) {
        this.errorRow = new SheetRow() {
            @Override
            public boolean isConsistent() {
                return true;
            }

            @Override
            public int size() {
                return 1;
            }

            @Override
            public String get(int index) {
                return error;
            }
        };
    }

    @Override
    public Map<String, Integer> getHeaderMap() {
        return ERROR_HEADER;
    }

    @Override
    public long getCurrentRecordNumber() {
        return 1;
    }

    @Override
    public void close() throws IOException {
        //NOOP
    }

    @Override
    public Iterator<SheetRow> iterator() {
        return Arrays.asList(errorRow).iterator();
    }
}
