import javax.sound.midi.MidiMessage;
import javax.sound.midi.Receiver;

public class SourceReceiver implements Receiver {
    public String fName;

    public SourceReceiver(String name) {
        fName = name;
    }

    public void send(MidiMessage message, long timeStamp) {
        System.out.println("MIDI Message Received: " + message.getMessage());
    }

    public void close() {}
}
