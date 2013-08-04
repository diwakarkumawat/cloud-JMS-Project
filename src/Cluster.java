import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.PriorityBlockingQueue;

/**
 * Cluster - runs in its own Thread.
 */
public class Cluster extends Thread {

    // Number of Nodes in Cluster
    public static int CLUSTER_SIZE = 4;

    private static int index = 0;

    private ExecutorService executorService;
    private List<Node> nodes = new ArrayList<Node>();
    private PriorityBlockingQueue<Job> jobs = new PriorityBlockingQueue<Job>();

    public Cluster() {
        executorService = Executors.newFixedThreadPool(CLUSTER_SIZE);
        setDaemon(true);
        start();
        for(int i=0;i<CLUSTER_SIZE;i++) {
            nodes.add(new Node("Node-" + i));
            executorService.execute(nodes.get(i));
        }
        System.out.println("Cluster Initialized with Size - " + CLUSTER_SIZE);
    }

    public synchronized  void addJob(Job newJob) {
        jobs.add(newJob);

        // Add task to Nodes
        List<Task> tasks = newJob.getJobTasks();
        for(Task task: tasks) {
            if(index >= 4)
                index  = 0;

            nodes.get(index++).addTask(task);
        }
//        notifyAll();
    }

    public Job[] getJobs() {
        return jobs.toArray(new Job[0]);
    }

    public String getClusterLoad() {
        StringBuffer sb = new StringBuffer();
        for(Node n: nodes) {
            sb.append(n.getNodeLoad());
            sb.append("\r\n");
        }
        return sb.toString();
    }

    private void trackProgress() {
        if(!jobs.isEmpty()) {
            for(Job job: jobs)
                FileLogger.logger.info(job.getProgress());
        }
    }

    @Override
    public void run() {
        do {
            // Print Progress

//            synchronized (this) {
//                if(jobs.isEmpty())
//                    try {
//                        wait();
//                    }catch(InterruptedException ix) {
//                        ix.printStackTrace();
//                    }
//            }

            // check if complete
//            for(Job job: jobs) {
//                if(job.isComplete())
//                    jobs.remove(job);
//            }

            try {
                Thread.sleep(1000);
            } catch(InterruptedException ix) {
                ix.printStackTrace();
            }
        }while(true);
    }
}
