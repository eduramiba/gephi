/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.gephi.legend.plugin.builders.description.elements;

import org.gephi.legend.api.DescriptionItemElementValue;
import org.openide.util.NbBundle;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author edubecks
 */
@ServiceProvider(service=DescriptionItemElementValue.class,position=1)
public class CustomValue extends DescriptionItemElementValue{

    public CustomValue() {
    }
    
    public static final String EMPTY_VALUE = "";
    
    @Override
    public String getValue() {
        return EMPTY_VALUE;
    }

    @Override
    public String getTitle() {
        return NbBundle.getMessage(CustomValue.class, "DescriptionItemElementValue.CustomValue.displayName");
    }

    @Override
    public String getDescription() {
        return NbBundle.getMessage(CustomValue.class, "DescriptionItemElementValue.CustomValue.description");
    }
    
}
