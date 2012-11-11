/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.gephi.legend.plugin.builders;

/**
 *
 * @author edubecks
 */
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
import org.gephi.legend.plugin.custombuilders.CustomTableItemBuilder;
import org.gephi.legend.plugin.items.TableItem;
import org.gephi.legend.plugin.items.TableItem.HorizontalPosition;
import org.gephi.legend.plugin.items.TableItem.VerticalPosition;
import org.gephi.legend.plugin.properties.TableProperty;
import org.gephi.legend.plugin.renderers.TableItemRenderer;
import org.gephi.legend.spi.CustomLegendItemBuilder;
import org.gephi.legend.spi.LegendItem;
import org.gephi.legend.spi.LegendItem.Alignment;
import org.gephi.legend.spi.LegendItem.Direction;
import org.gephi.legend.spi.LegendItemBuilder;
import org.gephi.preview.api.Item;
import org.gephi.preview.api.PreviewProperties;
import org.gephi.preview.api.PreviewProperty;
import org.gephi.preview.spi.ItemBuilder;
import org.openide.util.Lookup;
import org.openide.util.NbBundle;
import org.openide.util.lookup.ServiceProvider;
import org.openide.util.lookup.ServiceProviders;

@ServiceProviders(value = {
    @ServiceProvider(service = ItemBuilder.class, position = 104),
    @ServiceProvider(service = LegendItemBuilder.class, position = 104)
})
public class TableItemBuilder extends AbstractLegendItemBuilder {

    @Override
    public boolean setDefaultValues() {
        defaultWidth = 600f;
        defaultHeight = 500f;
        return false;
    }

    @Override
    public boolean isBuilderForItem(Item item) {
        return item instanceof TableItem;
    }

    @Override
    public String getType() {
        return TableItem.LEGEND_TYPE;
    }

    @Override
    public String getTitle() {
        return NbBundle.getMessage(TableItemBuilder.class, "TableItem.name");
    }

    @Override
    public Item buildCustomItem(CustomLegendItemBuilder builder,
                                   Graph graph,
                                   AttributeModel attributeModel) {


        Item item = createNewLegendItem(graph);

        // labels
        ArrayList<String> verticalLabels = new ArrayList<String>();
        ArrayList<String> horizontalLabels = new ArrayList<String>();
        ArrayList<TableItem.LabelSelection> labelsSelectionArrayList = new ArrayList<TableItem.LabelSelection>();
        // values
        ArrayList<ArrayList<String>> values = new ArrayList<ArrayList<String>>();
        // colors
        ArrayList<Color> horizontalColors = new ArrayList<Color>();
        ArrayList<Color> verticalColors = new ArrayList<Color>();
        ArrayList<ArrayList<Color>> valueColors = new ArrayList<ArrayList<Color>>();

        // retrieving data
        CustomTableItemBuilder customTableBuilder = (CustomTableItemBuilder) builder;
        customTableBuilder.retrieveData(
                labelsSelectionArrayList,
                horizontalLabels,
                verticalLabels,
                values,
                horizontalColors,
                verticalColors,
                valueColors);

        TableItem.LabelSelection labelSelection = labelsSelectionArrayList.get(0);
        ArrayList<StringBuilder> verticalLabelsStringBuilder = new ArrayList<StringBuilder>();
        ArrayList<StringBuilder> horizontalLabelsStringBuilder = new ArrayList<StringBuilder>();
        ArrayList<StringBuilder> labels = new ArrayList<StringBuilder>();
        if (labelSelection == TableItem.LabelSelection.BOTH) {
            for (String label : verticalLabels) {
                StringBuilder labelStringBuilder = new StringBuilder(label);
                verticalLabelsStringBuilder.add(labelStringBuilder);
                horizontalLabelsStringBuilder.add(labelStringBuilder);
                labels.add(labelStringBuilder);
            }
        }
        else if (labelSelection == TableItem.LabelSelection.HORIZONTAL) {
            for (String label : horizontalLabels) {
                StringBuilder labelStringBuilder = new StringBuilder(label);
                horizontalLabelsStringBuilder.add(labelStringBuilder);
                labels.add(labelStringBuilder);
            }
            for (String label : verticalLabels) {
                StringBuilder labelStringBuilder = new StringBuilder(label);
                verticalLabelsStringBuilder.add(labelStringBuilder);
            }
        }
        else if (labelSelection == TableItem.LabelSelection.VERTICAL) {
            for (String label : horizontalLabels) {
                StringBuilder labelStringBuilder = new StringBuilder(label);
                horizontalLabelsStringBuilder.add(labelStringBuilder);
            }
            for (String label : verticalLabels) {
                StringBuilder labelStringBuilder = new StringBuilder(label);
                verticalLabelsStringBuilder.add(labelStringBuilder);
                labels.add(labelStringBuilder);
            }
        }



        Integer numberOfRows = values.size();
        Integer numberOfColumns = values.get(0).size();





//        ArrayList<StringBuilder> labels = (labelSelection == TableItem.LabelSelection.HORIZONTAL) ? horizontalLabelsStringBuilder : verticalLabelsStringBuilder;


        // setting data
        item.setData(TableItem.NUMBER_OF_ROWS, numberOfRows);
        item.setData(TableItem.NUMBER_OF_COLUMNS, numberOfColumns);
        item.setData(TableItem.HORIZONTAL_LABELS, horizontalLabelsStringBuilder);
        item.setData(TableItem.VERTICAL_LABELS, verticalLabelsStringBuilder);
        item.setData(TableItem.LABELS_SELECTION, labelSelection);
        item.setData(TableItem.LABELS_IDS, labels);
        item.setData(TableItem.TABLE_VALUES, values);
        item.setData(TableItem.COLOR_VALUES, valueColors);
        item.setData(TableItem.COLOR_HORIZONTAL, horizontalColors);
        item.setData(TableItem.COLOR_VERTICAL, verticalColors);

        // setting default renderer
        item.setData(LegendItem.RENDERER, TableItemRenderer.class);
        return item;

    }

