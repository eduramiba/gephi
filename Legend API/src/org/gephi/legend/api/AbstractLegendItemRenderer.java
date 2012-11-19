/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.gephi.legend.api;

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
import org.gephi.legend.mouse.LegendMouseListener;
import org.gephi.legend.spi.LegendItem;
import org.gephi.legend.spi.LegendItem.Alignment;
import org.gephi.legend.spi.LegendItemRenderer;
import org.gephi.preview.api.*;
import org.gephi.preview.spi.MouseResponsiveRenderer;
import org.gephi.preview.spi.PreviewMouseListener;
import org.openide.util.Lookup;
import processing.core.PGraphicsJava2D;

/**
 *
 * @author edubecks
 */
public abstract class AbstractLegendItemRenderer implements LegendItemRenderer, MouseResponsiveRenderer {

    /**
     *
     * Function that actually renders the legend using the Graphics2D Object
     *
     * @param graphics2D Graphics2D instance used to render legend
     * @param origin transformation that contains the origin and level zoom of
     * the legend
     * @param width width of the legend to be rendered
     * @param height height of the legend to be rendered
     */
    protected abstract void renderToGraphics(Graphics2D graphics2D, AffineTransform origin, Integer width, Integer height);

    /**
     * Function that reads the custom properties values from the
     * PreviewProperties of the current PreviewModel
     *
     * @param item current Legend Item
     * @param properties PreviewProperties of the current PreviewModel
     */
    protected abstract void readOwnPropertiesAndValues(Item item, PreviewProperties properties);

    public abstract boolean isAnAvailableRenderer(Item item);

    private void readLocationProperties(Item item, PreviewProperties previewProperties) {
        if (item != null) {
            currentItemIndex = item.getData(LegendItem.ITEM_INDEX);


            // LEGEND DIMENSIONS
            currentWidth = previewProperties.getIntValue(LegendModel.getProperty(LegendProperty.LEGEND_PROPERTIES, currentItemIndex, LegendProperty.WIDTH));
            currentHeight = previewProperties.getIntValue(LegendModel.getProperty(LegendProperty.LEGEND_PROPERTIES, currentItemIndex, LegendProperty.HEIGHT));


            // GRAPH DIMENSIONS
            PreviewController previewController = Lookup.getDefault().lookup(PreviewController.class);
            PreviewModel previewModel = previewController.getModel();
            Dimension dimensions = previewModel.getDimensions();
            graphHeight = dimensions.height;
            graphWidth = dimensions.width;
            Point topLeftPosition = previewModel.getTopLeftPosition();
            graphOriginX = topLeftPosition.x;
            graphOriginY = topLeftPosition.y;


            // LEGEND POSITION
            currentRealOriginX = previewProperties.getFloatValue(LegendModel.getProperty(LegendProperty.LEGEND_PROPERTIES, currentItemIndex, LegendProperty.USER_ORIGIN_X));
            currentRealOriginY = previewProperties.getFloatValue(LegendModel.getProperty(LegendProperty.LEGEND_PROPERTIES, currentItemIndex, LegendProperty.USER_ORIGIN_Y));
        }
    }

