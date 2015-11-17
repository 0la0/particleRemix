package particles;

import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.transform.Rotate;

public class Particle {

	private final int PARTICLE_SIZE = 1;
	private Vector3d position = new Vector3d();
	private Vector3d velocity = new Vector3d();
	
	private int ttl;
	
	private PhongMaterial material;
	private Box box;
	private Color difuseColor = null;
	private Rotate rx = new Rotate(0, Rotate.X_AXIS);
	private Rotate ry = new Rotate(0, Rotate.Y_AXIS);
	private Rotate rz = new Rotate(0, Rotate.Z_AXIS);
	
	
	public Particle (Vector3d position, Vector3d velocity, Color color, int ttl) {
		this.box = new Box(PARTICLE_SIZE, PARTICLE_SIZE, PARTICLE_SIZE);
		this.box.getTransforms().addAll(rz, ry, rx);
		this.reset(position, velocity, color, ttl);
	}
	
//	public void reset (ParticleParameters newParams) {
//		//this.particleParams = newParams;
//		this.position.x = newParams.getWind().x;
//		this.position.y = newParams.getWind().y;
//		this.position.z = newParams.getWind().z;
//		
//		this.ttl = (int) (newParams.ttl * Math.random());
//	}
	
	public void reset (Vector3d position, Vector3d velocity, Color color, int ttl) {
		this.position = position.clone();
		this.velocity = velocity.clone();
		this.ttl = ttl;
		this.setColor(color);
	}
	
	public Box getBox () {
		return this.box;
	}
	
	public void update (Vector3d wind) {
		this.ttl--;
		this.position.x += wind.x;
		this.position.y += wind.y;
		this.position.z += wind.z;
		
		this.box.setTranslateX(this.position.x);
		this.box.setTranslateY(this.position.y);
		this.box.setTranslateZ(this.position.z);
	}

	public void setColor (Color color) {
		this.difuseColor = color;
		this.material = new PhongMaterial();
		this.material.setDiffuseColor(this.difuseColor);
		this.box.setMaterial(this.material);
	}
	
	public boolean isDead () {
		return this.ttl <= 0;
	}
	
	public void setRotate (double x, double y, double z) {
		this.rx.setAngle(x);
		this.ry.setAngle(y);
		this.rz.setAngle(z);
	}
	
}
