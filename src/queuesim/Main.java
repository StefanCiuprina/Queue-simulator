package queuesim;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import static java.lang.System.exit;

public class Main {

    public static void main(String[] args) {
        if(args.length != 2) {
            System.out.println("You need 2 arguments to run this program.\nThe input file followed by the output file.");
            exit(1);
        }
        int numberOfClients, numberOfQueues, simulationInterval, minArrivalTime, maxArrivalTime, minServiceTime, maxServiceTime;
        Scanner scanner;
        try { scanner = new Scanner(new File(args[0])); }
        catch(FileNotFoundException e) {
            System.out.println("File " + args[0] + " doesn't exist!");
            return; }
        try { numberOfClients = scanner.nextInt();
            numberOfQueues = scanner.nextInt();
            simulationInterval = scanner.nextInt();
            scanner.nextLine();
            String timeString = scanner.nextLine();
            String[] timeStrings = timeString.split(",");
            minArrivalTime = Integer.parseInt(timeStrings[0]);
            maxArrivalTime = Integer.parseInt(timeStrings[1]);
            timeString = scanner.nextLine();
            timeStrings = timeString.split(",");
            minServiceTime = Integer.parseInt(timeStrings[0]);
            maxServiceTime = Integer.parseInt(timeStrings[1]); }
        catch(Exception e) { System.out.println("An error occured reading the input."); return; }
        scanner.close();
        SimulateQueue simulation = new SimulateQueue(numberOfClients, numberOfQueues, simulationInterval, minArrivalTime, maxArrivalTime, minServiceTime, maxServiceTime, args[1]);
        Thread simulationThread = new Thread(simulation);
        simulationThread.start();
    }
}
