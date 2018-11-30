package com.fungo.netgo.task;

/**
 * @author Pinger
 * @since 18-10-24 上午9:37
 * <p>
 * 定义执行任务的对象和优先级
 */
public class PriorityObject<E> {

    public final int priority;
    public final E obj;

    public PriorityObject(int priority, E obj) {
        this.priority = priority;
        this.obj = obj;
    }
}
