/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.gephi.legend.builders;

import java.awt.Color;
import java.awt.Font;
import java.io.File;
import java.util.ArrayList;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;
import org.gephi.data.attributes.api.AttributeModel;
import org.gephi.graph.api.Graph;
import org.gephi.legend.api.CustomLegendItemBuilder;
import org.gephi.legend.items.DescriptionItem;
import org.gephi.legend.items.DescriptionItemElement;
import org.gephi.legend.items.LegendItem;
import org.gephi.legend.items.LegendItem.Alignment;
import org.gephi.legend.items.TableItem;
import org.gephi.legend.manager.LegendController;
import org.gephi.legend.manager.LegendManager;
import org.gephi.legend.properties.LegendProperty;
import org.gephi.preview.api.*;
import org.gephi.preview.spi.ItemBuilder;
import org.openide.util.NbBundle;

/**
 *
 * @author edubecks
 */
public abstract class LegendItemBuilder implements ItemBuilder {

    /**
     * Function used to override default values of
     * <code>properties</code> Possible values to be overriden:<br />
     *
     * LABEL: <ul> <li> defaultLabel </li> </ul> IS_DISPLAYING: <ul> <li>
     * defaultIsDisplaying </li> </ul> ORIGIN <ul> <li> defaultOriginX </li>
     * <li> defaultOriginY </li> </ul> WIDTH <ul> <li> defaultWidth </li> <li>
     * defaultHeight </li> </ul> TITLE: <ul> <li> defaultTitleIsDisplaying </li>
     * <li> defaultTitle </li> <li> defaultTitleFont </li> <li>
     * defaultTitleAlignment </li> <li> defaultTitleFontColor </li> </ul>
     * DESCRIPTION: <ul> <li> defaultDescription </li> <li>
     * defaultDescriptionIsDisplaying </li> <li> defaultDescriptionFontColor
     * </li> <li> defaultDescriptionAlignment </li> <li> defaultDescriptionFont
     * </li> </ul>
     */
    protected abstract boolean setDefaultValues();

    /**
     * Based on
     * <code>properties</code>, determine whether this builder was used to build
     * <code>Item</code>.
     *
     * @param item the item to be tested
     * @return <code>true</code> if <code>item</code> was built by this *
     * builder, <code>false</code> otherwise
     */
    protected abstract boolean isBuilderForItem(Item item);

    /**
     * Builds and item based on
     * <code>graph</code> and
     * <code>attributeModel</code> data
     *
     * @param graph
     * @param attributeModel
     * @return
     */
    protected abstract Item buildCustomItem(CustomLegendItemBuilder builder, Graph graph, AttributeModel attributeModel);

    /**
     * Used to determine if the
     * <code>item</code> has dynamic properties to be displayed in the
     * PropertySheet Editor
     *
     * @return <code>true</code> if <code>item</code> has dynamic * *
     * properties, <code>false</code> otherwise
     */
    protected abstract Boolean hasDynamicProperties();

    /**
     * Function used to create the specific PreviewProperty for each type of
     * LegendItem
     *
     * @param item
     * @return an array of PreviewProperty to be appended to the general
     * LegendItem's PreviewProperty
     */
    protected abstract PreviewProperty[] createLegendOwnProperties(Item item);

    /**
     *
     * @return a list of all the available builders for an specific type of
     * builder
     */
    public abstract ArrayList<CustomLegendItemBuilder> getAvailableBuilders();

    /**
     * @param graph
     * @return a new instance of an Item Ex:
     */
    public abstract Item createNewLegendItem(Graph graph);

