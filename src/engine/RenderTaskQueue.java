package engine;

import java.util.concurrent.ConcurrentLinkedQueue;

public class RenderTaskQueue {
    private static final ConcurrentLinkedQueue<Runnable> tasks = new ConcurrentLinkedQueue<>();

    public static void addTask(Runnable task) {
        tasks.add(task);
    }

    public static void processTasks() {
        Runnable task;
        while ((task = tasks.poll()) != null) {
            task.run();
        }
    }
}
