/*
 Copyright 2008-2010 Gephi
 Authors : Mathieu Bastian <mathieu.bastian@gephi.org>
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
package org.gephi.io.processor.plugin;

import java.awt.Color;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.gephi.graph.api.AttributeUtils;
import org.gephi.graph.api.Column;
import org.gephi.graph.api.Edge;
import org.gephi.graph.api.Element;
import org.gephi.graph.api.GraphModel;
import org.gephi.graph.api.Interval;
import org.gephi.graph.api.Node;
import org.gephi.graph.api.Origin;
import org.gephi.graph.api.Table;
import org.gephi.graph.api.TimeRepresentation;
import org.gephi.graph.api.types.IntervalSet;
import org.gephi.graph.api.types.TimeMap;
import org.gephi.graph.api.types.TimeSet;
import org.gephi.graph.api.types.TimestampSet;
import org.gephi.io.importer.api.ColumnDraft;
import org.gephi.io.importer.api.ContainerUnloader;
import org.gephi.io.importer.api.EdgeDraft;
import org.gephi.io.importer.api.ElementDraft;
import org.gephi.io.importer.api.NodeDraft;
import org.gephi.project.api.Workspace;
import org.gephi.utils.progress.ProgressTicket;

public abstract class AbstractProcessor {

    protected ProgressTicket progressTicket;
    protected Workspace workspace;
    protected ContainerUnloader[] containers;
    protected GraphModel graphModel;

    protected void flushColumns(ContainerUnloader container) {
        addColumnsToTable(container, graphModel.getNodeTable(), container.getNodeColumns());
        addColumnsToTable(container, graphModel.getEdgeTable(), container.getEdgeColumns());
    }

    private void addColumnsToTable(ContainerUnloader container, Table table, Iterable<ColumnDraft> columns) {
        TimeRepresentation timeRepresentation = container.getTimeRepresentation();
        for (ColumnDraft col : columns) {
            if (!table.hasColumn(col.getId())) {
                Class typeClass = col.getTypeClass();
                if (col.isDynamic() && TimeSet.class.isAssignableFrom(typeClass)) {
                    if (timeRepresentation.equals(TimeRepresentation.TIMESTAMP)) {
                        typeClass = AttributeUtils.getTimestampMapType(typeClass);
                    } else {
                        typeClass = AttributeUtils.getIntervalMapType(typeClass);
                    }
                }
                table.addColumn(col.getId(), col.getTitle(), typeClass, Origin.DATA, col.getDefaultValue(), !col.isDynamic());
            }
        }
    }

    protected void flushToNode(NodeDraft nodeDraft, Node node) {
        if (nodeDraft.getColor() != null) {
            node.setColor(nodeDraft.getColor());
        }

        if (nodeDraft.getLabel() != null) {
            if (node.getLabel() == null || !nodeDraft.isCreatedAuto()) {
                node.setLabel(nodeDraft.getLabel());
            }
        }

        if (node.getTextProperties() != null) {
            node.getTextProperties().setVisible(nodeDraft.isLabelVisible());
        }

        if (nodeDraft.getLabelColor() != null && node.getTextProperties() != null) {
            Color labelColor = nodeDraft.getLabelColor();
            node.getTextProperties().setColor(labelColor);
        } else {
            node.getTextProperties().setColor(new Color(0, 0, 0, 0));
        }

        if (nodeDraft.getLabelSize() != -1f && node.getTextProperties() != null) {
            node.getTextProperties().setSize(nodeDraft.getLabelSize());
        }

        if ((nodeDraft.getX() != 0 || nodeDraft.getY() != 0 || nodeDraft.getZ() != 0)
                && (node.x() == 0 && node.y() == 0 && node.z() == 0)) {
            node.setX(nodeDraft.getX());
            node.setY(nodeDraft.getY());
            node.setZ(nodeDraft.getZ());
        }

        if (nodeDraft.getSize() != 0 && !Float.isNaN(nodeDraft.getSize())) {
            node.setSize(nodeDraft.getSize());
        } else if (node.size() == 0) {
            node.setSize(10f);
        }

        //Timeset
        if (nodeDraft.getTimeSet() != null) {
            flushTimeSet(nodeDraft.getTimeSet(), node);
        }

        //Graph timeset
        if (nodeDraft.getGraphTimestamp() != null) {
            node.addTimestamp(nodeDraft.getGraphTimestamp());
        } else if (nodeDraft.getGraphInterval() != null) {
            node.addInterval(nodeDraft.getGraphInterval());
        }

        //Attributes
        flushToElementAttributes(nodeDraft, node);
    }

    protected void flushEdgeWeight(EdgeDraft edgeDraft, Edge edge) {
        Object val = edgeDraft.getValue("weight");
        if (val != null && val instanceof TimeMap) {
            TimeMap valMap = (TimeMap) val;

            TimeMap existingMap = (TimeMap) edge.getAttribute("weight");
            if (existingMap != null) {
                Object[] keys = ((TimeMap) val).toKeysArray();
                Object[] vals = ((TimeMap) val).toValuesArray();

                for (int i = 0; i < keys.length; i++) {
                    valMap.put(keys[i], ((Number) vals[i]).doubleValue());
                }
            }

            edge.setAttribute("weight", val);
        }
    }

    protected void flushToElementAttributes(ElementDraft elementDraft, Element element) {
        for (ColumnDraft columnDraft : elementDraft.getColumns()) {
            if (elementDraft instanceof EdgeDraft && columnDraft.getId().equals("weight")) {
                continue;//Special weight column
            }
            Object val = elementDraft.getValue(columnDraft.getId());

            Column column = element.getTable().getColumn(columnDraft.getId());
            if (!column.getTypeClass().isAssignableFrom(columnDraft.getTypeClass())) {
                Logger.getLogger("").log(
                        Level.SEVERE,
                        String.format(
                                "Existing column '%s' in graph with type '%s' is not compatible with imported column type '%s'",
                                column.getId(),
                                column.getTypeClass(),
                                columnDraft.getTypeClass()
                        )
                );
                //TODO: Add a Report after processor??
                //TODO: avoid repetition
                continue;//Incompatible types!
            }

            if (val != null) {
                Object processedNewValue = val;

                Object existingValue = element.getAttribute(columnDraft.getId());

                if (columnDraft.isDynamic() && existingValue != null) {
                    if (TimeMap.class.isAssignableFrom(columnDraft.getTypeClass())) {
                        TimeMap existingMap = (TimeMap) existingValue;
                        if (!existingMap.isEmpty()) {
                            TimeMap valMap = (TimeMap) val;

                            Object[] keys = existingMap.toKeysArray();
                            Object[] vals = existingMap.toValuesArray();
                            for (int i = 0; i < keys.length; i++) {
                                valMap.put(keys[i], vals[i]);
                            }

                            processedNewValue = valMap;
                        }
                    } else if (TimeSet.class.isAssignableFrom(columnDraft.getTypeClass())) {
                        TimeSet existingTimeSet = (TimeSet) existingValue;
                        
                        processedNewValue = mergeTimeSets(existingTimeSet, (TimeSet) val);
                    }
                }

                element.setAttribute(columnDraft.getId(), processedNewValue);
            }
        }
    }

    protected void flushToEdge(EdgeDraft edgeDraft, Edge edge) {
        if (edgeDraft.getColor() != null) {
            edge.setColor(edgeDraft.getColor());
        } else {
            edge.setR(0f);
            edge.setG(0f);
            edge.setB(0f);
            edge.setAlpha(0f);
        }

        if (edgeDraft.getLabel() != null) {
            edge.setLabel(edgeDraft.getLabel());
        }

        if (edge.getTextProperties() != null) {
            edge.getTextProperties().setVisible(edgeDraft.isLabelVisible());
        }

        if (edgeDraft.getLabelSize() != -1f && edge.getTextProperties() != null) {
            edge.getTextProperties().setSize(edgeDraft.getLabelSize());
        }

        if (edgeDraft.getLabelColor() != null && edge.getTextProperties() != null) {
            Color labelColor = edgeDraft.getLabelColor();
            edge.getTextProperties().setColor(labelColor);
        } else {
            edge.getTextProperties().setColor(new Color(0, 0, 0, 0));
        }

        //Timeset
        if (edgeDraft.getTimeSet() != null) {
            flushTimeSet(edgeDraft.getTimeSet(), edge);
        }

        //Graph timeset
        if (edgeDraft.getGraphTimestamp() != null) {
            edge.addTimestamp(edgeDraft.getGraphTimestamp());
        } else if (edgeDraft.getGraphInterval() != null) {
            edge.addInterval(edgeDraft.getGraphInterval());
        }

        //Dynamic edge weight (if any)
        flushEdgeWeight(edgeDraft, edge);

        //Attributes
        flushToElementAttributes(edgeDraft, edge);
    }

    protected void flushTimeSet(TimeSet timeSet, Element element) {
        TimeSet existingTimeSet = (TimeSet) element.getAttribute("timeset");
        element.setAttribute("timeset", mergeTimeSets(existingTimeSet, timeSet));
    }

    protected TimeSet mergeTimeSets(TimeSet set1, TimeSet set2) {
        if (set1 instanceof IntervalSet) {
            return mergeIntervalSets((IntervalSet) set1, (IntervalSet) set2);
        } else if (set1 instanceof TimestampSet) {
            return mergeTimestampSets((TimestampSet) set1, (TimestampSet) set2);
        } else {
            throw new IllegalArgumentException("Unknown TimeSet subtype " + set1.getClass());
        }
    }

    protected IntervalSet mergeIntervalSets(IntervalSet set1, IntervalSet set2) {
        IntervalSet merged = new IntervalSet();
        for (Interval i : set1.toArray()) {
            merged.add(i);
        }
        for (Interval i : set2.toArray()) {
            try {
                merged.add(i);
            } catch (Exception e) {
                //Catch overlapping intervals not allowed
                //TODO: Report??
            }
        }

        return merged;
    }

    protected TimestampSet mergeTimestampSets(TimestampSet set1, TimestampSet set2) {
        TimestampSet merged = new TimestampSet();
        for (Double t : set1.toArray()) {
            merged.add(t);
        }
        for (Double t : set2.toArray()) {
            merged.add(t);
        }

        return merged;
    }

    public void setWorkspace(Workspace workspace) {
        this.workspace = workspace;
    }

    public void setContainers(ContainerUnloader[] containers) {
        this.containers = containers;
    }

    public void setProgressTicket(ProgressTicket progressTicket) {
        this.progressTicket = progressTicket;
    }
}
