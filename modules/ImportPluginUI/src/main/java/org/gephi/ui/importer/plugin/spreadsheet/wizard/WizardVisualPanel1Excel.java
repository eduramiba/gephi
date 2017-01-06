/*
Copyright 2008-2016 Gephi
Authors : Eduardo Ramos <eduardo.ramos@gephi.org>
Website : http://www.gephi.org

This file is part of Gephi.

DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.

Copyright 2016 Gephi Consortium. All rights reserved.

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

Portions Copyrighted 2016 Gephi Consortium.
 */
package org.gephi.ui.importer.plugin.spreadsheet.wizard;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;
import org.gephi.io.importer.plugin.file.spreadsheet.ImporterSpreadsheetExcel;
import org.gephi.io.importer.plugin.file.spreadsheet.process.SpreadsheetGeneralConfiguration.Table;
import org.gephi.io.importer.plugin.file.spreadsheet.sheet.SheetParser;
import org.gephi.io.importer.plugin.file.spreadsheet.sheet.SheetRow;
import org.netbeans.validation.api.Problems;
import org.netbeans.validation.api.Severity;
import org.netbeans.validation.api.Validator;
import org.netbeans.validation.api.ui.ValidationGroup;
import org.netbeans.validation.api.ui.ValidationPanel;
import org.openide.util.NbBundle;

/**
 *
 * @author Eduardo Ramos
 */
public class WizardVisualPanel1Excel extends javax.swing.JPanel {

    private static final int MAX_ROWS_PREVIEW = 25;

    private final ImporterSpreadsheetExcel importer;

    private final WizardPanel1Excel wizard1;
    private int columnCount = 0;
    private boolean hasSourceNodeColumn = false;
    private boolean hasTargetNodeColumn = false;
    private boolean hasRowsMissingSourcesOrTargets = false;
    private ValidationPanel validationPanel;

    private boolean initialized = false;

    /**
     * Creates new form WizardVisualPanel1CSV
     */
    public WizardVisualPanel1Excel(ImporterSpreadsheetExcel importer, WizardPanel1Excel wizard1) {
        initComponents();
        this.wizard1 = wizard1;
        this.importer = importer;

        tableComboBox.addItem(getMessage("WizardVisualPanel1.nodes-table"));
        tableComboBox.addItem(getMessage("WizardVisualPanel1.edges-table"));

        //Setup with initial values:
        //Sheet:
        for (String sheetName : importer.getAvailableSheetNames()) {
            sheetComboBox.addItem(sheetName);
        }

        //Table:
        tableComboBox.setSelectedIndex(importer.getTable() == Table.NODES ? 0 : 1);

        //File path:
        final String filePath = importer.getFile().getAbsolutePath();
        pathTextField.setText(filePath);
        pathTextField.setToolTipText(filePath);

        initialized = true;
    }

    public ValidationPanel getValidationPanel() {
        if (validationPanel != null) {
            return validationPanel;
        }
        validationPanel = new ValidationPanel();
        validationPanel.setInnerComponent(WizardVisualPanel1Excel.this);
        ValidationGroup validationGroup = validationPanel.getValidationGroup();
        validationGroup.add(pathTextField, new Validator<String>() {

            @Override
            public boolean validate(Problems prblms, String string, String t) {
                if (!hasColumns()) {
                    prblms.add(getMessage("WizardVisualPanel1CSV.validation.no-columns"));
                    return false;
                }
                if (!areValidColumnsForTable()) {
                    prblms.add(getMessage("WizardVisualPanel1CSV.validation.edges.no-source-target-columns"));
                    return false;
                }
                if (hasRowsMissingSourcesOrTargets()) {
                    prblms.add(NbBundle.getMessage(WizardVisualPanel1Excel.class,
                            "WizardVisualPanel1CSV.validation.edges.empty-sources-or-targets"
                    ), Severity.WARNING);
                }
                return true;
            }
        });
        validationPanel.setName(getName());

        return validationPanel;
    }

    public void refreshPreviewTable() {
        try (SheetParser parser = importer.createParser()) {
            Map<String, Integer> headerMap = parser.getHeaderMap();
            String[] headers = headerMap.keySet().toArray(new String[0]);

            columnCount = headers.length;

            hasSourceNodeColumn = false;
            hasTargetNodeColumn = false;
            int sourceColumnIndex = 0;
            int targetColumnIndex = 0;

            for (String header : headers) {
                if (header.equalsIgnoreCase("source")) {
                    hasSourceNodeColumn = true;
                    sourceColumnIndex = headerMap.get(header);
                }
                if (header.equalsIgnoreCase("target")) {
                    hasTargetNodeColumn = true;
                    targetColumnIndex = headerMap.get(header);
                }
            }

            ArrayList<String[]> records = new ArrayList<>();
            hasRowsMissingSourcesOrTargets = false;
            Table table = getSelectedTable();
            if (columnCount > 0) {
                String[] currentRecord;

                Iterator<SheetRow> iterator = parser.iterator();

                int count = 0;
                while (iterator.hasNext() && count < MAX_ROWS_PREVIEW) {
                    count++;

                    SheetRow row = iterator.next();

                    int recordColumnCount = row.size();
                    currentRecord = new String[recordColumnCount];
                    for (int i = 0; i < currentRecord.length; i++) {
                        currentRecord[i] = row.get(i);
                    }

                    // Search for missing source or target columns for edges table
                    if (table == Table.EDGES) {
                        if (recordColumnCount < sourceColumnIndex
                                || currentRecord[sourceColumnIndex] == null
                                || recordColumnCount < targetColumnIndex
                                || currentRecord[targetColumnIndex] == null) {
                            hasRowsMissingSourcesOrTargets = true;
                        }
                    }

                    if (records.size() < MAX_ROWS_PREVIEW) {
                        records.add(currentRecord);
                    }
                }
            }

            final String[] columnNames = headers;
            final String[][] values = records.toArray(new String[0][]);
            previewTable.setModel(new TableModel() {

                @Override
                public int getRowCount() {
                    return values.length;
                }

                @Override
                public int getColumnCount() {
                    return columnNames.length;
                }

                @Override
                public String getColumnName(int columnIndex) {
                    return columnNames[columnIndex];
                }

                @Override
                public Class<?> getColumnClass(int columnIndex) {
                    return String.class;
                }

                @Override
                public boolean isCellEditable(int rowIndex, int columnIndex) {
                    return false;
                }

                @Override
                public Object getValueAt(int rowIndex, int columnIndex) {
                    if (values[rowIndex].length > columnIndex) {
                        return values[rowIndex][columnIndex];
                    } else {
                        return null;
                    }
                }

                @Override
                public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
                }

                @Override
                public void addTableModelListener(TableModelListener l) {
                }

                @Override
                public void removeTableModelListener(TableModelListener l) {
                }
            });
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
        wizard1.fireChangeEvent();
        pathTextField.setText(pathTextField.getText());//To fire validation panel messages.
    }

