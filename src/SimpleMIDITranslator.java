public class SimpleMIDITranslator {
    private static final String PATH_TO_CONFIG = "./config";    // Relative to the location of the JAR file, points
                                                                // to where the translation table is located

    public static void main(String[] args) {
        SimpleMIDITranslator thread = new SimpleMIDITranslator();
        thread.threadSleep();
    }
    private SimpleMIDITranslator() {
        // Load config files into memory
        MIDITranslate.loadTranslationTable(PATH_TO_CONFIG);
        // Load MIDI devices
        new MIDIHandler();
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
