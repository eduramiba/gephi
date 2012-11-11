/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.gephi.legend.spi;

import java.util.ArrayList;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;
import org.gephi.data.attributes.api.AttributeModel;
import org.gephi.graph.api.Graph;
import org.gephi.preview.api.*;
import org.gephi.preview.spi.ItemBuilder;

/**
 * Interface that all Legend item builders must implement.
 * When writing a legend item builder, you should normally extend AbstractLegendItemRenderer, which implements many common features for all legend item builders.
 * @see AbstractLegendItemRenderer
 * @author edubecks
 */
public interface LegendItemBuilder extends ItemBuilder {

    /**
     * Function used to override default values of
     * <code>properties</code> Possible values to be overriden:<br />
     *
     * LABEL: <ul> <li> defaultLabel </li> </ul> IS_DISPLAYING: <ul> <li> defaultIsDisplaying </li> </ul> ORIGIN <ul> <li> defaultOriginX </li> <li> defaultOriginY </li> </ul> WIDTH <ul> <li>
     * defaultWidth </li> <li> defaultHeight </li> </ul> TITLE: <ul> <li> defaultTitleIsDisplaying </li> <li> defaultTitle </li> <li> defaultTitleFont </li> <li> defaultTitleAlignment </li> <li>
     * defaultTitleFontColor </li> </ul> DESCRIPTION: <ul> <li> defaultDescription </li> <li> defaultDescriptionIsDisplaying </li> <li> defaultDescriptionFontColor </li> <li>
     * defaultDescriptionAlignment </li> <li> defaultDescriptionFont </li> </ul>
     */
    public boolean setDefaultValues();

    /**
     * Based on
     * <code>properties</code>, determine whether this builder was used to build
     * <code>Item</code>.
     *
     * @param item the item to be tested
     * @return <code>true</code> if <code>item</code> was built by this builder, <code>false</code> otherwise
     */
    public boolean isBuilderForItem(Item item);

    /**
     * Builds and item based on
     * <code>graph</code> and
     * <code>attributeModel</code> data
     *
     * @param graph
     * @param attributeModel
     * @return
     */
    public Item buildCustomItem(CustomLegendItemBuilder builder,
            Graph graph,
            AttributeModel attributeModel);

    /**
     * Used to determine if the
     * <code>item</code> has dynamic properties to be displayed in the PropertySheet Editor
     *
     * @return <code>true</code> if <code>item</code> has dynamic * * properties, <code>false</code> otherwise
     */
    public Boolean hasDynamicProperties();

    /**
     * Function used to create the specific PreviewProperty for each type of LegendItem
     *
     * @param item
     * @return an array of PreviewProperty to be appended to the general LegendItem's PreviewProperty
     */
    public PreviewProperty[] createLegendOwnProperties(Item item);

    /**
     *
     * @return a list of all the available builders for an specific type of builder
     */
    public ArrayList<CustomLegendItemBuilder> getAvailableBuilders();

    /**
     * @param graph
     * @return a new instance of an Item Ex:
     */
    public Item createNewLegendItem(Graph graph);

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
            CustomLegendItemBuilder builder);


    /**
     * Provides an user friendly name for the builder. This name will appear in the legend manager UI.
     *
     * @return User friendly builder name, not null
     */
    public abstract String getTitle();

    /**
     * Function that takes the data corresponding to each type of item and uses the writer to save the data in it. It saves the data inside an
     * <code>itemdata</code> node.
     *
     * @param writer the XMLStreamWriter to write to
     * @param item the item to be saved
     * @param previewProperties Current workspace PreviewProperties
     * @throws XMLStreamException
     */
    public void writeXMLFromData(XMLStreamWriter writer, Item item, PreviewProperties previewProperties) throws XMLStreamException;

    /**
     * Function that takes the specific properties of each item and uses the writer to save the properties in it. It saves the properties inside an
     * <code>itemproperty</code> tag
     *
     * @param writer the XMLStreamWriter to write to
     * @param item the item to be saved
     * @param previewProperties Current workspace PreviewProperties
     * @throws XMLStreamException
     */
    public void writeXMLFromItemOwnProperties(XMLStreamWriter writer, Item item, PreviewProperties previewProperties) throws XMLStreamException;

    /**
     * Function that takes the dynamic properties (if any) of each item and uses the writer to save the properties in it. It saves the properties inside an
     * <code>dynamicproperty</code> tag
     *
     * @param writer the XMLStreamWriter to write to
     * @param item the item to be saved
     * @param previewProperties Current workspace PreviewProperties
     * @throws XMLStreamException
     */
    public void writeXMLFromDynamicProperties(XMLStreamWriter writer, Item item, PreviewProperties previewProperties) throws XMLStreamException;

    /**
     * Function that takes an item and saves its data, legend properties, specific item properties, dynamic properties and data using the specified writer.
     *
     * @param writer the XMLStreamWriter to write to
     * @param item the item to be saved
     * @param previewProperties Current workspace PreviewProperties
     * @throws XMLStreamException
     */
    public void writeXMLFromItem(XMLStreamWriter writer, Item item, PreviewProperties previewProperties) throws XMLStreamException;
    
    /**
     * Function that retrieves the data from an XML reader and converts it to data for each kind of item
     *
     * @param reader the XML reader to read the data from
     * @param item the item where the data would be stored
     * @throws XMLStreamException
     */
    public void readXMLToData(XMLStreamReader reader, Item item) throws XMLStreamException;

    /**
     * Function that retrieves the properties (propertyName and value) from an XML reader and converts it to list of properties. Normally using the
     * <code>readXMLToSingleOwnProperty</code> for each property.
     *
     * @param reader the XML reader to read the data from
     * @param item the item where the data would be stored
     * @return
     * @throws XMLStreamException
     */
    public ArrayList<PreviewProperty> readXMLToOwnProperties(XMLStreamReader reader, Item item) throws XMLStreamException;

    /**
     * Function that retrieves the dynamic properties (if any) from an XML reader and converts it to list of properties. Normally using the
     * <code>readXMLToSingleOwnProperty</code> for each property.
     *
     * @param reader the XML reader to read the data from
     * @param item the item where the data would be stored
     * @return
     * @throws XMLStreamException
     */
    public ArrayList<PreviewProperty> readXMLToDynamicProperties(XMLStreamReader reader, Item item) throws XMLStreamException;

    public ArrayList<PreviewProperty> readXMLToLegendProperties(XMLStreamReader reader, Item item) throws XMLStreamException;

    public void readXMLToRenderer(XMLStreamReader reader, Item item) throws XMLStreamException;

    /**
     * Function that reads the legend properties, specific item properties, dynamic properties and data and converts it to an Item using the specified reader.
     *
     * @param reader the XML reader to read the data from
     * @param newItemIndex used to create the Item
     * @return
     * @throws XMLStreamException
     */
    public Item readXMLToItem(XMLStreamReader reader, Integer newItemIndex) throws XMLStreamException;
}
