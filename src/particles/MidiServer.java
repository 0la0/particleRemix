package particles;

import java.util.Arrays;

import javafx.geometry.Point3D;

import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Receiver;
import javax.sound.midi.ShortMessage;


public class MidiServer {
	
	private MidiDevice midiDevice;
	private Point3D currentVelocity = new Point3D(Math.random(), Math.random(), Math.random());
	private int ttlUpperBound = 20;
	
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
	
	public Point3D getCurrentVelocity () {
		return this.currentVelocity;
	}
	
	public int getTtlUpperBound () {
		return this.ttlUpperBound;
	}
	
	
	private class MidiInReceiver implements Receiver {
		
		private int magnitude = 32;
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
				currentVelocity = new Point3D(
						getNormalizedMidiValue(sm.getData2()),
						currentVelocity.getY(),
						currentVelocity.getZ());
			}
			else if (sm.getData1() == 38) {
				currentVelocity = new Point3D(
						currentVelocity.getX(),
						getNormalizedMidiValue(sm.getData2()),
						currentVelocity.getZ());
			}
			else if (sm.getData1() == 39) {
				currentVelocity = new Point3D(
						currentVelocity.getX(),
						currentVelocity.getY(),
						getNormalizedMidiValue(sm.getData2()));
			}
			else if (sm.getData1() == 40) {
				ttlUpperBound = sm.getData2() + 2;
			}
		}
		
		private int getNormalizedMidiValue (int realValue) {
			return (realValue - base) / magnitude;
		}
		
	}
	

}