import java.util.ArrayList;
import java.util.List;

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

    public static int TASK_LENGTH = 10; // 10 second tasks

    public Job(String clientName, String jobName, Long jobWorkLoad) {
        this.clientName = clientName;
        this.jobName = jobName;
        if(jobWorkLoad == null) // for non-work jobs, it can be missing
            jobWorkLoad = 0l;
        this.jobWorkLoad = jobWorkLoad;
        long taskCount = jobWorkLoad/10;
        // Create Tasks
        for(int i=0;i<taskCount;i++) {
            Task task = new Task(clientName, jobName, 10L);
            jobTasks.add(task);
        }
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
        priority = Priority.HIGH;
    }

    public void lowerPriority() {
        priority = Priority.LOW;
    }

    public void kill() {
        status = Status.KILLED;
    }

    public boolean isComplete() {

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

    public String getProgress() {
        isComplete();
        int completed = 0;

        int size = jobTasks.size();
        for(Task task: jobTasks)
            if(task.isComplete())
                completed++;

        float percentComplete = 100 * completed/size;

        StringBuffer sb = new StringBuffer("[" + clientName + "." + jobName + "." + jobWorkLoad +
                 "." + status.name() + "." + priority.name() +"] ");

        int i = 0;
        for(;i<percentComplete;i++)
            sb.append("|");

        for(;i<100;i++)
            sb.append(".");

        sb.append(status.name());

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
