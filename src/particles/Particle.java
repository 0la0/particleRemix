package particles;

import javafx.geometry.Point3D;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Scale;


public class Particle {

	private final int PARTICLE_SIZE = 1;
	private Point3D position;
	private Point3D velocity;
	
	private double ttl;
	
	private PhongMaterial material;
	private Box box;
	private Color difuseColor = null;
	
	private Rotate rx = new Rotate(0, Rotate.X_AXIS);
	private Rotate ry = new Rotate(0, Rotate.Y_AXIS);
	private Rotate rz = new Rotate(0, Rotate.Z_AXIS);
	private Scale sx = new Scale();
	private Scale sy = new Scale();
	private Scale sz = new Scale();
	
	
	public Particle (Point3D position, Point3D velocity, Color color, double ttl) {
		this.box = new Box(PARTICLE_SIZE, PARTICLE_SIZE, PARTICLE_SIZE);
		this.box.getTransforms().addAll(rz, ry, rx, sx, sy, sz);
		this.reset(position, velocity, color, ttl);
	}
	
	public void reset (Point3D position, Point3D velocity, Color color, double ttl) {
		this.position = position;
		this.velocity = velocity;
		this.ttl = ttl;
		this.setColor(color);
	}
	
	public Box getBox () {
		return this.box;
	}
	
	public void update (double elapsedTime, Point3D wind, Point3D scale, Point3D rotation) {
		this.ttl -= elapsedTime;
		
		this.velocity = velocity.add( wind.multiply(elapsedTime / 100.0) );		
		this.position = this.position.add(this.velocity);
		
		this.setTranslate(this.position);
		this.setRotate(rotation);
		this.setScale(scale);
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
	
	private void setTranslate (Point3D translate) {
		this.box.setTranslateX(translate.getX());
		this.box.setTranslateY(translate.getY());
		this.box.setTranslateZ(translate.getZ());
	}
	
	private void setScale (Point3D scale) {
		this.sx.setX(scale.getX());
		this.sy.setY(scale.getY());
		this.sz.setZ(scale.getZ());
	}
	
	private void setRotate (Point3D rotation) {
		this.rx.setAngle(rotation.getX());
		this.ry.setAngle(rotation.getY());
		this.rz.setAngle(rotation.getZ());
	}
	
}
