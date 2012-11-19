/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.gephi.legend.plugin.builders;

import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import java.util.Collection;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;
import org.gephi.data.attributes.api.AttributeModel;
import org.gephi.graph.api.Graph;
import org.gephi.legend.api.AbstractLegendItemBuilder;
import org.gephi.legend.api.LegendModel;
import org.gephi.legend.plugin.custombuilders.CustomTextItemBuilder;
import org.gephi.legend.plugin.items.TextItem;
import org.gephi.legend.plugin.properties.TextProperty;
import org.gephi.legend.plugin.renderers.TextItemRenderer;
import org.gephi.legend.spi.CustomLegendItemBuilder;
import org.gephi.legend.spi.LegendItem;
import org.gephi.legend.spi.LegendItem.Alignment;
import org.gephi.legend.spi.LegendItemBuilder;
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
public class TextItemBuilder extends AbstractLegendItemBuilder {

    @Override
    public boolean setDefaultValues() {
        return false;
    }

    @Override
    public boolean isBuilderForItem(Item item) {
        return item instanceof TextItem;
    }

    @Override
    public String getType() {
        return TextItem.LEGEND_TYPE;
    }

    @Override
    public String getTitle() {
        return NbBundle.getMessage(TextItemBuilder.class, "TextItem.name");
    }

    @Override
    public Item createNewLegendItem(Graph graph) {
        return new TextItem(graph);
    }

    @Override
    public Item buildCustomItem(CustomLegendItemBuilder builder, Graph graph, AttributeModel attributeModel) {
        CustomTextItemBuilder customBuilder = (CustomTextItemBuilder) builder;
        Item item = createNewLegendItem(graph);
        
        // setting default renderer
        item.setData(LegendItem.RENDERER, TextItemRenderer.class);
        return item;
    }

    private PreviewProperty createLegendProperty(Item item, int property, Object value) {
        PreviewProperty previewProperty = null;
        Integer itemIndex = item.getData(LegendItem.ITEM_INDEX);
        String propertyString = LegendModel.getProperty(TextProperty.OWN_PROPERTIES, itemIndex, property);

        switch (property) {
            case TextProperty.TEXT_BODY: {
                previewProperty = PreviewProperty.createProperty(
                        this,
                        propertyString,
                        String.class,
                        NbBundle.getMessage(TextItemBuilder.class, "TextItem.property.body.displayName"),
                        NbBundle.getMessage(TextItemBuilder.class, "TextItem.property.body.description"),
                        PreviewProperty.CATEGORY_LEGEND_PROPERTY).setValue(value);
                break;
            }

            case TextProperty.TEXT_BODY_FONT: {
                previewProperty = PreviewProperty.createProperty(
                        this,
                        propertyString,
                        Font.class,
                        NbBundle.getMessage(TextItemBuilder.class, "TextItem.property.body.font.displayName"),
                        NbBundle.getMessage(TextItemBuilder.class, "TextItem.property.body.font.description"),
                        PreviewProperty.CATEGORY_LEGEND_PROPERTY).setValue(value);
                break;
            }

            case TextProperty.TEXT_BODY_FONT_COLOR: {
                previewProperty = PreviewProperty.createProperty(
                        this,
                        propertyString,
                        Color.class,
                        NbBundle.getMessage(TextItemBuilder.class, "TextItem.property.body.font.color.displayName"),
                        NbBundle.getMessage(TextItemBuilder.class, "TextItem.property.body.font.color.description"),
                        PreviewProperty.CATEGORY_LEGEND_PROPERTY).setValue(value);
                break;
            }

            case TextProperty.TEXT_BODY_FONT_ALIGNMENT: {
                previewProperty = PreviewProperty.createProperty(
                        this,
                        propertyString,
                        Alignment.class,
                        NbBundle.getMessage(TextItemBuilder.class, "TextItem.property.body.alignment.displayName"),
                        NbBundle.getMessage(TextItemBuilder.class, "TextItem.property.body.alignment.description"),
                        PreviewProperty.CATEGORY_LEGEND_PROPERTY).setValue(value);
                break;
            }
        }
        return previewProperty;
    }

    @Override
    public PreviewProperty[] createLegendOwnProperties(Item item) {

        int[] properties = TextProperty.LIST_OF_PROPERTIES;

        PreviewProperty[] previewProperties = new PreviewProperty[defaultValues.length];
        for (int i = 0; i < defaultValues.length; i++) {
            previewProperties[i] = createLegendProperty(item, properties[i], defaultValues[i]);
        }

        return previewProperties;
    }

    @Override
    public Boolean hasDynamicProperties() {
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
    public void writeXMLFromItemOwnProperties(XMLStreamWriter writer, Item item, PreviewProperties previewProperties) throws XMLStreamException {

        PreviewProperty[] ownProperties = item.getData(LegendItem.OWN_PROPERTIES);
        for (PreviewProperty property : ownProperties) {
            writeXMLFromSingleProperty(writer, property, previewProperties);
        }

    }

    @Override
    public ArrayList<PreviewProperty> readXMLToOwnProperties(XMLStreamReader reader, Item item) throws XMLStreamException {

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

    public PreviewProperty readXMLToSingleOwnProperty(XMLStreamReader reader, Item item) throws XMLStreamException {
        String propertyName = reader.getAttributeValue(null, XML_NAME);
        String valueString = reader.getElementText();
        int propertyIndex = TextProperty.getInstance().getProperty(propertyName);
        Class valueClass = defaultValues[propertyIndex].getClass();
        Object value = readValueFromText(valueString, valueClass);
        PreviewProperty property = createLegendProperty(item, propertyIndex, value);
        return property;
    }


    // DEFAULT VALUES
    protected final String defaultBody = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Cras aliquam luctus ligula. Nunc mollis sagittis dui eget congue. Sed et turpis leo, vitae interdum magna. Pellentesque sollicitudin laoreet orci. Donec varius eleifend iaculis. Integer congue tempor nulla ac luctus. Nullam velit massa, convallis ut suscipit eget, auctor non velit. Etiam vitae velit sit amet justo luctus semper. Ut laoreet ullamcorper.";
    protected final Font defaultBodyFont = new Font("Arial", Font.PLAIN, 14);
    protected final Color defaultBodyFontColor = Color.BLACK;
    protected final Alignment defaultBodyFontAlignment = Alignment.LEFT;
    private final Object[] defaultValues = {
        defaultBody,
        defaultBodyFont,
        defaultBodyFontColor,
        defaultBodyFontAlignment
    };
}
