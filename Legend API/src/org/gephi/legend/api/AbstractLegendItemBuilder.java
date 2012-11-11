/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.gephi.legend.api;

import java.awt.Color;
import java.awt.Font;
import java.io.File;
import java.util.ArrayList;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;
import org.gephi.data.attributes.api.AttributeModel;
import org.gephi.graph.api.Graph;
import org.gephi.legend.spi.CustomLegendItemBuilder;
import org.gephi.legend.spi.LegendItem;
import org.gephi.legend.spi.LegendItem.Alignment;
import org.gephi.legend.spi.LegendItemBuilder;
import org.gephi.legend.spi.LegendItemRenderer;
import org.gephi.preview.api.*;
import org.openide.util.NbBundle;

/**
 *
 * @author edubecks
 */
public abstract class AbstractLegendItemBuilder implements LegendItemBuilder {

    /**
     * This function creates an Item using
     *
     * @param newItemIndex
     * @param graph
     * @param attributeModel
     * @param builder
     * @return
     */
    public Item createCustomItem(Integer newItemIndex,
            Graph graph,
            AttributeModel attributeModel,
            CustomLegendItemBuilder builder) {
        Item item = buildCustomItem(builder, graph, attributeModel);
        createDefaultProperties(newItemIndex, item);
        return item;

    }

    private void createDefaultProperties(Integer newItemIndex, Item item) {
        item.setData(LegendItem.ITEM_INDEX, newItemIndex);
        item.setData(LegendItem.PROPERTIES, createLegendProperties(item));
        item.setData(LegendItem.OWN_PROPERTIES, createLegendOwnProperties(item));
        item.setData(LegendItem.NUMBER_OF_DYNAMIC_PROPERTIES, 0);
        item.setData(LegendItem.HAS_DYNAMIC_PROPERTIES, hasDynamicProperties());
        item.setData(LegendItem.DYNAMIC_PROPERTIES, new PreviewProperty[0]);
        item.setData(LegendItem.IS_SELECTED, Boolean.FALSE);
        item.setData(LegendItem.IS_BEING_TRANSFORMED, Boolean.FALSE);
        item.setData(LegendItem.CURRENT_TRANSFORMATION, "");
    }

    @Override
    public Item[] getItems(Graph graph, AttributeModel attributeModel) {
        LegendModel legendManager = LegendController.getInstance().getLegendModel();

        ArrayList<Item> legendItems = legendManager.getLegendItems();
        ArrayList<Item> items = new ArrayList<Item>();
        for (Item item : legendItems) {
            if (isBuilderForItem(item)) {
                items.add(item);
            }
        }
        return items.toArray(new Item[items.size()]);
    }

