package com.fungo.netgo.task;

/**
 * @author Pinger
 * @since 18-10-24 上午9:38
 * <p>
 * Runnable执行任务体封装
 */
public class PriorityRunnable extends PriorityObject<Runnable> implements Runnable {

    public PriorityRunnable(int priority, Runnable obj) {
        super(priority, obj);
    }

    @Override
    public void run() {
        obj.run();
    }
}
