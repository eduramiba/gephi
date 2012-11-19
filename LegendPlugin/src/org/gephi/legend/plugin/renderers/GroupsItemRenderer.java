/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.gephi.legend.plugin.renderers;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.Collections;
import org.gephi.legend.api.AbstractLegendItemRenderer;
import org.gephi.legend.api.LegendModel;
import org.gephi.legend.plugin.builders.GroupsItemBuilder;
import org.gephi.legend.plugin.items.GroupsItem;
import org.gephi.legend.plugin.properties.GroupsProperty;
import org.gephi.legend.spi.LegendItem;
import org.gephi.legend.spi.LegendItem.Direction;
import org.gephi.legend.spi.LegendItem.Shape;
import org.gephi.preview.api.Item;
import org.gephi.preview.api.PreviewProperties;
import org.gephi.preview.spi.ItemBuilder;
import org.gephi.preview.spi.Renderer;
import org.openide.util.NbBundle;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author edubecks
 */
@ServiceProvider(service = Renderer.class, position = 504)
public class GroupsItemRenderer extends AbstractLegendItemRenderer {
    
    @Override
    public boolean isAnAvailableRenderer(Item item) {
        return item instanceof GroupsItem;
    }

    @Override
    protected void renderToGraphics(Graphics2D graphics2D,
                                    AffineTransform origin,
                                    Integer width,
                                    Integer height) {

        if (labels.isEmpty()) {
            return;
        }

        graphics2D.setTransform(origin);

        int numGroups = labels.size();
        int numRows = (int) Math.ceil((double) numGroups / numColumns);

        // max label length
        graphics2D.setFont(labelFont);
        FontMetrics fontMetrics = graphics2D.getFontMetrics();


//        int elementHeight = (height - (numRows - 1) * paddingBetweenElements) / numRows;
//        int elementWidth = (width - (numColumns - 1) * paddingBetweenElements) / numColumns;
        int elementWidth = width / numColumns - paddingBetweenElements;
        int elementHeight = height / numRows - paddingBetweenElements;

        int maxLabelWidth = Integer.MIN_VALUE;
        int maxLabelHeight = Integer.MIN_VALUE;

        // computing max label height
        for (StringBuilder label : labels) {
            maxLabelHeight = (int) Math.max(maxLabelHeight, computeVerticalTextSpaceUsed(graphics2D, label.toString(), labelFont, elementWidth));

        }

        // computing max label width
        for (StringBuilder label : labels) {
            maxLabelWidth = Math.max(maxLabelWidth, fontMetrics.stringWidth(label.toString()));
        }




        int shapeWidth = 0, shapeHeight = 0, labelHeight = 0, labelWidth = 0;
        if (labelPosition == Direction.RIGHT || labelPosition == Direction.LEFT) {
            shapeWidth = elementWidth - maxLabelWidth - paddingBetweenTextAndShape;
            shapeHeight = elementHeight - paddingBetweenElements;
            labelWidth = maxLabelWidth;
            labelHeight = shapeHeight;
        }
        else if (labelPosition == Direction.UP || labelPosition == Direction.DOWN) {
            shapeWidth = elementWidth - paddingBetweenElements;
            shapeHeight = elementHeight - maxLabelHeight - paddingBetweenTextAndShape;
            labelWidth = shapeWidth;
            labelHeight = maxLabelHeight;
        }

//        // 
//        ArrayList<Float> valuesNormalized = new ArrayList<Float>();
//        if (isScalingShapes) {
//            float maxValue = Collections.max(values);
//            for (Float value : values) {
//                valuesNormalized.add(value / maxValue);
//            }
//        }
//        else {
//            for (Float value : values) {
//                valuesNormalized.add(1f);
//            }
//        }

        for (int i = 0; i < labels.size(); i++) {
            int x = (i % numColumns) * (elementWidth + paddingBetweenElements);
            int y = (i / numColumns) * (elementHeight + paddingBetweenElements);
            int xShape = 0, yShape = 0, xLabel = 0, yLabel = 0;
            switch (labelPosition) {
                case RIGHT: {
                    xShape = x;
                    yShape = y;
                    xLabel = x + shapeWidth + paddingBetweenTextAndShape;
                    yLabel = y;
                    break;
                }
                case LEFT: {
                    xShape = x + labelWidth + paddingBetweenTextAndShape;
                    yShape = y;
                    xLabel = x;
                    yLabel = y;
                    break;
                }
                case UP: {
                    xShape = x;
                    yShape = y + labelHeight + paddingBetweenTextAndShape;
                    xLabel = x;
                    yLabel = y;
                    break;
                }
                case DOWN: {
                    xShape = x;
                    yShape = y;
                    xLabel = x;
                    yLabel = y + shapeHeight;
                    break;
                }
            }

            drawShape(graphics2D, shape, colors.get(i), xShape, yShape, shapeWidth, shapeHeight, values.get(i));
            drawElementLabel(graphics2D, labels.get(i).toString(), labelFont, colors.get(i), xLabel, yLabel, labelWidth, labelHeight);
        }
    }

    /**
     * Override this function to draw each element label in a different way.
     *
     * @param graphics2D the Graphics object to draw to
     * @param label text to be displayed as label
     * @param labelFont font used to draw the label
     * @param labelColor rendering color for the label
     * @param x the x coordinate of the area containing the label
     * @param y the y coordinate of the area containing the label
     * @param width the width of the area containing the label
     * @param height the height of the area containing the label
     *
     */
    protected void drawElementLabel(Graphics2D graphics2D,
                                    String label,
                                    Font labelFont,
                                    Color labelColor,
                                    int x,
                                    int y,
                                    Integer width,
                                    Integer height) {
        
//        AffineTransform restore = graphics2D.getTransform();
//        AffineTransform arrange = new AffineTransform(graphics2D.getTransform());
//        arrange.translate(x, y);
//        graphics2D.setTransform(arrange);
        legendDrawText(graphics2D, label, labelFont, labelColor, x, y, width, height, LegendItem.Alignment.CENTER);
//        graphics2D.setTransform(restore);
    }

