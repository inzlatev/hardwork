package ru.skillsmart.task1_reduce_cc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataProcessor {

    // Исходный код
    public List<Integer> processData(List<Map<String, Object>> data) {
        List<Integer> result = new ArrayList<>();

        for (Map<String, Object> item : data) {
            String type = (String) item.get("type");
            int value = (Integer) item.get("value");

            if (type.equals("A")) {
                if (value > 10) {
                    result.add(value * 2);
                } else {
                    result.add(value + 5);
                }
            } else if (type.equals("B")) {
                if (value > 20) {
                    result.add(value - 5);
                } else {
                    result.add(value / 2);
                }
            } else if (type.equals("C")) {
                if (value > 30) {
                    result.add(value * value);
                } else if (value > 15) {
                    result.add((int) Math.sqrt(value));
                } else {
                    result.add(value - 3);
                }
            } else if (type.equals("D")) {
                if (value % 2 == 0) {
                    result.add(value / 2);
                } else {
                    result.add(value * 3 + 1);
                }
            } else {
                if (value != 0) {
                    result.add(-value);
                } else {
                    result.add(0);
                }
            }
        }

        return result;
    }

    // Новая реализация. Избавился от for, всех if, вложенных условий, else, использовал полиморфизм.
    private static final Map<String, DataHandler> handlers = new HashMap<>();

    static {
        handlers.put("A", new TypeAHandler());
        handlers.put("B", new TypeBHandler());
        handlers.put("C", new TypeCHandler());
        handlers.put("D", new TypeDHandler());
        handlers.put("default", new DefaultHandler());
    }

    public List<Integer> processDataModified(List<Map<String, Object>> data) {

        List<Integer> result = new ArrayList<>();

        data.forEach(item -> {
            DataHandler handler = handlers.getOrDefault((String) item.get("type"), handlers.get("default"));
            result.add(handler.process((Integer) item.get("value")));
        });

        return result;
    }

    interface DataHandler {
        int process(int value);
    }

    static class TypeAHandler implements DataHandler {
        @Override
        public int process(int value) {
            return value > 10 ? (value * 2) : (value + 5);
        }
    }

    static class TypeBHandler implements DataHandler {
        @Override
        public int process(int value) {
            return value > 20 ? (value - 5) : (value / 2);
        }
    }

    static class TypeCHandler implements DataHandler {
        @Override
        public int process(int value) {
            return value > 30 ? (value * value) :
                    (value > 15 ? (int) Math.sqrt(value) : (value - 3));
        }
    }

    static class TypeDHandler implements DataHandler {
        @Override
        public int process(int value) {
            return value % 2 == 0 ? (value / 2) : (value * 3 + 1);
        }
    }

    static class DefaultHandler implements DataHandler {
        @Override
        public int process(int value) {
            return value != 0 ? (-value) : 0;
        }
    }
}
