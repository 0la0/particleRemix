package particles;

import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.transform.Rotate;

public class Particle {

	private final int PARTICLE_SIZE = 1;
	private ParticleParameters particleParams = new ParticleParameters();
	
	private PhongMaterial material;
	private Box box;
	private Color difuseColor = null;
	private Rotate rx = new Rotate(0, Rotate.X_AXIS);
	private Rotate ry = new Rotate(0, Rotate.Y_AXIS);
	private Rotate rz = new Rotate(0, Rotate.Z_AXIS);
	
	
	public Particle (Color color, ParticleParameters p) {
		this.particleParams = p;
		this.box = new Box(PARTICLE_SIZE, PARTICLE_SIZE, PARTICLE_SIZE);
		this.box.getTransforms().addAll(rz, ry, rx);
		this.setColor(color);
	}
	
	public void reset (ParticleParameters newParams) {
		this.particleParams = newParams;
		this.particleParams.ttl = (int) (newParams.ttl * Math.random());
	}
	
	public Box getBox () {
		return this.box;
	}
	
	public void update (ParticleParameters p) {
		this.particleParams.ttl--;
		this.particleParams.x += p.x;
		this.particleParams.y += p.y;
		this.particleParams.z += p.z;
		
		this.box.setTranslateX(this.particleParams.x);
		this.box.setTranslateY(this.particleParams.y);
		this.box.setTranslateZ(this.particleParams.z);
	}

	public void setColor (Color color) {
		this.difuseColor = color;
		this.material = new PhongMaterial();
		this.material.setDiffuseColor(this.difuseColor);
		this.box.setMaterial(this.material);
	}
	
	public boolean isDead () {
		return this.particleParams.ttl <= 0;
	}
	
	public void setRotate (double x, double y, double z) {
		this.rx.setAngle(x);
		this.ry.setAngle(y);
		this.rz.setAngle(z);
	}
	
}
