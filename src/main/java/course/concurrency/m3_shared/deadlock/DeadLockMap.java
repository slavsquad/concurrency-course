package course.concurrency.m3_shared.deadlock;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

public class DeadLockMap {
    private Map<Integer, Integer> concurrentMap = new ConcurrentHashMap<>();

    @BeforeEach
    void setUp() {
        concurrentMap.put(1, 1);
        concurrentMap.put(2, 2);
    }

    @Test
    void deadLockTest() {

        CountDownLatch syncLatch = new CountDownLatch(1);
        Executor executor = Executors.newFixedThreadPool(2);

        CompletableFuture<Integer> futureOne = CompletableFuture.supplyAsync(() -> {
            try {
                syncLatch.await();
            } catch (InterruptedException e) {
                System.out.println("Error during await latch in task 1!");
                e.printStackTrace();
            }
            System.out.println("Task 1 was run!");
            int result = concurrentMap.compute(1, (k, v) ->
            {
                System.out.println("Task 1 compute level 1");
                concurrentMap.compute(2, (k1, v1) -> {
                    System.out.println("Task 1 compute level 2");
                    return v + k;
                });
                return v + k;
            });
            System.out.println("Task 1 complete!");
            return result;
        }, executor);

        CompletableFuture<Integer> futureTwo = CompletableFuture.supplyAsync(() -> {
            try {
                syncLatch.await();
            } catch (InterruptedException e) {
                System.out.println("Error during await latch in task 1!");
                e.printStackTrace();
            }
            System.out.println("Task 2 was run!");
            int result = concurrentMap.compute(2, (k, v) ->
            {
                System.out.println("Task 2 compute level 1");
                concurrentMap.compute(1, (k1, v1) -> {
                    System.out.println("Task 2 compute level 2");
                    return v + k;
                });
                return v + k;
            });
            System.out.println("Task 2 complete!");
            return result;
        }, executor);


        List<CompletableFuture<Integer>> futureList = new ArrayList<>(2);

        syncLatch.countDown();

        int resultOne = futureOne.join();
        int resultTwo = futureTwo.join();


    }
}
