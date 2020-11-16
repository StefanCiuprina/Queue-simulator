package queuesim;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static java.lang.System.exit;

public class SimulateQueue implements Runnable {

    private List<Client> clients;
    private List<MyQueue> queues;

    private int numberOfClients, numberOfQueues, simulationInterval;
    private int minArrivalTime, maxArrivalTime;
    private int minServiceTime, maxServiceTime;
    private String outputFile;

    private double averageWaitingTime;

    public SimulateQueue(int numberOfClients, int numberOfQueues, int simulationInterval, int minArrivalTime, int maxArrivalTime, int minServiceTime, int maxServiceTime, String outputFile) {
        this.numberOfClients = numberOfClients;
        this.numberOfQueues = numberOfQueues;
        this.simulationInterval = simulationInterval;
        this.minArrivalTime = minArrivalTime;
        this.maxArrivalTime = maxArrivalTime;
        this.minServiceTime = minServiceTime;
        this.maxServiceTime = maxServiceTime;

        this.clients = new ArrayList<>();
        this.queues = new ArrayList<>();

        this.outputFile = outputFile;
        this.averageWaitingTime = 0;
    }

    public void run() {
        for(int i = 0; i < numberOfClients; i++) {
            clients.add(new Client(i+1, minArrivalTime, maxArrivalTime, minServiceTime, maxServiceTime));
        }
        Collections.sort(clients);
        Timer timer = new Timer(simulationInterval);
        for(int i = 0; i < numberOfQueues; i++) {
            queues.add(new MyQueue(timer, i+1));
            queues.get(i).start();
        }
             try {
                 Thread.sleep(300);
                 timer.start();
                 BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile));
                 while (!timer.isFinished()) {
                     distributeClients(timer);
                     exitIfSimulationEnded(writer);
                     printWaitingClients(writer, timer);
                     printQueues(writer);
                     Thread.sleep(1000);
                 }
                 averageWaitingTime /= numberOfClients;
                 System.out.println("Average waiting time: " + averageWaitingTime);
                 writer.write("Average waiting time: " + averageWaitingTime);
                 writer.newLine();
                 writer.close();
             } catch(Exception e) {}
    }

    private MyQueue getQueueWithSmallestWaitingTime() {
        if(queues.isEmpty())
            return null;
        int min = queues.get(0).getWaitingTime();
        MyQueue result = queues.get(0);
        for(MyQueue queue : queues) {
            if(queue.getWaitingTime() < min) {
                min = queue.getWaitingTime();
                result = queue;
            }
        }
        return result;
    }

    private void exitIfSimulationEnded(BufferedWriter writer) throws IOException {
        int clientsDone = 0;
        for(Client client : clients) {
            if(client.getQueueNo() == -1) {
                clientsDone++;
            }
        }

        if(clientsDone == numberOfClients) {
            if(numberOfClients != 0) {
                averageWaitingTime /= numberOfClients;
            }
            System.out.println("Average waiting time: " + averageWaitingTime);
            writer.write("Average waiting time: " + averageWaitingTime);
            writer.newLine();
            writer.close();
            exit(0);
        }
    }

    private void printWaitingClients(BufferedWriter writer, Timer timer) throws IOException {
        System.out.println("\nTime: " + timer.getTime());
        writer.newLine();
        writer.write("Time: " + timer.getTime() + "");
        writer.newLine();
        System.out.print("Waiting Clients: ");
        writer.write("Waiting Clients: ");
        for (Client client : clients) {
            if(client.getQueueNo() == 0) {
                System.out.print("(" + client.getID() + "," + client.gettArrival() + "," + client.gettService() + ")");
                writer.write("(" + client.getID() + "," + client.gettArrival() + "," + client.gettService() + ")");
            }
        }
        System.out.println();
        writer.newLine();
    }

    private void printQueues(BufferedWriter writer) throws IOException {
        for(int i = 1; i <= numberOfQueues; i++) {
            System.out.print("Queue " + i + ":");
            writer.write("Queue " + i + ":");

            for(Client client : clients) {
                if(client.getQueueNo() == i) {
                    averageWaitingTime++;
                    System.out.print("(" + client.getID() + "," + client.gettArrival() + "," + client.gettService() + ")");
                    writer.write("(" + client.getID() + "," + client.gettArrival() + "," + client.gettService() + ")");
                }
            }
            System.out.println("");
            writer.newLine();
        }
    }

    private void distributeClients(Timer timer) {
        for (Client client : clients) {
            if ((timer.getTime() >= client.gettArrival()) && (client.getQueueNo() == 0)) {
                MyQueue queue = getQueueWithSmallestWaitingTime();
                if(queue.isSleeping()) {
                    queue.wake();
                }
                queue.addClient(client);
            }
        }
    }

}