    private PreviewProperty createLegendProperty(Item item, int property, Object value) {
        PreviewProperty previewProperty = null;
        Integer itemIndex = item.getData(LegendItem.ITEM_INDEX);
        String propertyString = LegendModel.getProperty(LegendProperty.LEGEND_PROPERTIES, itemIndex, property);

        switch (property) {
            case LegendProperty.LABEL: {
                previewProperty = PreviewProperty.createProperty(
                        this,
                        propertyString,
                        String.class,
                        NbBundle.getMessage(AbstractLegendItemBuilder.class, "LegendItem.property.label.displayName"),
                        NbBundle.getMessage(AbstractLegendItemBuilder.class, "LegendItem.property.label.description"),
                        PreviewProperty.CATEGORY_LEGEND_PROPERTY).setValue(value);
                break;
            }
            case LegendProperty.IS_DISPLAYING: {
                previewProperty = PreviewProperty.createProperty(
                        this,
                        propertyString,
                        Boolean.class,
                        NbBundle.getMessage(AbstractLegendItemBuilder.class, "LegendItem.property.isDisplaying.displayName"),
                        NbBundle.getMessage(AbstractLegendItemBuilder.class, "LegendItem.property.isDisplaying.description"),
                        PreviewProperty.CATEGORY_LEGEND_PROPERTY).setValue(value);
                break;
            }
            case LegendProperty.USER_ORIGIN_X: {
                previewProperty = PreviewProperty.createProperty(
                        this,
                        propertyString,
                        Float.class,
                        NbBundle.getMessage(AbstractLegendItemBuilder.class, "LegendItem.property.originX.displayName"),
                        NbBundle.getMessage(AbstractLegendItemBuilder.class, "LegendItem.property.originX.description"),
                        PreviewProperty.CATEGORY_LEGEND_PROPERTY).setValue(value);
                break;
            }
            case LegendProperty.USER_ORIGIN_Y: {
                previewProperty = PreviewProperty.createProperty(
                        this,
                        propertyString,
                        Float.class,
                        NbBundle.getMessage(AbstractLegendItemBuilder.class, "LegendItem.property.originY.displayName"),
                        NbBundle.getMessage(AbstractLegendItemBuilder.class, "LegendItem.property.originY.description"),
                        PreviewProperty.CATEGORY_LEGEND_PROPERTY).setValue(value);
                break;
            }
            case LegendProperty.WIDTH: {
                previewProperty = PreviewProperty.createProperty(
                        this,
                        propertyString,
                        Float.class,
                        NbBundle.getMessage(AbstractLegendItemBuilder.class, "LegendItem.property.width.displayName"),
                        NbBundle.getMessage(AbstractLegendItemBuilder.class, "LegendItem.property.width.description"),
                        PreviewProperty.CATEGORY_LEGEND_PROPERTY).setValue(value);
                break;
            }
            case LegendProperty.HEIGHT: {
                previewProperty = PreviewProperty.createProperty(
                        this,
                        propertyString,
                        Float.class,
                        NbBundle.getMessage(AbstractLegendItemBuilder.class, "LegendItem.property.height.displayName"),
                        NbBundle.getMessage(AbstractLegendItemBuilder.class, "LegendItem.property.height.description"),
                        PreviewProperty.CATEGORY_LEGEND_PROPERTY).setValue(value);
                break;
            }
            case LegendProperty.BACKGROUND_IS_DISPLAYING: {
                previewProperty = PreviewProperty.createProperty(
                        this,
                        propertyString,
                        Boolean.class,
                        NbBundle.getMessage(AbstractLegendItemBuilder.class, "LegendItem.property.background.isDisplaying.displayName"),
                        NbBundle.getMessage(AbstractLegendItemBuilder.class, "LegendItem.property.background.isDisplaying.description"),
                        PreviewProperty.CATEGORY_LEGEND_PROPERTY).setValue(value);
                break;
            }
            case LegendProperty.BACKGROUND_COLOR: {
                previewProperty = PreviewProperty.createProperty(
                        this,
                        propertyString,
                        Color.class,
                        NbBundle.getMessage(AbstractLegendItemBuilder.class, "LegendItem.property.background.color.displayName"),
                        NbBundle.getMessage(AbstractLegendItemBuilder.class, "LegendItem.property.background.color.description"),
                        PreviewProperty.CATEGORY_LEGEND_PROPERTY).setValue(value);
                break;
            }
            case LegendProperty.BORDER_IS_DISPLAYING: {
                previewProperty = PreviewProperty.createProperty(
                        this,
                        propertyString,
                        Boolean.class,
                        NbBundle.getMessage(AbstractLegendItemBuilder.class, "LegendItem.property.border.isDisplaying.displayName"),
                        NbBundle.getMessage(AbstractLegendItemBuilder.class, "LegendItem.property.border.isDisplaying.description"),
                        PreviewProperty.CATEGORY_LEGEND_PROPERTY).setValue(value);
                break;
            }
            case LegendProperty.BORDER_COLOR: {
                previewProperty = PreviewProperty.createProperty(
                        this,
                        propertyString,
                        Color.class,
                        NbBundle.getMessage(AbstractLegendItemBuilder.class, "LegendItem.property.border.color.displayName"),
                        NbBundle.getMessage(AbstractLegendItemBuilder.class, "LegendItem.property.border.color.description"),
                        PreviewProperty.CATEGORY_LEGEND_PROPERTY).setValue(value);
                break;
            }
            case LegendProperty.BORDER_LINE_THICK: {
                previewProperty = PreviewProperty.createProperty(
                        this,
                        propertyString,
                        Integer.class,
                        NbBundle.getMessage(AbstractLegendItemBuilder.class, "LegendItem.property.border.lineThick.displayName"),
                        NbBundle.getMessage(AbstractLegendItemBuilder.class, "LegendItem.property.border.lineThick.description"),
                        PreviewProperty.CATEGORY_LEGEND_PROPERTY).setValue(value);
                break;
            }
            case LegendProperty.TITLE_IS_DISPLAYING: {
                previewProperty = PreviewProperty.createProperty(
                        this,
                        propertyString,
                        Boolean.class,
                        NbBundle.getMessage(AbstractLegendItemBuilder.class, "LegendItem.property.title.isDisplaying.displayName"),
                        NbBundle.getMessage(AbstractLegendItemBuilder.class, "LegendItem.property.title.isDisplaying.description"),
                        PreviewProperty.CATEGORY_LEGEND_PROPERTY).setValue(value);
                break;
            }
            case LegendProperty.TITLE: {
                previewProperty = PreviewProperty.createProperty(
                        this,
                        propertyString,
                        String.class,
                        NbBundle.getMessage(AbstractLegendItemBuilder.class, "LegendItem.property.title.displayName"),
                        NbBundle.getMessage(AbstractLegendItemBuilder.class, "LegendItem.property.title.description"),
                        PreviewProperty.CATEGORY_LEGEND_PROPERTY).setValue(value);
                break;
            }
            case LegendProperty.TITLE_FONT: {
                previewProperty = PreviewProperty.createProperty(
                        this,
                        propertyString,
                        Font.class,
                        NbBundle.getMessage(AbstractLegendItemBuilder.class, "LegendItem.property.title.font.displayName"),
                        NbBundle.getMessage(AbstractLegendItemBuilder.class, "LegendItem.property.title.font.description"),
                        PreviewProperty.CATEGORY_LEGEND_PROPERTY).setValue(value);
                break;
            }
            case LegendProperty.TITLE_FONT_COLOR: {
                previewProperty = PreviewProperty.createProperty(
                        this,
                        propertyString,
                        Color.class,
                        NbBundle.getMessage(AbstractLegendItemBuilder.class, "LegendItem.property.title.font.color.displayName"),
                        NbBundle.getMessage(AbstractLegendItemBuilder.class, "LegendItem.property.title.font.color.description"),
                        PreviewProperty.CATEGORY_LEGEND_PROPERTY).setValue(value);
                break;
            }
            case LegendProperty.TITLE_ALIGNMENT: {
                previewProperty = PreviewProperty.createProperty(
                        this,
                        propertyString,
                        Alignment.class,
                        NbBundle.getMessage(AbstractLegendItemBuilder.class, "LegendItem.property.title.alignment.displayName"),
                        NbBundle.getMessage(AbstractLegendItemBuilder.class, "LegendItem.property.title.alignment.description"),
                        PreviewProperty.CATEGORY_LEGEND_PROPERTY).setValue(value);
                break;
            }
            case LegendProperty.DESCRIPTION_IS_DISPLAYING: {
                previewProperty = PreviewProperty.createProperty(
                        this,
                        propertyString,
                        Boolean.class,
                        NbBundle.getMessage(AbstractLegendItemBuilder.class, "LegendItem.property.description.isDisplaying.displayName"),
                        NbBundle.getMessage(AbstractLegendItemBuilder.class, "LegendItem.property.description.isDisplaying.description"),
                        PreviewProperty.CATEGORY_LEGEND_PROPERTY).setValue(value);
                break;
            }
            case LegendProperty.DESCRIPTION: {
                previewProperty = PreviewProperty.createProperty(
                        this,
                        propertyString,
                        String.class,
                        NbBundle.getMessage(AbstractLegendItemBuilder.class, "LegendItem.property.description.displayName"),
                        NbBundle.getMessage(AbstractLegendItemBuilder.class, "LegendItem.property.description.description"),
                        PreviewProperty.CATEGORY_LEGEND_PROPERTY).setValue(value);
                break;
            }
            case LegendProperty.DESCRIPTION_FONT: {
                previewProperty = PreviewProperty.createProperty(
                        this,
                        propertyString,
                        Font.class,
                        NbBundle.getMessage(AbstractLegendItemBuilder.class, "LegendItem.property.description.font.displayName"),
                        NbBundle.getMessage(AbstractLegendItemBuilder.class, "LegendItem.property.description.font.description"),
                        PreviewProperty.CATEGORY_LEGEND_PROPERTY).setValue(value);
                break;
            }
            case LegendProperty.DESCRIPTION_FONT_COLOR: {
                previewProperty = PreviewProperty.createProperty(
                        this,
                        propertyString,
                        Color.class,
                        NbBundle.getMessage(AbstractLegendItemBuilder.class, "LegendItem.property.description.font.color.displayName"),
                        NbBundle.getMessage(AbstractLegendItemBuilder.class, "LegendItem.property.description.font.color.description"),
                        PreviewProperty.CATEGORY_LEGEND_PROPERTY).setValue(value);
                break;
            }
            case LegendProperty.DESCRIPTION_ALIGNMENT: {
                previewProperty = PreviewProperty.createProperty(
                        this,
                        propertyString,
                        Alignment.class,
                        NbBundle.getMessage(AbstractLegendItemBuilder.class, "LegendItem.property.description.alignment.displayName"),
                        NbBundle.getMessage(AbstractLegendItemBuilder.class, "LegendItem.property.description.alignment.description"),
                        PreviewProperty.CATEGORY_LEGEND_PROPERTY).setValue(value);
            }

        }

        return previewProperty;

    }

