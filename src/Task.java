/**
 * Created with IntelliJ IDEA.
 * User: dkumawat
 * Date: 8/3/13
 * Time: 2:09 PM
 * To change this template use File | Settings | File Templates.
 */
public class Task implements Comparable {
    private String clientName;
    private String jobNumber;
    private Long taskWorkLoad;
    private Status status = Status.NOT_STARTED;

    public Task(String clientName, String jobNumber, Long taskWorkLoad) {
        this.clientName = clientName;
        this.jobNumber = jobNumber;
        this.taskWorkLoad = taskWorkLoad;
    }

    public Status getStatus() {
        return status;
    }

    public void setComplete() {
        status = Status.COMPLETED;
    }

    public boolean isComplete() {
        return status.equals(Status.COMPLETED);
    }

    public String getClientName() {
        return clientName;
    }

    public String getJobNumber() {
        return jobNumber;
    }

    public Long getTaskWorkLoad() {
        return taskWorkLoad;
    }

    @Override
    public int compareTo(Object o) {
        if(! (o instanceof  Task))
            return -1;

        Task other = (Task) o;

        if(this.clientName.equals(other.clientName) && this.jobNumber.equals(other.jobNumber))
            return 0;

        return 1;
    }
}
