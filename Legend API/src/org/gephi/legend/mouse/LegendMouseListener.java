package org.gephi.legend.mouse;

import org.gephi.legend.api.LegendController;
import org.gephi.legend.api.LegendModel;
import org.gephi.legend.api.LegendProperty;
import org.gephi.legend.spi.LegendItem;
import static org.gephi.legend.spi.LegendItem.LEGEND_MIN_HEIGHT;
import static org.gephi.legend.spi.LegendItem.LEGEND_MIN_WIDTH;
import static org.gephi.legend.spi.LegendItem.TRANSFORMATION_ANCHOR_SIZE;
import org.gephi.preview.api.Item;
import org.gephi.preview.api.PreviewMouseEvent;
import org.gephi.preview.api.PreviewProperties;
import org.gephi.preview.spi.PreviewMouseListener;
import org.gephi.project.api.Workspace;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author Eduardo Ramos<eduramiba@gmail.com>
 */
@ServiceProvider(service = PreviewMouseListener.class)
public class LegendMouseListener implements PreviewMouseListener {

    private int isClickingInAnchor(int pointX,
            int pointY,
            Item item,
            PreviewProperties previewProperties) {

        Integer itemIndex = item.getData(LegendItem.ITEM_INDEX);
        float realOriginX = previewProperties.getFloatValue(LegendModel.getProperty(LegendProperty.LEGEND_PROPERTIES, itemIndex, LegendProperty.USER_ORIGIN_X));
        float realOriginY = previewProperties.getFloatValue(LegendModel.getProperty(LegendProperty.LEGEND_PROPERTIES, itemIndex, LegendProperty.USER_ORIGIN_Y));
        int width = previewProperties.getIntValue(LegendModel.getProperty(LegendProperty.LEGEND_PROPERTIES, itemIndex, LegendProperty.WIDTH));
        int height = previewProperties.getIntValue(LegendModel.getProperty(LegendProperty.LEGEND_PROPERTIES, itemIndex, LegendProperty.HEIGHT));
        float[][] anchorLocations = {
            {
                -TRANSFORMATION_ANCHOR_SIZE / 2,
                -TRANSFORMATION_ANCHOR_SIZE / 2,
                TRANSFORMATION_ANCHOR_SIZE,
                TRANSFORMATION_ANCHOR_SIZE
            },
            {
                width - TRANSFORMATION_ANCHOR_SIZE / 2,
                -TRANSFORMATION_ANCHOR_SIZE / 2,
                TRANSFORMATION_ANCHOR_SIZE,
                TRANSFORMATION_ANCHOR_SIZE
            },
            {
                -TRANSFORMATION_ANCHOR_SIZE / 2,
                height - TRANSFORMATION_ANCHOR_SIZE / 2,
                TRANSFORMATION_ANCHOR_SIZE,
                TRANSFORMATION_ANCHOR_SIZE
            },
            {
                width - TRANSFORMATION_ANCHOR_SIZE / 2,
                height - TRANSFORMATION_ANCHOR_SIZE / 2,
                TRANSFORMATION_ANCHOR_SIZE,
                TRANSFORMATION_ANCHOR_SIZE
            }
        };

        pointX -= realOriginX;
        pointY -= realOriginY;

        for (int i = 0; i < anchorLocations.length; i++) {
            if ((pointX >= anchorLocations[i][0]
                    && pointX < (anchorLocations[i][0] + anchorLocations[i][2]))
                    && pointY >= anchorLocations[i][1]
                    && pointY < (anchorLocations[i][1] + anchorLocations[i][3])) {
                return i;
            }
        }
        return -1;
    }

    private boolean isClickingInLegend(int pointX, int pointY, Item item, PreviewProperties previewProperties) {
        Integer itemIndex = item.getData(LegendItem.ITEM_INDEX);
        float realOriginX = previewProperties.getFloatValue(LegendModel.getProperty(LegendProperty.LEGEND_PROPERTIES, itemIndex, LegendProperty.USER_ORIGIN_X));
        float realOriginY = previewProperties.getFloatValue(LegendModel.getProperty(LegendProperty.LEGEND_PROPERTIES, itemIndex, LegendProperty.USER_ORIGIN_Y));
        int width = previewProperties.getIntValue(LegendModel.getProperty(LegendProperty.LEGEND_PROPERTIES, itemIndex, LegendProperty.WIDTH));
        int height = previewProperties.getIntValue(LegendModel.getProperty(LegendProperty.LEGEND_PROPERTIES, itemIndex, LegendProperty.HEIGHT));
        if ((pointX >= realOriginX && pointX < (realOriginX + width))
                && (pointY >= realOriginY && pointY < (realOriginY + height))) {
            return true;
        }
        return false;
    }

