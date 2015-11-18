package particles;


import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;

public class Init extends Application {
	
	private TransparentFrame transparentFrame;
    private long lastTime;
    private final double ONE_MILLION = 1000000.0;
	
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
				double elapsedTime = (now - lastTime) / ONE_MILLION;
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