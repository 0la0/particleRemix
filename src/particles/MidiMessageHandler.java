package particles;

import java.util.HashMap;

import javax.sound.midi.ShortMessage;

public class MidiMessageHandler {
	
	private double velocityMagnitude = 64.0;
	private double scaleMagnitude = 6.0;
	private int base = 64;
	private double maxValue = 127.0;
	
	private ParameterService parameterService;
	private HashMap<Integer, IParameterAction> midiMap = new HashMap<Integer, IParameterAction>();
	
	public MidiMessageHandler (ParameterService parameterService) {
		this.parameterService = parameterService;
		this.initializeMap();
	}
	
	public void handleMessage (ShortMessage sm) {
		//check for null pointer
		midiMap.get(sm.getData1()).runAction(sm);
	}
	
	private void initializeMap () {
		
		//slider 1 => velocity.x
		midiMap.put(37, (ShortMessage sm) -> parameterService.setVelocityX(getNormalizedVelocity(sm.getData2())));
		//slider 2 => velocity.y
		midiMap.put(38, (ShortMessage sm) -> parameterService.setVelocityY(getNormalizedVelocity(sm.getData2())));
		//slider 3 => velocity.z
		midiMap.put(39, (ShortMessage sm) -> parameterService.setVelocityZ(getNormalizedVelocity(sm.getData2())));
		//slider 4 => ttl
		midiMap.put(40, (ShortMessage sm) -> {
			double normalValue = sm.getData2() / maxValue;
			double ttlUpperBound = normalValue * 3000 + 1;
			parameterService.setTtlUpperBound(ttlUpperBound);
		});
		//top knob 1 => scale.x
		midiMap.put(42, (ShortMessage sm) -> parameterService.setScaleX(getNormalizedScale(sm.getData2())));
		//top knob 2 => scale.y
		midiMap.put(44, (ShortMessage sm) -> parameterService.setScaleY(getNormalizedScale(sm.getData2())));
		//top knob 3 => scale.z
		midiMap.put(46, (ShortMessage sm) -> parameterService.setScaleZ(getNormalizedScale(sm.getData2())));
		//top knob 4 => initial velocity
		midiMap.put(48, (ShortMessage sm) -> parameterService.setInitialVelocity(sm.getData2() / 32.0));
		//bottom knob 1 => rotate.x
		midiMap.put(41, (ShortMessage sm) -> parameterService.setRotateX(getNormalizedRotate(sm.getData2())));
		//bottom knob 2 => rotate.y
		midiMap.put(43, (ShortMessage sm) -> parameterService.setRotateY(getNormalizedRotate(sm.getData2())));
		//bottom knob 3 => rotate.z
		midiMap.put(45, (ShortMessage sm) -> parameterService.setRotateZ(getNormalizedRotate(sm.getData2())));
		//bottom knob 4 => goalAttraction
		midiMap.put(49, (ShortMessage sm) -> parameterService.setGoalAttraction(sm.getData2() * 0.00001));
		//top left button => toggle swarm
		midiMap.put(28, (ShortMessage sm) -> {
			if (sm.getData2() > 0) {
				parameterService.setSwarmIsOn(!parameterService.getSwarmIsOn());
			}
		});
		//bottom right button => camera routine
		midiMap.put(19, (ShortMessage sm) -> {
			if (sm.getData2() > 0) {
				parameterService.setCameraIsInRoutine(true);
			}
		});
		//bottom left button => motion detection
		midiMap.put(16, (ShortMessage sm) -> {
			if (sm.getData2() > 0) {
				parameterService.setMotionDetection(!parameterService.isMotionDetection());
			}
		});	
		
	}
	
	private double getNormalizedScale (int realValue) {
		return Math.pow(realValue / scaleMagnitude, 2);
	}
	
	private double getNormalizedVelocity (int realValue) {
		return (realValue - base) / velocityMagnitude;
	}
	
	private double getNormalizedRotate (int realValue) {
		return (realValue / maxValue) * 360;
	}

}