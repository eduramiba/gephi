/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.gephi.legend.plugin.renderers;

/**
 *
 * @author edubecks
 */
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import org.gephi.legend.api.AbstractLegendItemRenderer;
import org.gephi.legend.api.LegendModel;
import org.gephi.legend.plugin.builders.TableItemBuilder;
import org.gephi.legend.plugin.items.TableItem;
import org.gephi.legend.plugin.properties.TableProperty;
import org.gephi.legend.spi.LegendItem;
import org.gephi.legend.spi.LegendItem.Alignment;
import org.gephi.legend.spi.LegendItem.Direction;
import org.gephi.preview.api.*;
import org.gephi.preview.spi.ItemBuilder;
import org.gephi.preview.spi.Renderer;
import org.openide.util.NbBundle;
import org.openide.util.lookup.ServiceProvider;

@ServiceProvider(service = Renderer.class, position = 501)
public class TableItemRenderer extends AbstractLegendItemRenderer {

    @Override
    public boolean isAnAvailableRenderer(Item item) {
        return item instanceof TableItem;
    }

    @Override
    public String getDisplayName() {
        return NbBundle.getMessage(TableItemRenderer.class, "TableItemRenderer.name");
    }

    @Override
    public boolean needsItemBuilder(ItemBuilder itemBuilder, PreviewProperties properties) {
        return itemBuilder instanceof TableItemBuilder;
    }

    protected void createColumnLabels(Graphics2D graphics2D, AffineTransform arrangeTranslation, Integer width, Integer height) {

        graphics2D.setTransform(arrangeTranslation);

        int cellSizeWidth = width / columnLabels.size();
        int cellSizeHeight = height / rowLabels.size();

        // diagonal shift
        Integer diagonalShift = (int) (cellSizeWidth * Math.cos(columnTextDirection.rotationAngle()));


        //font
        graphics2D.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                                    RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        graphics2D.setColor(fontColor);
        graphics2D.setFont(font);

        //metrics
        FontMetrics metrics = graphics2D.getFontMetrics();
        Integer fontHeight = metrics.getHeight();

        //overriding centerdistance
        int centerDistance = (cellSizeWidth - metrics.getHeight()) / 2;


        int maxVerticalTextWidth = Integer.MIN_VALUE;
        for (StringBuilder label : columnLabels) {
            maxVerticalTextWidth = Math.max(maxVerticalTextWidth, metrics.stringWidth(label.toString()));
        }


        switch (columnTextDirection) {
            case VERTICAL: {

                arrangeTranslation.translate(0, height - verticalExtraMargin);
                arrangeTranslation.rotate(columnTextDirection.rotationAngle());
                graphics2D.setTransform(arrangeTranslation);


                for (int i = 0; i < columnLabels.size(); i++) {
                    String label = columnLabels.get(i).toString();
                    graphics2D.setColor(columnLabelColors.get(i));
                    graphics2D.drawString(label,
                                          height - metrics.stringWidth(label) - MINIMUM_MARGIN,
                                          i * cellSizeWidth + fontHeight + centerDistance);

                }
                break;
            }
            case DIAGONAL: {

                //overriding centerdistance
                centerDistance = (int) ((cellSizeWidth - fontHeight * Math.cos(columnTextDirection.rotationAngle())) / 2);

                //vertical shift for Diagonal case
                int verticalShift = -(int) (height * Math.sin(columnTextDirection.rotationAngle()));
                // centering
                arrangeTranslation.translate(centerDistance - fontHeight / 2, 0);

                arrangeTranslation.translate(0, verticalShift + diagonalShift - verticalExtraMargin);
                arrangeTranslation.rotate(columnTextDirection.rotationAngle());
                graphics2D.setTransform(arrangeTranslation);


                for (int i = 0; i < columnLabels.size(); i++) {
                    String label = columnLabels.get(i).toString();
                    graphics2D.setColor(columnLabelColors.get(i));
                    graphics2D.drawString(label,
                                          MINIMUM_MARGIN + ((i) * diagonalShift) + centerDistance + horizontalExtraMargin,
                                          (i * diagonalShift) + fontHeight - centerDistance + horizontalExtraMargin);
                }

                break;
            }
            case HORIZONTAL: {


                for (int i = 0; i < columnLabels.size(); i++) {
                    String label = columnLabels.get(i).toString();
                    graphics2D.setColor(columnLabelColors.get(i));

                    legendDrawText(graphics2D, label, font, columnLabelColors.get(i), i * cellSizeWidth, 0, cellSizeWidth, height, verticalTextAlignment);

                }
            }
        }



    }

