import javax.sound.midi.MidiMessage;

public class MIDITranslate {
    private static final int CC_OFFSET = 176;
    private static final int PC_OFFSET = 192;
    private static final int PC_OFFSET_MAX = 207;

    public static void sendToTarget(MidiMessage sourceMessage, long timestamp) {
        MidiMessage translatedMessage = translateMessage(sourceMessage, timestamp);
        MIDIHandler.getTargetReceiver().send(translatedMessage, timestamp);
        System.out.print("SENT: ");
        decodeMessage(translatedMessage);
    }

    private static MidiMessage translateMessage(MidiMessage sourceMessage, long timestamp) {
        return sourceMessage;   // TODO: change
    }

    public static void decodeMessage(MidiMessage message) {
        byte[] data = message.getMessage();
        int statusCode = data[0] & 0xFF;    // Convert to unsigned integer (otherwise we'll get a weird negative value)
        int dataByteOne = data[1];          // See: https://www.midi.org/specifications-old/item/table-2-expanded-messages-list-status-bytes
        // for more info

        if(statusCode >= CC_OFFSET && statusCode < PC_OFFSET) {             // CC instruction
            int dataByteTwo = data[2];      // Stores velocity (only relevant to CC instructions)
            int channelOffset = statusCode-CC_OFFSET+1;     // Channels start at 1 not 0 so add 1
            System.out.println("Channel: " + channelOffset + " CC Code: " + dataByteOne + " value: " + dataByteTwo);
        } else if(statusCode >= PC_OFFSET && statusCode <= PC_OFFSET_MAX) { // PC instruction
            int channelOffset = statusCode-PC_OFFSET+1;     // Channels start at 1 not 0 so add 1
            System.out.println("Channel: " + channelOffset + " PC Code: " + dataByteOne);  // We don't care about second data byte for PC instructions
        }
    }
}
