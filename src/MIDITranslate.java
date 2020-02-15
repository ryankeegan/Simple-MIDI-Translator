import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.ShortMessage;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class MIDITranslate {
    private static Map<String, String> TRANSLATION_TABLE = new HashMap();   // <originalMessage, translatedMessage>
    private static final int CC_OFFSET = 176;
    private static final int PC_OFFSET = 192;
    private static final int PC_OFFSET_MAX = 207;

    /**
     * Loads custom mappings into memory. The map will be used to translate the MIDI signals
     * from the source device to the target device.
     */
    public static void loadTranslationTable(String pathToConfig) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(pathToConfig));
            String currentLine;
            while((currentLine = reader.readLine()) != null) {
                String[] translationComponents = currentLine.split("\\|");
                String originalMessage = translationComponents[0];
                String translatedMessage = translationComponents[1];
                TRANSLATION_TABLE.put(originalMessage, translatedMessage);
            }
        } catch(IOException e) {
            Console.error("FATAL: Could not locate config mapping; expected at relative path ./config");
            System.exit(-2);
        }
    }

    public static void sendToTarget(MidiMessage sourceMessage, long timestamp) {
        String[] translation = translateMessage(sourceMessage).split(",");  // Split back into Status, PC/CC, Velocity
        int statusCode = Integer.parseInt(translation[0]);
        int dataOne = Integer.parseInt(translation[1]);
        int dataTwo = 0;
        if(translation.length > 2)      // Only attempt to set if velocity exists (CC instruction)
            dataTwo = Integer.parseInt(translation[2]);

        ShortMessage translatedMessage = new ShortMessage();
        try {
            translatedMessage.setMessage(statusCode, dataOne, dataTwo);
        } catch (InvalidMidiDataException e) {
            Console.error("MIDI data invalid; check your translation mapping ('./config')");
        }

        MIDIHandler.getTargetReceiver().send(translatedMessage, timestamp);
        System.out.print("SENT: ");
        decodeMessage(translatedMessage.getMessage());
    }

    private static String translateMessage(MidiMessage sourceMessage) {
        String decodedMessage = decodeMessage(sourceMessage.getMessage());
        String translation = TRANSLATION_TABLE.get(decodedMessage);
        return translation != null ? translation : decodedMessage;
    }

    public static String decodeMessage(byte[] data) {
        int statusCode = data[0] & 0xFF;    // Convert to unsigned integer (otherwise we'll get a weird negative value)
        int dataByteOne = data[1];          // See: https://www.midi.org/specifications-old/item/table-2-expanded-messages-list-status-bytes
                                            // for more info

        if(statusCode >= CC_OFFSET && statusCode < PC_OFFSET) {             // CC instruction
            int dataByteTwo = data[2];      // Stores velocity (only relevant to CC instructions)
            int channelOffset = statusCode-CC_OFFSET+1;     // Channels start at 1 not 0 so add 1
            System.out.println("Channel: " + channelOffset + " CC Code: " + dataByteOne + " value: " + dataByteTwo);
            return statusCode + "," + dataByteOne + "," + dataByteTwo;
        } else if(statusCode >= PC_OFFSET && statusCode <= PC_OFFSET_MAX) { // PC instruction
            int channelOffset = statusCode-PC_OFFSET+1;     // Channels start at 1 not 0 so add 1
            System.out.println("Channel: " + channelOffset + " PC Code: " + dataByteOne);  // We don't care about second data byte for PC instructions
            return statusCode + "," + dataByteOne;
        }

        return null;
    }

    /**
     * Re-encodes a message into the appropriate byte array. Wrote this without realizing
     * ShortMessage is a thing so it wasn't necessary to create a new translated MidiMessage.
     * Leaving it in because it could be useful to someone.
     * @param message
     * @return byte[] representing a MIDI message
     */
    public static byte[] encodeMessage(String message) {
        String[] components = message.split(",");
        int statusCode = Integer.parseInt(components[0]);
        byte dataByteOne = Byte.parseByte(components[1]);
        // We don't know if we have the second data byte yet

        if(statusCode >= CC_OFFSET && statusCode < PC_OFFSET) {             // CC instruction
            byte dataByteTwo = Byte.parseByte(components[2]);               // Velocity
            byte statusCodeByte = (byte) statusCode;
            byte[] data = {statusCodeByte, dataByteOne, dataByteTwo};
            return data;
        } else if(statusCode >= PC_OFFSET && statusCode <= PC_OFFSET_MAX) { // PC instruction
            byte statusCodeByte = (byte) statusCode;
            byte[] data = {statusCodeByte, dataByteOne};
            return data;
        }

        return null;
    }
}
