package etc.a0la0.particleRemix;

import javafx.scene.image.WritableImage;

public interface IDriver {
	
	public void update (double elapsedTime, WritableImage screenshot);
	
	public Xform getParticleGroup ();
	
	public String getName ();

}
