package particles;

import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.image.BufferedImage;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import javafx.scene.image.WritablePixelFormat;
import javafx.geometry.Point2D;

/**
 * Transparent javafx scene lifted and modified from:
 * https://assylias.wordpress.com/2013/12/08/383/
 */

public class TransparentFrame {

	private ParameterService parameterService;
	private WritableImage screenshot;
	private WritableImage previousScreenshot;
	private WritablePixelFormat<IntBuffer> format = WritablePixelFormat.getIntArgbInstance();
	
	public TransparentFrame (ParameterService parameterService) {
		this.parameterService = parameterService;
		
		Stage stage = new Stage();
		Label lbl = new Label("");
        VBox p = new VBox(lbl);
        
        final Delta dragDelta = new Delta();
        AtomicBoolean isResizing = new AtomicBoolean(false);
        
        p.setOnMousePressed(mouseEvent -> {
        	if ( getIsResize(mouseEvent.getX(), mouseEvent.getY(), stage.getWidth(), stage.getHeight()) ) {
    	    	isResizing.set(true);
                dragDelta.x = stage.getWidth() - mouseEvent.getX();
                dragDelta.y = stage.getHeight() - mouseEvent.getY();
            } else {
            	isResizing.set(false);
            	dragDelta.x = stage.getX() - mouseEvent.getScreenX();
        	    dragDelta.y = stage.getY() - mouseEvent.getScreenY();
            }
        });
        
        p.setOnMouseDragged(mouseEvent -> {
    	    if (isResizing.get()) {
    	    	stage.setWidth(mouseEvent.getX() + dragDelta.x);
                stage.setHeight(mouseEvent.getY() + dragDelta.y);
            } else {
                stage.setX(mouseEvent.getScreenX() + dragDelta.x);
        	    stage.setY(mouseEvent.getScreenY() + dragDelta.y);
            }
        });
        
        p.setBorder(new Border(new BorderStroke(Color.RED, BorderStrokeStyle.SOLID, null, null)));

        Scene scene = new Scene(p);
        stage.setScene(scene);

        p.setStyle("-fx-background-color: rgba(0, 0, 0, 0);");
        scene.setFill(null);
        stage.initStyle(StageStyle.TRANSPARENT);

        stage.setWidth(200);
        stage.setHeight(100);
        stage.show();
        
        //Take screenshot of scene area every n milliseconds
        Timeline timeline = new Timeline(new KeyFrame(
            Duration.millis(100),
            actionEvent -> {
            	int locationX = (int) stage.getX() + 1;
            	int locationY = (int) stage.getY() + 1;
            	int stageWidth = (int) stage.getWidth() - 2;
            	int stageHeight = (int) stage.getHeight() - 2;
        		
        		Rectangle targetArea = new Rectangle(locationX, locationY, stageWidth, stageHeight);
        		try {
            		setScreenCapture(new Robot().createScreenCapture(targetArea));
				} catch (Exception e) {
					e.printStackTrace();
				}
            })
        );
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
	}
	
	private void setScreenCapture (BufferedImage screenCapture) {
		this.previousScreenshot = this.screenshot;
		this.screenshot = SwingFXUtils.toFXImage(screenCapture, null);	
		
		if (this.parameterService.isMotionDetection()) {
			boolean widthIsEqual = this.screenshot.getWidth() == this.previousScreenshot.getWidth();
			boolean heightIsEqual = this.screenshot.getHeight() == this.previousScreenshot.getHeight();
			if (!widthIsEqual || !heightIsEqual) return;
			
			int width = (int) this.screenshot.getWidth();
			int height = (int) this.screenshot.getHeight();
			
			int totalPixels = width * height;
		
			int[] thisBuffer = new int[totalPixels];
			int[] previousBuffer = new int[totalPixels];
			
			PixelReader thisScreenshot = this.screenshot.getPixelReader();
			PixelReader previousScreenshot = this.previousScreenshot.getPixelReader();
			
			thisScreenshot.getPixels(0, 0, width, height, format, thisBuffer, 0, width);
			previousScreenshot.getPixels(0, 0, width, height, format, previousBuffer, 0, width);
			
			ArrayList<Point2D> pointList = new ArrayList<Point2D>();
			
			for (int i = 0; i < thisBuffer.length; i++) {
				double colorDistance = this.getColorDistance(thisBuffer[i], previousBuffer[i]);
				if (colorDistance > 10) {
					int x = i % width;
					int y = i / width;
					pointList.add(new Point2D(x, y));
				}
			}
			
			this.parameterService.setMotionPointList(pointList);
		}
	}
	
	private double getColorDistance (int thisImage, int previousImage) {
		int thisR = thisImage >>> 16 & 0xFF;
		int lastR = previousImage >>> 16 & 0xFF;
		
		int thisG = thisImage >>> 8 & 0xFF;
		int lastG = previousImage >>> 8 & 0xFF;
		
		int thisB = thisImage & 0xFF;
		int lastB = previousImage & 0xFF;
		
		double distanceR = Math.pow(thisR - lastR, 2);
		double distanceG = Math.pow(thisG - lastG, 2);
		double distanceB = Math.pow(thisB - lastB, 2);
		
		return Math.sqrt(distanceR + distanceG + distanceB);
	}
	
	public WritableImage getScreenshot () {
		return this.screenshot;
	}
	
	private boolean getIsResize (double mouseX, double mouseY, double stageWidth, double stageHeight) {
		boolean mouseInOnRight = (mouseX > stageWidth - 10) && (mouseX < stageWidth + 10);
		boolean mouseIsOnBottom = (mouseY > stageHeight - 10) && (mouseY < stageHeight + 10);
        return mouseInOnRight && mouseIsOnBottom;
	}
	
	private class Delta { double x, y; }
	
}
