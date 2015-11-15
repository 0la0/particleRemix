package particles;


import javafx.animation.*;
import javafx.application.Application;
import javafx.stage.*;
import javafx.scene.Scene;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;

public class InitFx extends Application {
	
	private TransparentFrame transparentFrame;
    private long lastTime;
	
	@Override
	public void start (Stage primaryStage) {
		int displayWidth = 500;
		int displayHeight = 400;
		
		Group root = new Group();
        Scene scene = new Scene(root, displayWidth, displayHeight, Color.LIGHTGREEN);
        
        DisplayNode displayNode = new DisplayNode(displayWidth, displayHeight);
        
        //---CREATE TIMER AND START---//
		this.lastTime = System.nanoTime();
		AnimationTimer timer = new AnimationTimer() {
			public void handle(long now) {
				float elapsedTime = (float) ((now - lastTime) / 1000000.0);
				lastTime = now;
				displayNode.update(elapsedTime, transparentFrame.getScreenCapture());
			}
		};
        
		BorderPane pane = new BorderPane();
		pane.setCenter(displayNode.getUiNode());
        scene.setRoot(pane);
		primaryStage.setScene(scene);
        primaryStage.show();
        
        transparentFrame = new TransparentFrame();
        timer.start();
	}
	

	public static void main (String[] args) {
		launch(args);
	}

}