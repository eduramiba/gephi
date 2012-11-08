/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.gephi.legend.manager;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;

import org.gephi.legend.api.renderers.LegendItemRenderer;
import org.gephi.legend.builders.LegendItemBuilder;
import org.gephi.legend.items.LegendItem;
import org.gephi.preview.api.Item;
import org.gephi.preview.api.PreviewController;
import org.gephi.preview.api.PreviewModel;
import org.gephi.preview.api.PreviewProperties;
import org.gephi.preview.api.PreviewProperty;
import org.gephi.project.api.ProjectController;
import org.gephi.project.api.Workspace;
import org.openide.util.Lookup;

/**
 *
 * @author edubecks
 */
public class LegendController {

    /**
     * Returns LegendManager instance of given workspace
     *
     * @param workspace Workspace
     * @return LegendManager instance
     */
    public LegendManager getLegendManager(Workspace workspace) {
        LegendManager legendManager = workspace.getLookup().lookup(LegendManager.class);

        if (legendManager == null) {
            legendManager = new LegendManager(workspace);
            workspace.add(legendManager);
        }

        return legendManager;
    }

    /**
     * Returns LegendManager instance of current workspace
     *
     * @return
     */
    public LegendManager getLegendManager() {
        return getLegendManager(Lookup.getDefault().lookup(ProjectController.class).getCurrentWorkspace());
    }

    private Collection<? extends LegendItemBuilder> registerLegendBuilders() {
        builders = new HashMap<String, LegendItemBuilder>();

        // retrieving available builders
        Collection<? extends LegendItemBuilder> legendItemBuilders =
                Lookup.getDefault().lookupAll(LegendItemBuilder.class);

        // registering builders
        for (LegendItemBuilder legendItemBuilder : legendItemBuilders) {
            builders.put(legendItemBuilder.getType(), legendItemBuilder);
        }

        return legendItemBuilders;
    }

    private void registerLegendRenderers() {
        renderers = new HashMap<String, LegendItemRenderer>();

        // retrieving available builders
        Collection<? extends LegendItemRenderer> legendItemRenderers =
                Lookup.getDefault().lookupAll(LegendItemRenderer.class);

        // registering renderers
        for (LegendItemRenderer legendItemRenderer : legendItemRenderers) {
            renderers.put(legendItemRenderer.getClass().getName(), legendItemRenderer);
        }
    }



    public void addItemToLegendManager(Item item) {
        addItemToLegendManager(Lookup.getDefault().lookup(ProjectController.class).getCurrentWorkspace(), item);
    }

    /**
     * Add an item
     *
     * @param workspace
     * @param item
     */
    public void addItemToLegendManager(Workspace workspace, Item item) {
        PreviewController previewController = Lookup.getDefault().lookup(PreviewController.class);
        PreviewModel previewModel = previewController.getModel(workspace);
        PreviewProperties previewProperties = previewModel.getProperties();

        LegendManager legendManager = getLegendManager(workspace);

        legendManager.addItem(item);

        // LEGEND PROPERTIES
        PreviewProperty[] legendProperties = item.getData(LegendItem.PROPERTIES);

        /////!Important: We put simple values instead of PreviewProperty in the properties because of 2 reasons:
        /////     #An old version of Gephi would read and show Legend preview properties if a project file with legends is loaded into it.
        /////     #Renderer manager removes any PreviewProperty that is not explicitely declared by a renderer in its 'getProperties' method
        /////      so when a renderer is disabled, its properties are not shown in the default sheet.
        /////      
        /////      Renderer manager does not remove simple values because they are not automatically shown in the UI, as they are intended for this kind of usage.

        for (PreviewProperty property : legendProperties) {
            previewProperties.putValue(property.getName(), property.getValue());
        }
        // LEGEND OWN PROPERTIES
        PreviewProperty[] ownProperties = item.getData(LegendItem.OWN_PROPERTIES);
        for (PreviewProperty property : ownProperties) {
            previewProperties.putValue(property.getName(), property.getValue());
        }
        // DYNAMIC PROPERTIES
        PreviewProperty[] dynamicProperties = item.getData(LegendItem.DYNAMIC_PROPERTIES);
        for (PreviewProperty property : dynamicProperties) {
            previewProperties.putValue(property.getName(), property.getValue());
        }
    }

    public void writeXML(XMLStreamWriter writer, Workspace workspace) {
        try {
            PreviewController previewController = Lookup.getDefault().lookup(PreviewController.class);
            PreviewModel previewModel = previewController.getModel(workspace);
            PreviewProperties previewProperties = previewModel.getProperties();

            LegendManager legendManager = getLegendManager(workspace);

            ArrayList<Item> legendItems = legendManager.getLegendItems();


            writer.writeStartElement(XML_LEGENDS);
            for (Item item : legendItems) {
                LegendItemBuilder builder = builders.get(item.getType());

                writer.writeStartElement(XML_LEGEND_ITEM);
                builder.writeXMLFromItem(writer, item, previewProperties);
                writer.writeEndElement();
            }
            writer.writeEndElement();
//            System.out.println("@Var: writer: "+writer.);


        } catch (XMLStreamException ex) {
            throw new RuntimeException(ex);
        }
    }

    public void readXMLToLegendManager(XMLStreamReader reader, Workspace workspace) {
        try {
            PreviewController previewController = Lookup.getDefault().lookup(PreviewController.class);
            PreviewModel previewModel = previewController.getModel(workspace);

            int newItemIndex = 0;
            boolean end = false;
            while (reader.hasNext() && !end) {

                // legend item

                int type = reader.next();
                String legendItem = reader.getLocalName();
                switch (type) {
                    case XMLStreamReader.START_ELEMENT: {

                        if (legendItem.equals(XML_LEGEND_ITEM)) {
                            reader.next();
                            String legendType = reader.getElementText();
                            LegendItemBuilder builder = builders.get(legendType);
                            Item item = builder.readXMLToItem(reader, newItemIndex);

                            // adding item
                            addItemToLegendManager(workspace, item);

                            // finish reading item
                            reader.next();
                            newItemIndex++;
                        }
                        break;
                    }
                    case XMLStreamReader.CHARACTERS: {
                        break;
                    }
                    case XMLStreamReader.END_ELEMENT: {
                        // finish reading legends
                        reader.next();
                        end = true;
                        break;
                    }
                }


            }
        } catch (XMLStreamException ex) {
            throw new RuntimeException(ex);
        }

    }
    private static LegendController instance = new LegendController();
    // available builders
    private Map<String, LegendItemBuilder> builders;
    private Map<String, LegendItemRenderer> renderers;
    private Collection<? extends LegendItemBuilder> availablebuilders;

    /*
     * returns a Collection of the available Legend Item Builders in the system
     * Default list:
     * TableItem Builder
     * ImageItem Builder
     * GroupsItem Builder
     * Descriptiontem Builder
     */
    public Collection<? extends LegendItemBuilder> getAvailablebuilders() {
        return availablebuilders;
    }

    public Map<String, LegendItemRenderer> getRenderers() {
        return renderers;
    }

    private LegendController() {
        builders = new HashMap<String, LegendItemBuilder>();
        availablebuilders = registerLegendBuilders();
        registerLegendRenderers();
    }

    /*
     * Returns an Instance of LegendController
     */
    public static LegendController getInstance() {
        return instance;
    }
    private static final String XML_LEGEND_ITEM = "legenditem";
    public static final String XML_LEGENDS = "legends";
}
