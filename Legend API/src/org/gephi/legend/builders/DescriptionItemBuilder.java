/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.gephi.legend.builders;

import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import java.util.Collection;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;
import org.gephi.data.attributes.api.AttributeModel;
import org.gephi.graph.api.Graph;
import org.gephi.legend.api.CustomDescriptionItemBuilder;
import org.gephi.legend.api.CustomLegendItemBuilder;
import org.gephi.legend.api.DescriptionItemElementValue;
import org.gephi.legend.api.renderers.DescriptionItemRenderer;
import org.gephi.legend.builders.description.elements.CustomValue;
import org.gephi.legend.items.DescriptionItem;
import org.gephi.legend.items.DescriptionItemElement;
import org.gephi.legend.items.LegendItem;
import org.gephi.legend.items.LegendItem.Alignment;
import org.gephi.legend.manager.LegendManager;
import org.gephi.legend.properties.DescriptionProperty;
import org.gephi.preview.api.Item;
import org.gephi.preview.api.PreviewProperties;
import org.gephi.preview.api.PreviewProperty;
import org.gephi.preview.spi.ItemBuilder;
import org.openide.util.Lookup;
import org.openide.util.NbBundle;
import org.openide.util.lookup.ServiceProvider;
import org.openide.util.lookup.ServiceProviders;

/**
 *
 * @author edubecks
 */
@ServiceProviders(value = {
    @ServiceProvider(service = ItemBuilder.class, position = 100),
    @ServiceProvider(service = LegendItemBuilder.class, position = 100)
})
public class DescriptionItemBuilder extends LegendItemBuilder {

    @Override
    protected boolean setDefaultValues() {
        return false;
    }

    @Override
    public Item createNewLegendItem(Graph graph) {
        return new DescriptionItem(graph);
    }

    @Override
    protected Item buildCustomItem(CustomLegendItemBuilder builder, Graph graph, AttributeModel attributeModel) {
        Item item = createNewLegendItem(graph);
        item.setData(LegendItem.LEGEND_ITEM, item);
        
        // setting default renderer
        item.setData(LegendItem.RENDERER, DescriptionItemRenderer.class);
        return item;
    }

