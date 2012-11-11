/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.gephi.legend.plugin.items;

import org.gephi.legend.api.LegendProperty;
import org.gephi.legend.spi.LegendItem;
import org.gephi.preview.api.PreviewProperty;
import org.openide.util.NbBundle;

/**
 *
 * @author edubecks
 */
public class TableItem extends AbstractItem implements LegendItem {

    public static final String LEGEND_TYPE = "Table Legend";
    //data
    public static final String TABLE_VALUES = "table values";
    public static final String HORIZONTAL_LABELS = "horizontal labels";
    public static final String VERTICAL_LABELS = "vertical labels";
    public static final String LABELS_IDS = "labels ids";
    public static final String LABELS_SELECTION = "labels selection";
    public static final String NUMBER_OF_LABELS = "number of labels";
    public static final String NUMBER_OF_ROWS = "number of rows";
    public static final String NUMBER_OF_COLUMNS = "number of columns";
    // colors
    public static final String COLOR_VALUES = "color values";
    public static final String COLOR_VERTICAL = "color vertical";
    public static final String COLOR_HORIZONTAL = "color horizontal";

    public enum LabelSelection {

        HORIZONTAL("horizontal"),
        VERTICAL("vertical"),
        BOTH("both");
        private final String value;

        private LabelSelection(String value) {
            this.value = value;
        }

    }

    public enum VerticalPosition {

        UP(0),
        BOTTOM(1);
        private final Integer value;
        private final String[] labels = {
            NbBundle.getMessage(TableItem.class, "TableItem.position.up"),
            NbBundle.getMessage(TableItem.class, "TableItem.position.bottom")
        };

        private VerticalPosition(Integer value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return labels[value];
        }

        public String getValue() {
            return value.toString();
        }

    }

    public enum HorizontalPosition {

        RIGHT(0),
        LEFT(1);
        private final Integer value;
        private final String[] labels = {
            NbBundle.getMessage(TableItem.class, "TableItem.position.right"),
            NbBundle.getMessage(TableItem.class, "TableItem.position.left")
        };

        private HorizontalPosition(Integer value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return labels[value];
        }

        public String getValue() {
            return value.toString();
        }

    }

    public enum VerticalTextDirection {

        // anti clockwise
        VERTICAL(0, -90d),
        HORIZONTAL(1, 0),
        DIAGONAL(2, -45d);
        private final double rotationAngle;
        private final Integer value;
        private final String[] labels = {
            NbBundle.getMessage(TableItem.class, "TableItem.rotation.vertical"),
            NbBundle.getMessage(TableItem.class, "TableItem.rotation.horizontal"),
            NbBundle.getMessage(TableItem.class, "TableItem.rotation.diagonal")
        };

        private VerticalTextDirection(Integer value, double rotationAngle) {
            this.rotationAngle = rotationAngle;
            this.value = value;
        }

        @Override
        public String toString() {
            return labels[value];
        }

        public String getValue() {
            return value.toString();
        }

        public double rotationAngle() {
            return Math.toRadians(rotationAngle);
        }

    }

    public TableItem(Object source) {
        super(source, LEGEND_TYPE);
    }

    @Override
    public String toString() {
        return (((PreviewProperty[]) this.getData(LegendItem.PROPERTIES))[LegendProperty.LABEL].getValue());
    }
}
