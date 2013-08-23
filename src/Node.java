import java.util.concurrent.PriorityBlockingQueue;

/**
 * Node implemented as a Thread.
 */
public class Node implements Runnable {
    private String name = null;

    // Job which is guaranteed this Node exclusively
    private Job rateGuaranteedJob = null;

    private Task task = null;
    private TaskScheduler taskScheduler;
    public Node(String s, TaskScheduler taskScheduler) {
        this.name = s;
        this.taskScheduler = taskScheduler;
    }


    public void addExclusiveJob(Job job) {
        rateGuaranteedJob = job;
    }

    public String getNodeLoad() {
        StringBuffer sb = new StringBuffer("{" + name + "}: ");
        if(task != null)
            sb.append(task.getClientName() + "." + task.getJobNumber() + "." + task.getTaskId());
        else
            sb.append(" No Task.");

        return sb.toString();
    }

    public String getNodeWorkLoad() {
        StringBuffer sb = new StringBuffer();
        if(task != null) {
            long load = Math.round(task.getTaskWorkLoad() * (1-percentComplete/100));
            for(int i=0;i< load;i++)
                sb.append(".");
        }

        return sb.toString();
    }

    public String getPercentComplete() {
        return String.valueOf(percentComplete) + "%";
    }

    private float percentComplete = 0;

    @Override
    public void run() {
        do {
        // Keep working on tasks
            if(null != rateGuaranteedJob)
                task = taskScheduler.nextTask(rateGuaranteedJob);
            else
                task = taskScheduler.nextTask();

            if(null == task) {
                addExclusiveJob(null);
            }

            if(task != null && !task.isComplete()) {
                percentComplete = 0;
                long size = task.getTaskWorkLoad();

                try {
                    for(int i=1;i<=size;i++) {
                        Thread.sleep(1000);
                        percentComplete = 100 * i/(size);
                    }
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
