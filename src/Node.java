import java.util.concurrent.PriorityBlockingQueue;

/**
 * Node implemented as a Thread.
 */
public class Node implements Runnable {
    private String name = null;
    private Task task = null;
//    private PriorityBlockingQueue<Task> taskQueue = new PriorityBlockingQueue<Task>();
    private TaskScheduler taskScheduler;
    public Node(String s, TaskScheduler taskScheduler) {
        this.name = s;
        this.taskScheduler = taskScheduler;
    }

    public synchronized void addTask(Task task) {
//        taskQueue.add(task);
//        notifyAll();
    }

    public String getNodeLoad() {
        StringBuffer sb = new StringBuffer("{" + name + "} - ");
        if(task != null)
            sb.append(task.getClientName() + "." + task.getJobNumber());
        else
            sb.append(" No Task.");

////        int size = taskQueue.size();
//        for(int i=0;i<size;i++)
//            sb.append("*");
        return sb.toString();
    }


    @Override
    public void run() {

//        synchronized (this) {
//            // Simulate the work by Sleeping for Task.
//            if(taskQueue.isEmpty()) {
//                try {
//                    wait();
//                }catch(InterruptedException ix) {
//                    System.out.print(ix);
//                }
//            }
//        }

        do {

            // Keep working on tasks
                task = taskScheduler.nextTask();
                if(task != null && !task.isComplete()) {
                    try {
                        Thread.sleep(task.getTaskWorkLoad() * 500);
                        task.setComplete();
                    }catch(InterruptedException ix) {
                        System.out.println(ix);
                    }
                }

            try {
                Thread.sleep(200);
            }catch(InterruptedException ix) {
                ix.printStackTrace();
            }
        }while(true);
    }
}
