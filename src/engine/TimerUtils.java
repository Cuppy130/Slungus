package engine;

import java.util.concurrent.*;

public class TimerUtils {

    private static final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    public static void invertval(Runnable task, long intervalMillis) {
        scheduler.scheduleAtFixedRate(task, 0, intervalMillis, TimeUnit.MILLISECONDS);
    }

    public static void timeout(Runnable task, long intervalMillis){
        scheduler.schedule(task, intervalMillis, TimeUnit.MILLISECONDS);
    }

    public static void shutdownScheduler() {
        scheduler.shutdown();
    }
}