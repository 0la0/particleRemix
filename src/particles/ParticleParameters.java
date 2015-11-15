package particles;

public class ParticleParameters {

	public int x;
	public int y;
	public int z;
	public int ttl;
	
	public ParticleParameters () {}
	
	public ParticleParameters (int x, int y, int z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public ParticleParameters (int x, int y, int z, int ttl) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.ttl = ttl;
	}
	
}
