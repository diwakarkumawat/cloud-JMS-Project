import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;
import java.util.StringTokenizer;

/**
 * Created with IntelliJ IDEA.
 * User: dkumawat
 * Date: 8/3/13
 * Time: 12:32 PM
 * To change this template use File | Settings | File Templates.
 */
public class CloudDemo {

    static Cluster cluster;

    public static void main(String [] args) throws Exception {

    String eoi = null;// end of input
    BufferedReader br = (new BufferedReader(new InputStreamReader(System.in)));
    String input = null;
    boolean continued = true;
    FileLogger fileLogger = new FileLogger();
    cluster = new Cluster();
    do {
        printMenu();
        //eoi = c.readLine("Enter input (type end to End Program): ");
        System.out.print("Enter input (type end to End Program): ");
        eoi = br.readLine();
        int command = -1;
        try {
            command = Integer.parseInt(eoi);
        }catch(NumberFormatException nfx) {
            print("Invalid Command. Try Again.");
            continue;
        }

        if(command < 0 || command > 6) {
            print("Invalid Command. Try Again.");
            continue;
        }

        if(command == 6) {
            print(cluster.getClusterLoad());
        }

        if(command == 5) {
            progress();
        }

        if(command == 0) {
            print("Shutting Off JMS. GoodBye.");
            continued = false;
            break;
        }

        if(command == 1) {
            print("Enter a New Job...");
            input = br.readLine();
            StringTokenizer stk = new StringTokenizer(input);
            cluster.addJob(new Job(stk.nextToken(), stk.nextToken(), Long.parseLong(stk.nextToken())));
        }

    }while(continued);

    }

    private static void progress() {
        Job[] jobs = cluster.getJobs();
        for(Job job: jobs) {
            print(job.getProgress());
        }
    }

    private static void print(String msg) {
        System.out.println(msg);
    }

    private  static void printMenu() {
        print("");
        print("-------------- Cloud JMS System Menu -----------------");
        print("0. Exit");
        print("1. Enter a New Job {Client JobName WorkLoad}");
        print("2. Kill an existing Job {Client JobName Kill}");
        print("3. Check Current Job Statuses");
        print("4. Change Job Priority {Client JobName Priority(LOW|HIGH)}");
        print("5. Print Progress");
        print("6. Print Cluster Load");
        print("-------------------------------------------------------");
    }
}
