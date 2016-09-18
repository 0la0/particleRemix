package etc.a0la0.particleRemix.messaging.midi;

import java.util.Arrays;
import java.util.NoSuchElementException;

import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Receiver;
import javax.sound.midi.ShortMessage;


public class MidiServer {
	
	private MidiMessageHandler messageHandler;

	public MidiServer (MidiMessageHandler messageHandler) {
		this.messageHandler = messageHandler;
		
		MidiDeviceFactory.refreshDevices();

		String[] receivers = MidiDeviceFactory.getReceivers();
		String targetDevice;
		try {
			targetDevice = Arrays.stream(receivers)
					.filter(receiver -> receiver.indexOf("Trigger Finger") > -1)
					.findFirst()
					.get();
		}
		catch (NoSuchElementException e) {
			System.out.println("Ahh! could not find midi device => nothing to control particles (for now)");
			return;
		}

		MidiDevice midiDevice = MidiDeviceFactory.getTransmitterDevice(targetDevice);
		try {
			midiDevice.open();
			midiDevice.getTransmitter().setReceiver(new MidiInReceiver());
		} catch (MidiUnavailableException e) {
			e.printStackTrace();
		}
	}
	
	private class MidiInReceiver implements Receiver {
		
		@Override
		public void close () {}

		@Override
		public void send (MidiMessage msg, long timeStamp) {
			ShortMessage sm = (ShortMessage) msg;
			//printMessage(sm);
			messageHandler.handleMessage(sm);
		}
		
		private void printMessage (ShortMessage sm) {
			String message = new StringBuffer()
				.append(sm.getCommand())
				.append(", ")
				.append(sm.getChannel())
				.append(", ")
				.append(sm.getData1())
				.append(", ")
				.append(sm.getData2())
				.toString();
			System.out.println(message);
		}
		
	}
	

}