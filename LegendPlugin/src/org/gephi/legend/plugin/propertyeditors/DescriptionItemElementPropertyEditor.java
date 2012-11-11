/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.gephi.legend.plugin.propertyeditors;

import java.awt.Component;
import java.beans.PropertyEditorSupport;
import org.gephi.legend.api.DescriptionItemElementValue;
import org.gephi.legend.plugin.builders.description.elements.CustomValue;
import org.gephi.legend.plugin.items.DescriptionItemElement;

/**
 *
 * @author edubecks
 */
public class DescriptionItemElementPropertyEditor extends PropertyEditorSupport {
    @Override
    public Component getCustomEditor() {
        DescriptionItemElementPanel descriptionItemPanel = new DescriptionItemElementPanel();
        descriptionItemPanel.setup(this);
        return descriptionItemPanel;
    }

    @Override
    public boolean supportsCustomEditor() {
        return true;
    }
    
    @Override
    public String getAsText() {
        DescriptionItemElement descriptionItemElement = (DescriptionItemElement) getValue();
        return descriptionItemElement.getValue();
    }

    @Override
    public void setAsText(String value) {
        DescriptionItemElementValue descriptionItemElementValue = new CustomValue();
        setValue(new DescriptionItemElement(descriptionItemElementValue, value));
    }

}
