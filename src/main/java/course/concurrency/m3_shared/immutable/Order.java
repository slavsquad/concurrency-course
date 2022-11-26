package course.concurrency.m3_shared.immutable;

import java.util.ArrayList;
import java.util.List;

import static course.concurrency.m3_shared.immutable.Order.Status.NEW;

final public class Order {


    public enum Status {NEW, IN_PROGRESS, DELIVERED}

    private final Long id;
    private final List<Item> items;
    private final PaymentInfo paymentInfo;
    private final boolean isPacked;
    private final Status status;

    public static Order create(Long id, List<Item> items) {
        return new Order(id, items, null, false, Status.NEW);
    }

    public static Order updatePaymentInfo(Order order, PaymentInfo paymentInfo) {
        if (order == null) return null;
        return new Order(order.getId(), order.getItems(), paymentInfo, order.isPacked, order.getStatus());
    }

    public static Order packed(Order order) {
        if (order == null) return null;
        return new Order(order.getId(), order.getItems(), order.getPaymentInfo(), true, order.getStatus());
    }

    public static Order delivered(Order order) {
        if (order == null) return null;
        List<String> df = new ArrayList<>();
        df.spliterator()
        return new Order(order.getId(), order.getItems(), order.getPaymentInfo(), order.isPacked, Status.DELIVERED);
    }

    private Order(Long id, List<Item> items, PaymentInfo paymentInfo, boolean isPacked, Status status) {
        this.id = id;
        this.items = items;
        this.paymentInfo = paymentInfo;
        this.isPacked = isPacked;
        this.status = status;
    }

    public synchronized boolean checkStatus() {
        if (items != null && !items.isEmpty() && paymentInfo != null && isPacked) {
            return true;
        }
        return false;
    }

    public Long getId() {
        return id;
    }


    public List<Item> getItems() {
        return items;
    }

    public PaymentInfo getPaymentInfo() {
        return paymentInfo;
    }


    public boolean isPacked() {
        return isPacked;
    }


    public Status getStatus() {
        return status;
    }

}
