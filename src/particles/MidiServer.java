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
			
			if (sm.getData1() == 37) {
				particleParams.x = getNormalizedMidiValue(sm.getData2());
			}
			else if (sm.getData1() == 38) {
				particleParams.y = getNormalizedMidiValue(sm.getData2());
			}
			else if (sm.getData1() == 39) {
				particleParams.z = getNormalizedMidiValue(sm.getData2());
			}
			else if (sm.getData1() == 40) {
				particleParams.ttl = sm.getData2();
			}
		}
		
		private int getNormalizedMidiValue (int realValue) {
			return (realValue - base) / magnitude;
		}
		
	}
	

}