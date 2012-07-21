/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.gephi.legend.renderers;

import com.itextpdf.awt.PdfGraphics2D;
import com.itextpdf.text.pdf.PdfContentByte;
import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.font.LineBreakMeasurer;
import java.awt.font.TextAttribute;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.text.AttributedCharacterIterator;
import java.text.AttributedString;
import org.apache.batik.svggen.DefaultExtensionHandler;
import org.apache.batik.svggen.ImageHandlerBase64Encoder;
import org.apache.batik.svggen.SVGGeneratorContext;
import org.apache.batik.svggen.SVGGraphics2D;
import org.gephi.legend.api.LegendItem;
import org.gephi.legend.api.LegendItem.Alignment;
import static org.gephi.legend.api.LegendItem.TRANSFORMATION_ANCHOR_LINE_THICK;
import static org.gephi.legend.api.LegendItem.TRANSFORMATION_ANCHOR_SIZE;
import org.gephi.legend.api.LegendManager;
import org.gephi.legend.mouse.LegendMouseListener;
import org.gephi.legend.properties.LegendProperty;
import org.gephi.preview.api.*;
import org.gephi.preview.spi.MouseResponsiveRenderer;
import org.gephi.preview.spi.PreviewMouseListener;
import org.gephi.preview.spi.Renderer;
import org.gephi.project.api.ProjectController;
import org.gephi.project.api.Workspace;
import org.openide.util.Lookup;
import processing.core.PGraphicsJava2D;

/**
 *
 * @author edubecks
 */
public abstract class LegendItemRenderer implements Renderer, MouseResponsiveRenderer {

    public abstract void renderToGraphics(Graphics2D graphics2D, AffineTransform origin, Integer width, Integer height);

    public void readLocationProperties(Item item, PreviewProperties previewProperties) {
        ProjectController pc = Lookup.getDefault().lookup(ProjectController.class);
        Workspace workspace = pc.getCurrentWorkspace();
        readLocationProperties(item, previewProperties, workspace);

    }

    public void readLocationProperties(Item item, PreviewProperties previewProperties, Workspace workspace) {
        if (item != null) {
            currentItemIndex = item.getData(LegendItem.ITEM_INDEX);


            // DIMENSIONS
            currentWidth = previewProperties.getIntValue(LegendManager.getProperty(LegendProperty.LEGEND_PROPERTIES, currentItemIndex, LegendProperty.WIDTH));
            currentHeight = previewProperties.getIntValue(LegendManager.getProperty(LegendProperty.LEGEND_PROPERTIES, currentItemIndex, LegendProperty.HEIGHT));





            PreviewController previewController = Lookup.getDefault().lookup(PreviewController.class);
            PreviewModel previewModel = previewController.getModel(workspace);
            Dimension dimensions = previewModel.getDimensions();
            graphHeight = dimensions.height;
            graphWidth = dimensions.width;
            Point topLeftPosition = previewModel.getTopLeftPosition();
            graphOriginX = topLeftPosition.x;
            graphOriginY = topLeftPosition.y;


            // REAL POSITION
            currentRealOriginX = previewProperties.getFloatValue(LegendManager.getProperty(LegendProperty.LEGEND_PROPERTIES, currentItemIndex, LegendProperty.USER_ORIGIN_X));
            currentRealOriginY = previewProperties.getFloatValue(LegendManager.getProperty(LegendProperty.LEGEND_PROPERTIES, currentItemIndex, LegendProperty.USER_ORIGIN_Y));
<<<<<<< HEAD
=======




>>>>>>> fe06096bce76b1727feb2be03c2ed25d2fafb521
        }
    }

