/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.gephi.legend.spi;

/**
 *
 * @author edubecks
 */
public interface CustomLegendItemBuilder {
    
    /**
     * Provides an user friendly description for the builder
     * This name will appear as a tooltip in the builders list
     * @return User friendly renderer name, not null
     */
    public String getDescription();
    
    /**
     * Provides an user friendly name for the builder
     * This name will appear in the builders list at the legend manager UI.
     * @return User friendly renderer name, not null
     */
    public String getTitle();
    
    /**
     * Used to determine if the
     * <code>builder</code> can create a new item <br/>
     * Ex: GroupsItem can only be
     * built if some partition was applied first
     *
     * @return <code>true</code> if <code>item</code> has dynamic properties, 
     * <code>false</code> otherwise
     */
    public boolean isAvailableToBuild();

    /**
     * if the value of <code> isAvailableToBuild()</code> is true, it means 
     * that some steps are required in order to use the current Legend Item
     * Builder, then this function returns a description of the needed steps
     * @return 
     */
    public String stepsNeededToBuild();
    
    public static final String NONE_NEEDED="";
    public static final String DEFAULT_TITLE="Default";
    public static final String DEFAULT_DESCRIPTION="Description";
}
