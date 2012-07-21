/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.gephi.legend.api;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import javax.print.attribute.standard.SheetCollate;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import org.gephi.data.attributes.api.AttributeModel;
import org.gephi.graph.api.Graph;
import org.gephi.legend.builders.DescriptionItemBuilder;
import org.gephi.legend.items.DescriptionItem;
import org.gephi.legend.items.ImageItem;
import org.gephi.legend.renderers.DescriptionItemRenderer;
import org.gephi.preview.api.Item;
import org.gephi.preview.api.PreviewModel;
import org.gephi.preview.api.PreviewProperties;
import org.gephi.preview.api.PreviewProperty;
import org.gephi.preview.spi.PreviewUI;
import org.gephi.legend.api.LegendNode.PreviewPropertyWrapper;
import org.gephi.legend.builders.*;
import org.gephi.legend.items.*;
import org.gephi.preview.api.*;
import org.gephi.project.api.ProjectController;
import org.gephi.project.api.Workspace;
import org.openide.util.NbBundle;
import org.openide.util.lookup.ServiceProvider;
import org.openide.explorer.propertysheet.PropertySheet;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.nodes.Node;
import org.openide.nodes.PropertySupport;
import org.openide.nodes.Sheet;
import org.openide.util.Lookup;

/**
 *
 * @author edubecks
 */
@ServiceProvider(service = PreviewUI.class, position = 404)
public class LegendManagerUI extends javax.swing.JPanel implements PreviewUI {

    /**
     * Creates new form LegendManagerUI
     */
    public LegendManagerUI() {
        initComponents();


        String[] legendTypes = {
            TextItem.LEGEND_TYPE,
            GroupsItem.LEGEND_TYPE,
            DescriptionItem.LEGEND_TYPE,
            ImageItem.LEGEND_TYPE,
            TableItem.LEGEND_TYPE
        };

        for (String legendType : legendTypes) {
            legendItemsComboBox.addItem(legendType);
        }

        numberOfItemsLabel.setVisible(false);
        numberOfItemsTextField.setVisible(false);

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        addLegendButton = new javax.swing.JButton();
        legendItemsComboBox = new javax.swing.JComboBox();
        legendManagerPanel = new javax.swing.JPanel();
        activeLegendsComboBox = new javax.swing.JComboBox();
        activeLegendLabel = new javax.swing.JLabel();
        legendPropertiesPanel = new javax.swing.JPanel();
        removeLegendButton = new javax.swing.JButton();
        numberOfItemsTextField = new javax.swing.JTextField();
        numberOfItemsLabel = new javax.swing.JLabel();

        setLayout(new java.awt.GridBagLayout());

        addLegendButton.setText(org.openide.util.NbBundle.getMessage(LegendManagerUI.class, "LegendManagerUI.addLegendButton.text")); // NOI18N
        addLegendButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addLegendButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        add(addLegendButton, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        add(legendItemsComboBox, gridBagConstraints);

        legendManagerPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, org.openide.util.NbBundle.getMessage(LegendManagerUI.class, "LegendManagerUI.legendManagerPanel.border.title"), javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.TOP)); // NOI18N
        legendManagerPanel.setLayout(new java.awt.GridBagLayout());

        activeLegendsComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                activeLegendsComboBoxActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        legendManagerPanel.add(activeLegendsComboBox, gridBagConstraints);