    public void readLegendPropertiesAndValues(Item item, PreviewProperties previewProperties) {

        if (item != null) {
<<<<<<< HEAD
            currentIsSelected = item.getData(LegendItem.IS_SELECTED);
            currentIsBeingTransformed = item.getData(LegendItem.IS_BEING_TRANSFORMED);
=======

//            PreviewProperty[] properties = item.getData(LegendItem.PROPERTIES);
//            String label = properties[0].getValue();

//            itemIndex = item.getData(LegendItem.ITEM_INDEX);
//            isScaling = item.getData(LegendItem.IS_SELECTED);

            currentIsSelected = item.getData(LegendItem.IS_SELECTED);
>>>>>>> fe06096bce76b1727feb2be03c2ed25d2fafb521

            readLocationProperties(item, previewProperties);

            // IS DISPLAYING
            isDisplayingLegend = previewProperties.getBooleanValue(LegendManager.getProperty(LegendProperty.LEGEND_PROPERTIES, currentItemIndex, LegendProperty.IS_DISPLAYING));

<<<<<<< HEAD
=======

//            // DIMENSIONS
//            currentWidth = previewProperties.getIntValue(LegendManager.getProperty(LegendProperty.LEGEND_PROPERTIES, itemIndex, LegendProperty.WIDTH));
//            currentHeight = previewProperties.getIntValue(LegendManager.getProperty(LegendProperty.LEGEND_PROPERTIES, itemIndex, LegendProperty.HEIGHT));

>>>>>>> fe06096bce76b1727feb2be03c2ed25d2fafb521
            //TITLE
            isDisplayingTitle = previewProperties.getBooleanValue(LegendManager.getProperty(LegendProperty.LEGEND_PROPERTIES, currentItemIndex, LegendProperty.TITLE_IS_DISPLAYING));
            titleFont = previewProperties.getFontValue(LegendManager.getProperty(LegendProperty.LEGEND_PROPERTIES, currentItemIndex, LegendProperty.TITLE_FONT));
            titleFontColor = previewProperties.getColorValue(LegendManager.getProperty(LegendProperty.LEGEND_PROPERTIES, currentItemIndex, LegendProperty.TITLE_FONT_COLOR));
            titleAlignment = (Alignment) previewProperties.getValue(LegendManager.getProperty(LegendProperty.LEGEND_PROPERTIES, currentItemIndex, LegendProperty.TITLE_ALIGNMENT));
            title = previewProperties.getStringValue(LegendManager.getProperty(LegendProperty.LEGEND_PROPERTIES, currentItemIndex, LegendProperty.TITLE));

            //DESCRIPTION
            isDisplayingDescription = previewProperties.getBooleanValue(LegendManager.getProperty(LegendProperty.LEGEND_PROPERTIES, currentItemIndex, LegendProperty.DESCRIPTION_IS_DISPLAYING));
            descriptionFont = previewProperties.getFontValue(LegendManager.getProperty(LegendProperty.LEGEND_PROPERTIES, currentItemIndex, LegendProperty.DESCRIPTION_FONT));
            descriptionFontColor = previewProperties.getColorValue(LegendManager.getProperty(LegendProperty.LEGEND_PROPERTIES, currentItemIndex, LegendProperty.DESCRIPTION_FONT_COLOR));
            descriptionAlignment = (Alignment) previewProperties.getValue(LegendManager.getProperty(LegendProperty.LEGEND_PROPERTIES, currentItemIndex, LegendProperty.DESCRIPTION_ALIGNMENT));
            description = previewProperties.getStringValue(LegendManager.getProperty(LegendProperty.LEGEND_PROPERTIES, currentItemIndex, LegendProperty.DESCRIPTION));



<<<<<<< HEAD
            processingMargin = 0f;
//            if(properties.hasProperty(PreviewProperty.MARGIN)){
//                processingMargin = properties.getFloatValue(PreviewProperty.MARGIN);
//                float tempWidth = previewModel.getProperties().getFloatValue("width");
//                float tempHeight = previewModel.getProperties().getFloatValue("height");
=======
//            originTranslation = new AffineTransform();




            processingMargin = 0f;
//            if(properties.hasProperty(PreviewProperty.MARGIN)){
//                processingMargin = properties.getFloatValue(PreviewProperty.MARGIN);
//                float tempWidth = previewModel.getProperties().getFloatValue("currentWidth");
//                float tempHeight = previewModel.getProperties().getFloatValue("currentHeight");
>>>>>>> fe06096bce76b1727feb2be03c2ed25d2fafb521
//                graphOriginX = tempWidth* processingMargin/100f;
//                graphOriginY = tempHeight * processingMargin/100f;
//            }

        }
    }

    private void renderSVG(SVGTarget target) {
        org.w3c.dom.Document document = target.getDocument();

        SVGGeneratorContext svgGeneratorContext = SVGGraphics2D.buildSVGGeneratorContext(document, new ImageHandlerBase64Encoder(), new DefaultExtensionHandler());
        svgGeneratorContext.setEmbeddedFontsOn(true);

        SVGGraphics2D graphics2D = new SVGGraphics2D(svgGeneratorContext, false);

        float targetOriginX = (int) graphOriginX;
        float targetOriginY = (int) graphOriginY;
//        graphics2D = (SVGGraphics2D) graphics2D.create((int)targetOriginX, (int)targetOriginY, (int)graphWidth, (int)graphHeight);
//        graphics2D.setClip((int)targetOriginX, (int)targetOriginY, (int)graphWidth, (int)graphHeight);
        originTranslation = new AffineTransform();
        originTranslation.translate(currentRealOriginX, currentRealOriginY);
        originTranslation.translate(targetOriginX, targetOriginY);
//        graphics2D.setTransform(originTranslation);
////      
//        
////        AffineTransform graphTransform = graphics2D.getTransform();
////        originTranslation = new AffineTransform(graphTransform);
////        originTranslation.translate(graphOriginX, graphOriginY);
//
//        graphics2D.setColor(Color.BLACK);
//        graphics2D.fillRect(10,10, 600,600);
//        graphics2D.setTransform(new AffineTransform());

        render(graphics2D, originTranslation, currentWidth, currentHeight);


        //appending
        org.w3c.dom.Element svgRoot = document.getDocumentElement();
        svgRoot.appendChild(graphics2D.getRoot().getLastChild());
        graphics2D.dispose();
    }

