package org.gephi.ui.utils;

import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author Eduardo Ramos
 */
public class TimeZoneWrapper {

    private final TimeZone timeZone;
    private final long currentTimestamp;

    public TimeZoneWrapper(TimeZone timeZone, long currentTimestamp) {
        this.timeZone = timeZone;
        this.currentTimestamp = currentTimestamp;
    }

    private String getTimeZoneText() {
        int offset = timeZone.getOffset(currentTimestamp);
        long hours = TimeUnit.MILLISECONDS.toHours(offset);
        long minutes = TimeUnit.MILLISECONDS.toMinutes(offset)
                - TimeUnit.HOURS.toMinutes(hours);
        minutes = Math.abs(minutes);

        if (hours >= 0) {
            return String.format("%s (GMT+%d:%02d)", timeZone.getID(), hours, minutes);
        } else {
            return String.format("%s (GMT%d:%02d)", timeZone.getID(), hours, minutes);
        }
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 97 * hash + (this.timeZone != null ? this.timeZone.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final TimeZoneWrapper other = (TimeZoneWrapper) obj;
        if (this.timeZone != other.timeZone && (this.timeZone == null || !this.timeZone.equals(other.timeZone))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return getTimeZoneText();
    }

    public TimeZone getTimeZone() {
        return timeZone;
    }
}
