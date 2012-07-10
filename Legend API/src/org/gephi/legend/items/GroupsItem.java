/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.gephi.legend.items;

import org.gephi.legend.api.LegendItem;
import org.gephi.preview.api.PreviewProperty;
import org.gephi.preview.plugin.items.AbstractItem;

/**
 *
 * @author edubecks
 */
public class GroupsItem extends AbstractItem implements LegendItem {

    public static final String LEGEND_TYPE = "Groups Item";
    public static final String LABELS_GROUP = "labels group";
    public static final String COLORS_GROUP = "colors group";
    public static final String VALUES_GROUP = "values group";

    //BODY
    public GroupsItem(Object source) {
        super(source, TYPE);
    }

    @Override
    public String toString() {
        return (((PreviewProperty[]) this.getData(LegendItem.PROPERTIES))[0].getValue()) + " [" + LEGEND_TYPE + "]";
    }

    @Override
    public PreviewProperty[] getDynamicPreviewProperties() {
        return new PreviewProperty[0];
    }

}