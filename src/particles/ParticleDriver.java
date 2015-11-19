package particles;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.Point3D;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

public class ParticleDriver {

	private final int NUM_PARTICLES = 10000;
	private ArrayList<Particle> particleList = new ArrayList<Particle>();
	private MidiServer midiServer = new MidiServer();
	private WritableImage screenshot;
	
	public ParticleDriver () {
		//---populate particle list with particles---//
		for (int i = 0; i < NUM_PARTICLES; i++) {
			Color color = Color.color(Math.random(), Math.random(), Math.random());
			Point3D position = new Point3D(getRandPosition(), getRandPosition(), 0);
			Point3D velocity = midiServer.getCurrentVelocity();
			double ttl = midiServer.getTtlUpperBound();
			Particle particle = new Particle(position, velocity, color, ttl);
			this.particleList.add(particle);
		}
	}
	
	public void update (double elapsedTime, BufferedImage screenCapture) {
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
				Point3D position = new Point3D(xPosition, yPosition, 0);
				Point3D velocity = createNewVelocity(midiServer.getCurrentVelocity());
				//int ttlMultiplier = midiServer.getTtlUpperBound();
				double ttl = midiServer.getTtlUpperBound() * Math.random();
				particle.reset(position, velocity, color, ttl);
			}
			else {
				particle.update(
						elapsedTime, 
						midiServer.getCurrentVelocity(), 
						midiServer.getCurrentScale(),
						midiServer.getCurrentRotation());
			}
		});
	}
	
	public ArrayList<Particle> getParticleList () {
		return this.particleList;
	}
	
	private Point3D createNewVelocity (Point3D currentWind) {
		double vectorMultiplier = 0.5;
		double x = vectorMultiplier * this.getPosNeg() * Math.random();
		double y = vectorMultiplier * this.getPosNeg() * Math.random();
		double z = vectorMultiplier * this.getPosNeg() * Math.random();
		return new Point3D(x, y, z);
	}
	
	private int getRandPosition () {
		return (int) (100 * Math.random());
	}
	
	private int getPosNeg () {
		return (Math.random() < 0.5) ? 1 : -1;
	}
	
}