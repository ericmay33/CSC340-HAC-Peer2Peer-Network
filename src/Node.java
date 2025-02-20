import java.util.ArrayList;
import java.util.HashMap;
import java.security.SecureRandom;
import java.util.Timer;
import java.util.TimerTask;

public class Node {

    private String nodeIP;
    private ArrayList<String> knownNodes;
    private HashMap<String, Integer> lastHeartbeats;
    private SecureRandom random;
    private Timer timer;
    private HashMap<String, ArrayList<String>> nodeFileLists;

    public Node(String nodeIP) {
        this.nodeIP = nodeIP;
        this.knownNodes = new ArrayList<>();
        this.lastHeartbeats = new HashMap<>();
        this.random = new SecureRandom();
        this.timer = new Timer();
        this.nodeFileLists = new HashMap<>();
    }

    // knownNodes is a list of IP addresses of other nodes in the network
    public void loadKnownNodes(ArrayList<String> knownNodes) {
        this.knownNodes = knownNodes;
        this.knownNodes.remove(this.nodeIP);
    }

    public void startHeartbeatTimer() {
        
        // PIERCE TIMER
    }

    private void sendHeartbeat() {
        // Sends a heartbeat message to all known nodes
    }

    private void receiveMessage(String message) {
        // Receives a message from another node
    }

    private void checkNodeHealth() {
        // Checks if any nodes have not sent a heartbeat in the last 30 seconds
    }

    private void updateNodeFileLists() {
        // Updates the file lists of all known nodes
    }

    public ArrayList<String> getKnownNodes() {
        return knownNodes;
    }
}
