package etc.a0la0.particleRemix.messaging.osc;

import com.illposed.osc.OSCListener;
import com.illposed.osc.OSCPortIn;

import java.net.SocketException;


public class OscServer {

    public OscServer(OscMessageHandler oscMessageHandler) {
        try {
            OSCPortIn receiver = new OSCPortIn(8081);

            OSCListener listener = (time, message) -> {
                String address = message.getAddress();
                float val = (float) message.getArguments().get(0);

//                System.out.println("Message received");
//                System.out.println(address);
//                System.out.println(val);

                oscMessageHandler.handleMessage(address, val);
            };

            receiver.addListener("//", listener);
            receiver.startListening();
        }
        catch (SocketException e) {
            e.printStackTrace();
        }
    }

}
