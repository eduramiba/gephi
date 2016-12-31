package org.gephi.ui.utils;

import org.gephi.graph.api.TimeRepresentation;
import org.openide.util.NbBundle;

/**
 *
 * @author Eduardo Ramos
 */
public class TimeRepresentationWrapper {

    private final TimeRepresentation timeRepresentation;

    public TimeRepresentationWrapper(TimeRepresentation timeRepresentation) {
        this.timeRepresentation = timeRepresentation;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 41 * hash + (this.timeRepresentation != null ? this.timeRepresentation.hashCode() : 0);
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
        final TimeRepresentationWrapper other = (TimeRepresentationWrapper) obj;
        if (this.timeRepresentation != other.timeRepresentation) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return NbBundle.getMessage(TimeRepresentationWrapper.class, "TimeRepresentationWrapper.timeRepresentation." + timeRepresentation.name());
    }

    public TimeRepresentation getTimeRepresentation() {
        return timeRepresentation;
    }
    
}
