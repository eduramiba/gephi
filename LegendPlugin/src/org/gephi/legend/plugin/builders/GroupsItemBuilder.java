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
import org.gephi.legend.plugin.custombuilders.CustomGroupsItemBuilder;
import org.gephi.legend.plugin.items.GroupsItem;
import org.gephi.legend.plugin.properties.GroupsProperty;
import org.gephi.legend.plugin.renderers.GroupsItemRenderer;
import org.gephi.legend.spi.CustomLegendItemBuilder;
import org.gephi.legend.spi.LegendItem;
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
    @ServiceProvider(service = ItemBuilder.class, position = 101),
    @ServiceProvider(service = LegendItemBuilder.class, position = 101)
})
public class GroupsItemBuilder extends AbstractLegendItemBuilder {

    @Override
    public boolean setDefaultValues() {
        return false;
    }

    @Override
    public boolean isBuilderForItem(Item item) {
        return item instanceof GroupsItem;
    }

    @Override
    public String getType() {
        return GroupsItem.LEGEND_TYPE;
    }

    @Override
    public String getTitle() {
        return NbBundle.getMessage(GroupsItemBuilder.class, "GroupsItem.name");
    }

    private PreviewProperty createLegendProperty(Item item, int property, Object value) {
        PreviewProperty previewProperty = null;
        Integer itemIndex = item.getData(LegendItem.ITEM_INDEX);
        String propertyString = LegendModel.getProperty(GroupsProperty.OWN_PROPERTIES, itemIndex, property);

        switch (property) {


            case GroupsProperty.GROUPS_NUMBER_COLUMNS: {
                previewProperty = PreviewProperty.createProperty(
                        this,
                        propertyString,
                        Integer.class,
                        NbBundle.getMessage(GroupsItemBuilder.class, "GroupsItem.property.numColumns.displayName"),
                        NbBundle.getMessage(GroupsItemBuilder.class, "GroupsItem.property.numColumns.description"),
                        PreviewProperty.CATEGORY_LEGEND_PROPERTY).setValue(value);
                break;
            }

            case GroupsProperty.GROUPS_LABEL_POSITION: {
                previewProperty = PreviewProperty.createProperty(
                        this,
                        propertyString,
                        LegendItem.Direction.class,
                        NbBundle.getMessage(GroupsItemBuilder.class, "GroupsItem.property.label.position.displayName"),
                        NbBundle.getMessage(GroupsItemBuilder.class, "GroupsItem.property.label.position.description"),
                        PreviewProperty.CATEGORY_LEGEND_PROPERTY).setValue(value);
                break;
            }

            case GroupsProperty.GROUPS_LABEL_FONT: {
                previewProperty = PreviewProperty.createProperty(
                        this,
                        propertyString,
                        Font.class,
                        NbBundle.getMessage(GroupsItemBuilder.class, "GroupsItem.property.label.font.displayName"),
                        NbBundle.getMessage(GroupsItemBuilder.class, "GroupsItem.property.label.font.description"),
                        PreviewProperty.CATEGORY_LEGEND_PROPERTY).setValue(value);
                break;
            }

            case GroupsProperty.GROUPS_LABEL_FONT_COLOR: {
                previewProperty = PreviewProperty.createProperty(
                        this,
                        propertyString,
                        Color.class,
                        NbBundle.getMessage(GroupsItemBuilder.class, "GroupsItem.property.label.font.color.displayName"),
                        NbBundle.getMessage(GroupsItemBuilder.class, "GroupsItem.property.label.font.color.description"),
                        PreviewProperty.CATEGORY_LEGEND_PROPERTY).setValue(value);
                break;
            }

            case GroupsProperty.GROUPS_SHAPE: {
                previewProperty = PreviewProperty.createProperty(
                        this,
                        propertyString,
                        LegendItem.Shape.class,
                        NbBundle.getMessage(GroupsItemBuilder.class, "GroupsItem.property.shape.displayName"),
                        NbBundle.getMessage(GroupsItemBuilder.class, "GroupsItem.property.shape.description"),
                        PreviewProperty.CATEGORY_LEGEND_PROPERTY).setValue(value);
                break;
            }

            case GroupsProperty.GROUPS_PADDING_BETWEEN_TEXT_AND_SHAPE: {
                previewProperty = PreviewProperty.createProperty(
                        this,
                        propertyString,
                        Integer.class,
                        NbBundle.getMessage(GroupsItemBuilder.class, "GroupsItem.property.paddingBetweenTextAndShape.displayName"),
                        NbBundle.getMessage(GroupsItemBuilder.class, "GroupsItem.property.paddingBetweenTextAndShape.description"),
                        PreviewProperty.CATEGORY_LEGEND_PROPERTY).setValue(value);
                break;
            }

            case GroupsProperty.GROUPS_PADDING_BETWEEN_ELEMENTS: {
                previewProperty = PreviewProperty.createProperty(
                        this,
                        propertyString,
                        Integer.class,
                        NbBundle.getMessage(GroupsItemBuilder.class, "GroupsItem.property.paddingBetweenElements.displayName"),
                        NbBundle.getMessage(GroupsItemBuilder.class, "GroupsItem.property.paddingBetweenElements.description"),
                        PreviewProperty.CATEGORY_LEGEND_PROPERTY).setValue(value);
                break;
            }

            case GroupsProperty.GROUPS_SCALE_SHAPE: {
                previewProperty = PreviewProperty.createProperty(
                        this,
                        propertyString,
                        Boolean.class,
                        NbBundle.getMessage(GroupsItemBuilder.class, "GroupsItem.property.scaleShape.displayName"),
                        NbBundle.getMessage(GroupsItemBuilder.class, "GroupsItem.property.scaleShape.description"),
                        PreviewProperty.CATEGORY_LEGEND_PROPERTY).setValue(value);
                break;
            }


        }

        return previewProperty;
    }

