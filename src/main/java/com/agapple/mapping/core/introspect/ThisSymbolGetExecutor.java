package com.agapple.mapping.core.introspect;

import com.agapple.mapping.core.BeanMappingException;

/**
 * 处理下特殊符号的get操作，比如针对"this"返回当前对象的引用
 * 
 * @author jianghang 2011-6-27 下午07:58:25
 */
public class ThisSymbolGetExecutor extends AbstractExecutor implements GetExecutor {

    private static final int NOT  = 0;
    private static final int THIS = 1;
    private int              sign = NOT;

    public ThisSymbolGetExecutor(Introspector is, Class<?> clazz, String key){
        super(clazz, key);
        sign = discover(property);
    }

    @Override
    public Object invoke(Object obj) throws BeanMappingException {
        switch (sign) {
            case THIS:
                return obj;

            default:
                throw new BeanMappingException("error sign");
        }
    }

    public static int discover(String key) {
        if ("this".equalsIgnoreCase(key)) { // 处理下this指针
            return THIS;
        }

        return NOT;
    }

    @Override
    public boolean isAlive() {
        return sign != NOT;
    }

}
