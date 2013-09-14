/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.gephi.desktop.legend.manager;

import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Collection;
import javax.swing.DefaultListCellRenderer;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import org.gephi.attribute.api.AttributeModel;
import org.gephi.desktop.preview.api.PreviewUIController;
import org.gephi.graph.api.Graph;
import org.gephi.legend.api.LegendController;
import org.gephi.legend.api.LegendModel;
import org.gephi.legend.api.LegendProperty;
import org.gephi.legend.api.BlockNode;
import org.gephi.legend.inplaceeditor.InplaceEditor;
import org.gephi.legend.spi.CustomLegendItemBuilder;
import org.gephi.legend.spi.LegendItem;
import org.gephi.legend.spi.LegendItemBuilder;
import org.gephi.legend.spi.LegendItemRenderer;
import org.gephi.preview.api.*;
import org.gephi.preview.spi.PreviewUI;
import org.gephi.preview.spi.Renderer;
import org.openide.explorer.propertysheet.PropertySheet;
import org.openide.nodes.Node;
import org.openide.util.Lookup;
import org.openide.util.NbBundle;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author edubecks
 */
@ServiceProvider(service = PreviewUI.class, position = 404)
public class LegendManagerUI extends javax.swing.JPanel implements PreviewUI, PropertyChangeListener {

    private final LegendController legendController;
    private final PreviewUIController previewUIController;
    private JTable layerOrder;

    /**
     * Creates new form LegendManagerUI
     */
    public LegendManagerUI() {
        legendController = LegendController.getInstance();
        previewUIController = Lookup.getDefault().lookup(PreviewUIController.class);

        legendController.addListener(this);

        initComponents();
    }

