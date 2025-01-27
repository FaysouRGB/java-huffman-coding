import com.faycal.huffman.Huffman;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {
    private static final Logger LOGGER = Logger.getLogger(Main.class.getName());
    private static final String USAGE_MESSAGE = "Usage: java Main [-d] <file>";

    public static void main(String[] args) {
        try {
            validateArgs(args);
            boolean decompress = args[0].equals("-d");
            String filename = args[decompress ? 1 : 0];

            validateFile(filename);

            String text = Huffman.readFile(filename);

            if (decompress) {
                text = Huffman.decompress(text);
            } else {
                text = Huffman.compress(text);
            }

            System.out.println(text);
        } catch (IllegalArgumentException | IOException e) {
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
        }
    }

    private static void validateArgs(String[] args) {
        if (args.length < 1 || args.length > 3) {
            throw new IllegalArgumentException(USAGE_MESSAGE);
        }
    }

    private static void validateFile(String filename) throws IOException {
        if (!Files.exists(Paths.get(filename))) {
            throw new IOException("File not found: " + filename);
        }
    }
}
