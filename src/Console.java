import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

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
