/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.gephi.legend.plugin.renderers;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import org.apache.batik.svggen.*;
import org.apache.commons.codec.binary.*;
import org.gephi.legend.api.AbstractLegendItemRenderer;
import org.gephi.legend.api.LegendModel;
import org.gephi.legend.api.LegendProperty;
import org.gephi.legend.plugin.builders.ImageItemBuilder;
import org.gephi.legend.plugin.items.ImageItem;
import org.gephi.legend.plugin.properties.ImageProperty;
import org.gephi.legend.spi.LegendItem;
import org.gephi.preview.api.*;
import org.gephi.preview.spi.ItemBuilder;
import org.gephi.preview.spi.Renderer;
import org.openide.util.NbBundle;
import org.openide.util.lookup.ServiceProvider;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 *
 * @author edubecks
 */
@ServiceProvider(service = Renderer.class, position = 0)
public class ImageItemRenderer extends AbstractLegendItemRenderer {

    @Override
    public boolean isAnAvailableRenderer(Item item) {
        return item instanceof ImageItem;
    }

    @Override
    protected void renderToGraphics(Graphics2D graphics2D, AffineTransform origin, Integer width, Integer height) {
        try {
            graphics2D.setTransform(origin);
            if (imageFile.exists() && imageFile.isFile()) {
                BufferedImage before = ImageIO.read(imageFile);

                if (before.getWidth() == width && before.getHeight() == height) {
                    graphics2D.drawImage(before, 0, 0, null);
                } else {
                    if(useImageAspectRatio){
                        float aspectRatio = (float) before.getWidth() / (float) before.getHeight();
                        height = (int) (width / aspectRatio);
                    }
                    
                    // SCALING
                    BufferedImage after = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
                    double scaleHeight = height / ((double) before.getHeight());
                    double scaleWidth = width / ((double) before.getWidth());

                    AffineTransform scaleTransform = new AffineTransform();
                    scaleTransform.scale(scaleWidth, scaleHeight);
                    AffineTransformOp scaleOperation = new AffineTransformOp(scaleTransform, AffineTransformOp.TYPE_BILINEAR);
                    after = scaleOperation.filter(before, after);

//                    // EXPORTING TO SVG
                    if (graphics2D instanceof SVGGraphics2D) {
                        int x = (int) origin.getTranslateX();
                        int y = (int) origin.getTranslateY();

                        renderImageToSVGGraphics(graphics2D, after, x, y);

                    } else {
                        graphics2D.drawImage(after, 0, 0, null);
                    }

                }
            } else {
                String noImageMessage = NbBundle.getMessage(ImageItemRenderer.class, "ImageItemRenderer.no.image.message");
                
                float messageHeight = computeVerticalTextSpaceUsed(graphics2D, noImageMessage, titleFont, width);
                legendDrawText(graphics2D, noImageMessage, titleFont, Color.BLACK, 0, 0, width, (int)messageHeight, LegendItem.Alignment.CENTER);
            }
        } catch (Exception e) {
            Logger.getLogger(ImageItemRenderer.class.getName()).log(Level.WARNING, e.getMessage());
        }
    }

    @Override
    protected void readOwnPropertiesAndValues(Item item, PreviewProperties properties) {
        Integer itemIndex = item.getData(LegendItem.ITEM_INDEX);

        imageFile = properties.getValue(LegendModel.getProperty(ImageProperty.OWN_PROPERTIES, itemIndex, ImageProperty.IMAGE_URL));
        useImageAspectRatio = properties.getValue(LegendModel.getProperty(ImageProperty.OWN_PROPERTIES, itemIndex, ImageProperty.LOCK_ASPECT_RATIO));
        
        titleFont = properties.getFontValue(LegendModel.getProperty(LegendProperty.LEGEND_PROPERTIES, itemIndex, LegendProperty.TITLE_FONT));
    }

    @Override
    public String getDisplayName() {
        return NbBundle.getMessage(ImageItemRenderer.class, "ImageItemRenderer.name");
    }

    @Override
    public boolean needsItemBuilder(ItemBuilder itemBuilder, PreviewProperties properties) {
        return itemBuilder instanceof ImageItemBuilder;
    }

    /**
     * Renders an image into an SVGGraphics2D object
     *
     * @param graphics2D Graphics2D instance used to render legend
     * @param image image that will be renderer
     * @param x the x coordinate of the the position where the image will be rendered
     * @param y the y coordinate of the the position where the image will be rendered
     */
    public static void renderImageToSVGGraphics(Graphics2D graphics2D, BufferedImage image, int x, int y) {
        try {
            SVGGraphics2D svgGraphics2D = (SVGGraphics2D) graphics2D;
            SVGGeneratorContext svgGeneratorContext = svgGraphics2D.getGeneratorContext();

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(image, "png", baos);
            baos.flush();
            String encodedImage = Base64.encodeBase64String(baos.toByteArray());
            baos.close(); // should be inside a finally block
            Document svgDocument = svgGeneratorContext.getDOMFactory();

            Element imageBase64 = svgDocument.createElementNS("http://www.w3.org/2000/svg", "image");
            imageBase64.setAttribute("width", "" + image.getWidth());
            imageBase64.setAttribute("height", "" + image.getHeight());
//                        imageBase64.setAttribute("transform", "scale("+scaleWidth+","+scaleHeight+") translate("+origin.getTranslateX()+","+origin.getTranslateY()+")");
            imageBase64.setAttribute("transform", "scale(1,1) translate(" + x + "," + y + ")");
            imageBase64.setAttribute("xlink:href", DATA_PROTOCOL_PNG_PREFIX + encodedImage);

            svgDocument.getLastChild().appendChild(imageBase64);
//            svgDocument.getFirstChild().appendChild(imageBase64);
        } catch (Exception e) {
            System.out.println("@Var: e: " + e);
        }

    }
    // OWN PROPERTIES
    private File imageFile;
    private Font titleFont;
    private Boolean useImageAspectRatio;
    // encoding
    public static final String DATA_PROTOCOL_PNG_PREFIX = "data:image/png;base64,";
}
