package particles;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.PerspectiveCamera;
import javafx.scene.SubScene;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.SceneAntialiasing;

public class DisplayNode {

	private Group root = new Group();
	private Xform world = new Xform();
	private PerspectiveCamera camera = new PerspectiveCamera(true);
	private Xform cameraXform = new Xform();
	private Xform cameraXform2 = new Xform();
	private Xform cameraXform3 = new Xform();
	private double cameraDistance = 1000;

	private Xform particleGroup = new Xform();
	private ArrayList<CubeParticle> particleList = new ArrayList<CubeParticle>();
	
	private SubScene scene;
	private int particleSize = 5;
	
	private double mousePosX;
	private double mousePosY;
	private double mouseOldX;
	private double mouseOldY;
	private double mouseDeltaX;
	private double mouseDeltaY;
	
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
		this.buildCamera();
		this.buildParticles();

		this.scene = new SubScene(root, width, height, true, SceneAntialiasing.BALANCED);
		
		this.scene.setFill(Color.BLACK);
		this.scene.setCamera(camera);
		this.handleMouse();
		
	}

	private void buildCamera() {
		root.getChildren().add(cameraXform);
		cameraXform.getChildren().add(cameraXform2);
		cameraXform2.getChildren().add(cameraXform3);
		cameraXform3.getChildren().add(camera);
		cameraXform3.setRotateZ(180.0);

		camera.setNearClip(0.1);
		camera.setFarClip(10000.0);
		camera.setTranslateZ(-cameraDistance);
		//cameraXform.ry.setAngle(320.0);
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
				//ParticleParameters newParams = new ParticleParameters(xPosition, yPosition, 0, 50);
				
//				particleCube.translate(xPosition, yPosition, 0);
				particleCube.setColor(color);
				particleCube.reset(newParams);
			}
			else {
				particleCube.update(midiServer.getParticleParams());
				//particleCube.update(new ParticleParameters(1, 1, 0, 0));
			}
		});
		
		
	
		
	}
	
	private class CubeParticle {
		
		//private int ttlMultiplier = 50;
		//private Position position = new Position();
		private ParticleParameters particleParams = new ParticleParameters();
		private Color color;
		private Cube cube;
		//private int ttl;
		
		public CubeParticle () {}
		
		public CubeParticle (Color c, ParticleParameters p) {
			this.color = c;
			//this.position = p;
			this.particleParams = p;
			
			cube = new Cube (particleSize, particleSize, particleSize, color, color);
			//this.reset();
		}
		
		public void reset (ParticleParameters newParams) {
			this.particleParams = newParams;
			this.particleParams.ttl = (int) (newParams.ttl * Math.random());
			//this.ttl = (int) (ttlMultiplier * Math.random());
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
		
//		public void translate (int x, int y, int z) {
//			this.particleParams.x = x;
//			this.particleParams.y = y;
//			this.particleParams.z = z;
//			cube.translate(this.particleParams.x, this.particleParams.y, this.particleParams.z);
//		}
//		
		public void setColor (Color c) {
			cube.setColor(c);
		}
		
		public boolean isDead () {
			return this.particleParams.ttl <= 0;
		}
		
	}
	
//	private class Position {
//		public int x;
//		public int y;
//		public int z;
//		
//		public Position () {}
//		public Position (int x, int y, int z) {
//			this.x = x;
//			this.y = y;
//			this.z = z;
//		}
//	}
	
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
	
	/*
	 * 3D camera rotate on mouse drag
	 * from http://docs.oracle.com/javafx/8/3d_graphics/sampleapp.htm
	 */
	public void handleMouse() {
		
		scene.setOnMousePressed((MouseEvent e) -> {
			mousePosX = e.getSceneX();
			mousePosY = e.getSceneY();
			mouseOldX = e.getSceneX();
			mouseOldY = e.getSceneY();
		});
		
		scene.setOnMouseDragged((MouseEvent e) -> {
			mouseOldX = mousePosX;
			mouseOldY = mousePosY;
			mousePosX = e.getSceneX();
			mousePosY = e.getSceneY();
			mouseDeltaX = (mousePosX - mouseOldX);
			mouseDeltaY = (mousePosY - mouseOldY);

			double modifier = 1.0;
			double modifierFactor = 0.1;

			if (e.isControlDown()) {
				modifier = 0.1;
			}
			if (e.isShiftDown()) {
				modifier = 10.0;
			}
			if (e.isPrimaryButtonDown()) {
				cameraXform.ry.setAngle(cameraXform.ry.getAngle() - mouseDeltaX * modifierFactor * modifier * 2.0);  // +
				cameraXform.rx.setAngle(cameraXform.rx.getAngle() + mouseDeltaY * modifierFactor * modifier * 2.0);  // -
			} else if (e.isSecondaryButtonDown()) {
				double z = camera.getTranslateZ();
				double newZ = z + mouseDeltaX * modifierFactor * modifier;
				camera.setTranslateZ(newZ);
			} else if (e.isMiddleButtonDown()) {
				cameraXform2.t.setX(cameraXform2.t.getX() + mouseDeltaX * modifierFactor * modifier * 0.3);  // -
				cameraXform2.t.setY(cameraXform2.t.getY() + mouseDeltaY * modifierFactor * modifier * 0.3);  // -
			}
		});
		
	}

}
