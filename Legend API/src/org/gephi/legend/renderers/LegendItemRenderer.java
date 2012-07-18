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
import org.apache.batik.svggen.*;
import org.gephi.legend.api.LegendItem;
import org.gephi.legend.api.LegendItem.Alignment;
import org.gephi.legend.api.LegendManager;
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
            itemIndex = item.getData(LegendItem.ITEM_INDEX);
            isScaling = item.getData(LegendItem.IS_SCALING);

            // DIMENSIONS
            width = previewProperties.getIntValue(LegendManager.getProperty(LegendProperty.LEGEND_PROPERTIES, itemIndex, LegendProperty.WIDTH));
//            System.out.println("@Var: Reading PROPERTY width: " + width);
            height = previewProperties.getIntValue(LegendManager.getProperty(LegendProperty.LEGEND_PROPERTIES, itemIndex, LegendProperty.HEIGHT));
//            System.out.println("@Var: Reading PROPERTY height: " + height);


            


            PreviewController previewController = Lookup.getDefault().lookup(PreviewController.class);
            PreviewModel previewModel = previewController.getModel(workspace);
            Dimension dimensions = previewModel.getDimensions();
            System.out.println("@Var: previewModel: " + previewModel);
            System.out.println("@Var: previewModel getTopLeftPosition: " + previewModel.getTopLeftPosition());
            System.out.println("@Var: previewModel getDimensions: " + previewModel.getDimensions());
            graphHeight = dimensions.height;
            graphWidth = dimensions.width;
//            System.out.println("@Var: dimensions: " + dimensions);
            Point topLeftPosition = previewModel.getTopLeftPosition();
            graphOriginX = topLeftPosition.x;
            System.out.println("@Var: Reading graphOriginX: " + graphOriginX);
//            System.out.println("@Var: graphOriginX: "+graphOriginX);
            graphOriginY = topLeftPosition.y;
            System.out.println("@Var: Reading graphOriginY: " + graphOriginY);
            
            
            // REAL POSITION
//            userOriginY = previewProperties.getFloatValue(LegendManager.getProperty(LegendProperty.LEGEND_PROPERTIES, itemIndex, LegendProperty.USER_ORIGIN_Y));
//            userOriginX = previewProperties.getFloatValue(LegendManager.getProperty(LegendProperty.LEGEND_PROPERTIES, itemIndex, LegendProperty.USER_ORIGIN_X));
//            realOriginX = userOriginX + graphOriginX;
//            realOriginY = userOriginY + graphOriginY;
            realOriginY = previewProperties.getFloatValue(LegendManager.getProperty(LegendProperty.LEGEND_PROPERTIES, itemIndex, LegendProperty.REAL_ORIGIN_Y));
            realOriginX = previewProperties.getFloatValue(LegendManager.getProperty(LegendProperty.LEGEND_PROPERTIES, itemIndex, LegendProperty.REAL_ORIGIN_X));
            System.out.println("@Var: realOriginX: " + realOriginX);
            System.out.println("@Var: realOriginY: " + realOriginY);


//            // USER ORIGIN
//            previewProperties.getProperty(LegendManager.getProperty(LegendProperty.LEGEND_PROPERTIES, itemIndex, LegendProperty.USER_ORIGIN_X)).setValue(realOriginX - graphOriginX);
//            previewProperties.getProperty(LegendManager.getProperty(LegendProperty.LEGEND_PROPERTIES, itemIndex, LegendProperty.USER_ORIGIN_Y)).setValue(realOriginY - graphOriginY);


