package ru.skillsmart.task1_reduce_cc;

import lombok.Data;
import lombok.Getter;

import java.util.List;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class OrderProcessor {

    public String processOrder(Order order) {
        if (order == null) {
            return "Order is null";
        }

        if (order.getItems() == null || order.getItems().isEmpty()) {
            return "Order has no items";
        }

        Customer customer = order.getCustomer();
        if (customer == null) {
            return "Customer information is missing";
        }

        if (customer.getName() == null || customer.getName().isEmpty()) {
            return "Customer name is missing";
        }

        Address address = customer.getAddress();
        if (address == null) {
            return "Customer address is missing";
        }

        if (address.getStreet() == null || address.getStreet().isEmpty()) {
            return "Street address is missing";
        }

        if (address.getCity() == null || address.getCity().isEmpty()) {
            return "City is missing";
        }

        if (address.getZipCode() == null || address.getZipCode().isEmpty()) {
            return "Zip code is missing";
        }

        if (address.getCountry() == null || address.getCountry().isEmpty()) {
            return "Country is missing";
        }

        double totalAmount = 0.0;
        boolean hasDiscount = false;
        for (OrderItem item : order.getItems()) {
            if (item == null) {
                return "Order contains null item";
            }

            if (item.getPrice() < 0) {
                return "Item price is invalid";
            }

            if (item.getQuantity() <= 0) {
                return "Item quantity is invalid";
            }

            if (item.isDiscounted()) {
                hasDiscount = true;
            }

            totalAmount += item.getPrice() * item.getQuantity();
        }

        if (totalAmount <= 0) {
            return "Total amount is invalid";
        }

        if (totalAmount > 1000) {
            return "Order exceeds the maximum allowed amount";
        }

        if (hasDiscount && !customer.isPremium()) {
            return "Non-premium customers cannot use discounts";
        }

        PaymentInfo paymentInfo = order.getPaymentInfo();
        if (paymentInfo == null) {
            return "Payment information is missing";
        }

        if (paymentInfo.getCardNumber() == null || paymentInfo.getCardNumber().isEmpty()) {
            return "Card number is missing";
        }

        if (paymentInfo.getExpirationDate() == null) {
            return "Card expiration date is missing";
        }

        if (paymentInfo.getSecurityCode() == null || paymentInfo.getSecurityCode().length() != 3) {
            return "Invalid security code";
        }

        if (!isValidCardNumber(paymentInfo.getCardNumber())) {
            return "Invalid card number";
        }

        // Process payment
        boolean paymentProcessed = processPayment(totalAmount, paymentInfo);
        if (!paymentProcessed) {
            return "Payment processing failed";
        }

        // Send confirmation email
        sendConfirmationEmail(customer.getEmail(), order);

        // Finalize order
        finalizeOrder(order);
        return "Order processed successfully";
    }

    private boolean processPayment(double amount, PaymentInfo paymentInfo) {
        // Dummy implementation for payment processing
        // Simulating a real payment processing logic
        System.out.println("Processing payment of $" + amount + " for card " + paymentInfo.getCardNumber());
        return true;
    }

    private void finalizeOrder(Order order) {
        // Dummy implementation for finalizing order
        // Example: Update order status in database
        System.out.println("Finalizing order for customer " + order.getCustomer().getName());
    }

    private void sendConfirmationEmail(String email, Order order) {
        // Dummy implementation for sending confirmation email
        // Example: Sending email via an email service
        System.out.println("Sending confirmation email to " + email);
    }

    private boolean isValidCardNumber(String cardNumber) {
        // Dummy implementation for card number validation
        // Simple check for length, real implementation would involve more complex checks
        return cardNumber.length() == 16;
    }

    // Dummy classes for the example
    @Data
    public static class Order {
        private List<OrderItem> items;
        private Customer customer;
        private PaymentInfo paymentInfo;
    }

    @Data
    public static class OrderItem {
        private double price;
        private int quantity;
        private boolean isDiscounted;
    }

    @Data
    public static class Customer {
        private String name;
        private String email;
        private Address address;
        private boolean isPremium;
    }

    @Data
    public static class Address {
        private String street;
        private String city;
        private String zipCode;
        private String country;
    }

    @Data
    public static class PaymentInfo {
        private String cardNumber;
        private String expirationDate;
        private String securityCode;
    }

    // Новая реализация. Избавился от почти всех if, for,
    // использовал функциональное программирование и некое подобие табличного подхода для валидации.
    @Getter
    static
    class ValidationRule<T> {
        private final String fieldName;
        private final Predicate<T> condition;
        private final String errorMessage;

        public ValidationRule(String fieldName, Predicate<T> condition, String errorMessage) {
            this.fieldName = fieldName;
            this.condition = condition;
            this.errorMessage = errorMessage;
        }

        public boolean validate(T entity) {
            return condition.test(entity);
        }
    }

    class ValidationConfig {
        public List<ValidationRule<Order>> getOrderValidationRules() {
            List<ValidationRule<Order>> validationRules = new ArrayList<>();

            validationRules.add(new ValidationRule<>("orderNotNull",
                    Objects::nonNull,
                    "Order is null"));
            validationRules.add(new ValidationRule<>("orderHasItems",
                    order -> order.getItems() != null && !order.getItems().isEmpty(),
                    "Order has no items"));
            validationRules.add(new ValidationRule<>("customerInfoPresent",
                    order -> order.getCustomer() != null,
                    "Customer information is missing"));
            validationRules.add(new ValidationRule<>("customerInfoPresent",
                    order -> order.getCustomer().getName() != null && !order.getCustomer().getName().isEmpty(),
                    "Customer name is missing"));
            validationRules.add(new ValidationRule<>("addressIsPresent",
                    order -> order.getCustomer().getAddress() != null,
                    "Customer address is missing"));
            validationRules.add(new ValidationRule<>("streetIsPresent",
                    order -> order.getCustomer().getAddress().getStreet() != null && !order.getCustomer().getAddress().getStreet().isEmpty(),
                    "Street address is missing"));
            validationRules.add(new ValidationRule<>("cityIsPresent",
                    order -> order.getCustomer().getAddress().getCity() != null && !order.getCustomer().getAddress().getCity().isEmpty(),
                    "City is missing"));
            validationRules.add(new ValidationRule<>("zipCodeIsPresent",
                    order -> order.getCustomer().getAddress().getZipCode() != null && !order.getCustomer().getAddress().getZipCode().isEmpty(),
                    "Zip code is missing"));
            validationRules.add(new ValidationRule<>("countryIsPresent",
                    order -> order.getCustomer().getAddress().getCountry() != null && !order.getCustomer().getAddress().getCountry().isEmpty(),
                    "Country is missing"));

            validationRules.add(new ValidationRule<>("totalAmountIsValid", order -> {
                double totalAmount = order.getItems().stream().mapToDouble(item -> item.getPrice() * item.getQuantity()).sum();
                return totalAmount > 0 && totalAmount <= 1000;
            }, "Total amount is invalid"));
            validationRules.add(new ValidationRule<>("discountAllowed", order -> {
                boolean hasDiscount = order.getItems().stream().anyMatch(OrderItem::isDiscounted);
                return !(hasDiscount && !order.getCustomer().isPremium());
            }, "Non-premium customers cannot use discounts"));
            validationRules.add(new ValidationRule<>("paymentInfoPresent",
                    order -> order.getPaymentInfo() != null,
                    "Payment information is missing"));
            validationRules.add(new ValidationRule<>("cardNumberPresent",
                    order -> order.getPaymentInfo().getCardNumber() != null && !order.getPaymentInfo().getCardNumber().isEmpty(),
                    "Card number is missing"));
            validationRules.add(new ValidationRule<>("expirationDatePresent",
                    order -> order.getPaymentInfo().getExpirationDate() != null,
                    "Card expiration date is missing"));
            validationRules.add(new ValidationRule<>("securityCodeIsValid",
                    order -> order.getPaymentInfo().getSecurityCode() != null && order.getPaymentInfo().getSecurityCode().length() == 3,
                    "Invalid security code"));
            validationRules.add(new ValidationRule<>("cardNumberIsValid",
                    order -> isValidCardNumber(order.getPaymentInfo().getCardNumber()),
                    "Invalid card number"));

            return validationRules;
        }

        public List<ValidationRule<OrderItem>> getOrderItemValidationRules() {
            List<ValidationRule<OrderItem>> validationRules = new ArrayList<>();

            validationRules.add(new ValidationRule<>("itemNotNull", Objects::nonNull, "Order contains null item"));
            validationRules.add(new ValidationRule<>("priceValid", item -> item.getPrice() >= 0, "Item price is invalid"));
            validationRules.add(new ValidationRule<>("quantityValid", item -> item.getQuantity() > 0, "Item quantity is invalid"));

            return validationRules;
        }
    }

    public String processOrderModified(Order order) {
        ValidationConfig validationConfig = new ValidationConfig();
        List<ValidationRule<Order>> orderValidationRules = validationConfig.getOrderValidationRules();
        List<ValidationRule<OrderItem>> orderItemValidationRules = validationConfig.getOrderItemValidationRules();

        Optional<String> orderValidationError = orderValidationRules.stream()
                .filter(rule -> !rule.validate(order))
                .map(ValidationRule::getErrorMessage)
                .findFirst();

        if (orderValidationError.isPresent())
            return orderValidationError.get();

        Optional<String> orderItemValidationError = order.getItems().stream()
                .flatMap(item -> orderItemValidationRules.stream()
                .filter(rule -> !rule.validate(item))
                .map(ValidationRule::getErrorMessage))
                .findFirst();

        return orderItemValidationError.orElseGet(() -> {
            double totalAmount = order.getItems().stream().mapToDouble(item -> item.getPrice() * item.getQuantity()).sum();
            PaymentInfo paymentInfo = order.getPaymentInfo();

            return Stream.of(processPayment(totalAmount, paymentInfo))
                    .filter(paymentResult -> paymentResult)
                    .findFirst()
                    .map(paymentResult -> {
                        sendConfirmationEmail(order.getCustomer().getEmail(), order);
                        finalizeOrder(order);
                        return "Order processed successfully";
                    })
                    .orElse("Payment processing failed");
        });
    }
}