    @Override
    public void mouseClicked(PreviewMouseEvent event, PreviewProperties previewProperties, Workspace workspace) {
        LegendModel legendModel = LegendController.getInstance().getLegendModel();
        boolean someItemStateChanged = false;
        for (Item item : legendModel.getLegendItems()) {
            Boolean isSelected = item.getData(LegendItem.IS_SELECTED);
            if (isClickingInLegend(event.x, event.y, item, previewProperties) || isClickingInAnchor(event.x, event.y, item, previewProperties) >= 0) {
                //Unselect all other items:
                for (Item otherItem : legendModel.getLegendItems()) {
                    otherItem.setData(LegendItem.IS_SELECTED, Boolean.FALSE);

                    System.out.println("@Var: item. renderer : " + otherItem.getData(LegendItem.RENDERER).toString());
                }
                item.setData(LegendItem.IS_SELECTED, Boolean.TRUE);

                //updating manager ui:
                LegendController.getInstance().selectItem(item);
                event.setConsumed(true);
                return;
            } else if (isSelected) {
                someItemStateChanged = someItemStateChanged || isSelected;
                item.setData(LegendItem.IS_SELECTED, Boolean.FALSE);
            }
        }
        if (someItemStateChanged) {
            event.setConsumed(true);
        }
    }

