package com.agapple.mapping.process.internal;

import com.agapple.mapping.BeanMappingException;

/**
 * 数据处理接口，允许在get/set Executor执行之前处理下value。
 * 
 * @author jianghang 2011-5-25 下午01:39:25
 */
public interface ValueProcess {

    public Object getProcess(GetProcessInvocation invocation) throws BeanMappingException;

    public Object setProcess(Object value, SetProcessInvocation invocation) throws BeanMappingException;

}
