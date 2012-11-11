/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.gephi.legend.api;

import com.itextpdf.text.PageSize;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import org.gephi.data.attributes.api.AttributeController;
import org.gephi.data.attributes.api.AttributeModel;
import org.gephi.desktop.welcome.WelcomeTopComponent;
import org.gephi.graph.api.Edge;
import org.gephi.graph.api.Graph;
import org.gephi.graph.api.GraphController;
import org.gephi.graph.api.Node;
import org.gephi.io.exporter.preview.PDFExporter;
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

/**
 *
 * @author edubecks
 */
public class TestPDFTarget {

    public TestPDFTarget() {
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
    public void testPDF() {
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


            // pdf document
            Rectangle pdfPageSize = PageSize.A4.rotate();
            com.itextpdf.text.Document pdfDocument = new com.itextpdf.text.Document(pdfPageSize);
            FileOutputStream pdfFile = new FileOutputStream(new File("/Users/edubecks/Desktop/Untitled.pdf"));
            PdfWriter writer = PdfWriter.getInstance(pdfDocument, pdfFile);
            pdfDocument.open();
            PdfContentByte pdfContentByte = writer.getDirectContent();


            // attribute ???
            AttributeController attributeController = Lookup.getDefault().lookup(AttributeController.class);
            AttributeModel attributeModel = null;

            // preview
            PreviewController previewController = Lookup.getDefault().lookup(PreviewController.class);
            PreviewModel previewModel = previewController.getModel(workspace);
            PreviewProperty pdfPageSizePreviewProperty = PreviewProperty.createProperty(null, PDFTarget.PAGESIZE).setValue(pdfPageSize);
            PreviewProperty pdfCBPreviewProperty = PreviewProperty.createProperty(null, PDFTarget.PDF_CONTENT_BYTE).setValue(pdfContentByte);
            PreviewProperties previewProperties = previewModel.getProperties();
            previewProperties.addProperty(pdfPageSizePreviewProperty);
            previewProperties.addProperty(pdfCBPreviewProperty);
            previewController.refreshPreview(workspace);


            PDFTarget target = (PDFTarget) previewController.getRenderTarget(RenderTarget.PDF_TARGET, workspace);




            previewProperties.putValue(LegendModel.LEGEND_PROPERTIES, new LegendModel(workspace));

            LegendModel legendManager = previewProperties.getValue(LegendModel.LEGEND_PROPERTIES);
            Integer newItemIndex = legendManager.getCurrentIndex();


            // creating item
            Item item = addTextItem(newItemIndex, graph, attributeModel);


            // add item
            legendManager.addItem(item);
            PreviewProperty[] legendProperties = item.getData(LegendItem.PROPERTIES);
            for (PreviewProperty property : legendProperties) {
                previewController.getModel().getProperties().putValue(property.getName(), property.getValue());
            }

            // render
            previewController.refreshPreview(workspace);
            previewController.render(target);


pdfDocument.close();
            pdfFile.close();

            PreviewProperties props = previewModel.getProperties();
            props.putValue(PreviewProperty.SHOW_NODE_LABELS, false);
            props.putValue(PreviewProperty.EDGE_OPACITY, 20f);
//            props.putValue(PreviewProperty.BACKGROUND_COLOR, new BaseColor(1, 1, 1));

            PDFExporter pDFExporter = new PDFExporter();
            pDFExporter.setLandscape(true);

            pDFExporter.setWorkspace(workspace);
            try {
                File file = new File("/Users/edubecks/Desktop/Untitled-Exporter.pdf");
                System.out.println(file.getAbsolutePath());
                FileOutputStream fos = new FileOutputStream(file);
                pDFExporter.setOutputStream(fos);
                pDFExporter.execute();
            } catch (Exception ex) {
                ex.printStackTrace();
                Exceptions.printStackTrace(ex);
            }


            

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
