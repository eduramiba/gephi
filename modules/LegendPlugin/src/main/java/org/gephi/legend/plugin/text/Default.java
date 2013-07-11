/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.gephi.legend.plugin.text;

import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author mvvijesh
 */
@ServiceProvider(service=CustomTextItemBuilder.class, position=1)
public class Default implements CustomTextItemBuilder {
        @Override
    public String getDescription() {
        return DEFAULT_DESCRIPTION;
    }

    @Override
    public String getTitle() {
        return DEFAULT_TITLE;
    }


    @Override
    public boolean isAvailableToBuild() {
        return true;
    }

    @Override
    public String stepsNeededToBuild() {
        return NONE_NEEDED;
    }
}