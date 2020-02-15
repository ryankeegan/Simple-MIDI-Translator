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

import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Cleaner than calling System.out... or Logger...
 */
public class Console {
    static Logger LOGGER = Logger.getLogger("SimpleMIDITranslator: ");
    static String BANNER = "------------------------------------------";
    static Scanner INPUT = new Scanner(System.in);

    public static void banner() {
        System.out.println(BANNER);
    }

    public static void message(String message) {
        System.out.println(message);
    }

    public static void messageNoLine(String message) {
        System.out.print(message);
    }

    public static void info(String message) {
        LOGGER.log(Level.INFO, message);
    }

    public static void error(String message) {
        LOGGER.log(Level.SEVERE, message);
    }

    public static Scanner getInput() {
        return INPUT;
    }
}
