package particles;

public class Vector3d {
	
	public double x;
	public double y;
	public double z;
	
	public Vector3d () {}
	
	public Vector3d (double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public Vector3d clone () {
		return new Vector3d (this.x, this.y, this.z);
	}
	
}
