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
import org.gephi.legend.api.CustomLegendItemBuilder;
import org.gephi.legend.api.CustomTextItemBuilder;
import org.gephi.legend.api.renderers.TextItemRenderer;
import org.gephi.legend.items.LegendItem;
import org.gephi.legend.items.LegendItem.Alignment;
import org.gephi.legend.items.TextItem;
import org.gephi.legend.manager.LegendManager;
import org.gephi.legend.properties.TextProperty;
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
    @ServiceProvider(service = ItemBuilder.class, position = 105),
    @ServiceProvider(service = LegendItemBuilder.class, position = 105)
})
public class TextItemBuilder extends LegendItemBuilder {

    @Override
    protected boolean setDefaultValues() {
        return false;
    }

    @Override
    protected boolean isBuilderForItem(Item item) {
        return item instanceof TextItem;
    }

    @Override
    public String getType() {
        return TextItem.LEGEND_TYPE;
    }

    @Override
    public String getTitle() {
        return NbBundle.getMessage(LegendManager.class, "TextItem.name");
    }

    @Override
    public Item createNewLegendItem(Graph graph) {
        return new TextItem(graph);
    }

    @Override
    protected Item buildCustomItem(CustomLegendItemBuilder builder, Graph graph, AttributeModel attributeModel) {
        CustomTextItemBuilder customBuilder = (CustomTextItemBuilder) builder;
        Item item = createNewLegendItem(graph);
        
        // setting default renderer
        item.setData(LegendItem.RENDERER, TextItemRenderer.class);
        return item;
    }

    private PreviewProperty createLegendProperty(Item item, int property, Object value) {
        PreviewProperty previewProperty = null;
        Integer itemIndex = item.getData(LegendItem.ITEM_INDEX);
        String propertyString = LegendManager.getProperty(TextProperty.OWN_PROPERTIES, itemIndex, property);

        switch (property) {
            case TextProperty.TEXT_BODY: {
                previewProperty = PreviewProperty.createProperty(
                        this,
                        propertyString,
                        String.class,
                        NbBundle.getMessage(LegendManager.class, "TextItem.property.body.displayName"),
                        NbBundle.getMessage(LegendManager.class, "TextItem.property.body.description"),
                        PreviewProperty.CATEGORY_LEGEND_PROPERTY).setValue(value);
                break;
            }

            case TextProperty.TEXT_BODY_FONT: {
                previewProperty = PreviewProperty.createProperty(
                        this,
                        propertyString,
                        Font.class,
                        NbBundle.getMessage(LegendManager.class, "TextItem.property.body.font.displayName"),
                        NbBundle.getMessage(LegendManager.class, "TextItem.property.body.font.description"),
                        PreviewProperty.CATEGORY_LEGEND_PROPERTY).setValue(value);
                break;
            }

            case TextProperty.TEXT_BODY_FONT_COLOR: {
                previewProperty = PreviewProperty.createProperty(
                        this,
                        propertyString,
                        Color.class,
                        NbBundle.getMessage(LegendManager.class, "TextItem.property.body.font.color.displayName"),
                        NbBundle.getMessage(LegendManager.class, "TextItem.property.body.font.color.description"),
                        PreviewProperty.CATEGORY_LEGEND_PROPERTY).setValue(value);
                break;
            }

            case TextProperty.TEXT_BODY_FONT_ALIGNMENT: {
                previewProperty = PreviewProperty.createProperty(
                        this,
                        propertyString,
                        Alignment.class,
                        NbBundle.getMessage(LegendManager.class, "TextItem.property.body.alignment.displayName"),
                        NbBundle.getMessage(LegendManager.class, "TextItem.property.body.alignment.description"),
                        PreviewProperty.CATEGORY_LEGEND_PROPERTY).setValue(value);
                break;
            }
        }
        return previewProperty;
    }

    @Override
    protected PreviewProperty[] createLegendOwnProperties(Item item) {

        int[] properties = TextProperty.LIST_OF_PROPERTIES;

        PreviewProperty[] previewProperties = new PreviewProperty[defaultValues.length];
        for (int i = 0; i < defaultValues.length; i++) {
            previewProperties[i] = createLegendProperty(item, properties[i], defaultValues[i]);
        }

        return previewProperties;
    }

    @Override
    protected Boolean hasDynamicProperties() {
        return Boolean.FALSE;
    }

    @Override
    public ArrayList<CustomLegendItemBuilder> getAvailableBuilders() {
        Collection<? extends CustomTextItemBuilder> customBuilders = Lookup.getDefault().lookupAll(CustomTextItemBuilder.class);
        ArrayList<CustomLegendItemBuilder> availableBuilders = new ArrayList<CustomLegendItemBuilder>();
        for (CustomTextItemBuilder customBuilder : customBuilders) {
            availableBuilders.add((CustomLegendItemBuilder) customBuilder);
        }
        return availableBuilders;
    }


    @Override
    protected void writeXMLFromItemOwnProperties(XMLStreamWriter writer, Item item, PreviewProperties previewProperties) throws XMLStreamException {

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
    protected PreviewProperty readXMLToSingleOwnProperty(XMLStreamReader reader, Item item) throws XMLStreamException {
        String propertyName = reader.getAttributeValue(null, XML_NAME);
        String valueString = reader.getElementText();
        int propertyIndex = TextProperty.getInstance().getProperty(propertyName);
        Class valueClass = defaultValues[propertyIndex].getClass();
        Object value = PreviewProperties.readValueFromText(valueString, valueClass);
        if (value == null) {
            value = readValueFromText(valueString, valueClass);
        }
        PreviewProperty property = createLegendProperty(item, propertyIndex, value);
        return property;
    }


    // DEFAULT VALUES
    protected final String defaultBody = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Cras aliquam luctus ligula. Nunc mollis sagittis dui eget congue. Sed et turpis leo, vitae interdum magna. Pellentesque sollicitudin laoreet orci. Donec varius eleifend iaculis. Integer congue tempor nulla ac luctus. Nullam velit massa, convallis ut suscipit eget, auctor non velit. Etiam vitae velit sit amet justo luctus semper. Ut laoreet ullamcorper.";
    protected final Font defaultBodyFont = new Font("Arial", Font.PLAIN, 14);
    protected final Color defaultBodyFontColor = Color.BLUE;
    protected final Alignment defaultBodyFontAlignment = Alignment.JUSTIFIED;
    private final Object[] defaultValues = {
        defaultBody,
        defaultBodyFont,
        defaultBodyFontColor,
        defaultBodyFontAlignment
    };
}
