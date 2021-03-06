package etc.a0la0.particleRemix.messaging.websocket;

import etc.a0la0.particleRemix.messaging.ParameterService;
import etc.a0la0.particleRemix.ui.DisplayFrame;

import java.util.HashMap;
import java.util.Map;


public class WebSocketMessageHandler {
	
	private ParameterService parameterService;
	private DisplayFrame displayFrame;
	private Map<String, ParamAction> messageMap = new HashMap<>();
	
	public WebSocketMessageHandler (ParameterService parameterService, DisplayFrame displayFrame) {
		this.parameterService = parameterService;
		this.displayFrame = displayFrame;
		initializeMap();
	}
	
	public void handleMessage (String msgKey, double msgValue) {
		messageMap.get(msgKey).runAction(msgValue);
	}
	
	private void initializeMap () {
		messageMap.put("velocityX", (double msgValue) -> parameterService.setVelocityX(normalizePositiveNegative(msgValue)));
		messageMap.put("velocityY", (double msgValue) -> parameterService.setVelocityY(normalizePositiveNegative(msgValue)));
		messageMap.put("velocityZ", (double msgValue) -> parameterService.setVelocityZ(normalizePositiveNegative(msgValue)));
		
		messageMap.put("ttl", (double msgValue) -> {
			double ttlUpperBound = msgValue * 3000 + 1;
			parameterService.setTtlUpperBound(ttlUpperBound);
		});
		
		messageMap.put("initialVelocity", (double msgValue) -> parameterService.setInitialVelocity(msgValue));

		messageMap.put("scaleX", (double msgValue) -> parameterService.setScaleX(getNormalizedScale(msgValue)));
		messageMap.put("scaleY", (double msgValue) -> parameterService.setScaleY(getNormalizedScale(msgValue)));
		messageMap.put("scaleZ", (double msgValue) -> parameterService.setScaleZ(getNormalizedScale(msgValue)));

		messageMap.put("rotateX", (double msgValue) -> parameterService.setRotateX(getNormalizedRotate(msgValue)));
		messageMap.put("rotateY", (double msgValue) -> parameterService.setRotateY(getNormalizedRotate(msgValue)));
		messageMap.put("rotateZ", (double msgValue) -> parameterService.setRotateZ(getNormalizedRotate(msgValue)));
		
		messageMap.put("cameraDistance", (double msgValue) -> {
			double adjustedValue = 100 + 600 * msgValue;
			displayFrame.setCameraDistance(adjustedValue);
		});
		
	}
	
	private interface ParamAction {
		void runAction (double val);
	}
	
	private double normalizePositiveNegative (double val) {
		return (val - 0.5) / 0.5;
	}
	
	private double getNormalizedScale (double val) {
		return Math.pow(val * 20, 2);
	}
	
	private double getNormalizedRotate (double val) {
		return val * 360;
	}
	
}
