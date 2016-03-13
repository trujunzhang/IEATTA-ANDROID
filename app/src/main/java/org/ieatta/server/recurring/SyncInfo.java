package org.ieatta.server.recurring;

import com.parse.ParseQuery;

import org.ieatta.parse.ParseQueryUtil;
import org.wikipedia.settings.Prefs;

import java.util.Date;

public class SyncInfo {
    public static final String TAG_NEW_RECORD_DATE = "lastRunDate";

    public Date lastRecordCreateAt;
    private String task;

    private SyncInfo() {
    }

    public SyncInfo(String task) {
        this.task = task;

        this.lastRecordCreateAt = null;
        long lastRunTime = Prefs.getLastRunTime(task);
        if (lastRunTime != 0) {
            this.lastRecordCreateAt = new Date(lastRunTime);
        }
    }

    public void setLastRunTime(Date newDate) {
        this.lastRecordCreateAt = newDate;
        Prefs.setLastRunTime(this.task, newDate.getTime());
    }

    public Date getLastRunTime() {
        return this.lastRecordCreateAt;
    }

    public ParseQuery createQuery(int limit) {
        return ParseQueryUtil.createQueryForNewRecord(this.lastRecordCreateAt, limit);
    }
}
