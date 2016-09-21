package etc.a0la0.particleRemix.messaging.osc;

import etc.a0la0.particleRemix.messaging.ParameterService;
import etc.a0la0.particleRemix.ui.DisplayFrame;

import java.util.HashMap;
import java.util.Map;


public class OscMessageHandler {

    private ParameterService parameterService;
    private DisplayFrame displayFrame;
    private Map<String, OscMessageHandler.ParamAction> messageMap;

    private interface ParamAction {
        void runAction (float value);
    }

    public OscMessageHandler (ParameterService parameterService, DisplayFrame displayFrame) {
        this.parameterService = parameterService;
        this.displayFrame = displayFrame;
        messageMap = createMessageMap();
    }

    public void handleMessage(String address, float value) {
        if (messageMap.containsKey(address)) {
            messageMap.get(address).runAction(value);
        }
        else {
            System.out.println("Missed message address: " + address);
        }
    }

    private Map<String, OscMessageHandler.ParamAction> createMessageMap() {
        messageMap = new HashMap<>();

        messageMap.put("/velocity/x", value ->
                parameterService.setVelocityX(transformVelocity(value)));

        messageMap.put("/velocity/y", value ->
                parameterService.setVelocityY(transformVelocity(value)));

        messageMap.put("/velocity/z", value ->
                parameterService.setVelocityZ(transformVelocity(value)));

        messageMap.put("/scale/x", value ->
                parameterService.setScaleX(transformScale(value)));

        messageMap.put("/scale/y", value ->
                parameterService.setScaleY(transformScale(value)));

        messageMap.put("/scale/z", value ->
                parameterService.setScaleZ(transformScale(value)));

        messageMap.put("/velocity/initial", value ->
                parameterService.setInitialVelocity(value));

        messageMap.put("/timeToLive", value -> {
            double ttlUpperBound = value * 3000 + 1;
            parameterService.setTtlUpperBound(ttlUpperBound);
        });

        messageMap.put("/rotate/x", value ->
                parameterService.setRotateX(transformRotate(value)));

        messageMap.put("/rotate/y", value ->
                parameterService.setRotateY(transformRotate(value)));

        messageMap.put("/rotate/z", value ->
                parameterService.setRotateZ(transformRotate(value)));

        messageMap.put("/goalAttraction", value ->
                parameterService.setGoalAttraction(value * 0.002));

        messageMap.put("/camera/distance", value -> {
                double adjustedValue = 100 + 600 * value;
                displayFrame.setCameraDistance(adjustedValue);
        });

        return messageMap;
    }

    private float transformVelocity(float normalValue) {
        return (normalValue - 0.5f) * 2;
    }

    private float transformScale(float normalValue) {
        return normalValue * 10;
    }

    private float transformRotate(float normalValue) {
        return normalValue * 360;
    }

}
