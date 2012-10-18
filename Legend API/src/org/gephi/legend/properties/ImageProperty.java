/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.gephi.legend.properties;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author edubecks
 */
public class ImageProperty {

    public static final int IMAGE_URL = 0;
    public static final int LOCK_ASPECT_RATIO = 1;
    public static final String[] OWN_PROPERTIES = {
        ".imageUrl",
    };
    public static final int[] LIST_OF_PROPERTIES = {
        IMAGE_URL,
    };
    private static ImageProperty instance = new ImageProperty();
    private Map<String, Integer> propertyIndex;

    public int getProperty(String propertyName) {
        return propertyIndex.get(propertyName);
    }

    private ImageProperty() {
        propertyIndex = new HashMap<String, Integer>();
        for (int i = 0; i < OWN_PROPERTIES.length; i++) {
            propertyIndex.put(OWN_PROPERTIES[i], i);
        }
    }

    public static ImageProperty getInstance() {
        return instance;
    }

}