    private void renderPDF(PDFTarget target) {
        PdfContentByte pdfContentByte = target.getContentByte();
        com.itextpdf.text.Document pdfDocument = pdfContentByte.getPdfDocument();
        pdfContentByte.saveState();

        float pdfWidth = target.getPageSize().getWidth() - target.getMarginLeft() - target.getMarginRight();
        float pdfHeight = target.getPageSize().getHeight() - target.getMarginBottom() - target.getMarginTop();
        float scaleWidth = pdfWidth / graphWidth;
        float scaleHeight = pdfWidth / graphHeight;
        float scaleValue = Math.min(scaleWidth, scaleHeight);

//        float pdfOriginX = pdfWidth * graphOriginX / graphWidth;
//        float pdfOriginY = pdfHeight * graphOriginY / graphHeight;

        float targetOriginX = (int) graphOriginX;
        float targetOriginY = (int) graphOriginY - 12;
//        Graphics2D graphics2D = new PdfGraphics2D(pdfContentByte, graphWidth, graphHeight);
        originTranslation = new AffineTransform();
        originTranslation.translate(targetOriginX, targetOriginY);
        originTranslation.translate(currentRealOriginX, -currentRealOriginY);



//        originTranslation.translate(pdfOriginX, pdfOriginY);
//                originTranslation.scale(scaleValue, scaleValue);
//        originTranslation.translate(30, 30);
        pdfContentByte.transform(originTranslation);
//        Graphics2D graphics2D = new PdfGraphics2D(pdfContentByte, pdfDocument.getPageSize().getWidth(), pdfDocument.getPageSize().getHeight());
        Graphics2D graphics2D = new PdfGraphics2D(pdfContentByte, graphWidth, graphHeight);
//        Graphics2D graphics2D = pdfContentByte.createGraphics(pdfDocument.getPageSize().getWidth(), pdfDocument.getPageSize().getHeight());
        AffineTransform graphTransform = graphics2D.getTransform();







//        graphics2D.setColor(Color.PINK);
//        graphics2D.fillRect(tempX, tempY, (int) graphWidth, (int) graphHeight);

        originTranslation = new AffineTransform();
        render(graphics2D, originTranslation, currentWidth, currentHeight);
        graphics2D.dispose();
        pdfContentByte.restoreState();
    }

    private void renderProcessing(ProcessingTarget target) {

        Graphics2D graphics2D = (Graphics2D) ((PGraphicsJava2D) target.getGraphics()).g2;


        AffineTransform graphTransform = graphics2D.getTransform();
        AffineTransform saveState = graphics2D.getTransform();
        originTranslation = new AffineTransform(saveState);
//        originTranslation.translate(graphOriginX, graphOriginY);
        originTranslation.translate(currentRealOriginX, currentRealOriginY);
//        originTranslation.translate(graphTransform.getTranslateX(), graphTransform.getTranslateY());

//        originTranslation.translate(graphWidth*(processingMargin)/100, graphWidth*(processingMargin)/100);
//        originTranslation.scale(graphTransform.getScaleX(), graphTransform.getScaleY());

//        originTranslation.translate(graphTransform.getTranslateX(), graphTransform.getTranslateY());
//        originTranslation.translate(graphOriginX * graphTransform.getScaleX() , graphOriginY * graphTransform.getScaleY());
//        originTranslation.scale(graphTransform.getScaleX(), graphTransform.getScaleY());
//        originTranslation.translate(originX, originY);
//        originTranslation.translate(graphTransform.getTranslateX(),graphTransform.getTranslateY());

//        graphics2D.setTransform(originTranslation);
//        graphics2D.setColor(Color.BLACK);
//        graphics2D.drawRect(0, 0, (int)graphWidth, (int)graphHeight);
//        graphics2D.setColor(Color.RED);

//        originTranslation.scale(graphTransform.getScaleX(), graphTransform.getScaleY());

<<<<<<< HEAD
        // temp
        if (currentIsBeingTransformed) {
            renderTransformed(graphics2D, originTranslation, currentWidth, currentHeight);
            drawScaleAnchors(graphics2D, originTranslation, currentWidth, currentHeight);
        }
        else {
            render(graphics2D, originTranslation, currentWidth, currentHeight);
        }
        graphics2D.setTransform(saveState);

    }

    public void renderTransformed(Graphics2D graphics2D, AffineTransform origin, Integer width, Integer height) {
        graphics2D.setTransform(origin);
        graphics2D.setColor(TRANSFORMATION_LEGEND_BORDER_COLOR);
        graphics2D.fillRect(0, 0, width, height);
        graphics2D.setColor(TRANSFORMATION_LEGEND_CENTER_COLOR);
        int lineThickness = 5;
        graphics2D.fillRect(lineThickness, lineThickness, width - 2 * lineThickness, height - 2 * lineThickness);
        // centeredText
        graphics2D.setColor(TRANSFORMATION_LEGEND_BORDER_COLOR);
        graphics2D.setFont(TRANSFORMATION_LEGEND_FONT);
        int draggedLegendLabelWidth = graphics2D.getFontMetrics().stringWidth(TRANSFORMATION_LEGEND_LABEL);
        graphics2D.drawString("Legend", (width - draggedLegendLabelWidth) / 2, height / 2);

    }

    public void render(Graphics2D graphics2D, AffineTransform origin, Integer width, Integer height) {



=======
        render(graphics2D, originTranslation, currentWidth, currentHeight);
        graphics2D.setTransform(saveState);
    }

