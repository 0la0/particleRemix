package etc.a0la0.particleRemix.messaging;

import java.util.ArrayList;
import java.util.List;

import javafx.geometry.Point2D;
import javafx.geometry.Point3D;

public class ParameterService {
	
	private Point3D velocity = new Point3D(0, 0, 0);
	private Point3D scale = new Point3D(1, 1, 1);
	private Point3D rotate = new Point3D(0, 0, 0);
	private double ttlUpperBound = 2500;
	private double initialVelocity = Math.random();
	private double goalAttraction = 0;
	private boolean cameraIsInRoutine = false;
	private double motionThreshold = 0;
	private List<Point2D> motionPointList = new ArrayList<>();
	private double imageRefreshRate = 0;

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
	
	public double getMotionThreshold () {
		return this.motionThreshold;
	}

	public void setMotionThreshold (double motionThreshold) {
		this.motionThreshold = motionThreshold;
	}
	
	public List<Point2D> getMotionPointList() {
		return motionPointList;
	}

	public void setMotionPointList(List<Point2D> motionPointList) {
		this.motionPointList = motionPointList;
	}
	
	public double getImageRefreshRate () {
		return this.imageRefreshRate;
	}
	
	public void setImageRefreshRate (double imageRefreshRate) {
		this.imageRefreshRate = imageRefreshRate;
	}
	
}
