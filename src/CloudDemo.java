import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: dkumawat
 * Date: 8/3/13
 * Time: 12:32 PM
 * To change this template use File | Settings | File Templates.
 */
public class CloudDemo {

    public static void main(String [] args) throws Exception {

    String eoi = null;// end of input
    BufferedReader br = (new BufferedReader(new InputStreamReader(System.in)));
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

        if(command < 0 || command > 4) {
            print("Invalid Command. Try Again.");
            continue;
        }

        if(command == 0) {
            print("Shutting Off JMS. GoodBye.");
            break;
        }

    }while(true);

    }

    private static void print(String msg) {
        System.out.println(msg);
    }

    private  static void printMenu() {
        print("Cloud JMS System Menu");
        print("0. Exit");
        print("1. Enter a New Job {Client JobName WorkLoad}");
        print("2. Kill an existing Job {Client JobName Kill}");
        print("3. Check Current Job Statuses");
        print("4. Change Job Priority {Client JobName Priority(LOW|HIGH)}");
    }
}
