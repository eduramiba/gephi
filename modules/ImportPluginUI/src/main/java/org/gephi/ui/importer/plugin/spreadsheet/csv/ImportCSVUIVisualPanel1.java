/*
Copyright 2008-2010 Gephi
Authors : Mathieu Bastian, Mathieu Jacomy, Julian Bilcke, Eduardo Ramos
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
import java.util.Iterator;
import java.util.Map;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;
import org.gephi.io.importer.plugin.file.spreadsheet.AbstractImporterSpreadsheet;
import org.gephi.io.importer.plugin.file.spreadsheet.ImporterSpreadsheetCSV;
import org.gephi.io.importer.plugin.file.spreadsheet.sheet.SheetParser;
import org.gephi.io.importer.plugin.file.spreadsheet.sheet.SheetRow;
import org.netbeans.validation.api.Problems;
import org.netbeans.validation.api.Severity;
import org.netbeans.validation.api.Validator;
import org.netbeans.validation.api.ui.ValidationGroup;
import org.netbeans.validation.api.ui.ValidationPanel;
import org.openide.util.NbBundle;
import org.openide.util.NbPreferences;

/**
 *
 * @author Eduardo Ramos
 */
public class ImportCSVUIVisualPanel1 extends javax.swing.JPanel {

    private static final String SEPARATOR_SAVED_PREFERENCES = "ImportCSVUIVisualPanel1_Separator";
    private static final String TABLE_SAVED_PREFERENCES = "ImportCSVUIVisualPanel1_Table";

    private static final int MAX_ROWS_PREVIEW = 25;

    private final ImporterSpreadsheetCSV importer;

    private ImportCSVUIWizardPanel1 wizard1;
    private int columnCount = 0;
    private boolean hasSourceNodeColumn = false;
    private boolean hasTargetNodeColumn = false;
    private boolean hasRowsMissingSourcesOrTargets = false;
    private ValidationPanel validationPanel;

    /**
     * Creates new form ImportCSVUIVisualPanel1
     */
    public ImportCSVUIVisualPanel1(ImporterSpreadsheetCSV importer, ImportCSVUIWizardPanel1 wizard1) {
        initComponents();
        this.wizard1 = wizard1;
        this.importer = importer;
        separatorComboBox.addItem(new SeparatorWrapper((','), getMessage("ImportCSVUIVisualPanel1.comma")));
        separatorComboBox.addItem(new SeparatorWrapper((';'), getMessage("ImportCSVUIVisualPanel1.semicolon")));
        separatorComboBox.addItem(new SeparatorWrapper(('\t'), getMessage("ImportCSVUIVisualPanel1.tab")));
        separatorComboBox.addItem(new SeparatorWrapper((' '), getMessage("ImportCSVUIVisualPanel1.space")));

        separatorComboBox.setSelectedIndex(NbPreferences.forModule(ImportCSVUIVisualPanel1.class).getInt(SEPARATOR_SAVED_PREFERENCES, 0));//Use saved separator or comma if not saved yet

        tableComboBox.addItem(getMessage("ImportCSVUIVisualPanel1.nodes-table"));
        tableComboBox.addItem(getMessage("ImportCSVUIVisualPanel1.edges-table"));

        tableComboBox.setSelectedIndex(NbPreferences.forModule(ImportCSVUIVisualPanel1.class).getInt(TABLE_SAVED_PREFERENCES, 0));//Use saved table or nodes table if not saved yet

        final String filePath = importer.getFile().getAbsolutePath();
        pathTextField.setText(filePath);
        pathTextField.setToolTipText(filePath);
        //TODO: add time representation chooser
    }

    public void unSetup() {
        NbPreferences.forModule(ImportCSVUIVisualPanel1.class).putInt(SEPARATOR_SAVED_PREFERENCES, separatorComboBox.getSelectedIndex());
        NbPreferences.forModule(ImportCSVUIVisualPanel1.class).putInt(TABLE_SAVED_PREFERENCES, tableComboBox.getSelectedIndex());
    }

    public ValidationPanel getValidationPanel() {
        if (validationPanel != null) {
            return validationPanel;
        }
        validationPanel = new ValidationPanel();
        validationPanel.setInnerComponent(ImportCSVUIVisualPanel1.this);
        ValidationGroup validationGroup = validationPanel.getValidationGroup();
        validationGroup.add(pathTextField, new Validator<String>() {

            @Override
            public boolean validate(Problems prblms, String string, String t) {
                if (!hasColumns()) {
                    prblms.add(getMessage("ImportCSVUIVisualPanel1.validation.no-columns"));
                    return false;
                }
                if (!areValidColumnsForTable()) {
                    prblms.add(getMessage("ImportCSVUIVisualPanel1.validation.edges.no-source-target-columns"));
                    return false;
                }
                if (hasRowsMissingSourcesOrTargets()) {
                    prblms.add(NbBundle.getMessage(ImportCSVUIVisualPanel1.class,
                            "ImportCSVUIVisualPanel1.validation.edges.empty-sources-or-targets"
                    ), Severity.WARNING);
                }
                return true;
            }
        });
        validationPanel.setName(getName());

        return validationPanel;
    }

