/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.gephi.legend.plugin.builders.description.elements;

import org.gephi.graph.api.Graph;
import org.gephi.graph.api.GraphController;
import org.gephi.legend.api.DescriptionItemElementValue;
import org.openide.util.Lookup;
import org.openide.util.NbBundle;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author edubecks
 */
@ServiceProvider(service = DescriptionItemElementValue.class, position = 2)
public class NumberOfNodes extends DescriptionItemElementValue {

    @Override
    public String getValue() {
        GraphController graphController = Lookup.getDefault().lookup(GraphController.class);
        Graph graph = graphController.getModel().getGraph();
        return String.valueOf(graph.getNodeCount());
    }

    @Override
    public String getTitle() {
        return NbBundle.getMessage(NumberOfNodes.class, "DescriptionItemElementValue.NumberOfNodes.displayName");
    }

    @Override
    public String getDescription() {
        return NbBundle.getMessage(NumberOfNodes.class, "DescriptionItemElementValue.NumberOfNodes.description");
    }

}