    /**
     * Override this function to draw each element shape or representative
     * element in a different way.
     *
     * @param graphics2D the Graphics object to draw to
     * @param shape shape specified by the user, it could be one the three
     * values:
     * @param value value to express as a shape
     * @param valueColor rendering color for the value
     * @param x the x coordinate of the area containing the label
     * @param y the y coordinate of the area containing the label
     * @param width the width of the area containing the label
     * @param height the height of the area containing the label
     */
    protected void drawElementShape(Graphics2D graphics2D,
                                    Shape shape,
                                    Float value,
                                    Color valueColor,
                                    int x,
                                    int y,
                                    Integer width,
                                    Integer height) {
        drawShape(graphics2D, shape, valueColor, x, y, width, height, value);
    }

    private void drawShape(Graphics2D graphics2D, Shape shape, Color color, int x, int y, Integer width, Integer height, float scale) {

        int shapeWidth = (int) (width * scale);
        int shapeHeight = (int) (height * scale);
        x = (x + (width - shapeWidth) / 2);
        y = (y + (height - shapeHeight) / 2);
        graphics2D.setColor(color);
        switch (shape) {
            case RECTANGLE: {
                graphics2D.fillRect(x, y, shapeWidth, shapeHeight);
                break;
            }
            case CIRCLE: {
                graphics2D.fillOval(x, y, shapeWidth, shapeHeight);
                break;
            }
            case TRIANGLE: {
                int[] xpoints = {x, x + shapeWidth, x + shapeWidth / 2};
                int[] ypoints = {y + shapeHeight, y + shapeHeight, y};
                Polygon triangle = new Polygon(xpoints, ypoints, xpoints.length);
                graphics2D.fillPolygon(triangle);
            }
        }
    }

    @Override
    protected void readOwnPropertiesAndValues(Item item, PreviewProperties properties) {

        Integer itemIndex = item.getData(LegendItem.ITEM_INDEX);


        // READING LABELS
        labels = item.getData(GroupsItem.LABELS_IDS);
        for (int i = 0; i < labels.size(); i++) {
            StringBuilder label = labels.get(i);
            String newLabel = properties.getStringValue(GroupsProperty.getLabelProperty(itemIndex, i));
            label.replace(0, newLabel.length(), newLabel);
        }


        colors = item.getData(GroupsItem.COLORS);
        values = item.getData(GroupsItem.VALUES);

        // properties
        numColumns = properties.getIntValue(LegendModel.getProperty(GroupsProperty.OWN_PROPERTIES, itemIndex, GroupsProperty.GROUPS_NUMBER_COLUMNS));
        numColumns = Math.min(numColumns, labels.size());
        labelPosition = (LegendItem.Direction) properties.getValue(LegendModel.getProperty(GroupsProperty.OWN_PROPERTIES, itemIndex, GroupsProperty.GROUPS_LABEL_POSITION));
        shape = (LegendItem.Shape) properties.getValue(LegendModel.getProperty(GroupsProperty.OWN_PROPERTIES, itemIndex, GroupsProperty.GROUPS_SHAPE));
        labelFont = properties.getFontValue(LegendModel.getProperty(GroupsProperty.OWN_PROPERTIES, itemIndex, GroupsProperty.GROUPS_LABEL_FONT));
        isScalingShapes = properties.getBooleanValue(LegendModel.getProperty(GroupsProperty.OWN_PROPERTIES, itemIndex, GroupsProperty.GROUPS_SCALE_SHAPE));

        // 
        ArrayList<Float> valuesNormalized = new ArrayList<Float>();
        if (isScalingShapes) {
            float maxValue = Collections.max(values);
            for (Float value : values) {
                valuesNormalized.add(value / maxValue);
            }
        }
        else {
            for (Float value : values) {
                valuesNormalized.add(1f);
            }
        }

        values = valuesNormalized;

        paddingBetweenTextAndShape = properties.getIntValue(LegendModel.getProperty(GroupsProperty.OWN_PROPERTIES, itemIndex, GroupsProperty.GROUPS_PADDING_BETWEEN_TEXT_AND_SHAPE));
        paddingBetweenElements = properties.getIntValue(LegendModel.getProperty(GroupsProperty.OWN_PROPERTIES, itemIndex, GroupsProperty.GROUPS_PADDING_BETWEEN_ELEMENTS));

    }

    @Override
    public String getDisplayName() {
        return NbBundle.getMessage(GroupsItemRenderer.class, "GroupsItemRenderer.name");
    }

    @Override
    public boolean needsItemBuilder(ItemBuilder itemBuilder, PreviewProperties properties) {
        return itemBuilder instanceof GroupsItemBuilder;
    }

    //PROPERTIES
    protected Integer numColumns;
    protected Integer paddingBetweenTextAndShape;
    protected Integer paddingBetweenElements;
    protected Font labelFont;
    protected Direction labelPosition;
    protected Shape shape;
    // TO ADD
    protected Boolean isScalingShapes;
    //VALUES
    protected ArrayList<StringBuilder> labels;
    protected ArrayList<Color> colors;
    protected ArrayList<Float> values;
    // min shape size
    protected Float MINIMUM_SHAPE_SIZE = 10f;
}
