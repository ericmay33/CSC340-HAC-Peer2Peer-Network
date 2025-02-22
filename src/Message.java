import java.io.File;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.stream.Collectors;

public class Message {

    // HARDCODE IP ADDRESS USING CONFIG FILE
    // TWO PROGRAMS READ AND WRITE
    // CAN USE EXECUTER SERVICE INSTEAD OF GIVEN CODE

    // Application protocol fixed field sizes represented in number of bytes
    private static final int VERSION_SIZE = 1;
    private static final int NODE_IP_SIZE = 15;
    private static final int TIMESTAMP_SIZE = 4;
    private static final int FILE_LISTING_LENGTH_SIZE = 2;
    
    // Application protocol control field instance variables
    private byte version;
    private String nodeIP;
    private int timestamp;
    private String fileListing;

    // Encoded message byte array
    private byte[] messageBytes;

    // Message Constructor
    public Message(byte version, String nodeIP, int timestamp, String fileListing) {
        this.version = version;
        this.nodeIP = nodeIP;
        this.timestamp = timestamp;
        this.fileListing = fileListing;
        this.messageBytes = this.encode();
    }

    // Create a byte array representing the application protocol message (control fields and data)
    public byte[] encode() {
        // Converts file listing into byte array using UTF-8 encoding
        // Find length of the file listing array in bytes
        byte[] fileListingBytes = fileListing.getBytes(StandardCharsets.UTF_8);
        int fileListingLength = fileListingBytes.length;

        // Creates a ByteBuffer with the application protocol fixed field sizes and file listing variable field size
        // Buffer allows us to manipulate the fields and convert them into a byte array
        ByteBuffer buffer = ByteBuffer.allocate(VERSION_SIZE + NODE_IP_SIZE + TIMESTAMP_SIZE + FILE_LISTING_LENGTH_SIZE + fileListingLength);

        // Puts the control fields and data into the buffer in the correct order determined by application protocol
        buffer.put(version);
        buffer.put(formatIPAddress(nodeIP).getBytes(StandardCharsets.UTF_8));
        buffer.putInt(timestamp);
        buffer.putShort((short) fileListingLength); // short is a 2-byte integer
        buffer.put(fileListingBytes);

        this.messageBytes = buffer.array();

        return buffer.array();
    }

    // Create a Message object from a byte array representing the application protocol message
    public static Message decode(byte[] messageBytes) {

        // Creates a ByteBuffer from the byte array to take out the control fields and data
        // Uses wrap this time because we already have the existing byte array (encoded message)
        ByteBuffer buffer = ByteBuffer.wrap(messageBytes);

        // buffer.get reads the next byte in the buffer, then moves pointer to the next byte in the buffer
        // Reads the first byte which based on our protocol was the version
        byte version = buffer.get();

        // Reads the next 15 bytes which is the node IP address
        // Converts the array of bytes (each byte is a character in UTF-8 encoding) into a string, then removes the padding
        byte[] nodeIPBytes = new byte[NODE_IP_SIZE];
        buffer.get(nodeIPBytes);
        String nodeIP = new String(nodeIPBytes, StandardCharsets.UTF_8).trim();

        // Reads the next 4 bytes which is the timestamp (integer is 4 bytes)
        int timestamp = buffer.getInt();

        // Reads the next 2 bytes which is the file listing length (short is 2 bytes)
        int fileListingLength = buffer.getShort();

        // Reads the next n bytes (n = fileListingLength) which is the actual file listings of the source node's home directory
        // Converts the array of bytes (each byte is a character in UTF-8 encoding) into a string, no need for removing padding
        byte[] fileListingBytes = new byte[fileListingLength];
        buffer.get(fileListingBytes);
        String fileListing = new String(fileListingBytes, StandardCharsets.UTF_8);

        return new Message(version, nodeIP, timestamp, fileListing);
    }

    // IP addresses can vary in length
    // This method ensures that the IP address is always 15 bytes long (IPv4 maximum length)
    private static String formatIPAddress(String ip) {
        // Calculates difference in length between current ip length and the predetermined 15 bytes ip length
        int paddingLengthNeeded = 15 - ip.length();

        // Creates a StringBuilder object with the current IP address so we can add spaces to the string easily
        StringBuilder sb = new StringBuilder(ip);

        // Adds padding (spaces) to the end of the IP address to make it 15 bytes long
        while (paddingLengthNeeded > 0) {
            sb.append(" ");
            paddingLengthNeeded--;
        }
        return sb.toString();
    }

    public byte getVersion() {
        return version;
    }

    public String getNodeIP() {
        return nodeIP;
    }

    public int getTimestamp() {
        return timestamp;
    }

    public String getFileListing() {
        return fileListing;
    }

    public byte[] getMessageBytes() {
        return messageBytes;
    }

    public static String getCurrentFileListing() {
        File homeDir = new File("home");
        if (homeDir.exists() && homeDir.isDirectory()) {
            return Arrays.stream(homeDir.list()).collect(Collectors.joining(","));
        } else {
            return "";
        }
    }
}