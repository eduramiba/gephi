/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.gephi.legend.plugin.custombuilders;

import org.gephi.legend.spi.CustomLegendItemBuilder;

/**
 *
 * @author edubecks
 */
public interface CustomImageItemBuilder extends CustomLegendItemBuilder  {
    
    /**
     * This function receives the objects that need to be filled in order to
     * build the item
     */
    public void retrieveData();
    
}
