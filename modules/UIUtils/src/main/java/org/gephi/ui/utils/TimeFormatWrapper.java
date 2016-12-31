package org.gephi.ui.utils;

import org.gephi.graph.api.TimeFormat;
import org.openide.util.NbBundle;

/**
 *
 * @author Eduardo Ramos
 */
public class TimeFormatWrapper {

    private final TimeFormat timeFormat;

    public TimeFormatWrapper(TimeFormat timeFormat) {
        this.timeFormat = timeFormat;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 53 * hash + (this.timeFormat != null ? this.timeFormat.hashCode() : 0);
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
        final TimeFormatWrapper other = (TimeFormatWrapper) obj;
        if (this.timeFormat != other.timeFormat) {
            return false;
        }
        return true;
    }

    public TimeFormat getTimeFormat() {
        return timeFormat;
    }

    @Override
    public String toString() {
        return NbBundle.getMessage(TimeFormatWrapper.class, "TimeFormatWrapper.timeFormat." + timeFormat.name());
    }
}
