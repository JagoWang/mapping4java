package com.agapple.mapping.core.introspect;

import net.sf.cglib.reflect.FastMethod;

/**
 * @author jianghang 2011-5-25 上午11:21:47
 */
public class AbstractExecutor {

    protected final Class<?>   objectClass; // 操作object class
    protected final FastMethod method;     // 方法

    protected AbstractExecutor(Class<?> theClass, FastMethod theMethod){
        objectClass = theClass;
        method = theMethod;
    }

    /**
     * 判断当前executor是否可用
     * 
     * @return
     */
    public final boolean isAlive() {
        return (method != null);
    }

    public boolean isCacheable() {
        return method != null;
    }

    public Class<?> getObjectClass() {
        return objectClass;
    }

    public FastMethod getMethod() {
        return method;
    }

    public final Class<?> getTargetClass() {
        return objectClass;
    }

    public Object getTargetProperty() {
        return null;
    }

    public final String getMethodName() {
        return method.getName();
    }

}
