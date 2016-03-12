package org.ieatta.server.recurring;


import java.util.LinkedList;
import java.util.List;

/**
 * Created by djzhang on 12/16/15.
 */
public class SerialTasksManager<T> {
    private SerialTasksManager self = this;
    private List<T> array = new LinkedList<T>();

    public SerialTasksManager(List<T> array) {
        this.array = array;
    }

    // MARK: Fetch object step by step.
    int taskStep = 0;

    public boolean hasNext() {
        if (taskStep < self.array.size())
            return true;

        return false;
    }

    public T next() {
        T model = (T) self.array.get(self.taskStep);
        self.taskStep += 1;

        return model;
    }

    public int index() {
        return self.taskStep;
    }

    public int getFetchedCount() {
        return self.array.size();
    }
}