    private PreviewProperty[] createLegendProperties(Item item) {
        if (setDefaultValues()) {
            updateDefaultValues();
        }

        int[] properties = LegendProperty.LIST_OF_PROPERTIES;

        PreviewProperty[] previewProperties = new PreviewProperty[defaultValuesArrayList.size()];

        // creating label
        Integer itemIndex = item.getData(LegendItem.ITEM_INDEX);
        previewProperties[0] = createLegendProperty(item, LegendProperty.LABEL, defaultLabel + itemIndex + " [" + item.getType() + "]");
        for (int i = 1; i < previewProperties.length; i++) {
            previewProperties[i] = createLegendProperty(item, properties[i], defaultValuesArrayList.get(i));
        }

        return previewProperties;
    }

    @Override
    public String toString() {
        return getTitle();
    }

    public void writeXMLFromData(XMLStreamWriter writer, Item item, PreviewProperties previewProperties) throws XMLStreamException {
    }

    public void writeXMLFromDynamicProperties(XMLStreamWriter writer, Item item, PreviewProperties previewProperties) throws XMLStreamException {
    }

    /**
     * Function that automatically saves a property using its PropertyName and the Value attached to it. Only works if property has a known value type. Known types:
     * <code>Integer</code>,
     * <code> Float</code>,
     * <code> String</code>,,
     * <code> Color</code>,
     * <code> LegendItem.Alignment</code>,
     * <code> LegendItem.Shape</code> and
     * <code> LegendItem.Direction</code>
     *
     * @param writer the XMLStreamWriter to write to
     * @param property property to be saved
     * @param previewProperties Current workspace PreviewProperties
     * @throws XMLStreamException
     */
    protected void writeXMLFromSingleProperty(XMLStreamWriter writer, PreviewProperty property, PreviewProperties previewProperties) throws XMLStreamException {
        //Better read from previewProperties instead of just the property, because LegendMouseListener puts origin x and y in previewProperties.
        Object propertyValue = previewProperties.getValue(property.getName());

        if (propertyValue != null) {
            String text = PreviewProperties.getValueAsText(propertyValue);
            if (text == null) {
                text = writeValueAsText(propertyValue);
            }
            writer.writeStartElement(XML_PROPERTY);
            String name = LegendModel.getPropertyFromPreviewProperty(property);
            writer.writeAttribute(XML_NAME, name);
            writer.writeAttribute(XML_CLASS, propertyValue.getClass().getName());
            writer.writeCharacters(text);
            writer.writeEndElement();
        }
    }

