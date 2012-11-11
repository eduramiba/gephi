/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.gephi.legend.api;

import java.io.InputStream;
import java.io.StringReader;
import java.io.StringWriter;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;
import org.gephi.data.attributes.api.AttributeController;
import org.gephi.data.attributes.api.AttributeModel;
import org.gephi.desktop.welcome.WelcomeTopComponent;
import org.gephi.graph.api.Edge;
import org.gephi.graph.api.Graph;
import org.gephi.graph.api.GraphController;
import org.gephi.graph.api.Node;
import org.gephi.io.importer.api.Container;
import org.gephi.io.importer.api.ImportController;
import org.gephi.io.importer.spi.FileImporter;
import org.gephi.io.processor.plugin.DefaultProcessor;
import org.gephi.legend.plugin.builders.DescriptionItemBuilder;
import org.gephi.legend.plugin.builders.GroupsItemBuilder;
import org.gephi.legend.plugin.builders.ImageItemBuilder;
import org.gephi.legend.plugin.builders.TableItemBuilder;
import org.gephi.legend.plugin.builders.TextItemBuilder;
import org.gephi.legend.plugin.renderers.extra.AnotherGroupsRenderer;
import org.gephi.legend.spi.LegendItem;
import org.gephi.preview.api.Item;
import org.gephi.preview.api.PreviewController;
import org.gephi.preview.api.PreviewModel;
import org.gephi.preview.api.PreviewProperties;
import org.gephi.preview.api.PreviewProperty;
import org.gephi.preview.api.ProcessingTarget;
import org.gephi.preview.api.RenderTarget;
import org.gephi.project.api.ProjectController;
import org.gephi.project.api.Workspace;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openide.util.Exceptions;
import org.openide.util.Lookup;

/**
 *
 * @author edubecks
 */
public class TestPersistence {

    public TestPersistence() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testXMLWriter() {

        try {

            //Init a project - and therefore a workspace
            ProjectController pc = Lookup.getDefault().lookup(ProjectController.class);
            pc.newProject();
            Workspace workspace = pc.getCurrentWorkspace();

            //Append container to graph structure
            String sample = "/org/gephi/desktop/welcome/samples/Les Miserables.gexf";
            final InputStream stream = WelcomeTopComponent.class.getResourceAsStream(sample);
            ImportController importController = Lookup.getDefault().lookup(ImportController.class);
            FileImporter fileImporter = importController.getFileImporter(".gexf");
            Container container = importController.importFile(stream, fileImporter);

            importController.process(container, new DefaultProcessor(), workspace);

            //Add self loop
            GraphController graphController = Lookup.getDefault().lookup(GraphController.class);
            Graph graph = graphController.getModel().getGraph();
            Node node = graph.getNode(12);
            System.out.println("Self loop " + node.getNodeData().getLabel());
            graph.addEdge(graphController.getModel().factory().newEdge(node, node, 31, true));

            //Set label edges       
            for (Edge edge : graphController.getModel().getGraph().getEdges()) {
                edge.getEdgeData().setLabel("Label test");
            }


            // svg document



            // attribute ???
            AttributeController attributeController = Lookup.getDefault().lookup(AttributeController.class);
            AttributeModel attributeModel = null;

            // preview
            PreviewController previewController = Lookup.getDefault().lookup(PreviewController.class);
            PreviewModel previewModel = previewController.getModel(workspace);
            PreviewProperties previewProperties = previewModel.getProperties();
            previewController.refreshPreview(workspace);


            previewModel.getProperties().putValue("width", 1000);
            previewModel.getProperties().putValue("height", 1000);
            ProcessingTarget target = (ProcessingTarget) previewController.getRenderTarget(
                    RenderTarget.PROCESSING_TARGET, workspace);



            previewProperties.putValue(LegendModel.LEGEND_PROPERTIES, new LegendModel(workspace));

            LegendModel legendManager = previewProperties.getValue(LegendModel.LEGEND_PROPERTIES);
            Integer itemIndex = legendManager.getCurrentIndex();


            // creating item
//            Item item = addDescriptionItem(itemIndex, graph, attributeModel, previewController);
//            Item item = addTableItem(itemIndex, graph, attributeModel);
//            Item item = addTextItem(itemIndex, graph, attributeModel);
//            Item item = addImageItem(itemIndex, graph, attributeModel);
            Item item = addGroupsItem(itemIndex, graph, attributeModel);
            item.setData(sample, target);


            // add item
            LegendController legendController = LegendController.getInstance();
            legendController.addItemToLegendModel(item);
//            legendManager.addItem(item);

            PreviewProperty[] legendProperties = item.getData(LegendItem.PROPERTIES);
            for (PreviewProperty property : legendProperties) {
                previewController.getModel().getProperties().putValue(property.getName(), property.getValue());
            }




            StringWriter stringWriter = new StringWriter();
            XMLOutputFactory xmlOutputFactory = XMLOutputFactory.newFactory();
            XMLStreamWriter xmlStreamWriter = xmlOutputFactory.createXMLStreamWriter(stringWriter);
            LegendController.getInstance().writeXML(xmlStreamWriter, workspace);


            System.out.println("@Var: stringWriter: " + stringWriter + "\n\n\n\n");


            XMLInputFactory xmlInputFactory = XMLInputFactory.newFactory();
            StringReader stringReader = new StringReader(stringWriter.toString());
            XMLStreamReader reader = xmlInputFactory.createXMLStreamReader(stringReader);
            LegendController.getInstance().readXMLToLegendModel(reader, workspace);







        } catch (Exception e) {
            Exceptions.printStackTrace(e);
        }
    }

