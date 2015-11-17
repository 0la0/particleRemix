package particles;

public class ParticleParameters {
	
	private int ttl;
	private Vector3d wind = new Vector3d();
	
	public ParticleParameters () {}
	
	public ParticleParameters (int x, int y, int z) {
		wind.x = x;
		wind.y = y;
		wind.z = z;
	}
	
	public ParticleParameters (int x, int y, int z, int ttl) {
		wind.x = x;
		wind.y = y;
		wind.z = z;
		this.ttl = ttl;
	}
	
	public int getTTL () {
		return this.ttl;
	}
	
	public void setTTL (int ttl) {
		this.ttl = ttl;
	}
	
	public Vector3d getWind () {
		return this.wind;
	}
	
}
