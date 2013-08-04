/**
 * Created with IntelliJ IDEA.
 * User: dkumawat
 * Date: 8/3/13
 * Time: 2:09 PM
 * To change this template use File | Settings | File Templates.
 */
public class Task {
    private String clientName;
    private String jobNumber;
    private Long taskWorkLoad;

    public Task(String clientName, String jobNumber, Long taskWorkLoad) {
        this.clientName = clientName;
        this.jobNumber = jobNumber;
        this.taskWorkLoad = taskWorkLoad;
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
}
