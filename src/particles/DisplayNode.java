package particles;

import javafx.scene.AmbientLight;
import javafx.scene.Group;
import javafx.scene.SubScene;
import javafx.scene.paint.Color;
import javafx.scene.image.WritableImage;


public class DisplayNode {

	private Group root = new Group();
	private Xform world = new Xform();
	private SubScene scene;
	private Xform particleGroup = new Xform();
	
	private double width;
	private double height;
	private ParticleDriver particleDriver;
	private CameraPositionService camerPosition;
	
	
	public DisplayNode (int width, int height, ParticleDriver particleDriver, CameraPositionService camerPosition) {
		this.width = width;
		this.height = height;
		this.particleDriver = particleDriver;
		this.camerPosition = camerPosition;
		
		
		root.getChildren().add(world);
		//this.scene = new SubScene(root, width, height, true, SceneAntialiasing.BALANCED);
		this.scene = new SubScene(root, width, height);
		this.scene.setFill(Color.BLACK);
		
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
		DraggableFxWorld draggableWorld = new DraggableFxWorld(this.scene, this.root, this.camerPosition.getCameraXform());
	}

	public void update (double elapsedTime, WritableImage screenshot) {
		this.particleDriver.update(elapsedTime, screenshot);
		this.camerPosition.update(elapsedTime);
	}
	
	public SubScene getSubscene () {
		return this.scene;
	}
		
	public void setFullscreen (boolean isFullscreen, double w, double h) {
		if (isFullscreen) {
			this.scene.setWidth(w);
			this.scene.setHeight(h);
		}
		else {
			this.scene.setWidth(width);
			this.scene.setHeight(height);
		}
	}
	

}