    /**
     * Function that takes an item and saves its data, legend properties, specific item properties, dynamic properties and data using the specified writer.
     *
     * @param writer the XMLStreamWriter to write to
     * @param item the item to be saved
     * @param previewProperties Current workspace PreviewProperties
     * @throws XMLStreamException
     */
    public void writeXMLFromItem(XMLStreamWriter writer, Item item, PreviewProperties previewProperties) throws XMLStreamException {

        // legend type
        writer.writeStartElement(XML_LEGEND_TYPE);
        writer.writeCharacters(item.getType());
        writer.writeEndElement();

        // renderer
        writer.writeStartElement(XML_RENDERER);
        System.out.println("" + item.getData(LegendItem.RENDERER).getClass().getName());
        writer.writeCharacters(item.getData(LegendItem.RENDERER).getClass().getName());
        writer.writeEndElement();

        // legend properties
        writer.writeStartElement(XML_LEGEND_PROPERTY);
        PreviewProperty[] legendProperties = item.getData(LegendItem.PROPERTIES);
        for (PreviewProperty property : legendProperties) {
            writeXMLFromSingleProperty(writer, property, previewProperties);
        }
        writer.writeEndElement();

        // own properties
        writer.writeStartElement(XML_OWN_PROPERTY);
        writeXMLFromItemOwnProperties(writer, item, previewProperties);
        writer.writeEndElement();

        // dynamic properties
        writer.writeStartElement(XML_DYNAMIC_PROPERTY);
        writeXMLFromDynamicProperties(writer, item, previewProperties);
        writer.writeEndElement();


        // data
        writer.writeStartElement(XML_DATA);
        writeXMLFromData(writer, item, previewProperties);
        writer.writeEndElement();
    }