    protected void createRowLabels(Graphics2D graphics, AffineTransform arrangeTranslation, Integer width, Integer height) {



        int cellSizeWidth = width / columnLabels.size();
        int cellSizeHeight = height / rowLabels.size();




        //arrange
        graphics.setTransform(arrangeTranslation);

        int fontSize = font.getSize();
        int horizontalCenterDistance = (cellSizeHeight - fontSize) / 2;



        FontMetrics metrics = graphics.getFontMetrics(font);
        graphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        graphics.setColor(Color.WHITE);
        for (int i = 0; i < rowLabels.size(); i++) {
            String label = rowLabels.get(i).toString();
            legendDrawText(graphics, label, font, rowLabelColors.get(i), 0, (i * cellSizeHeight), width - MINIMUM_MARGIN, cellSizeHeight, horizontalTextAlignment);

        }
    }

    protected void createTableImage(Graphics2D graphics, AffineTransform arrangeTranslation, int width, int height) {
        //drawing background 


        int cellSizeWidth = width / columnLabels.size();
        int cellSizeHeight = height / rowLabels.size();

        //arrange
        graphics.setTransform(arrangeTranslation);


        graphics.setColor(Color.WHITE);
        for (int i = 0; i < tableValuesFloat.length; i++) {

            for (int j = 0; j < tableValuesFloat[i].length; j++) {

                int x = j * cellSizeWidth;
                int y = i * cellSizeHeight;

                if (isCellColoring) {
                    drawCellColoring(graphics, x, y, cellSizeWidth, cellSizeHeight, tableValuesString.get(i).get(j), tableValuesFloat[i][j], valueColors.get(i).get(j));
                }
                else {
                    drawCellText(graphics, x, y, cellSizeWidth, cellSizeHeight, tableValues[i][j], font, valueColors.get(i).get(j));
                }
//                graphics.setColor(valueColors.get(i).get(j));
//
//                int x1, y1, x2, y2;
//
//                if (isCellColoring) {
//                    x1 = j * cellSizeWidth;
//                    y2 = (int) (cellSizeHeight * tableValuesFloat[i][j]) - 1;
//                    y1 = ((i + 1) * cellSizeHeight) - y2;
//                    x2 = cellSizeWidth - 1;
//                    graphics.fillRect(x1, y1, x2, y2);
//                }
//                else {
//                    x1 = j * cellSizeWidth;
//                    y1 = i * cellSizeHeight;
//                    y2 = cellSizeHeight;
//                    x2 = cellSizeWidth;
//                    legendDrawText(graphics, tableValuesFloat[i][j] + "", font, fontColor, x1, y1, x2, y2, Alignment.CENTER);
//                }

                // GRID DISPLAYING
                if (isDisplayingGrid) {
//                    int x1 = j * cellSizeWidth;
//                    y1 = i * cellSizeHeight;
//                    y2 = cellSizeHeight;
//                    x2 = cellSizeWidth;
                    graphics.setColor(gridColor);
                    graphics.drawRect(x, y, cellSizeWidth, cellSizeHeight);
                }

            }
        }

    }

    /**
     * Override this function to render the specified cell in a different way
     *
     * @param graphics the Graphics object to draw to
     * @param x the x coordinate of the area containing the cell
     * @param y the y coordinate of the area containing the cell
     * @param width the width of the area containing the cell
     * @param height the height of the area containing the cell
     * @param value value to be displayed
     * @param color rendering color for the value
     */
    protected void drawCellColoring(Graphics2D graphics, Integer x, Integer y, Integer width, Integer height, String value, Float valueNormalized, Color color) {
        int x1 = x;
        int y2 = (int) (height * valueNormalized);
        int y1 = (y + height) - y2;
        int x2 = width;
        graphics.setColor(color);
        graphics.fillRect(x1, y1, x2, y2);
    }

    protected void drawCellText(Graphics2D graphics, Integer x, Integer y, Integer width, Integer height, String value, Font font, Color color) {
        legendDrawText(graphics, value, font, color, x, y, width, height, Alignment.CENTER);
    }

