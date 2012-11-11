/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.gephi.legend.api;

import java.io.InputStream;
import org.gephi.desktop.welcome.WelcomeTopComponent;
import org.gephi.dynamic.api.DynamicController;
import org.gephi.dynamic.api.DynamicModel;
import org.gephi.graph.api.Edge;
import org.gephi.graph.api.Graph;
import org.gephi.graph.api.GraphController;
import org.gephi.graph.api.Node;
import org.gephi.io.importer.api.Container;
import org.gephi.io.importer.api.ImportController;
import org.gephi.io.importer.spi.FileImporter;
import org.gephi.io.processor.plugin.DefaultProcessor;
import org.gephi.partition.api.Partition;
import org.gephi.partition.api.PartitionController;
import org.gephi.partition.api.PartitionModel;
import org.gephi.project.api.ProjectController;
import org.gephi.project.api.Workspace;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openide.util.Lookup;

/**
 *
 * @author eduBecKs
 */
public class TestPartitionData {

    public TestPartitionData() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testPartitionData() {
        try {


            //Init a project - and therefore a workspace
            ProjectController pc = Lookup.getDefault().lookup(ProjectController.class);
            pc.newProject();
            Workspace workspace = pc.getCurrentWorkspace();

            //Append container to graph structure
            String sample = "/org/gephi/desktop/welcome/samples/Les Miserables.gexf";
            final InputStream stream = WelcomeTopComponent.class.getResourceAsStream(sample);
//            final InputStream stream = new FileInputStream("/Users/eduBecKs/Desktop/partitions.gephi");
            ImportController importController = Lookup.getDefault().lookup(ImportController.class);
            FileImporter fileImporter = importController.getFileImporter(".gexf");
//            FileImporter fileImporter = importController.getFileImporter(new File("/Users/eduBecKs/Desktop/partitions.gephi"));
            System.out.println("@Var: fileImporter: " + fileImporter);

            Container container = importController.importFile(stream, fileImporter);

            importController.process(container, new DefaultProcessor(), workspace);

            //Add self loop
            GraphController graphController = Lookup.getDefault().lookup(GraphController.class);
            Graph graph = graphController.getModel().getGraph();
            Node node = graph.getNode(12);
            System.out.println("Self loop " + node.getNodeData().getLabel());
            graph.addEdge(graphController.getModel().factory().newEdge(node, node, 31, true));


            PartitionController partitionController = Lookup.getDefault().lookup(PartitionController.class);
            DynamicController dynamicController =  Lookup.getDefault().lookup(DynamicController.class);
            DynamicModel model = dynamicController.getModel();
            System.out.println("@Var: model: "+model);
            PartitionModel partitionModel = partitionController.getModel();
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                System.out.println("@Var: e: "+e);
            }
            partitionController.refreshPartitions();

            partitionController.refreshPartitions();
            partitionController.setSelectedPartitioning(PartitionModel.NODE_PARTITIONING);
            
            Partition[] partitionArray = partitionModel.getNodePartitions();
            partitionController.setSelectedPartition(partitionArray[0]);
            partitionController.transform(partitionModel.getSelectedPartition(), partitionModel.getSelectedTransformer());


//            System.out.println("@Var: partitionModel.getSelectedPartition(): " + partitionModel.getSelectedPartition());
//
//            partitionController.transform(partitionModel.getSelectedPartition(), partitionModel.getSelectedTransformer());

            //Set label edges       
            for (Edge edge : graphController.getModel().getGraph().getEdges()) {
                edge.getEdgeData().setLabel("Label test");
            }

        } catch (Exception e) {
            System.out.println("@Var: e: " + e);
        }
    }

}
