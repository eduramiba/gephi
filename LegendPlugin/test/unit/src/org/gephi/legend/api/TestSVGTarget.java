/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.gephi.legend.api;

import java.io.*;
import org.apache.batik.dom.GenericDOMImplementation;
import org.gephi.data.attributes.api.AttributeController;
import org.gephi.data.attributes.api.AttributeModel;
import org.gephi.desktop.welcome.WelcomeTopComponent;
import org.gephi.graph.api.Edge;
import org.gephi.graph.api.Graph;
import org.gephi.graph.api.GraphController;
import org.gephi.graph.api.Node;
import org.gephi.io.exporter.preview.SVGExporter;
import org.gephi.io.importer.api.Container;
import org.gephi.io.importer.api.ImportController;
import org.gephi.io.importer.spi.FileImporter;
import org.gephi.io.processor.plugin.DefaultProcessor;
import org.gephi.legend.plugin.builders.GroupsItemBuilder;
import org.gephi.legend.plugin.builders.ImageItemBuilder;
import org.gephi.legend.plugin.builders.TextItemBuilder;
import org.gephi.legend.spi.LegendItem;
import org.gephi.preview.api.*;
import org.gephi.project.api.ProjectController;
import org.gephi.project.api.Workspace;
import org.junit.*;
import org.openide.util.Exceptions;
import org.openide.util.Lookup;
import org.w3c.dom.DOMImplementation;

/**
 *
 * @author edubecks
 */
public class TestSVGTarget {
    
    public TestSVGTarget() {
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
    public void testSVG() {
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
//            Get a DOMImplementation
            DOMImplementation domImpl =
                    GenericDOMImplementation.getDOMImplementation();


            
            
            



            // attribute ???
            AttributeController attributeController = Lookup.getDefault().lookup(AttributeController.class);
            AttributeModel attributeModel = null;

            // preview
            PreviewController previewController = Lookup.getDefault().lookup(PreviewController.class);
            PreviewModel previewModel = previewController.getModel(workspace);
            PreviewProperties previewProperties = previewModel.getProperties();
            previewController.refreshPreview(workspace);


            SVGTarget target = (SVGTarget) previewController.getRenderTarget(RenderTarget.SVG_TARGET, workspace);
            org.w3c.dom.Document svgDocument = target.getDocument();




            previewProperties.putValue(LegendModel.LEGEND_PROPERTIES, new LegendModel(workspace));

            LegendModel legendManager = previewProperties.getValue(LegendModel.LEGEND_PROPERTIES);
            Integer newItemIndex = legendManager.getCurrentIndex();


            // creating item
            Item item = addImageItem(newItemIndex, graph, attributeModel);
            
            


            // add item
            legendManager.addItem(item);
            PreviewProperty[] properties = item.getData(LegendItem.PROPERTIES);
            
            
            //override some properties
//            origin X
//            properties[3].setValue(600);
            
            for (PreviewProperty property : properties) {
                previewController.getModel().getProperties().addProperty(property);
            }



            // render
            previewController.refreshPreview(workspace);
            previewController.render(target);
            
            
            OutputStream fos = new FileOutputStream(new File("/Users/edubecks/Desktop/Untitled.svg"));
            Writer writer = new OutputStreamWriter(fos, "UTF-8");
            
            
            SVGExporter svgExporter = new SVGExporter();
            svgExporter.setWorkspace(workspace);
            svgExporter.setWriter(writer);
            svgExporter.execute();

//            SVGGraphics2D graphics2D = new SVGGraphics2D(svgDocument);
//            graphics2D.stream(writer, true);



        } catch (Exception e) {
            Exceptions.printStackTrace(e);
        }
    }

    public Item addTextItem(int newItemIndex, Graph graph, AttributeModel attributeModel) {
        TextItemBuilder builder = new TextItemBuilder();
        Item item = builder.createCustomItem(newItemIndex, graph, attributeModel,new org.gephi.legend.plugin.builders.text.Default());
        return item;
    }

    public Item addImageItem(int newItemIndex, Graph graph, AttributeModel attributeModel) {
        ImageItemBuilder builder = new ImageItemBuilder();
        Item item = builder.createCustomItem(newItemIndex, graph, attributeModel,new org.gephi.legend.plugin.builders.image.Default());
        return item;
    }

    public Item addGroupsItem(int newItemIndex, Graph graph, AttributeModel attributeModel) {
        GroupsItemBuilder builder = new GroupsItemBuilder();
        Item item = builder.createCustomItem(newItemIndex, graph, attributeModel,new org.gephi.legend.plugin.builders.text.Default());
        return item;
    }
    
}
