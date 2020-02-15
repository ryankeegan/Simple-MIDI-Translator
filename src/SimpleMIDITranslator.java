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

public class SimpleMIDITranslator {
    private static final String PATH_TO_CONFIG = "./mappings.txt";
    // Path is relative to the location of the JAR file, points to where the translation mapping is located

    public static void main(String[] args) {
        SimpleMIDITranslator thread = new SimpleMIDITranslator();
        thread.threadSleep();
    }

    private SimpleMIDITranslator() {
        System.out.println("GPL v2  Copyright (C) 2020  Ryan Keegan (see LICENSE for details)");
        // Load config file into memory
        MIDITranslate.loadTranslationTable(PATH_TO_CONFIG);
        // Load MIDI devices
        new MIDIHandler();
    }

    /**
     * Run forrreeeeeeeeeeevverrrrrrrrrr...
     */
    private synchronized void threadSleep() {
        while(true) {
            try {
                this.wait(2000);
            } catch(InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