    private PreviewProperty createLegendProperty(Item item, int property, Object value) {
        PreviewProperty previewProperty = null;
        Integer itemIndex = item.getData(LegendItem.ITEM_INDEX);
        String propertyString = LegendManager.getProperty(DescriptionProperty.OWN_PROPERTIES, itemIndex, property);

        switch (property) {
            case DescriptionProperty.DESCRIPTION_KEY_FONT: {
                previewProperty = PreviewProperty.createProperty(
                        this,
                        propertyString,
                        Font.class,
                        NbBundle.getMessage(LegendManager.class, "DescriptionItem.property.key.font.displayName"),
                        NbBundle.getMessage(LegendManager.class, "DescriptionItem.property.key.font.description"),
                        PreviewProperty.CATEGORY_LEGEND_PROPERTY).setValue(value);
                break;
            }

            case DescriptionProperty.DESCRIPTION_KEY_FONT_COLOR: {
                previewProperty = PreviewProperty.createProperty(
                        this,
                        propertyString,
                        Color.class,
                        NbBundle.getMessage(LegendManager.class, "DescriptionItem.property.key.font.color.displayName"),
                        NbBundle.getMessage(LegendManager.class, "DescriptionItem.property.key.font.color.description"),
                        PreviewProperty.CATEGORY_LEGEND_PROPERTY).setValue(value);
                break;
            }

            case DescriptionProperty.DESCRIPTION_KEY_FONT_ALIGNMENT: {
                previewProperty = PreviewProperty.createProperty(
                        this,
                        propertyString,
                        Alignment.class,
                        NbBundle.getMessage(LegendManager.class, "DescriptionItem.property.key.font.alignment.displayName"),
                        NbBundle.getMessage(LegendManager.class, "DescriptionItem.property.key.font.alignment.description"),
                        PreviewProperty.CATEGORY_LEGEND_PROPERTY).setValue(value);
                break;
            }

            case DescriptionProperty.DESCRIPTION_VALUE_FONT: {
                previewProperty = PreviewProperty.createProperty(
                        this,
                        propertyString,
                        Font.class,
                        NbBundle.getMessage(LegendManager.class, "DescriptionItem.property.value.font.displayName"),
                        NbBundle.getMessage(LegendManager.class, "DescriptionItem.property.value.font.description"),
                        PreviewProperty.CATEGORY_LEGEND_PROPERTY).setValue(value);
                break;
            }

            case DescriptionProperty.DESCRIPTION_VALUE_FONT_COLOR: {
                previewProperty = PreviewProperty.createProperty(
                        this,
                        propertyString,
                        Color.class,
                        NbBundle.getMessage(LegendManager.class, "DescriptionItem.property.value.font.color.displayName"),
                        NbBundle.getMessage(LegendManager.class, "DescriptionItem.property.value.font.color.description"),
                        PreviewProperty.CATEGORY_LEGEND_PROPERTY).setValue(value);
                break;
            }

            case DescriptionProperty.DESCRIPTION_VALUE_FONT_ALIGNMENT: {
                previewProperty = PreviewProperty.createProperty(
                        this,
                        propertyString,
                        Alignment.class,
                        NbBundle.getMessage(LegendManager.class, "DescriptionItem.property.value.font.alignment.displayName"),
                        NbBundle.getMessage(LegendManager.class, "DescriptionItem.property.value.font.alignment.description"),
                        PreviewProperty.CATEGORY_LEGEND_PROPERTY).setValue(value);
                break;
            }

            case DescriptionProperty.DESCRIPTION_IS_FLOW_LAYOUT: {
                previewProperty = PreviewProperty.createProperty(
                        this,
                        propertyString,
                        Boolean.class,
                        NbBundle.getMessage(LegendManager.class, "DescriptionItem.property.isFlowLayout.displayName"),
                        NbBundle.getMessage(LegendManager.class, "DescriptionItem.property.isFlowLayout.description"),
                        PreviewProperty.CATEGORY_LEGEND_PROPERTY).setValue(value);
                break;
            }

            case DescriptionProperty.DESCRIPTION_TEMP: {
                previewProperty = PreviewProperty.createProperty(
                        this,
                        propertyString,
                        DescriptionItemElement.class,
                        "temp",
                        "temp",
                        PreviewProperty.CATEGORY_LEGEND_PROPERTY).setValue(value);
                break;
            }

        }

        return previewProperty;
    }

    @Override
    protected PreviewProperty[] createLegendOwnProperties(Item item) {

        int[] properties = DescriptionProperty.LIST_OF_PROPERTIES;

        PreviewProperty[] previewProperties = new PreviewProperty[defaultValues.length];
        for (int i = 0; i < defaultValues.length; i++) {
            previewProperties[i] = createLegendProperty(item, properties[i], defaultValues[i]);
        }

        return previewProperties;

    }


    protected static void removePreviewProperty(Item item, int numOfProperties) {
        // item index
        PreviewProperty[] itemProperties = item.getData(LegendItem.DYNAMIC_PROPERTIES);
        PreviewProperty[] newDescriptionProperties = new PreviewProperty[itemProperties.length - numOfProperties * 2];
        System.arraycopy(itemProperties, 0, newDescriptionProperties, 0, newDescriptionProperties.length);
        //
        item.setData(LegendItem.DYNAMIC_PROPERTIES, newDescriptionProperties);
    }

