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
    private Map<String, Priority> priorityMap =  new HashMap<String, Priority>();
    private Map<Priority, List<String>> jobsByPriorityMap = new HashMap<Priority, List<String>>();
    private static int index = 0;

    public TaskScheduler(){
        jobsByPriorityMap.put(Priority.HIGH, new ArrayList<String>());
        jobsByPriorityMap.put(Priority.EQUAL, new ArrayList<String>());
        jobsByPriorityMap.put(Priority.LOW, new ArrayList<String>());
    }

    /**
     * Bump up all Tasks to front of the work Q
     * @param job
     */
    public synchronized  void raiseJob(Job job) {
        Priority priority = priorityMap.get(getKey(job));
        if(priority.equals(Priority.HIGH)) {
            // can't go higher then that
        } else if(priority.equals(Priority.EQUAL)) {
            priorityMap.remove(getKey(job));
            priorityMap.put(getKey(job), Priority.HIGH);
            jobsByPriorityMap.get(Priority.EQUAL).remove(getKey(job));
            jobsByPriorityMap.get(Priority.HIGH).add(getKey(job));
        } else if(priority.equals(Priority.LOW)) {
            priorityMap.remove(getKey(job));
            priorityMap.put(getKey(job), Priority.EQUAL);
            jobsByPriorityMap.get(Priority.LOW).remove(getKey(job));
            jobsByPriorityMap.get(Priority.EQUAL).add(getKey(job));
        }
    }

    /**
     * Take all the tasks for job to back of the q
     * @param job
     */
    public synchronized void lowerJob(Job job) {
        Priority priority = priorityMap.get(getKey(job));
        if(priority.equals(Priority.HIGH)) {
            priorityMap.remove(getKey(job));
            priorityMap.put(getKey(job), Priority.EQUAL);
            jobsByPriorityMap.get(Priority.HIGH).remove(getKey(job));
            jobsByPriorityMap.get(Priority.EQUAL).add(getKey(job));
        } else if(priority.equals(Priority.EQUAL)) {
            priorityMap.remove(getKey(job));
            priorityMap.put(getKey(job), Priority.LOW);
            jobsByPriorityMap.get(Priority.EQUAL).remove(getKey(job));
            jobsByPriorityMap.get(Priority.LOW).add(getKey(job));
        } else if(priority.equals(Priority.LOW)) {
            // can't go lower
        }
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
            priorityMap.put(getKey(job), job.getPriority());
            jobsByPriorityMap.get(job.getPriority()).add(getKey(job));
        }
    }

    /**
     * Return the new Task in a fair Manner.
     * @return next Task
     */
    public synchronized Task nextTask() {
        if(mapClientToTaskList.isEmpty())
            return null;

        if(index < 0)
            index = 0;

        String key;
        if(jobsByPriorityMap.get(Priority.HIGH).size() > 0) {
            int serialEnd = jobsByPriorityMap.get(Priority.HIGH).size();
            if(index >= serialEnd)
                index = 0;

            key = jobsByPriorityMap.get(Priority.HIGH).get(index++);
        } else if(jobsByPriorityMap.get(Priority.EQUAL).size() > 0) {
            int serialEnd = jobsByPriorityMap.get(Priority.EQUAL).size();
            if(index >= serialEnd)
                index = 0;

            key = jobsByPriorityMap.get(Priority.EQUAL).get(index++);
        } else if(jobsByPriorityMap.get(Priority.LOW).size() > 0) {
            int serialEnd = jobsByPriorityMap.get(Priority.LOW).size();
            if(index >= serialEnd)
                index = 0;

            key = jobsByPriorityMap.get(Priority.LOW).get(index++);
        } else {
            return null;
        }

        // Get task list
        PriorityBlockingQueue<Task> taskList = mapClientToTaskList.get(key);

        if(taskList == null || taskList.isEmpty()) {
            // Completed.
            keys.remove(key);
            index--;
            mapClientToTaskList.remove(key);
            // remove from priority maps
            Priority priority = priorityMap.get(key);
            if(priority != null)
                jobsByPriorityMap.get(priority).remove(key);
            priorityMap.remove(key);
            return null;
        }

        // Remove the task
        return taskList.remove();
    }


    private String getKey(Job job) {
        return new StringBuffer().append(job.getClientName()).append(".").append(job.getJobName()).toString();
    }
}
