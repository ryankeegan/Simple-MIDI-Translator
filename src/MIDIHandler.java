import javax.sound.midi.*;

public class MIDIHandler {
    MidiDevice fSourceDevice;  // Device that we will be translating from
    MidiDevice fTargetDevice;  // Device that we will be broadcasting to
    MidiDevice.Info[] fDevicesInfo = MidiSystem.getMidiDeviceInfo();
    public MIDIHandler() {
        try {
            // Set source device
            fSourceDevice = MidiSystem.getMidiDevice(fDevicesInfo[selectMIDIDeviceIndex()]);
            // Set target device
            fTargetDevice = MidiSystem.getMidiDevice(fDevicesInfo[selectMIDIDeviceIndex()]);
        } catch(MidiUnavailableException e) {
            Console.error("FATAL: Failed to open MIDI device(s)");
            System.exit(-1);
        }
    }

    public int selectMIDIDeviceIndex() {
        Console.banner();

        // List devices
        for(int i = 0; i < fDevicesInfo.length; i++) {
            Console.message("Device Index: " + i);
            Console.message("Device Name: " + fDevicesInfo[i].getName());
            Console.message("Device Description: " + fDevicesInfo[i].getDescription());
            Console.banner();
        }

        // User selects input device
        int deviceIndex = 0;
        boolean invalidInput = true;
        while(invalidInput) {
            Console.message("Select input device:");
            try {
                deviceIndex = Integer.parseInt(Console.getInput().nextLine());
                if(deviceIndex < 0 || deviceIndex >= fDevicesInfo.length)
                    throw new NumberFormatException();
                invalidInput = false;
            } catch(NumberFormatException e) {
                Console.error("Invalid input; expects integer within range: 0, " + (fDevicesInfo.length - 1));
            }
        }

        return deviceIndex;
    }
}
