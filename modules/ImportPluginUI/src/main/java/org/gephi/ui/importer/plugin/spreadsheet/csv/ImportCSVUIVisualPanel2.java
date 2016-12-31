/*
 Copyright 2008-2010 Gephi
 Authors : Eduardo Ramos <eduramiba@gmail.com>
 Website : http://www.gephi.org

 This file is part of Gephi.

 DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.

 Copyright 2011 Gephi Consortium. All rights reserved.

 The contents of this file are subject to the terms of either the GNU
 General Public License Version 3 only ("GPL") or the Common
 Development and Distribution License("CDDL") (collectively, the
 "License"). You may not use this file except in compliance with the
 License. You can obtain a copy of the License at
 http://gephi.org/about/legal/license-notice/
 or /cddl-1.0.txt and /gpl-3.0.txt. See the License for the
 specific language governing permissions and limitations under the
 License.  When distributing the software, include this License Header
 Notice in each file and include the License files at
 /cddl-1.0.txt and /gpl-3.0.txt. If applicable, add the following below the
 License Header, with the fields enclosed by brackets [] replaced by
 your own identifying information:
 "Portions Copyrighted [year] [name of copyright owner]"

 If you wish your version of this file to be governed by only the CDDL
 or only the GPL Version 3, indicate your decision by adding
 "[Contributor] elects to include this software in this distribution
 under the [CDDL or GPL Version 3] license." If you do not indicate a
 single choice of license, a recipient has the option to distribute
 your version of this file under either the CDDL, the GPL Version 3 or
 to extend the choice of license to its licensees as provided above.
 However, if you add GPL Version 3 code and therefore, elected the GPL
 Version 3 license, then the option applies only if the new code is
 made subject to such option by the copyright holder.

 Contributor(s):

 Portions Copyrighted 2011 Gephi Consortium.
 */
package org.gephi.ui.importer.plugin.spreadsheet.csv;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import net.miginfocom.swing.MigLayout;
import org.gephi.io.importer.plugin.file.spreadsheet.AbstractImporterSpreadsheet;
import org.gephi.ui.utils.SupportedColumnTypeWrapper;
import org.openide.util.Exceptions;
import org.openide.util.NbBundle;
import org.openide.util.NbPreferences;

public final class ImportCSVUIVisualPanel2 extends JPanel {

    private static final String ASSIGN_NEW_NODES_IDS_SAVED_PREFERENCES = "ImportCSVUIVisualPanel2_assign_new_nodes_ids";
    private static final String CREATE_NEW_NODES_SAVED_PREFERENCES = "ImportCSVUIVisualPanel2_create_new_nodes";
    private final ImportCSVUIWizardPanel2 wizard2;
    private final ArrayList<JCheckBox> columnsCheckBoxes = new ArrayList<>();
    private final ArrayList<JComboBox> columnsComboBoxes = new ArrayList<>();
    //Nodes table settings:
    private JCheckBox assignNewNodeIds;
    //Edges table settings:
    private JCheckBox createNewNodes;

    private final AbstractImporterSpreadsheet importer;

    /**
     * Creates new form ImportCSVUIVisualPanel2
     */
    public ImportCSVUIVisualPanel2(AbstractImporterSpreadsheet importer, ImportCSVUIWizardPanel2 wizard2) {
        initComponents();
        this.importer = importer;
        this.wizard2 = wizard2;
        
        //TODO: add time representation chooser
    }

    public void unSetup() {
        if (assignNewNodeIds != null) {
            NbPreferences.forModule(ImportCSVUIVisualPanel1.class).putBoolean(ASSIGN_NEW_NODES_IDS_SAVED_PREFERENCES, assignNewNodeIds.isSelected());
        }
        if (createNewNodes != null) {
            NbPreferences.forModule(ImportCSVUIVisualPanel1.class).putBoolean(CREATE_NEW_NODES_SAVED_PREFERENCES, createNewNodes.isSelected());
        }
    }

    public void reloadSettings() {
        JPanel settingsPanel = new JPanel();
        settingsPanel.setLayout(new MigLayout());
        loadDescription(settingsPanel);
        switch (importer.getTable()) {
            case NODES:
                loadColumns(settingsPanel);
                loadNodesTableSettings(settingsPanel);
                break;
            case EDGES:
                loadColumns(settingsPanel);
                loadEdgesTableSettings(settingsPanel);
                break;
        }

        scroll.setViewportView(settingsPanel);
        wizard2.fireChangeEvent();//Enable/disable finish button
    }

    private void loadDescription(JPanel settingsPanel) {
        JLabel descriptionLabel = new JLabel();
        switch (importer.getTable()) {
            case NODES:
                descriptionLabel.setText(getMessage("ImportCSVUIVisualPanel2.nodes.description"));
                break;
            case EDGES:
                descriptionLabel.setText(getMessage("ImportCSVUIVisualPanel2.edges.description"));
                break;
        }
        settingsPanel.add(descriptionLabel, "wrap 15px");
    }

