/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.gephi.legend.api;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import org.gephi.data.attributes.api.AttributeController;
import org.gephi.data.attributes.api.AttributeModel;
import org.gephi.desktop.welcome.WelcomeTopComponent;
import org.gephi.graph.api.Edge;
import org.gephi.graph.api.Graph;
import org.gephi.graph.api.GraphController;
import org.gephi.graph.api.Node;
import org.gephi.io.exporter.preview.PNGExporter;
import org.gephi.io.importer.api.Container;
import org.gephi.io.importer.api.ImportController;
import org.gephi.io.importer.spi.FileImporter;
import org.gephi.io.processor.plugin.DefaultProcessor;
import org.gephi.legend.plugin.builders.GroupsItemBuilder;
import org.gephi.legend.plugin.builders.ImageItemBuilder;
import org.gephi.legend.plugin.builders.TableItemBuilder;
import org.gephi.legend.plugin.builders.TextItemBuilder;
import org.gephi.preview.api.Item;
import org.gephi.preview.api.PreviewController;
import org.gephi.preview.api.PreviewModel;
import org.gephi.preview.api.PreviewProperties;
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
public class TestCustomRenderers {
    
    

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
    public void testPNG() {
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
            Item item = addGroupsItem(itemIndex, graph, attributeModel);
//            Item item = addTextItem(itemIndex, graph, attributeModel);


            // add item
            LegendController.getInstance().addItemToLegendModel(workspace, item);
            
            
            // render
            previewController.refreshPreview(workspace);
            previewController.render(target);
            
            


            File fileToSave = new File("/Volumes/edubecks/edubecks/Untitled.png");
            OutputStream fos = new FileOutputStream(fileToSave);
            Writer writer = new OutputStreamWriter(fos, "UTF-8");

//            // saving
//            int width = 1000;
//            int height = 1000;
//            PGraphicsJava2D pg2 = (PGraphicsJava2D) target.getGraphics();
//            BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
//            
//            System.out.println("@Var: pg2.pixels: "+pg2.pixels);
//            img.setRGB(0, 0, width, height, pg2.pixels, 0, width);
//            ImageIO.write(img, "png", fileToSave);
//            stream.close();

            PNGExporter pngExporter = new PNGExporter();
//            pngExporter.setHeight(2000);
//            pngExporter.setWidth(2000);
            pngExporter.setWorkspace(workspace);
            pngExporter.setOutputStream(fos);
            pngExporter.execute();




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
        return item;
    }

}
