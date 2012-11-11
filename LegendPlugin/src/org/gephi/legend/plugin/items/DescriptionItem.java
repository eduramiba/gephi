/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.gephi.legend.plugin.items;

import org.gephi.legend.api.LegendProperty;
import org.gephi.legend.plugin.builders.DescriptionItemBuilder;
import org.gephi.legend.spi.LegendItem;
import org.gephi.preview.api.PreviewProperty;

/**
 *
 * @author edubecks
 */
public class DescriptionItem extends AbstractItem implements LegendItem, LegendItem.DynamicItem {

    public static final String LEGEND_TYPE = "Description Item";
    public static final String KEY = "keys";
    public static final String VALUE = "values";
    public static final String NUMBER_OF_ITEMS = "number of items";

    public DescriptionItem(Object source) {
        super(source, LEGEND_TYPE);
    }

    @Override
    public String toString() {
        return (((PreviewProperty[]) this.getData(LegendItem.PROPERTIES))[LegendProperty.LABEL].getValue());
    }

    @Override
    public PreviewProperty[] getDynamicPreviewProperties() {
        return (PreviewProperty[]) this.getData(DYNAMIC_PROPERTIES);
    }

    @Override
    public void updateDynamicProperties(int numOfProperties) {
        int currentNumOfProperties = ((Integer) (getData(LegendItem.NUMBER_OF_DYNAMIC_PROPERTIES))).intValue();

        if (numOfProperties > currentNumOfProperties) {
            int newProperties = numOfProperties - currentNumOfProperties;

            DescriptionItemBuilder.addPreviewProperty(this, currentNumOfProperties, newProperties);
        } else {// removing properties
            int removeProperties = currentNumOfProperties - numOfProperties;
            DescriptionItemBuilder.removePreviewProperty(this, removeProperties);
        }
    }
}