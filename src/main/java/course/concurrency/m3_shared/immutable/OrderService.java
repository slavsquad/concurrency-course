package course.concurrency.m3_shared.immutable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class OrderService {

    private final Map<Long, Order> currentOrders = new ConcurrentHashMap<>();
    private final AtomicLong nextId = new AtomicLong();

    public long createOrder(List<Item> items) {
        Order order = Order.create(nextId.incrementAndGet(), items);
        currentOrders.put(order.getId(), order);
        return order.getId();
    }

    public void updatePaymentInfo(long orderId, PaymentInfo paymentInfo) {
        Order updateOrder = currentOrders.compute(orderId, (k, v) -> Order.updatePaymentInfo(v, paymentInfo));
        if (Objects.nonNull(updateOrder) && updateOrder.checkStatus()) {
            deliver(updateOrder);
        }
    }

    public void setPacked(long orderId) {
        Order packedOrder = currentOrders.compute(orderId, (k, v) -> Order.packed(v));
        if (Objects.nonNull(packedOrder) && packedOrder.checkStatus()) {
            deliver(packedOrder);
        }
    }

    private void deliver(Order order) {
        currentOrders.compute(order.getId(), (k, v) -> Order.delivered(v));
    }

    public synchronized boolean isDelivered(long orderId) {
        return currentOrders.get(orderId).getStatus().equals(Order.Status.DELIVERED);
    }
}
