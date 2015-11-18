package particles;

import javafx.geometry.Point3D;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.transform.Rotate;


public class Particle {

	private final int PARTICLE_SIZE = 1;
	private Point3D position;
	private Point3D velocity;
	
	private int ttl;
	
	private PhongMaterial material;
	private Box box;
	private Color difuseColor = null;
	private Rotate rx = new Rotate(0, Rotate.X_AXIS);
	private Rotate ry = new Rotate(0, Rotate.Y_AXIS);
	private Rotate rz = new Rotate(0, Rotate.Z_AXIS);
	
	
	public Particle (Point3D position, Point3D velocity, Color color, int ttl) {
		this.box = new Box(PARTICLE_SIZE, PARTICLE_SIZE, PARTICLE_SIZE);
		this.box.getTransforms().addAll(rz, ry, rx);
		this.reset(position, velocity, color, ttl);
	}
	
	public void reset (Point3D position, Point3D velocity, Color color, int ttl) {
		this.position = position;
		this.velocity = velocity;
		this.ttl = ttl;
		this.setColor(color);
	}
	
	public Box getBox () {
		return this.box;
	}
	
	public void update (double elapsedTime, Point3D wind) {
		this.ttl--;

		this.velocity = velocity.add( wind.multiply(elapsedTime) );		
		this.position = this.position.add(this.velocity);
		
		this.box.setTranslateX(this.position.getX());
		this.box.setTranslateY(this.position.getY());
		this.box.setTranslateZ(this.position.getZ());
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
