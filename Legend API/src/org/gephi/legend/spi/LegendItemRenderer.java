/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.gephi.legend.spi;

import org.gephi.preview.api.Item;
import org.gephi.preview.spi.Renderer;

/**
 * Interface that all Legend renderers must implement.
 * When writing a legend renderer, you should normally extend AbstractLegendItemRenderer, which implements many common features for all renderers.
 * @see AbstractLegendItemRenderer
 * @author edubecks
 */
public interface LegendItemRenderer extends Renderer {
    
    /**
     * Indicates if it is a legend item renderer for the given item.
     * @param item Legend item
     * @return True if it is a renderer for the item, false otherwise
     */
    public boolean isAnAvailableRenderer(Item item);
}
