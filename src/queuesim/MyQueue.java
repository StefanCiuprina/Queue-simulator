package queuesim;

import java.util.ArrayList;
import java.util.List;

public class MyQueue extends Thread{

    private int queueNo;
    private List<Client> clients;
    private Timer timer;
    private int waitingTime;
    private boolean isSleeping;
    public MyQueue(Timer timer, int queueNo) {
        this.timer = timer;
        this.clients = new ArrayList<>();
        this.waitingTime = 0;
        this.queueNo = queueNo;
        this.isSleeping = true;
    }

    @Override
    public void run() {
        boolean done = false;
        while(!done) {
            try {
                this.isSleeping = false;
                Thread.sleep(200);
                while (!timer.isFinished()) {
                    if (!clients.isEmpty()) {
                        if (clients.get(0).gettService() > 1) {
                            clients.get(0).processClient();
                        } else {
                            clients.get(0).setQueueNo(-1);
                            clients.remove(0);
                        }
                        this.waitingTime--;
                        Thread.sleep(1000);
                    } else {
                        isSleeping = true;
                        Thread.sleep(timer.remainingTime() * 1000);
                    }
                }
                done = true;
            } catch (InterruptedException e) { } //if an exception is caught, done will not be set to true and the loop will start again
        }
    }

    public int getWaitingTime() {
        return waitingTime;
    }

    public void addClient(Client client) {
        clients.add(client);
        client.setQueueNo(this.queueNo);
        this.waitingTime += client.gettService();
    }

    public boolean isSleeping() {
        return isSleeping;
    }

    public void wake() {
        this.interrupt();
    }
}
