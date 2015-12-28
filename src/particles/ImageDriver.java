package particles;

import java.util.ArrayList;

import javafx.geometry.Point3D;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import javafx.scene.shape.TriangleMesh;
import javafx.scene.shape.MeshView;
import javafx.scene.Group;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.DrawMode;

public class ImageDriver implements IDriver {

	//private Xform parentGroup = new Xform();
	private Xform particleGroup = new Xform();
	//private Xform meshGroup = new Xform();
	private final int NUM_PARTICLES = 5000;
	private ArrayList<Particle> particleList = new ArrayList<Particle>();
	private ParameterService parameterService;
	private String name;
	private final double RENDER_MULTIPLIER = 50;
	
	private MeshView meshView;
	
	private final int MATERIAL_Z_INDEX = 0;
	
	
	
	public ImageDriver (ParameterService parameterService, String name) {
		this.parameterService = parameterService;
		this.name = name;
		
		//---populate particle list with particles---//
		for (int i = 0; i < NUM_PARTICLES; i++) {
			Color color = Color.color(Math.random(), Math.random(), Math.random());
			Point3D position = new Point3D(getRandPosition(), getRandPosition(), 0);
			Point3D velocity = parameterService.getVelocity();
			double ttl = parameterService.getTtlUpperBound();
			Particle particle = new Particle(position, velocity, color, ttl);
			this.particleList.add(particle);
		}
		
		
		
		this.meshView = this.createMesh();
		//this.meshView.setTranslateZ(1000);
	    Group plane = new Group(this.meshView);	    
	    this.particleGroup.getChildren().add(plane);
	    
	    
	    
	    this.particleList.forEach(particle -> {
			particleGroup.getChildren().add(particle.getBox());
		});
	    //this.meshGroup.getChildren().add(plane);
	    
	    //this.meshGroup.setTranslateX(1000);
	    
//	    this.parentGroup.getChildren().addAll(this.particleGroup.getChildren());
//	    this.parentGroup.getChildren().addAll(this.meshGroup.getChildren());
	}
	
	private MeshView createMesh () {
		
		float[] points = {
	            -1, 1, MATERIAL_Z_INDEX,
	            -1, -1, MATERIAL_Z_INDEX,
	            1, 1, MATERIAL_Z_INDEX,
	            1, -1, MATERIAL_Z_INDEX
	    	};
		
		float[] texCoords = {
				1, 0,
				1, 1,
				0, 0,
				0, 1
			};
		
	    int[] faces = {
	    	    2, 2, 1, 1, 0, 0,
	    	    2, 2, 3, 3, 1, 1
	    	};
	    
	    TriangleMesh triangleMesh = new TriangleMesh();
	    triangleMesh.getPoints().addAll(points);
	    triangleMesh.getTexCoords().addAll(texCoords);
	    triangleMesh.getFaces().addAll(faces);
	    
	    return new MeshView(triangleMesh);
	}
	
	@Override
	public void update(double elapsedTime, WritableImage screenshot) {
		if (screenshot == null) {
			return;
		}
		
		this.meshView.setMaterial(new PhongMaterial(Color.WHITE, screenshot, null, null, null));
		
		double screenshotRatio = screenshot.getWidth() / screenshot.getHeight();
		double width = RENDER_MULTIPLIER * screenshotRatio;
		double height = RENDER_MULTIPLIER;
		
		this.meshView.setScaleX(width);
		this.meshView.setScaleY(height);
		
		this.particleList.forEach((particle) -> {
			if (particle.isDead()) {
				RenderPoint renderPoint = new RenderPoint(screenshot, parameterService);
				
				if (renderPoint.hasNothingToRender()) {
					particle.reset(
							new Point3D(2000, 2000, 2000),
							new Point3D(0, 0, 0),
							Color.BLACK, 500);
				}
				else {
					Point3D renderPosition = renderPoint.getRenderPosition();
					
					double posX = RENDER_MULTIPLIER * screenshotRatio * renderPosition.getX() / (screenshot.getWidth() / 2.0);
					double posY = RENDER_MULTIPLIER * renderPosition.getY() / (screenshot.getHeight() / 2.0);
					
					Point3D adjustedRenderPosition = new Point3D(posX, posY, renderPosition.getZ());
					
					particle.setPivotX(-0.5);
					particle.setPivotY(-0.5);
					
					particle.reset(
							adjustedRenderPosition, 
							new Point3D(0, 0, 0),
							renderPoint.getColor(),
							renderPoint.getTTL());
				}
				
			}
			else {
//				Point3D velocity = parameterService.getVelocity()
//						.add(getJitter())
//						.multiply(elapsedTime / 100.0);
				
				Point3D velocity = new Point3D(0, 0, 0);
				
				particle.update(
						elapsedTime, 
						velocity, 
						parameterService.getScale(),
						parameterService.getRotate());
			}
		});
	}

	@Override
	public Xform getParticleGroup () {
		return this.particleGroup;
	}
	
	@Override
	public String getName () {
		return this.name;
	}
	
	private int getRandPosition () {
		return (int) (100 * Math.random());
	}
	
	private double getPosNeg () {
		return Math.random() < 0.5 ? -1 : 1;
	}
	
	private Point3D getJitter () {
		double jitterFactor = 0.01;
		double jitterX = this.getPosNeg() * jitterFactor * Math.random();
		double jitterY = this.getPosNeg() * jitterFactor * Math.random();
		double jitterZ = this.getPosNeg() * jitterFactor * Math.random();
		return new Point3D(jitterX, jitterY, jitterZ);
	}

}
