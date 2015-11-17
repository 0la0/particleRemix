package particles;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.AmbientLight;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.SubScene;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import javafx.scene.SceneAntialiasing;


public class DisplayNode {

	private Group root = new Group();
	private Xform world = new Xform();
	private SubScene scene;
	private Xform particleGroup = new Xform();
	
	private ArrayList<Particle> particleList = new ArrayList<Particle>();
	private double width;
	private double height;
	private final int NUM_PARTICLES = 10000;
	private WritableImage screenshot;
	private MidiServer midiServer = new MidiServer();
	
	
	public DisplayNode (int width, int height) {
		this.width = width;
		this.height = height;
		
		//---UI SETUP---//
		root.getChildren().add(world);
		this.buildParticles();
		this.scene = new SubScene(root, width, height, true, SceneAntialiasing.BALANCED);
		this.scene.setFill(Color.BLACK);
		
		AmbientLight light = new AmbientLight();
	    light.setColor(Color.WHITE);
	    
	
	    Group lightGroup = new Group();
	    lightGroup.getChildren().add(light);
	    root.getChildren().add(lightGroup);

		DraggableFxWorld draggableWorld = new DraggableFxWorld(this.scene, this.root);		
	}


	private void buildParticles () {
		for (int i = 0; i < NUM_PARTICLES; i++) {
			Color color = Color.color(Math.random(), Math.random(), Math.random());
			//ParticleParameters p = new ParticleParameters(getRandPosition(), getRandPosition(), getRandPosition(), 50);
			Vector3d position = new Vector3d(getRandPosition(), getRandPosition(), 0);
			Vector3d velocity = midiServer.getParticleParams().getWind().clone();
			int ttl = midiServer.getParticleParams().getTTL();
			Particle particle = new Particle(position, velocity, color, ttl);
			//Particle cubeParticle = new Particle(color, );
			this.particleList.add(particle);
		}
	
		particleList.forEach(particle -> {
			particleGroup.getChildren().addAll(particle.getBox());
		});
		this.world.getChildren().addAll(this.particleGroup);
	}

	private int getRandPosition () {
		return (int) (100 * Math.random());
	}
	
	private int getPosNeg () {
		return (Math.random() < 0.5) ? 1 : -1;
	}

	public void update (float elapsedTime, BufferedImage screenCapture) {
		if (screenCapture == null) {
			return;
		}
		screenshot = SwingFXUtils.toFXImage(screenCapture, null);
	
		int imageWidth = (int) screenshot.getWidth();
		int imageHeight = (int) screenshot.getHeight();
		PixelReader pr = screenshot.getPixelReader();
		
		this.particleList.forEach((particle) -> {
			if (particle.isDead()) {
				int x = (int) (imageWidth * Math.random());
				int y = (int) (imageHeight * Math.random());
				Color color = pr.getColor(x, y);
				int xPosition = -x + (imageWidth / 2);
				int yPosition = -y + (imageHeight / 2);
				Vector3d position = new Vector3d(xPosition, yPosition, 0);
				Vector3d wind = midiServer.getParticleParams().getWind();
				int ttlMultiplier = midiServer.getParticleParams().getTTL();
				int ttl = (int) (ttlMultiplier * Math.random());
				particle.reset(position, wind, color, ttl);
			}
			else {
				particle.update(midiServer.getParticleParams().getWind());
			}
		});
	}
	
	public Node getUiNode () {
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
