package io.github.darkkronicle.advancedchatcore.util;

import lombok.AllArgsConstructor;
import lombok.Value;
import org.jetbrains.annotations.NotNull;

import java.util.TreeSet;

// Refereneced https://github.com/vacla/Watson/blob/fabric_1.16.2/src/main/java/eu/minemania/watson/scheduler/SyncTaskQueue.java
public class SyncTaskQueue {

    private static final SyncTaskQueue INSTANCE = new SyncTaskQueue();

    public static SyncTaskQueue getInstance() {
        return INSTANCE;
    }

    private int lastTick = 0;

    @Value
    @AllArgsConstructor
    public static class QueuedTask implements Comparable<QueuedTask> {
        int tick;
        Runnable task;

        @Override
        public int compareTo(@NotNull SyncTaskQueue.QueuedTask o) {
            return Integer.compare(tick, o.tick);
        }
    }

    private final TreeSet<QueuedTask> queue = new TreeSet<>();

    public void add(int after, Runnable runnable) {
        queue.add(new QueuedTask(lastTick + after, runnable));
    }

    public void update(int tick) {
        lastTick = tick;
        if (queue.size() == 0) {
            return;
        }
        QueuedTask task = queue.first();
        while (task != null && task.tick <= lastTick) {
            task.task.run();
            queue.pollFirst();
            if (queue.size() == 0) {
                break;
            }
            task = queue.first();
        }
    }



}
