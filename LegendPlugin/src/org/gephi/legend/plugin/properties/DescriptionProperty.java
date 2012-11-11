/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.gephi.legend.plugin.properties;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author edubecks
 */
public class DescriptionProperty {

    public static final int DESCRIPTION_KEY_FONT = 0;
    public static final int DESCRIPTION_KEY_FONT_COLOR = 1;
    public static final int DESCRIPTION_KEY_FONT_ALIGNMENT = 2;
    public static final int DESCRIPTION_VALUE_FONT = 3;
    public static final int DESCRIPTION_VALUE_FONT_COLOR = 4;
    public static final int DESCRIPTION_VALUE_FONT_ALIGNMENT = 5;
    public static final int DESCRIPTION_IS_FLOW_LAYOUT = 6;
    public static final int DESCRIPTION_DATA = 7;
    public static final int DESCRIPTION_NUMBER_OF_ITEMS = 8;
    public static final int DESCRIPTION_KEY = 9;
    public static final int DESCRIPTION_VALUE = 10;
    public static final int DESCRIPTION_TEMP = 11;
    public static final String[] OWN_PROPERTIES = {
        ".key.font",
        ".key.font.color",
        ".key.font.alignment",
        ".value.font",
        ".value.font.color",
        ".value.font.alignment",
        ".isFlowLayout",
        ".data",
        ".numberOfItems",
        ".key",
        ".value",
        ".temp"
    };
    public static final int[] LIST_OF_PROPERTIES = {
        DESCRIPTION_KEY_FONT,
        DESCRIPTION_KEY_FONT_COLOR,
        DESCRIPTION_KEY_FONT_ALIGNMENT,
        DESCRIPTION_VALUE_FONT,
        DESCRIPTION_VALUE_FONT_COLOR,
        DESCRIPTION_VALUE_FONT_ALIGNMENT,
        DESCRIPTION_IS_FLOW_LAYOUT
    };
    private static DescriptionProperty instance = new DescriptionProperty();
    private Map<String, Integer> propertyIndex;

    public int getProperty(String propertyName) {
        return propertyIndex.get(propertyName);
    }

    private DescriptionProperty() {
        propertyIndex = new HashMap<String, Integer> ();
        for (int i = 0; i < OWN_PROPERTIES.length; i++) {
            propertyIndex.put(OWN_PROPERTIES[i], i);
        }
    }

    public static DescriptionProperty getInstance() {
        return instance;
    }

}