    @Override
    public void propertyChange(PropertyChangeEvent event) {
        // when a different element is selected in UI, the same must be reflected in the layers panel as well.
        if (event.getSource().getClass().equals(legendController.getClass()) && event.getPropertyName().equals(LegendController.LEGEND_ITEM_SELECTED)) {
            Item selectedItem = (Item) event.getNewValue();
            if (selectedItem != null) {
                LegendModel legendModel = legendController.getLegendModel();
                legendModel.setPickedLegend(legendModel.getListIndexFromItemIndex((Integer) selectedItem.getData(LegendItem.ITEM_INDEX)));
                refreshLayers();
            }
        }
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

        controls = new javax.swing.JPanel();
        addLegendButton = new javax.swing.JButton();
        removeLegendButton = new javax.swing.JButton();
        moveLayers = new javax.swing.JPanel();
        moveUp = new javax.swing.JButton();
        moveDown = new javax.swing.JButton();
        legendManagerPanel = new javax.swing.JPanel();
        legendLayers = new javax.swing.JPanel();

        setLayout(new javax.swing.BoxLayout(this, javax.swing.BoxLayout.Y_AXIS));

        controls.setLayout(new javax.swing.BoxLayout(controls, javax.swing.BoxLayout.LINE_AXIS));

        addLegendButton.setText(org.openide.util.NbBundle.getMessage(LegendManagerUI.class, "LegendManagerUI.addLegendButton.text")); // NOI18N
        addLegendButton.setToolTipText(org.openide.util.NbBundle.getMessage(LegendManagerUI.class, "LegendManagerUI.addLegendButton.toolTipText")); // NOI18N
        addLegendButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addLegendButtonActionPerformed(evt);
            }
        });
        controls.add(addLegendButton);

        removeLegendButton.setText(org.openide.util.NbBundle.getMessage(LegendManagerUI.class, "LegendManagerUI.removeLegendButton.text")); // NOI18N
        removeLegendButton.setToolTipText(org.openide.util.NbBundle.getMessage(LegendManagerUI.class, "LegendManagerUI.removeLegendButton.toolTipText")); // NOI18N
        removeLegendButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                removeLegendButtonActionPerformed(evt);
            }
        });
        controls.add(removeLegendButton);

        moveLayers.setLayout(new javax.swing.BoxLayout(moveLayers, javax.swing.BoxLayout.Y_AXIS));

        moveUp.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/gephi/desktop/legend/graphics/moveup_8.png"))); // NOI18N
        moveUp.setText(org.openide.util.NbBundle.getMessage(LegendManagerUI.class, "LegendManagerUI.moveUp.text")); // NOI18N
        moveUp.setToolTipText(org.openide.util.NbBundle.getMessage(LegendManagerUI.class, "LegendManagerUI.moveUp.toolTipText")); // NOI18N
        moveUp.setMargin(new java.awt.Insets(0, 0, 0, 0));
        moveUp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                moveUpActionPerformed(evt);
            }
        });
        moveLayers.add(moveUp);

        moveDown.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/gephi/desktop/legend/graphics/movedown_8.png"))); // NOI18N
        moveDown.setText(org.openide.util.NbBundle.getMessage(LegendManagerUI.class, "LegendManagerUI.moveDown.text")); // NOI18N
        moveDown.setToolTipText(org.openide.util.NbBundle.getMessage(LegendManagerUI.class, "LegendManagerUI.moveDown.toolTipText")); // NOI18N
        moveDown.setMargin(new java.awt.Insets(0, 0, 0, 0));
        moveDown.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                moveDownActionPerformed(evt);
            }
        });
        moveLayers.add(moveDown);

        controls.add(moveLayers);

        add(controls);

        legendManagerPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, org.openide.util.NbBundle.getMessage(LegendManagerUI.class, "LegendManagerUI.legendManagerPanel.border.title_1"), javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Andale Mono", 0, 14))); // NOI18N
        legendManagerPanel.setToolTipText(org.openide.util.NbBundle.getMessage(LegendManagerUI.class, "LegendManagerUI.legendManagerPanel.toolTipText")); // NOI18N
        legendManagerPanel.setMinimumSize(new java.awt.Dimension(162, 78));
        legendManagerPanel.setPreferredSize(new java.awt.Dimension(162, 78));
        legendManagerPanel.setLayout(new java.awt.GridBagLayout());

        legendLayers.setMinimumSize(new java.awt.Dimension(152, 57));
        legendLayers.setPreferredSize(new java.awt.Dimension(152, 57));
        legendLayers.setLayout(new java.awt.BorderLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        legendManagerPanel.add(legendLayers, gridBagConstraints);

        add(legendManagerPanel);
        legendManagerPanel.getAccessibleContext().setAccessibleName(org.openide.util.NbBundle.getMessage(LegendManagerUI.class, "LegendManagerUI.legendManagerPanel.AccessibleContext.accessibleName")); // NOI18N
    }// </editor-fold>//GEN-END:initComponents

    private void moveDownActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_moveDownActionPerformed
        // This method is executed when the user clicks on the move down button.

        LegendModel legendModel = legendController.getLegendModel();

        // moving a layer down is same as swapping the active legend with the previous legend. Hence, find the indices for swapping.
        int pickedLegendIndex = legendModel.getPickedLegend();
        int previousActiveLegend = legendModel.getPreviousActiveLegend(); // getPreviousActiveLegend() returns -1 if the active Legend is already at the bottom and cannot be moved further down.

        if (previousActiveLegend != -1) {
            legendModel.swapItems(pickedLegendIndex, previousActiveLegend);
            legendModel.setPickedLegend(previousActiveLegend);
            refreshLayers();
        }
        
        previewUIController.refreshPreview();
    }//GEN-LAST:event_moveDownActionPerformed

    private void addLegendButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addLegendButtonActionPerformed
        // this method is executed when the user clicks on the add button in the legend manager.

        // show a dialog box that lists all the legend builders. The user is expected to choose any one.
        Collection<? extends LegendItemBuilder> availableBuilders = legendController.getAvailableBuilders();
        Object[] availableBuildersArray = availableBuilders.toArray();
        LegendItemBuilder chosenLegend = (LegendItemBuilder) JOptionPane.showInputDialog(this, "Choose the type of Legend:", "Add Legend", JOptionPane.PLAIN_MESSAGE, null, availableBuildersArray, availableBuildersArray[0]);

        // if the user hits cancel, chosenLegend will become null, and no further operations must take place.
        if (chosenLegend != null) {
            // retrieve the list of custom builders. if there are more than one builders (including the Default), allow the user to select the custom builder by providing a dialog.

            ArrayList<CustomLegendItemBuilder> chosenLegendCustomBuilders = chosenLegend.getAvailableBuilders();
            CustomLegendItemBuilder chosenLegendCustomBuilder = chosenLegendCustomBuilders.get(0); //for Default
            //if there is more than one custom builder, allow the user to choose between them.
            if (chosenLegendCustomBuilders.size() > 1) {
                Object[] chosenLegendCustomBuildersArray = chosenLegendCustomBuilders.toArray();
                chosenLegendCustomBuilder = (CustomLegendItemBuilder) JOptionPane.showInputDialog(this, "Choose the custom builder:", "Custom Builder", JOptionPane.PLAIN_MESSAGE, null, chosenLegendCustomBuildersArray, chosenLegendCustomBuildersArray[0]);
            }

            // if the user hits cancel, chosenLegendCustomBuilder will become null, and no further operations must take place.
            if (chosenLegendCustomBuilder != null) {
                if (chosenLegendCustomBuilder.isAvailableToBuild()) {
                    // build the legend item
                    LegendModel legendModel = legendController.getLegendModel();
                    Integer newItemIndex = legendModel.getNextItemIndex();
                    Item item = chosenLegend.createCustomItem(newItemIndex, null, null, chosenLegendCustomBuilder);

                    // adding item to legend model
                    legendController.addItemToLegendModel(item);

                    //update the renderer about the number of items
                    LegendItemRenderer legendItemRenderer = (LegendItemRenderer) Lookup.getDefault().lookup(LegendItemRenderer.class);
                    legendItemRenderer.setNumberOfLegendItems(legendModel.getNumberOfActiveItems());

                    // the user must be notified that the legend was actually added. Hence, update the legend layers panel.
                    refreshLayers();
                    previewUIController.refreshPreview();
                } else {
                    JOptionPane.showMessageDialog(this, chosenLegendCustomBuilder.stepsNeededToBuild(), NbBundle.getMessage(LegendModel.class, "LegendItem.stepsNeededToBuildItem"), JOptionPane.INFORMATION_MESSAGE, null);
                }
            }
        }
    }//GEN-LAST:event_addLegendButtonActionPerformed

    private void removeLegendButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_removeLegendButtonActionPerformed
        // This method is executed when the user clicks on the remove button. It removes the legend that is currently active.

        LegendModel legendModel = legendController.getLegendModel();
        int indexOfPickedLegend = legendModel.getPickedLegend();
        if (indexOfPickedLegend == -1) {
            //if index is -1, it means that that there are no active items
            JOptionPane.showMessageDialog(this, "There are no legends to be removed.");
        } else {
            // confirm removal
            int dialogResult = JOptionPane.showConfirmDialog(this, "Delete this legend?", "confirm removal", JOptionPane.YES_NO_OPTION);
            if (dialogResult == JOptionPane.YES_OPTION) {

                Item item = legendModel.getItemAtIndex(indexOfPickedLegend);
                legendModel.removeBlockTree((Integer) item.getData(LegendItem.ITEM_INDEX));

                InplaceEditor ipeditor = legendModel.getInplaceEditor();
                if (ipeditor != null) {
                    BlockNode node = ipeditor.getData(InplaceEditor.BLOCKNODE);
                    Item currentInplaceItem = node.getItem();
                    if ((Integer) currentInplaceItem.getData(LegendItem.ITEM_INDEX) == (Integer) item.getData(LegendItem.ITEM_INDEX)) {
                        legendModel.setInplaceEditor(null);
                    }
                }

                legendModel.removeItem(indexOfPickedLegend);

                //update the renderer about the number of items
                LegendItemRenderer legendItemRenderer = (LegendItemRenderer) Lookup.getDefault().lookup(LegendItemRenderer.class);
                legendItemRenderer.setNumberOfLegendItems(legendModel.getNumberOfActiveItems());

                refreshLayers();
                previewUIController.refreshPreview();
            }
        }
    }//GEN-LAST:event_removeLegendButtonActionPerformed

    private void moveUpActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_moveUpActionPerformed
        // This method is executed when the user clicks on the move up button.

        LegendModel legendManager = legendController.getLegendModel();
        // moving a layer up is same as swapping the active legend with the next legend. Hence, find the indices for swapping.
        int pickedLegendIndex = legendManager.getPickedLegend();
        int nextActiveLegend = legendManager.getNextActiveLegend(); // getNextActiveLegend() returns -1 if the active Legend is already at the top and cannot be moved further up.

        if (nextActiveLegend != -1) {
            legendManager.swapItems(pickedLegendIndex, nextActiveLegend);
            legendManager.setPickedLegend(nextActiveLegend);
            refreshLayers();
        }
        
        previewUIController.refreshPreview();
    }//GEN-LAST:event_moveUpActionPerformed

    private JTable getLegendLayerModel() {
        // This method fetches the names all the active legends, arranges them in the form of a table and returns the table.
        // The names of the legends can be changed by double-clicking on the legend layer, editing it and pressing return.

        /* Reason to have a JTable:
         * This is a step towards extendibility. The columns can be added as and when there is a requirement.
         * (For example, you can have a button directly to perform a particular action for the corresponding legend layer.)
         * Right now, we have 1 column, for the user defined name.
         * When an layer is clicked, we can easily figure out what was clicked by finding out the row and column, instead of having callbacks registered for each element.
         */

        LegendModel legendManager = legendController.getLegendModel();
        ArrayList<Item> items = legendManager.getActiveItems(); // fetches all the active legend items.

        int numberOfActiveItems = legendManager.getNumberOfActiveItems();
        // column names should be specified
        String[] columnNames = {"Legends"};

        // data in the rows must be constructed
        Object[][] rowData = new Object[numberOfActiveItems][];
        for (int i = 0; i < numberOfActiveItems; i++) {
            PreviewProperty[] props = (PreviewProperty[]) items.get(i).getData(LegendItem.PROPERTIES);
            PreviewProperty name = props[LegendProperty.USER_LEGEND_NAME];

            rowData[numberOfActiveItems - i - 1] = new Object[columnNames.length];
            rowData[numberOfActiveItems - i - 1][0] = name.getValue();
        }
        // build a table model with the rows and column names. construct a table with the table model.
        DefaultTableModel legendLayerModel = new DefaultTableModel(rowData, columnNames);
        JTable layerOrderTemp = new JTable(legendLayerModel);

        // now that the table is built, we need to add some event listeners in order to make the table interactive.

        // onclick listener will reset the index of the activeLegend whenever a row is clicked
        layerOrderTemp.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                // get the row and column of the clicked cell
                int row = layerOrder.rowAtPoint(e.getPoint()); // returns -1 if clicked outside the table. Hence this condition has to be checked in the code further.
                int col = layerOrder.columnAtPoint(e.getPoint());

                if (row >= 0) {
                    LegendModel legendManager = legendController.getLegendModel();
                    legendManager.setPickedLegend(legendManager.getNumberOfActiveItems() - 1 - row);
                }
            }
        });

        // if the text in a cell has changed, it means that the user has given a different name to the layer. hence the 'user_legend_name' property must be changed to its value.
        layerOrderTemp.addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent e) {
                if ("tableCellEditor".equals(e.getPropertyName())) {
                    if (!layerOrder.isEditing()) {
                        // if the event is fired and the isEditing flag is unset, it means that the editing of the cell is complete.
                        LegendModel legendManager = legendController.getLegendModel();

                        // the editing can be done only on the activelegend, In order to update the layer data, we need to find out the row and the column
                        int row = layerOrder.convertRowIndexToModel(layerOrder.getEditingRow());
                        int col = layerOrder.convertColumnIndexToModel(layerOrder.getEditingColumn());

                        // fetch the active item and get the reference for its user defined name attribute.
                        Item activeItem = legendManager.getPickedLegendItem();
                        PreviewProperty[] props = (PreviewProperty[]) activeItem.getData(LegendItem.PROPERTIES);
                        PreviewProperty name = props[LegendProperty.USER_LEGEND_NAME];

                        // set the user defined name attribute to the updated cell value
                        name.setValue(layerOrder.getModel().getValueAt(row, col));
                    }
                }
            }
        });

        return layerOrderTemp;
    }

    private void refreshLayers() {
        // this method will update the legend layers and fill the legend layers panel with the user defined names of all the legends.
        PreviewController previewController = Lookup.getDefault().lookup(PreviewController.class);
        PreviewModel previewModel = previewController.getModel();

        // check if previewModel exists
        if (previewModel == null) {
            return;
        }

        LegendModel legendModel = legendController.getLegendModel();

        // remove the existing JTable
        legendLayers.removeAll();

        // get the JTable with all the filled values and the listeners.
        layerOrder = getLegendLayerModel();

        // add the JTable, repaint and update the UI
        legendLayers.add(layerOrder, BorderLayout.CENTER);
        legendLayers.repaint();
        legendLayers.updateUI();

        // after updating the UI, we need to indicate to the user about the legend that is currently active. hence, we select the row that contains the active legend.
        if (legendModel.getPickedLegend() != -1) {
            int pickedLegendPosition = legendModel.getNumberOfActiveItems() - 1 - legendModel.getPickedLegend();
            layerOrder.setRowSelectionInterval(pickedLegendPosition, pickedLegendPosition);
        }
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addLegendButton;
    private javax.swing.JPanel controls;
    private javax.swing.JPanel legendLayers;
    private javax.swing.JPanel legendManagerPanel;
    private javax.swing.JButton moveDown;
    private javax.swing.JPanel moveLayers;
    private javax.swing.JButton moveUp;
    private javax.swing.JButton removeLegendButton;
    // End of variables declaration//GEN-END:variables

    @Override
    public void setup(PreviewModel previewModel) {
    }

    @Override
    public JPanel getPanel() {
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

    class CustomLegendItemBuilderWrapper {

        public CustomLegendItemBuilder builder;

        public CustomLegendItemBuilderWrapper(CustomLegendItemBuilder builder) {
            this.builder = builder;
        }

        @Override
        public String toString() {
            return builder.getTitle();
        }
    }
}
