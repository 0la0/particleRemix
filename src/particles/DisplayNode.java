package particles;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.AmbientLight;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.SubScene;
import javafx.scene.paint.Color;
import javafx.scene.layout.Pane;


public class DisplayNode {

	private Group root = new Group();
	private Xform world = new Xform();
	private SubScene scene;
	private Xform particleGroup = new Xform();
	
	private double width;
	private double height;
	private ParticleDriver particleDriver;
	private MidiServer midiServer;
	private CameraPositionService camerPosition;
	
	
	public DisplayNode (int width, int height) {
		this.width = width;
		this.height = height;
		
		ParameterService parameterService = new ParameterService();
		this.particleDriver = new ParticleDriver(parameterService);
		this.midiServer = new MidiServer(parameterService);
		this.camerPosition = new CameraPositionService(parameterService);
		
		
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

	public void update (double elapsedTime, BufferedImage screenCapture) {
		this.particleDriver.update(elapsedTime, screenCapture);
		this.camerPosition.update(elapsedTime);
	}
	
	public Node getUiNode () {
		return this.scene;
	}
	
	//bind to parent for resizing
	public void bindSceneToParent (Pane parentPane) {
		this.scene.heightProperty().bind(parentPane.heightProperty());
		this.scene.widthProperty().bind(parentPane.widthProperty());
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
