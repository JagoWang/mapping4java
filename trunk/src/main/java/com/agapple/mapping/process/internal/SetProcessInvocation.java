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

    private ValueProcessContext   context;          // valueProcess执行的上下文参数
    private List<SetValueProcess> processes;        // 处理的process列表
    private SetExecutor           executor;         // get方法调用
    private int                   currentIndex = -1; // 当前执行的valueProcess下标

    public SetProcessInvocation(SetExecutor executor, ValueProcessContext context, List<SetValueProcess> processes){
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
                SetValueProcess vp = this.processes.get(++this.currentIndex);
                return vp.process(value, this); // 保存一下，上一次执行的结果
            }
        }
    }

    protected Object invokeExecutor(Object value) {
        if (isBatch()) { // 如果是batch模式
            context.getHolder().setObject(value); // 更新下holder的value值
            return value;
        }

        if (executor == null) {
            throw new BeanMappingException("SetExecutor is null!");
        }
        return executor.invoke(context.getParam().getTargetRef(), value);
    }

    /**
     * 判断一下是否处于batch处理模式
     */
    private boolean isBatch() {
        return context.getBeanObject().isBatch() && context.getBeanObject().getSetBatchExecutor() != null;
    }

    // =================== get 操作===============

    public ValueProcessContext getContext() {
        return context;
    }

}
