package etc.a0la0.particleRemix;

/*
 *  Lifted and modified from:
 *  https://github.com/dinchak/pages-2/blob/master/src/org/monome/pages/midi/MidiDeviceFactory.java
 */

import java.util.ArrayList;

import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Receiver;
import javax.sound.midi.Transmitter;


public class MidiDeviceFactory {

	private static ArrayList<MidiDevice> transmitters;
	
	private static ArrayList<MidiDevice> receivers;
	
	public static void refreshDevices () {
		transmitters = new ArrayList<MidiDevice>();
		receivers = new ArrayList<MidiDevice>();
		
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
		String[] transmitterStrings = new String[transmitters.size()];
		for (int i = 0; i < transmitters.size(); i++) {
			transmitterStrings[i] = transmitters.get(i).getDeviceInfo().toString();
		}
		return transmitterStrings;
	}
	
	public static String[] getReceivers () {
		String[] receiverStrings = new String[receivers.size()];
		for (int i = 0; i < receivers.size(); i++) {
			receiverStrings[i] = receivers.get(i).getDeviceInfo().toString();
		}
		return receiverStrings;
	}
	
	public static Transmitter getTransmitter (String deviceInfo) {
		for (MidiDevice midiDevice : transmitters) {
			if (midiDevice.getDeviceInfo().toString().compareTo(deviceInfo) == 0) {
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
			if (midiDevice.getDeviceInfo().toString().compareTo(deviceInfo) == 0) {
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
			if (midiDevice.getDeviceInfo().toString().compareTo(deviceInfo) == 0) {
				return midiDevice;
			}
		}
		return null;
	}
	
	public static MidiDevice getTransmitterDevice (String deviceInfo) {
		for (MidiDevice midiDevice : transmitters) {
			if (midiDevice.getDeviceInfo().toString().compareTo(deviceInfo) == 0) {
				return midiDevice;
			}
		}
		return null;
	}
	
}