    private void readLegendPropertiesAndValues(Item item, PreviewProperties previewProperties) {
        if (item != null) {
            currentIsSelected = item.getData(LegendItem.IS_SELECTED);
            currentIsBeingTransformed = item.getData(LegendItem.IS_BEING_TRANSFORMED);

            readLocationProperties(item, previewProperties);

            isDisplayingLegend = previewProperties.getBooleanValue(LegendModel.getProperty(LegendProperty.LEGEND_PROPERTIES, currentItemIndex, LegendProperty.IS_DISPLAYING));

            // BACKGROUND
            backgroundIsDisplaying = previewProperties.getBooleanValue(LegendModel.getProperty(LegendProperty.LEGEND_PROPERTIES, currentItemIndex, LegendProperty.BACKGROUND_IS_DISPLAYING));
            backgroundColor = previewProperties.getColorValue(LegendModel.getProperty(LegendProperty.LEGEND_PROPERTIES, currentItemIndex, LegendProperty.BACKGROUND_COLOR));
            borderIsDisplaying = previewProperties.getBooleanValue(LegendModel.getProperty(LegendProperty.LEGEND_PROPERTIES, currentItemIndex, LegendProperty.BORDER_IS_DISPLAYING));
            borderColor = previewProperties.getColorValue(LegendModel.getProperty(LegendProperty.LEGEND_PROPERTIES, currentItemIndex, LegendProperty.BORDER_COLOR));
            borderLineThick = previewProperties.getIntValue(LegendModel.getProperty(LegendProperty.LEGEND_PROPERTIES, currentItemIndex, LegendProperty.BORDER_LINE_THICK));

            // TITLE
            isDisplayingTitle = previewProperties.getBooleanValue(LegendModel.getProperty(LegendProperty.LEGEND_PROPERTIES, currentItemIndex, LegendProperty.TITLE_IS_DISPLAYING));
            titleFont = previewProperties.getFontValue(LegendModel.getProperty(LegendProperty.LEGEND_PROPERTIES, currentItemIndex, LegendProperty.TITLE_FONT));
            titleFontColor = previewProperties.getColorValue(LegendModel.getProperty(LegendProperty.LEGEND_PROPERTIES, currentItemIndex, LegendProperty.TITLE_FONT_COLOR));
            titleAlignment = (Alignment) previewProperties.getValue(LegendModel.getProperty(LegendProperty.LEGEND_PROPERTIES, currentItemIndex, LegendProperty.TITLE_ALIGNMENT));
            title = previewProperties.getStringValue(LegendModel.getProperty(LegendProperty.LEGEND_PROPERTIES, currentItemIndex, LegendProperty.TITLE));

            //DESCRIPTION
            isDisplayingDescription = previewProperties.getBooleanValue(LegendModel.getProperty(LegendProperty.LEGEND_PROPERTIES, currentItemIndex, LegendProperty.DESCRIPTION_IS_DISPLAYING));
            descriptionFont = previewProperties.getFontValue(LegendModel.getProperty(LegendProperty.LEGEND_PROPERTIES, currentItemIndex, LegendProperty.DESCRIPTION_FONT));
            descriptionFontColor = previewProperties.getColorValue(LegendModel.getProperty(LegendProperty.LEGEND_PROPERTIES, currentItemIndex, LegendProperty.DESCRIPTION_FONT_COLOR));
            descriptionAlignment = (Alignment) previewProperties.getValue(LegendModel.getProperty(LegendProperty.LEGEND_PROPERTIES, currentItemIndex, LegendProperty.DESCRIPTION_ALIGNMENT));
            description = previewProperties.getStringValue(LegendModel.getProperty(LegendProperty.LEGEND_PROPERTIES, currentItemIndex, LegendProperty.DESCRIPTION));
        }
    }

    private void renderSVG(SVGTarget target) {
        org.w3c.dom.Document document = target.getDocument();

        SVGGeneratorContext svgGeneratorContext = SVGGraphics2D.buildSVGGeneratorContext(document, new ImageHandlerBase64Encoder(), new DefaultExtensionHandler());
        svgGeneratorContext.setEmbeddedFontsOn(true);

        SVGGraphics2D graphics2D = new SVGGraphics2D(svgGeneratorContext, false);

        originTranslation = new AffineTransform();
        originTranslation.translate(currentRealOriginX, currentRealOriginY);
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



        originTranslation = new AffineTransform();
        // BUG dont know why 11
        originTranslation.translate(graphOriginX, graphOriginY - 11);
        pdfContentByte.transform(originTranslation);
        Graphics2D graphics2D = new PdfGraphics2D(pdfContentByte, graphWidth, graphHeight);
        originTranslation = new AffineTransform();

        originTranslation.translate(-graphOriginX, -graphOriginY);
        originTranslation.translate(currentRealOriginX, currentRealOriginY);
        render(graphics2D, originTranslation, currentWidth, currentHeight);
        graphics2D.dispose();
        pdfContentByte.restoreState();
    }

    private void renderProcessing(ProcessingTarget target) {

        Graphics2D graphics2D = ((PGraphicsJava2D) target.getGraphics()).g2;

        AffineTransform saveState = graphics2D.getTransform();
        originTranslation = new AffineTransform(saveState);
        originTranslation.translate(currentRealOriginX, currentRealOriginY);

//        if (currentIsBeingTransformed) {//Transformed rendering disabled
//            renderTransformed(graphics2D, originTranslation, currentWidth, currentHeight);
//            drawScaleAnchors(graphics2D, originTranslation, currentWidth, currentHeight);
//        } else {
//            render(graphics2D, originTranslation, currentWidth, currentHeight);
//        }

        render(graphics2D, originTranslation, currentWidth, currentHeight);
        graphics2D.setTransform(saveState);
    }

    private void renderTransformed(Graphics2D graphics2D, AffineTransform origin, Integer width, Integer height) {
        graphics2D.setTransform(origin);
        graphics2D.setColor(TRANSFORMATION_LEGEND_BORDER_COLOR);
        graphics2D.fillRect(0, 0, width, height);
        graphics2D.setColor(TRANSFORMATION_LEGEND_CENTER_COLOR);
        graphics2D.fillRect(TRANSFORMATION_ANCHOR_LINE_THICK,
                            TRANSFORMATION_ANCHOR_LINE_THICK,
                            width - 2 * TRANSFORMATION_ANCHOR_LINE_THICK,
                            height - 2 * TRANSFORMATION_ANCHOR_LINE_THICK);
        // centeredText
        graphics2D.setColor(TRANSFORMATION_LEGEND_BORDER_COLOR);
        if (width < 100) {
            graphics2D.setFont(TRANSFORMATION_LEGEND_FONT_SMALL);
        }
        else {
            graphics2D.setFont(TRANSFORMATION_LEGEND_FONT);
        }
        int draggedLegendLabelWidth = graphics2D.getFontMetrics().stringWidth(TRANSFORMATION_LEGEND_LABEL);
        graphics2D.drawString(TRANSFORMATION_LEGEND_LABEL, (width - draggedLegendLabelWidth) / 2, height / 2);

    }

