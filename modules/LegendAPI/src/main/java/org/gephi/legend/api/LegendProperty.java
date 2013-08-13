/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.gephi.legend.api;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author eduBecKs
 */
public class LegendProperty {

    // LABEL
    public static final int LABEL = 0;
    // DISPLAY
    public static final int IS_DISPLAYING = 1;
    // LOCATION
    public static final int USER_ORIGIN_X = 2;
    public static final int USER_ORIGIN_Y = 3;
    // DIMENSIONS
    public static final int WIDTH = 4;
    public static final int HEIGHT = 5;
    // BACKGROUND
    public static final int BACKGROUND_IS_DISPLAYING = 6;
    public static final int BACKGROUND_COLOR = 7;
    public static final int BORDER_IS_DISPLAYING = 8;
    public static final int BORDER_COLOR = 9;
    public static final int BORDER_LINE_THICK = 10;
    // TITLE
    public static final int TITLE_IS_DISPLAYING = 11;
    public static final int TITLE = 12;
    public static final int TITLE_FONT = 13;
    public static final int TITLE_FONT_COLOR = 14;
    public static final int TITLE_ALIGNMENT = 15;
    // DESCRIPTION
    public static final int DESCRIPTION_IS_DISPLAYING = 16;
    public static final int DESCRIPTION = 17;
    public static final int DESCRIPTION_FONT = 18;
    public static final int DESCRIPTION_FONT_COLOR = 19;
    public static final int DESCRIPTION_ALIGNMENT = 20;
    // PROPERTIES SET BY USER
    public static final int USER_LEGEND_NAME = 21;
    
    public static final String[] LEGEND_PROPERTIES = {
        // LABEL
        ".label",                               // 0 
        ".isDisplaying",                        // 1
        // LOCATION                              
        ".originX",                             // 2
        ".originY",                             // 3
        ".width",                               // 4
        ".height",                              // 5
        // BACKGROUND
        ".background.isDisplaying",             // 6
        ".background.color",                    // 7
        // BORDER
        ".border.isDisplaying",                 // 8
        ".border.color",                        // 9
        ".border.lineThick",                    // 10
        // TITLE
        ".title.isDisplaying",                  // 11
        ".title",                               // 12 
        ".title.font",                          // 13
        ".title.font.color",                    // 14
        ".title.alignment",                     // 15
        // DESCRIPTION
        ".description.isDisplaying",            // 16
        ".description",                         // 17
        ".description.font",                    // 18
        ".description.font.color",              // 19
        ".description.alignment",               // 20
        // PROPERTIES SET BY USER
        ".user.legend.name"                     // 21
    };
    
    public static final int[] LIST_OF_PROPERTIES = {
        LABEL,
        IS_DISPLAYING,
        USER_ORIGIN_X,
        USER_ORIGIN_Y,
        WIDTH,
        HEIGHT,
        BACKGROUND_IS_DISPLAYING,
        BACKGROUND_COLOR,
        BORDER_IS_DISPLAYING,
        BORDER_COLOR,
        BORDER_LINE_THICK,
        TITLE_IS_DISPLAYING,
        TITLE,
        TITLE_FONT,
        TITLE_FONT_COLOR,
        TITLE_ALIGNMENT,
        DESCRIPTION_IS_DISPLAYING,
        DESCRIPTION,
        DESCRIPTION_FONT,
        DESCRIPTION_FONT_COLOR,
        DESCRIPTION_ALIGNMENT,
        USER_LEGEND_NAME
    };
    
    private static LegendProperty instance = new LegendProperty();
    private Map<String,Integer> propertyIndex;
    
        public int getProperty(String propertyName){
        return propertyIndex.get(propertyName);
    }
 
    private LegendProperty() {
        propertyIndex= new HashMap<String, Integer>();
        for (int i = 0; i < LEGEND_PROPERTIES.length; i++) {
            propertyIndex.put(LEGEND_PROPERTIES[i], i);
        }        
    }
 
    public static LegendProperty getInstance() {
        return instance;
    }
}
