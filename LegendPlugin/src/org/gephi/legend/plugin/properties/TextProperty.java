/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.gephi.legend.plugin.properties;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author eduBecKs
 */
public class TextProperty {

    //TEXT 
    public static final int TEXT_BODY = 0;
    public static final int TEXT_BODY_FONT = 1;
    public static final int TEXT_BODY_FONT_COLOR = 2;
    public static final int TEXT_BODY_FONT_ALIGNMENT = 3;
    public static final String[] OWN_PROPERTIES = {
        ".text.body",
        ".text.body.font",
        ".text.body.font.color",
        ".text.body.alignment"
    };
    public static final int[] LIST_OF_PROPERTIES = {
        TEXT_BODY,
        TEXT_BODY_FONT,
        TEXT_BODY_FONT_COLOR,
        TEXT_BODY_FONT_ALIGNMENT
    };
    private static TextProperty instance = new TextProperty();
    private Map<String, Integer> propertyIndex;

    public int getProperty(String propertyName) {
        return propertyIndex.get(propertyName);
    }

    private TextProperty() {
        propertyIndex = new HashMap<String, Integer>();
        for (int i = 0; i < OWN_PROPERTIES.length; i++) {
            propertyIndex.put(OWN_PROPERTIES[i], i);
        }
    }

    public static TextProperty getInstance() {
        return instance;
    }

}