    private PreviewProperty createLegendProperty(Item item, int property, Object value) {
        PreviewProperty previewProperty = null;
        Integer itemIndex = item.getData(LegendItem.ITEM_INDEX);
        String propertyString = LegendModel.getProperty(
                TableProperty.OWN_PROPERTIES,
                itemIndex,
                property);

        switch (property) {
            case TableProperty.TABLE_VERTICAL_EXTRA_MARGIN: {
                previewProperty = PreviewProperty.createProperty(
                        this,
                        propertyString,
                        Integer.class,
                        NbBundle.getMessage(TableItemBuilder.class, "TableItem.property.vertical.extraMargin.displayName"),
                        NbBundle.getMessage(TableItemBuilder.class, "TableItem.property.vertical.extraMargin.description"),
                        PreviewProperty.CATEGORY_LEGEND_PROPERTY).setValue(value);
                break;
            }

            case TableProperty.TABLE_HORIZONTAL_EXTRA_MARGIN: {
                previewProperty = PreviewProperty.createProperty(
                        this,
                        propertyString,
                        Integer.class,
                        NbBundle.getMessage(TableItemBuilder.class, "TableItem.property.vertical.extraMargin.displayName"),
                        NbBundle.getMessage(TableItemBuilder.class, "TableItem.property.vertical.extraMargin.description"),
                        PreviewProperty.CATEGORY_LEGEND_PROPERTY).setValue(value);
                break;
            }

            case TableProperty.TABLE_FONT: {
                previewProperty = PreviewProperty.createProperty(
                        this,
                        propertyString,
                        Font.class,
                        NbBundle.getMessage(TableItemBuilder.class, "TableItem.property.font.displayName"),
                        NbBundle.getMessage(TableItemBuilder.class, "TableItem.property.font.description"),
                        PreviewProperty.CATEGORY_LEGEND_PROPERTY).setValue(value);
                break;
            }

            case TableProperty.TABLE_FONT_COLOR: {
                previewProperty = PreviewProperty.createProperty(
                        this,
                        propertyString,
                        Color.class,
                        NbBundle.getMessage(TableItemBuilder.class, "TableItem.property.font.color.displayName"),
                        NbBundle.getMessage(TableItemBuilder.class, "TableItem.property.font.color.description"),
                        PreviewProperty.CATEGORY_LEGEND_PROPERTY).setValue(value);
                break;
            }

            case TableProperty.TABLE_IS_CELL_COLORING: {
                previewProperty = PreviewProperty.createProperty(
                        this,
                        propertyString,
                        Boolean.class,
                        NbBundle.getMessage(TableItemBuilder.class, "TableItem.property.isCellColoring.displayName"),
                        NbBundle.getMessage(TableItemBuilder.class, "TableItem.property.isCellColoring.description"),
                        PreviewProperty.CATEGORY_LEGEND_PROPERTY).setValue(value);
                break;
            }

            case TableProperty.TABLE_HORIZONTAL_TEXT_ALIGNMENT: {
                previewProperty = PreviewProperty.createProperty(
                        this,
                        propertyString,
                        Alignment.class,
                        NbBundle.getMessage(TableItemBuilder.class, "TableItem.property.horizontalText.alignment.displayName"),
                        NbBundle.getMessage(TableItemBuilder.class, "TableItem.property.horizontalText.alignment.description"),
                        PreviewProperty.CATEGORY_LEGEND_PROPERTY).setValue(value);
                break;
            }

            case TableProperty.TABLE_HORIZONTAL_TEXT_POSITION: {
                previewProperty = PreviewProperty.createProperty(
                        this,
                        propertyString,
                        HorizontalPosition.class,
                        NbBundle.getMessage(TableItemBuilder.class, "TableItem.property.horizontalText.position.displayName"),
                        NbBundle.getMessage(TableItemBuilder.class, "TableItem.property.horizontalText.position.description"),
                        PreviewProperty.CATEGORY_LEGEND_PROPERTY).setValue(value);
                break;
            }

            case TableProperty.TABLE_VERTICAL_TEXT_ALIGNMENT: {
                previewProperty = PreviewProperty.createProperty(
                        this,
                        propertyString,
                        Alignment.class,
                        NbBundle.getMessage(TableItemBuilder.class, "TableItem.property.verticalText.alignment.displayName"),
                        NbBundle.getMessage(TableItemBuilder.class, "TableItem.property.verticalText.alignment.description"),
                        PreviewProperty.CATEGORY_LEGEND_PROPERTY).setValue(value);
                break;
            }

            case TableProperty.TABLE_VERTICAL_TEXT_POSITION: {
                previewProperty = PreviewProperty.createProperty(
                        this,
                        propertyString,
                        VerticalPosition.class,
                        NbBundle.getMessage(TableItemBuilder.class, "TableItem.property.verticalText.position.displayName"),
                        NbBundle.getMessage(TableItemBuilder.class, "TableItem.property.verticalText.position.description"),
                        PreviewProperty.CATEGORY_LEGEND_PROPERTY).setValue(value);
                break;
            }

            case TableProperty.TABLE_VERTICAL_TEXT_ROTATION: {
                previewProperty = PreviewProperty.createProperty(
                        this,
                        propertyString,
                        TableItem.VerticalTextDirection.class,
                        NbBundle.getMessage(TableItemBuilder.class, "TableItem.property.verticalText.rotation.displayName"),
                        NbBundle.getMessage(TableItemBuilder.class, "TableItem.property.verticalText.rotation.description"),
                        PreviewProperty.CATEGORY_LEGEND_PROPERTY).setValue(value);
                break;
            }

            case TableProperty.TABLE_IS_DISPLAYING_GRID: {
                previewProperty = PreviewProperty.createProperty(
                        this,
                        propertyString,
                        Boolean.class,
                        NbBundle.getMessage(TableItemBuilder.class, "TableItem.property.grid.isDisplaying.displayName"),
                        NbBundle.getMessage(TableItemBuilder.class, "TableItem.property.grid.isDisplaying.description"),
                        PreviewProperty.CATEGORY_LEGEND_PROPERTY).setValue(value);
                break;
            }

            case TableProperty.TABLE_GRID_COLOR: {
                previewProperty = PreviewProperty.createProperty(
                        this,
                        propertyString,
                        Color.class,
                        NbBundle.getMessage(TableItemBuilder.class, "TableItem.property.grid.color.displayName"),
                        NbBundle.getMessage(TableItemBuilder.class, "TableItem.property.grid.color.description"),
                        PreviewProperty.CATEGORY_LEGEND_PROPERTY).setValue(value);
                break;
            }

        }

        return previewProperty;
    }