    /**
     * Function that retrieves the data from an XML reader and converts it to data for each kind of item
     *
     * @param reader the XML reader to read the data from
     * @param item the item where the data would be stored
     * @throws XMLStreamException
     */
    public void readXMLToData(XMLStreamReader reader, Item item) throws XMLStreamException {
    }

    public PreviewProperty readXMLToSingleLegendProperty(XMLStreamReader reader, Item item) throws XMLStreamException {
        String propertyName = reader.getAttributeValue(null, XML_NAME);
        String valueString = reader.getElementText();
        int propertyIndex = LegendProperty.getInstance().getProperty(propertyName);
        Class valueClass = defaultValuesArrayList.get(propertyIndex).getClass();
        Object value = PreviewProperties.readValueFromText(valueString, valueClass);
        if (value == null) {
            value = readValueFromText(valueString, valueClass);
        }

        PreviewProperty property = createLegendProperty(item, propertyIndex, value);
        return property;
    }

    /**
     * Function that takes some value in a String form and converts it to the specified class type
     *
     * @param valueString the value in a String form
     * @param valueClass the class type to convert the value
     * @return
     */
    protected Object readValueFromText(String valueString, Class valueClass) {
        Object value = null;
        if (valueClass.equals(LegendItem.Alignment.class)) {
            value = availableAlignments[Integer.parseInt(valueString)];
        } else if (valueClass.equals(LegendItem.Shape.class)) {
            value = availableShapes[Integer.parseInt(valueString)];
        } else if (valueClass.equals(LegendItem.Direction.class)) {
            value = availableDirections[Integer.parseInt(valueString)];
        } else if (valueClass.equals(Boolean.class)) {
            value = Boolean.parseBoolean(valueString);
        } else if (valueClass.equals(Integer.class)) {
            value = Integer.parseInt(valueString);
        } else if (valueClass.equals(File.class)) {
            value = new File(valueString);
        }

        return value;
    }

    /**
     * Function that retrieves the properties (propertyName and value) from an XML reader and converts it to list of properties. Normally using the
     * <code>readXMLToSingleOwnProperty</code> for each property.
     *
     * @param reader the XML reader to read the data from
     * @param item the item where the data would be stored
     * @return
     * @throws XMLStreamException
     */
    public abstract ArrayList<PreviewProperty> readXMLToOwnProperties(XMLStreamReader reader, Item item) throws XMLStreamException;

    /**
     * Function that retrieves the dynamic properties (if any) from an XML reader and converts it to list of properties. Normally using the
     * <code>readXMLToSingleOwnProperty</code> for each property.
     *
     * @param reader the XML reader to read the data from
     * @param item the item where the data would be stored
     * @return
     * @throws XMLStreamException
     */
    public ArrayList<PreviewProperty> readXMLToDynamicProperties(XMLStreamReader reader, Item item) throws XMLStreamException {
        reader.nextTag();
        return new ArrayList<PreviewProperty>();
    }

    public ArrayList<PreviewProperty> readXMLToLegendProperties(XMLStreamReader reader, Item item) throws XMLStreamException {
        ArrayList<PreviewProperty> properties = new ArrayList<PreviewProperty>();

        // legend properties

        boolean end = false;
        while (reader.hasNext() && !end) {
            int type = reader.next();
            switch (type) {
                case XMLStreamReader.START_ELEMENT: {
                    PreviewProperty property = readXMLToSingleLegendProperty(reader, item);
                    properties.add(property);
                    break;
                }
                case XMLStreamReader.CHARACTERS: {
                    break;
                }
                case XMLStreamReader.END_ELEMENT: {
                    end = true;
                    break;
                }
            }
        }

        return properties;
    }

