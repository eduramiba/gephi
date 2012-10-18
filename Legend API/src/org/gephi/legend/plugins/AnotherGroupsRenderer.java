/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.gephi.legend.plugins;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;
import org.gephi.legend.api.renderers.GroupsItemRenderer;
import org.gephi.legend.api.renderers.LegendItemRenderer;
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
@ServiceProvider(service = Renderer.class, position = 601)
public class AnotherGroupsRenderer extends GroupsItemRenderer {

    @Override
    public String getDisplayName() {
        return NbBundle.getMessage(AnotherGroupsRenderer.class, "AnotherGroupsRenderer.displayName");
    }
    
    @Override
    public boolean isRendererForitem(Item item, PreviewProperties properties) {
        LegendItemRenderer renderer = item.getData(LegendItem.RENDERER);
        return (item instanceof GroupsItem && renderer.getClass().equals(AnotherGroupsRenderer.class));
    }

    @Override
    public void renderToGraphics(Graphics2D graphics2D, AffineTransform origin, Integer width, Integer height) {

        // setting the origin of the legend
        graphics2D.setTransform(origin);


        // sorting elements
        TreeMap<String, Integer> sorting = new TreeMap<String, Integer>();
        for (int i = 0; i < values.size(); i++) {
            sorting.put(values.get(i) + " " + labels.get(i), i);
        }
        ArrayList<Float> sortedValues = new ArrayList<Float>();
        ArrayList<Color> sortedColors = new ArrayList<Color>();
        ArrayList<String> sortedLabels = new ArrayList<String>();

        for (Map.Entry<String, Integer> entry : sorting.entrySet()) {
            Integer index = (Integer) entry.getValue();
            sortedValues.add(values.get(index));
            sortedColors.add(colors.get(index));
            sortedLabels.add(labels.get(index).toString());
        }




        // computing element sizes
        int elementHeight = height;
        int elementWidth = width / sortedValues.size() - paddingBetweenElements;


        // computing label sizes
        float labelHeight = Integer.MIN_VALUE;
        for (int i = 0; i < sortedLabels.size(); i++) {
            float singleLabelHeight = computeVerticalTextSpaceUsed(graphics2D, sortedLabels.get(i).toString(), labelFont, width);
            labelHeight = Math.max(singleLabelHeight, labelHeight);
        }
        int labelWidth = elementWidth;

        // compute shape width and height
        int shapeHeight = (int) (elementHeight - labelHeight);
        int shapeWitdh = elementWidth;



        // computing size for each shape
        int shapeSize = Math.min(shapeHeight, shapeWitdh);
        ArrayList<Integer> shapeSizes = new ArrayList<Integer>();
        for (Float value : sortedValues) {
            shapeSizes.add((int) (value * shapeSize));
        }

        // drawing elements
        for (int i = 0; i < sortedLabels.size(); i++) {
            
            // computing shape positions
            int x = (elementWidth + paddingBetweenElements) * i;
            int y = 0;
            // centering elements
            x += (elementWidth - shapeSizes.get(i)) / 2;
            y += (shapeHeight - shapeSizes.get(i)) / 2;

            // drawing border
            int borderSize = 2;
            Color borderColor = Color.BLACK;

            // drawing line
            int centerX = x + shapeSizes.get(i) / 2;
            int centerY = y + shapeSizes.get(i);
            graphics2D.setColor(borderColor);
            graphics2D.fillRect(centerX, centerY, borderSize, shapeHeight - centerY);

            // drawing element
            graphics2D.setColor(sortedColors.get(i));
            graphics2D.fillOval(x, y, shapeSizes.get(i), shapeSizes.get(i));
            
            // drawing border
            graphics2D.setColor(borderColor);
            for (int j = 0; j < borderSize; j++) {
                graphics2D.drawOval(x + j,
                                    y + j,
                                    shapeSizes.get(i) - 2 * j,
                                    shapeSizes.get(i) - 2 * j);
            }

            // drawing label
            double labelX = (elementWidth + paddingBetweenElements) * i;
            double labelY = shapeHeight;
            legendDrawText(graphics2D,
                           sortedLabels.get(i),
                           labelFont,
                           sortedColors.get(i),
                           labelX,
                           labelY,
                           labelWidth,
                           (int) labelHeight,
                           LegendItem.Alignment.CENTER);


            

        }
    }

}
