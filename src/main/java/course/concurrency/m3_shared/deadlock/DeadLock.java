package course.concurrency.m3_shared.deadlock;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DeadLock {
    public static void main(String[] args) {
        System.out.println("sdfsdfsdfsdfsdfdsf");

        Map<String, String> map = new ConcurrentHashMap<>();

        map.put("1", "one");
        map.put("2", "two");
        map.put("3", "three");

        System.out.println(map);
        System.out.println(map.compute("2", (k, v) -> (v == null) ? "Result" : "Not Result"));
        System.out.println(map);

        System.out.println(map.computeIfAbsent("7", k -> k + k));
        System.out.println(map);

        System.out.println(map.merge("3", "MERGE KEY 2 NOT PRERSENT", (v1, v2) -> "OLD VALUE: " + v1 + " NEW VALUE: " + v2));
        System.out.println(map);

/*        Map<String, Integer> map2 = new HashMap<>();
        for (int i = 0; i < 10; i++) {
            System.out.println(map2.merge("key", 1, Integer::sum));
        }*/

        map.putAll();
        map.clear();
    }
}
