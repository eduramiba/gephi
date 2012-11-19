/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.gephi.legend.plugin.properties;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import org.gephi.legend.api.LegendModel;

/**
 *
 * @author eduBecKs
 */
public class TableProperty {
    //TEXT 

    public static final int TABLE_FONT = 0;
    public static final int TABLE_FONT_COLOR = 1;
    public static final int TABLE_IS_CELL_COLORING = 2;
//    public static final int TABLE_CELL_COLORING_DIRECTION = 3;
    public static final int TABLE_ROW_TEXT_POSITION = 3;
    public static final int TABLE_ROW_TEXT_ALIGNMENT = 4;
    public static final int TABLE_ROW_EXTRA_MARGIN = 5;
    public static final int TABLE_COLUMN_TEXT_POSITION = 6;
    public static final int TABLE_COLUMN_TEXT_ALIGNMENT = 7;
    public static final int TABLE_COLUMN_TEXT_ROTATION = 8;
    public static final int TABLE_COLUMN_EXTRA_MARGIN = 9;
    public static final int TABLE_IS_DISPLAYING_GRID = 10;
    public static final int TABLE_GRID_COLOR = 11;
    public static final int TABLE_LABEL = 12;
    public static String[] OWN_PROPERTIES = {
        ".table.font",
        ".table.font.color",
        ".table.isCellColoring",
//        ".table.cellColoringDirection",
        ".table.horizontalText.position",
        ".table.horizontalText.alignment",
        ".table.horizontal.extraMargin",
        ".table.verticalText.position",
        ".table.verticalText.alignment",
        ".table.verticalText.rotation",
        ".table.vertical.extraMargin",
        ".table.isDisplayingGrid",
        ".table.gridColor",
        ".table.label"
    };
    public static final int[] LIST_OF_PROPERTIES = {
        TABLE_FONT,
        TABLE_FONT_COLOR,
        TABLE_IS_CELL_COLORING,
//        TABLE_CELL_COLORING_DIRECTION,
        TABLE_ROW_TEXT_POSITION,
        TABLE_ROW_TEXT_ALIGNMENT,
        TABLE_ROW_EXTRA_MARGIN,
        TABLE_COLUMN_TEXT_POSITION,
        TABLE_COLUMN_TEXT_ALIGNMENT,
        TABLE_COLUMN_TEXT_ROTATION,
        TABLE_COLUMN_EXTRA_MARGIN,
        TABLE_IS_DISPLAYING_GRID,
        TABLE_GRID_COLOR,
        TABLE_LABEL
    };

    public static String getLabelProperty(Integer itemIndex, int i) {
        ArrayList<String> tableProperties = LegendModel.getProperties(TableProperty.OWN_PROPERTIES, itemIndex);
        return tableProperties.get(TableProperty.TABLE_LABEL) + i;
    }

    private static TableProperty instance = new TableProperty();
    private Map<String, Integer> propertyIndex;

    public int getProperty(String propertyName) {
        return propertyIndex.get(propertyName);
    }

    private TableProperty() {
        propertyIndex = new HashMap<String, Integer>();
        for (int i = 0; i < OWN_PROPERTIES.length; i++) {
            propertyIndex.put(OWN_PROPERTIES[i], i);
        }
    }

    public static TableProperty getInstance() {
        return instance;
    }

}
