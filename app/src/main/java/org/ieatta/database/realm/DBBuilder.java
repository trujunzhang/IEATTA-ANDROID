package org.ieatta.database.realm;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class DBBuilder {
    private HashMap<String, Object> equalMap = new LinkedHashMap<>();
    private HashMap<String, Object> greaterMap = new LinkedHashMap<>();
    private HashMap<String, String> matchersMap = new LinkedHashMap<>();
    public HashMap<String, Object> containedMap = new LinkedHashMap<>();
    private List<String> orderedByDescendingList = new LinkedList<>();
    private List<String> orderedByAscendingList = new LinkedList<>();
    public int limit = -1; // negative limits mean, do not send a limit

    public void whereEqualTo(String key, Object value) {
        this.equalMap.put(key, value);
    }

    public void whereGreaterThan(String key, Object value) {
        this.greaterMap.put(key, value);
    }

    public void whereContainedIn(String key, Object value) {
        this.containedMap.put(key, value);
    }

    public void whereMatchers(String key, String keyword) {
        this.matchersMap.put(key, keyword);
    }

    public void orderByDescending(String key) {
        this.orderedByDescendingList.add(key);
    }

    public void orderByAscending(String key) {
        this.orderedByAscendingList.add(key);
    }

    public void setLimit(int newLimit) {
        this.limit = newLimit;
    }

}
