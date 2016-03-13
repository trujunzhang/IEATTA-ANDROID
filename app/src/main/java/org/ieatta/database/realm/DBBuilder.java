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

    public DBBuilder whereEqualTo(String key, Object value) {
        this.equalMap.put(key, value);
        return this;
    }

    public DBBuilder whereGreaterThan(String key, Object value) {
        this.greaterMap.put(key, value);
        return this;
    }

    public DBBuilder whereContainedIn(String key, Object value) {
        this.containedMap.put(key, value);
        return this;
    }

    public DBBuilder whereMatchers(String key, String keyword) {
        this.matchersMap.put(key, keyword);
        return this;
    }

    public DBBuilder orderByDescending(String key) {
        this.orderedByDescendingList.add(key);
        return this;
    }

    public DBBuilder orderByAscending(String key) {
        this.orderedByAscendingList.add(key);
        return this;
    }

    public DBBuilder setLimit(int newLimit) {
        this.limit = newLimit;
        return this;
    }

}
