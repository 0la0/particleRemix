package particles;

import java.util.Arrays;
import java.util.NoSuchElementException;

import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Receiver;
import javax.sound.midi.ShortMessage;


public class MidiServer {
	
	private MidiDevice midiDevice;
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
		
		this.midiDevice = MidiDeviceFactory.getTransmitterDevice(targetDevice);
		try {
			this.midiDevice.open();
			this.midiDevice.getTransmitter().setReceiver(new MidiInReceiver());
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
			StringBuffer sb = new StringBuffer();
			sb.append(sm.getCommand() + ", ");
			sb.append(sm.getChannel() + ", ");
			sb.append(sm.getData1() + ", ");
			sb.append(sm.getData2());
			System.out.println(sb.toString());
		}
		
	}
	

}