    public void render(Graphics2D graphics2D, AffineTransform origin, Integer width, Integer height) {
>>>>>>> fe06096bce76b1727feb2be03c2ed25d2fafb521
        graphics2D.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // TITLE
        AffineTransform titleOrigin = new AffineTransform(origin);
        float titleSpaceUsed = renderTitle(graphics2D, titleOrigin, width, height);
        boolean descriptionComputeSpace = true;
        float descriptionSpaceUsed = legendDrawText(graphics2D, description, descriptionFont, descriptionFontColor, origin.getTranslateX(), origin.getTranslateY(), width, height, descriptionAlignment, descriptionComputeSpace);

        // LEGEND
        AffineTransform legendOrigin = new AffineTransform(origin);
        legendOrigin.translate(0, titleSpaceUsed);
        int legendWidth = width;
        int legendHeight = (Integer) (height - Math.round(titleSpaceUsed) - Math.round(descriptionSpaceUsed));
        renderToGraphics(graphics2D, legendOrigin, legendWidth, legendHeight);

        // DESCRIPTION
        AffineTransform descriptionOrigin = new AffineTransform(origin);
        descriptionOrigin.translate(0, titleSpaceUsed + legendHeight);
        renderDescription(graphics2D, descriptionOrigin, width, height);


        // is scaling
        if (currentIsSelected) {
            drawScaleAnchors(graphics2D, origin, width, height);
        }
    }

    private void drawScaleAnchors(Graphics2D graphics2D, AffineTransform origin, Integer width, Integer height) {
        float[][] anchorLocations = {
            {-TRANSFORMATION_ANCHOR_SIZE / 2, -TRANSFORMATION_ANCHOR_SIZE / 2, TRANSFORMATION_ANCHOR_SIZE, TRANSFORMATION_ANCHOR_SIZE},
            {width - TRANSFORMATION_ANCHOR_SIZE / 2, -TRANSFORMATION_ANCHOR_SIZE / 2, TRANSFORMATION_ANCHOR_SIZE, TRANSFORMATION_ANCHOR_SIZE},
            {-TRANSFORMATION_ANCHOR_SIZE / 2, height - TRANSFORMATION_ANCHOR_SIZE / 2, TRANSFORMATION_ANCHOR_SIZE, TRANSFORMATION_ANCHOR_SIZE},
            {width - TRANSFORMATION_ANCHOR_SIZE / 2, height - TRANSFORMATION_ANCHOR_SIZE / 2, TRANSFORMATION_ANCHOR_SIZE, TRANSFORMATION_ANCHOR_SIZE}
        };

<<<<<<< HEAD

        graphics2D.setTransform(origin);
        graphics2D.setColor(TRANSFORMATION_LEGEND_BORDER_COLOR);
=======
        graphics2D.setTransform(origin);
        graphics2D.setColor(TRANSFORMATION_ANCHOR_COLOR);
>>>>>>> fe06096bce76b1727feb2be03c2ed25d2fafb521
        graphics2D.drawRect(0, 0, width, height);

        for (int i = 0; i < anchorLocations.length; i++) {
            graphics2D.setColor(TRANSFORMATION_ANCHOR_COLOR);
            graphics2D.fillRect((int) anchorLocations[i][0], (int) anchorLocations[i][1], (int) anchorLocations[i][2], (int) anchorLocations[i][3]);

            graphics2D.setColor(Color.WHITE);
            graphics2D.fillRect((int) anchorLocations[i][0] + TRANSFORMATION_ANCHOR_LINE_THICK,
<<<<<<< HEAD
                                (int) anchorLocations[i][1] + TRANSFORMATION_ANCHOR_LINE_THICK,
                                (int) anchorLocations[i][2] - 2 * TRANSFORMATION_ANCHOR_LINE_THICK,
                                (int) anchorLocations[i][3] - 2 * TRANSFORMATION_ANCHOR_LINE_THICK);
=======
                    (int) anchorLocations[i][1] + TRANSFORMATION_ANCHOR_LINE_THICK,
                    (int) anchorLocations[i][2] - 2 * TRANSFORMATION_ANCHOR_LINE_THICK,
                    (int) anchorLocations[i][3] - 2 * TRANSFORMATION_ANCHOR_LINE_THICK);
>>>>>>> fe06096bce76b1727feb2be03c2ed25d2fafb521
        }
    }

    public float renderDescription(Graphics2D graphics2D, AffineTransform origin, Integer width, Integer height) {
        if (isDisplayingDescription && !description.isEmpty()) {
            graphics2D.setTransform(origin);
            return legendDrawText(graphics2D, description, descriptionFont, descriptionFontColor, 0, 0, width, height, descriptionAlignment);
        }
        return 0f;
    }

    public float renderTitle(Graphics2D graphics2D, AffineTransform origin, Integer width, Integer height) {
        if (isDisplayingTitle && !title.isEmpty()) {
            graphics2D.setTransform(origin);
            return legendDrawText(graphics2D, title, titleFont, titleFontColor, 0, 0, width, height, titleAlignment);
        }
        return 0f;
    }

    @Override
    public void preProcess(PreviewModel previewModel) {
    }

    public abstract void readOwnPropertiesAndValues(Item item, PreviewProperties properties);

    @Override
    public void render(Item item, RenderTarget target, PreviewProperties properties) {

        if (item != null) {



//            graphOriginX = Float.MAX_VALUE;
//            graphOriginY = Float.MAX_VALUE;
//            for (Item node : previewModel.getItems(Item.NODE)) {
//                graphOriginX = Math.min(graphOriginX, (Float) node.getData(NodeItem.X) - (Float) node.getData(NodeItem.SIZE));
//                graphOriginY = Math.min(graphOriginY, (Float) node.getData(NodeItem.Y) - (Float) node.getData(NodeItem.SIZE));
//            }


//            graphOriginX -= defaultMargin;
//            graphOriginY -= defaultMargin;


            readLegendPropertiesAndValues(item, properties);
            readOwnPropertiesAndValues(item, properties);

            if (isDisplayingLegend) {

                if (target instanceof ProcessingTarget) {
                    renderProcessing((ProcessingTarget) target);
                }
                else if (target instanceof SVGTarget) {
                    renderSVG((SVGTarget) target);
                }
                else if (target instanceof PDFTarget) {
                    renderPDF((PDFTarget) target);
                }
            }
        }
    }

    @Override
    public PreviewProperty[] getProperties() {
        return new PreviewProperty[0];
    }

    protected float legendDrawText(Graphics2D graphics2D, String text, Font font, Color color, double x, double y, Integer width, Integer height, Alignment alignment, boolean isComputingSpace) {

        if (text.isEmpty()) {
            return 0f;
        }

        AttributedString styledText = new AttributedString(text);
        styledText.addAttribute(TextAttribute.FONT, font);
        graphics2D.setFont(font);
        graphics2D.setColor(color);
        AttributedCharacterIterator m_iterator = styledText.getIterator();
        int start = m_iterator.getBeginIndex();
        int end = m_iterator.getEndIndex();
        FontRenderContext fontRenderContext = graphics2D.getFontRenderContext();

        LineBreakMeasurer measurer = new LineBreakMeasurer(m_iterator, fontRenderContext);
        measurer.setPosition(start);


        float xText = (float) x, yText = (float) y; // text positions

        float descent = 0, leading = 0;
        while (measurer.getPosition() < end) {
            TextLayout layout = measurer.nextLayout(width);
            yText += layout.getAscent();
            if (!isComputingSpace) {
                switch (alignment) {
                    case LEFT: {
                        break;
                    }
                    case RIGHT: {
                        Rectangle2D bounds = layout.getBounds();
                        xText = (float) ((x + width - bounds.getWidth()) - bounds.getX());
                        break;
                    }
                    case CENTER: {
                        Rectangle2D bounds = layout.getBounds();
                        xText = (float) ((x + width / 2 - bounds.getWidth() / 2) - bounds.getX());
                        break;
                    }
                    case JUSTIFIED: {
                        if (measurer.getPosition() < end) {
                            layout = layout.getJustifiedLayout(width);
                        }
                        break;
                    }

                }
                layout.draw(graphics2D, xText, yText);
            }
            descent = layout.getDescent();
            leading = layout.getLeading();
            yText += descent + leading;
        }
        return (float) Math.ceil(yText - y - descent - leading);
    }

    protected float legendDrawText(Graphics2D graphics2D, String text, Font font, Color color, double x, double y, Integer width, Integer height, Alignment alignment) {
        return legendDrawText(graphics2D, text, font, color, x, y, width, height, alignment, false);
    }

    protected float computeVerticalTextSpaceUsed(Graphics2D graphics2D, String text, Font font, double x, double y, Integer width) {
        return legendDrawText(graphics2D, text, font, Color.BLACK, x, y, width, currentHeight, Alignment.LEFT, true);
    }
