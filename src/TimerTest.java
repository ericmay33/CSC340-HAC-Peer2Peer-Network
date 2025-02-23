import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.security.SecureRandom;



public class TimerTest {

    //stuff for random variables
    private static SecureRandom secureRandom = new SecureRandom();
    //int variable which is assigned a random number between 0 and 30
    private static int ranNum1 = secureRandom.nextInt(31);
    private static int ranNum2 = secureRandom.nextInt(31);
    private static int ranNum3 = secureRandom.nextInt(31);
    private static int ranNum4 = secureRandom.nextInt(31);
    private static int ranNum5 = secureRandom.nextInt(31);
    private static int ranNum6 = secureRandom.nextInt(31);

    //public static void main(String[] args) {



    //     // Ngl idrk
    //     var scheduler = Executors.newScheduledThreadPool(1);

    //     // Says what to do when the task executes
    //     Runnable task = new Runnable() {
    //         @Override
    //         public void run() {

    //             System.out.println("Task has run after " + randomInRange +  " seconds.");

    //             randomInRange = secureRandom.nextInt(31);

    //             // Reschedule the task to run again after a random amount of seconds
    //             scheduler.schedule(this, randomInRange, TimeUnit.SECONDS);
    //         }
    //     };

    //     // Start the first execution of the task
    //     scheduler.schedule(task, randomInRange, TimeUnit.SECONDS); 
    // }
   // }   
    private static void singleTest() {
        // Ngl idrk
        var scheduler = Executors.newScheduledThreadPool(1);

        // Says what to do when the task executes
        Runnable task = new Runnable() {
            @Override
            public void run() {

                System.out.println("Task has run after " + ranNum1 +  " seconds.");

                ranNum1 = secureRandom.nextInt(31);

                // Reschedule the task to run again after a random amount of seconds
                scheduler.schedule(this, ranNum1, TimeUnit.SECONDS);
            }
        };

        // Start the first execution of the task
        scheduler.schedule(task, ranNum1, TimeUnit.SECONDS); 
    }

    private static void Multitest(){

        //number of timers to be created
        var scheduler = Executors.newScheduledThreadPool(6);

        // Says what to do when the task executes
        Runnable node1 = new Runnable() {
            @Override
            public void run() {

                System.out.println("node 1 sends out a \"heartbeat\" after " + ranNum1 +  " seconds.");

                ranNum1 = secureRandom.nextInt(31);

                // Reschedule the task to run again after a random amount of seconds
                scheduler.schedule(this, ranNum1, TimeUnit.SECONDS);
            }
        };

        Runnable node2 = new Runnable() {
            @Override
            public void run() {

                System.out.println("node 2 sends out a \"heartbeat\" after " + ranNum2 +  " seconds.");

                ranNum2 = secureRandom.nextInt(31);

                // Reschedule the task to run again after a random amount of seconds
                scheduler.schedule(this, ranNum2, TimeUnit.SECONDS);
            }
        };

        Runnable node3 = new Runnable() {
            @Override
            public void run() {

                System.out.println("node 3 sends out a \"heartbeat\" after " + ranNum3 +  " seconds.");

                ranNum3 = secureRandom.nextInt(31);

                // Reschedule the task to run again after a random amount of seconds
                scheduler.schedule(this, ranNum3, TimeUnit.SECONDS);
            }
        };

        Runnable node4 = new Runnable() {
            @Override
            public void run() {

                System.out.println("node 4 sends out a \"heartbeat\" after " + ranNum4 +  " seconds.");

                ranNum4 = secureRandom.nextInt(31);

                // Reschedule the task to run again after a random amount of seconds
                scheduler.schedule(this, ranNum4, TimeUnit.SECONDS);
            }
        };

        Runnable node5 = new Runnable() {
            @Override
            public void run() {

                System.out.println("node 5 sends out a \"heartbeat\" after " + ranNum5 +  " seconds.");

                ranNum5 = secureRandom.nextInt(31);

                // Reschedule the task to run again after a random amount of seconds
                scheduler.schedule(this, ranNum5, TimeUnit.SECONDS);
            }
        };

        Runnable node6 = new Runnable() {
            @Override
            public void run() {

                System.out.println("node 6 sends out a \"heartbeat\" after " + ranNum6 +  " seconds.");

                ranNum6 = secureRandom.nextInt(31);

                // Reschedule the task to run again after a random amount of seconds
                scheduler.schedule(this, ranNum6, TimeUnit.SECONDS);
            }
        };

        // Start the first execution of the task
        scheduler.schedule(node1, ranNum1, TimeUnit.SECONDS); 
        scheduler.schedule(node2, ranNum2, TimeUnit.SECONDS);
        scheduler.schedule(node3, ranNum3, TimeUnit.SECONDS); 
        scheduler.schedule(node4, ranNum4, TimeUnit.SECONDS);
        scheduler.schedule(node5, ranNum5, TimeUnit.SECONDS); 
        scheduler.schedule(node6, ranNum6, TimeUnit.SECONDS); 
    
    }

    public static void main(String[] args){
        //singleTest();
        Multitest();
    }

}