    @Override
    public void mousePressed(PreviewMouseEvent event, PreviewProperties previewProperties, Workspace workspace) {
        mouseClicked(event, previewProperties, workspace);//Update selected legend as if the press was a click.
        
        LegendModel legendModel = LegendController.getInstance().getLegendModel();
        for (Item item : legendModel.getLegendItems()) {
            Boolean isSelected = item.getData(LegendItem.IS_SELECTED);
            isSelected = isSelected || isClickingInLegend(event.x, event.y, item, previewProperties);
            if (!isSelected) {
                continue;
            }
            Integer itemIndex = item.getData(LegendItem.ITEM_INDEX);
            float realOriginX = previewProperties.getFloatValue(LegendModel.getProperty(LegendProperty.LEGEND_PROPERTIES, itemIndex, LegendProperty.USER_ORIGIN_X));
            float realOriginY = previewProperties.getFloatValue(LegendModel.getProperty(LegendProperty.LEGEND_PROPERTIES, itemIndex, LegendProperty.USER_ORIGIN_Y));
            int width = previewProperties.getIntValue(LegendModel.getProperty(LegendProperty.LEGEND_PROPERTIES, itemIndex, LegendProperty.WIDTH));
            int height = previewProperties.getIntValue(LegendModel.getProperty(LegendProperty.LEGEND_PROPERTIES, itemIndex, LegendProperty.HEIGHT));

            clickedAnchor = isClickingInAnchor(event.x, event.y, item, previewProperties);
            boolean isClickingInAnchor = clickedAnchor >= 0;
            if (isClickingInAnchor) {
                relativeX = event.x - realOriginX;
                relativeY = event.y - realOriginY;
                switch (clickedAnchor) {
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
            } else if (isClickingInLegend(event.x, event.y, item, previewProperties)) {
                relativeX = event.x - realOriginX;
                relativeY = event.y - realOriginY;

                item.setData(LegendItem.IS_BEING_TRANSFORMED, Boolean.TRUE);
                item.setData(LegendItem.CURRENT_TRANSFORMATION, TRANSFORMATION_TRANSLATE_OPERATION);

                event.setConsumed(true);
                return;
            } else {
                item.setData(LegendItem.IS_SELECTED, Boolean.FALSE);
                item.setData(LegendItem.IS_BEING_TRANSFORMED, Boolean.FALSE);
                relativeX = 0;
                relativeY = 0;
                LegendController.getInstance().selectItem(null);

                return;
            }
        }
    }

    @Override
    public void mouseDragged(PreviewMouseEvent event, PreviewProperties previewProperties, Workspace workspace) {
        LegendModel legendModel = LegendController.getInstance().getLegendModel();
        for (Item item : legendModel.getLegendItems()) {
            Boolean isSelected = item.getData(LegendItem.IS_SELECTED);
            if (!isSelected) {
                continue;
            }
            Integer itemIndex = item.getData(LegendItem.ITEM_INDEX);


            Boolean isBeingTransformed = item.getData(LegendItem.IS_BEING_TRANSFORMED);

            if (isBeingTransformed) {
                String currentTransformation = item.getData(LegendItem.CURRENT_TRANSFORMATION);

                // SCALING
                if (currentTransformation.equals(TRANSFORMATION_SCALE_OPERATION)) {

                    float realOriginX = previewProperties.getFloatValue(LegendModel.getProperty(LegendProperty.LEGEND_PROPERTIES, itemIndex, LegendProperty.USER_ORIGIN_X));
                    float realOriginY = previewProperties.getFloatValue(LegendModel.getProperty(LegendProperty.LEGEND_PROPERTIES, itemIndex, LegendProperty.USER_ORIGIN_Y));
                    int width = previewProperties.getIntValue(LegendModel.getProperty(LegendProperty.LEGEND_PROPERTIES, itemIndex, LegendProperty.WIDTH));
                    int height = previewProperties.getIntValue(LegendModel.getProperty(LegendProperty.LEGEND_PROPERTIES, itemIndex, LegendProperty.HEIGHT));

                    boolean isClickingInAnchor = clickedAnchor >= 0;
                    if (isClickingInAnchor) {
                        float newOriginX = realOriginX;
                        float newOriginY = realOriginY;
                        float newWidth = width;
                        float newHeight = height;

                        switch (clickedAnchor) {
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

                        // change for key event
                        boolean isCtrlKeyPressed = event.keyEvent != null && event.keyEvent.isControlDown();
                        if (isCtrlKeyPressed) {
                            float scaleWidth = newWidth / width;
                            float scaleHeight = newHeight / height;
                            float scale = Math.min(scaleWidth, scaleHeight);
                            newWidth = width * scale;
                            newHeight = height * scale;
                        }


                        if (newWidth >= LEGEND_MIN_WIDTH && newHeight >= LEGEND_MIN_HEIGHT) {
                            previewProperties.putValue(LegendModel.getProperty(LegendProperty.LEGEND_PROPERTIES, itemIndex, LegendProperty.USER_ORIGIN_X), newOriginX);
                            previewProperties.putValue(LegendModel.getProperty(LegendProperty.LEGEND_PROPERTIES, itemIndex, LegendProperty.USER_ORIGIN_Y), newOriginY);
                            previewProperties.putValue(LegendModel.getProperty(LegendProperty.LEGEND_PROPERTIES, itemIndex, LegendProperty.WIDTH), newWidth);
                            previewProperties.putValue(LegendModel.getProperty(LegendProperty.LEGEND_PROPERTIES, itemIndex, LegendProperty.HEIGHT), newHeight);
                        }
                        event.setConsumed(true);
                    }
                } else if (currentTransformation.equals(TRANSFORMATION_TRANSLATE_OPERATION)) {
                    float newOriginX = event.x - relativeX;
                    float newOriginY = event.y - relativeY;

                    previewProperties.putValue(LegendModel.getProperty(LegendProperty.LEGEND_PROPERTIES, itemIndex, LegendProperty.USER_ORIGIN_X), newOriginX);
                    previewProperties.putValue(LegendModel.getProperty(LegendProperty.LEGEND_PROPERTIES, itemIndex, LegendProperty.USER_ORIGIN_Y), newOriginY);
                    
                    event.setConsumed(true);
                }
            }
        }
    }

    @Override
    public void mouseReleased(PreviewMouseEvent event, PreviewProperties previewProperties, Workspace workspace) {
        LegendModel legendModel = LegendController.getInstance().getLegendModel();

        for (Item item : legendModel.getLegendItems()) {
            Boolean isSelected = item.getData(LegendItem.IS_SELECTED);
            if (!isSelected) {
                continue;
            }

            item.setData(LegendItem.IS_BEING_TRANSFORMED, Boolean.FALSE);
        }
        event.setConsumed(true);
    }
    private float relativeX;
    private float relativeY;
    private float relativeAnchorX;
    private float relativeAnchorY;
    private int clickedAnchor;
    private final String TRANSFORMATION_SCALE_OPERATION = "Scale operation";
    private final String TRANSFORMATION_TRANSLATE_OPERATION = "Translate operation";
}