    public void readXMLToRenderer(XMLStreamReader reader, Item item) throws XMLStreamException {
        if (reader.getLocalName().equals(XML_RENDERER)) {
            String valueString = reader.getElementText();
            LegendItemRenderer availableRenderer = LegendController.getInstance().getRenderers().get(valueString);
            if (availableRenderer != null) {
                item.setData(LegendItem.RENDERER, availableRenderer);
            }
        }
    }

    /**
     * Function that reads the legend properties, specific item properties, dynamic properties and data and converts it to an Item using the specified reader.
     *
     * @param reader the XML reader to read the data from
     * @param newItemIndex used to create the Item
     * @return
     * @throws XMLStreamException
     */
    public Item readXMLToItem(XMLStreamReader reader, Integer newItemIndex) throws XMLStreamException {
        Item item = createNewLegendItem(null);
        item.setData(LegendItem.ITEM_INDEX, newItemIndex);

        // opening renderer
        reader.nextTag();
        readXMLToRenderer(reader, item);


        // opening legend properties
        reader.nextTag();
        ArrayList<PreviewProperty> legendProperties = readXMLToLegendProperties(reader, item);

        // opening own properties
        reader.nextTag();
        ArrayList<PreviewProperty> ownProperties = readXMLToOwnProperties(reader, item);

        // opening dynamic properties
        reader.nextTag();
        ArrayList<PreviewProperty> dynamicProperties = readXMLToDynamicProperties(reader, item);
        // closing dynamic properties

        // data
//        reader.nextTag();
        reader.nextTag();
        readXMLToData(reader, item);

        // finish reading
        reader.next();



        PreviewProperty[] legendPropertiesArray = legendProperties.toArray(new PreviewProperty[legendProperties.size()]);
        PreviewProperty[] ownPropertiesArray = ownProperties.toArray(new PreviewProperty[ownProperties.size()]);
        PreviewProperty[] dynamicPropertiesArray = dynamicProperties.toArray(new PreviewProperty[dynamicProperties.size()]);

        // setting data
        item.setData(LegendItem.ITEM_INDEX, newItemIndex);
        item.setData(LegendItem.PROPERTIES, legendPropertiesArray);
        item.setData(LegendItem.OWN_PROPERTIES, ownPropertiesArray);

        item.setData(LegendItem.HAS_DYNAMIC_PROPERTIES, hasDynamicProperties());
        item.setData(LegendItem.DYNAMIC_PROPERTIES, dynamicPropertiesArray);
        item.setData(LegendItem.IS_SELECTED, Boolean.FALSE);
        item.setData(LegendItem.IS_BEING_TRANSFORMED, Boolean.FALSE);
        item.setData(LegendItem.CURRENT_TRANSFORMATION, "");

        return item;
    }

