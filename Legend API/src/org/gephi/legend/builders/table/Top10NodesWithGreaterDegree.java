/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.gephi.legend.builders.table;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import org.gephi.graph.api.Graph;
import org.gephi.graph.api.GraphController;
import org.gephi.graph.api.GraphModel;
import org.gephi.graph.api.Node;
import org.gephi.legend.api.CustomLegendItemBuilder;
import org.gephi.legend.api.CustomTableItemBuilder;
import org.gephi.legend.items.TableItem;
import org.openide.util.Lookup;
import org.openide.util.NbBundle;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author edubecks
 */
@ServiceProvider(service = CustomTableItemBuilder.class, position = 2)
public class Top10NodesWithGreaterDegree extends CustomLegendItemBuilder implements CustomTableItemBuilder {

    @Override
    public String getDescription() {
        return NbBundle.getMessage(AverageNumberOfNodesInPartition.class, "Table.builder.Top10NodesWithGreaterDegree.description");
    }

    @Override
    public String getTitle() {
        return NbBundle.getMessage(AverageNumberOfNodesInPartition.class, "Table.builder.Top10NodesWithGreaterDegree.title");
    }

    @Override
    public void retrieveData(ArrayList<TableItem.LabelSelection> labels, ArrayList<String> horizontalLabels, ArrayList<String> verticalLabels, ArrayList<ArrayList<Float>> values, ArrayList<Color> horizontalColors, ArrayList<Color> verticalColors, ArrayList<ArrayList<Color>> valueColors) {
        GraphController graphController = Lookup.getDefault().lookup(GraphController.class);
        GraphModel model = graphController.getModel();
        Graph graph = model.getGraph();

        List<Node> nodes = Arrays.asList(graph.getNodes().toArray());
        Collections.sort(nodes, new NodeSort(graph));
        
        int topResults = 10;
        // FILLING HORIZONTAL LABELS AND COLORS
        for (int i = 0; i < topResults; i++) {
//            StringBuilder label = new StringBuilder(nodes.get(i).getNodeData().getLabel());
            String label = nodes.get(i).getNodeData().getLabel();
            horizontalLabels.add(label);
            horizontalColors.add(Color.BLUE);
        }
        labels.add(TableItem.LabelSelection.HORIZONTAL);
        
        // FILLING VERTICAL LABELS
//        StringBuilder value = new StringBuilder("value");
        String value = "value";
        verticalLabels.add(value);
        verticalColors.add(Color.BLUE);
        
        // FILLING VALUES AND COLORS
        for (int i = 0; i < topResults; i++) {
            ArrayList<Float> row = new ArrayList<Float>();
            row.add((float)graph.getDegree(nodes.get(i)));
            values.add(row);
            ArrayList<Color> colorsRow = new ArrayList<Color>();
            colorsRow.add(Color.BLACK);
            valueColors.add(colorsRow);
        }
        

    }

    @Override
    public boolean isAvailableToBuild() {
        return true;
    }

    @Override
    public String stepsNeededToBuild() {
        return NONE_NEEDED;
    }

    private class NodeSort implements Comparator<Node> {

        private Graph graph;
        
        public NodeSort(Graph graph) {
            this.graph = graph;
        }
        
        @Override
        public int compare(Node n1, Node n2) {
            return graph.getDegree(n2) - graph.getDegree(n1);
        }

    }
}
