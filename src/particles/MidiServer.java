package particles;

import java.util.Arrays;

import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Receiver;
import javax.sound.midi.ShortMessage;


public class MidiServer {
	
	private MidiDevice midiDevice;
	private ParticleParameters particleParams = new ParticleParameters(0, 0, 0, 50);
	
	public MidiServer () {
		MidiDeviceFactory.refreshDevices();
		
		String[] receivers = MidiDeviceFactory.getReceivers();
		String targetDevice = Arrays.stream(receivers)
			.filter(receiver -> receiver.indexOf("Trigger Finger") > -1)
			.findFirst()
			.get();
		
		this.midiDevice = MidiDeviceFactory.getTransmitterDevice(targetDevice);
		try {
			this.midiDevice.open();
			this.midiDevice.getTransmitter().setReceiver(new MidiInReceiver());
		} catch (MidiUnavailableException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public ParticleParameters getParticleParams () {
		return this.particleParams;
	}
	
	private class MidiInReceiver implements Receiver {
		
		private int magnitude = 16;
		private int base = 64;
		
		@Override
		public void close () {}

		@Override
		public void send (MidiMessage msg, long timeStamp) {
			ShortMessage sm = (ShortMessage) msg;
			String dataFrame = sm.getCommand() + "," + sm.getChannel() + "," + sm.getData1() + "," + sm.getData2();
			//-------SEND TO ALL OBSERVERS-------//
			System.out.println("message received: " + dataFrame);
//			for (WebSocket socket : server.connections()) {
//				socket.send(dataFrame);
//			}
			runMidiMapping(sm);
		}
		
		private boolean messageIsTargeted (ShortMessage sm) {
			boolean isCommand = sm.getCommand() == 176;
			boolean isChannel = sm.getChannel() == 14;
			boolean isData = sm.getData1() >= 37 && sm.getData1() <= 40;
			return isCommand && isChannel && isData;
		}
		
		
		
		private void runMidiMapping (ShortMessage sm) {
			if (!messageIsTargeted(sm)) return;
			System.out.println("run mapping");
			int normalizedValue = (sm.getData2() - base) / magnitude;
			
			if (sm.getData1() == 37) {
				particleParams.x = normalizedValue;
				System.out.println("setting x at: " + particleParams.x);
			}
			else if (sm.getData1() == 38) {
				particleParams.y = normalizedValue;
			}
			else if (sm.getData1() == 39) {
				particleParams.z = normalizedValue;
			}
			else if (sm.getData1() == 40) {
				particleParams.ttl = sm.getData2();
			}
			
			
		}
		
	}
	

}