    /**
     * Converts the propertyValue of a known type to an String object
     *
     * @param propertyValue Known * types: <code> LegendItem.Alignment</code>, <code> LegendItem.Shape</code> and <code> LegendItem.Direction</code>
     * @return
     */
    protected String writeValueAsText(Object propertyValue) {
        String text;
        if (propertyValue instanceof LegendItem.Alignment) {
            LegendItem.Alignment propertyValueString = (LegendItem.Alignment) propertyValue;
            text = propertyValueString.getValue();
        } else if (propertyValue instanceof LegendItem.Direction) {
            LegendItem.Direction propertyValueString = (LegendItem.Direction) propertyValue;
            text = propertyValueString.getValue();
        } else if (propertyValue instanceof LegendItem.Shape) {
            LegendItem.Shape propertyValueString = (LegendItem.Shape) propertyValue;
            text = propertyValueString.getValue();
        }else{
            text = propertyValue.toString();
        }
        return text;
    }
    // xml 
    protected static final String XML_PROPERTY = "property";
    private static final String XML_LEGEND_TYPE = "legendtype";
    private static final String XML_RENDERER = "renderer";
    private static final String XML_DYNAMIC_PROPERTY = "dynamicproperty";
    private static final String XML_LEGEND_PROPERTY = "legendproperty";
    protected static final String XML_OWN_PROPERTY = "itemproperty";
    protected static final String XML_NAME = "name";
    protected static final String XML_CLASS = "class";
    private static final String XML_DATA = "itemdata";
    //DEFAULT VALUES 
    // BACKGROUND AND BORDER
    protected Boolean defaultBackgroundIsDisplaying = Boolean.FALSE;
    protected Color defaultBackgroundColor = Color.WHITE;
    protected Boolean defaultBorderIsDisplaying = Boolean.FALSE;
    protected Color defaultBorderColor = Color.BLACK;
    protected Integer defaultBorderLineThick = 5;
    // LABEL
    protected String defaultLabel = "";
    // IS_DISPLAYING
    protected Boolean defaultIsDisplaying = Boolean.TRUE;
    //ORIGIN
    protected Float defaultOriginX = 0f;
    protected Float defaultOriginY = 0f;
    //WIDTH
    protected Float defaultWidth = 500f;
    protected Float defaultHeight = 300f;
    // TITLE
    protected Boolean defaultTitleIsDisplaying = Boolean.FALSE;
    protected String defaultTitle = "TITLE";
    protected Font defaultTitleFont = new Font("Arial", Font.BOLD, 30);
    protected Alignment defaultTitleAlignment = Alignment.CENTER;
    protected Color defaultTitleFontColor = Color.BLACK;
    // DESCRIPTION
    protected String defaultDescription = "description ... description ...description ...description ...description ...description ...description ...description ...description ...description ...description ...description ...";
    protected Boolean defaultDescriptionIsDisplaying = Boolean.FALSE;
    protected Color defaultDescriptionFontColor = Color.BLACK;
    protected Alignment defaultDescriptionAlignment = Alignment.LEFT;
    protected Font defaultDescriptionFont = new Font("Arial", Font.PLAIN, 10);
    // default values list
    private ArrayList<Object> defaultValuesArrayList;

    public AbstractLegendItemBuilder() {
        updateDefaultValues();
    }

    public void updateDefaultValues() {
        this.defaultValuesArrayList = new ArrayList<Object>();
        defaultValuesArrayList.add(this.defaultLabel);
        defaultValuesArrayList.add(this.defaultIsDisplaying);
        defaultValuesArrayList.add(this.defaultOriginX);
        defaultValuesArrayList.add(this.defaultOriginY);
        defaultValuesArrayList.add(this.defaultWidth);
        defaultValuesArrayList.add(this.defaultHeight);
        defaultValuesArrayList.add(this.defaultBackgroundIsDisplaying);
        defaultValuesArrayList.add(this.defaultBackgroundColor);
        defaultValuesArrayList.add(this.defaultBorderIsDisplaying);
        defaultValuesArrayList.add(this.defaultBorderColor);
        defaultValuesArrayList.add(this.defaultBorderLineThick);
        defaultValuesArrayList.add(this.defaultTitleIsDisplaying);
        defaultValuesArrayList.add(this.defaultTitle);
        defaultValuesArrayList.add(this.defaultTitleFont);
        defaultValuesArrayList.add(this.defaultTitleFontColor);
        defaultValuesArrayList.add(this.defaultTitleAlignment);
        defaultValuesArrayList.add(this.defaultDescriptionIsDisplaying);
        defaultValuesArrayList.add(this.defaultDescription);
        defaultValuesArrayList.add(this.defaultDescriptionFont);
        defaultValuesArrayList.add(this.defaultDescriptionFontColor);
        defaultValuesArrayList.add(this.defaultDescriptionAlignment);
    }
    private final Object[] availableAlignments = {
        LegendItem.Alignment.LEFT,
        LegendItem.Alignment.RIGHT,
        LegendItem.Alignment.CENTER,
        LegendItem.Alignment.JUSTIFIED
    };
    private final Object[] availableShapes = {
        LegendItem.Shape.RECTANGLE,
        LegendItem.Shape.CIRCLE,
        LegendItem.Shape.TRIANGLE
    };
    private final Object[] availableDirections = {
        LegendItem.Direction.UP,
        LegendItem.Direction.DOWN,
        LegendItem.Direction.RIGHT,
        LegendItem.Direction.LEFT
    };
}
