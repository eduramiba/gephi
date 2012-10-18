/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.gephi.legend.plugins;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.List;
import org.apache.batik.svggen.SVGGraphics2D;
import org.gephi.legend.api.renderers.GroupsItemRenderer;
import org.gephi.legend.api.renderers.ImageItemRenderer;
import org.gephi.legend.api.renderers.LegendItemRenderer;
import org.gephi.legend.items.GroupsItem;
import org.gephi.legend.items.LegendItem;
import org.gephi.preview.api.Item;
import org.gephi.preview.api.PreviewProperties;
import org.gephi.preview.spi.Renderer;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PiePlot;
import org.jfree.data.general.DefaultPieDataset;
import org.openide.util.NbBundle;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author edubecks
 */
@ServiceProvider(service = Renderer.class, position = 603)
public class GroupsPieChartRenderer extends GroupsItemRenderer {

    @Override
    public String getDisplayName() {
        return NbBundle.getMessage(GroupsPieChartRenderer.class, "GroupsPieChartRenderer.displayName");
    }
    
    @Override
    public boolean isRendererForitem(Item item, PreviewProperties properties) {
        LegendItemRenderer renderer = item.getData(LegendItem.RENDERER);
        return (item instanceof GroupsItem && renderer.getClass().equals(GroupsPieChartRenderer.class));
    }

    @Override
    protected void renderToGraphics(Graphics2D graphics2D, AffineTransform origin, Integer width, Integer height) {
        
        // creating dataset for jreechart
        DefaultPieDataset dataset = new DefaultPieDataset();
        for (int i = 0; i < values.size(); i++) {
            dataset.setValue(labels.get(i).toString(), values.get(i));
        }

        JFreeChart chart = ChartFactory.createPieChart("", dataset, false, false, false);
        chart.setBackgroundPaint(TRANSPARENT_COLOR);
        chart.setBorderVisible(false);
        chart.setBackgroundImageAlpha(0f);

        // Specify the colors here 
        PiePlot plot = (PiePlot) chart.getPlot();
        plot.setBackgroundPaint(TRANSPARENT_COLOR);
        plot.setBackgroundAlpha(0f);
        plot.setOutlineVisible(false);
        plot.setLabelFont(labelFont);
        List<Comparable> keys = dataset.getKeys();
        for (int i = 0; i < keys.size(); i++) {
            plot.setSectionPaint(keys.get(i), colors.get(i));
        }

        BufferedImage jfreechartImage = chart.createBufferedImage(width, height);

        int x = (int) origin.getTranslateX();
        int y = (int) origin.getTranslateY();

        graphics2D.setTransform(origin);
        if (graphics2D instanceof SVGGraphics2D) {
            ImageItemRenderer.renderImageToSVGGraphics(
                    graphics2D,
                    jfreechartImage,
                    x,
                    y);
        }
        else {
            graphics2D.drawImage(jfreechartImage, 0, 0, null);
        }

    }
    
    private final Color TRANSPARENT_COLOR = new Color(255,255,255,0);

}
