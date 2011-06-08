/*
 * Copyright 1999-2004 Alibaba.com All right reserved. This software is the confidential and proprietary information of
 * Alibaba.com ("Confidential Information"). You shall not disclose such Confidential Information and shall use it only
 * in accordance with the terms of the license agreement you entered into with Alibaba.com.
 */
package com.agapple.mapping.core.introspect;

/**
 * 批量操作的抽象父类
 * 
 * @author jianghang 2011-6-2 下午04:57:47
 */
public abstract class AbstractBatchExecutor implements BatchExecutor {

    protected final Class<?> objectClass; // 操作object class

    public AbstractBatchExecutor(Class<?> theClass){
        this.objectClass = theClass;
    }

    public abstract boolean isAlive();

    public Class<?> getObjectClass() {
        return objectClass;
    }

}
