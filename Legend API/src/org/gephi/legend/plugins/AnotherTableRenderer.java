/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.gephi.legend.plugins;

import java.awt.Color;
import java.awt.Graphics2D;
import org.gephi.legend.api.renderers.LegendItemRenderer;
import org.gephi.legend.api.renderers.TableItemRenderer;
import org.gephi.legend.items.GroupsItem;
import org.gephi.legend.items.LegendItem;
import org.gephi.preview.api.Item;
import org.gephi.preview.api.PreviewProperties;
import org.gephi.preview.spi.Renderer;
import org.openide.util.NbBundle;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author edubecks
 */
@ServiceProvider(service = Renderer.class, position = 602)
public class AnotherTableRenderer extends TableItemRenderer {

    @Override
    public String getDisplayName() {
        return NbBundle.getMessage(AnotherTableRenderer.class, "AnotherTableRenderer.displayName");
    }
    
    @Override
    public boolean isRendererForitem(Item item, PreviewProperties properties) {
        LegendItemRenderer renderer = item.getData(LegendItem.RENDERER);
        return (item instanceof GroupsItem && renderer.getClass().equals(AnotherTableRenderer.class));
    }

    @Override
    protected void drawCellColoring(Graphics2D graphics, Integer x, Integer y, Integer width, Integer height, String value, Float valueNormalized, Color color) {
        int shapeWidth = (int) (width * valueNormalized);
        int shapeHeight = (int) (height * valueNormalized);
        // centering element
        int xShape = x + (width - shapeWidth) / 2;
        int yShape = y + (height - shapeHeight) / 2;

        graphics.setColor(color);
        graphics.fillOval(xShape, yShape, shapeWidth, shapeHeight);

        drawCellText(graphics, x, y, width, height, value, font, fontColor);


    }

}
