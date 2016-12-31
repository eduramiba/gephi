package org.gephi.io.importer.plugin.file.spreadsheet;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.gephi.io.importer.api.Issue;
import org.gephi.io.importer.api.Report;
import org.gephi.io.importer.plugin.file.spreadsheet.sheet.SheetParser;

/**
 *
 * @author Eduardo Ramos
 */
public class SpreadsheetUtils {

    public static void logInfo(Report report, String message, SheetParser parser) {
        logIssue(report, new Issue(message, Issue.Level.INFO), parser);
    }

    public static void logWarning(Report report, String message, SheetParser parser) {
        logIssue(report, new Issue(message, Issue.Level.WARNING), parser);
    }

    public static void logError(Report report, String message, SheetParser parser) {
        logIssue(report, new Issue(message, Issue.Level.SEVERE), parser);
    }

    public static void logCritical(Report report, String message, SheetParser parser) {
        logIssue(report, new Issue(message, Issue.Level.CRITICAL), parser);
    }

    public static void logIssue(Report report, Issue issue, SheetParser parser) {
        if (parser != null) {
            String newMessage = "[Record #" + parser.getCurrentRecordNumber() + "] " + issue.getMessage();
            issue = new Issue(newMessage, issue.getLevel());
        }

        if (report != null) {
            report.logIssue(issue);
        } else {
            Level level;
            switch (issue.getLevel()) {
                case INFO:
                    level = Level.INFO;
                    break;
                case WARNING:
                    level = Level.WARNING;
                    break;
                case SEVERE:
                case CRITICAL:
                    level = Level.SEVERE;
                    break;
                default:
                    level = Level.FINE;
            }
            Logger.getLogger("").log(level, issue.getMessage());
        }
    }

    public static CSVParser configureCSVParser(File file, Character fieldSeparator, Charset charset) throws IOException {
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

        FileInputStream fileInputStream = new FileInputStream(file);
        InputStreamReader is = new InputStreamReader(fileInputStream, charset);
        return new CSVParser(is, csvFormat);
    }
}
