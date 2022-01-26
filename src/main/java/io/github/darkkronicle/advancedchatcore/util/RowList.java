package io.github.darkkronicle.advancedchatcore.util;

import java.util.*;

public class RowList<T> {

    private final Map<String, List<T>> list = new HashMap<>();
    private final List<String> order = new ArrayList<>();

    public RowList() {

    }

    private List<T> makeNewList() {
        return new ArrayList<>();
    }

    public void createSection(String key, int y) {
        List<T> newList = makeNewList();
        order.add(y, key);
        list.put(key, newList);
    }

    public void add(String key, T value) {
        add(key, value, -1);
    }

    public void add(String key, T value, int index) {
        if (!list.containsKey(key)) {
            List<T> newList = makeNewList();
            order.add(key);
            newList.add(value);
            list.put(key, newList);
            return;
        }
        if (index < 0) {
            list.get(key).add(value);
        } else {
            list.get(key).add(index, value);
        }
    }

    public List<T> get(String key) {
        return list.get(key);
    }

    public List<T> get(int y) {
        String key;
        if (y >= order.size()) {
            key = order.get(order.size() - 1);
        } else {
            key = order.get(y);
        }
        return list.get(key);
    }

    public int rowSize() {
        return list.size();
    }

}
