package engine;

import java.util.concurrent.*;

public class TimerUtils {

    private static final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    public static ScheduledFuture<?> interval(Runnable task, long intervalMillis) {
        return scheduler.scheduleAtFixedRate(task, 0, intervalMillis, TimeUnit.MILLISECONDS);
    }

    public static void timeout(Runnable task, long delayMillis) {
        scheduler.schedule(task, delayMillis, TimeUnit.MILLISECONDS);
    }

    public static void stopInterval(ScheduledFuture<?> future) {
        if (future != null && !future.isCancelled()) {
            future.cancel(true);
        }
    }

    public static void shutdownScheduler() {
        scheduler.shutdown();
    }
}
