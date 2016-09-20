package etc.a0la0.particleRemix.messaging.osc;

import etc.a0la0.particleRemix.messaging.ParameterService;
import etc.a0la0.particleRemix.ui.DisplayFrame;

import java.util.HashMap;
import java.util.Map;


public class OscMessageHandler {

    private ParameterService parameterService;
    private Map<String, OscMessageHandler.ParamAction> messageMap;

    private interface ParamAction {
        void runAction (float value);
    }

    public OscMessageHandler (ParameterService parameterService, DisplayFrame displayFrame) {
        this.parameterService = parameterService;
        messageMap = createMessageMap();
    }

    public void handleMessage(String address, float value) {
        messageMap.get(address).runAction(value);
    }

    private Map<String, OscMessageHandler.ParamAction> createMessageMap() {
        messageMap = new HashMap<>();

        messageMap.put("/velocity/x", value -> parameterService.setVelocityX(value));

        messageMap.put("/velocity/y", value -> parameterService.setVelocityY(value));

        messageMap.put("/velocity/z", value -> parameterService.setVelocityZ(value));

        messageMap.put("/velocity/initial", value -> parameterService.setInitialVelocity(value));

        messageMap.put("/scale/x", value -> parameterService.setScaleX(value));

        messageMap.put("/scale/y", value -> parameterService.setScaleY(value));

        messageMap.put("/scale/z", value -> parameterService.setScaleZ(value));

        return messageMap;
    }

}
