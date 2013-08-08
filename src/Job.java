import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created with IntelliJ IDEA.
 * User: dkumawat
 * Date: 8/3/13
 * Time: 10:51 PM
 * To change this template use File | Settings | File Templates.
 */
public class Job implements Comparable<Job> {
    private String clientName;
    private String jobName;
    private Long jobWorkLoad;
    private Status status = Status.NOT_STARTED;
    private Priority priority = Priority.EQUAL;
    private List<Task> jobTasks = new ArrayList<Task>();
    private static Random random = new Random(78938193L);

    public static int TASK_LENGTH = 10; // 10 second tasks

    public Job(String clientName, String jobName, Long jobWorkLoad) {
        this.clientName = clientName;
        this.jobName = jobName;
        if(jobWorkLoad == null) // for non-work jobs, it can be missing
            jobWorkLoad = 0l;
        this.jobWorkLoad = jobWorkLoad;
        // Create Tasks
        long assignedWorkLoad = 0;
        int i = 1;
        for(;assignedWorkLoad<jobWorkLoad;) {
            long nextExpLoad = nextExpDistibutedLoad();
            if(nextExpLoad > (jobWorkLoad - assignedWorkLoad))
                nextExpLoad = jobWorkLoad - assignedWorkLoad;
            Task task = new Task(clientName, jobName, i++, nextExpLoad);
            assignedWorkLoad += task.getTaskWorkLoad();
            jobTasks.add(task);
        }
    }

    /**
     * Return real number uniformly in [0, 1).
     */
    public static double uniform() {
        return random.nextDouble();
    }

    /**
     * Return a real number from an exponential distribution with rate lambda.
     */
    public static double exp(double lambda) {
        return -Math.log(1 - uniform()) / lambda;
    }

    public static long nextExpDistibutedLoad() {
        long random = Math.round(10 * exp(1));
        return random > 0 ? random : 1;
    }

    public static void main(String [] args) throws Exception {
        for(int i=100;i<1000;i++) {
            System.out.println(nextExpDistibutedLoad());
        }
    }

    public Priority getPriority() {
        return priority;
    }

    public String getClientName() {
        return clientName;
    }

    public String getJobName() {
        return jobName;
    }

    public List<Task> getJobTasks() {
        return jobTasks;
    }

    public void raisePriority() {
        if(priority.equals(Priority.LOW))
            priority = Priority.EQUAL;
        else if(priority.equals(Priority.EQUAL))
            priority = Priority.HIGH;
    }

    public void pause() {
        status = Status.PAUSED;
    }

    public void resume() {
        status = Status.STARTED;
    }

    public void lowerPriority() {
        if(priority.equals(Priority.HIGH))
            priority = Priority.EQUAL;
        else if(priority.equals(Priority.EQUAL))
            priority = Priority.LOW;
    }

    public void kill() {
        status = Status.KILLED;
    }

    public boolean isComplete() {

        if(status.equals(Status.PAUSED))
            return false;

        if(status.equals(Status.KILLED))
            return true;

        for(Task task: jobTasks) {
            if(!task.isComplete()) {
                status = Status.STARTED;
                return false;
            }
        }

        status = Status.COMPLETED;
        return true;
    }

    public String getJobDetail() {
        StringBuffer sb = new StringBuffer("[" + clientName + "." + jobName + "." + jobWorkLoad +
                "." + status.name() + "." + priority.name() +"] ");

        return sb.toString();
    }

    public String getProgress() {
        isComplete();
        int completed = 0;

        int size = jobTasks.size();
        for(Task task: jobTasks)
            if(task.isComplete())
                completed++;

        float percentComplete = 100 * completed/size;


        StringBuffer sb = new StringBuffer();

        int i = 0;
        for(;i<percentComplete;i++)
            sb.append("|");

        for(;i<100;i++)
            sb.append(".");

        return sb.toString();
    }

    public String getPercentComplete() {
        int size = jobTasks.size();
        int completed = 0;
        for(Task task: jobTasks)
            if(task.isComplete())
                completed++;

        float percentComplete = 100 * completed/size;

        StringBuffer sb = new StringBuffer();
        if(status.equals(Status.KILLED) || status.equals(Status.COMPLETED) || status.equals(Status.PAUSED))
            sb.append(status.name());
        else
            sb.append(" " + percentComplete + "%");

        return sb.toString();
    }

    @Override
    public int compareTo(Job other) {

        if(this.clientName.equals(other.clientName) && this.jobName.equals(other.jobName))
            return 0;

        return 1;
    }

    @Override
    public boolean equals(Object o) {
        Job other = (Job) o;

        return this.clientName.equals(other.clientName) && this.jobName.equals(other.jobName);
    }

    @Override
    public String toString() {
        return "Job{" +
                "clientName='" + clientName + '\'' +
                ", jobName='" + jobName + '\'' +
                '}';
    }

    public int hashCode() {
        return clientName.hashCode() + jobName.hashCode();
    }
}
