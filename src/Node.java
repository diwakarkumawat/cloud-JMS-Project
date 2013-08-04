import java.util.concurrent.PriorityBlockingQueue;

/**
 * Created with IntelliJ IDEA.
 * User: dkumawat
 * Date: 8/3/13
 * Time: 2:05 PM
 * To change this template use File | Settings | File Templates.
 */
public class Node implements Runnable {
    private String name = null;
    private String status = "NotStarted";
    private PriorityBlockingQueue<Task> taskQueue = new PriorityBlockingQueue<Task>();
    public Node(String s) {
        this.name = s;
    }

    public void addTask(Task task) {
        taskQueue.add(task);
        notifyAll();
    }


    @Override
    public void run() {

        // Simulate the work by Sleeping for Task.
        if(taskQueue.isEmpty()) {
            status = "Started.NoTask " + taskQueue.size();
            try {
                wait();
            }catch(InterruptedException ix) {
                System.out.print(ix);
            }
        }

        Task task = taskQueue.remove();
        status = "Started.Task.Execute " + taskQueue.size();

        try {
            Thread.sleep(task.getTaskWorkLoad());
        }catch(InterruptedException ix) {
            System.out.println(ix);
        }
    }
}
