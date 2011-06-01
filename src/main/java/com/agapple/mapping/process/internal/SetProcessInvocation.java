package com.agapple.mapping.process.internal;

import java.util.List;

import com.agapple.mapping.BeanMappingException;
import com.agapple.mapping.introspect.SetExecutor;

/**
 * ValueProcess执行的set操作的控制器
 * 
 * @author jianghang 2011-5-30 上午08:11:03
 */
public class SetProcessInvocation {

    private ValueProcessContext context;          // valueProcess执行的上下文参数
    private List<ValueProcess>  processes;        // 处理的process列表
    private SetExecutor         executor;         // get方法调用
    private int                 currentIndex = -1; // 当前执行的valueProcess下标

    public SetProcessInvocation(SetExecutor executor, ValueProcessContext context, List<ValueProcess> processes){
        this.executor = executor;
        this.context = context;
        this.processes = processes;
    }

    public Object proceed(Object value) throws BeanMappingException {
        if (processes == null) { // 如果处理列表为空，则直接调用
            return invokeExecutor(value);
        } else {
            if (this.currentIndex == this.processes.size() - 1) {
                return invokeExecutor(value);
            } else {
                ValueProcess vp = this.processes.get(++this.currentIndex);
                return vp.setProcess(value, this); // 保存一下，上一次执行的结果
            }
        }
    }

    protected Object invokeExecutor(Object value) {
        if (executor == null) {
            throw new BeanMappingException("SetExecutor is null!");
        }
        return executor.invoke(context.getParam().getTargetRef(), value);
    }

    // =================== get 操作===============

    public ValueProcessContext getContext() {
        return context;
    }

}
