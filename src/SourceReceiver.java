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

import javax.sound.midi.MidiMessage;
import javax.sound.midi.Receiver;

/**
 * Receives all MIDI events from the input device. Hands them off to MIDITranslator to be rebroadcasted
 * to the target device.
 */
public class SourceReceiver implements Receiver {
    /**
     * Called whenever the input device sends a MIDI message
     * @param message
     * @param timeStamp
     */
    public void send(MidiMessage message, long timeStamp) {
        Console.messageNoLine("RECEIVED: ");
        MIDITranslate.sendToTarget(message, timeStamp);     // Handed off to translator which sends it to target
    }

    public void close() {}
}