    private PreviewProperty createLegendLabelProperty(Item item, int numberOfLabel, Object value) {

        Integer itemIndex = item.getData(LegendItem.ITEM_INDEX);
        String propertyString = GroupsProperty.getLabelProperty(itemIndex, numberOfLabel);

        PreviewProperty property = PreviewProperty.createProperty(
                this,
                propertyString,
                String.class,
                NbBundle.getMessage(GroupsItemBuilder.class, "GroupsItem.property.labels.displayName") + " " + numberOfLabel,
                NbBundle.getMessage(GroupsItemBuilder.class, "GroupsItem.property.labels.description") + " " + numberOfLabel,
                PreviewProperty.CATEGORY_LEGEND_PROPERTY).setValue(value);



        return property;
    }

    @Override
    public PreviewProperty[] createLegendOwnProperties(Item item) {


        // creating one property for each label
        ArrayList<StringBuilder> labelsGroup = item.getData(GroupsItem.LABELS_IDS);
        PreviewProperty[] labelProperties = new PreviewProperty[labelsGroup.size()];
        for (int i = 0; i < labelProperties.length; i++) {
            labelProperties[i] = createLegendLabelProperty(item, i, labelsGroup.get(i).toString());
        }

        int[] properties = GroupsProperty.LIST_OF_PROPERTIES;

        PreviewProperty[] previewProperties = new PreviewProperty[defaultValues.length];
        for (int i = 0; i < defaultValues.length; i++) {
            previewProperties[i] = createLegendProperty(item, properties[i], defaultValues[i]);
        }


        PreviewProperty[] propertiesWithLabels = new PreviewProperty[labelProperties.length + previewProperties.length];
        System.arraycopy(labelProperties, 0, propertiesWithLabels, 0, labelProperties.length);
        System.arraycopy(previewProperties, 0, propertiesWithLabels, labelProperties.length, previewProperties.length);
        return propertiesWithLabels;

    }

    @Override
    public Boolean hasDynamicProperties() {
        return Boolean.FALSE;
    }

