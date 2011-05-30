package com.agapple.mapping.process.internal;

import com.agapple.mapping.BeanMappingException;

/**
 * value process的默认实现
 * 
 * @author jianghang 2011-5-30 下午04:41:19
 */
public class ValueProcessSupport implements ValueProcess {

    public Object getProcess(GetProcessInvocation getInvocation) throws BeanMappingException {
        return getInvocation.proceed();
    }

    public Object setProcess(Object value, SetProcessInvocation setInvocation) throws BeanMappingException {
        return setInvocation.proceed(value);
    }

}