    protected static void addPreviewProperty(Item item, int begin, int numOfProperties) {
        // item index
        Integer itemIndex = item.getData(LegendItem.ITEM_INDEX);
        PreviewProperty[] itemProperties = item.getData(LegendItem.DYNAMIC_PROPERTIES);


        // creating new PreviewProperty[]
        PreviewProperty[] newDescriptionProperties = new PreviewProperty[numOfProperties * 2];
        for (int i = 0; i < numOfProperties; i++) {

            int dataIndex = begin + i;
            String key = NbBundle.getMessage(LegendManager.class, "DescriptionItem.property.key.displayName") + " " + dataIndex;
            String valueString = (NbBundle.getMessage(LegendManager.class, "DescriptionItem.property.value.displayName") + " " + dataIndex);


            newDescriptionProperties[2 * i] = createDescriptionKeyProperty(item, dataIndex, key);
            newDescriptionProperties[2 * i + 1] = createDescriptionValueProperty(item, dataIndex, valueString);
        }

        // appending
        PreviewProperty[] previewProperties = new PreviewProperty[itemProperties.length + newDescriptionProperties.length];
        System.arraycopy(itemProperties, 0, previewProperties, 0, itemProperties.length);
        System.arraycopy(newDescriptionProperties, 0, previewProperties, itemProperties.length, newDescriptionProperties.length);

        item.setData(LegendItem.DYNAMIC_PROPERTIES, previewProperties);

    }

    private static PreviewProperty createDescriptionKeyProperty(Item item, int dataIndex, String value) {
        Integer itemIndex = item.getData(LegendItem.ITEM_INDEX);
        String propertyString = LegendManager.getDynamicProperty(DescriptionProperty.OWN_PROPERTIES[DescriptionProperty.DESCRIPTION_KEY], itemIndex, dataIndex);

        PreviewProperty property = PreviewProperty.createProperty(
                item,
                propertyString,
                String.class,
                NbBundle.getMessage(LegendManager.class, "DescriptionItem.property.key.displayName") + " " + dataIndex,
                NbBundle.getMessage(LegendManager.class, "DescriptionItem.property.key.description") + " " + dataIndex,
                PreviewProperty.CATEGORY_LEGEND_DYNAMIC_PROPERTY).setValue(value);

        return property;
    }

    private static PreviewProperty createDescriptionValueProperty(Item item, int dataIndex, String valueString) {
        DescriptionItemElementValue customValue = new CustomValue();
        DescriptionItemElement value = new DescriptionItemElement(customValue, valueString);


        Integer itemIndex = item.getData(LegendItem.ITEM_INDEX);
        String propertyString = LegendManager.getDynamicProperty(DescriptionProperty.OWN_PROPERTIES[DescriptionProperty.DESCRIPTION_VALUE], itemIndex, dataIndex);

        PreviewProperty property = PreviewProperty.createProperty(
                item,
                propertyString,
                DescriptionItemElement.class,
                NbBundle.getMessage(LegendManager.class, "DescriptionItem.property.value.displayName") + " " + dataIndex,
                NbBundle.getMessage(LegendManager.class, "DescriptionItem.property.value.description") + " " + dataIndex,
                PreviewProperty.CATEGORY_LEGEND_DYNAMIC_PROPERTY).setValue(value);

        return property;
    }

    @Override
    public String getType() {
        return DescriptionItem.LEGEND_TYPE;
    }

    @Override
    public String getTitle() {
        return NbBundle.getMessage(LegendManager.class, "DescriptionItem.name");
    }


    @Override
    protected boolean isBuilderForItem(Item item) {
        return item instanceof DescriptionItem;
    }

    @Override
    protected Boolean hasDynamicProperties() {
        return Boolean.TRUE;
    }

    @Override
    public ArrayList<CustomLegendItemBuilder> getAvailableBuilders() {
        Collection<? extends CustomDescriptionItemBuilder> customBuilders = Lookup.getDefault().lookupAll(CustomDescriptionItemBuilder.class);
        ArrayList<CustomLegendItemBuilder> availableBuilders = new ArrayList<CustomLegendItemBuilder>();
        for (CustomDescriptionItemBuilder customBuilder : customBuilders) {
            availableBuilders.add((CustomLegendItemBuilder) customBuilder);
        }
        return availableBuilders;
    }

