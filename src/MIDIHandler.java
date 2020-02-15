import javax.sound.midi.*;
import java.util.List;

public class MIDIHandler {
    MidiDevice fSourceDevice;  // Device that we will be translating from
    MidiDevice fTargetDevice;  // Device that we will be broadcasting to
    MidiDevice.Info[] fDevicesInfo = MidiSystem.getMidiDeviceInfo();
    static Receiver fTargetReceiver;

    public MIDIHandler() {
        try {
            // Set source device
            fSourceDevice = MidiSystem.getMidiDevice(fDevicesInfo[selectMIDIDeviceIndex("input")]);
            // Set target device
            fTargetDevice = MidiSystem.getMidiDevice(fDevicesInfo[selectMIDIDeviceIndex("output")]);
        } catch(MidiUnavailableException e) {
            Console.error("FATAL: Failed to open MIDI device(s)");
            System.exit(-1);
        }

        // Start receiving input from source device
        try {
            Transmitter sourceTransmitter = fSourceDevice.getTransmitter();
            sourceTransmitter.setReceiver(new SourceReceiver(fSourceDevice.getDeviceInfo().toString()));
            fSourceDevice.open();
            Console.info("Source MIDI device opened successfully!");
        } catch(MidiUnavailableException e) {
            Console.error("FATAL: Source MIDI device busy");
            System.exit(-1);
        }

        // Open receiver for target device
        try {
            fTargetReceiver = fTargetDevice.getReceiver();
            fTargetDevice.open();
            Console.info("Target MIDI device opened successfully!");
        } catch(MidiUnavailableException e) {
            Console.error("FATAL: Target MIDI device busy");
            System.exit(-1);
        }
    }

    public int selectMIDIDeviceIndex(String type) {
        Console.banner();

        // List devices
        for(int i = 0; i < fDevicesInfo.length; i++) {
            Console.message("Device Index: " + i);
            Console.message("Device Name: " + fDevicesInfo[i].getName());
            Console.message("Device Description: " + fDevicesInfo[i].getDescription());
            Console.banner();
        }

        // User selects device
        int deviceIndex = 0;
        boolean invalidInput = true;
        while(invalidInput) {
            Console.message("Select " + type + " device:");
            try {
                deviceIndex = Integer.parseInt(Console.getInput().nextLine());
                if(deviceIndex < 0 || deviceIndex >= fDevicesInfo.length)       // Ensure device is valid
                    throw new NumberFormatException();
                invalidInput = false;
            } catch(NumberFormatException e) {
                Console.error("Invalid input; expects integer within range: 0, " + (fDevicesInfo.length - 1));
            }
        }

        return deviceIndex;
    }

    public static Receiver getTargetReceiver() {
        return fTargetReceiver;
    }
}
