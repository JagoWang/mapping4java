/*
 * Copyright 1999-2004 Alibaba.com All right reserved. This software is the confidential and proprietary information of
 * Alibaba.com ("Confidential Information"). You shall not disclose such Confidential Information and shall use it only
 * in accordance with the terms of the license agreement you entered into with Alibaba.com.
 */
package com.agapple.mapping.process.internal;

import com.agapple.mapping.BeanMappingException;

/**
 * 数据处理接口，允许在set Executor执行之前处理下value。
 * 
 * @author jianghang 2011-6-7 下午08:14:53
 */
public interface SetValueProcess {

    public Object process(Object value, SetProcessInvocation invocation) throws BeanMappingException;
}