        activeLegendLabel.setText(org.openide.util.NbBundle.getMessage(LegendManagerUI.class, "LegendManagerUI.activeLegendLabel.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        legendManagerPanel.add(activeLegendLabel, gridBagConstraints);

        legendPropertiesPanel.setMinimumSize(new java.awt.Dimension(152, 57));
        legendPropertiesPanel.setPreferredSize(new java.awt.Dimension(152, 57));
        legendPropertiesPanel.setLayout(new java.awt.BorderLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        legendManagerPanel.add(legendPropertiesPanel, gridBagConstraints);

        removeLegendButton.setText(org.openide.util.NbBundle.getMessage(LegendManagerUI.class, "LegendManagerUI.removeLegendButton.text")); // NOI18N
        removeLegendButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                removeLegendButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        legendManagerPanel.add(removeLegendButton, gridBagConstraints);

        numberOfItemsTextField.setColumns(2);
        numberOfItemsTextField.setText(org.openide.util.NbBundle.getMessage(LegendManagerUI.class, "LegendManagerUI.numberOfItemsTextField.text")); // NOI18N
        numberOfItemsTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                numberOfItemsTextFieldActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        legendManagerPanel.add(numberOfItemsTextField, gridBagConstraints);

        numberOfItemsLabel.setText(org.openide.util.NbBundle.getMessage(LegendManagerUI.class, "LegendManagerUI.numberOfItemsLabel.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        legendManagerPanel.add(numberOfItemsLabel, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        add(legendManagerPanel, gridBagConstraints);
    }// </editor-fold>//GEN-END:initComponents

    public void refreshActiveLegendsComboBox() {

        ProjectController projectController = Lookup.getDefault().lookup(ProjectController.class);
        Workspace workspace = projectController.getCurrentWorkspace();


        PreviewController previewController = Lookup.getDefault().lookup(PreviewController.class);
        PreviewModel previewModel = previewController.getModel(workspace);
        PreviewProperties previewProperties = previewModel.getProperties();

        // creating if it doesnt exist
        if (!previewProperties.hasProperty(LegendManager.LEGEND_PROPERTIES)) {
            LegendManager legendManager = new LegendManager();
            previewProperties.putValue(LegendManager.LEGEND_PROPERTIES, legendManager);
        }
        LegendManager legendManager = previewProperties.getValue(LegendManager.LEGEND_PROPERTIES);
        Item activeLegendItem = legendManager.getActiveLegendItem();
        System.out.println("@Var: refreshActiveLegendsComboBox activeLegend: " + activeLegendItem);
        activeLegendsComboBox.removeAllItems();
        if (activeLegendItem != null) {
            ArrayList<Item> legendItems = legendManager.getLegendItems();
            for (Item item : legendItems) {
                activeLegendsComboBox.addItem(item);
            }

//        activeLegendsComboBox.setSelectedIndex(activeLegend);
            activeLegendsComboBox.setSelectedItem(activeLegendItem);
        }
        else {
            activeLegendsComboBox.setSelectedIndex(-1);
        }

    }

    private void addLegendButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addLegendButtonActionPerformed
        Graph graph = null;
        AttributeModel attributeModel = null;

        ProjectController projectController = Lookup.getDefault().lookup(ProjectController.class);
        Workspace workspace = projectController.getCurrentWorkspace();


        PreviewController previewController = Lookup.getDefault().lookup(PreviewController.class);
        PreviewModel previewModel = previewController.getModel(workspace);
        
        // check if previewModel exists
        if(previewModel==null){
            return;
        }
        
        PreviewProperties previewProperties = previewModel.getProperties();

        String selectedType = legendItemsComboBox.getSelectedItem().toString();
        System.out.println("@Var: selectedType: " + selectedType);


        if (!previewProperties.hasProperty(LegendManager.LEGEND_PROPERTIES)) {
            LegendManager legendManager = new LegendManager();
            previewProperties.putValue(LegendManager.LEGEND_PROPERTIES, legendManager);
        }

        LegendManager legendManager = previewProperties.getValue(LegendManager.LEGEND_PROPERTIES);
        Integer newItemIndex = legendManager.getCurrentIndex();

        LegendItemBuilder builder = null;
        if (selectedType.equals(DescriptionItem.LEGEND_TYPE)) {
            builder = new DescriptionItemBuilder();

        }
        else if (selectedType.equals(TextItem.LEGEND_TYPE)) {
            builder = new TextItemBuilder();
        }
        else if (selectedType.equals(GroupsItem.LEGEND_TYPE)) {
            builder = new GroupsItemBuilder();
        }
        else if (selectedType.equals(ImageItem.LEGEND_TYPE)) {
            builder = new ImageItemBuilder();
        }
        else if (selectedType.equals(TableItem.LEGEND_TYPE)) {
            builder = new TableItemBuilder();
        }

        Item item = builder.createItem(newItemIndex, graph, attributeModel);


        legendManager.addItem(item);
        Item activeLegendItem = legendManager.getActiveLegendItem();
        refreshActiveLegendsComboBox();
        PreviewProperty[] legendProperties = item.getData(LegendItem.PROPERTIES);
        for (PreviewProperty property : legendProperties) {
            previewController.getModel().getProperties().putValue(property.getName(), property.getValue());
        }
        PreviewProperty[] dynamicProperties = item.getData(LegendItem.DYNAMIC_PROPERTIES);
        for (PreviewProperty property : dynamicProperties) {
            previewController.getModel().getProperties().putValue(property.getName(), property.getValue());
        }
<<<<<<< HEAD
//        PreviewProperty[] realPositionProperties = item.getData(LegendItem.REAL_POSITION);
//        for (PreviewProperty property : realPositionProperties) {
//            previewController.getModel().getProperties().addProperty(property);
//        }
=======
        PreviewProperty[] realPositionProperties = item.getData(LegendItem.REAL_POSITION);
        for (PreviewProperty property : realPositionProperties) {
            previewController.getModel().getProperties().putValue(property.getName(), property.getValue());
        }
>>>>>>> fe06096bce76b1727feb2be03c2ed25d2fafb521
        
        // update property sheet
        refreshPropertySheet(activeLegendItem);


//        currentUsedLegends.revalidate();
    }//GEN-LAST:event_addLegendButtonActionPerformed

    private void refreshPropertySheet(Item activeLegendItem) {
        PreviewController previewController = Lookup.getDefault().lookup(PreviewController.class);
        PreviewModel previewModel = previewController.getModel();
        
        // check if previewModel exists
        if(previewModel==null){
            return;
        }
        
        legendPropertiesPanel.removeAll();

        System.out.println("@Var:CREATING PROPERTIES FOR Sheet activeLegend: " + activeLegendItem);
        if (activeLegendItem != null) {
            PropertySheet propertySheet = new PropertySheet();

            propertySheet.setNodes(new Node[]{new LegendNode(propertySheet, activeLegendItem, previewModel.getProperties())});
            propertySheet.setDescriptionAreaVisible(true);
            // @bug: check
            legendPropertiesPanel.add(propertySheet, BorderLayout.CENTER);
        }
        legendPropertiesPanel.repaint();
    }

    private void removeLegendButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_removeLegendButtonActionPerformed

        // check wheter an element is active
        if (activeLegendsComboBox.getSelectedIndex() == -1) {
            return;
        }

        ProjectController projectController = Lookup.getDefault().lookup(ProjectController.class);
        Workspace workspace = projectController.getCurrentWorkspace();
        PreviewController previewController = Lookup.getDefault().lookup(PreviewController.class);
        PreviewModel previewModel = previewController.getModel(workspace);
        PreviewProperties previewProperties = previewModel.getProperties();

        if (previewProperties.hasProperty(LegendManager.LEGEND_PROPERTIES)) {

//            Integer activeLegend = activeLegendsComboBox.getSelectedIndex();
            Integer activeLegend = ((Item) activeLegendsComboBox.getSelectedItem()).getData(LegendItem.ITEM_INDEX);
            LegendManager legendManager = previewProperties.getValue(LegendManager.LEGEND_PROPERTIES);
            legendManager.removeItem(activeLegend);
            refreshActiveLegendsComboBox();
            System.out.println("@Var: legendManager.getActiveLegend(): " + legendManager.getActiveLegend());
            refreshPropertySheet(legendManager.getActiveLegendItem());

//            if (activeLegend != -1) {
//                System.out.println("Removing ..... @Var: activeLegend: " + activeLegend);
//                // clean previewProperties
//                for (PreviewProperty property : previewProperties.getProperties()) {
//
//                    if (LegendManager.getItemIndexFromProperty(property, activeLegend)) {
//                        previewProperties.removeProperty(property);
//                    }
////                activeLegendsComboBox.removeItemAt(activeLegend);
//                }
//                LegendManager legendManager = previewProperties.getValue(LegendManager.LEGEND_PROPERTIES);
//                legendManager.removeItem(activeLegend);
//            }
        }
    }//GEN-LAST:event_removeLegendButtonActionPerformed

    private void activeLegendsComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_activeLegendsComboBoxActionPerformed
        Item activeLegendItem = (Item) activeLegendsComboBox.getSelectedItem();
        System.out.println("@Var: item: " + activeLegendItem);

        if (activeLegendItem != null) {
            Integer activeLegend = (Integer) activeLegendItem.getData(LegendItem.ITEM_INDEX);
            System.out.println("+----------------------------------->>>>>>   @Var: activeLegend: " + activeLegend);
            refreshPropertySheet(activeLegendItem);

            Boolean hasDynamicProperties = activeLegendItem.getData(LegendItem.HAS_DYNAMIC_PROPERTIES);
            numberOfItemsLabel.setVisible(hasDynamicProperties);
            numberOfItemsTextField.setVisible(hasDynamicProperties);
            PreviewProperty[] properties = activeLegendItem.getData(LegendItem.DYNAMIC_PROPERTIES);

            System.out.println("@Var: properties.length: " + properties.length);
            numberOfItemsTextField.setText("" + properties.length);
        }
        else {
            refreshPropertySheet(null);
        }


    }//GEN-LAST:event_activeLegendsComboBoxActionPerformed

    private void numberOfItemsTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_numberOfItemsTextFieldActionPerformed

        ProjectController projectController = Lookup.getDefault().lookup(ProjectController.class);
        Workspace workspace = projectController.getCurrentWorkspace();
        PreviewController previewController = Lookup.getDefault().lookup(PreviewController.class);
        PreviewModel previewModel = previewController.getModel(workspace);
        PreviewProperties previewProperties = previewModel.getProperties();

        System.out.printf("Refresh property sheet\n");

        if (!numberOfItemsTextField.getText().isEmpty()) {

            int numberOfItems = Integer.parseInt(numberOfItemsTextField.getText());
            System.out.println("@Var: numberOfItems: " + numberOfItems);
            Item item = (Item) activeLegendsComboBox.getSelectedItem();
            if (LegendItemBuilder.updatePreviewProperty(item, numberOfItems)) {
                System.out.printf("Refresh property sheet\n");
                LegendManager legendManager = previewProperties.getValue(LegendManager.LEGEND_PROPERTIES);
                Item activeLegendItem = legendManager.getActiveLegendItem();
                refreshPropertySheet(activeLegendItem);
//                legendManager.refreshActiveLegendsComboBox();
            }
        }
    }//GEN-LAST:event_numberOfItemsTextFieldActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel activeLegendLabel;
    private javax.swing.JComboBox activeLegendsComboBox;
    private javax.swing.JButton addLegendButton;
    private javax.swing.JComboBox legendItemsComboBox;
    private javax.swing.JPanel legendManagerPanel;
    private javax.swing.JPanel legendPropertiesPanel;
    private javax.swing.JLabel numberOfItemsLabel;
    private javax.swing.JTextField numberOfItemsTextField;
    private javax.swing.JButton removeLegendButton;
    // End of variables declaration//GEN-END:variables

    @Override
    public void setup(PreviewModel previewModel) {
    }

    @Override
    public JPanel getPanel() {
        refreshActiveLegendsComboBox();
        return this;
    }

    @Override
    public void unsetup() {
    }

    @Override
    public Icon getIcon() {
        return new ImageIcon();
    }

    @Override
    public String getPanelTitle() {
        return NbBundle.getMessage(LegendManagerUI.class, "LegendManagerUI.title");
    }

}
