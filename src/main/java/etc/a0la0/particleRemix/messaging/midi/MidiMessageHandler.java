package etc.a0la0.particleRemix.messaging.midi;

import etc.a0la0.particleRemix.messaging.ParameterService;
import etc.a0la0.particleRemix.ui.DisplayFrame;

import java.util.Map;
import java.util.HashMap;

import javax.sound.midi.ShortMessage;

public class MidiMessageHandler {
	
	private final double VELOCITY_MAGNITUDE = 64.0;
	private final double SCALE_MAGNITUDE = 6.0;
	private final int BASE = 64;
	private final double MAX_VALUE = 127.0;
	
	private ParameterService parameterService;
	private DisplayFrame displayFrame;
	private Map<Integer, ParamAction> midiMap = new HashMap<>();

	private interface ParamAction {
		void runAction (ShortMessage sm);
	}
	
	public MidiMessageHandler (ParameterService parameterService, DisplayFrame displayFrame) {
		this.parameterService = parameterService;
		this.displayFrame = displayFrame;
		initializeMap();
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
			double normalValue = sm.getData2() / MAX_VALUE;
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
		//bottom right button => camera routine
		midiMap.put(19, (ShortMessage sm) -> {
			if (sm.getData2() > 0) {
				parameterService.setCameraIsInRoutine(true);
			}
		});
		
		//P02 left slider => camera distance
		midiMap.put(25, (ShortMessage sm) -> {
			double normalValue = getNormalizedValue(sm.getData2());
			double adjustedValue = 100 + 600 * normalValue;
			displayFrame.setCameraDistance(adjustedValue);
		});
		
		//P02 middle left slider => motion detection threshold
		midiMap.put(26, (ShortMessage sm) -> {
			double normalValue = getNormalizedValue(sm.getData2());
			double adjustedValue = 100 * normalValue;
			parameterService.setMotionThreshold(adjustedValue);
		});	
		
		//P02 middle right slider => image refresh rate upper bound
		midiMap.put(27, (ShortMessage sm) -> {
			double normalValue = getNormalizedValue(sm.getData2());
			double adjustedValue = 3000 * normalValue;
			parameterService.setImageRefreshRate(adjustedValue);
		});	
		
		//P02 top button row => set active drivers
		midiMap.put(12, (ShortMessage sm) -> {
			if (sm.getData2() > 0) {
				displayFrame.setActiveDriverByName("particleDriver");
			}
		});	
		midiMap.put(13, (ShortMessage sm) -> {
			if (sm.getData2() > 0) {
				displayFrame.setActiveDriverByName("imageDriver");
			}
		});	
		
	}
	
	private double getNormalizedScale (int realValue) {
		return Math.pow(realValue / SCALE_MAGNITUDE, 2);
	}
	
	private double getNormalizedVelocity (int realValue) {
		return (realValue - BASE) / VELOCITY_MAGNITUDE;
	}
	
	private double getNormalizedRotate (int realValue) {
		return (realValue / MAX_VALUE) * 360;
	}
	
	private double getNormalizedValue (int realValue) {
		return realValue / MAX_VALUE;
	}

}