    private void render(Graphics2D graphics2D, AffineTransform origin, Integer width, Integer height) {
        graphics2D.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        //BACKGROUND
        renderBackground(graphics2D, origin, width, height);

        // BORDER
        renderBorder(graphics2D, origin, width, height, borderLineThick, borderColor);

        // TITLE
        AffineTransform titleOrigin = new AffineTransform(origin);
        float titleHeight = 0;
        if (isDisplayingTitle && !title.isEmpty()) {
            titleHeight = computeVerticalTextSpaceUsed(graphics2D, title, titleFont, width);
            renderTitle(graphics2D, titleOrigin, width, (int) titleHeight);

        }

        float descriptionHeight = 0;
        if (isDisplayingDescription && !description.isEmpty()) {
            descriptionHeight = computeVerticalTextSpaceUsed(graphics2D, description, descriptionFont, width);
        }
        // LEGEND
        AffineTransform legendOrigin = new AffineTransform(origin);
        //adding space between elements
        legendOrigin.translate(0, titleHeight + MARGIN_BETWEEN_ELEMENTS);
        int legendWidth = width;
        int legendHeight = (Integer) (height - Math.round(titleHeight) - Math.round(descriptionHeight) - 2 * MARGIN_BETWEEN_ELEMENTS);


        // DESCRIPTION
        AffineTransform descriptionOrigin = new AffineTransform(origin);
        descriptionOrigin.translate(0, titleHeight + legendHeight + MARGIN_BETWEEN_ELEMENTS);
        if (isDisplayingDescription && !description.isEmpty()) {
            renderDescription(graphics2D, descriptionOrigin, width, (int) descriptionHeight);
        }


        // rendering legend
        renderToGraphics(graphics2D, legendOrigin, legendWidth, legendHeight);

        // is selected
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


        graphics2D.setTransform(origin);
        graphics2D.setColor(TRANSFORMATION_LEGEND_BORDER_COLOR);
        graphics2D.drawRect(0, 0, width, height);

        for (int i = 0; i < anchorLocations.length; i++) {
            graphics2D.setColor(TRANSFORMATION_ANCHOR_COLOR);
            graphics2D.fillRect((int) anchorLocations[i][0], (int) anchorLocations[i][1], (int) anchorLocations[i][2], (int) anchorLocations[i][3]);

            graphics2D.setColor(Color.WHITE);
            graphics2D.fillRect((int) anchorLocations[i][0] + TRANSFORMATION_ANCHOR_LINE_THICK,
                                (int) anchorLocations[i][1] + TRANSFORMATION_ANCHOR_LINE_THICK,
                                (int) anchorLocations[i][2] - 2 * TRANSFORMATION_ANCHOR_LINE_THICK,
                                (int) anchorLocations[i][3] - 2 * TRANSFORMATION_ANCHOR_LINE_THICK);
        }
    }

    private float renderDescription(Graphics2D graphics2D, AffineTransform origin, Integer width, Integer height) {
        graphics2D.setTransform(origin);
        return legendDrawText(graphics2D, description, descriptionFont, descriptionFontColor, 0, 0, width, height, descriptionAlignment);
    }

    private void renderBackground(Graphics2D graphics2D, AffineTransform origin, Integer width, Integer height) {
        if (backgroundIsDisplaying) {
            graphics2D.setTransform(origin);
            graphics2D.setColor(backgroundColor);
            graphics2D.fillRect(0, 0, width, height);
//            graphics2D.setColor(backgroundBorderColor);
//            for (int i = 1; i <= backgroundBorderLineThick; i++) {
//                graphics2D.drawRect(-i, -i, width + 2 * i, height + 2 * i);
//            }
        }
    }

    private void renderBorder(Graphics2D graphics2D,
                              AffineTransform origin,
                              Integer width,
                              Integer height,
                              Integer borderThick,
                              Color borderColor) {

        if (borderIsDisplaying) {

            graphics2D.setTransform(origin);
            graphics2D.setColor(borderColor);

            // border is external

            // top
            graphics2D.fillRect(-borderThick, -borderThick, width + 2 * borderThick, borderThick);
            // bottom
            graphics2D.fillRect(-borderThick, height, width + 2 * borderThick, borderThick);
            // left
            graphics2D.fillRect(-borderThick, -borderThick, borderThick, height + 2 * borderThick);
            // right
            graphics2D.fillRect(width, -borderThick, borderThick, height + 2 * borderThick);
        }

    }

