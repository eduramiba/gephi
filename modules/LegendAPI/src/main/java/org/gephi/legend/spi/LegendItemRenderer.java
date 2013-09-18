/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.gephi.legend.spi;

import java.util.ArrayList;
import org.gephi.legend.api.LegendController;
import org.gephi.legend.api.LegendModel;
import org.gephi.legend.mouse.LegendMouseListener;
import org.gephi.preview.api.Item;
import org.gephi.preview.api.PreviewModel;
import org.gephi.preview.api.PreviewProperties;
import org.gephi.preview.api.PreviewProperty;
import org.gephi.preview.api.RenderTarget;
import org.gephi.preview.spi.ItemBuilder;
import org.gephi.preview.spi.MouseResponsiveRenderer;
import org.gephi.preview.spi.PreviewMouseListener;
import org.gephi.preview.spi.Renderer;
import org.openide.util.lookup.ServiceProvider;
import org.openide.util.lookup.ServiceProviders;

/**
 * @author mvvijesh, edubecks
 */
@ServiceProviders(value = {
    @ServiceProvider(service = Renderer.class, position = 600),
    @ServiceProvider(service = LegendItemRenderer.class)
})
public class LegendItemRenderer implements Renderer, MouseResponsiveRenderer {

    Integer nextRenderIndex = 0;

    @Override
    public String getDisplayName() {
        return "Legend Item Renderer";
    }

    @Override
    public void preProcess(PreviewModel previewModel) {
    }

    @Override
    public void render(Item item, RenderTarget target, PreviewProperties properties) {
        LegendController legendController = LegendController.getInstance();
        LegendModel legendModel = legendController.getLegendModel();
        ArrayList<Item> legendItems = legendModel.getActiveItems();
        Item legendItem = legendItems.get(nextRenderIndex);
        Renderer renderer = legendItem.getData(LegendItem.RENDERER);
        renderer.render(legendItem, target, properties);

        nextRenderIndex += 1;
        if (nextRenderIndex == legendItems.size()) {
            nextRenderIndex = 0;
        }
    }

    @Override
    public PreviewProperty[] getProperties() {
        return new PreviewProperty[0];
    }

    @Override
    public boolean isRendererForitem(Item item, PreviewProperties properties) {
        Renderer itemRenderer = item.getData(LegendItem.RENDERER);
        if (itemRenderer == null) {
            return false;
        }

        LegendController legendController = LegendController.getInstance();
        LegendModel legendModel = legendController.getLegendModel();
        ArrayList<Renderer> renderers = legendModel.getRenderers();
        for (Renderer r : renderers) {
            if (itemRenderer.equals(r)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean needsPreviewMouseListener(PreviewMouseListener previewMouseListener) {
        return previewMouseListener instanceof LegendMouseListener;
    }

    @Override
    public boolean needsItemBuilder(ItemBuilder itemBuilder, PreviewProperties properties) {
        return itemBuilder instanceof LegendItemBuilder;
    }
}