    private void loadColumns(JPanel settingsPanel) {
        try {
            columnsCheckBoxes.clear();
            columnsComboBoxes.clear();
            JLabel columnsLabel = new JLabel(getMessage("ImportCSVUIVisualPanel2.columnsLabel.text"));
            settingsPanel.add(columnsLabel, "wrap");

            final String[] headers = importer.getHeadersMap().keySet().toArray(new String[0]);

            boolean isEdgesTable = importer.getTable() == AbstractImporterSpreadsheet.Table.EDGES;
            
            for (String header : headers) {
                if (header.isEmpty()) {
                    continue;//Remove empty column headers:
                }
                
                //TODO: auto fill with guessed types and existing graph types and mandatory special types (source, target, type, kind, weight...)
                
                JCheckBox columnCheckBox = new JCheckBox(header, true);
                columnsCheckBoxes.add(columnCheckBox);
                settingsPanel.add(columnCheckBox, "wrap");
                JComboBox columnComboBox = new JComboBox();
                columnsComboBoxes.add(columnComboBox);
                fillComboBoxWithColumnTypes(header, columnComboBox);
                settingsPanel.add(columnComboBox, "wrap 15px");
            }
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }
    }

    private void fillComboBoxWithColumnTypes(String column, JComboBox comboBox) {
        comboBox.removeAllItems();
        List<SupportedColumnTypeWrapper> supportedTypesWrappers = SupportedColumnTypeWrapper.buildOrderedSupportedTypesList(importer.getTimeRepresentation());

        for (SupportedColumnTypeWrapper supportedColumnTypeWrapper : supportedTypesWrappers) {
            comboBox.addItem(supportedColumnTypeWrapper);
        }

        comboBox.setSelectedItem(new SupportedColumnTypeWrapper(String.class));//Set STRING by default
    }

    private void loadNodesTableSettings(JPanel settingsPanel) {
        //Create assignNewNodeIds checkbox and set its selection with saved preferences or true by default:
        assignNewNodeIds = new JCheckBox(getMessage("ImportCSVUIVisualPanel2.nodes.assign-ids-checkbox"),
                NbPreferences.forModule(ImportCSVUIVisualPanel1.class)
                        .getBoolean(ASSIGN_NEW_NODES_IDS_SAVED_PREFERENCES, false));//False => by default update nodes instead of creating new ones
        settingsPanel.add(assignNewNodeIds, "wrap");
    }

    private void loadEdgesTableSettings(JPanel settingsPanel) {
        //Create createNewNodes checkbox and set its selection with saved preferences or true by default:
        createNewNodes = new JCheckBox(getMessage("ImportCSVUIVisualPanel2.edges.create-new-nodes-checkbox"),
                NbPreferences.forModule(ImportCSVUIVisualPanel1.class)
                        .getBoolean(CREATE_NEW_NODES_SAVED_PREFERENCES, true));//True => by default create missing nodes
        settingsPanel.add(createNewNodes, "wrap");
    }

    public boolean isValidCSV() {
        return true;
    }

    public String[] getColumnsToImport() {
        ArrayList<String> columns = new ArrayList<>();
        for (JCheckBox columnCheckBox : columnsCheckBoxes) {
            if (columnCheckBox.isSelected()) {
                columns.add(columnCheckBox.getText());
            }
        }
        return columns.toArray(new String[0]);
    }

    public Class[] getColumnsToImportTypes() {
        ArrayList<Class> types = new ArrayList<>();
        for (int i = 0; i < columnsCheckBoxes.size(); i++) {
            if (columnsCheckBoxes.get(i).isSelected()) {
                SupportedColumnTypeWrapper selected = (SupportedColumnTypeWrapper) columnsComboBoxes.get(i).getSelectedItem();
                types.add(selected.getType());
            }
        }
        return types.toArray(new Class[0]);
    }

    public boolean getAssignNewNodeIds() {
        return assignNewNodeIds != null ? assignNewNodeIds.isSelected() : false;
    }

    public boolean getCreateNewNodes() {
        return createNewNodes != null ? createNewNodes.isSelected() : false;
    }

    @Override
    public String getName() {
        return NbBundle.getMessage(ImportCSVUIVisualPanel2.class, "ImportCSVUIVisualPanel2.name");
    }

    private String getMessage(String resName) {
        return NbBundle.getMessage(ImportCSVUIVisualPanel2.class, resName);
    }

    /**
     * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The content of this method is always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        scroll = new javax.swing.JScrollPane();

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(scroll, javax.swing.GroupLayout.DEFAULT_SIZE, 290, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(scroll, javax.swing.GroupLayout.DEFAULT_SIZE, 320, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane scroll;
    // End of variables declaration//GEN-END:variables
}
