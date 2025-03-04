import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.HashMap;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class P2PNode {

    private String nodeIP;
    private ArrayList<Node> knownNodes;
    private SecureRandom secureRandom;
    private ScheduledExecutorService scheduler;
    private int ranNum1;
    private byte version;
    private static String myIP;
    private int portNum;
    HashMap<String, Message> ipAndMSG = new HashMap<>();
    HashMap<String, Boolean> nodeUp = new HashMap<>();
    //private boolean nodeUp;
    public P2PNode() {
        this.knownNodes = new ArrayList<>();
        this.secureRandom = new SecureRandom();
        this.scheduler = Executors.newScheduledThreadPool(1);
        this.ranNum1 = secureRandom.nextInt(31);
        this.version = 1;
        this.portNum = 0;
        // knownNodes.add(new Node(nodeIP, 7000));
    }

    

    // Load list of known nodes from config
    // Read config file with IPs and Port of each node
    // CREATE A NODE OBJECT FOR EACH OF THE OTHER NODES AND ADD TO THIS ARRAY LIST
    public void loadKnownNodes() {
        String filePath = ".config"; // Path to your .config file
        try {
            // Get the local IP address
            String myIP = InetAddress.getLocalHost().getHostAddress();
            System.out.println("Your IP: " + myIP);

            // Read and compare each line from the .config file
            try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
                String line;
                String[] splitLine;
                String lineIP;
                int linePort;
                int lineNumber = 0;
                boolean foundMatch = false;

                // Loop through all lines in the file
                while ((line = reader.readLine()) != null) {
                    lineNumber++;
                    line = line.replaceAll("\\s+", "");
                    splitLine = line.split(":");
                    lineIP = splitLine[0];
                    linePort = Integer.parseInt(splitLine[1]);
                    // Compare the current line with myIP
                    if (myIP.equals(lineIP)) { // .trim() removes leading/trailing whitespace
                        System.out.println(" ............................... \n| Match found at line " + lineNumber + ": " + line + " |\n ............................... ");
                        foundMatch = true;
                        this.nodeIP = myIP;
                        this.portNum = linePort;
                    } else {
                        System.out.println(" ............................... \n| No match at line " + lineNumber + ": " + line + " |\n ............................... ");
                        knownNodes.add(new Node(lineIP, linePort));
                    }
                }

                if (!foundMatch && lineNumber > 0) {
                    System.out.println("No matching IP found in the config file.");
                    this.nodeIP = "127.0.0.1";
                } else if (lineNumber == 0) {
                    System.out.println("The config file is empty.");
                }

            } catch (IOException e) {
                System.out.println("Error reading file: " + e.getMessage());
            }

        } catch (UnknownHostException e) {
            System.out.println("Could not determine local IP address: " + e.getMessage());
        }
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

            //update p2p node in hashmap
            ipAndMSG.put(myIP,heartbeat);
            nodeUp.put(myIP,true);

            // Send to every other node
            for (Node node : knownNodes) {
                // WE CAN CREATE A NODE CLASS WITH IP AND PORT AS INSTANCE VARIABLES
                // WE CAN JUST USE GETTER METHODS TO ACCESS THE IP AND PORT OF EACH NODE WITH THE LOOP
                // String ip = 
                // int port = 

                // TESTING IP AND PORT
                String ip = node.getIP();
                int port = node.getPort();

                // Destination IP address, create datagram packet
                InetAddress IPaddress = InetAddress.getByName(ip);
                DatagramPacket packet = new DatagramPacket(byteMessage, byteMessage.length, IPaddress, port);
                socket.send(packet);
                System.out.println("  --------------------------------------------------------  \n| Sent heartbeat to " + ip + ":" + port + " at Unix time " + timestamp + " |\n  --------------------------------------------------------  \n");
            }
            socket.close();
            version++;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void listenForHeartbeat() {
        Thread receiveThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    // Bind to a specific port (7000 for testing)
                    DatagramSocket socket = new DatagramSocket(portNum);
                    byte[] incomingData = new byte[5120];

                    System.out.println("  --------------------------------------------------  \n| Listening for heartbeats on " + nodeIP + ":" + portNum + "... |\n  --------------------------------------------------  \n");
                    
                    while (true) {
                        DatagramPacket incomingPacket = new DatagramPacket(incomingData, incomingData.length);
                        socket.receive(incomingPacket);

                        // Pass the full buffer because decode() stops where it should
                        // Message knows the lengths of the fields and data
                        Message receivedMessage = Message.decode(incomingPacket.getData());
                        //InetAddress IPAddress = incomingPacket.getAddress();
                        //int port = incomingPacket.getPort();
                        
                        System.out.println("  ----------------------------------------------------------------------------------------------------------------------  \n| Received message from client: " + receivedMessage.getNodeIP() +
                                          " - Version: " + receivedMessage.getVersion() +
                                          ", Timestamp: " + receivedMessage.getTimestamp() +
                                          ", Files: " + receivedMessage.getFileListing() + " |\n  ----------------------------------------------------------------------------------------------------------------------  \n");

                        //UPDATE HASHMAP VARIABLES
                        ipAndMSG.put(receivedMessage.getNodeIP(), receivedMessage);
                        nodeUp.put(receivedMessage.getNodeIP(), true);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        receiveThread.setDaemon(true);
        receiveThread.start();
    }

    public void listingUpdate () {
        //make it so that this runs every 30 secs running through every node.
        Runnable task = new Runnable() {
            @Override
            public void run() {
                System.out.println("######################\n# NODE AVAILABILITY: #\n######################\n");
                int upOrDownTimestamp = (int) (System.currentTimeMillis() / 1000);
                for (String key : ipAndMSG.keySet()) {
                    Message message = ipAndMSG.get(key);
                    if(upOrDownTimestamp > message.getTimestamp() + 30) {
                        nodeUp.put(message.getNodeIP(),false);
                    }
                    if(nodeUp.get(key)){
                        System.out.println("  -------------------------------------------------------------  \n| IP: " + message.getNodeIP() + " - IS ALIVE " + " FileListing: " + message.getFileListing() + " |\n  -------------------------------------------------------------  ");
                    }
                    else{
                        System.out.println("  -------------------------------------------------------------  \n| IP: " + message.getNodeIP() + " - IS NOT ALIVE " + " FileListing: " + message.getFileListing() + " |\n  -------------------------------------------------------------  ");
                    }
                }
                // String fileListing = Message.getCurrentFileListing();
                // System.out.println("IP: " + ipAndMSG + " FileListing: " + fileListing);
                
                scheduler.schedule(this, 30, TimeUnit.SECONDS);
            }
        };

        // Initial execution
        scheduler.schedule(task, 30, TimeUnit.SECONDS);
    }



    public static void main(String[] args) {
        P2PNode thisPC = new P2PNode();

        thisPC.loadKnownNodes();
        thisPC.listenForHeartbeat();
        thisPC.startHeartbeatTimer();
        thisPC.listingUpdate();
        
    }
}