package particles;

import java.util.ArrayList;

import javafx.geometry.Point3D;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

public class ImageDriver implements IDriver {

	private Xform particleGroup = new Xform();
	private final int NUM_PARTICLES = 5000;
	private ArrayList<Particle> particleList = new ArrayList<Particle>();
	private ParameterService parameterService;
	private String name;
	
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
		
		this.particleList.forEach(particle -> {
			particleGroup.getChildren().add(particle.getBox());
		});
	}
	
	@Override
	public void update(double elapsedTime, WritableImage screenshot) {
		if (screenshot == null) {
			return;
		}
		
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
					particle.reset(
							renderPoint.getRenderPosition(), 
							new Point3D(0, 0, 0),
							renderPoint.getColor(),
							renderPoint.getTTL());
				}
				
			}
			else {
				particle.update(
						elapsedTime, 
						new Point3D(0, 0, 0), 
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

}
