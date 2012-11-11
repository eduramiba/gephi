/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.gephi.legend.plugin.builders.description.elements;

import org.gephi.legend.api.DescriptionItemElementValue;
import org.gephi.partition.api.PartitionController;
import org.gephi.partition.api.PartitionModel;
import org.openide.util.Lookup;
import org.openide.util.NbBundle;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author edubecks
 */
@ServiceProvider(service = DescriptionItemElementValue.class, position = 3)
public class NumberOfGroups extends DescriptionItemElementValue {

    @Override
    public String getValue() {
        PartitionController partitionController = Lookup.getDefault().lookup(PartitionController.class);
        PartitionModel partitionModel = partitionController.getModel();
        if (partitionModel != null) {
            if (partitionModel.getSelectedPartition() != null) {
                return ""+partitionModel.getSelectedPartition().getPartsCount();
            }
        }
        return "1";
    }

    @Override
    public String getTitle() {
        return NbBundle.getMessage(NumberOfGroups.class, "DescriptionItemElementValue.NumberOfGroups.displayName");
    }

    @Override
    public String getDescription() {
        return NbBundle.getMessage(NumberOfGroups.class, "DescriptionItemElementValue.NumberOfGroups.description");
    }
}
