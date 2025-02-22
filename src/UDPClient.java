import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class UDPClient {

    private String nodeIP;
    private ArrayList<String> knownNodes;
    private SecureRandom secureRandom;
    private ScheduledExecutorService scheduler;
    private int ranNum1;

    public UDPClient(String nodeIP) {
        this.nodeIP = nodeIP;
        this.knownNodes = new ArrayList<>();
        this.secureRandom = new SecureRandom();
        this.scheduler = Executors.newScheduledThreadPool(1);
        this.ranNum1 = secureRandom.nextInt(31);
    }

    // Load list of known nodes from config
    public void loadKnownNodes(ArrayList<String> knownNodes) {
        this.knownNodes = knownNodes;
        this.knownNodes.remove(this.nodeIP);
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
        // UDP sending logic
    }
}
