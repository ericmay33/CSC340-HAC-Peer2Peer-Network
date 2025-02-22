import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class P2PNode {

    private String nodeIP;
    private ArrayList<String> knownNodes;
    private SecureRandom secureRandom;
    private ScheduledExecutorService scheduler;
    private int ranNum1;
    private byte version;

    public P2PNode(String nodeIP) {
        this.nodeIP = nodeIP;
        this.knownNodes = new ArrayList<>();
        this.secureRandom = new SecureRandom();
        this.scheduler = Executors.newScheduledThreadPool(1);
        this.ranNum1 = secureRandom.nextInt(31);
        this.version = 1;
    }

    // Load list of known nodes from config
    // Read config file with IPs and Port of each node
    // CREATE A NODE CLASS, CONSTRUCT THE IP AND PORT USING FILE READER FOR EACH NODE OBJECT
    // WE CAN THEN ADD NODES TO AN ARRAY LIST AND ACCESS THE PORT AND IP WHEN NEEDED LATER FOR EACH NODE
    public void loadKnownNodes() {
        // Fake nodes for testing right now
        this.knownNodes.add("1");
        this.knownNodes.add("2");
        this.knownNodes.add("3");
    }

    // Start sending heartbeats at random intervals
    public void startHeartbeatTimer() {
        Runnable task = new Runnable() {
            @Override
            public void run() {
                sendHeartbeat();
                ranNum1 = secureRandom.nextInt(31);
                scheduler.schedule(this, ranNum1, TimeUnit.SECONDS);
            }
        };

        // Initial execution
        scheduler.schedule(task, ranNum1, TimeUnit.SECONDS);
    }

    private void sendHeartbeat() {
        try {
            // Create UDPClient Socket, 
            DatagramSocket socket = new DatagramSocket();

            // Information for the application protocol (file listing and timestamp)
            int timestamp = (int) (System.currentTimeMillis() / 1000);
            String fileListing = Message.getCurrentFileListing();

            // Create the application protocol packet with correct information
            // Extract usable byte array encoded message to be sent using UDP
            Message heartbeat = new Message(version, nodeIP, timestamp, fileListing);
            byte[] byteMessage = heartbeat.getMessageBytes();

            // Send to every other node
            for (String node : knownNodes) {
                // WE CAN CREATE A NODE CLASS WITH IP AND PORT AS INSTANCE VARIABLES
                // WE CAN JUST USE GETTER METHODS TO ACCESS THE IP AND PORT OF EACH NODE WITH THE LOOP
                // String ip = 
                // int port = 

                // TESTING IP AND PORT
                String ip = "127.0.0.1";
                int port = 7000;

                // Destination IP address, create datagram packet
                InetAddress IPaddress = InetAddress.getByName(ip);
                DatagramPacket packet = new DatagramPacket(byteMessage, byteMessage.length, IPaddress, port);
                socket.send(packet);
                System.out.println("Sent heartbeat to " + ip + ":" + port + " at Unix time " + timestamp + "\n");
            }
            socket.close();
            version++;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        P2PNode thisPC = new P2PNode("127.0.0.1");
        thisPC.loadKnownNodes();
        thisPC.startHeartbeatTimer();
    }
}