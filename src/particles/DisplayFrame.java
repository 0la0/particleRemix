package particles;

import javafx.scene.AmbientLight;
import javafx.scene.Group;
import javafx.scene.SubScene;
import javafx.scene.paint.Color;
import javafx.scene.image.WritableImage;


public class DisplayFrame {

	private Group root = new Group();
	private Xform world = new Xform();
	private SubScene subScene;
	private Xform particleGroup = new Xform();
	
	private double width;
	private double height;
	private ParticleDriver particleDriver;
	private CameraPositionService camerPosition;
	
	public DisplayFrame (int width, int height, ParticleDriver particleDriver, CameraPositionService camerPosition) {
		this.width = width;
		this.height = height;
		this.particleDriver = particleDriver;
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

	    this.particleDriver.getParticleList().forEach(particle -> {
			particleGroup.getChildren().add(particle.getBox());
		});
		this.world.getChildren().addAll(this.particleGroup);
	    
		//DraggableFxWorld creates a 3D draggable world given a scene
		new DraggableFxWorld(this.subScene, this.root, this.camerPosition.getCameraXform());
	}

	public void update (double elapsedTime, WritableImage screenshot) {
		this.particleDriver.update(elapsedTime, screenshot);
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
	
}
