import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.PriorityBlockingQueue;

/**
 * Main Task Scheduler
 * Maintains a Queue for Each Client. Ensures Fair Task assignment for Task Scheduling.
 */
public class TaskScheduler {
    private Map<String, PriorityBlockingQueue<Task>> mapClientToTaskList = new HashMap<String, PriorityBlockingQueue<Task>>();
    private List<String> keys = new ArrayList<String>();
    private static int index = 0;

    public TaskScheduler(){}

    /**
     * Bump up all Tasks to front of the work Q
     * @param job
     */
    public synchronized  void raiseJob(Job job) {

    }

    /**
     * Take all the tasks for job to back of the q
     * @param job
     */
    public synchronized void lowerJob(Job job) {

    }

    public synchronized  void killJob(Job job) {
        mapClientToTaskList.remove(getKey(job));
        keys.remove(getKey(job));
        index--;
    }

    public synchronized  void addTasks(Job job) {

        if(mapClientToTaskList.containsKey(getKey(job))) {
            // Add to the same Entry
            mapClientToTaskList.get(getKey(job)).addAll(job.getJobTasks());
        } else {
            PriorityBlockingQueue<Task> newTaskList = new PriorityBlockingQueue<Task>();
            newTaskList.addAll(job.getJobTasks());
            mapClientToTaskList.put(getKey(job), newTaskList);
            keys.add(getKey(job));
        }
    }

    /**
     * Return the new Task in a fair Manner.
     * @return next Task
     */
    public synchronized Task nextTask() {
        if(mapClientToTaskList.isEmpty())
            return null;

        // reset
        if(index >= keys.size() || index < 0)
            index = 0;

        // Get next key
        String key = keys.get(index++);

        // Get task list
        PriorityBlockingQueue<Task> taskList = mapClientToTaskList.get(key);

        if(taskList == null || taskList.isEmpty()) {
            // Completed.
            keys.remove(key);
            index--;
            mapClientToTaskList.remove(key);
            return null;
        }

        // Remove the task
        return taskList.remove();
    }


    private String getKey(Job job) {
        return new StringBuffer().append(job.getClientName()).append(".").append(job.getJobName()).toString();
    }
}
