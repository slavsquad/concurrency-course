package course.concurrency.exams.auction;

import java.util.concurrent.atomic.AtomicReference;

public class AuctionStoppableOptimistic implements AuctionStoppable {

    private Notifier notifier;

    public AuctionStoppableOptimistic(Notifier notifier) {
        this.notifier = notifier;
    }
    volatile boolean isAccept = true;

    private AtomicReference<Bid> latestBid = new AtomicReference<>(new Bid(Long.MIN_VALUE, Long.MIN_VALUE, Long.MIN_VALUE));

    public boolean propose(Bid bid) {
        Bid currentBid;
        if (!isAccept) return false;
        do {
            currentBid = latestBid.get();
            if (bid.getPrice() <= currentBid.getPrice()) {
                return false;
            }
        } while (!latestBid.compareAndSet(currentBid, bid));
        notifier.sendOutdatedMessage(currentBid);
        return false;
    }

    public Bid getLatestBid() {
        return latestBid.get();
    }

    public Bid stopAuction() {
        isAccept = false;
        return latestBid.get();
    }
}
