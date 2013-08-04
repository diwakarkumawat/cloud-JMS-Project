import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: dkumawat
 * Date: 8/3/13
 * Time: 10:51 PM
 * To change this template use File | Settings | File Templates.
 */
public class Job implements Comparable {
    private String clientName;
    private String jobName;
    private Long jobWorkLoad;
    private List<Task> jobTasks = new ArrayList<Task>();

    public static int TASK_LENGTH = 10; // 10 second tasks

    public Job(String clientName, String jobName, Long jobWorkLoad) {
        this.clientName = clientName;
        this.jobName = jobName;
        this.jobWorkLoad = jobWorkLoad;
        long taskCount = jobWorkLoad/10;
        // Create Tasks
        for(int i=0;i<taskCount;i++) {
            Task task = new Task(clientName, jobName, 10L);
            jobTasks.add(task);
        }
    }

    public List<Task> getJobTasks() {
        return jobTasks;
    }

    public boolean isComplete() {
        for(Task task: jobTasks) {
            if(!task.isComplete())
                return false;
        }

        return true;
    }

    public String getProgress() {
        int completed = 0;

        int size = jobTasks.size();
        for(Task task: jobTasks)
            if(task.isComplete())
                completed++;

        float percentComplete = 100 * completed/size;

        StringBuffer sb = new StringBuffer("[" + clientName + "." + jobName + "." + jobWorkLoad + "] ");
        for(int i=0;i<percentComplete;i++)
            sb.append("|");

        for(int i=(int)percentComplete;i<100;i++)
            sb.append(".");

        return sb.toString();
    }

    @Override
    public int compareTo(Object o) {
        if( ! (o instanceof Job))
            return -1;

        Job other = (Job) o;

        if(this.clientName.equals(other.clientName) && this.jobName.equals(other.clientName))
            return 0;

        return 1;
    }
}
