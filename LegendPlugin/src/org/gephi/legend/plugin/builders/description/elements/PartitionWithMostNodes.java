/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.gephi.legend.plugin.builders.description.elements;

import org.gephi.legend.api.DescriptionItemElementValue;
import org.gephi.partition.api.Part;
import org.gephi.partition.api.PartitionController;
import org.gephi.partition.api.PartitionModel;
import org.openide.util.Lookup;
import org.openide.util.NbBundle;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author edubecks
 */
@ServiceProvider(service = DescriptionItemElementValue.class, position = 5)
public class PartitionWithMostNodes extends DescriptionItemElementValue {

    @Override
    public String getValue() {
        PartitionController partitionController = Lookup.getDefault().lookup(PartitionController.class);
        PartitionModel partitionModel = partitionController.getModel();
        if (partitionModel != null) {
            if (partitionModel.getSelectedPartition() != null) {
                Part partitionWithMostNodes = null;
                int maxNodes = Integer.MIN_VALUE;
                for (Part part : partitionModel.getSelectedPartition().getParts()) {
                    if(part.getObjects().length>maxNodes){
                        partitionWithMostNodes = part;
                    }
                }
                return partitionWithMostNodes.getDisplayName()+" ("+partitionWithMostNodes.getObjects().length +")";
            }
        }
        return "Apply some partition first!";
    }

    @Override
    public String getTitle() {
        return NbBundle.getMessage(PartitionWithMostNodes.class, "DescriptionItemElementValue.PartitionWithMostNodes.displayName");
    }

    @Override
    public String getDescription() {
        return NbBundle.getMessage(PartitionWithMostNodes.class, "DescriptionItemElementValue.PartitionWithMostNodes.description");
    }
}