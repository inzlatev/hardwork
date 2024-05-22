package ru.skillsmart.task1_reduce_cc;

import java.util.HashMap;
import java.util.Map;

public class OrderManager {

    // Исходный код
    public String processOrder(String dayOfWeek, int hourOfDay, String orderType, boolean isHoliday, int quantity) {
        String result = "";

        if (dayOfWeek.equals("Monday")) {
            if (hourOfDay < 10) {
                if (orderType.equals("Breakfast")) {
                    if (isHoliday) {
                        result = "Holiday Breakfast Special";
                    } else {
                        result = "Regular Breakfast";
                    }
                } else if (orderType.equals("Lunch")) {
                    result = "Sorry, lunch is not available until 11am.";
                } else {
                    result = "Sorry, we are not open for dinner until 5pm.";
                }
            } else if (hourOfDay < 15) {
                if (orderType.equals("Lunch")) {
                    if (quantity > 10) {
                        result = "Large Lunch Order";
                    } else {
                        result = "Regular Lunch Order";
                    }
                } else {
                    result = "Sorry, we are not open for dinner until 5pm.";
                }
            } else {
                if (orderType.equals("Dinner")) {
                    if (quantity > 5) {
                        result = "Large Dinner Order";
                    } else {
                        result = "Regular Dinner Order";
                    }
                } else {
                    result = "Sorry, we are not open for breakfast or lunch at this time.";
                }
            }
        } else if (dayOfWeek.equals("Tuesday")) {
            // Similar logic for Tuesday
        } else if (dayOfWeek.equals("Wednesday")) {
            // Similar logic for Wednesday
        } else if (dayOfWeek.equals("Thursday")) {
            // Similar logic for Thursday
        } else if (dayOfWeek.equals("Friday")) {
            // Similar logic for Friday
        } else if (dayOfWeek.equals("Saturday")) {
            // Similar logic for Saturday
        } else if (dayOfWeek.equals("Sunday")) {
            // Similar logic for Sunday
        } else {
            result = "Invalid day of the week.";
        }

        return result;
    }

    // Новая реализация. Избавился от условий и вложенных условий, использовал полиморфизм, enum вместо строк для дней недели и типа заказа.

    public enum DayOfWeek {
        MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY, SUNDAY
    }

    public enum OrderType {
        BREAKFAST("breakfast", 0, 10),
        LUNCH("lunch", 10, 15),
        DINNER("dinner", 15, 24);

        final String displayName;
        final int startHour;
        final int endHour;

        OrderType(String displayName, int startHour, int endHour) {
            this.displayName = displayName;
            this.startHour = startHour;
            this.endHour = endHour;
        }

        boolean isAvailableAt(int hour) {
            return hour >= startHour && hour < endHour;
        }
    }

    public static class Order {
        DayOfWeek dayOfWeek;
        int hourOfDay;
        OrderType orderType;
        boolean isHoliday;
        int quantity;

        public Order(){
        };
        public Order(DayOfWeek dayOfWeek, int hourOfDay, OrderType orderType, boolean isHoliday, int quantity){
            this.dayOfWeek = dayOfWeek;
            this.hourOfDay = hourOfDay;
            this.orderType = orderType;
            this.isHoliday = isHoliday;
            this.quantity = quantity;
        }
    }

    private final Map<OrderType, OrderHandler> handlers = new HashMap<>();

    public OrderManager() {
        handlers.put(OrderType.BREAKFAST, new BreakfastOrderHandler());
        handlers.put(OrderType.LUNCH, new LunchOrderHandler());
        handlers.put(OrderType.DINNER, new DinnerOrderHandler());
    }

    public String processOrderModified(Order order) {
        OrderHandler handler = handlers.get(order.orderType);

        boolean isValidOrder = order.orderType.isAvailableAt(order.hourOfDay);

        return isValidOrder ? handler.process(order) :
                "Sorry, " + order.orderType.displayName +
                        " can be served only from " + order.orderType.startHour + " to " + order.orderType.endHour;
    }

    public interface OrderHandler {
        String process(Order order);
    }

    public static class BreakfastOrderHandler implements OrderHandler {
        @Override
        public String process(Order order) {
                return order.isHoliday ? "Holiday Breakfast Special" : "Regular Breakfast";
        }
    }

    public static class LunchOrderHandler implements OrderHandler {
        @Override
        public String process(Order order) {
                return order.quantity > 10 ? "Large Lunch Order" : "Regular Lunch Order";
        }
    }

    public static class DinnerOrderHandler implements OrderHandler {
        @Override
        public String process(Order order) {
            return order.quantity > 5 ? "Large Dinner Order" : "Regular Dinner Order";
        }
    }
}
