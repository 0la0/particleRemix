package particles;

import java.util.ArrayList;

import javafx.geometry.Point3D;

/**
 * Boids
 *
 */
public class SwarmService {
	
	private Point3D meanPosition = new Point3D(0, 0, 0);
	private Point3D meanVelocity = new Point3D(0, 0, 0);
	private ArrayList<Particle> particleList;

	public SwarmService () {}
	
	
	public void setParticleList (ArrayList<Particle> particleList) {
		this.particleList = particleList;
	}
	
	public void update (ArrayList<Particle> particleList) {
		this.meanPosition = particleList.stream()
				.map(particle -> particle.getPosition()) //try ::
				.reduce(new Point3D(0, 0, 0),
						(sum, currentPoint) -> sum.add(currentPoint))
				.multiply(1 / (particleList.size() * 1.0));
		
		this.meanVelocity = particleList.stream()
				.map(particle -> particle.getVelocity()) //try ::
				.reduce(new Point3D(0, 0, 0),
						(sum, currentPoint) -> sum.add(currentPoint))
				.multiply(1 / (particleList.size() * 1.0));
	}
	
	//Boids fly toward center of flock
	public Point3D getRuleOneVector (Particle particle) {
		return this.meanPosition
				.subtract(particle.getPosition())
				.multiply(0.05);
	}
	
	//Boids mind their neighbor's personal space
	public Point3D getRuleTwoVector (Particle particle) {
		Point3D distance = particleList.stream()
				.filter(p -> {
					return p.getPosition().distance(particle.getPosition()) < 10;
				})
				.map(Particle::getPosition)
				.reduce(new Point3D(0, 0, 0), 
						(sum, currentPoint) -> sum.subtract(currentPoint.subtract(particle.getPosition())))
				.multiply(2);
		return distance;
	}
	
	//Boids try to match the velocity of other boids
	public Point3D getRuleThreeVector (Particle particle) {
		return this.meanVelocity
				.subtract(particle.getVelocity())
				.multiply(0.05);
	}
	
}
