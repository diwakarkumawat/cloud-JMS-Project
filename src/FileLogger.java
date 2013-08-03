import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 */
public class FileLogger {

    private static final Logger logger = Logger.getLogger(FileLogger.class.getName());
    private FileHandler fileHandler;

    public FileLogger() {
        /*
         * This is one option using the FileHandler in the constructor
         * for the class. This will work if we need to instantiate
         * the class before use. In the case of a class where the main()
         * method is located we should consider a separate case in
         * the main method to initialize the handler. (See below)
         */
        addFileHandler(logger);
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        logger.info("main() method called.");

        /*
         * Instantiating the class with actually create two
         * FileHandlers.
         */
        FileLogger le = new FileLogger();

        /*
         * Anonymous inner class method for adding a FileHandler
         * in main() method.
         */
        try {
            logger.info("Adding FileHandler");
            logger.addHandler(new FileHandler(FileLogger.class.getName()
                    + ".main.log"));
            logger.info("Added FileHandler to Logger");
        } catch (IOException ex) {
            logger.log(Level.SEVERE, null, ex);
        } catch (SecurityException ex) {
            logger.log(Level.SEVERE, null, ex);
        }

        // Example logging.
        logger.info("Logging for fun and profit");
        logger.fine("Fine...");
        logger.finer("Finer...");
        logger.finest("Finest...");
        logger.warning("Warning...");
        logger.severe("Severe...");
        logger.info("Logging complete.");
    }

    /**
     * Add a <code>FileHandler</code> to the provided <code>Logger</code>.
     * @param logger <code>Logger</code> to add <code>FileHandler</code>.
     */
    private void addFileHandler(Logger logger) {
        try {
            fileHandler = new FileHandler(FileLogger.class.getName() + ".log");
        } catch (IOException ex) {
            logger.log(Level.SEVERE, null, ex);
        } catch (SecurityException ex) {
            logger.log(Level.SEVERE, null, ex);
        }
        logger.addHandler(fileHandler);
    }
}
