import java.io.File;
import java.util.Arrays;
import java.util.stream.Collectors;

public class Message {

    private String encodedMessage;
    private String version;
    private String nodeIP;
    private String timestamp;
    private String fileListing;
    private static final String DELIMITER = "::";

    public Message(String version, String nodeIP, String timestamp, String fileListing) {
        this.version = version;
        this.nodeIP = nodeIP;
        this.timestamp = timestamp;
        this.fileListing = fileListing;
        this.encodedMessage = this.encode();
    }

    private String encode() {
        return version + DELIMITER + nodeIP + DELIMITER + timestamp + DELIMITER + fileListing;
    }

    public static Message decode(String messageString) {
        String[] messageParts = messageString.split(DELIMITER, -1);
        if (messageParts.length != 4) {
            throw new IllegalArgumentException("Invalid message format");
        }
        return new Message(messageParts[0], messageParts[1], messageParts[2], messageParts[3]);
    }

    public String getEncodedMessage() {
        return encodedMessage;
    }

    public String getVersion() {
        return version;
    }

    public String getNodeIP() {
        return nodeIP;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public String getFileListing() {
        return fileListing;
    }

    public static String getCurrentFileListing() {
        File homeDir = new File(System.getProperty("user.home"));
        if (homeDir.exists() && homeDir.isDirectory()) {
            return Arrays.stream(homeDir.list()).collect(Collectors.joining(","));
        } else {
            return "";
        }
    }
}