<<<<<<< HEAD

    private int isClickingInAnchor(int pointX, int pointY, Item item, PreviewProperties previewProperties) {
        Integer itemIndex = item.getData(LegendItem.ITEM_INDEX);
        float realOriginX = previewProperties.getFloatValue(LegendManager.getProperty(LegendProperty.LEGEND_PROPERTIES, itemIndex, LegendProperty.USER_ORIGIN_X));
        float realOriginY = previewProperties.getFloatValue(LegendManager.getProperty(LegendProperty.LEGEND_PROPERTIES, itemIndex, LegendProperty.USER_ORIGIN_Y));
        int width = previewProperties.getIntValue(LegendManager.getProperty(LegendProperty.LEGEND_PROPERTIES, itemIndex, LegendProperty.WIDTH));
        int height = previewProperties.getIntValue(LegendManager.getProperty(LegendProperty.LEGEND_PROPERTIES, itemIndex, LegendProperty.HEIGHT));
        float[][] anchorLocations = {
            {-TRANSFORMATION_ANCHOR_SIZE / 2, -TRANSFORMATION_ANCHOR_SIZE / 2, TRANSFORMATION_ANCHOR_SIZE, TRANSFORMATION_ANCHOR_SIZE},
            {width - TRANSFORMATION_ANCHOR_SIZE / 2, -TRANSFORMATION_ANCHOR_SIZE / 2, TRANSFORMATION_ANCHOR_SIZE, TRANSFORMATION_ANCHOR_SIZE},
            {-TRANSFORMATION_ANCHOR_SIZE / 2, height - TRANSFORMATION_ANCHOR_SIZE / 2, TRANSFORMATION_ANCHOR_SIZE, TRANSFORMATION_ANCHOR_SIZE},
            {width - TRANSFORMATION_ANCHOR_SIZE / 2, height - TRANSFORMATION_ANCHOR_SIZE / 2, TRANSFORMATION_ANCHOR_SIZE, TRANSFORMATION_ANCHOR_SIZE}
        };

        pointX -= realOriginX;
        pointY -= realOriginY;

        for (int i = 0; i < anchorLocations.length; i++) {
            if ((pointX >= anchorLocations[i][0] && pointX < (anchorLocations[i][0] + anchorLocations[i][2]))
                && (pointY >= anchorLocations[i][1] && pointY < (anchorLocations[i][1] + anchorLocations[i][3]))) {
                return i;
            }
        }
        return -1;
    }

    private boolean isClickingInLegend(int pointX, int pointY, Item item, PreviewProperties previewProperties) {
        Integer itemIndex = item.getData(LegendItem.ITEM_INDEX);
        float realOriginX = previewProperties.getFloatValue(LegendManager.getProperty(LegendProperty.LEGEND_PROPERTIES, itemIndex, LegendProperty.USER_ORIGIN_X));
        float realOriginY = previewProperties.getFloatValue(LegendManager.getProperty(LegendProperty.LEGEND_PROPERTIES, itemIndex, LegendProperty.USER_ORIGIN_Y));
        int width = previewProperties.getIntValue(LegendManager.getProperty(LegendProperty.LEGEND_PROPERTIES, itemIndex, LegendProperty.WIDTH));
        int height = previewProperties.getIntValue(LegendManager.getProperty(LegendProperty.LEGEND_PROPERTIES, itemIndex, LegendProperty.HEIGHT));
        if ((pointX >= realOriginX && pointX < (realOriginX + width))
            && (pointY >= realOriginY && pointY < (realOriginY + height))) {
            return true;
        }
        return false;
    }

    public void mousePressedEvent(PreviewMouseEvent event, PreviewProperties previewProperties, Workspace workspace) {
        LegendManager legendManager = previewProperties.getValue(LegendManager.LEGEND_PROPERTIES);
        for (Item item : legendManager.getLegendItems()) {
            if (isRendererForitem(item, previewProperties)) {
                Boolean isSelected = item.getData(LegendItem.IS_SELECTED);
                if (!isSelected) {
                    continue;
                }
                Integer itemIndex = item.getData(LegendItem.ITEM_INDEX);
                float realOriginX = previewProperties.getFloatValue(LegendManager.getProperty(LegendProperty.LEGEND_PROPERTIES, itemIndex, LegendProperty.USER_ORIGIN_X));
                float realOriginY = previewProperties.getFloatValue(LegendManager.getProperty(LegendProperty.LEGEND_PROPERTIES, itemIndex, LegendProperty.USER_ORIGIN_Y));
                int width = previewProperties.getIntValue(LegendManager.getProperty(LegendProperty.LEGEND_PROPERTIES, itemIndex, LegendProperty.WIDTH));
                int height = previewProperties.getIntValue(LegendManager.getProperty(LegendProperty.LEGEND_PROPERTIES, itemIndex, LegendProperty.HEIGHT));

                currentClickedAnchor = isClickingInAnchor(event.x, event.y, item, previewProperties);
                boolean isClickingInLegend = isClickingInLegend(event.x, event.y, item, previewProperties);
                boolean isClickingInAnchor = (currentClickedAnchor >= 0);


                if (isClickingInAnchor) {
                    relativeX = event.x - realOriginX;
                    relativeY = event.y - realOriginY;
                    switch (currentClickedAnchor) {
                        // Top Left Anchor
                        case 0: {
                            relativeAnchorX = relativeX;
                            relativeAnchorY = relativeY;
                            break;
                        }
                        // Top Right
                        case 1: {
                            relativeAnchorX = relativeX - width;
                            relativeAnchorY = relativeY;
                            break;
                        }
                        // Bottom Left
                        case 2: {
                            relativeAnchorX = relativeX;
                            relativeAnchorY = relativeY - height;
                            break;
                        }
                        // Bottom Right
                        case 3: {
                            relativeAnchorX = relativeX - width;
                            relativeAnchorY = relativeY - height;
                            break;
                        }
                    }

                    item.setData(LegendItem.IS_BEING_TRANSFORMED, Boolean.TRUE);
                    item.setData(LegendItem.CURRENT_TRANSFORMATION, TRANSFORMATION_SCALE_OPERATION);

                    event.setConsumed(true);
                    return;
                }
                else if (isClickingInLegend) {
                    relativeX = event.x - realOriginX;
                    relativeY = event.y - realOriginY;

                    item.setData(LegendItem.IS_BEING_TRANSFORMED, Boolean.TRUE);
                    item.setData(LegendItem.CURRENT_TRANSFORMATION, TRANSFORMATION_TRANSLATE_OPERATION);

                    event.setConsumed(true);
                    return;
                }
                else {
                    item.setData(LegendItem.IS_SELECTED, Boolean.FALSE);
                    item.setData(LegendItem.IS_BEING_TRANSFORMED, Boolean.FALSE);
                    relativeX = 0;
                    relativeY = 0;
                    event.setConsumed(true);
                }
            }
        }
        
    }

    public void mouseClickedEvent(PreviewMouseEvent event, PreviewProperties previewProperties, Workspace workspace) {
        LegendManager legendManager = previewProperties.getValue(LegendManager.LEGEND_PROPERTIES);
        boolean someItemStateChanged = false;
        for (Item item : legendManager.getLegendItems()) {
            Boolean isSelected = item.getData(LegendItem.IS_SELECTED);
            if (isClickingInLegend(event.x, event.y, item, previewProperties)) {
                //Unselect all other items:
                for (Item otherItem : legendManager.getLegendItems()) {
                    otherItem.setData(LegendItem.IS_SELECTED, Boolean.FALSE);
                }
                item.setData(LegendItem.IS_SELECTED, Boolean.TRUE);
                event.setConsumed(true);
                return;
            }
            else if (isSelected) {
                someItemStateChanged = someItemStateChanged || isSelected;
                item.setData(LegendItem.IS_SELECTED, Boolean.FALSE);
            }
        }
        if (someItemStateChanged) {
            event.setConsumed(true);
        }
    }

    public void mouseDraggedEvent(PreviewMouseEvent event, PreviewProperties previewProperties, Workspace workspace) {
        LegendManager legendManager = previewProperties.getValue(LegendManager.LEGEND_PROPERTIES);
        for (Item item : legendManager.getLegendItems()) {
            if (isRendererForitem(item, previewProperties)) {
                Boolean isSelected = item.getData(LegendItem.IS_SELECTED);
                if (!isSelected) {
                    continue;
                }
                Integer itemIndex = item.getData(LegendItem.ITEM_INDEX);

                if (currentIsBeingTransformed) {
                    String currentTransformation = item.getData(LegendItem.CURRENT_TRANSFORMATION);

                    // SCALING
                    if (currentTransformation.equals(TRANSFORMATION_SCALE_OPERATION)) {


                        float realOriginX = previewProperties.getFloatValue(LegendManager.getProperty(LegendProperty.LEGEND_PROPERTIES, itemIndex, LegendProperty.USER_ORIGIN_X));
                        float realOriginY = previewProperties.getFloatValue(LegendManager.getProperty(LegendProperty.LEGEND_PROPERTIES, itemIndex, LegendProperty.USER_ORIGIN_Y));
                        int width = previewProperties.getIntValue(LegendManager.getProperty(LegendProperty.LEGEND_PROPERTIES, itemIndex, LegendProperty.WIDTH));
                        int height = previewProperties.getIntValue(LegendManager.getProperty(LegendProperty.LEGEND_PROPERTIES, itemIndex, LegendProperty.HEIGHT));

                        boolean isClickingInAnchor = currentClickedAnchor >= 0;
                        if (isClickingInAnchor) {
                            float newOriginX = realOriginX;
                            float newOriginY = realOriginY;
                            float newWidth = width;
                            float newHeight = height;

                            switch (currentClickedAnchor) {
                                // Top Left Anchor
                                case 0: {
                                    newOriginX = event.x - relativeAnchorX;
                                    newOriginY = event.y - relativeAnchorY;
                                    newWidth = realOriginX + width - newOriginX;
                                    newHeight = realOriginY + height - newOriginY;
                                    break;
                                }
                                // Top Right
                                case 1: {
                                    newOriginX = realOriginX;
                                    newOriginY = event.y - relativeAnchorY;
                                    newWidth = event.x - relativeAnchorX - newOriginX;
                                    newHeight = realOriginY + height - newOriginY;
                                    break;
                                }
                                // Bottom Left
                                case 2: {
                                    newOriginX = event.x - relativeAnchorX;
                                    newOriginY = realOriginY;
                                    newWidth = realOriginX + width - newOriginX;
                                    newHeight = event.y - realOriginY - relativeAnchorY;
                                    break;
                                }
                                // Bottom Right
                                case 3: {
                                    newOriginX = realOriginX;
                                    newOriginY = realOriginY;
                                    newWidth = event.x - relativeAnchorX - newOriginX;
                                    newHeight = event.y - relativeAnchorY - newOriginY;
                                    break;
                                }
                            }

                            if (newWidth >= LEGEND_MIN_WIDTH && newHeight >= LEGEND_MIN_HEIGHT) {

                                previewProperties.getProperty(LegendManager.getProperty(LegendProperty.LEGEND_PROPERTIES, itemIndex, LegendProperty.USER_ORIGIN_X)).setValue(newOriginX);
                                previewProperties.getProperty(LegendManager.getProperty(LegendProperty.LEGEND_PROPERTIES, itemIndex, LegendProperty.USER_ORIGIN_Y)).setValue(newOriginY);
                                previewProperties.getProperty(LegendManager.getProperty(LegendProperty.LEGEND_PROPERTIES, itemIndex, LegendProperty.WIDTH)).setValue(newWidth);
                                previewProperties.getProperty(LegendManager.getProperty(LegendProperty.LEGEND_PROPERTIES, itemIndex, LegendProperty.HEIGHT)).setValue(newHeight);
                            }
                        }
                    }
                    else if (currentTransformation.equals(TRANSFORMATION_TRANSLATE_OPERATION)) {

                        float newRealOriginX = event.x - relativeX;
                        float newRealOriginY = event.y - relativeY;


                        previewProperties.getProperty(LegendManager.getProperty(LegendProperty.LEGEND_PROPERTIES, itemIndex, LegendProperty.USER_ORIGIN_X)).setValue(newRealOriginX);
                        previewProperties.getProperty(LegendManager.getProperty(LegendProperty.LEGEND_PROPERTIES, itemIndex, LegendProperty.USER_ORIGIN_Y)).setValue(newRealOriginY);

                    }

                    event.setConsumed(true);
                    return;

                }


            }
        }

    }

    public void mouseReleasedEvent(PreviewMouseEvent event, PreviewProperties previewProperties, Workspace workspace) {
        LegendManager legendManager = previewProperties.getValue(LegendManager.LEGEND_PROPERTIES);

        for (Item item : legendManager.getLegendItems()) {

            // BUG 
            if (isRendererForitem(item, previewProperties)) {
            Boolean isSelected = item.getData(LegendItem.IS_SELECTED);
            if (!isSelected) {
                continue;
            }

            item.setData(LegendItem.IS_BEING_TRANSFORMED, Boolean.FALSE);
            event.setConsumed(true);
            return;
            }
        }
        
    }

    @Override
    public PreviewMouseListener[] getListeners() {


        return new PreviewMouseListener[]{
                    new PreviewMouseListener() {
                        @Override
                        public void mouseClicked(PreviewMouseEvent event, PreviewProperties previewProperties, Workspace workspace) {
                            mouseClickedEvent(event, previewProperties, workspace);
                        }

                        @Override
                        public void mousePressed(PreviewMouseEvent event, PreviewProperties previewProperties, Workspace workspace) {
                            mousePressedEvent(event, previewProperties, workspace);
                        }

                        @Override
                        public void mouseDragged(PreviewMouseEvent event, PreviewProperties previewProperties, Workspace workspace) {
                            mouseDraggedEvent(event, previewProperties, workspace);
                        }

                        @Override
                        public void mouseReleased(PreviewMouseEvent event, PreviewProperties previewProperties, Workspace workspace) {
                            mouseReleasedEvent(event, previewProperties, workspace);
                        }

                    }
                };
    }

