/*
 * Copyright 1999-2004 Alibaba.com All right reserved. This software is the confidential and proprietary information of
 * Alibaba.com ("Confidential Information"). You shall not disclose such Confidential Information and shall use it only
 * in accordance with the terms of the license agreement you entered into with Alibaba.com.
 */
package com.agapple.mapping.helper;

import java.util.LinkedList;

/**
 * 批量处理对象的holder处理
 * 
 * @author jianghang 2011-6-2 下午12:44:26
 */
public class BatchObjectHolder {

    // public ThreadLocal<StackQueue> objectRefs = new ThreadLocal() {
    //
    // protected Object initialValue() {
    // return new StackQueue();
    // }
    //
    // };
    private StackQueue sq = new StackQueue();

    public StackQueue get() {
        return sq;
    }

    public void clear() {
        sq.clear();
    }

    /**
     * 简单定义stack(queue)，提供先进先出的功能，同时支持压栈的模式(比如递归调用时，需要临时先将当前的Queue压入底部，所有操作在新的Queue上操作)
     * 
     * <pre>
     * 选择使用Deque实现stack/queue的原因：不存在数据复制,插入O(1)。因为大量的操作都是顺序读和写
     * </pre>
     */
    public static class StackQueue {

        private LinkedList<LinkedList<Object>> stack = new LinkedList<LinkedList<Object>>();
        private LinkedList<Object>             queue;

        public StackQueue(){
            queue = new LinkedList<Object>(); // 当前的queue切换到下一个节点
            pushStack();// 先压入一个节点
        }

        public void pushStack() {
            stack.offerLast(queue); // 老节点入栈
            queue = new LinkedList<Object>(); // 当前的queue切换到下一个节点
        }

        public void popStack() {
            queue = stack.removeLast();// 删除最后一个节点，并切换queue为上一个节点
        }

        public Object poll() {// 获取头数据,并删除节点
            return queue.pollFirst();
        }

        public void offer(Object value) { // 添加到末尾
            queue.offerLast(value);
        }

        public void clear() {
            if (queue != null) {
                queue.clear();
            }
            if (stack != null) {
                stack.clear();
            }
        }
    }

}
