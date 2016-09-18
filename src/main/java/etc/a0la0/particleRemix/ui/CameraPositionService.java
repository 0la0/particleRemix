package etc.a0la0.particleRemix.ui;

import etc.a0la0.particleRemix.messaging.ParameterService;
import etc.a0la0.particleRemix.ui.util.Xform;

public class CameraPositionService {
	
	private Xform cameraXform = new Xform();
	private ParameterService parameterService;
	private double routineAngle = 0;
	
	public CameraPositionService (ParameterService parameterService) {
		this.parameterService = parameterService;
	}
	
	public Xform getCameraXform () {
		return cameraXform;
	}

	public void update (double elapsedTime) {
		if (parameterService.cameraIsInRoutine()) {
			routineAngle = cameraXform.ry.getAngle() + elapsedTime * 0.1;
			cameraXform.ry.setAngle(routineAngle);
		}
		if (routineAngle >= 360) {
			routineAngle = 0;
			cameraXform.ry.setAngle(routineAngle);
			parameterService.setCameraIsInRoutine(false);
		}
	}

}
