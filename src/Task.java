/**
 * Created with IntelliJ IDEA.
 * User: dkumawat
 * Date: 8/3/13
 * Time: 2:09 PM
 * To change this template use File | Settings | File Templates.
 */
public class Task implements Comparable<Task> {
    private String clientName;
    private String jobNumber;
    private Long taskWorkLoad;
    private Integer taskId;
    private Status status = Status.NOT_STARTED;

    public Task(String clientName, String jobNumber, int taskId, Long taskWorkLoad) {
        this.clientName = clientName;
        this.jobNumber = jobNumber;
        this.taskId = taskId;
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

    public Integer getTaskId() {
        return taskId;
    }

    public Long getTaskWorkLoad() {
        return taskWorkLoad;
    }

    @Override
    public boolean equals(Object o) {
        Task other = (Task) o;

        return this.clientName.equals(other.clientName) && this.jobNumber.equals(other.jobNumber)
                && this.taskId == other.taskId;
    }


    @Override
    public int compareTo(Task other) {

        if(this.clientName.equals(other.clientName) && this.jobNumber.equals(other.jobNumber)
                && this.taskId == other.taskId)
            return 0;

        return 1;
    }

    public int hashCode() {
        return clientName.hashCode() + jobNumber.hashCode() + taskId.hashCode();
    }

}
