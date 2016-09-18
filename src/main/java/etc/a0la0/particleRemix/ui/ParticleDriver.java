package etc.a0la0.particleRemix.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import etc.a0la0.particleRemix.messaging.ParameterService;
import etc.a0la0.particleRemix.ui.util.Xform;
import javafx.geometry.Point3D;
import javafx.scene.paint.Color;
import javafx.scene.image.WritableImage;

public class ParticleDriver implements DriverManager.Driver {

	private Xform particleGroup = new Xform();
	private final int NUM_PARTICLES = 5000;
	private List<Particle> particleList;
	private ParameterService parameterService;
	private SwarmService swarmService;
	private String name;
	
	public ParticleDriver (ParameterService parameterService, String name) {
		this.parameterService = parameterService;
		this.name = name;

		particleList = IntStream.range(0, NUM_PARTICLES)
				.mapToObj(index -> {
					Color color = Color.color(Math.random(), Math.random(), Math.random());
					Point3D position = new Point3D(getRandPosition(), getRandPosition(), 0);
					Point3D velocity = parameterService.getVelocity();
					double ttl = parameterService.getTtlUpperBound();
					return new Particle(position, velocity, color, ttl);
				})
				.collect(Collectors.toList());

		swarmService = new SwarmService(parameterService, particleList);
		
		particleList.forEach(particle ->
			particleGroup.getChildren().add(particle.getBox())
		);
	}
	
	@Override
	public void update (double elapsedTime, WritableImage screenshot) {
		if (screenshot == null) {
			return;
		}
		
		swarmService.update(elapsedTime, particleList);
		
		particleList.forEach((particle) -> {
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
		return particleGroup;
	}
	
	@Override
	public String getName () {
		return name;
	}
	
	private int getRandPosition () {
		return (int) (100 * Math.random());
	}
	
	private double getPosNeg () {
		return Math.random() < 0.5 ? -1 : 1;
	}
	
	private Point3D getJitter () {
		double jitterFactor = 0.01;
		double jitterX = getPosNeg() * jitterFactor * Math.random();
		double jitterY = getPosNeg() * jitterFactor * Math.random();
		double jitterZ = getPosNeg() * jitterFactor * Math.random();
		return new Point3D(jitterX, jitterY, jitterZ);
	}
	
}
