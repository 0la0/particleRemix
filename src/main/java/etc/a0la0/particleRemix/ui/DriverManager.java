package etc.a0la0.particleRemix.ui;

import java.util.Set;

public class DriverManager {
	
	private Set<IDriver> drivers;
	private IDriver activeDriver;
	
	public DriverManager (Set<IDriver> drivers) {
		this.drivers = drivers;
	}
	
	public void setActiveDriverByName (String name) {
		this.activeDriver =  drivers.stream()
				.filter(driver -> driver.getName() == name)
				.findFirst()
				.get();
	}
	
	public IDriver getActiveDriver () {
		return this.activeDriver;
	}

}
