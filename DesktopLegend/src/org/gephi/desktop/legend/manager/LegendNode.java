/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.gephi.desktop.legend.manager;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.lang.reflect.InvocationTargetException;
import org.gephi.legend.spi.LegendItem;
import org.gephi.preview.api.*;
import org.openide.explorer.propertysheet.PropertySheet;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.nodes.Node;
import org.openide.nodes.PropertySupport;
import org.openide.nodes.Sheet;
import org.openide.util.NbBundle;

/**
 *
 * @author eduBecKs
 */
public class LegendNode extends AbstractNode implements PropertyChangeListener {
    private final PreviewProperties previewProperties;

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        throw new UnsupportedOperationException("Not supported yet.");
    }


    public LegendNode(PropertySheet propertySheet, Item activeLegendItem, PreviewProperties previewProperties) {
        super(Children.LEAF);
        this.propertySheet = propertySheet;
        this.activeLegendItem = activeLegendItem;
        this.previewProperties = previewProperties;
        setDisplayName(NbBundle.getMessage(LegendNode.class, "LegendNode.displayName"));
    }

    @Override
    protected Sheet createSheet() {

        Sheet sheet = Sheet.createDefault();
        
        
        // regular properties
        PreviewProperty[] properties = activeLegendItem.getData(LegendItem.PROPERTIES);
        String label = properties[0].getValue();
        Sheet.Set itemSet = Sheet.createPropertiesSet();
        itemSet.setDisplayName(label);
        itemSet.setName(label);
        for (PreviewProperty property : properties) {
            Node.Property nodeProperty = new LegendPropertyWrapper(property, previewProperties);
            itemSet.put(nodeProperty);
        }
        
        // own properties
        PreviewProperty[] ownProperties = activeLegendItem.getData(LegendItem.OWN_PROPERTIES);
        for (PreviewProperty property : ownProperties) {
            Node.Property nodeProperty = new LegendPropertyWrapper(property, previewProperties);
            itemSet.put(nodeProperty);
        }
        
        
        // dynamic properties
        PreviewProperty[] dynamicProperties = activeLegendItem.getData(LegendItem.DYNAMIC_PROPERTIES);
        for (PreviewProperty property : dynamicProperties) {
            Node.Property nodeProperty = new LegendPropertyWrapper(property, previewProperties);
            itemSet.put(nodeProperty);
        }
        
        
        sheet.put(itemSet);

        return sheet;
    }

    public static class LegendPropertyWrapper extends PropertySupport.ReadWrite {

        private final PreviewProperty property;
        private final PreviewProperties previewProperties;

        public LegendPropertyWrapper(PreviewProperty previewProperty, PreviewProperties previewProperties) {
            super(previewProperty.getName(), previewProperty.getType(), previewProperty.getDisplayName(), previewProperty.getDescription());
            this.property = previewProperty;
            this.previewProperties = previewProperties;
        }

        @Override
        public Object getValue() throws IllegalAccessException, InvocationTargetException {
            return previewProperties.getValue(property.getName());
        }

        @Override
        public void setValue(Object t) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
            previewProperties.putValue(property.getName(), t);
            property.setValue(t);
        }

    }
    private PropertySheet propertySheet;
    private Item activeLegendItem;
}