    public Item addTextItem(int newItemIndex, Graph graph, AttributeModel attributeModel) {
        TextItemBuilder builder = new TextItemBuilder();
        Item item = builder.createCustomItem(newItemIndex, graph, attributeModel, new org.gephi.legend.plugin.builders.text.Default());
        return item;
    }

    public Item addTableItem(int newItemIndex, Graph graph, AttributeModel attributeModel) {
        TableItemBuilder builder = new TableItemBuilder();
        Item item = builder.createCustomItem(newItemIndex, graph, attributeModel, new org.gephi.legend.plugin.builders.table.Default());
        return item;
    }

    public Item addImageItem(int newItemIndex, Graph graph, AttributeModel attributeModel) {
        ImageItemBuilder builder = new ImageItemBuilder();
        Item item = builder.createCustomItem(newItemIndex, graph, attributeModel, new org.gephi.legend.plugin.builders.image.Default());
        return item;
    }

    public Item addGroupsItem(int newItemIndex, Graph graph, AttributeModel attributeModel) {
        GroupsItemBuilder builder = new GroupsItemBuilder();
        Item item = builder.createCustomItem(newItemIndex, graph, attributeModel, new org.gephi.legend.plugin.builders.group.Default());
        String renderer = (new AnotherGroupsRenderer()).getClass().getName();
        item.setData(LegendItem.RENDERER,renderer );
        return item;
    }

    public Item addDescriptionItem(int newItemIndex, Graph graph, AttributeModel attributeModel, PreviewController previewController) {
        DescriptionItemBuilder builder = new DescriptionItemBuilder();
        Item item = builder.createCustomItem(newItemIndex, graph, attributeModel, new org.gephi.legend.plugin.builders.description.Default());
        if (LegendController.getInstance().updateItemDynamicPreviewProperties(item, 2)) {
            PreviewProperty[] dynamicProperties = item.getData(LegendItem.DYNAMIC_PROPERTIES);
            for (PreviewProperty property : dynamicProperties) {
                previewController.getModel().getProperties().putValue(property.getName(), property.getValue());
            }
        }
        return item;
    }

}
