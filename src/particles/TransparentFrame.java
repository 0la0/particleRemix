package particles;

import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.image.BufferedImage;
import java.util.concurrent.atomic.AtomicBoolean;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

/**
 * Transparent javafx scene lifted and modified from:
 * https://assylias.wordpress.com/2013/12/08/383/
 */

public class TransparentFrame {

	private BufferedImage screenCapture;
	
	public TransparentFrame () {
		
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
            Duration.millis(200),
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
		this.screenCapture = screenCapture;
	}
	
	public BufferedImage getScreenCapture () {
		return this.screenCapture;
	}
	
	private boolean getIsResize (double mouseX, double mouseY, double stageWidth, double stageHeight) {
		boolean mouseInOnRight = (mouseX > stageWidth - 10) && (mouseX < stageWidth + 10);
		boolean mouseIsOnBottom = (mouseY > stageHeight - 10) && (mouseY < stageHeight + 10);
        return mouseInOnRight && mouseIsOnBottom;
	}
	
	private class Delta { double x, y; }
	
}
