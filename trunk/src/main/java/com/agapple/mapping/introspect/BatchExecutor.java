/*
 * Copyright 1999-2004 Alibaba.com All right reserved. This software is the confidential and proprietary information of
 * Alibaba.com ("Confidential Information"). You shall not disclose such Confidential Information and shall use it only
 * in accordance with the terms of the license agreement you entered into with Alibaba.com.
 */
package com.agapple.mapping.introspect;

import com.agapple.mapping.BeanMappingException;

/**
 * 针对一个obj对象，批量处理的get/set操作
 * 
 * @author jianghang 2011-5-31 下午08:41:14
 */
public interface BatchExecutor {

    Object[] gets(Object obj) throws BeanMappingException;

    void sets(Object obj, Object[] values) throws BeanMappingException;

}
