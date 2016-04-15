package org.ieatta.database.utils;

import com.lukazakrajsek.timeago.TimeAgo;

import org.ieatta.IEAApp;

import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

public class DBUtil {
    public static String getUUID() {
        return UUID.randomUUID().toString();
    }

    public static String getTimeAgoString(Date date) {
        TimeAgo timeAgo = new TimeAgo(IEAApp.getInstance());

        return timeAgo.timeAgo(date);
    }

    public static Date getNextHourDate() {
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        c.add(Calendar.HOUR, 1);
        return c.getTime();
    }

}