    @Override
    protected void renderToGraphics(Graphics2D graphics2D, AffineTransform origin, Integer width, Integer height) {

        graphics2D.setTransform(origin);
        graphics2D.setFont(font);
        FontMetrics fontMetrics = graphics2D.getFontMetrics(font);

        int maxRowTextWidth = Integer.MIN_VALUE;
        for (StringBuilder label : rowLabels) {
            maxRowTextWidth = Math.max(maxRowTextWidth, fontMetrics.stringWidth(label.toString()));
        }
        int maxVerticalTextWidth = Integer.MIN_VALUE;
        for (StringBuilder label : columnLabels) {
            maxVerticalTextWidth = Math.max(maxVerticalTextWidth, fontMetrics.stringWidth(label.toString()));
        }

        int rowLabelsWidth = maxRowTextWidth + 2 * MINIMUM_MARGIN;

        int columnTextHeight = 0;
        int tableWidth = 0;
        int tableHeight = 0;
        switch (columnTextDirection) {
            case VERTICAL: {
                columnTextHeight = maxVerticalTextWidth + 2 * MINIMUM_MARGIN;
                tableHeight = height - columnTextHeight;
                tableWidth = width - rowLabelsWidth;
                break;
            }
            case DIAGONAL: {
                columnTextHeight = maxVerticalTextWidth + 2 * MINIMUM_MARGIN;
                tableHeight = height - columnTextHeight;
                tableWidth = width - rowLabelsWidth - (int) Math.cos(columnTextHeight);
                break;
            }
            case HORIZONTAL: {
                columnTextHeight = fontMetrics.getHeight();
                tableHeight = height - columnTextHeight;
                tableWidth = width - rowLabelsWidth;
                break;
            }
        }

        int verticalLabelsWidth = tableWidth;
        int horizontalLabelsHeight = tableHeight;




        // DRAWING VERTICAL TEXT
        AffineTransform verticalTextOrigin = new AffineTransform(origin);
        AffineTransform horizontalTextOrigin = new AffineTransform(origin);
        AffineTransform tableOrigin = new AffineTransform(origin);
        // arranging vertical text
        if (verticalTextPosition == TableItem.ColumnPosition.UP) {
            verticalTextOrigin.translate(0, 0);
            tableOrigin.translate(0, columnTextHeight);
            horizontalTextOrigin.translate(0, columnTextHeight);
        }
        else if (verticalTextPosition == TableItem.ColumnPosition.BOTTOM) {
            verticalTextOrigin.translate(0, tableHeight);
            tableOrigin.translate(0, 0);
            horizontalTextOrigin.translate(0, 0);
        }
        // arranging horizontal text
        if (horizontalTextPosition == TableItem.RowPosition.LEFT) {
            horizontalTextOrigin.translate(0, 0);
            verticalTextOrigin.translate(rowLabelsWidth, 0);
            tableOrigin.translate(rowLabelsWidth, 0);
        }
        else if (horizontalTextPosition == TableItem.RowPosition.RIGHT) {
            horizontalTextOrigin.translate(tableWidth + MINIMUM_MARGIN, 0);
            verticalTextOrigin.translate(0, 0);
            tableOrigin.translate(0, 0);
        }
        createColumnLabels(graphics2D, verticalTextOrigin, verticalLabelsWidth, columnTextHeight);
        createRowLabels(graphics2D, horizontalTextOrigin, rowLabelsWidth, horizontalLabelsHeight);
        if (isCellColoring) {
            normalizeWithMinValueZero(tableValuesFloat);
        }
        tableValues = new String[tableValuesFloat.length][tableValuesFloat[0].length];
        for (int i = 0; i < tableValuesFloat.length; i++) {
            for (int j = 0; j < tableValuesFloat[0].length; j++) {
                tableValues[i][j] = tableValuesFloat[i][j] + "";
            }
        }

        createTableImage(graphics2D, tableOrigin, tableWidth, tableHeight);
    }

    private void normalize(Float[][] table, float minValue, float maxValue) {
        float range = maxValue - minValue;
        for (int i = 0; i < table.length; i++) {
            for (int j = 0; j < table[i].length; j++) {
                table[i][j] = (table[i][j] - minValue) / (range);
            }
        }
    }

    private void normalizeWithMinValueZero(Float[][] table) {
        float minValue = 0f;
        float maxValue = Float.MIN_VALUE;
        for (int i = 0; i < table.length; i++) {
            for (int j = 0; j < table[i].length; j++) {
                maxValue = Math.max(maxValue, table[i][j]);
            }
        }
        normalize(table, minValue, maxValue);
    }

    private void normalize(Float[][] table) {
        float minValue = Float.MAX_VALUE;
        float maxValue = Float.MIN_VALUE;
        for (int i = 0; i < table.length; i++) {
            for (int j = 0; j < table[i].length; j++) {
                maxValue = Math.max(maxValue, table[i][j]);
                minValue = Math.min(minValue, table[i][j]);
            }
        }
        normalize(table, minValue, maxValue);
    }

