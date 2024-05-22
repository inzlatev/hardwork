package ru.skillsmart.task1_reduce_cc;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DataProcessorTest {
    private final DataProcessor dataProcessor = new DataProcessor();

    @Test
    public void processDataShouldHandleTypeA() {
        List<Map<String, Object>> data = new ArrayList<>();
        Map<String, Object> item = new HashMap<>();
        item.put("type", "A");
        item.put("value", 15);
        data.add(item);

        List<Integer> initialResult = dataProcessor.processData(data);
        List<Integer> modifiedResult = dataProcessor.processDataModified(data);

        assertEquals(30, initialResult.get(0));
        assertEquals(30, modifiedResult.get(0));
    }

    @Test
    public void processDataShouldHandleTypeB() {
        List<Map<String, Object>> data = new ArrayList<>();
        Map<String, Object> item = new HashMap<>();
        item.put("type", "B");
        item.put("value", 25);
        data.add(item);

        List<Integer> initialResult = dataProcessor.processData(data);
        List<Integer> modifiedResult = dataProcessor.processDataModified(data);

        assertEquals(20, initialResult.get(0));
        assertEquals(20, modifiedResult.get(0));
    }

    @Test
    public void processDataShouldHandleTypeC() {
        List<Map<String, Object>> data = new ArrayList<>();
        Map<String, Object> item = new HashMap<>();
        item.put("type", "C");
        item.put("value", 35);
        data.add(item);

        List<Integer> initialResult = dataProcessor.processData(data);
        List<Integer> modifiedResult = dataProcessor.processDataModified(data);

        assertEquals(1225, initialResult.get(0));
        assertEquals(1225, modifiedResult.get(0));
    }

    @Test
    public void processDataShouldHandleTypeD() {
        List<Map<String, Object>> data = new ArrayList<>();
        Map<String, Object> item = new HashMap<>();
        item.put("type", "D");
        item.put("value", 6);
        data.add(item);

        List<Integer> initialResult = dataProcessor.processData(data);
        List<Integer> modifiedResult = dataProcessor.processDataModified(data);

        assertEquals(3, initialResult.get(0));
        assertEquals(3, modifiedResult.get(0));
    }

    @Test
    public void processDataShouldHandleDefaultType() {
        List<Map<String, Object>> data = new ArrayList<>();
        Map<String, Object> item = new HashMap<>();
        item.put("type", "E");
        item.put("value", 5);
        data.add(item);

        List<Integer> initialResult = dataProcessor.processData(data);
        List<Integer> modifiedResult = dataProcessor.processDataModified(data);

        assertEquals(-5, initialResult.get(0));
        assertEquals(-5, modifiedResult.get(0));
    }

}