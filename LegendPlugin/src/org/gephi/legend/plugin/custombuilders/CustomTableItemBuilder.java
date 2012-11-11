/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.gephi.legend.plugin.custombuilders;

import java.awt.Color;
import java.util.ArrayList;
import org.gephi.legend.plugin.items.TableItem;
import org.gephi.legend.spi.CustomLegendItemBuilder;

/**
 *
 * @author edubecks
 */
public interface CustomTableItemBuilder extends CustomLegendItemBuilder {

    /**
     * This function receives the objects that need to be filled in order to
     * build the the table.
     * <code>rowLabels</code> and
     * <code>rowLabelColors</code> should have the same size.
     * <code>columnLabels</code> and
     * <code>columnLabelColors</code> should have the same size too. Finally,
     * <code>values</code> represents the table and its number of rows should be
     * equal to the number of elements in
     * <code>rowLabels</code> as well as the number of columns should be
     * equal to the number of elements in
     * <code>columnLabels</code>
     *
     * @param labels an arraylist of one single value containing the selection
     * of labels: <code>TableItem.LabelSelection.VERTICAL</code> or
     * <code>TableItem.LabelSelection.HORIZONTAL</code>
     * <code>TableItem.LabelSelection.BOTH</code>
     * @param rowLabels list of labels that will be displayed at the
     * sides
     * @param columnLabels list of labels that will be displayed at top or
     * bottom
     * @param values table values representing the matrix
     * @param rowLabelColors colors used to represent the row labels
     * @param columnLabelColors colors used to represent the column labels
     * @param valueColors table colors representing the matrix
     */
    public void retrieveData(ArrayList<TableItem.LabelSelection> labels,
                             ArrayList<String> rowLabels,
                             ArrayList<String> columnLabels,
                             ArrayList<ArrayList<String>> values,
                             ArrayList<Color> rowLabelColors,
                             ArrayList<Color> columnLabelColors,
                             ArrayList<ArrayList<Color>> valueColors);

}
