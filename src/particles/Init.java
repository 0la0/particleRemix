package particles;


import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.scene.Scene;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;

public class Init extends Application {
	
	private TransparentFrame transparentFrame;
    private long lastTime;
    private final double ONE_MILLION = 1000000.0;
    private boolean isFullscreen = false;
	
	@Override
	public void start (Stage primaryStage) {
		int displayWidth = 500;
		int displayHeight = 400;
		
		Group root = new Group();
        Scene scene = new Scene(root, displayWidth, displayHeight, Color.LIGHTGREEN);
        
        ParameterService parameterService = new ParameterService();
        ParticleDriver particleDriver = new ParticleDriver(parameterService);
        CameraPositionService cameraPosition = new CameraPositionService(parameterService);
        
        DisplayNode displayNode = new DisplayNode(displayWidth, displayHeight, particleDriver, cameraPosition);
        
        //---CREATE TIMER AND START---//
		this.lastTime = System.nanoTime();
		AnimationTimer timer = new AnimationTimer() {
			public void handle(long now) {
				double elapsedTime = (now - lastTime) / ONE_MILLION;
				lastTime = now;
				displayNode.update(elapsedTime, transparentFrame.getScreenshot());
			}
		};
        
		BorderPane pane = new BorderPane();
		pane.setCenter(displayNode.getSubscene());
		scene.setRoot(pane);
		primaryStage.setScene(scene);
        primaryStage.show();
        
        displayNode.getSubscene().heightProperty().bind(pane.heightProperty());
        displayNode.getSubscene().widthProperty().bind(pane.widthProperty());
        
        
        transparentFrame = new TransparentFrame(parameterService);
        timer.start();
        
        scene.setOnKeyPressed((KeyEvent e) -> {
			if (e.getCode() == KeyCode.SPACE || e.getCode() == KeyCode.ESCAPE) {
				isFullscreen = !isFullscreen;
				primaryStage.setFullScreen(isFullscreen);
			}
		});
        
        primaryStage.setOnCloseRequest((WindowEvent e) -> {
        	Platform.exit();
        	System.exit(0);
        });
        
        //create midi server for controlling parameters
        //midi is an arbitrary choice, as any HMI could control the pramaters
        new MidiServer(parameterService);
	}
	

	public static void main (String[] args) {
		launch(args);
	}

}