    private float renderTitle(Graphics2D graphics2D, AffineTransform origin, Integer width, Integer height) {
        graphics2D.setTransform(origin);
        return legendDrawText(graphics2D, title, titleFont, titleFontColor, 0, 0, width, height, titleAlignment);
    }

    @Override
    public void preProcess(PreviewModel previewModel) {
    }

    @Override
    public void render(Item item, RenderTarget target, PreviewProperties properties) {

        if (item != null) {


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
//                System.out.println("@Var: y: "+y);
//                System.out.println("@Var: yText: " + (yText - y - layout.getAscent()));
//                System.out.println("@Var: height: " + height);
                if (yText - y -layout.getAscent() > height) {
                    break;
                }
//                    break;
                layout.draw(graphics2D, xText, yText);
            }
            descent = layout.getDescent();
            leading = layout.getLeading();
            yText += descent + leading;
        }
        return (float) Math.ceil(yText - y - leading);
    }

    /**
     * Function that display some text just like the regular
     * <code>drawString</code> function from
     * <code>Graphics2D</code>. It has an additional parameter Alignment to
     * define the alignment of the text.
     *
     * @param graphics2D
     * @param text
     * @param font
     * @param color
     * @param x
     * @param y
     * @param width
     * @param height
     * @param alignment
     * @return
     */
    protected float legendDrawText(Graphics2D graphics2D, String text, Font font, Color color, double x, double y, Integer width, Integer height, Alignment alignment) {
//        System.out.println("@Var: drawElementLabel: " + text);
        float spaceUsed = legendDrawText(graphics2D, text, font, color, x, y, width, height, alignment, true);
//        System.out.println("@Var: spaceUsed: " + spaceUsed);
        y = y + (height - spaceUsed) / 2;
        return legendDrawText(graphics2D, text, font, color, x, y, width, height, alignment, false);
    }

    /**
     * Using the width as a parameter, it computes the vertical space used by
     * some text even if it fits in multiple lines
     *
     * @param graphics2D
     * @param text string containing the text
     * @param font font used to display the text
     * @param width max width to be used by the text
     * @return
     */
    protected float computeVerticalTextSpaceUsed(Graphics2D graphics2D, String text, Font font, Integer width) {
        return legendDrawText(graphics2D, text, font, Color.BLACK, 0, 0, width, currentHeight, Alignment.LEFT, true);
    }

    @Override
    public boolean isRendererForitem(Item item, PreviewProperties properties) {
        LegendItemRenderer renderer = item.getData(LegendItem.RENDERER);
        return renderer != null && renderer.getClass().equals(getClass());
    }

    @Override
    public boolean needsPreviewMouseListener(PreviewMouseListener previewMouseListener) {
        return previewMouseListener instanceof LegendMouseListener;
    }

    @Override
    public String toString() {
        return getDisplayName();
    }

    private Integer currentItemIndex;
    private float graphOriginX = Float.MAX_VALUE;
    private float graphOriginY = Float.MAX_VALUE;
    private float graphWidth = 0;
    private float graphHeight = 0;
    private final int MARGIN_BETWEEN_ELEMENTS = 5;
    // VARIABLES
    // IS DISPLAYING
    private Boolean isDisplayingLegend;
    // BACKGROUND
    private boolean backgroundIsDisplaying;
    private Color backgroundColor;
    private Boolean borderIsDisplaying;
    private Color borderColor;
    private int borderLineThick;
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
    // TRANSFORMATION
    private Boolean currentIsSelected = Boolean.FALSE;
    private Boolean currentIsBeingTransformed;
    private final Color TRANSFORMATION_LEGEND_BORDER_COLOR = new Color(0.5f, 0.5f, 0.5f, 0.5f);
    private final Color TRANSFORMATION_LEGEND_CENTER_COLOR = new Color(1f, 1f, 1f, 0.5f);
    private final Font TRANSFORMATION_LEGEND_FONT = new Font(Font.SANS_SERIF, Font.BOLD, 24);
    private final Font TRANSFORMATION_LEGEND_FONT_SMALL = new Font(Font.SANS_SERIF, Font.BOLD, 12);
    private final String TRANSFORMATION_LEGEND_LABEL = "Legend";
    private final Color TRANSFORMATION_ANCHOR_COLOR = Color.LIGHT_GRAY;
    private final int TRANSFORMATION_ANCHOR_SIZE = 20;
    private final int TRANSFORMATION_ANCHOR_LINE_THICK = 3;
}
