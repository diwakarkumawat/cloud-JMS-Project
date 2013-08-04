import java.util.concurrent.PriorityBlockingQueue;

/**
 * Node implemented as a Thread.
 */
public class Node implements Runnable {
    private String name = null;
    private PriorityBlockingQueue<Task> taskQueue = new PriorityBlockingQueue<Task>();
    public Node(String s) {
        this.name = s;
    }

    public synchronized void addTask(Task task) {
        taskQueue.add(task);
//        notifyAll();
    }

    public String getNodeLoad() {
        StringBuffer sb = new StringBuffer("{" + name + "} ");
        int size = taskQueue.size();
        for(int i=0;i<size;i++)
            sb.append("*");
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
            if(!taskQueue.isEmpty()) {
                Task task = taskQueue.remove();
                if(task.isComplete())
                    return;

                try {
                    Thread.sleep(task.getTaskWorkLoad() * 1000);
                    task.setComplete();
                }catch(InterruptedException ix) {
                    System.out.println(ix);
                }
            }
            try {
                Thread.sleep(500);
            }catch(InterruptedException ix) {
                ix.printStackTrace();
            }
        }while(true);
    }
}