=======
    
    @Override
    public boolean needsPreviewMouseListener(PreviewMouseListener previewMouseListener){
        return previewMouseListener instanceof LegendMouseListener;
    }
   
>>>>>>> fe06096bce76b1727feb2be03c2ed25d2fafb521
    private Integer currentItemIndex;
    private float defaultMargin = 100f;
    private float graphOriginX = Float.MAX_VALUE;
    private float graphOriginY = Float.MAX_VALUE;
    private float graphWidth = 0;
    private float graphHeight = 0;
    // VARIABLES
    // IS DISPLAYING
    private Boolean isDisplayingLegend;
    // DIMENSIONS
    protected Integer currentWidth;
    protected Integer currentHeight;
    protected AffineTransform originTranslation;
    private float currentRealOriginX;
    private float currentRealOriginY;
    //description
    private Boolean isDisplayingDescription;
    private String description;
    private Alignment descriptionAlignment;
    private Font descriptionFont;
    private Color descriptionFontColor;
    //title
    private Boolean isDisplayingTitle;
    private String title;
    private Font titleFont;
    private Alignment titleAlignment;
    private Color titleFontColor;
    // processing margin
    private Float processingMargin;
<<<<<<< HEAD
    // click
    private Boolean currentIsSelected;
    private float relativeX;
    private float relativeY;
    private float relativeAnchorX;
    private float relativeAnchorY;
    // TRANSFORMATION
    private Boolean currentIsBeingTransformed;
    private int currentClickedAnchor;
    private final Color TRANSFORMATION_LEGEND_BORDER_COLOR = new Color(0.5f, 0.5f, 0.5f, 0.5f);
    private final Color TRANSFORMATION_LEGEND_CENTER_COLOR = new Color(1f, 1f, 1f, 0.5f);
    private final Font TRANSFORMATION_LEGEND_FONT = new Font(Font.SANS_SERIF, Font.BOLD, 24);
    private final String TRANSFORMATION_LEGEND_LABEL = "Legend";
    private final String TRANSFORMATION_SCALE_OPERATION = "Scale operation";
    private final String TRANSFORMATION_TRANSLATE_OPERATION = "Translate operation";
    private final Color TRANSFORMATION_ANCHOR_COLOR = Color.LIGHT_GRAY;
    private final int TRANSFORMATION_ANCHOR_SIZE = 20;
    private final int TRANSFORMATION_ANCHOR_LINE_THICK = 3;
    private final float LEGEND_MIN_WIDTH = 50;
    private final float LEGEND_MIN_HEIGHT = 50;
=======
    // is scaling legend
    // TRANSFORMATION
    private Boolean currentIsSelected = Boolean.FALSE;
    private final Color TRANSFORMATION_ANCHOR_COLOR = Color.LIGHT_GRAY;
>>>>>>> fe06096bce76b1727feb2be03c2ed25d2fafb521
}
