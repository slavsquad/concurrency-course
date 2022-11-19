package course.concurrency.exams.auction;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class AuctionPessimistic implements Auction {

    private Notifier notifier;

    public AuctionPessimistic(Notifier notifier) {
        this.notifier = notifier;
    }

    private final Lock lock = new ReentrantLock();
    private volatile Bid latestBid = new Bid(Long.MIN_VALUE, Long.MIN_VALUE, Long.MIN_VALUE);

    public boolean propose(Bid bid) {

        if(bid.getPrice() <= latestBid.getPrice()) {
            return false;//если предложенная цена меньше или равна текущей то сразу отбрасываем ее.
        }

        Bid latestBidCopy = null;
        try {
            lock.lock();
            if (bid.getPrice() > latestBid.getPrice()) {
                latestBid = bid;
                notifier.sendOutdatedMessage(latestBid);
            }
        } finally {
            lock.unlock();
        }
        return false;
    }

    public Bid getLatestBid() {
        return latestBid;
    }
}