    /**
     * This function creates an Item using
     *
     * @param newItemIndex
     * @param graph
     * @param attributeModel
     * @param builder
     * @return
     */
    public Item createCustomItem(Integer newItemIndex, Graph graph, AttributeModel attributeModel, CustomLegendItemBuilder builder) {
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
        LegendManager legendManager = LegendController.getInstance().getLegendManager();

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
        String propertyString = LegendManager.getProperty(LegendProperty.LEGEND_PROPERTIES, itemIndex, property);



        switch (property) {
            case LegendProperty.LABEL: {
                previewProperty = PreviewProperty.createProperty(
                        this,
                        propertyString,
                        String.class,
                        NbBundle.getMessage(LegendManager.class, "LegendItem.property.label.displayName"),
                        NbBundle.getMessage(LegendManager.class, "LegendItem.property.label.description"),
                        PreviewProperty.CATEGORY_LEGEND_PROPERTY).setValue(value);
                break;
            }
            case LegendProperty.IS_DISPLAYING: {
                previewProperty = PreviewProperty.createProperty(
                        this,
                        propertyString,
                        Boolean.class,
                        NbBundle.getMessage(LegendManager.class, "LegendItem.property.isDisplaying.displayName"),
                        NbBundle.getMessage(LegendManager.class, "LegendItem.property.isDisplaying.description"),
                        PreviewProperty.CATEGORY_LEGEND_PROPERTY).setValue(value);
                break;
            }
            case LegendProperty.USER_ORIGIN_X: {
                previewProperty = PreviewProperty.createProperty(
                        this,
                        propertyString,
                        Float.class,
                        NbBundle.getMessage(LegendManager.class, "LegendItem.property.originX.displayName"),
                        NbBundle.getMessage(LegendManager.class, "LegendItem.property.originX.description"),
                        PreviewProperty.CATEGORY_LEGEND_PROPERTY).setValue(value);
                break;
            }
            case LegendProperty.USER_ORIGIN_Y: {
                previewProperty = PreviewProperty.createProperty(
                        this,
                        propertyString,
                        Float.class,
                        NbBundle.getMessage(LegendManager.class, "LegendItem.property.originY.displayName"),
                        NbBundle.getMessage(LegendManager.class, "LegendItem.property.originY.description"),
                        PreviewProperty.CATEGORY_LEGEND_PROPERTY).setValue(value);
                break;
            }
            case LegendProperty.WIDTH: {
                previewProperty = PreviewProperty.createProperty(
                        this,
                        propertyString,
                        Float.class,
                        NbBundle.getMessage(LegendManager.class, "LegendItem.property.width.displayName"),
                        NbBundle.getMessage(LegendManager.class, "LegendItem.property.width.description"),
                        PreviewProperty.CATEGORY_LEGEND_PROPERTY).setValue(value);
                break;
            }
            case LegendProperty.HEIGHT: {
                previewProperty = PreviewProperty.createProperty(
                        this,
                        propertyString,
                        Float.class,
                        NbBundle.getMessage(LegendManager.class, "LegendItem.property.height.displayName"),
                        NbBundle.getMessage(LegendManager.class, "LegendItem.property.height.description"),
                        PreviewProperty.CATEGORY_LEGEND_PROPERTY).setValue(value);
                break;
            }
            case LegendProperty.BACKGROUND_IS_DISPLAYING: {
                previewProperty = PreviewProperty.createProperty(
                        this,
                        propertyString,
                        Boolean.class,
                        NbBundle.getMessage(LegendManager.class, "LegendItem.property.background.isDisplaying.displayName"),
                        NbBundle.getMessage(LegendManager.class, "LegendItem.property.background.isDisplaying.description"),
                        PreviewProperty.CATEGORY_LEGEND_PROPERTY).setValue(value);
                break;
            }
            case LegendProperty.BACKGROUND_COLOR: {
                previewProperty = PreviewProperty.createProperty(
                        this,
                        propertyString,
                        Color.class,
                        NbBundle.getMessage(LegendManager.class, "LegendItem.property.background.color.displayName"),
                        NbBundle.getMessage(LegendManager.class, "LegendItem.property.background.color.description"),
                        PreviewProperty.CATEGORY_LEGEND_PROPERTY).setValue(value);
                break;
            }
            case LegendProperty.BORDER_IS_DISPLAYING: {
                previewProperty = PreviewProperty.createProperty(
                        this,
                        propertyString,
                        Boolean.class,
                        NbBundle.getMessage(LegendManager.class, "LegendItem.property.border.isDisplaying.displayName"),
                        NbBundle.getMessage(LegendManager.class, "LegendItem.property.border.isDisplaying.description"),
                        PreviewProperty.CATEGORY_LEGEND_PROPERTY).setValue(value);
                break;
            }
            case LegendProperty.BORDER_COLOR: {
                previewProperty = PreviewProperty.createProperty(
                        this,
                        propertyString,
                        Color.class,
                        NbBundle.getMessage(LegendManager.class, "LegendItem.property.border.color.displayName"),
                        NbBundle.getMessage(LegendManager.class, "LegendItem.property.border.color.description"),
                        PreviewProperty.CATEGORY_LEGEND_PROPERTY).setValue(value);
                break;
            }
            case LegendProperty.BORDER_LINE_THICK: {
                previewProperty = PreviewProperty.createProperty(
                        this,
                        propertyString,
                        Integer.class,
                        NbBundle.getMessage(LegendManager.class, "LegendItem.property.border.lineThick.displayName"),
                        NbBundle.getMessage(LegendManager.class, "LegendItem.property.border.lineThick.description"),
                        PreviewProperty.CATEGORY_LEGEND_PROPERTY).setValue(value);
                break;
            }
            case LegendProperty.TITLE_IS_DISPLAYING: {
                previewProperty = PreviewProperty.createProperty(
                        this,
                        propertyString,
                        Boolean.class,
                        NbBundle.getMessage(LegendManager.class, "LegendItem.property.title.isDisplaying.displayName"),
                        NbBundle.getMessage(LegendManager.class, "LegendItem.property.title.isDisplaying.description"),
                        PreviewProperty.CATEGORY_LEGEND_PROPERTY).setValue(value);
                break;
            }
            case LegendProperty.TITLE: {
                previewProperty = PreviewProperty.createProperty(
                        this,
                        propertyString,
                        String.class,
                        NbBundle.getMessage(LegendManager.class, "LegendItem.property.title.displayName"),
                        NbBundle.getMessage(LegendManager.class, "LegendItem.property.title.description"),
                        PreviewProperty.CATEGORY_LEGEND_PROPERTY).setValue(value);
                break;
            }
            case LegendProperty.TITLE_FONT: {
                previewProperty = PreviewProperty.createProperty(
                        this,
                        propertyString,
                        Font.class,
                        NbBundle.getMessage(LegendManager.class, "LegendItem.property.title.font.displayName"),
                        NbBundle.getMessage(LegendManager.class, "LegendItem.property.title.font.description"),
                        PreviewProperty.CATEGORY_LEGEND_PROPERTY).setValue(value);
                break;
            }
            case LegendProperty.TITLE_FONT_COLOR: {
                previewProperty = PreviewProperty.createProperty(
                        this,
                        propertyString,
                        Color.class,
                        NbBundle.getMessage(LegendManager.class, "LegendItem.property.title.font.color.displayName"),
                        NbBundle.getMessage(LegendManager.class, "LegendItem.property.title.font.color.description"),
                        PreviewProperty.CATEGORY_LEGEND_PROPERTY).setValue(value);
                break;
            }
            case LegendProperty.TITLE_ALIGNMENT: {
                previewProperty = PreviewProperty.createProperty(
                        this,
                        propertyString,
                        Alignment.class,
                        NbBundle.getMessage(LegendManager.class, "LegendItem.property.title.alignment.displayName"),
                        NbBundle.getMessage(LegendManager.class, "LegendItem.property.title.alignment.description"),
                        PreviewProperty.CATEGORY_LEGEND_PROPERTY).setValue(value);
                break;
            }
            case LegendProperty.DESCRIPTION_IS_DISPLAYING: {
                previewProperty = PreviewProperty.createProperty(
                        this,
                        propertyString,
                        Boolean.class,
                        NbBundle.getMessage(LegendManager.class, "LegendItem.property.description.isDisplaying.displayName"),
                        NbBundle.getMessage(LegendManager.class, "LegendItem.property.description.isDisplaying.description"),
                        PreviewProperty.CATEGORY_LEGEND_PROPERTY).setValue(value);
                break;
            }
            case LegendProperty.DESCRIPTION: {
                previewProperty = PreviewProperty.createProperty(
                        this,
                        propertyString,
                        String.class,
                        NbBundle.getMessage(LegendManager.class, "LegendItem.property.description.displayName"),
                        NbBundle.getMessage(LegendManager.class, "LegendItem.property.description.description"),
                        PreviewProperty.CATEGORY_LEGEND_PROPERTY).setValue(value);
                break;
            }
            case LegendProperty.DESCRIPTION_FONT: {
                previewProperty = PreviewProperty.createProperty(
                        this,
                        propertyString,
                        Font.class,
                        NbBundle.getMessage(LegendManager.class, "LegendItem.property.description.font.displayName"),
                        NbBundle.getMessage(LegendManager.class, "LegendItem.property.description.font.description"),
                        PreviewProperty.CATEGORY_LEGEND_PROPERTY).setValue(value);
                break;
            }
            case LegendProperty.DESCRIPTION_FONT_COLOR: {
                previewProperty = PreviewProperty.createProperty(
                        this,
                        propertyString,
                        Color.class,
                        NbBundle.getMessage(LegendManager.class, "LegendItem.property.description.font.color.displayName"),
                        NbBundle.getMessage(LegendManager.class, "LegendItem.property.description.font.color.description"),
                        PreviewProperty.CATEGORY_LEGEND_PROPERTY).setValue(value);
                break;
            }
            case LegendProperty.DESCRIPTION_ALIGNMENT: {
                previewProperty = PreviewProperty.createProperty(
                        this,
                        propertyString,
                        Alignment.class,
                        NbBundle.getMessage(LegendManager.class, "LegendItem.property.description.alignment.displayName"),
                        NbBundle.getMessage(LegendManager.class, "LegendItem.property.description.alignment.description"),
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

    private PreviewProperty[] getLegendProperties(Item item) {

        PreviewProperty[] legendProperties = createLegendProperties(item);
        PreviewProperty[] properties = createLegendOwnProperties(item);
        PreviewProperty[] previewProperties = new PreviewProperty[legendProperties.length + properties.length];
        System.arraycopy(legendProperties, 0, previewProperties, 0, legendProperties.length);
        System.arraycopy(properties, 0, previewProperties, legendProperties.length, properties.length);

        return previewProperties;

    }

    public static boolean updatePreviewProperty(Item item, int numOfProperties) {
        // item index
//        Integer itemIndex = item.getData(LegendItem.ITEM_INDEX);
//        Integer currentNumOfPropertiews = item.getData(DescriptionItem.NUMBER_OF_ITEMS);
//        int currentNumOfProperties = ((PreviewProperty[]) (item.getData(LegendItem.DYNAMIC_PROPERTIES))).length;
        int currentNumOfProperties = ((Integer) (item.getData(LegendItem.NUMBER_OF_DYNAMIC_PROPERTIES))).intValue();
        // number of items didn't change
        if (numOfProperties == currentNumOfProperties) {
            return false;
        } // adding properties
        else if (numOfProperties > currentNumOfProperties) {
            int newProperties = numOfProperties - currentNumOfProperties;

            //bug
            if (item instanceof DescriptionItem) {
                DescriptionItemBuilder.addPreviewProperty(item, currentNumOfProperties, newProperties);
            }
        } // removing properties
        else {
            int removeProperties = currentNumOfProperties - numOfProperties;
            //bug
            if (item instanceof DescriptionItem) {
                DescriptionItemBuilder.removePreviewProperty(item, removeProperties);
            }
        }
        item.setData(LegendItem.NUMBER_OF_DYNAMIC_PROPERTIES, numOfProperties);
        return true;
    }

    @Override
    public String toString() {
        return getTitle();
    }

    /**
     * Provides an user friendly name for the builder. This name will appear in
     * the legend manager UI.
     *
     * @return User friendly builder name, not null
     */
    public abstract String getTitle();

    /**
     * Function that takes the data corresponding to each type of item and uses
     * the writer to save the data in it. It saves the data inside an
     * <code>itemdata</code> node.
     *
     * @param writer the XMLStreamWriter to write to
     * @param item the item to be saved
     * @param previewProperties Current workspace PreviewProperties
     * @throws XMLStreamException
     */
    protected void writeXMLFromData(XMLStreamWriter writer, Item item, PreviewProperties previewProperties) throws XMLStreamException {
    }

    /**
     * Function that takes the specific properties of each item and uses the
     * writer to save the properties in it. It saves the properties inside an
     * <code>itemproperty</code> tag
     *
     * @param writer the XMLStreamWriter to write to
     * @param item the item to be saved
     * @param previewProperties Current workspace PreviewProperties
     * @throws XMLStreamException
     */
    protected abstract void writeXMLFromItemOwnProperties(XMLStreamWriter writer, Item item, PreviewProperties previewProperties) throws XMLStreamException;

    /**
     * Function that takes the dynamic properties (if any) of each item and uses
     * the writer to save the properties in it. It saves the properties inside
     * an
     * <code>dynamicproperty</code> tag
     *
     * @param writer the XMLStreamWriter to write to
     * @param item the item to be saved
     * @param previewProperties Current workspace PreviewProperties
     * @throws XMLStreamException
     */
    protected void writeXMLFromDynamicProperties(XMLStreamWriter writer, Item item, PreviewProperties previewProperties) throws XMLStreamException {
    }

    /**
     * Function that automatically saves a property using its PropertyName and
     * the Value attached to it. Only works if property has a known value type.
     * Known types:
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
            String name = LegendManager.getPropertyFromPreviewProperty(property);
            writer.writeAttribute(XML_NAME, name);
            writer.writeCharacters(text);
            writer.writeEndElement();
        }
    }

    /**
     * Function that takes an item and saves its data, legend properties,
     * specific item properties, dynamic properties and data using the specified
     * writer.
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
     * Function that retrieves the data from an XML reader and converts it to
     * data for each kind of item
     *
     * @param reader the XML reader to read the data from
     * @param item the item where the data would be stored
     * @throws XMLStreamException
     */
    public void readXMLToData(XMLStreamReader reader, Item item) throws XMLStreamException {
    }

    /**
     * Function that retrieves the property (propertyName and value) from an XML
     * reader and converts it to single specific kind of property corresponding
     * to an Item.
     *
     * @param reader the XML reader to read the data from
     * @param item the item where the data would be stores
     * @return a PreviewProperty to be added to the specific Item properties
     * @throws XMLStreamException
     */
    protected abstract PreviewProperty readXMLToSingleOwnProperty(XMLStreamReader reader, Item item) throws XMLStreamException;

    private PreviewProperty readXMLToSingleLegendProperty(XMLStreamReader reader, Item item) throws XMLStreamException {
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
     * Function that takes some value in a String form and converts it to the
     * specified class type
     *
     * @param valueString the value in a String form
     * @param valueClass the class type to convert the value
     * @return
     */
    protected Object readValueFromText(String valueString, Class valueClass) {
        Object value = null;
        if (valueClass.equals(LegendItem.Alignment.class)) {
            value = availableAlignments[Integer.parseInt(valueString)];
        }
        else if (valueClass.equals(LegendItem.Shape.class)) {
            value = availableShapes[Integer.parseInt(valueString)];
        }
        else if (valueClass.equals(LegendItem.Direction.class)) {
            value = availableDirections[Integer.parseInt(valueString)];
        }
        else if (valueClass.equals(Boolean.class)) {
            value = Boolean.parseBoolean(valueString);
        }
        else if (valueClass.equals(Integer.class)) {
            value = Integer.parseInt(valueString);
        }
        else if (valueClass.equals(File.class)) {
            value = new File(valueString);
        }
        else if (valueClass.equals(TableItem.VerticalPosition.class)) {
            value = availableTableVerticalPositions[Integer.parseInt(valueString)];
        }
        else if (valueClass.equals(TableItem.HorizontalPosition.class)) {
            value = availableTableHorizontalPositions[Integer.parseInt(valueString)];
        }
        else if (valueClass.equals(TableItem.VerticalTextDirection.class)) {
            value = availableTableVerticalTextDirections[Integer.parseInt(valueString)];
        }

        return value;
    }

    /**
     * Function that retrieves the properties (propertyName and value) from an
     * XML reader and converts it to list of properties. Normally using the
     * <code>readXMLToSingleOwnProperty</code> for each property.
     *
     * @param reader the XML reader to read the data from
     * @param item the item where the data would be stored
     * @return
     * @throws XMLStreamException
     */
    protected abstract ArrayList<PreviewProperty> readXMLToOwnProperties(XMLStreamReader reader, Item item) throws XMLStreamException;

    /**
     * Function that retrieves the dynamic properties (if any) from an XML
     * reader and converts it to list of properties. Normally using the
     * <code>readXMLToSingleOwnProperty</code> for each property.
     *
     * @param reader the XML reader to read the data from
     * @param item the item where the data would be stored
     * @return
     * @throws XMLStreamException
     */
    protected ArrayList<PreviewProperty> readXMLToDynamicProperties(XMLStreamReader reader, Item item) throws XMLStreamException {
        reader.nextTag();
        return new ArrayList<PreviewProperty>();
    }

    private ArrayList<PreviewProperty> readXMLToLegendProperties(XMLStreamReader reader, Item item) throws XMLStreamException {


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

    /**
     * Function that reads the legend properties, specific item properties,
     * dynamic properties and data and converts it to an Item using the
     * specified reader.
     *
     * @param reader the XML reader to read the data from
     * @param newItemIndex used to create the Item
     * @return
     * @throws XMLStreamException
     */
    public Item readXMLToItem(XMLStreamReader reader, Integer newItemIndex) throws XMLStreamException {


        Item item = createNewLegendItem(null);
        item.setData(LegendItem.ITEM_INDEX, newItemIndex);

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
     * @param propertyValue Known
     * types: <code> LegendItem.Alignment</code>, <code> LegendItem.Shape</code>
     * and <code> LegendItem.Direction</code>
     * @return
     */
    protected String writeValueAsText(Object propertyValue) {

        String text = null;
        if (propertyValue instanceof LegendItem.Alignment) {
            LegendItem.Alignment propertyValueString = (LegendItem.Alignment) propertyValue;
            text = propertyValueString.getValue();
        }
        else if (propertyValue instanceof LegendItem.Direction) {
            LegendItem.Direction propertyValueString = (LegendItem.Direction) propertyValue;
            text = propertyValueString.getValue();
        }
        else if (propertyValue instanceof LegendItem.Shape) {
            LegendItem.Shape propertyValueString = (LegendItem.Shape) propertyValue;
            text = propertyValueString.getValue();
        }
        else if (propertyValue instanceof TableItem.HorizontalPosition) {
            TableItem.HorizontalPosition propertyValueString = (TableItem.HorizontalPosition) propertyValue;
            text = propertyValueString.getValue();
        }
        else if (propertyValue instanceof TableItem.VerticalPosition) {
            TableItem.VerticalPosition propertyValueString = (TableItem.VerticalPosition) propertyValue;
            text = propertyValueString.getValue();
        }
        else if (propertyValue instanceof TableItem.VerticalTextDirection) {
            TableItem.VerticalTextDirection propertyValueString = (TableItem.VerticalTextDirection) propertyValue;
            text = propertyValueString.getValue();
        }
        else if (propertyValue instanceof DescriptionItemElement) {
            DescriptionItemElement propertyValueString = (DescriptionItemElement) propertyValue;
            text = propertyValueString.getValue();
        }
        else {
            text = propertyValue.toString();
        }
        return text;
    }
    // xml 

    protected static final String XML_PROPERTY = "property";
    private static final String XML_LEGEND_TYPE = "legendtype";
    private static final String XML_DYNAMIC_PROPERTY = "dynamicproperty";
    private static final String XML_LEGEND_PROPERTY = "legendproperty";
    protected static final String XML_OWN_PROPERTY = "itemproperty";
    protected static final String XML_NAME = "name";
    private static final String XML_DATA = "itemdata";
    //DEFAULT VALUES 
    // BACKGROUND AND BORDER
    protected Boolean defaultBackgroundIsDisplaying = Boolean.FALSE;
    protected Color defaultBackgroundColor = Color.WHITE;
    protected Boolean defaultBorderIsDisplaying = Boolean.TRUE;
    protected Color defaultBorderColor = Color.BLACK;
    protected Integer defaultBorderLineThick = 15;
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
    protected Boolean defaultTitleIsDisplaying = Boolean.TRUE;
    protected String defaultTitle = "TITLE";
    protected Font defaultTitleFont = new Font("Arial", Font.BOLD, 30);
    protected Alignment defaultTitleAlignment = Alignment.CENTER;
    protected Color defaultTitleFontColor = Color.BLACK;
    // DESCRIPTION
    protected String defaultDescription = "description ... description ...description ...description ...description ...description ...description ...description ...description ...description ...description ...description ...";
    protected Boolean defaultDescriptionIsDisplaying = true;
    protected Color defaultDescriptionFontColor = Color.BLACK;
    protected Alignment defaultDescriptionAlignment = Alignment.LEFT;
    protected Font defaultDescriptionFont = new Font("Arial", Font.PLAIN, 10);
    // default values list
    private ArrayList<Object> defaultValuesArrayList;

    public LegendItemBuilder() {
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
    // table
    private final Object[] availableTableVerticalPositions = {
        TableItem.VerticalPosition.UP,
        TableItem.VerticalPosition.BOTTOM
    };
    private final Object[] availableTableHorizontalPositions = {
        TableItem.HorizontalPosition.RIGHT,
        TableItem.HorizontalPosition.LEFT
    };
    private final Object[] availableTableVerticalTextDirections = {
        TableItem.VerticalTextDirection.VERTICAL,
        TableItem.VerticalTextDirection.HORIZONTAL,
        TableItem.VerticalTextDirection.DIAGONAL
    };
}
