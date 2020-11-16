package queuesim;

public class Client implements Comparable<Client>{

    private int ID;
    private int tArrival;
    private int tService;
    private int queueNo;

    public Client(int ID, int minArr, int maxArr, int minSrv, int maxSrv) {
        this.ID = ID;
        this.tArrival = (int) (Math.random() * ((maxArr - minArr) + 1)) + minArr;
        this.tService = (int)(Math.random() * ((maxSrv - minSrv) + 1)) + minSrv;
        this.queueNo = 0;
    }

    public int getID() {
        return ID;
    }

    public Integer gettArrival() {
        return tArrival;
    }

    public int gettService() {
        return tService;
    }

    public void processClient() {
        this.tService--;
    }

    public int getQueueNo() {
        return queueNo;
    }

    public void setQueueNo(int queueNo) {
        this.queueNo = queueNo;
    }

    @Override
    public int compareTo(Client o) {
        return this.gettArrival().compareTo(o.gettArrival());
    }
}
