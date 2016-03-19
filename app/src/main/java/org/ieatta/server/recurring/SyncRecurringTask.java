package org.ieatta.server.recurring;

import java.text.DateFormat;
import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class SyncRecurringTask {
    final static DateFormat fmt = DateFormat.getTimeInstance(DateFormat.LONG);

    // Create a scheduled thread pool with 5 core threads
    ScheduledThreadPoolExecutor sch = (ScheduledThreadPoolExecutor)
            Executors.newScheduledThreadPool(5);

    // Create a task for one-shot execution using schedule()
    Runnable oneShotTask = new Runnable() {
        @Override
        public void run() {
            System.out.println("\t oneShotTask Execution Time: "
                    + fmt.format(new Date()));
        }
    };

    // Create another task
    Runnable delayTask = new Runnable() {
        @Override
        public void run() {
            try {
                System.out.println("\t delayTask Execution Time: "
                        + fmt.format(new Date()));
                Thread.sleep(10 * 1000);
                System.out.println("\t delayTask End Time: "
                        + fmt.format(new Date()));
            } catch (Exception e) {

            }
        }
    };

    // And yet another
    Runnable periodicTask = new Runnable() {
        @Override
        public void run() {
            try {
                everyTask();
                Thread.sleep(10 * 1000);
            } catch (Exception e) {
            }
        }
    };


    private void everyTask() {
        SyncHandler.sharedInstance.startTask();
    }

    public void prepareTimer() {
        //      ScheduledFuture<?> oneShotFuture = sch.schedule(oneShotTask, 5, TimeUnit.SECONDS);
        //      ScheduledFuture<?> delayFuture = sch.scheduleWithFixedDelay(delayTask, 5, 5, TimeUnit.SECONDS);
        ScheduledFuture<?> periodicFuture = sch.scheduleAtFixedRate(periodicTask, 5, 5, TimeUnit.SECONDS);
    }
}
