/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.gephi.legend.plugin.builders.group;

import java.awt.Color;
import java.util.ArrayList;
import org.gephi.legend.plugin.custombuilders.CustomGroupsItemBuilder;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author edubecks
 */
@ServiceProvider(service=CustomGroupsItemBuilder.class, position=1)
public class Default implements CustomGroupsItemBuilder{

    @Override
    public String getDescription() {
        return DEFAULT_DESCRIPTION;
    }

    @Override
    public String getTitle() {
        return DEFAULT_TITLE;
    }

    @Override
    public boolean isAvailableToBuild() {
        return true;
    }

    @Override
    public String stepsNeededToBuild() {
        return NONE_NEEDED;
    }

    @Override
    public void retrieveData(ArrayList<String> labels, ArrayList<Color> colors, ArrayList<Float> values) {
//        StringBuilder group1 = new StringBuilder("group 1");
//        StringBuilder group2 = new StringBuilder("group 2");
        
        
        // FILLING LABELS
        String group1 = "group 1";
        String group2 = "group 2";
        String group3 = "group 3";
        String group4 = "group 4";
        labels.add(group1);
        labels.add(group2);
        labels.add(group3);
        labels.add(group4);
        
        // FILLING COLORS
        Color color1 = Color.RED;
        Color color2 = Color.BLUE;
        Color color3 = Color.YELLOW;
        Color color4 = Color.GREEN;
        colors.add(color1);
        colors.add(color2);
        colors.add(color3);
        colors.add(color4);
        
        // FILLING VALUES
        Float value1=0.8f;
        Float value2=0.4f;
        Float value3=0.33f;
        Float value4=0.99f;
        values.add(value1);
        values.add(value2);
        values.add(value3);
        values.add(value4);
    }
    
}
