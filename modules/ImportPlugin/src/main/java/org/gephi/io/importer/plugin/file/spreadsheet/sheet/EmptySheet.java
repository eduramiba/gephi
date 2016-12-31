package org.gephi.io.importer.plugin.file.spreadsheet.sheet;

import java.io.IOException;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;

/**
 *
 * @author Eduardo Ramos
 */
public final class EmptySheet implements SheetParser {
    
    public static final EmptySheet INSTANCE = new EmptySheet();

    @Override
    public Map<String, Integer> getHeaderMap() {
        return Collections.emptyMap();
    }

    @Override
    public long getCurrentRecordNumber() {
        return 0;
    }

    @Override
    public void close() throws IOException {
        //NOOP
    }

    @Override
    public Iterator<SheetRow> iterator() {
        return Collections.emptyIterator();
    }
}
