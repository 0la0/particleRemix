package etc.a0la0.particleRemix.ui;

import java.util.ArrayList;
import java.util.List;

import etc.a0la0.particleRemix.messaging.ParameterService;
import javafx.geometry.Point3D;

/**
 * Boids
 *
 */
public class SwarmService {
	
	private ParameterService parameterService;
	private Point3D meanPosition = new Point3D(0, 0, 0);
	private Point3D meanVelocity = new Point3D(0, 0, 0);
	private List<Particle> particleList;
	private Point3D goalState = new Point3D(0, 0, 0);
	private final int GOAL_DISTANCE = 200;
	private double totalTime = 0;

	public SwarmService (ParameterService parameterService, List<Particle> particleList) {
		this.parameterService = parameterService;
		this.particleList = particleList;
	}
	
	public void update (double elapsedTime, List<Particle> particleList) {
		this.totalTime += elapsedTime / 1000.0;
		double x = GOAL_DISTANCE * Math.sin(totalTime / 2.0);
		double y = GOAL_DISTANCE * Math.sin(totalTime / 3.0);
		double z = GOAL_DISTANCE * Math.cos(totalTime / 4.0);
		this.goalState = new Point3D(x, y, z);
		
		this.meanPosition = particleList.stream()
				.map(Particle::getPosition)
				.reduce(new Point3D(0, 0, 0), Point3D::add)
						//(sum, currentPoint) -> sum.add(currentPoint))
				.multiply(1 / (particleList.size() * 1.0));
		
		this.meanVelocity = particleList.stream()
				.map(Particle::getVelocity)
				.reduce(new Point3D(0, 0, 0), Point3D::add)
						//(sum, currentPoint) -> sum.add(currentPoint))
				.multiply(1 / (particleList.size() * 1.0));
	}
	
	//Boids fly toward center of flock
	public Point3D getRuleOneVector (Particle particle) {
		return this.meanPosition
				.subtract(particle.getPosition())
				.multiply(0.001);
	}
	
	//Boids mind their neighbor's personal space
	public Point3D getRuleTwoVector (Particle particle) {
		Point3D distance = particleList.stream()
				.filter(p -> p.getPosition().distance(particle.getPosition()) < 10)
				.map(Particle::getPosition)
				.reduce(new Point3D(0, 0, 0), 
						(sum, currentPoint) -> sum.subtract(currentPoint.subtract(particle.getPosition())))
				.multiply(0.01);
		return distance;
	}
	
	//Boids try to match the velocity of other boids
	public Point3D getRuleThreeVector (Particle particle) {
		return this.meanVelocity
				.subtract(particle.getVelocity())
				.multiply(0.001);
	}
	
	public Point3D moveTowardGoalState (Particle particle) {
		return this.goalState
				.subtract(particle.getPosition())
				.multiply(parameterService.getGoalAttraction());
	}
	
}
