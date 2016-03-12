package org.ieatta.server.recurring.util;


import java.util.LinkedList;
import java.util.List;

public class SerialTasksManager<T> {
    private List<T> array = new LinkedList<T>();

    public SerialTasksManager(List<T> array) {
        this.array = array;
    }

    // MARK: Fetch object step by step.
    int taskStep = 0;

    public boolean hasNext() {
        if (taskStep < this.array.size())
            return true;

        return false;
    }

    public T next() {
        T model = (T) this.array.get(this.taskStep);
        this.taskStep += 1;

        return model;
    }

    public int index() {
        return this.taskStep;
    }

    public int getFetchedCount() {
        return this.array.size();
    }
}
