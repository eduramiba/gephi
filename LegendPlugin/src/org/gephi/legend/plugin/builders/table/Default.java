/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.gephi.legend.plugin.builders.table;

import java.awt.Color;
import java.util.ArrayList;
import org.gephi.legend.plugin.custombuilders.CustomTableItemBuilder;
import org.gephi.legend.plugin.items.TableItem;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author edubecks
 */
@ServiceProvider(service = CustomTableItemBuilder.class, position = 1)
public class Default implements CustomTableItemBuilder {

    @Override
    public String getDescription() {
        return DEFAULT_DESCRIPTION;
    }

    @Override
    public String getTitle() {
        return DEFAULT_TITLE;
    }

    @Override
    public void retrieveData(ArrayList<TableItem.LabelSelection> labels,
                             ArrayList<String> rowLabels,
                             ArrayList<String> columnLabels,
                             ArrayList<ArrayList<String>> values,
                             ArrayList<Color> rowLabelColors,
                             ArrayList<Color> columndLabelColors,
                             ArrayList<ArrayList<Color>> valueColors) {
        // LABELS ID
        labels.add(TableItem.LabelSelection.VERTICAL);


        // VERTICAL LABELS
//        StringBuilder label1 = new StringBuilder("Label 1");
//        StringBuilder label2 = new StringBuilder("Label 2");
//        StringBuilder label3 = new StringBuilder("Label 3");
        String label1 = "Label 1";
        String label2 = "Label 2";
        String label3 = "Label 3";
        columnLabels.add(label1);
        columnLabels.add(label2);
        columnLabels.add(label3);

        // VERTICAL COLORS
        Color color1 = Color.BLACK;
        columndLabelColors.add(color1);
        columndLabelColors.add(color1);
        columndLabelColors.add(color1);

        // HORIZONTAL LABELS
//        StringBuilder value1 = new StringBuilder("Value Uno Dos");
//        StringBuilder value2 = new StringBuilder("Value.... ...............");
        String value1 = "Value Uno Dos";
        String value2 = "Value ................";
        rowLabels.add(value1);
        rowLabels.add(value2);

        // HORIZONTAL COLORS
        Color color2 = Color.RED;
        rowLabelColors.add(color2);
        rowLabelColors.add(color2);

        // VALUES
        ArrayList<String> row = new ArrayList<String>();
        row.add(1 + "");
        row.add(0.5 + "");
        row.add(0.85 + "");
        values.add(row);
        values.add(row);

        // VALUES COLOR
        Color color3 = Color.GREEN;
        ArrayList<Color> colorsRow = new ArrayList<Color>();
        colorsRow.add(color3);
        colorsRow.add(color3);
        colorsRow.add(color3);
        valueColors.add(colorsRow);
        valueColors.add(colorsRow);

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
