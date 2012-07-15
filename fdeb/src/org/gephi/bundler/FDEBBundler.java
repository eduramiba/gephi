package org.gephi.bundler;

import java.util.ArrayList;
import java.util.Arrays;
import org.gephi.edgelayout.spi.EdgeLayout;
import org.gephi.edgelayout.spi.EdgeLayoutBuilder;
import org.gephi.fdeb.FDEBCompatibilityRecord;
import org.gephi.fdeb.FDEBLayoutData;
import org.gephi.fdeb.utils.FDEBUtilities;
import org.gephi.graph.api.Edge;
import org.gephi.utils.longtask.spi.LongTask;

/**
 *
 * @author megaterik
 */
public class FDEBBundler extends FDEBAbstractBundler implements EdgeLayout, LongTask {

    public FDEBBundler(EdgeLayoutBuilder layoutBuilder) {
        super(layoutBuilder);
    }

    /*
     * Get parameters and init structures
     */
    @Override
    public void initAlgo() {
        for (Edge edge : graphModel.getGraph().getEdges()) {
            edge.getEdgeData().setLayoutData(
                    new FDEBLayoutData(edge.getSource().getNodeData().x(), edge.getSource().getNodeData().y(),
                    edge.getTarget().getNodeData().x(), edge.getTarget().getNodeData().y()));
        }
        cycle = 1;
        setConverged(false);
        if (!useUserConstant) {
            sprintConstant = FDEBUtilities.calculateSprintConstant(graphModel.getGraph());
        }
        subdivisionPointsPerEdge = 1;//start and end doesnt count
        stepSize = stepSizeAtTheBeginning;
        iterationsPerCycle = iterationsPerCycleAtTheBeginning;
        System.out.println("K " + sprintConstant);

        createCompatibilityLists();
    }

    @Override
    public void goAlgo() {
        if (cancel) {
            return;
        }

        System.err.println("Next iteration");
        for (int step = 0; step < iterationsPerCycle; step++) {
            for (Edge edge : graphModel.getGraph().getEdges()) {
                if (cancel)
                    return;
                ((FDEBLayoutData) edge.getEdgeData().getLayoutData()).newSubdivisionPoints = Arrays.copyOf(((FDEBLayoutData) edge.getEdgeData().getLayoutData()).subdivisionPoints,
                        ((FDEBLayoutData) edge.getEdgeData().getLayoutData()).subdivisionPoints.length);
            }

            for (Edge edge : graphModel.getGraph().getEdges()) {
                if (cancel) {
                    return;
                }
                FDEBUtilities.updateNewSubdivisionPoints(edge, sprintConstant, stepSize, useInverseQuadraticModel);
            }

            for (Edge edge : graphModel.getGraph().getEdges()) {
                ((FDEBLayoutData) edge.getEdgeData().getLayoutData()).subdivisionPoints = ((FDEBLayoutData) edge.getEdgeData().getLayoutData()).newSubdivisionPoints;
                ((FDEBLayoutData) edge.getEdgeData().getLayoutData()).newSubdivisionPoints = null;
            }

            if (cancel) {
                return;
            }
        }

        if (cycle == numCycles) {
            setConverged(true);
        } else {
            prepareForTheNextStep();
        }
    }

    void prepareForTheNextStep() {
        cycle++;
        stepSize *= (1.0 - stepDampingFactor);
        iterationsPerCycle = (iterationsPerCycle * 2) / 3;
        divideEdges();
    }

    void divideEdges() {
        subdivisionPointsPerEdge *= subdivisionPointIncreaseRate;
        for (Edge edge : graphModel.getGraph().getEdges()) {
            FDEBUtilities.divideEdge(edge, subdivisionPointsPerEdge);
        }
    }

    @Override
    public void endAlgo() {
    }

    private void createCompatibilityLists() {
        ArrayList<FDEBCompatibilityRecord> similar = new ArrayList<FDEBCompatibilityRecord>();
        for (Edge edge : graphModel.getGraph().getEdges()) {
            if (cancel)
                return;
            FDEBUtilities.createCompatibilityRecords(edge, compatibilityThreshold, graphModel.getGraph(), computator);
        }
        int totalEdges = graphModel.getGraph().getEdgeCount() * graphModel.getGraph().getEdgeCount();
        int passedEdges = 0;
        double csum = 0;
        for (Edge edge : graphModel.getGraph().getEdges()) {
            if (cancel) {
                return;
            }
            passedEdges += ((FDEBLayoutData) edge.getEdgeData().getLayoutData()).similarEdges.length;
            for (FDEBCompatibilityRecord record : ((FDEBLayoutData) edge.getEdgeData().getLayoutData()).similarEdges) {
                csum += record.compatibility;
            }
        }
        System.err.println("total: " + totalEdges + " passed " + passedEdges + " sum of compatibility " + csum
                + " fraction " + ((double) passedEdges) / totalEdges);
    }

    @Override
    public void modifyAlgo() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