//            userOriginX = previewProperties.getFloatValue(LegendManager.getProperty(LegendProperty.LEGEND_PROPERTIES, itemIndex, LegendProperty.USER_ORIGIN_X));
//            System.out.println("@Var: Reading PROPERTY originX: " + realOriginX);
////            System.out.println("@Var: originX: "+originX);
//            userOriginY = previewProperties.getFloatValue(LegendManager.getProperty(LegendProperty.LEGEND_PROPERTIES, itemIndex, LegendProperty.USER_ORIGIN_Y));
//            System.out.println("@Var: Reading PROPERTY originY: " + realOriginY);

            int anchorSize = 20;
            int[][] anchorLocations = {
                {-anchorSize / 2, -anchorSize / 2, anchorSize, anchorSize},
                {width - anchorSize / 2, -anchorSize / 2, anchorSize, anchorSize},
                {-anchorSize / 2, height - anchorSize / 2, anchorSize, anchorSize},
                {width - anchorSize / 2, height - anchorSize / 2, anchorSize, anchorSize}
            };
            this.anchorLocations = anchorLocations;


        }
    }

    public void readLegendPropertiesAndValues(Item item, PreviewProperties previewProperties) {

        if (item != null) {

//            PreviewProperty[] properties = item.getData(LegendItem.PROPERTIES);
//            String label = properties[0].getValue();

//            itemIndex = item.getData(LegendItem.ITEM_INDEX);
//            isScaling = item.getData(LegendItem.IS_SCALING);

            readLocationProperties(item, previewProperties);

//            // UPDATING LABEL
//            String label = previewProperties.getStringValue(LegendManager.getProperty(LegendProperty.LEGEND_PROPERTIES, itemIndex, LegendProperty.LABEL));
//            LegendManager legendManager = previewProperties.getValue(LegendManager.LEGEND_PROPERTIES);

            // IS DISPLAYING
            isDisplayingLegend = previewProperties.getBooleanValue(LegendManager.getProperty(LegendProperty.LEGEND_PROPERTIES, itemIndex, LegendProperty.IS_DISPLAYING));


//            // DIMENSIONS
//            width = previewProperties.getIntValue(LegendManager.getProperty(LegendProperty.LEGEND_PROPERTIES, itemIndex, LegendProperty.WIDTH));
//            System.out.println("@Var: Reading PROPERTY width: "+width);
//            height = previewProperties.getIntValue(LegendManager.getProperty(LegendProperty.LEGEND_PROPERTIES, itemIndex, LegendProperty.HEIGHT));
//            System.out.println("@Var: Reading PROPERTY height: "+height);

            //TITLE
            isDisplayingTitle = previewProperties.getBooleanValue(LegendManager.getProperty(LegendProperty.LEGEND_PROPERTIES, itemIndex, LegendProperty.TITLE_IS_DISPLAYING));
            titleFont = previewProperties.getFontValue(LegendManager.getProperty(LegendProperty.LEGEND_PROPERTIES, itemIndex, LegendProperty.TITLE_FONT));
            titleFontColor = previewProperties.getColorValue(LegendManager.getProperty(LegendProperty.LEGEND_PROPERTIES, itemIndex, LegendProperty.TITLE_FONT_COLOR));
            titleAlignment = (Alignment) previewProperties.getValue(LegendManager.getProperty(LegendProperty.LEGEND_PROPERTIES, itemIndex, LegendProperty.TITLE_ALIGNMENT));
            title = previewProperties.getStringValue(LegendManager.getProperty(LegendProperty.LEGEND_PROPERTIES, itemIndex, LegendProperty.TITLE));

            //DESCRIPTION
            isDisplayingDescription = previewProperties.getBooleanValue(LegendManager.getProperty(LegendProperty.LEGEND_PROPERTIES, itemIndex, LegendProperty.DESCRIPTION_IS_DISPLAYING));
            descriptionFont = previewProperties.getFontValue(LegendManager.getProperty(LegendProperty.LEGEND_PROPERTIES, itemIndex, LegendProperty.DESCRIPTION_FONT));
            descriptionFontColor = previewProperties.getColorValue(LegendManager.getProperty(LegendProperty.LEGEND_PROPERTIES, itemIndex, LegendProperty.DESCRIPTION_FONT_COLOR));
            descriptionAlignment = (Alignment) previewProperties.getValue(LegendManager.getProperty(LegendProperty.LEGEND_PROPERTIES, itemIndex, LegendProperty.DESCRIPTION_ALIGNMENT));
            description = previewProperties.getStringValue(LegendManager.getProperty(LegendProperty.LEGEND_PROPERTIES, itemIndex, LegendProperty.DESCRIPTION));



//            System.out.println("@Var: originY: "+originY);
//            originTranslation = new AffineTransform();


//            System.out.println("@Var: graphOriginY: "+graphOriginY);


            processingMargin = 0f;
//            if(properties.hasProperty(PreviewProperty.MARGIN)){
//                processingMargin = properties.getFloatValue(PreviewProperty.MARGIN);
//                float tempWidth = previewModel.getProperties().getFloatValue("width");
//                System.out.println("@Var: tempWidth: "+tempWidth);
//                float tempHeight = previewModel.getProperties().getFloatValue("height");
//                System.out.println("@Var: tempHeight: "+tempHeight);
//                System.out.println("@Var: processingMargin: "+processingMargin);
//                graphOriginX = tempWidth* processingMargin/100f;
//                System.out.println("@Var: Margin graphOriginX: "+graphOriginX);
//                graphOriginY = tempHeight * processingMargin/100f;
//                System.out.println("@Var: Margin graphOriginY: "+graphOriginY);
//            }
//            System.out.println("@Var: processingMargin: "+processingMargin);

        }
    }

    private void renderSVG(SVGTarget target) {
        org.w3c.dom.Document document = target.getDocument();

        SVGGeneratorContext svgGeneratorContext = SVGGraphics2D.buildSVGGeneratorContext(document, new ImageHandlerBase64Encoder(), new DefaultExtensionHandler());
        svgGeneratorContext.setEmbeddedFontsOn(true);

        SVGGraphics2D graphics2D = new SVGGraphics2D(svgGeneratorContext, false);

        float targetOriginX = (int) graphOriginX;
//        System.out.println("@Var: tempX: " + targetOriginX);
        float targetOriginY = (int) graphOriginY;
//        System.out.println("@Var: tempY: " + targetOriginY);
//        graphics2D = (SVGGraphics2D) graphics2D.create((int)targetOriginX, (int)targetOriginY, (int)graphWidth, (int)graphHeight);
//        graphics2D.setClip((int)targetOriginX, (int)targetOriginY, (int)graphWidth, (int)graphHeight);
        originTranslation = new AffineTransform();
        originTranslation.translate(realOriginX, realOriginY);
//        System.out.println("@Var: originTranslation: "+originTranslation);
        originTranslation.translate(targetOriginX, targetOriginY);
//        System.out.println("@Var: originTranslation: "+originTranslation);
//        graphics2D.setTransform(originTranslation);
////      
//        
////        AffineTransform graphTransform = graphics2D.getTransform();
////        System.out.printf("SVG:   graphTransform T[%f,%f] S[%f,%f]\n", graphTransform.getTranslateX(), graphTransform.getTranslateY(), graphTransform.getScaleX(), graphTransform.getScaleY());
////        originTranslation = new AffineTransform(graphTransform);
////        originTranslation.translate(graphOriginX, graphOriginY);
//
//        graphics2D.setColor(Color.BLACK);
//        graphics2D.fillRect(10,10, 600,600);
//        graphics2D.setTransform(new AffineTransform());

        render(graphics2D, originTranslation, width, height);


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
//        System.out.println("@Var: pdfWidth: " + pdfWidth);
        float pdfHeight = target.getPageSize().getHeight() - target.getMarginBottom() - target.getMarginTop();
        float scaleWidth = pdfWidth / graphWidth;
//        System.out.println("@Var: scaleWidth: " + scaleWidth);
        float scaleHeight = pdfWidth / graphHeight;
//        System.out.println("@Var: scaleHeight: " + scaleHeight);
        float scaleValue = Math.min(scaleWidth, scaleHeight);
//        System.out.println("@Var: scaleValue: " + scaleValue);

//        float pdfOriginX = pdfWidth * graphOriginX / graphWidth;
//        float pdfOriginY = pdfHeight * graphOriginY / graphHeight;

        float targetOriginX = (int) graphOriginX;
        System.out.println("@Var: tempX: " + targetOriginX);
        float targetOriginY = (int) graphOriginY - 12;
//        Graphics2D graphics2D = new PdfGraphics2D(pdfContentByte, graphWidth, graphHeight);
        System.out.println("@Var: tempY: " + targetOriginY);
        originTranslation = new AffineTransform();
        originTranslation.translate(targetOriginX, targetOriginY);
        originTranslation.translate(realOriginX, -realOriginY);



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
        render(graphics2D, originTranslation, width, height);
        graphics2D.dispose();
        pdfContentByte.restoreState();
    }

    private void renderProcessing(ProcessingTarget target) {
        Graphics2D graphics2D = (Graphics2D) ((PGraphicsJava2D) target.getGraphics()).g2;
//        System.out.println("+----------------------->@Var: graphics2D: "+graphics2D);


        AffineTransform graphTransform = graphics2D.getTransform();
//        System.out.printf("PROCESSING:   graphTransform T[%f,%f] S[%f,%f]\n", graphTransform.getTranslateX(), graphTransform.getTranslateY(), graphTransform.getScaleX(), graphTransform.getScaleY());
        AffineTransform saveState = graphics2D.getTransform();
        originTranslation = new AffineTransform(saveState);
//        originTranslation.translate(graphOriginX, graphOriginY);
        originTranslation.translate(realOriginX, realOriginY);
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

        // temp


        render(graphics2D, originTranslation, width, height);
        graphics2D.setTransform(saveState);
    }

    public void render(Graphics2D graphics2D, AffineTransform origin, Integer width, Integer height) {
//        System.out.println("+----------------------->@Var: graphics2D: "+graphics2D);



        graphics2D.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // TITLE
        AffineTransform titleOrigin = new AffineTransform(origin);
        float titleSpaceUsed = renderTitle(graphics2D, titleOrigin, width, height);
//        System.out.println("@Var: titleOrigin: "+titleOrigin);
        boolean descriptionComputeSpace = true;
        float descriptionSpaceUsed = legendDrawText(graphics2D, description, descriptionFont, descriptionFontColor, origin.getTranslateX(), origin.getTranslateY(), width, height, descriptionAlignment, descriptionComputeSpace);

        // LEGEND
        AffineTransform legendOrigin = new AffineTransform(origin);
        legendOrigin.translate(0, titleSpaceUsed);
        int legendWidth = width;
        int legendHeight = (Integer) (height - Math.round(titleSpaceUsed) - Math.round(descriptionSpaceUsed));
        renderToGraphics(graphics2D, legendOrigin, legendWidth, legendHeight);
//        System.out.println("@Var: legendOrigin: "+legendOrigin);

        // DESCRIPTION
        AffineTransform descriptionOrigin = new AffineTransform(origin);
        descriptionOrigin.translate(0, titleSpaceUsed + legendHeight);
        renderDescription(graphics2D, descriptionOrigin, width, height);
//        System.out.println("@Var: descriptionOrigin: " + descriptionOrigin);


        // is scaling
        if (isScaling) {
            drawScaleAnchors(graphics2D, origin, width, height);
        }


    }

    private void drawScaleAnchors(Graphics2D graphics2D, AffineTransform origin, Integer width, Integer height) {

        Color anchorColor = Color.LIGHT_GRAY;

        graphics2D.setTransform(origin);
        graphics2D.setColor(anchorColor);
        graphics2D.drawRect(0, 0, width, height);
        graphics2D.setColor(Color.DARK_GRAY);

        for (int i = 0; i < anchorLocations.length; i++) {
            graphics2D.drawRect(anchorLocations[i][0], anchorLocations[i][1], anchorLocations[i][2], anchorLocations[i][3]);
        }
//        
//        graphics2D.drawRect(-anchorSize / 2, height - anchorSize / 2, anchorSize, anchorSize);
//        graphics2D.drawRect(width - anchorSize / 2, -anchorSize / 2, anchorSize, anchorSize);
//        graphics2D.drawRect(width - anchorSize / 2, height - anchorSize / 2, anchorSize, anchorSize);
//
    }

    private int isClickingInAnchor(int pointX, int pointY) {
        System.out.println("@Var: isClickingInAnchor: ");
        System.out.println("@Var: pointX: " + pointX);
        System.out.println("@Var: pointY: " + pointY);

        pointX -= graphOriginX;
        System.out.println("@Var: pointX: " + pointX);
        pointY -= graphOriginY;
        System.out.println("@Var: pointY: " + pointY);


        for (int i = 0; i < anchorLocations.length; i++) {
            System.out.println("@Var: Anchor i: " + i);
            System.out.println("@Var: pointX: " + pointX + " in [" + anchorLocations[i][0] + "," + (anchorLocations[i][0] + anchorLocations[i][2]) + "]");
            System.out.println("@Var: pointY: " + pointY + " in [" + anchorLocations[i][1] + "," + (anchorLocations[i][1] + anchorLocations[i][3]) + "]");
            if ((pointX >= anchorLocations[i][0] && pointX < (anchorLocations[i][0] + anchorLocations[i][2]))
                && (pointY >= anchorLocations[i][1] && pointY < (anchorLocations[i][1] + anchorLocations[i][3]))) {
                System.out.println("@Var: Anchor SELECTED i: " + i);
                return i;
            }
        }
        return -1;
    }

    private boolean isClickingInLegend(int pointX, int pointY) {
        System.out.println("@Var: pointX: " + pointX + " in [" + realOriginX + "," + (realOriginX + width) + "]");
        System.out.println("@Var: pointY: " + pointY + " in [" + realOriginY + "," + (realOriginY + height) + "]");
        if ((pointX >= realOriginX && pointX < (realOriginX + width))
            && (pointY >= realOriginY && pointY < (realOriginY + height))) {
            System.out.println("@Var: isClickingInLegend: ");

            return true;
        }
        return false;
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



//            System.out.println("@Var: topLeftPosition: "+topLeftPosition);
//            graphOriginX = Float.MAX_VALUE;
//            graphOriginY = Float.MAX_VALUE;
//            for (Item node : previewModel.getItems(Item.NODE)) {
//                graphOriginX = Math.min(graphOriginX, (Float) node.getData(NodeItem.X) - (Float) node.getData(NodeItem.SIZE));
//                graphOriginY = Math.min(graphOriginY, (Float) node.getData(NodeItem.Y) - (Float) node.getData(NodeItem.SIZE));
//            }

//            System.out.printf("graphOrigin [%f.%f]\n", graphOriginX, graphOriginY);

//            graphOriginX -= defaultMargin;
//            graphOriginY -= defaultMargin;

            System.out.println("@Var: rendering item: " + item.getType());

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
//        System.out.println("@Var: +--------------------+");
//        System.out.println("@Var: text: "+text);
//        System.out.println("@Var: x: "+x);
//        System.out.println("@Var: y: "+y);

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
        return legendDrawText(graphics2D, text, font, Color.BLACK, x, y, width, height, Alignment.LEFT, true);
    }

    public void mousePressedEvent(PreviewMouseEvent event, PreviewProperties previewProperties, Workspace workspace) {
        System.out.printf("\n\n\n");
        System.out.println("@Var: mousePressed: ");
        LegendManager legendManager = previewProperties.getValue(LegendManager.LEGEND_PROPERTIES);
        Item activeLegendItem = legendManager.getActiveLegendItem();
        readLocationProperties(activeLegendItem, previewProperties);

        // is selected

        Boolean isSelected = activeLegendItem.getData(LegendItem.IS_SCALING);
        int clickedAnchor = isClickingInAnchor(event.x, event.y);
        Boolean isClickingInAnchor = (clickedAnchor >= 0) ? true : false;
        if (isSelected && isClickingInAnchor) {

            // Top Left anchor
            System.out.println("@Var: clickedAnchor: " + clickedAnchor);

        }
        else if (isSelected && isClickingInLegend(event.x, event.y)) {
            relativeX = event.x - realOriginX;
            relativeY = event.y - realOriginY;
//            System.out.println("@Var: relativeX: " + relativeX);
//            System.out.println("@Var: relativeY: " + relativeY);
        }
        else {
            activeLegendItem.setData(LegendItem.IS_SCALING, Boolean.FALSE);

            relativeX = 0;
            relativeY = 0;
        }


        event.setConsumed(true);
    }

    public void mouseClickedEvent(PreviewMouseEvent event, PreviewProperties previewProperties, Workspace workspace) {
        System.out.printf("\n\n\n");
        System.out.println("@Var: mouseClicked: ");

        LegendManager legendManager = previewProperties.getValue(LegendManager.LEGEND_PROPERTIES);
        Item activeLegendItem = legendManager.getActiveLegendItem();
        readLocationProperties(activeLegendItem, previewProperties);
        Boolean isSelected = activeLegendItem.getData(LegendItem.IS_SCALING);
        int clickedAnchor = isClickingInAnchor(event.x, event.y);
        Boolean isClickingInAnchor = (clickedAnchor >= 0) ? true : false;
        if (isSelected && isClickingInAnchor) {

            // Top Left anchor
            System.out.println("@Var: clickedAnchor: " + clickedAnchor);

        }
        else if (isClickingInLegend(event.x, event.y)) {
            activeLegendItem.setData(LegendItem.IS_SCALING, Boolean.TRUE);

        }
        else {
            activeLegendItem.setData(LegendItem.IS_SCALING, Boolean.FALSE);
        }
        event.setConsumed(true);
    }

    public void mouseDraggedEvent(PreviewMouseEvent event, PreviewProperties previewProperties, Workspace workspace) {
        System.out.printf("\n\n\n");
        System.out.println("@Var: +---------------------> mouseDragged: ");


        LegendManager legendManager = previewProperties.getValue(LegendManager.LEGEND_PROPERTIES);
        Item activeLegendItem = legendManager.getActiveLegendItem();
        readLocationProperties(activeLegendItem, previewProperties);


        // is selected
        Boolean isSelected = activeLegendItem.getData(LegendItem.IS_SCALING);
        int clickedAnchor = isClickingInAnchor(event.x, event.y);
        Boolean isClickingInAnchor = (clickedAnchor >= 0) ? true : false;
        if (isSelected && isClickingInAnchor) {

            float relativeAnchorX = relativeX - graphWidth;
            float relativeAnchorY = relativeY - graphHeight;


            switch (clickedAnchor) {
                // Top Left Anchor
                case 0: {
                    System.out.println("@Var: graphOriginX: " + graphOriginX);
                    System.out.println("@Var: graphOriginY: " + graphOriginY);


                    System.out.println("@Var: dragging relativeX: " + relativeX);
                    System.out.println("@Var: dragging relativeY: " + relativeY);


                    float newRealOriginX = event.x - relativeX;
                    System.out.println("@Var: newRealOriginX: " + newRealOriginX);
                    float newRealOriginY = event.y - relativeY;
                    System.out.println("@Var: newRealOriginY: " + newRealOriginY);
                    float newUserOriginX = event.x - graphOriginX - relativeX;
                    System.out.println("@Var: newUserOriginX: " + newUserOriginX);
                    float newUserOriginY = event.y - graphOriginY - relativeY;
                    System.out.println("@Var: newUserOriginY: " + newUserOriginY);
//                    previewProperties.getProperty(LegendManager.getProperty(LegendProperty.LEGEND_PROPERTIES, itemIndex, LegendProperty.USER_ORIGIN_X)).setValue(newUserOriginX);
                    break;
                }
                // Top Right
                case 1: {
//                    previewProperties.getProperty(LegendManager.getProperty(LegendProperty.LEGEND_PROPERTIES, itemIndex, LegendProperty.WIDTH)).setValue(event.x - relativeAnchorX - graphOriginX);
//                    previewProperties.getProperty(LegendManager.getProperty(LegendProperty.LEGEND_PROPERTIES, itemIndex, LegendProperty.HEIGHT)).setValue(event.x - relativeAnchorY - graphOriginY);
                    break;
                }
                // Bottom Left
                case 2: {
//                    previewProperties.getProperty(LegendManager.getProperty(LegendProperty.LEGEND_PROPERTIES, itemIndex, LegendProperty.WIDTH)).setValue(event.x - relativeAnchorX - graphOriginX);
//                    previewProperties.getProperty(LegendManager.getProperty(LegendProperty.LEGEND_PROPERTIES, itemIndex, LegendProperty.HEIGHT)).setValue(event.x - relativeAnchorY - graphOriginY);
                    break;
                }
                // Bottom Right
                case 3: {
//                    previewProperties.getProperty(LegendManager.getProperty(LegendProperty.LEGEND_PROPERTIES, itemIndex, LegendProperty.WIDTH)).setValue(event.x - relativeAnchorX - graphOriginX);
//                    previewProperties.getProperty(LegendManager.getProperty(LegendProperty.LEGEND_PROPERTIES, itemIndex, LegendProperty.HEIGHT)).setValue(event.x - relativeAnchorY - graphOriginY);
                    break;
                }
            }

        }
        // translate
        else if (isSelected && isClickingInLegend(event.x, event.y)) {

            System.out.println("@Var: graphOriginX: " + graphOriginX);
            System.out.println("@Var: graphOriginY: " + graphOriginY);


            System.out.println("@Var: dragging relativeX: " + relativeX);
            System.out.println("@Var: dragging relativeY: " + relativeY);


            float newRealOriginX = event.x - relativeX;
            System.out.println("@Var: newRealOriginX: " + newRealOriginX);
            float newRealOriginY = event.y - relativeY;
            System.out.println("@Var: newRealOriginY: " + newRealOriginY);
            float newUserOriginX = event.x - graphOriginX - relativeX;
            System.out.println("@Var: newUserOriginX: " + newUserOriginX);
            float newUserOriginY = event.y - graphOriginY - relativeY;
            System.out.println("@Var: newUserOriginY: " + newUserOriginY);
            previewProperties.getProperty(LegendManager.getProperty(LegendProperty.LEGEND_PROPERTIES, itemIndex, LegendProperty.REAL_ORIGIN_X)).setValue(newRealOriginX);
            previewProperties.getProperty(LegendManager.getProperty(LegendProperty.LEGEND_PROPERTIES, itemIndex, LegendProperty.REAL_ORIGIN_Y)).setValue(newRealOriginY);


            previewProperties.getProperty(LegendManager.getProperty(LegendProperty.LEGEND_PROPERTIES, itemIndex, LegendProperty.USER_ORIGIN_X)).setValue(newUserOriginX);
            previewProperties.getProperty(LegendManager.getProperty(LegendProperty.LEGEND_PROPERTIES, itemIndex, LegendProperty.USER_ORIGIN_Y)).setValue(newUserOriginY);
        }

        event.setConsumed(true);
        System.out.println("@Var: +---------------------> END mouseDragged: ");
    }

    public void mouseReleasedEvent(PreviewMouseEvent event, PreviewProperties previewProperties, Workspace workspace) {
        System.out.printf("\n\n\n");
        System.out.println("@Var: mouseReleased: ");
//                            previewProperties.getProperty(LegendManager.getProperty(LegendProperty.LEGEND_PROPERTIES, itemIndex, LegendProperty.USER_ORIGIN_X)).setValue(event.x);
//                            previewProperties.getProperty(LegendManager.getProperty(LegendProperty.LEGEND_PROPERTIES, itemIndex, LegendProperty.USER_ORIGIN_Y)).setValue(event.y);
        event.setConsumed(true);
    }

    @Override
    public PreviewMouseListener[] getListeners() {


        return new PreviewMouseListener[]{
                    new PreviewMouseListener() {
                        public void mouseClicked(PreviewMouseEvent event, PreviewProperties previewProperties, Workspace workspace) {
                            mouseClickedEvent(event, previewProperties, workspace);
                        }

                        public void mousePressed(PreviewMouseEvent event, PreviewProperties previewProperties, Workspace workspace) {
                            mousePressedEvent(event, previewProperties, workspace);
                        }

                        public void mouseDragged(PreviewMouseEvent event, PreviewProperties previewProperties, Workspace workspace) {
                            mouseDraggedEvent(event, previewProperties, workspace);
                        }

                        public void mouseReleased(PreviewMouseEvent event, PreviewProperties previewProperties, Workspace workspace) {
                            mouseReleasedEvent(event, previewProperties, workspace);
                        }

                    }
                };
    }

    private Integer itemIndex;
    private float defaultMargin = 100f;
    private float graphOriginX = Float.MAX_VALUE;
    private float graphOriginY = Float.MAX_VALUE;
    private float graphWidth = 0;
    private float graphHeight = 0;
    // VARIABLES
    // IS DISPLAYING
    private Boolean isDisplayingLegend;
    // DIMENSIONS
    protected Integer width;
    protected Integer height;
    protected AffineTransform originTranslation;
    private float realOriginX;
    private float realOriginY;
    private float userOriginX;
    private float userOriginY;
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
    // is scaling legend
    private Boolean isScaling;
    private int[][] anchorLocations = null;
    // click
    private float relativeX;
    private float relativeY;
}
