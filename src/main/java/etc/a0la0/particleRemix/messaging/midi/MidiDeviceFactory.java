package etc.a0la0.particleRemix.messaging.midi;

import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;

import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;


public class MidiDeviceFactory {

	private static List<MidiDevice> transmitters;
	private static List<MidiDevice> receivers;
	
	public static void refreshDevices () {
		transmitters = new ArrayList<>();
		receivers = new ArrayList<>();

		receivers = Arrays.stream(MidiSystem.getMidiDeviceInfo())
				.map(deviceInfoElement -> {
					try {
						return MidiSystem.getMidiDevice(deviceInfoElement);
					} catch (MidiUnavailableException e) {
						e.printStackTrace();
					}
					return null;
				})
				.filter(device -> device.getMaxReceivers() != 0)
				.collect(Collectors.toList());

		transmitters = Arrays.stream(MidiSystem.getMidiDeviceInfo())
				.map(deviceInfoElement -> {
					try {
						return MidiSystem.getMidiDevice(deviceInfoElement);
					} catch (MidiUnavailableException e) {
						e.printStackTrace();
					}
					return null;
				})
				.filter(device -> device.getMaxTransmitters() != 0)
				.collect(Collectors.toList());
	}
	
	public static String[] getTransmitters () {
		return transmitters.stream()
				.map(transmitter -> transmitter.getDeviceInfo().toString())
				.toArray(String[]::new);
	}
	
	public static String[] getReceivers () {
		return receivers.stream()
				.map(receiver -> receiver.getDeviceInfo().toString())
				.toArray(String[]::new);
	}
	
	public static MidiDevice getReceiverDevice (String deviceInfo) {
		return receivers.stream()
				.filter(transmitter -> transmitter.getDeviceInfo().toString().equals((deviceInfo)))
				.findFirst()
				.get();
	}
	
	public static MidiDevice getTransmitterDevice (String deviceInfo) {
		return transmitters.stream()
				.filter(transmitter -> transmitter.getDeviceInfo().toString().equals((deviceInfo)))
				.findFirst()
				.get();
	}
	
}