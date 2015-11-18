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
	private Point3D scale = new Point3D(1, 1, 1);
	private Point3D rotate = new Point3D(0, 0, 0);
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
	
	public Point3D getCurrentScale () {
		return this.scale;
	}
	
	public Point3D getCurrentRotation () {
		return this.rotate;
	}
	
	public int getTtlUpperBound () {
		return this.ttlUpperBound;
	}
	
	
	private class MidiInReceiver implements Receiver {
		
		private int velocityMagnitude = 32;
		private double scaleMagnitude = 6.0;
		private int base = 64;
		private double maxValue = 127.0;
		
		@Override
		public void close () {}

		@Override
		public void send (MidiMessage msg, long timeStamp) {
			ShortMessage sm = (ShortMessage) msg;
			//printMessage(sm);
			runMidiMapping(sm);
		}
		
		private boolean messageIsTargeted (ShortMessage sm) {
			boolean isCommand = sm.getCommand() == 176;
			boolean isChannel = sm.getChannel() == 14;
			boolean isData = sm.getData1() >= 37 && sm.getData1() <= 50;
			return isCommand && isChannel && isData;
		}
		
		private void runMidiMapping (ShortMessage sm) {
			if (!messageIsTargeted(sm)) return;
			
			//slider 1 => velocity.x
			if (sm.getData1() == 37) {
				currentVelocity = new Point3D(
						getNormalizedVelocity(sm.getData2()),
						currentVelocity.getY(),
						currentVelocity.getZ());
			}
			//slider 2 => velocity.y
			else if (sm.getData1() == 38) {
				currentVelocity = new Point3D(
						currentVelocity.getX(),
						getNormalizedVelocity(sm.getData2()),
						currentVelocity.getZ());
			}
			//slider 3 => velocity.z
			else if (sm.getData1() == 39) {
				currentVelocity = new Point3D(
						currentVelocity.getX(),
						currentVelocity.getY(),
						getNormalizedVelocity(sm.getData2()));
			}
			//slider 4 => ttl
			else if (sm.getData1() == 40) {
				ttlUpperBound = sm.getData2() + 2;
			}
			//top knob 1 => scale.x
			else if (sm.getData1() == 42) {
				scale = new Point3D(
						getNormalizedScale(sm.getData2()),
						scale.getY(),
						scale.getZ());
			}
			//top knob 2 => scale.y
			else if (sm.getData1() == 44) {
				scale = new Point3D(
						scale.getX(),
						getNormalizedScale(sm.getData2()),
						scale.getZ());
			}
			//top knob 3 => scale.z
			else if (sm.getData1() == 46) {
				scale = new Point3D(
						scale.getX(),
						scale.getY(),
						getNormalizedScale(sm.getData2()));
			}
			//bottom knob 1 => rotate.x
			else if (sm.getData1() == 41) {
				rotate = new Point3D(
						getNormalizedRotate(sm.getData2()),
						rotate.getY(),
						rotate.getZ());
			}
			//bottom knob 2 => rotate.y
			else if (sm.getData1() == 43) {
				rotate = new Point3D(
						rotate.getX(),
						getNormalizedRotate(sm.getData2()),
						rotate.getZ());
			}
			//bottom knob 3 => rotate.z
			else if (sm.getData1() == 45) {
				rotate = new Point3D(
						rotate.getX(),
						rotate.getY(),
						getNormalizedRotate(sm.getData2()));
			}
		}
		
		private double getNormalizedScale (int realValue) {
			return realValue / scaleMagnitude;
		}
		
		private int getNormalizedVelocity (int realValue) {
			return (realValue - base) / velocityMagnitude;
		}
		
		private double getNormalizedRotate (int realValue) {
			return (realValue / maxValue) * 360;
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