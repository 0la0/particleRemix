package particles;

import javafx.geometry.Point3D;

public class ParameterService {
	
	private Point3D velocity = new Point3D(0, 0, 0);
	private Point3D scale = new Point3D(1, 1, 1);
	private Point3D rotate = new Point3D(0, 0, 0);
	private double ttlUpperBound = 2500;
	private boolean swarmIsOn = false;
	private double initialVelocity = Math.random();
	private double goalAttraction = 0.0001;
	private boolean cameraIsInRoutine = false;
	private boolean isMotionDetection = false;

	public ParameterService () {}

	public Point3D getVelocity () {
		return velocity;
	}

	public void setVelocity (Point3D velocity) {
		this.velocity = velocity;
	}
	
	public void setVelocityX (double x) {
		this.velocity = new Point3D(x, this.velocity.getY(), this.velocity.getZ());
	}
	
	public void setVelocityY (double y) {
		this.velocity = new Point3D(this.velocity.getX(), y, this.velocity.getZ());
	}
	
	public void setVelocityZ (double z) {
		this.velocity = new Point3D(this.velocity.getX(), this.velocity.getY(), z);
	}

	public Point3D getScale () {
		return scale;
	}

	public void setScale (Point3D scale) {
		this.scale = scale;
	}
	
	public void setScaleX (double x) {
		this.scale = new Point3D(x, this.scale.getY(), this.scale.getZ());
	}
	
	public void setScaleY (double y) {
		this.scale = new Point3D(this.scale.getX(), y, this.scale.getZ());
	}
	
	public void setScaleZ (double z) {
		this.scale = new Point3D(this.scale.getX(), this.scale.getY(), z);
	}

	public Point3D getRotate () {
		return rotate;
	}

	public void setRotate (Point3D rotate) {
		this.rotate = rotate;
	}
	
	public void setRotateX (double x) {
		this.rotate = new Point3D(x, this.rotate.getY(), this.rotate.getZ());
	}
	
	public void setRotateY (double y) {
		this.rotate = new Point3D(this.rotate.getX(), y, this.rotate.getZ());
	}
	
	public void setRotateZ (double z) {
		this.rotate = new Point3D(this.rotate.getX(), this.rotate.getY(), z);
	}

	public double getTtlUpperBound () {
		return ttlUpperBound;
	}

	public void setTtlUpperBound (double ttlUpperBound) {
		this.ttlUpperBound = ttlUpperBound;
	}

	public boolean getSwarmIsOn() {
		return this.swarmIsOn;
	}

	public void setSwarmIsOn(boolean swarmIsOn) {
		this.swarmIsOn = swarmIsOn;
	}
	
	public double getInitialVelocity () {
		return initialVelocity;
	}

	public void setInitialVelocity (double initialVelocity) {
		this.initialVelocity = initialVelocity;
	}
	
	public double getGoalAttraction () {
		return this.goalAttraction;
	}
	
	public void setGoalAttraction (double goalAttraction) {
		this.goalAttraction = goalAttraction;
	}
	
	public boolean cameraIsInRoutine () {
		return this.cameraIsInRoutine;
	}

	public void setCameraIsInRoutine (boolean cameraIsInRoutine) {
		this.cameraIsInRoutine = cameraIsInRoutine;
	}
	
	public boolean isMotionDetection () {
		return isMotionDetection;
	}

	public void setMotionDetection (boolean isMotionDetection) {
		this.isMotionDetection = isMotionDetection;
	}
	
}