    public void refreshPreviewTable() {
        SheetParser parser = null;
        try {
            parser = importer.createParser();
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
            AbstractImporterSpreadsheet.Table table = getSelectedTable();
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
                    if (table == AbstractImporterSpreadsheet.Table.EDGES) {
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
        } finally {
            if (parser != null) {
                try {
                    parser.close();
                } catch (IOException ex) {
                    //NOOP
                }
            }
        }
        wizard1.fireChangeEvent();
        pathTextField.setText(pathTextField.getText());//To fire validation panel messages.
    }

    @Override
    public String getName() {
        return getMessage("ImportCSVUIVisualPanel1.name");
    }

    public Character getSelectedSeparator() {
        Object item = separatorComboBox.getSelectedItem();
        if (item instanceof SeparatorWrapper) {
            return ((SeparatorWrapper) item).separator;
        } else {
            return item.toString().charAt(0);
        }
    }

    public AbstractImporterSpreadsheet.Table getSelectedTable() {
        switch (tableComboBox.getSelectedIndex()) {
            case 1:
                return AbstractImporterSpreadsheet.Table.EDGES;
            default:
                return AbstractImporterSpreadsheet.Table.NODES;
        }
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

    class SeparatorWrapper {

        private Character separator;
        private String displayText;

        public SeparatorWrapper(Character separator) {
            this.separator = separator;
        }

        public SeparatorWrapper(Character separator, String displayText) {
            this.separator = separator;
            this.displayText = displayText;
        }

        @Override
        public String toString() {
            if (displayText != null) {
                return displayText;
            } else {
                return String.valueOf(separator);
            }
        }
    }

    private String getMessage(String resName) {
        return NbBundle.getMessage(ImportCSVUIVisualPanel1.class, resName);
    }

    /**
     * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The content of this method is always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        descriptionLabel = new javax.swing.JLabel();
        pathTextField = new javax.swing.JTextField();
        separatorLabel = new javax.swing.JLabel();
        separatorComboBox = new javax.swing.JComboBox();
        tableLabel = new javax.swing.JLabel();
        tableComboBox = new javax.swing.JComboBox();
        previewLabel = new javax.swing.JLabel();
        scroll = new javax.swing.JScrollPane();
        previewTable = new javax.swing.JTable();

        descriptionLabel.setText(org.openide.util.NbBundle.getMessage(ImportCSVUIVisualPanel1.class, "ImportCSVUIVisualPanel1.descriptionLabel.text")); // NOI18N

        pathTextField.setEditable(false);
        pathTextField.setText(org.openide.util.NbBundle.getMessage(ImportCSVUIVisualPanel1.class, "ImportCSVUIVisualPanel1.pathTextField.text")); // NOI18N

        separatorLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        separatorLabel.setText(org.openide.util.NbBundle.getMessage(ImportCSVUIVisualPanel1.class, "ImportCSVUIVisualPanel1.separatorLabel.text")); // NOI18N

        separatorComboBox.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                separatorComboBoxItemStateChanged(evt);
            }
        });

        tableLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        tableLabel.setText(org.openide.util.NbBundle.getMessage(ImportCSVUIVisualPanel1.class, "ImportCSVUIVisualPanel1.tableLabel.text")); // NOI18N

        tableComboBox.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                tableComboBoxItemStateChanged(evt);
            }
        });

        previewLabel.setText(org.openide.util.NbBundle.getMessage(ImportCSVUIVisualPanel1.class, "ImportCSVUIVisualPanel1.previewLabel.text")); // NOI18N

        scroll.setViewportView(previewTable);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(scroll, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(descriptionLabel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(pathTextField, javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addComponent(previewLabel)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(separatorLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(separatorComboBox, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(tableLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(tableComboBox, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(descriptionLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pathTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(separatorLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(separatorComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
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

    private void separatorComboBoxItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_separatorComboBoxItemStateChanged
        importer.setFieldDelimiter(getSelectedSeparator());
        refreshPreviewTable();
    }//GEN-LAST:event_separatorComboBoxItemStateChanged

    private void tableComboBoxItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_tableComboBoxItemStateChanged
        importer.setTable(getSelectedTable());
        refreshPreviewTable();
    }//GEN-LAST:event_tableComboBoxItemStateChanged
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel descriptionLabel;
    private javax.swing.JTextField pathTextField;
    private javax.swing.JLabel previewLabel;
    private javax.swing.JTable previewTable;
    private javax.swing.JScrollPane scroll;
    private javax.swing.JComboBox separatorComboBox;
    private javax.swing.JLabel separatorLabel;
    private javax.swing.JComboBox tableComboBox;
    private javax.swing.JLabel tableLabel;
    // End of variables declaration//GEN-END:variables
}
