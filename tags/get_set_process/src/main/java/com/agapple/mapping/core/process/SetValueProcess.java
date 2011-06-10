package com.agapple.mapping.core.process;

import com.agapple.mapping.core.BeanMappingException;

/**
 * 数据处理接口，允许在set Executor执行之前处理下value。
 * 
 * @author jianghang 2011-6-7 下午08:14:53
 */
public interface SetValueProcess {

    public Object process(Object value, SetProcessInvocation invocation) throws BeanMappingException;
}