    @Override
    protected void readOwnPropertiesAndValues(Item item, PreviewProperties properties) {

        Integer itemIndex = item.getData(LegendItem.ITEM_INDEX);

        tableValuesString = item.getData(TableItem.TABLE_VALUES);

        // READING VALUES AND CONVERTING THEM TO FLOAT
        tableValuesFloat = new Float[tableValuesString.size()][tableValuesString.get(0).size()];
        for (int i = 0; i < tableValuesString.size(); i++) {
            for (int j = 0; j < tableValuesString.get(i).size(); j++) {
                tableValuesFloat[i][j] = Float.parseFloat(tableValuesString.get(i).get(j));
            }
        }

        // READING COLORS
        valueColors = item.getData(TableItem.COLOR_VALUES);
        columnLabelColors = item.getData(TableItem.COLOR_VERTICAL);
        rowLabelColors = item.getData(TableItem.COLOR_HORIZONTAL);


        // READING LABELS
        labels = item.getData(TableItem.LABELS_IDS);
        rowLabels = item.getData(TableItem.HORIZONTAL_LABELS);
        columnLabels = item.getData(TableItem.VERTICAL_LABELS);
        for (int i = 0; i < labels.size(); i++) {
            StringBuilder label = labels.get(i);
            String newLabel = properties.getStringValue(TableProperty.getLabelProperty(itemIndex, i));
            label.replace(0, newLabel.length(), newLabel);
        }



        //properties
        font = properties.getFontValue(LegendModel.getProperty(TableProperty.OWN_PROPERTIES, itemIndex, TableProperty.TABLE_FONT));
        fontColor = properties.getColorValue(LegendModel.getProperty(TableProperty.OWN_PROPERTIES, itemIndex, TableProperty.TABLE_FONT_COLOR));
        isCellColoring = properties.getBooleanValue(LegendModel.getProperty(TableProperty.OWN_PROPERTIES, itemIndex, TableProperty.TABLE_IS_CELL_COLORING));
        verticalExtraMargin = properties.getIntValue(LegendModel.getProperty(TableProperty.OWN_PROPERTIES, itemIndex, TableProperty.TABLE_COLUMN_EXTRA_MARGIN));
        horizontalExtraMargin = properties.getIntValue(LegendModel.getProperty(TableProperty.OWN_PROPERTIES, itemIndex, TableProperty.TABLE_ROW_EXTRA_MARGIN));

        //grid
        gridColor = properties.getColorValue(LegendModel.getProperty(TableProperty.OWN_PROPERTIES, itemIndex, TableProperty.TABLE_GRID_COLOR));
        isDisplayingGrid = properties.getBooleanValue(LegendModel.getProperty(TableProperty.OWN_PROPERTIES, itemIndex, TableProperty.TABLE_IS_DISPLAYING_GRID));

        horizontalTextAlignment = (Alignment) properties.getValue(LegendModel.getProperty(TableProperty.OWN_PROPERTIES, itemIndex, TableProperty.TABLE_ROW_TEXT_ALIGNMENT));
        verticalTextAlignment = (Alignment) properties.getValue(LegendModel.getProperty(TableProperty.OWN_PROPERTIES, itemIndex, TableProperty.TABLE_COLUMN_TEXT_ALIGNMENT));
        verticalTextPosition = (TableItem.ColumnPosition) properties.getValue(LegendModel.getProperty(TableProperty.OWN_PROPERTIES, itemIndex, TableProperty.TABLE_COLUMN_TEXT_POSITION));
        horizontalTextPosition = (TableItem.RowPosition) properties.getValue(LegendModel.getProperty(TableProperty.OWN_PROPERTIES, itemIndex, TableProperty.TABLE_ROW_TEXT_POSITION));
        columnTextDirection = (TableItem.ColumnTextDirection) properties.getValue(LegendModel.getProperty(TableProperty.OWN_PROPERTIES, itemIndex, TableProperty.TABLE_COLUMN_TEXT_ROTATION));
    }

    protected Font font;
    protected Color fontColor;
    protected ArrayList<StringBuilder> labels;
    protected ArrayList<StringBuilder> rowLabels;
    protected ArrayList<StringBuilder> columnLabels;
    protected ArrayList<Color> rowLabelColors;
    protected ArrayList<Color> columnLabelColors;
    protected ArrayList<ArrayList<Color>> valueColors;
    protected Boolean isCellColoring;
    protected TableItem.RowPosition horizontalTextPosition;
    protected Alignment horizontalTextAlignment;
    protected TableItem.ColumnPosition verticalTextPosition;
    protected Alignment verticalTextAlignment;
    protected TableItem.ColumnTextDirection columnTextDirection;
    protected Integer verticalExtraMargin;
    protected Integer horizontalExtraMargin;
    private final Integer MINIMUM_MARGIN = 3;
    protected ArrayList<ArrayList<String>> tableValuesString;
    protected Float[][] tableValuesFloat;
    protected String[][] tableValues;
    // Grid
    protected Boolean isDisplayingGrid;
    protected Color gridColor;
}
