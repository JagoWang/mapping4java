package com.agapple.mapping.core.introspect;

import java.lang.ref.SoftReference;

import com.agapple.mapping.core.BeanMappingException;

/**
 * 暴露给外部的内审接口操作，外部可通过Uberspector.getInstance()进行操作
 * 
 * @author jianghang 2011-5-25 下午01:18:18
 */
public class Uberspector {

    private static volatile Uberspector singleton;
    private SoftReference<Introspector> introspector;

    public Uberspector(){
        introspector = new SoftReference(new Introspector());
    }

    public Uberspector(Introspector introspector){
        // 允许自定义传入introspector
        this.introspector = new SoftReference(introspector);
    }

    public static Uberspector getInstance() {
        if (singleton == null) {
            synchronized (Uberspector.class) {
                if (singleton == null) { // double check
                    singleton = new Uberspector();
                }
            }
        }
        return singleton;
    }

    public BatchExecutor getBatchExecutor(Class locatorClass, String[] identifier, Class[] args) {
        final Class<?> clazz = locatorClass;
        // 尝试一下map处理
        MapBatchExecutor mBatchExecutor = new MapBatchExecutor(getIntrospector(), clazz, identifier);
        if (mBatchExecutor.isAlive()) {
            return mBatchExecutor;
        }

        // 尝试一下bean处理
        if (identifier != null) {
            PropertyBatchExecutor pBatchExecutor = new PropertyBatchExecutor(getIntrospector(), clazz, identifier, args);
            if (pBatchExecutor.isAlive()) {
                return pBatchExecutor;
            }
        }

        // 如果没有匹配到executor，则返回null，不做batch优化
        return null;
    }

    public GetExecutor getGetExecutor(Class locatorClass, Object identifier) {
        final Class<?> clazz = locatorClass;
        final String property = (identifier == null ? null : identifier.toString());

        // 尝试一下bean处理
        if (property != null) {
            PropertyGetExecutor pExecutor = new PropertyGetExecutor(getIntrospector(), clazz, property);
            if (pExecutor.isAlive()) {
                return pExecutor;
            }
        }

        // 尝试一下map处理
        MapGetExecutor mExecutor = new MapGetExecutor(getIntrospector(), clazz, identifier);
        if (mExecutor.isAlive()) {
            return mExecutor;
        }

        throw new BeanMappingException("can not found GetExecutor for Class[" + clazz.getName() + "] , identifier["
                                       + identifier + "]");
    }

    public SetExecutor getSetExecutor(Class locatorClass, Object identifier, Class arg) {
        final Class<?> clazz = locatorClass;
        final String property = (identifier == null ? null : identifier.toString());

        // 尝试一下bean处理
        if (property != null) {
            PropertySetExecutor pExecutor = new PropertySetExecutor(getIntrospector(), clazz, property, arg);
            if (pExecutor.isAlive()) {
                return pExecutor;
            }
        }

        // 尝试一下map处理
        MapSetExecutor mExecutor = new MapSetExecutor(getIntrospector(), clazz, identifier, arg);
        if (mExecutor.isAlive()) {
            return mExecutor;
        }

        throw new BeanMappingException("can not found SetExecutor for Class[" + clazz.getName() + "] , identifier["
                                       + identifier + "]");
    }

    // ================= setter / getter =================

    public Introspector getIntrospector() {
        Introspector in = introspector.get();
        if (in == null) {
            introspector = new SoftReference<Introspector>(new Introspector());// 重新创建一个
            return getIntrospector();
        } else {
            return in;
        }
    }

    public void setIntrospector(Introspector introspector) {
        this.introspector = new SoftReference<Introspector>(introspector);
    }

}
