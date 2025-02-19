public class Main {
    public static void main(String[] args) {
        String mes = Message.getCurrentFileListing();
        Message message = new Message("1.0", "124.0.0.10", "1234567890", mes);
        System.out.println();
        System.out.println(message.getFileListing() + "\n");
        System.out.println(message.getEncodedMessage());
    }
}