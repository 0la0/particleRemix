package etc.a0la0.particleRemix.ui;

import etc.a0la0.particleRemix.ui.util.Xform;
import javafx.scene.image.WritableImage;

public interface IDriver {
	
	public void update (double elapsedTime, WritableImage screenshot);
	
	public Xform getParticleGroup ();
	
	public String getName ();

}