    @Override
    public void writeXMLFromItemOwnProperties(XMLStreamWriter writer, Item item, PreviewProperties previewProperties) throws XMLStreamException {
        PreviewProperty[] ownProperties = item.getData(LegendItem.OWN_PROPERTIES);
        for (PreviewProperty property : ownProperties) {
            writeXMLFromSingleProperty(writer, property, previewProperties);
        }
    }

    @Override
    protected ArrayList<PreviewProperty> readXMLToOwnProperties(XMLStreamReader reader, Item item) throws XMLStreamException {


        ArrayList<PreviewProperty> properties = new ArrayList<PreviewProperty>();

        // own properties
        boolean end = false;
        while (reader.hasNext() && !end) {
            int type = reader.next();

            switch (type) {
                case XMLStreamReader.START_ELEMENT: {
                    PreviewProperty property = readXMLToSingleOwnProperty(reader, item);
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

    @Override
    protected ArrayList<PreviewProperty> readXMLToDynamicProperties(XMLStreamReader reader, Item item) throws XMLStreamException {


        ArrayList<PreviewProperty> properties = new ArrayList<PreviewProperty>();

        // read number of dynamic properties
        reader.next();
        Integer numberOfDynamicProperties = Integer.parseInt(reader.getElementText());
        item.setData(LegendItem.NUMBER_OF_DYNAMIC_PROPERTIES, numberOfDynamicProperties);
        for (int i = 0; i < numberOfDynamicProperties; i++) {
            // reading key
            reader.next();
            String key = reader.getElementText();
            PreviewProperty keyProperty = createDescriptionKeyProperty(item, i, key);
            properties.add(keyProperty);

            // reading value
            reader.next();
            String value = reader.getElementText();
            PreviewProperty valueProperty = createDescriptionValueProperty(item, i, value);
            properties.add(valueProperty);
        }
        
        
        // finish reading
        reader.next();
        return properties;
    }

    @Override
    protected PreviewProperty readXMLToSingleOwnProperty(XMLStreamReader reader, Item item) throws XMLStreamException {
        String propertyName = reader.getAttributeValue(null, XML_NAME);
        String valueString = reader.getElementText();
        int propertyIndex = DescriptionProperty.getInstance().getProperty(propertyName);
        Class valueClass = defaultValues[propertyIndex].getClass();
        Object value = PreviewProperties.readValueFromText(valueString, valueClass);
        if (value == null) {
            value = readValueFromText(valueString, valueClass);
        }
        PreviewProperty property = createLegendProperty(item, propertyIndex, value);
        return property;
    }

    @Override
    protected void writeXMLFromDynamicProperties(XMLStreamWriter writer, Item item, PreviewProperties previewProperties) throws XMLStreamException {
        PreviewProperty[] dynamicProperties = item.getData(LegendItem.DYNAMIC_PROPERTIES);

        writer.writeStartElement(XML_PROPERTY);
        String name = LegendItem.NUMBER_OF_DYNAMIC_PROPERTIES;
        String text = item.getData(LegendItem.NUMBER_OF_DYNAMIC_PROPERTIES).toString();
        writer.writeAttribute(XML_NAME, name);
        writer.writeCharacters(text);
        writer.writeEndElement();

        for (PreviewProperty property : dynamicProperties) {
            writeXMLFromSingleProperty(writer, property, previewProperties);
        }
    }
    
    
    
    private Font defaultKeyFont = new Font("Arial", Font.PLAIN, 13);
    private Color defaultKeyFontColor = Color.RED;
    private Font defaultValueFont = new Font("Arial", Font.BOLD, 16);
    private Color defaultValueFontColor = Color.BLUE;
    private Alignment defaultKeyAlignment = Alignment.LEFT;
    private Alignment defaultValueAlignment = Alignment.LEFT;
    private Boolean defaultIsFlowLayout = true;
    private final Object[] defaultValues = {
        defaultKeyFont,
        defaultKeyFontColor,
        defaultKeyAlignment,
        defaultValueFont,
        defaultValueFontColor,
        defaultValueAlignment
    };

}
