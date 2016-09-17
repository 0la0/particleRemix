package etc.a0la0.particleRemix.ui;

import etc.a0la0.particleRemix.ui.util.DraggableFxWorld;
import etc.a0la0.particleRemix.ui.util.Xform;
import javafx.scene.AmbientLight;
import javafx.scene.Group;
import javafx.scene.SubScene;
import javafx.scene.paint.Color;
import javafx.scene.image.WritableImage;
import javafx.application.Platform;


public class DisplayFrame {

	private Group root = new Group();
	private Xform world = new Xform();
	private SubScene subScene;
	
	private double width;
	private double height;
	private CameraPositionService camerPosition;
	private DriverManager driverManager;
	private DraggableFxWorld draggableWorld;
	
	public DisplayFrame (int width, int height, DriverManager driverManager, CameraPositionService camerPosition) {
		this.width = width;
		this.height = height;
		this.driverManager = driverManager;
		this.camerPosition = camerPosition;
		
		root.getChildren().add(world);
		//this.scene = new SubScene(root, width, height, true, SceneAntialiasing.BALANCED);
		this.subScene = new SubScene(root, width, height);
		this.subScene.setFill(Color.BLACK);
		
		AmbientLight light = new AmbientLight();
	    light.setColor(Color.WHITE);
	    Group lightGroup = new Group();
	    lightGroup.getChildren().add(light);
	    root.getChildren().add(lightGroup);
	    
	    this.world.getChildren().addAll(driverManager.getActiveDriver().getParticleGroup());
	    
		//DraggableFxWorld creates a 3D draggable world given a scene
		this.draggableWorld = new DraggableFxWorld(this.subScene, this.root, this.camerPosition.getCameraXform());
	}

	public void update (double elapsedTime, WritableImage screenshot) {
		this.driverManager.getActiveDriver().update(elapsedTime, screenshot);
		this.camerPosition.update(elapsedTime);
	}
	
	public SubScene getSubScene () {
		return this.subScene;
	}
		
	public void setFullscreen (boolean isFullscreen, double w, double h) {
		if (isFullscreen) {
			this.subScene.setWidth(w);
			this.subScene.setHeight(h);
		}
		else {
			this.subScene.setWidth(width);
			this.subScene.setHeight(height);
		}
	}
	
	public void setActiveDriverByName (String activeDriverName) {
		Platform.runLater(() -> {
			world.getChildren().removeAll(driverManager.getActiveDriver().getParticleGroup());
			driverManager.setActiveDriverByName(activeDriverName);
			world.getChildren().addAll(driverManager.getActiveDriver().getParticleGroup());
		});
	}
	
	public void setCameraDistance (double cameraDistance) {
		Platform.runLater(() -> {
			draggableWorld.setCameraDistance(cameraDistance);
		});
	}
	
}
