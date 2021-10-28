/*
 * Mozilla Public License v2.0
 *
 * Copyright (C) 2021 DarkKronicle
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package io.github.darkkronicle.advancedchatcore.util;

import java.util.TreeSet;
import lombok.AllArgsConstructor;
import lombok.Value;
import org.jetbrains.annotations.NotNull;

// Referenced
// https://github.com/vacla/Watson/blob/fabric_1.16.2/src/main/java/eu/minemania/watson/scheduler/SyncTaskQueue.java

/** A queue to handle delayed tasks in ticks. */
public class SyncTaskQueue {

    private static final SyncTaskQueue INSTANCE = new SyncTaskQueue();

    public static SyncTaskQueue getInstance() {
        return INSTANCE;
    }

    private int lastTick = 0;

    /** A task that contains a time to trigger and a {@link Runnable} for when it should happen. */
    @Value
    @AllArgsConstructor
    public static class QueuedTask implements Comparable<QueuedTask> {

        /**
         * Tick number when it should be triggered. This isn't delay, this is based off of the
         * current tick value in {@link net.minecraft.client.gui.hud.InGameHud}
         */
        int tick;

        /** {@link Runnable} to run when the task is called. */
        Runnable task;

        @Override
        public int compareTo(@NotNull SyncTaskQueue.QueuedTask o) {
            // Compares when it should happen. Used to ensure that the first in the stack is what
            // needs to
            // happen.
            return Integer.compare(tick, o.tick);
        }
    }

    // Use TreeSet to automagically sort by when it needs to happen
    private final TreeSet<QueuedTask> queue = new TreeSet<>();

    /**
     * Add's a new task to do after a certain amount of ticks
     *
     * @param after Delay in ticks
     * @param runnable What to run when it should be called
     */
    public void add(int after, Runnable runnable) {
        queue.add(new QueuedTask(lastTick + after, runnable));
    }

    /**
     * Updates the queue with the tick. This shouldn't be called outside of the core.
     *
     * @param tick Current time in ticks.
     */
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
