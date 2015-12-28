package particles;

public class CameraPositionService {
	
	private Xform cameraXform = new Xform();
	private ParameterService parameterService;
	private double routineAngle = 0;
	
	public CameraPositionService (ParameterService parameterService) {
		this.parameterService = parameterService;
	}
	
	public Xform getCameraXform () {
		return this.cameraXform;
	}
	
	public void update (double elapsedTime) {
		if (this.parameterService.cameraIsInRoutine()) {
			this.routineAngle = cameraXform.ry.getAngle() + elapsedTime * 0.1;
			this.cameraXform.ry.setAngle(routineAngle);
		}
		if (this.routineAngle >= 360) {
			this.routineAngle = 0;
			this.cameraXform.ry.setAngle(this.routineAngle);
			this.parameterService.setCameraIsInRoutine(false);
		}
	}

}
