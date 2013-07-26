/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.gephi.legend.plugin.table;

import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import java.util.Arrays;
import org.gephi.legend.api.AbstractItem;
import org.gephi.legend.api.LegendModel;
import org.gephi.legend.spi.LegendItem;
import org.gephi.legend.spi.LegendItem.Alignment;
import org.gephi.preview.api.Item;
import org.gephi.preview.api.PreviewProperty;

/**
 * This class represents a table cell. This is NOT a main legend class. This is
 * a supporting class that just holds individual properties. Hence, the builders
 * and renderers are not necessary.
 *
 * @author mvvijesh
 */
public class Cell {

    private static final int BACKGROUND_COLOR = 0;
    private static final int BORDER_COLOR = 1;
    private static final int CELL_FONT = 2;
    private static final int CELL_ALIGNMENT = 3;
    private static final int CELL_FONT_COLOR = 4;
    private static final int CELL_CONTENT = 5;
    private static String[] OWN_PROPERTIES = {
        ".cell.background.color",
        ".cell.border.color",
        ".cell.font.face",
        ".cell.font.alignment",
        ".cell.font.color",
        ".cell.content"
    };

    /**
     * @return the BACKGROUND_COLOR
     */
    public static int getBACKGROUND_COLOR() {
        return BACKGROUND_COLOR;
    }

    /**
     * @return the BORDER_COLOR
     */
    public static int getBORDER_COLOR() {
        return BORDER_COLOR;
    }

    /**
     * @return the CELL_FONT
     */
    public static int getCELL_FONT() {
        return CELL_FONT;
    }

    /**
     * @return the CELL_ALIGNMENT
     */
    public static int getCELL_ALIGNMENT() {
        return CELL_ALIGNMENT;
    }

    /**
     * @return the CELL_FONT_COLOR
     */
    public static int getCELL_FONT_COLOR() {
        return CELL_FONT_COLOR;
    }

    /**
     * @return the CELL_CONTENT
     */
    public static int getCELL_CONTENT() {
        return CELL_CONTENT;
    }

    /**
     * @return the OWN_PROPERTIES
     */
    public static String[] getOWN_PROPERTIES() {
        return OWN_PROPERTIES;
    }
    // define the default properties of the cell
    private Item item = null;
    private Integer row = null;
    private Integer column = null;
    private Color backgroundColor = new Color(1f, 1f, 1f, 0.5f);
    private Color borderColor = Color.BLACK;
    private Font cellFont = new Font("Arial", Font.PLAIN, 20);
    private Alignment cellAlignment = Alignment.CENTER;
    private Color cellFontColor = Color.BLACK;
    private String cellContent = "click to modify";
    private Object[] defaultValues = {
        backgroundColor,
        borderColor,
        cellFont,
        cellAlignment,
        cellFontColor,
        cellContent
    };

    Cell(Item item, int row, int column) {
        this.item = item;
        this.row = row;
        this.column = column;

        // the rest of the values are default
        for (int i = 0; i < OWN_PROPERTIES.length; i++) {
            addCellProperty(row, column, i, defaultValues[i]);
        }
    }

    Cell(Item item, int row, int column, Color backgroundColor, Color borderColor, Font cellFont, Alignment cellAlignment, Color cellFontColor, String cellContent) {
        this.item = item;
        this.row = row;
        this.column = column;
        this.backgroundColor = backgroundColor;
        this.borderColor = borderColor;
        this.cellFont = cellFont;
        this.cellAlignment = cellAlignment;
        this.cellFontColor = cellFontColor;
        this.cellContent = cellContent;

        for (int i = 0; i < OWN_PROPERTIES.length; i++) {
            addCellProperty(row, column, i, defaultValues[i]);
        }
    }

