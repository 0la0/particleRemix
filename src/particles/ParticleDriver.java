package particles;

import java.util.ArrayList;

import javafx.geometry.Point3D;
import javafx.scene.paint.Color;
import javafx.scene.image.WritableImage;

public class ParticleDriver implements IDriver {

	private Xform particleGroup = new Xform();
	private final int NUM_PARTICLES = 5000;
	private ArrayList<Particle> particleList = new ArrayList<Particle>();
	private ParameterService parameterService;
	private SwarmService swarmService;
	private String name;
	
	public ParticleDriver (ParameterService parameterService, String name) {
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
		this.swarmService = new SwarmService(parameterService, particleList);
		
		this.particleList.forEach(particle -> {
			particleGroup.getChildren().add(particle.getBox());
		});
	}
	
	@Override
	public void update (double elapsedTime, WritableImage screenshot) {
		if (screenshot == null) {
			return;
		}
		
		this.swarmService.update(elapsedTime, this.particleList);
		
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
							renderPoint.createNewVelocity(),
							renderPoint.getColor(),
							renderPoint.getTTL());
				}
				
			}
			else {
				Point3D velocity = parameterService.getVelocity()
						.add(getJitter())
						.multiply(elapsedTime / 100.0);
						
				
				if (parameterService.getGoalAttraction() > 0) {
					velocity = velocity
							.add(swarmService.moveTowardGoalState(particle));
							//.add(swarmService.getRuleOneVector(particle));
							//.add(swarmService.getRuleTwoVector(particle))
							//.add(swarmService.getRuleThreeVector(particle));
				}
				
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
