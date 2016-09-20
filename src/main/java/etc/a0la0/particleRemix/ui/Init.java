package etc.a0la0.particleRemix.ui;


import java.util.HashSet;
import java.util.Set;

import etc.a0la0.particleRemix.messaging.ParameterService;
import etc.a0la0.particleRemix.messaging.midi.MidiMessageHandler;
import etc.a0la0.particleRemix.messaging.midi.MidiServer;
import etc.a0la0.particleRemix.messaging.osc.OscMessageHandler;
import etc.a0la0.particleRemix.messaging.osc.OscServer;
import etc.a0la0.particleRemix.messaging.websocket.WebSocketMessageHandler;
import etc.a0la0.particleRemix.messaging.websocket.WsServer;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;

public class Init extends Application {
	
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

        DriverManager.Driver particleDriver = new ParticleDriver(parameterService, "particleDriver");
		DriverManager.Driver imageDriver = new ImageDriver(parameterService, "imageDriver");
        Set<DriverManager.Driver> driverSet = new HashSet<>();
        driverSet.add(particleDriver);
        driverSet.add(imageDriver);
        DriverManager driverManager = new DriverManager(driverSet);
        driverManager.setActiveDriverByName("particleDriver");

        CameraPositionService cameraPosition = new CameraPositionService(parameterService);
        DisplayFrame displayFrame = new DisplayFrame(displayWidth, displayHeight, driverManager, cameraPosition);

        TransparentFrame transparentFrame = new TransparentFrame(parameterService);

        //---CREATE TIMER AND START---//
		this.lastTime = System.nanoTime();
		AnimationTimer timer = new AnimationTimer() {
			public void handle(long now) {
				double elapsedTime = (now - lastTime) / ONE_MILLION;
				lastTime = now;
				displayFrame.update(elapsedTime, transparentFrame.getScreenshot());
			}
		};

		BorderPane pane = new BorderPane();
		pane.setCenter(displayFrame.getSubScene());
		scene.setRoot(pane);
		primaryStage.setScene(scene);
        primaryStage.show();

        displayFrame.getSubScene().heightProperty().bind(pane.heightProperty());
        displayFrame.getSubScene().widthProperty().bind(pane.widthProperty());

        timer.start();

        scene.setOnKeyPressed(keyEvent -> {
			if (keyEvent.getCode() == KeyCode.SPACE || keyEvent.getCode() == KeyCode.ESCAPE) {
				isFullscreen = !isFullscreen;
				primaryStage.setFullScreen(isFullscreen);
			}
		});

        primaryStage.setOnCloseRequest(windowEvent -> {
        	Platform.exit();
        	System.exit(0);
        });
        
        //create midi server for controlling parameters
        //midi is an arbitrary choice, as any HMI could control the pramaters
//        MidiMessageHandler midiHandler = new MidiMessageHandler(parameterService, displayFrame);
//        new MidiServer(midiHandler);
        
//        WebSocketMessageHandler wsHandler = new WebSocketMessageHandler(parameterService, displayFrame);
//        new WsServer(wsHandler);

		OscMessageHandler oscHandler = new OscMessageHandler(parameterService, displayFrame);
		new OscServer(oscHandler);
	}
	
	public static void main (String[] args) {
		launch(args);
	}

}