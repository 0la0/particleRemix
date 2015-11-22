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
	private ParameterService parameterService;
	
	
	public MidiServer (ParameterService parameterService) {
		this.parameterService = parameterService;
		
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
	
	private class MidiInReceiver implements Receiver {
		
		private double velocityMagnitude = 64.0;
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
			//if (!messageIsTargeted(sm)) return;
			
			//slider 1 => velocity.x
			if (sm.getData1() == 37) {
				parameterService.setVelocityX(getNormalizedVelocity(sm.getData2()));
			}
			//slider 2 => velocity.y
			else if (sm.getData1() == 38) {
				parameterService.setVelocityY(getNormalizedVelocity(sm.getData2()));
			}
			//slider 3 => velocity.z
			else if (sm.getData1() == 39) {
				parameterService.setVelocityZ(getNormalizedVelocity(sm.getData2()));
			}
			//slider 4 => ttl
			else if (sm.getData1() == 40) {
				double normalValue = sm.getData2() / maxValue;
				double ttlUpperBound = normalValue * 3000 + 1;
				parameterService.setTtlUpperBound(ttlUpperBound);
			}
			//top knob 1 => scale.x
			else if (sm.getData1() == 42) {
				parameterService.setScaleX(getNormalizedScale(sm.getData2()));
			}
			//top knob 2 => scale.y
			else if (sm.getData1() == 44) {
				parameterService.setScaleY(getNormalizedScale(sm.getData2()));
			}
			//top knob 3 => scale.z
			else if (sm.getData1() == 46) {
				parameterService.setScaleZ(getNormalizedScale(sm.getData2()));
			}
			//top knob 4 => initial velocity
			else if (sm.getData1() == 48) {
				parameterService.setInitialVelocity(sm.getData2() / 32.0);
			}
 			//bottom knob 1 => rotate.x
			else if (sm.getData1() == 41) {
				parameterService.setRotateX(getNormalizedRotate(sm.getData2()));
			}
			//bottom knob 2 => rotate.y
			else if (sm.getData1() == 43) {
				parameterService.setRotateY(getNormalizedRotate(sm.getData2()));
			}
			//bottom knob 3 => rotate.z
			else if (sm.getData1() == 45) {
				parameterService.setRotateZ(getNormalizedRotate(sm.getData2()));
			}
			//bottom knob 4 => goalAttraction
			else if (sm.getData1() == 49) {
				parameterService.setGoalAttraction(sm.getData2() * 0.00001);
			}
			//top left button => toggle swarm
			else if (sm.getData1() == 28 && sm.getData2() > 0) {
				parameterService.setSwarmIsOn(!parameterService.getSwarmIsOn());
			}
		}
		
		private double getNormalizedScale (int realValue) {
			return Math.pow(realValue / scaleMagnitude, 2);
		}
		
		private double getNormalizedVelocity (int realValue) {
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