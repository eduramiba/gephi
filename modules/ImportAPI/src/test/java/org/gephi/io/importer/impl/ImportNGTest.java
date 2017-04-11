package org.gephi.io.importer.impl;

import java.lang.reflect.Method;
import java.util.Iterator;
import org.gephi.graph.api.Edge;
import org.gephi.graph.api.Graph;
import org.gephi.graph.api.GraphController;
import org.gephi.io.importer.api.ContainerLoader;
import org.gephi.io.importer.api.ContainerUnloader;
import org.gephi.io.importer.api.EdgeDraft;
import org.gephi.io.importer.api.EdgeMergeStrategy;
import org.gephi.io.importer.api.ImportController;
import org.gephi.io.importer.api.Issue;
import org.gephi.io.importer.api.NodeDraft;
import org.gephi.io.importer.api.Report;
import org.gephi.io.processor.spi.Processor;
import org.gephi.project.api.ProjectController;
import org.gephi.project.api.Workspace;
import org.openide.util.Lookup;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 *
 * @author Eduardo Ramos
 */
public class ImportNGTest {

    private final ProjectController projectController = Lookup.getDefault().lookup(ProjectController.class);
    private final ImportController importController = Lookup.getDefault().lookup(ImportController.class);
    private final GraphController graphController = Lookup.getDefault().lookup(GraphController.class);
    private final Processor defaultProcessor = Lookup.getDefault().lookup(Processor.class);
    private Workspace workspace;
    private ImportContainerImpl container;
    
    private static final double EPS = 0.001;

    @BeforeMethod
    public void setup(Method method) {
        String testName = method.getName(); 
        System.out.println("Starting test: " + testName);
        
        projectController.newProject();
        workspace = projectController.getCurrentWorkspace();

        container = new ImportContainerImpl();
        defaultProcessor.setContainers(new ContainerUnloader[]{container});

        Report containerReport = new Report();
        container.setReport(containerReport);
    }

    @AfterMethod
    public void teardown() {
        projectController.closeCurrentProject();
        workspace = null;
        container = null;
    }

    private void showReport(Report report) {
        System.out.println(report.getText());
        Iterator<Issue> issuesIterator = report.getIssues(100);
        while (issuesIterator.hasNext()) {
            Issue issue = issuesIterator.next();

            System.out.println(issue);
        }
    }

    private NodeDraft buildNode(ContainerLoader container, String id) {
        NodeDraft node = container.factory().newNodeDraft(id);
        return node;
    }

    private EdgeDraft buildEdge(ContainerLoader container, NodeDraft source, NodeDraft target) {
        return buildEdge(container, source, target, 1);
    }

    private EdgeDraft buildEdge(ContainerLoader container, NodeDraft source, NodeDraft target, double weight) {
        EdgeDraft edge = container.factory().newEdgeDraft();
        edge.setSource(source);
        edge.setTarget(target);
        edge.setWeight(weight);

        Assert.assertNotNull(edge);

        return edge;
    }

    private void buildMergeWeightsTestGraph() {
        NodeDraft node1 = buildNode(container, "1");
        NodeDraft node2 = buildNode(container, "2");

        container.addNode(node1);
        container.addNode(node2);

        EdgeDraft edge12_1 = buildEdge(container, node1, node2);
        EdgeDraft edge12_2 = buildEdge(container, node1, node2);
        EdgeDraft edge12_3 = buildEdge(container, node1, node2, 4.2);
        EdgeDraft edge21 = buildEdge(container, node2, node1);
        EdgeDraft edge22 = buildEdge(container, node2, node2, 1.5);

        container.addEdge(edge12_1);
        container.addEdge(edge12_2);
        container.addEdge(edge12_3);
        container.addEdge(edge21);
        container.addEdge(edge22);
    }

    private void checkWeightsSummed() {
        importController.process(container, defaultProcessor, workspace);

        showReport(container.getReport());
        showReport(defaultProcessor.getReport());
        
        Assert.assertTrue(defaultProcessor.getReport().isEmpty());

        Graph graph = graphController.getGraphModel(workspace).getGraph();

        Assert.assertEquals(graph.getNodeCount(), 2);
        Assert.assertEquals(graph.getEdgeCount(), 3);

        Edge edge12 = graph.getEdge(graph.getNode("1"), graph.getNode("2"));
        Assert.assertNotNull(edge12);
        Assert.assertEquals(edge12.getWeight(), 6.2);
    }

    private void checkWeightsAveraged() {
        importController.process(container, defaultProcessor, workspace);

        showReport(container.getReport());
        showReport(defaultProcessor.getReport());
        
        Assert.assertTrue(defaultProcessor.getReport().isEmpty());

        Graph graph = graphController.getGraphModel(workspace).getGraph();

        Assert.assertEquals(graph.getNodeCount(), 2);
        Assert.assertEquals(graph.getEdgeCount(), 3);

        Edge edge12 = graph.getEdge(graph.getNode("1"), graph.getNode("2"));
        Assert.assertNotNull(edge12);
        Assert.assertEquals(edge12.getWeight(), 2.0666, EPS);
    }

    @Test
    public void testProcessContainer_Default_Weights_Merged_as_Sum() {
        buildMergeWeightsTestGraph();
        checkWeightsSummed();
    }

    @Test
    public void testProcessContainer_Weights_Sum() {
        buildMergeWeightsTestGraph();

        container.setEdgesMergeStrategy(EdgeMergeStrategy.SUM);

        checkWeightsSummed();
    }

    @Test
    public void testProcessContainer_Weights_Average() {
        buildMergeWeightsTestGraph();

        container.setEdgesMergeStrategy(EdgeMergeStrategy.AVG);

        checkWeightsAveraged();
    }
}
