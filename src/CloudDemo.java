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
        System.out.print("Enter Menu Number (type 0 to End Program): ");
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
            //print(cluster.getClusterLoad());
            cluster.printLoad();
        }

        if(command == 5) {
            progress();
        }

        if(command == 2) {
            print("Enter Job To Kill {ClientName JobName");
            input = br.readLine();
            StringTokenizer stk = new StringTokenizer(input);
            cluster.killJob(new Job(stk.nextToken(), stk.nextToken(), null));
        }

        if(command == 3) {
            print("Enter Job to Lower Priority {ClientName JobName}");
            input = br.readLine();
            StringTokenizer stk = new StringTokenizer(input);
            cluster.lowerJob(new Job(stk.nextToken(), stk.nextToken(), null));
        }

        if(command == 4) {
            print("Enter Job to Raise Priority {ClientName JobName}");
            input = br.readLine();
            StringTokenizer stk = new StringTokenizer(input);
            cluster.raiseJob(new Job(stk.nextToken(), stk.nextToken(), null));
        }

        if(command == 0) {
            print("Shutting Off JMS. GoodBye.");
            System.exit(0);
            break;
        }

        if(command == 1) {
            print("Enter a New Job {ClientName JobName WorkLoad(seconds)} ...");
            input = br.readLine();
            StringTokenizer stk = new StringTokenizer(input);
            cluster.addJob(new Job(stk.nextToken(), stk.nextToken(), Long.parseLong(stk.nextToken())));
        }

    }while(continued);

    }

    private static void progress() {
        Job[] jobs = cluster.getJobs();
        System.out.println();
        System.out.printf("%-40s %-100s %20s %n", "Client.Job.WorkLoad.Status.Priority", "Progress", "Percent Complete");

        for(Job job: jobs) {
            System.out.printf("%-40s %100s %20s %n", job.getJobDetail(), job.getProgress(), job.getPercentComplete());
        }
    }

    private static void print(String msg) {
        System.out.println(msg);
    }

    private  static void printMenu() {
        print("");
        print("-------------- Cloud JMS System Menu -----------------");
        print("0. Exit");
        print("1. Enter a New Job");
        print("2. Kill an existing Job");
        print("3. Lower Job Priority");
        print("4. Raise Job Priority");
        print("5. Print Progress");
        print("6. Print Cluster Load");
        print("-------------------------------------------------------");
    }
}
