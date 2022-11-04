package course.concurrency.m2_async.cf.min_price;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class PriceAggregator {

    private final static int SLA = 3000;

    private PriceRetriever priceRetriever = new PriceRetriever();

    private ExecutorService executor = Executors.newFixedThreadPool(4);

    public void setPriceRetriever(PriceRetriever priceRetriever) {
        this.priceRetriever = priceRetriever;
    }

    private Collection<Long> shopIds = Set.of(10l, 45l, 66l, 345l, 234l, 333l, 67l, 123l, 768l);

    public void setShops(Collection<Long> shopIds) {
        this.shopIds = shopIds;
    }

    public double getMinPrice(long itemId) {
        // здесь будет ваш код
        List<CompletableFuture<Double>> futureList = new ArrayList<>(shopIds.size());
        for (Long shopId : shopIds) {
            CompletableFuture<Double> future = CompletableFuture
                    .supplyAsync(() -> {
                        final double price = priceRetriever.getPrice(itemId, shopId);
                        System.out.println("Thread #" + Thread.currentThread().getName() + " Shop id: " + shopId + " itemId: " + itemId + " price : " + price);
                        return price;
                    }, executor)
                    .exceptionally(throwable -> {
                        System.out.println("Try get price shop id: " + shopId + " with itemId:" + itemId + " canceled, with exception: " + throwable);
                        return Double.NaN;
                    })
                    .completeOnTimeout(Double.NaN, SLA / shopIds.size(), TimeUnit.MILLISECONDS);
            futureList.add(future);
        }

        return futureList.stream()
                .mapToDouble(CompletableFuture::join)
                .filter(d -> !Double.isNaN(d))
                .min().orElse(Double.NaN);
    }
}
