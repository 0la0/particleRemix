package particles;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javafx.embed.swing.SwingFXUtils;
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
			Vector3d position = new Vector3d(getRandPosition(), getRandPosition(), 0);
			Vector3d velocity = midiServer.getCurrentVelocity();
			int ttl = midiServer.getTtlUpperBound();
			Particle particle = new Particle(position, velocity, color, ttl);
			this.particleList.add(particle);
		}
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
				Vector3d velocity = createNewVelocity(midiServer.getCurrentVelocity());
				int ttlMultiplier = midiServer.getTtlUpperBound();
				int ttl = (int) (ttlMultiplier * Math.random());
				particle.reset(position, velocity, color, ttl);
			}
			else {
				particle.update(midiServer.getCurrentVelocity());
			}
		});
	}
	
	public ArrayList<Particle> getParticleList () {
		return this.particleList;
	}
	
	private Vector3d createNewVelocity (Vector3d currentWind) {
		int vectorMultiplier = 10;
		double x = vectorMultiplier * this.getPosNeg() * Math.random();
		double y = vectorMultiplier * this.getPosNeg() * Math.random();
		double z = vectorMultiplier * this.getPosNeg() * Math.random();
		return new Vector3d(x, y, z);
	}
	
	private int getRandPosition () {
		return (int) (100 * Math.random());
	}
	
	private int getPosNeg () {
		return (Math.random() < 0.5) ? 1 : -1;
	}
	
}
