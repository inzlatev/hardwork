package ru.skillsmart.task1_reduce_cc;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import ru.skillsmart.task1_reduce_cc.OrderManager.DayOfWeek;
import ru.skillsmart.task1_reduce_cc.OrderManager.Order;
import ru.skillsmart.task1_reduce_cc.OrderManager.OrderType;

public class OrderManagerTest {
    private final OrderManager orderManager = new OrderManager();

    @Test
    public void testProcessOrderForBreakfast() {
        String result = orderManager.processOrder("Monday", 9, "Breakfast", false, 1);
        assertEquals("Regular Breakfast", result);
    }

    @Test
    public void testProcessOrderForLunch() {
        String result = orderManager.processOrder("Monday", 12, "Lunch", false, 1);
        assertEquals("Regular Lunch Order", result);
    }

    @Test
    public void testProcessOrderForDinner() {
        String result = orderManager.processOrder("Monday", 18, "Dinner", false, 1);
        assertEquals("Regular Dinner Order", result);
    }

    @Test
    public void testProcessOrderForInvalidDay() {
        String result = orderManager.processOrder("InvalidDay", 9, "Breakfast", false, 1);
        assertEquals("Invalid day of the week.", result);
    }

    @Test
    public void testProcessOrderForInvalidOrderType() {
        String result = orderManager.processOrder("Monday", 9, "InvalidOrderType", false, 1);
        assertEquals("Sorry, we are not open for dinner until 5pm.", result);
    }

    @Test
    public void testProcessOrderModifiedForBreakfast() {
        String result = orderManager.processOrderModified(new Order(DayOfWeek.MONDAY, 9, OrderType.BREAKFAST, false, 1));
        assertEquals("Regular Breakfast", result);
    }

    @Test
    public void testProcessOrderModifiedForHolidayBreakfast() {
        String result = orderManager.processOrderModified(new Order(DayOfWeek.MONDAY, 9, OrderType.BREAKFAST, true, 10));
        assertEquals("Holiday Breakfast Special", result);
    }

    @Test
    public void testProcessOrderModifiedForBreakfastInvalid() {
        String result = orderManager.processOrderModified(new Order(DayOfWeek.MONDAY, 11, OrderType.BREAKFAST, false, 1));
        assertEquals("Sorry, breakfast can be served only from 0 to 10", result);
    }

    @Test
    public void testProcessOrderModifiedForLunchRegular() {
        String result = orderManager.processOrderModified(new Order(DayOfWeek.MONDAY, 14, OrderType.LUNCH, false, 1));
        assertEquals("Regular Lunch Order", result);
    }

    @Test
    public void testProcessOrderModifiedForLunchLarge() {
        String result = orderManager.processOrderModified(new Order(DayOfWeek.MONDAY, 14, OrderType.LUNCH, false, 15));
        assertEquals("Large Lunch Order", result);
    }

    @Test
    public void testProcessOrderModifiedForLunchInvalid() {
        String result = orderManager.processOrderModified(new Order(DayOfWeek.MONDAY, 1, OrderType.LUNCH, false, 1));
        assertEquals("Sorry, lunch can be served only from 10 to 15", result);
    }

    @Test
    public void testProcessOrderModifiedForDinnerRegular() {
        String result = orderManager.processOrderModified(new Order(DayOfWeek.MONDAY, 20, OrderType.DINNER, false, 1));
        assertEquals("Regular Dinner Order", result);
    }

    @Test
    public void testProcessOrderModifiedForDinnerLarge() {
        String result = orderManager.processOrderModified(new Order(DayOfWeek.MONDAY, 20, OrderType.DINNER, false, 15));
        assertEquals("Large Dinner Order", result);
    }

    @Test
    public void testProcessOrderModifiedForDinnerInvalid() {
        String result = orderManager.processOrderModified(new Order(DayOfWeek.MONDAY, 13, OrderType.DINNER, false, 15));
        assertEquals("Sorry, dinner can be served only from 15 to 24", result);
    }
}
