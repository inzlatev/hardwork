package ru.skillsmart.task1_reduce_cc;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class OrderProcessorTest {
    private final OrderProcessor orderProcessor = new OrderProcessor();

    @Test
    public void processOrderModifiedShouldHandleValidOrder() {
        OrderProcessor.Order order = createValidOrder();
        String result = orderProcessor.processOrderModified(order);
        assertEquals("Order processed successfully", result);
    }

    @Test
    public void processOrderModifiedShouldHandleNullOrder() {
        String result = orderProcessor.processOrderModified(null);
        assertEquals("Order is null", result);
    }

    @Test
    public void processOrderModifiedShouldHandleOrderWithNoItems() {
        OrderProcessor.Order order = createValidOrder();
        order.setItems(new ArrayList<>());
        String result = orderProcessor.processOrderModified(order);
        assertEquals("Order has no items", result);
    }

    @Test
    public void processOrderModifiedShouldHandleOrderWithInvalidItem() {
        OrderProcessor.Order order = createValidOrder();
        OrderProcessor.OrderItem invalidItem = new OrderProcessor.OrderItem();
        invalidItem.setPrice(-1);
        invalidItem.setQuantity(1);
        invalidItem.setDiscounted(false);
        order.getItems().add(invalidItem);
        String result = orderProcessor.processOrderModified(order);
        assertEquals("Item price is invalid", result);
    }

    @Test
    public void processOrderModifiedShouldHandleOrderWithInvalidPaymentInfo() {
        OrderProcessor.Order order = createValidOrder();
        OrderProcessor.PaymentInfo invalidPaymentInfo = new OrderProcessor.PaymentInfo();
        invalidPaymentInfo.setCardNumber("123456781234567"); // Invalid card number
        invalidPaymentInfo.setExpirationDate("12/25");
        invalidPaymentInfo.setSecurityCode("123");
        order.setPaymentInfo(invalidPaymentInfo);
        String result = orderProcessor.processOrderModified(order);
        assertEquals("Invalid card number", result);
    }

    private OrderProcessor.Order createValidOrder() {
        OrderProcessor.Order order = new OrderProcessor.Order();
        List<OrderProcessor.OrderItem> items = new ArrayList<>();
        OrderProcessor.OrderItem item = new OrderProcessor.OrderItem();
        item.setPrice(100);
        item.setQuantity(1);
        item.setDiscounted(false);
        items.add(item);
        order.setItems(items);

        OrderProcessor.Customer customer = new OrderProcessor.Customer();
        customer.setName("John Doe");
        customer.setEmail("john.doe@example.com");
        customer.setPremium(true);

        OrderProcessor.Address address = new OrderProcessor.Address();
        address.setStreet("123 Main St");
        address.setCity("Anytown");
        address.setZipCode("12345");
        address.setCountry("USA");
        customer.setAddress(address);

        order.setCustomer(customer);

        OrderProcessor.PaymentInfo paymentInfo = new OrderProcessor.PaymentInfo();
        paymentInfo.setCardNumber("1234567812345678");
        paymentInfo.setExpirationDate("12/25");
        paymentInfo.setSecurityCode("123");
        order.setPaymentInfo(paymentInfo);

        return order;
    }
}
