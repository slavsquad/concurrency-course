package course.concurrency.exams.auction;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class AuctionStoppablePessimistic implements AuctionStoppable {

    private Notifier notifier;

    public AuctionStoppablePessimistic(Notifier notifier) {
        this.notifier = notifier;
    }

    private final Lock lock = new ReentrantLock();
    volatile private Bid latestBid = new Bid(Long.MIN_VALUE, Long.MIN_VALUE, Long.MIN_VALUE);
    volatile boolean isAccept = true;

    public boolean propose(Bid bid) {

        if (!isAccept || bid.getPrice() <= latestBid.getPrice()) return false;
        try {
            lock.lock();
            if (isAccept && bid.getPrice() > latestBid.getPrice()){
                latestBid = bid;
                notifier.sendOutdatedMessage(latestBid);
                return true;
            }
        } finally {
            lock.unlock();
        }
        return false;
    }

    public Bid getLatestBid() {
        return latestBid;
    }

    public Bid stopAuction() {
        isAccept = false;
        return latestBid;
    }
}
