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
 * @author edubecks
 */
public class GroupsProperty {

    public static final int GROUPS_NUMBER_COLUMNS = 0;
    public static final int GROUPS_SHAPE = 1;
    public static final int GROUPS_SCALE_SHAPE = 2;
    public static final int GROUPS_LABEL_POSITION = 3;
    public static final int GROUPS_LABEL_FONT = 4;
    public static final int GROUPS_LABEL_FONT_COLOR = 5;
    public static final int GROUPS_PADDING_BETWEEN_TEXT_AND_SHAPE = 6;
    public static final int GROUPS_PADDING_BETWEEN_ELEMENTS = 7;
    public static final int GROUPS_LABEL = 8;
    public static final String[] OWN_PROPERTIES = {
        ".numberColumns",
        ".shape",
        ".scaleShape",
        ".label.position",
        ".label.font",
        ".label.font.color",
        ".paddingBetweenTextAndShape",
        ".paddingBetweenElements",
        ".label.item"
    };
    
    
    public static final int[] LIST_OF_PROPERTIES = {
        GROUPS_NUMBER_COLUMNS,
        GROUPS_SHAPE,
        GROUPS_SCALE_SHAPE,
        GROUPS_LABEL_POSITION,
        GROUPS_LABEL_FONT,
        GROUPS_LABEL_FONT_COLOR,
        GROUPS_PADDING_BETWEEN_TEXT_AND_SHAPE,
        GROUPS_PADDING_BETWEEN_ELEMENTS
    };

    public static String getLabelProperty(Integer itemIndex, int i) {
        ArrayList<String> properties = LegendModel.getProperties(OWN_PROPERTIES, itemIndex);
        return properties.get(GROUPS_LABEL) + i;
    }
    
    private static GroupsProperty instance = new GroupsProperty();
    private Map<String, Integer> propertyIndex;

    public int getProperty(String propertyName) {
        return propertyIndex.get(propertyName);
    }

    private GroupsProperty() {
        propertyIndex = new HashMap <String, Integer>();
        for (int i = 0; i < OWN_PROPERTIES.length; i++) {
            propertyIndex.put(OWN_PROPERTIES[i], i);
        }
    }

    public static GroupsProperty getInstance() {
        return instance;
    }

}
