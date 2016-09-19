package etc.a0la0.particleRemix.ui;

import etc.a0la0.particleRemix.ui.util.Xform;
import javafx.scene.image.WritableImage;

import java.util.Set;

public class DriverManager {
	
	private Set<Driver> drivers;
	private Driver activeDriver;

	interface Driver {
		void update (double elapsedTime, WritableImage screenshot);
		Xform getParticleGroup ();
		String getName ();
	}
	
	public DriverManager (Set<Driver> drivers) {
		this.drivers = drivers;
	}
	
	public void setActiveDriverByName (String name) {
		this.activeDriver =  drivers.stream()
				.filter(driver -> driver.getName() == name)
				.findFirst()
				.get();
	}
	
	public Driver getActiveDriver () {
		return activeDriver;
	}

}