    private void addCellProperty(int row, int column, int property, Object value) {
        // get the list of preview properties, convert to an array list, 

        // During the creation of the legend, previewPropertiesList will be null, since the OWN_PROPERTIES are yet to be defined.
        ArrayList<PreviewProperty> previewProperties = new ArrayList<PreviewProperty>();
        if (getItem().getData(LegendItem.OWN_PROPERTIES) != null) {
            // populate previewProperties with all the preview properties
            Object[] previewPropertyObjectList = getItem().getData(LegendItem.OWN_PROPERTIES);
            PreviewProperty[] previewPropertyList = new PreviewProperty[previewPropertyObjectList.length];
            for (Object prop : previewPropertyObjectList) {
                previewProperties.add((PreviewProperty) prop);
            }
        }

        PreviewProperty previewProperty = null;
        String propertyString = LegendModel.getProperty(getOWN_PROPERTIES(), (Integer) getItem().getData(LegendItem.ITEM_INDEX), property);
        switch (property) {
            case BACKGROUND_COLOR:
                previewProperty = PreviewProperty.createProperty(
                        this,
                        propertyString,
                        Color.class,
                        "TableItem.cell." + row + "." + column + getOWN_PROPERTIES()[getBACKGROUND_COLOR()],
                        "TableItem.cell." + row + "." + column + getOWN_PROPERTIES()[getBACKGROUND_COLOR()],
                        PreviewProperty.CATEGORY_LEGEND_PROPERTY).setValue(value);
                break;

            case BORDER_COLOR:
                previewProperty = PreviewProperty.createProperty(
                        this,
                        propertyString,
                        Color.class,
                        "TableItem.cell." + row + "." + column + getOWN_PROPERTIES()[getBORDER_COLOR()],
                        "TableItem.cell." + row + "." + column + getOWN_PROPERTIES()[getBORDER_COLOR()],
                        PreviewProperty.CATEGORY_LEGEND_PROPERTY).setValue(value);
                break;

            case CELL_FONT:
                previewProperty = PreviewProperty.createProperty(
                        this,
                        propertyString,
                        Font.class,
                        "TableItem.cell." + row + "." + column + getOWN_PROPERTIES()[getCELL_FONT()],
                        "TableItem.cell." + row + "." + column + getOWN_PROPERTIES()[getCELL_FONT()],
                        PreviewProperty.CATEGORY_LEGEND_PROPERTY).setValue(value);
                break;

            case CELL_ALIGNMENT:
                previewProperty = PreviewProperty.createProperty(
                        this,
                        propertyString,
                        Alignment.class,
                        "TableItem.cell." + row + "." + column + getOWN_PROPERTIES()[getCELL_ALIGNMENT()],
                        "TableItem.cell." + row + "." + column + getOWN_PROPERTIES()[getCELL_ALIGNMENT()],
                        PreviewProperty.CATEGORY_LEGEND_PROPERTY).setValue(value);
                break;

            case CELL_FONT_COLOR:
                previewProperty = PreviewProperty.createProperty(
                        this,
                        propertyString,
                        Color.class,
                        "TableItem.cell." + row + "." + column + getOWN_PROPERTIES()[getCELL_FONT_COLOR()],
                        "TableItem.cell." + row + "." + column + getOWN_PROPERTIES()[getCELL_FONT_COLOR()],
                        PreviewProperty.CATEGORY_LEGEND_PROPERTY).setValue(value);
                break;

            case CELL_CONTENT:
                previewProperty = PreviewProperty.createProperty(
                        this,
                        propertyString,
                        String.class,
                        "TableItem.cell." + row + "." + column + getOWN_PROPERTIES()[getCELL_CONTENT()],
                        "TableItem.cell." + row + "." + column + getOWN_PROPERTIES()[getCELL_CONTENT()],
                        PreviewProperty.CATEGORY_LEGEND_PROPERTY).setValue(value);
                break;
        }

        previewProperties.add(previewProperty);
        getItem().setData(LegendItem.OWN_PROPERTIES, previewProperties.toArray());
    }

    String getCellContent() {
        return cellContent;
    }

    Item getItem() {
        return item;
    }

    public Integer getRow() {
        return row;
    }

    public Integer getColumn() {
        return column;
    }

    public Color getBackgroundColor() {
        return backgroundColor;
    }

    public Color getBorderColor() {
        return borderColor;
    }

    public Font getCellFont() {
        return cellFont;
    }

    public Alignment getCellAlignment() {
        return cellAlignment;
    }

    public Color getCellFontColor() {
        return cellFontColor;
    }

    public Object[] getDefaultValues() {
        return defaultValues;
    }
}