package particles;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javafx.embed.swing.SwingFXUtils;
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
	
	private ArrayList<CubeParticle> particleList = new ArrayList<CubeParticle>();
	private int particleSize = 5;
	private double width;
	private double height;
	private final int NUM_PARTICLES = 4000;
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

		DraggableFxWorld draggableWorld = new DraggableFxWorld(this.scene, this.root);		
	}


	private void buildParticles () {
		for (int i = 0; i < NUM_PARTICLES; i++) {
			Color color = Color.color(Math.random(), Math.random(), Math.random());
			ParticleParameters p = new ParticleParameters(getRandPosition(), getRandPosition(), getRandPosition(), 50);
			CubeParticle cubeParticle = new CubeParticle(color, p);
			this.particleList.add(cubeParticle);
		}
	
		for (CubeParticle cubeParticle : particleList) {
			this.particleGroup.getChildren().addAll(cubeParticle.getCube().getBox());
		}
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
		
		this.particleList.forEach((particleCube) -> {
			if (particleCube.isDead()) {
				int x = (int) (imageWidth * Math.random());
				int y = (int) (imageHeight * Math.random());
				Color color = pr.getColor(x, y);
				
				int xPosition = -x + (imageWidth / 2);
				int yPosition = -y + (imageHeight / 2);
				
				ParticleParameters newParams = new ParticleParameters(xPosition, yPosition, 0, midiServer.getParticleParams().ttl);
				particleCube.setColor(color);
				particleCube.reset(newParams);
			}
			else {
				particleCube.update(midiServer.getParticleParams());
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
	
	
	//TODO: move to seperate class after geometry gets hammered out
	private class CubeParticle {
		
		private ParticleParameters particleParams = new ParticleParameters();
		private Color color;
		private Cube cube;
		
		public CubeParticle () {}
		
		public CubeParticle (Color c, ParticleParameters p) {
			this.color = c;
			this.particleParams = p;
			this.cube = new Cube (particleSize, particleSize, particleSize, color, color);
		}
		
		public void reset (ParticleParameters newParams) {
			this.particleParams = newParams;
			this.particleParams.ttl = (int) (newParams.ttl * Math.random());
		}
		
		public Cube getCube () {
			return this.cube;
		}
		
		public void update (ParticleParameters p) {
			this.particleParams.ttl--;
			this.particleParams.x += p.x;
			this.particleParams.y += p.y;
			this.particleParams.z += p.z;
			
			cube.translate(this.particleParams.x, this.particleParams.y, this.particleParams.z);
		}

		public void setColor (Color c) {
			cube.setColor(c);
		}
		
		public boolean isDead () {
			return this.particleParams.ttl <= 0;
		}
		
	}

}
