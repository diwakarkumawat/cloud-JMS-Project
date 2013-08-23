import java.io.FileWriter;
import java.io.PrintWriter;
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
    private TaskScheduler taskScheduler = new TaskScheduler();
    private PrintWriter outJobs;
    private PrintWriter outNodes;

    public Cluster() {
        executorService = Executors.newFixedThreadPool(CLUSTER_SIZE);
        setDaemon(true);
        start();
        for(int i=0;i<CLUSTER_SIZE;i++) {
            nodes.add(new Node("Node-" + i, taskScheduler));
            executorService.execute(nodes.get(i));
        }

        try {
            outJobs = new PrintWriter(new FileWriter("cloud_jobs.txt", true));
            outNodes = new PrintWriter(new FileWriter("cloud_nodes.txt", true));
        }catch(Exception x) {
            x.printStackTrace();
        }
        System.out.println("Cluster Initialized with Size - " + CLUSTER_SIZE);
    }

    public synchronized  void raiseJob(Job jobToRaise) {
        if(jobs.contains(jobToRaise)) {
            taskScheduler.raiseJob(jobToRaise);
            Job[] allJobs = getJobs();
            for(Job job: allJobs) {
                if(job.equals(jobToRaise))
                    job.raisePriority();
            }
        } else {
            System.out.println("Job does not exist - " + jobToRaise);
        }
    }

    public synchronized void resumeJob(Job jobToResume) {
        if(jobs.contains(jobToResume)) {
//            taskScheduler.resumeJob(jobToResume);
            Job[] allJobs = getJobs();
            for(Job job: allJobs) {
                if(job.equals(jobToResume))
//                    job.resume();
                    taskScheduler.resumeJob(job);
            }
        }
    }

    public synchronized void rateAssignJob(Job rateJob, String percentRate) {
        Integer rate = Integer.parseInt(percentRate);

        if(rate <= 25) {
            // Assign 1 node
            nodes.get(0).addExclusiveJob(rateJob);
        } else if(rate <= 50) {
            // Assign 2 nodes
            nodes.get(0).addExclusiveJob(rateJob);
            nodes.get(1).addExclusiveJob(rateJob);
        } else if(rate <= 75) {
            // Assign 3 nodes
            nodes.get(0).addExclusiveJob(rateJob);
            nodes.get(1).addExclusiveJob(rateJob);
            nodes.get(2).addExclusiveJob(rateJob);

        } else if(rate <= 100) {
            // Assign 4 nodes.
            nodes.get(0).addExclusiveJob(rateJob);
            nodes.get(1).addExclusiveJob(rateJob);
            nodes.get(2).addExclusiveJob(rateJob);
            nodes.get(3).addExclusiveJob(rateJob);

        }
    }

    public synchronized void pauseJob(Job jobToPause) {
            if(jobs.contains(jobToPause)) {
            //taskScheduler.pauseJob(jobToPause);
            Job[] allJobs = getJobs();
            for(Job job: allJobs) {
                if(job.equals(jobToPause))
                    taskScheduler.pauseJob(job);
                    //job.pause();
            }
        } else {
            System.out.println("Job does not exist - " + jobToPause);
        }
    }

    public synchronized  void lowerJob(Job jobToLower) {
        if(jobs.contains(jobToLower)) {
            taskScheduler.lowerJob(jobToLower);
            Job[] allJobs = getJobs();
            for(Job job: allJobs) {
                if(job.equals(jobToLower))
                    job.lowerPriority();
            }
        } else {
            System.out.println("Job does not exist - " + jobToLower);
        }
    }

    public synchronized void killJob(Job jobToKill) {
        if(jobs.contains(jobToKill)) {
            taskScheduler.killJob(jobToKill);
            Job[] allJobs = getJobs();
            for(Job job: allJobs) {
                if(job.equals(jobToKill))
                    job.kill();
            }
        } else {
            System.out.println("Job does not exist - " + jobToKill);
        }
    }

    public synchronized  void addJob(Job newJob) {
        jobs.add(newJob);
        taskScheduler.addTasks(newJob);
    }

    public Job[] getJobs() {
        return jobs.toArray(new Job[0]);
    }

    public void printLoad() {
        System.out.println();
        System.out.printf("%-50s %70s %20s %n", "Node-Id.Client.JobName.TaskId", "Node Work Queue", "Percent Complete");

        for(Node node: nodes) {
            System.out.printf("%-50s %70s %20s %n", node.getNodeLoad(), node.getNodeWorkLoad(), node.getPercentComplete());
        }
    }

    public String getClusterLoad() {
        StringBuffer sb = new StringBuffer();
        for(Node n: nodes) {
            sb.append(n.getNodeLoad());
            sb.append(" ").append(n.getPercentComplete());
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
            try {
                Thread.sleep(250);
                outNodes.printf("%-40s %60s %20s %n", "Node-Id.Client.JobName.TaskId", "Node Work Queue", "Percent Complete");
                for(Node node: nodes) {
                    outNodes.printf("%-40s %60s %20s %n", node.getNodeLoad(), node.getNodeWorkLoad(), node.getPercentComplete());
                }

                Job[] jobs = getJobs();
                outJobs.printf("%-40s %100s %20s %n", "Client.Job.WorkLoad.Status.Priority", "Progress", "Percent Complete");

                for(Job job: jobs) {
                    outJobs.printf("%-40s %100s %20s %n", job.getJobDetail(), job.getProgress(), job.getPercentComplete());
                }

            } catch(InterruptedException ix) {
                ix.printStackTrace();
            }
        }while(true);
    }
}