    private PreviewProperty createLegendLabelProperty(Item item, int numberOfLabel, Object value) {

        Integer itemIndex = item.getData(LegendItem.ITEM_INDEX);
        String propertyString = TableProperty.getLabelProperty(itemIndex, numberOfLabel);

        PreviewProperty property = PreviewProperty.createProperty(
                this,
                propertyString,
                String.class,
                NbBundle.getMessage(TableItemBuilder.class, "TableItem.property.labels.displayName") + " " + numberOfLabel,
                NbBundle.getMessage(TableItemBuilder.class, "TableItem.property.labels.description") + " " + numberOfLabel,
                PreviewProperty.CATEGORY_LEGEND_PROPERTY).setValue(value);

        return property;
    }

    @Override
    public PreviewProperty[] createLegendOwnProperties(Item item) {



        ArrayList<StringBuilder> labelsGroup = item.getData(TableItem.LABELS_IDS);
        item.setData(TableItem.NUMBER_OF_LABELS, labelsGroup.size());
        PreviewProperty[] labelProperties = new PreviewProperty[labelsGroup.size()];
        for (int i = 0; i < labelProperties.length; i++) {
            labelProperties[i] = createLegendLabelProperty(item, i, labelsGroup.get(i).toString());
        }


        int[] properties = TableProperty.LIST_OF_PROPERTIES;

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
    public ArrayList<CustomLegendItemBuilder> getAvailableBuilders() {
        Collection<? extends CustomTableItemBuilder> customBuilders = Lookup.getDefault().lookupAll(CustomTableItemBuilder.class);
        ArrayList<CustomLegendItemBuilder> availableBuilders = new ArrayList<CustomLegendItemBuilder>();
        for (CustomTableItemBuilder customBuilder : customBuilders) {
            availableBuilders.add((CustomLegendItemBuilder) customBuilder);
        }
        return availableBuilders;
    }

    @Override
    public void writeXMLFromData(XMLStreamWriter writer, Item item, PreviewProperties previewProperties) throws XMLStreamException {

        String name = null;
        String text = null;

        // number of rows
        name = TableItem.NUMBER_OF_ROWS;
        text = item.getData(TableItem.NUMBER_OF_ROWS).toString();
        writer.writeStartElement(LegendItem.DATA);
        writer.writeAttribute(XML_NAME, name);
        writer.writeCharacters(text);
        writer.writeEndElement();

        // number of columns
        name = TableItem.NUMBER_OF_COLUMNS;
        text = item.getData(TableItem.NUMBER_OF_COLUMNS).toString();
        writer.writeStartElement(LegendItem.DATA);
        writer.writeAttribute(XML_NAME, name);
        writer.writeCharacters(text);
        writer.writeEndElement();

        // labels
        TableItem.LabelSelection labelSelection = item.getData(TableItem.LABELS_SELECTION);
        ArrayList<StringBuilder> horizontalLabels = item.getData(TableItem.HORIZONTAL_LABELS);
        ArrayList<StringBuilder> verticalLabels = item.getData(TableItem.VERTICAL_LABELS);
        ArrayList<StringBuilder> labels = item.getData(TableItem.LABELS_IDS);

        Integer numItems = labels.size();
        name = TableItem.LABELS_SELECTION;
        text = labelSelection.toString();
        writer.writeStartElement(LegendItem.DATA);
        writer.writeAttribute(XML_NAME, name);
        writer.writeCharacters(text);
        writer.writeEndElement();

        // HORIZONTAL LABELS

        name = TableItem.HORIZONTAL_LABELS;
        text = horizontalLabels.toString();
        writer.writeStartElement(LegendItem.DATA);
        writer.writeAttribute(XML_NAME, name);
        writer.writeCharacters(text);
        writer.writeEndElement();

        // VERTICAL LABELS

        name = TableItem.VERTICAL_LABELS;
        text = verticalLabels.toString();
        writer.writeStartElement(LegendItem.DATA);
        writer.writeAttribute(XML_NAME, name);
        writer.writeCharacters(text);
        writer.writeEndElement();





        // values
        ArrayList<ArrayList<String>> values = item.getData(TableItem.TABLE_VALUES);
        name = TableItem.TABLE_VALUES;
        text = values.toString();
        System.out.println("@Var: text: " + text);

        writer.writeStartElement(LegendItem.DATA);
        writer.writeAttribute(XML_NAME, name);
        writer.writeCharacters(text);
        writer.writeEndElement();


        // color values
        ArrayList<ArrayList<Color>> valueColors = item.getData(TableItem.COLOR_VALUES);
        name = TableItem.COLOR_VALUES;
        ArrayList<ArrayList<Integer>> integerValueColors = new ArrayList<ArrayList<Integer>>();
        for (ArrayList<Color> row : valueColors) {
            ArrayList<Integer> newrow = new ArrayList<Integer>();
            for (Color color : row) {
                newrow.add(color.getRGB());
            }
            integerValueColors.add(newrow);
        }
        text = integerValueColors.toString();
        writer.writeStartElement(LegendItem.DATA);
        writer.writeAttribute(XML_NAME, name);
        writer.writeCharacters(text);
        writer.writeEndElement();


        // horizontal colors
        ArrayList<Color> horizontalColors = item.getData(TableItem.COLOR_HORIZONTAL);
        name = TableItem.COLOR_HORIZONTAL;
        ArrayList<Integer> horizontalColorsRow = new ArrayList<Integer>();
        for (Color color : horizontalColors) {
            horizontalColorsRow.add(color.getRGB());
        }
        text = horizontalColorsRow.toString();
        writer.writeStartElement(LegendItem.DATA);
        writer.writeAttribute(XML_NAME, name);
        writer.writeCharacters(text);
        writer.writeEndElement();



        // vertical colors
        ArrayList<Color> verticalColors = item.getData(TableItem.COLOR_VERTICAL);
        name = TableItem.COLOR_VERTICAL;
        ArrayList<Integer> verticalColorsRow = new ArrayList<Integer>();
        for (Color color : verticalColors) {
            verticalColorsRow.add(color.getRGB());
        }
        text = verticalColorsRow.toString();
        writer.writeStartElement(LegendItem.DATA);
        writer.writeAttribute(XML_NAME, name);
        writer.writeCharacters(text);
        writer.writeEndElement();

    }

    @Override
    public void readXMLToData(XMLStreamReader reader, Item item) throws XMLStreamException {

        // number of rows
        reader.next();
        Integer numberOfRows = Integer.parseInt(reader.getElementText());

        //number of columns
        reader.next();
        Integer numberOfColumns = Integer.parseInt(reader.getElementText());

        //Labels id
        reader.next();
        TableItem.LabelSelection labelSelection = TableItem.LabelSelection.valueOf(reader.getElementText());

        // reading horizontal labels
        reader.next();
        String horizontalLabelsString = reader.getElementText();
        String[] horizontalLabelsArray = horizontalLabelsString.replace("[", "").replace("]", "").split(", ");
        ArrayList<StringBuilder> horizontalLabels = new ArrayList<StringBuilder>();
        for (int i = 0; i < numberOfRows; i++) {
            horizontalLabels.add(new StringBuilder(horizontalLabelsArray[i]));
        }

        // reading vertical labels
        reader.next();
        String verticalLabelsString = reader.getElementText();
        String[] verticalLabelsArray = verticalLabelsString.replace("[", "").replace("]", "").split(", ");
        ArrayList<StringBuilder> verticalLabels = new ArrayList<StringBuilder>();
        for (int i = 0; i < numberOfColumns; i++) {
            verticalLabels.add(new StringBuilder(verticalLabelsArray[i]));
        }


        ArrayList<StringBuilder> labels = item.getData(TableItem.LABELS_IDS);
        if (labelSelection == TableItem.LabelSelection.BOTH) {
            horizontalLabels = labels;
            verticalLabels = labels;
        }
        else if (labelSelection == TableItem.LabelSelection.HORIZONTAL) {
            horizontalLabels = labels;
        }
        else if (labelSelection == TableItem.LabelSelection.VERTICAL) {
            verticalLabels = labels;
        }




        // reading values
        reader.next();
        String valuesString = reader.getElementText();
        String[] valuesArray = valuesString.replace("[", "").replace("]", "").split(", ");
        ArrayList<ArrayList<String>> values = new ArrayList<ArrayList<String>>();
        for (int i = 0; i < numberOfRows; i++) {
            ArrayList<String> row = new ArrayList<String>();
            for (int j = 0; j < numberOfColumns; j++) {
                row.add(valuesArray[i * numberOfColumns + j]);
            }
            values.add(row);
        }

        // reading colors
        reader.next();
        String colorsString = reader.getElementText();
        String[] colorsArray = colorsString.replace("[", "").replace("]", "").split(", ");
        ArrayList<ArrayList<Color>> colors = new ArrayList<ArrayList<Color>>();
        for (int i = 0; i < numberOfRows; i++) {
            ArrayList<Color> row = new ArrayList<Color>();
            for (int j = 0; j < numberOfColumns; j++) {
                row.add(new Color(Integer.parseInt(colorsArray[i * numberOfColumns + j])));
            }
            colors.add(row);
        }


        // reading horizontal colors
        reader.next();
        String horizontalColorsString = reader.getElementText();
        String[] horizontalColorsArray = horizontalColorsString.replace("[", "").replace("]", "").split(", ");
        ArrayList<Color> horizontalColors = new ArrayList<Color>();
        for (int i = 0; i < numberOfRows; i++) {
            horizontalColors.add(new Color(Integer.parseInt(horizontalColorsArray[i])));
        }

        // reading vertical colors
        reader.next();
        String verticalColorsString = reader.getElementText();
        String[] verticalColorsArray = verticalColorsString.replace("[", "").replace("]", "").split(", ");
        ArrayList<Color> verticalColors = new ArrayList<Color>();
        for (int i = 0; i < numberOfColumns; i++) {
            verticalColors.add(new Color(Integer.parseInt(verticalColorsArray[i])));
        }


        // setting data
        item.setData(TableItem.NUMBER_OF_ROWS, numberOfRows);
        item.setData(TableItem.NUMBER_OF_COLUMNS, numberOfColumns);
        item.setData(TableItem.HORIZONTAL_LABELS, horizontalLabels);
        item.setData(TableItem.VERTICAL_LABELS, verticalLabels);
        item.setData(TableItem.LABELS_SELECTION, labelSelection);
        item.setData(TableItem.TABLE_VALUES, values);
        item.setData(TableItem.COLOR_VALUES, colors);
        item.setData(TableItem.COLOR_HORIZONTAL, horizontalColors);
        item.setData(TableItem.COLOR_VERTICAL, verticalColors);

    }

    @Override
    public Item createNewLegendItem(Graph graph) {
        return new TableItem(graph);
    }

    @Override
    public void writeXMLFromItemOwnProperties(XMLStreamWriter writer, Item item, PreviewProperties previewProperties) throws XMLStreamException {

        // write number of items
        Integer numberOfLabels = item.getData(TableItem.NUMBER_OF_LABELS);
        String name = TableItem.NUMBER_OF_LABELS;
        String text = numberOfLabels.toString();
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
        item.setData(TableItem.NUMBER_OF_LABELS, numberOfItems);

        // reading labels
        ArrayList<StringBuilder> labels = new ArrayList<StringBuilder>();
        for (int i = 0; i < numberOfItems; i++) {
            reader.next();
            StringBuilder valueStringBuilder = new StringBuilder(reader.getElementText());
            labels.add(valueStringBuilder);
            PreviewProperty property = createLegendLabelProperty(item, i, valueStringBuilder.toString());
            properties.add(property);
        }
        item.setData(TableItem.LABELS_IDS, labels);

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
        int propertyIndex = TableProperty.getInstance().getProperty(propertyName);
        Class valueClass = defaultValues[propertyIndex].getClass();
        Object value = PreviewProperties.readValueFromText(valueString, valueClass);
        if (value == null) {
            value = readValueFromText(valueString, valueClass);
        }
        PreviewProperty property = createLegendProperty(item, propertyIndex, value);
        return property;
    }

    //default values
    protected final Integer defaultColumnExtraMargin = 3;
    protected final Integer defaultRowExtraMargin = 3;
    protected final Font defaultFont = new Font("Arial", Font.PLAIN, 13);
    protected final Color defaultFontColor = Color.BLACK;
    // grid
    protected final Color defaultGridColor = Color.BLACK;
    protected final Boolean defaultIsDisplayingGrid = true;
    // cell
    protected final Boolean defaultIsCellColoring = false;
    protected final Direction defaultCellColoringDirection = Direction.UP;
    // side labels
    protected final HorizontalPosition defaultRowLabelsPosition = HorizontalPosition.LEFT;
    protected final Alignment defaultRowTextAlignment = Alignment.JUSTIFIED;
    // up/bottom labels
    protected final Alignment defaultColumnTextAlignment = Alignment.CENTER;
    protected final VerticalPosition defaultColumnLabelsPosition = VerticalPosition.UP;
    protected final TableItem.VerticalTextDirection defaultColumnTextRotation = TableItem.VerticalTextDirection.DIAGONAL;
    private final Object[] defaultValues = {
        defaultFont,
        defaultFontColor,
        defaultIsCellColoring,
//        defaultCellColoringDirection,
        defaultRowLabelsPosition,
        defaultRowTextAlignment,
        defaultRowExtraMargin,
        defaultColumnLabelsPosition,
        defaultColumnTextAlignment,
        defaultColumnTextRotation,
        defaultColumnExtraMargin,
        defaultIsDisplayingGrid,
        defaultGridColor
    };
}