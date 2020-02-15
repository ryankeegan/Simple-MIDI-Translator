public class SimpleMIDITranslator {
    public static void main(String[] args) {
        SimpleMIDITranslator thread = new SimpleMIDITranslator();
        thread.threadSleep();
    }
    public SimpleMIDITranslator() {
        // Load MIDI devices
        new MIDIHandler();
        // Load config files into memory
        // Actually capture and rebroadcast MIDI messages
    }


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