    @Override
    public String getName() {
        return getMessage("WizardVisualPanel1Excel.name");
    }

    public Table getSelectedTable() {
        switch (tableComboBox.getSelectedIndex()) {
            case 1:
                return Table.EDGES;
            default:
                return Table.NODES;
        }
    }

    public int getSelectedSheetIndex() {
        return sheetComboBox.getSelectedIndex();
    }

    public int getColumnCount() {
        return columnCount;
    }

    public boolean hasColumns() {
        return columnCount > 0;
    }

    public boolean areValidColumnsForTable() {
        switch (getSelectedTable()) {
            case EDGES:
                return hasSourceNodeColumn && hasTargetNodeColumn;
            default:
                return true;
        }
    }

    public boolean isCSVValid() {
        return hasColumns() && areValidColumnsForTable();
    }

    public boolean hasRowsMissingSourcesOrTargets() {
        return hasRowsMissingSourcesOrTargets;
    }

    private String getMessage(String resName) {
        return NbBundle.getMessage(WizardVisualPanel1Excel.class, resName);
    }

    /**
     * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The content of this method is always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        filePathLabel = new javax.swing.JLabel();
        pathTextField = new javax.swing.JTextField();
        separatorLabel = new javax.swing.JLabel();
        sheetComboBox = new javax.swing.JComboBox();
        tableLabel = new javax.swing.JLabel();
        tableComboBox = new javax.swing.JComboBox();
        previewLabel = new javax.swing.JLabel();
        scroll = new javax.swing.JScrollPane();
        previewTable = new javax.swing.JTable();

        filePathLabel.setText(org.openide.util.NbBundle.getMessage(WizardVisualPanel1Excel.class, "WizardVisualPanel1Excel.filePathLabel.text")); // NOI18N

        pathTextField.setEditable(false);
        pathTextField.setText(org.openide.util.NbBundle.getMessage(WizardVisualPanel1Excel.class, "WizardVisualPanel1Excel.pathTextField.text")); // NOI18N

        separatorLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        separatorLabel.setText(org.openide.util.NbBundle.getMessage(WizardVisualPanel1Excel.class, "WizardVisualPanel1Excel.separatorLabel.text")); // NOI18N

        sheetComboBox.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                sheetComboBoxItemStateChanged(evt);
            }
        });

        tableLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        tableLabel.setText(org.openide.util.NbBundle.getMessage(WizardVisualPanel1Excel.class, "WizardVisualPanel1Excel.tableLabel.text")); // NOI18N

        tableComboBox.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                tableComboBoxItemStateChanged(evt);
            }
        });

        previewLabel.setText(org.openide.util.NbBundle.getMessage(WizardVisualPanel1Excel.class, "WizardVisualPanel1Excel.previewLabel.text")); // NOI18N

        scroll.setViewportView(previewTable);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(scroll, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 537, Short.MAX_VALUE)
                    .addComponent(filePathLabel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 537, Short.MAX_VALUE)
                    .addComponent(pathTextField, javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addComponent(previewLabel)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(separatorLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(sheetComboBox, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(tableLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(tableComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(filePathLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pathTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(separatorLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(sheetComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(tableLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(tableComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addComponent(previewLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(scroll, javax.swing.GroupLayout.DEFAULT_SIZE, 150, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void sheetComboBoxItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_sheetComboBoxItemStateChanged
        if (initialized) {
            importer.setSheetIndex(getSelectedSheetIndex());
        }
        refreshPreviewTable();
    }//GEN-LAST:event_sheetComboBoxItemStateChanged

    private void tableComboBoxItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_tableComboBoxItemStateChanged
        refreshPreviewTable();
    }//GEN-LAST:event_tableComboBoxItemStateChanged
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel filePathLabel;
    private javax.swing.JTextField pathTextField;
    private javax.swing.JLabel previewLabel;
    private javax.swing.JTable previewTable;
    private javax.swing.JScrollPane scroll;
    private javax.swing.JLabel separatorLabel;
    private javax.swing.JComboBox sheetComboBox;
    private javax.swing.JComboBox tableComboBox;
    private javax.swing.JLabel tableLabel;
    // End of variables declaration//GEN-END:variables
}
