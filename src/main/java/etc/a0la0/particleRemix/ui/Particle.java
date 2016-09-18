package etc.a0la0.particleRemix.ui;

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
	private Box box;
	
	private Rotate rx = new Rotate(0, Rotate.X_AXIS);
	private Rotate ry = new Rotate(0, Rotate.Y_AXIS);
	private Rotate rz = new Rotate(0, Rotate.Z_AXIS);
	private Scale sx = new Scale();
	private Scale sy = new Scale();
	private Scale sz = new Scale();
	
	
	public Particle (Point3D position, Point3D velocity, Color color, double ttl) {
		box = new Box(PARTICLE_SIZE, PARTICLE_SIZE, PARTICLE_SIZE);
		box.getTransforms().addAll(rz, ry, rx, sx, sy, sz);
		reset(position, velocity, color, ttl);
	}
	
	public void reset (Point3D position, Point3D velocity, Color color, double ttl) {
		this.position = position;
		this.velocity = velocity;
		this.ttl = ttl;
		setColor(color);
	}
	
	public Box getBox () {
		return box;
	}
	
	public void update (double elapsedTime, Point3D wind, Point3D scale, Point3D rotation) {
		ttl -= elapsedTime;
		
		velocity = velocity.add(wind);		
		position = position.add(velocity);
		
		setTranslate(position);
		setRotate(rotation);
		setScale(scale);
	}

	public void setColor (Color color) {
		Color difuseColor = color;
		PhongMaterial material = new PhongMaterial();
		material.setDiffuseColor(difuseColor);
		box.setMaterial(material);
	}
	
	public boolean isDead () {
		return ttl <= 0;
	}
	
	public Point3D getPosition () {
		return position;
	}
	
	public Point3D getVelocity () {
		return velocity;
	}
	
	private void setTranslate (Point3D translate) {
		box.setTranslateX(translate.getX());
		box.setTranslateY(translate.getY());
		box.setTranslateZ(translate.getZ());
	}
	
	private void setScale (Point3D scale) {
		sx.setX(scale.getX());
		sy.setY(scale.getY());
		sz.setZ(scale.getZ());
	}
	
	private void setRotate (Point3D rotation) {
		rx.setAngle(rotation.getX());
		ry.setAngle(rotation.getY());
		rz.setAngle(rotation.getZ());
	}
	
	public void setPivotX (double pivotX) {
		sx.setPivotX(pivotX);
		rx.setPivotX(pivotX);
	}
	
	public void setPivotY (double pivotY) {
		sx.setPivotY(pivotY);
		rx.setPivotY(pivotY);
	}
	
}
