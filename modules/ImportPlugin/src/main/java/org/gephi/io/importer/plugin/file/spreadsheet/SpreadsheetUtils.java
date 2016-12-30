package org.gephi.io.importer.plugin.file.spreadsheet;

import java.io.IOException;
import java.io.Reader;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.gephi.io.importer.api.Issue;
import org.gephi.io.importer.api.Report;
import org.gephi.io.importer.plugin.file.spreadsheet.sheets.SheetParser;

/**
 *
 * @author Eduardo Ramos
 */
public class SpreadsheetUtils {

    public static void logInfo(Report report, String message, SheetParser parser) {
        report.logIssue(new Issue(message, Issue.Level.INFO));
    }

    public static void logWarning(Report report, String message, SheetParser parser) {
        report.logIssue(new Issue(message, Issue.Level.WARNING));
    }

    public static void logError(Report report, String message, SheetParser parser) {
        report.logIssue(new Issue(message, Issue.Level.SEVERE));
    }

    public static void logCritical(Report report, String message, SheetParser parser) {
        report.logIssue(new Issue(message, Issue.Level.CRITICAL));
    }

    public static void logIssue(Report report, Issue issue, SheetParser parser) {
        if (parser != null) {
            String newMessage = "[Record #" + parser.getCurrentRecordNumber()+ "] " + issue.getMessage();
            issue = new Issue(newMessage, issue.getLevel());
        }

        report.logIssue(issue);
    }

    public static CSVParser configureCSVParser(Reader reader, Character fieldSeparator) throws IOException {
        if (fieldSeparator == null) {
            fieldSeparator = ',';
        }

        CSVFormat csvFormat = CSVFormat.DEFAULT
                .withDelimiter(fieldSeparator)
                .withFirstRecordAsHeader()
                .withIgnoreEmptyLines(true)
                .withCommentMarker('#')
                .withNullString("")
                .withIgnoreSurroundingSpaces(true)
                .withTrim(true);

        return new CSVParser(reader, csvFormat);
    }
}
