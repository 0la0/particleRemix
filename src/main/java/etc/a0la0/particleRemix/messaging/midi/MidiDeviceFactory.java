package etc.a0la0.particleRemix.messaging.midi;

/*
 *  Lifted and modified from:
 *  https://github.com/dinchak/pages-2/blob/master/src/org/monome/pages/midi/MidiDeviceFactory.java
 */

import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;

import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Receiver;
import javax.sound.midi.Transmitter;


public class MidiDeviceFactory {

	private static List<MidiDevice> transmitters;
	
	private static List<MidiDevice> receivers;
	
	public static void refreshDevices () {
		transmitters = new ArrayList<>();
		receivers = new ArrayList<>();

		MidiDevice.Info[] deviceInfo = MidiSystem.getMidiDeviceInfo();
		for (int i = 0; i < deviceInfo.length; i++) {
			try {
				MidiDevice device = MidiSystem.getMidiDevice(deviceInfo[i]);
				if (device.getMaxReceivers() != 0) receivers.add(device);
				if (device.getMaxTransmitters() != 0) transmitters.add(device);
			} catch (MidiUnavailableException e) {
				e.printStackTrace();
			}
			
		}
	}
	
	public static String[] getTransmitters () {
		return (String[]) transmitters.stream()
				.map(transmitter -> transmitter.getDeviceInfo().toString())
				.collect(Collectors.toList())
				.toArray();
	}
	
	public static String[] getReceivers () {
		return (String[]) receivers.stream()
				.map(receiver -> receiver.getDeviceInfo().toString())
				.collect(Collectors.toList())
				.toArray();
	}
	
	public static Transmitter getTransmitter (String deviceInfo) {
		for (MidiDevice midiDevice : transmitters) {
			if (midiDevice.getDeviceInfo().toString().equals(deviceInfo)) {
				try {
					return midiDevice.getTransmitter();
				} catch (MidiUnavailableException e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}
	
	public static Receiver getReceiver (String deviceInfo) {
		for (MidiDevice midiDevice : receivers) {
			if (midiDevice.getDeviceInfo().toString().equals(deviceInfo)) {
				try {
					return midiDevice.getReceiver();
				} catch (MidiUnavailableException e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}
	
	public static MidiDevice getReceiverDevice (String deviceInfo) {
		for (MidiDevice midiDevice : receivers) {
			if (midiDevice.getDeviceInfo().toString().equals(deviceInfo)) {
				return midiDevice;
			}
		}
		return null;
	}
	
	public static MidiDevice getTransmitterDevice (String deviceInfo) {
		for (MidiDevice midiDevice : transmitters) {
			if (midiDevice.getDeviceInfo().toString().equals(deviceInfo)) {
				return midiDevice;
			}
		}
		return null;
	}
	
}