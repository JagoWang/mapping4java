package com.agapple.mapping.core.process;

import java.util.List;

import com.agapple.mapping.core.BeanMappingException;
import com.agapple.mapping.core.introspect.GetExecutor;

/**
 * ValueProcess执行的get操作的控制器
 * 
 * @author jianghang 2011-5-30 上午07:44:02
 */
public class GetProcessInvocation {

    private ValueProcessContext   context;          // valueProcess执行的上下文参数
    private List<GetValueProcess> processes;        // 处理的process列表
    private GetExecutor           executor;         // get方法调用
    private int                   currentIndex = -1; // 当前执行的valueProcess下标

    public GetProcessInvocation(GetExecutor executor, ValueProcessContext context, List<GetValueProcess> processes){
        this.executor = executor;
        this.context = context;
        this.processes = processes;
    }

    public Object proceed() throws BeanMappingException {
        if (processes == null) { // 如果处理列表为空，则直接调用
            return invokeExecutor();
        } else {
            if (this.currentIndex == this.processes.size() - 1) {
                return invokeExecutor();
            } else {
                GetValueProcess vp = this.processes.get(++this.currentIndex);
                return vp.process(this);
            }
        }

    }

    protected Object invokeExecutor() {
        if (isBatch()) { // 如果是batch模式
            return context.getHolder().getNext();
        }

        if (executor != null) {
            return executor.invoke(context.getParam().getSrcRef());
        } else {
            return null;
        }
    }

    /**
     * 判断一下是否处于batch处理模式
     */
    private boolean isBatch() {
        return context.getBeanObject().isBatch() && context.getBeanObject().getGetBatchExecutor() != null;
    }

    // =================== get 操作===============

    public ValueProcessContext getContext() {
        return context;
    }

}
