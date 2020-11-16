package queuesim;

public class Timer extends Thread {

    private int seconds;
    private int time;
    private boolean isFinished;

    public Timer(int seconds) {
        this.seconds = seconds;
        isFinished = false;
        time = 0;
    }

    @Override
    public void run() {
        try{
            while(time < seconds) {
                Thread.sleep(1000);
                time++;
            }
            isFinished = true;
        } catch(InterruptedException e) {
        }
    }

    public int getTime() {
        return time;
    }

    public int remainingTime() {
        return seconds-time;
    }

    public boolean isFinished() {
        return isFinished;
    }
}
