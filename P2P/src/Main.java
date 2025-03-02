public class Main {
    public static void main(String[] args) {
        String fileListString = Message.getCurrentFileListing();
        System.out.println();
        System.out.println(fileListString + "\n");

        Message message = new Message((byte) 1, "255.0.0.1", 12345, fileListString);

        byte[] encodedMessage = message.getMessageBytes();

        Message decodedMessage = Message.decode(encodedMessage);

        System.out.println("Version: " + message.getVersion());
        System.out.println("Node IP: " + message.getNodeIP());
        System.out.println("Timestamp: " + message.getTimestamp());
        System.out.println("File Listing: " + message.getFileListing() + "\n");

        if (fileListString.equals(decodedMessage.getFileListing())) {
            System.out.println("Test Passed: File listing matches!");
        } else {
            System.out.println("Test Failed: File listing mismatch.");
        }
    }
}