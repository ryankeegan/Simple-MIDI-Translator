/**
 * Copyright (C) 2020  Ryan Keegan
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 */

import javax.sound.midi.*;

/**
 * Sets up connections to the input and output MIDI devices
 */
public class MIDIHandler {
    private MidiDevice fSourceDevice;  // Device that we will be translating from
    private MidiDevice fTargetDevice;  // Device that we will be broadcasting to
    private MidiDevice.Info[] fDevicesInfo = MidiSystem.getMidiDeviceInfo();
    private static Receiver TARGET_RECEIVER;

    /**
     * Open a pair of connections (an input MIDI device and an output to broadcast the translated MIDI
     * instructions to)
     */
    MIDIHandler() {
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
            sourceTransmitter.setReceiver(new SourceReceiver());
            fSourceDevice.open();
            Console.info("Source MIDI device opened successfully!");
        } catch(MidiUnavailableException e) {
            Console.error("FATAL: Source MIDI device busy");
            System.exit(-1);
        }

        // Open receiver for target device
        try {
            TARGET_RECEIVER = fTargetDevice.getReceiver();
            fTargetDevice.open();
            Console.info("Target MIDI device opened successfully!");
        } catch(MidiUnavailableException e) {
            Console.error("FATAL: Target MIDI device busy");
            System.exit(-1);
        }
    }

    /**
     * Used to list the available MIDI devices on the machine. Each listing contains the device name,
     * description, and a corresponding index. The user selects the device by entering the corresponding
     * index which is then returned.
     * @param type  Used to change the prompt for user input to reflect what device they are selecting
     *              (input or target)
     * @return Integer representing the index the user selected
     */
    private int selectMIDIDeviceIndex(String type) {
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

    static Receiver getTargetReceiver() {
        return TARGET_RECEIVER;
    }
}