    @Override
    public Item buildCustomItem(CustomLegendItemBuilder builder, Graph graph, AttributeModel attributeModel) {

        Item item = createNewLegendItem(graph);


        ArrayList<Color> colors = new ArrayList<Color>();
        ArrayList<Float> values = new ArrayList<Float>();

        CustomGroupsItemBuilder customGroupsBuilder = (CustomGroupsItemBuilder) builder;
        ArrayList<String> labels = new ArrayList<String>();
        customGroupsBuilder.retrieveData(labels, colors, values);

        ArrayList<StringBuilder> labelsStringBuilder = new ArrayList<StringBuilder>();
        for (String label : labels) {
            labelsStringBuilder.add(new StringBuilder(label));
        }
        
        
        item.setData(GroupsItem.NUMBER_OF_GROUPS, labels.size());
        item.setData(GroupsItem.COLORS, colors);
        item.setData(GroupsItem.LABELS_IDS, labelsStringBuilder);
        item.setData(GroupsItem.VALUES, values);
        
        // setting default renderer
        item.setData(LegendItem.RENDERER, GroupsItemRenderer.class);
        return item;

    }

    @Override
    public ArrayList<CustomLegendItemBuilder> getAvailableBuilders() {
        Collection<? extends CustomGroupsItemBuilder> customBuilders = Lookup.getDefault().lookupAll(CustomGroupsItemBuilder.class);
        ArrayList<CustomLegendItemBuilder> availableBuilders = new ArrayList<CustomLegendItemBuilder>();
        for (CustomGroupsItemBuilder customBuilder : customBuilders) {
            availableBuilders.add((CustomLegendItemBuilder) customBuilder);
        }
        return availableBuilders;
    }

    @Override
    public void writeXMLFromData(XMLStreamWriter writer, Item item, PreviewProperties previewProperties) throws XMLStreamException {

        String name = null;
        String text = null;

        // write number of items
        Integer numberOfGroups = item.getData(GroupsItem.NUMBER_OF_GROUPS);
        text = numberOfGroups.toString();
        name = GroupsItem.NUMBER_OF_GROUPS;
        writer.writeStartElement(LegendItem.DATA);
        writer.writeAttribute(XML_NAME, name);
        writer.writeCharacters(text);
        writer.writeEndElement();

        // writing labels
        ArrayList<StringBuilder> labels = item.getData(GroupsItem.LABELS_IDS);
        text = labels.toString();
        name = GroupsItem.LABELS_IDS;
        writer.writeStartElement(LegendItem.DATA);
        writer.writeAttribute(XML_NAME, name);
        writer.writeCharacters(text);
        writer.writeEndElement();


        // writing colors
        ArrayList<Color> colors = item.getData(GroupsItem.COLORS);
        name = GroupsItem.COLORS;
        ArrayList<Integer> colorsIntegerArrayList = new ArrayList<Integer>();
        for (Color color : colors) {
            colorsIntegerArrayList.add(color.getRGB());
        }
        text = colorsIntegerArrayList.toString();
        writer.writeStartElement(LegendItem.DATA);
        writer.writeAttribute(XML_NAME, name);
        writer.writeCharacters(text);
        writer.writeEndElement();

        // writing values
        ArrayList<Float> values = item.getData(GroupsItem.VALUES);
        text = values.toString();
        name = GroupsItem.VALUES;
        writer.writeStartElement(LegendItem.DATA);
        writer.writeAttribute(XML_NAME, name);
        writer.writeCharacters(text);
        writer.writeEndElement();

    }

