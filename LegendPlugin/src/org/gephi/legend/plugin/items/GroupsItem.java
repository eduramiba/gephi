/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.gephi.legend.plugin.items;

import org.gephi.legend.api.LegendProperty;
import org.gephi.legend.spi.LegendItem;
import org.gephi.preview.api.PreviewProperty;

/**
 *
 * @author edubecks
 */
public class GroupsItem extends AbstractItem implements LegendItem {

    public static final String LEGEND_TYPE = "Groups Item";
    public static final String LABELS_IDS = "labels group";
    public static final String COLORS = "colors group";
    public static final String VALUES = "values group";
    public static final String NUMBER_OF_GROUPS = "number of groups";

    //BODY
    public GroupsItem(Object source) {
        super(source, LEGEND_TYPE);
    }

    @Override
    public String toString() {
        return (((PreviewProperty[]) this.getData(LegendItem.PROPERTIES))[LegendProperty.LABEL].getValue());
    }
}