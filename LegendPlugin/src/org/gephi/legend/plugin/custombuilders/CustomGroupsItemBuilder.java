/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.gephi.legend.plugin.custombuilders;

import java.awt.Color;
import java.util.ArrayList;
import org.gephi.legend.spi.CustomLegendItemBuilder;

/**
 *
 * @author edubecks
 */
public interface CustomGroupsItemBuilder extends CustomLegendItemBuilder {

    /**
     * This function receives the objects that need to be filled in order to
     * build the item
     * @param labels
     * @param colors
     * @param values 
     */
    public void retrieveData(ArrayList<String> labels, ArrayList<Color> colors, ArrayList<Float> values);

}