    @Override
    public void readXMLToData(XMLStreamReader reader, Item item) throws XMLStreamException {
        // read number of items
        reader.next();
        Integer numberOfItems = Integer.parseInt(reader.getElementText());

        // reading labels
        reader.next();
        String labelsString = reader.getElementText();
        String[] labelsArray = labelsString.replace("[", "").replace("]", "").split(", ");
        ArrayList<StringBuilder> labels = new ArrayList<StringBuilder>();
        for (int i = 0; i < numberOfItems; i++) {
            labels.add(new StringBuilder(labelsArray[i]));
        }

        // reading colors
        reader.next();
        String colorsString = reader.getElementText();
        String[] colorsArray = colorsString.replace("[", "").replace("]", "").split(", ");
        ArrayList<Color> colors = new ArrayList<Color>();
        for (int i = 0; i < numberOfItems; i++) {
            colors.add(new Color(Integer.parseInt(colorsArray[i])));
        }


        // reading values
        reader.next();
        String valuesString = reader.getElementText();
        String[] valuesArray = valuesString.replace("[", "").replace("]", "").split(", ");
        ArrayList<Float> values = new ArrayList<Float>();
        for (int i = 0; i < numberOfItems; i++) {
            values.add(Float.parseFloat(valuesArray[i]));
        }



        item.setData(GroupsItem.NUMBER_OF_GROUPS, labels.size());
        item.setData(GroupsItem.COLORS, colors);
        item.setData(GroupsItem.VALUES, values);


    }

    @Override
    public Item createNewLegendItem(Graph graph) {
        return new GroupsItem(graph);
    }

    @Override
    public void writeXMLFromItemOwnProperties(XMLStreamWriter writer, Item item, PreviewProperties previewProperties) throws XMLStreamException {

        // write number of items
        Integer numberOfGroups = item.getData(GroupsItem.NUMBER_OF_GROUPS);
        String text = numberOfGroups.toString();
        String name = GroupsItem.NUMBER_OF_GROUPS;
        writer.writeStartElement(LegendItem.DATA);
        writer.writeAttribute(XML_NAME, name);
        writer.writeCharacters(text);
        writer.writeEndElement();

        PreviewProperty[] ownProperties = item.getData(LegendItem.OWN_PROPERTIES);
        for (PreviewProperty property : ownProperties) {
            writeXMLFromSingleProperty(writer, property, previewProperties);
        }
    }

    @Override
    public ArrayList<PreviewProperty> readXMLToOwnProperties(XMLStreamReader reader, Item item) throws XMLStreamException {

        ArrayList<PreviewProperty> properties = new ArrayList<PreviewProperty>();

        // read number of items
        reader.next();
        Integer numberOfItems = Integer.parseInt(reader.getElementText());

        // reading labels
        ArrayList<StringBuilder> labels = new ArrayList<StringBuilder>();
        for (int i = 0; i < numberOfItems; i++) {
            reader.next();
            StringBuilder valueStringBuilder = new StringBuilder(reader.getElementText());
            labels.add(valueStringBuilder);
            PreviewProperty property = createLegendLabelProperty(item, i, valueStringBuilder.toString());
            properties.add(property);
        }
        item.setData(GroupsItem.LABELS_IDS, labels);

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
        int propertyIndex = GroupsProperty.getInstance().getProperty(propertyName);
        Class valueClass = defaultValues[propertyIndex].getClass();
        Object value = readValueFromText(valueString, valueClass);
        PreviewProperty property = createLegendProperty(item, propertyIndex, value);
        return property;
    }

    // DEFAULT PROPERTIES
    private Integer defaultNumColumns = 5;
    private LegendItem.Direction defaultLabelPosition = LegendItem.Direction.DOWN;
    private Color defaultLabelFontColor = Color.BLACK;
    private Font defaultLabelFont = new Font("Arial", Font.PLAIN, 15);
    private Integer defaultPaddingBetweenTextAndShape = 5;
    private Integer defaultPaddingBetweenElements = 5;
    private LegendItem.Shape defaultShape = LegendItem.Shape.RECTANGLE;
    private Boolean defaultIsScalingShapes = Boolean.TRUE;
    private final Object[] defaultValues = {
        defaultNumColumns,
        defaultShape,
        defaultIsScalingShapes,
        defaultLabelPosition,
        defaultLabelFont,
        defaultLabelFontColor,
        defaultPaddingBetweenTextAndShape,
        defaultPaddingBetweenElements
    };
}
