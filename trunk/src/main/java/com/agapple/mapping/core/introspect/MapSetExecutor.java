package com.agapple.mapping.core.introspect;

import java.util.Map;

import net.sf.cglib.reflect.FastClass;
import net.sf.cglib.reflect.FastMethod;

import com.agapple.mapping.core.BeanMappingException;

/**
 * 基于map的set操作
 * 
 * @author jianghang 2011-5-25 下午01:02:03
 */
public class MapSetExecutor extends AbstractExecutor implements SetExecutor {

    private static FastMethod MAP_SET;
    private final Object      property;

    static {
        // 初始化map的get方法
        FastClass ft = FastClass.create(Map.class);
        MAP_SET = ft.getMethod("put", new Class[] { Object.class, Object.class });
    }

    public MapSetExecutor(Introspector is, Class<?> clazz, Object key, Class arg){
        super(clazz, discover(clazz));
        property = key;
    }

    public Object getTargetProperty() {
        return property;
    }

    @Override
    public Object invoke(Object obj, Object value) throws BeanMappingException {
        final Map<Object, Object> map = ((Map<Object, Object>) obj);
        map.put(property, value);
        return value;
    }

    public static FastMethod discover(Class<?> clazz) {
        return (Map.class.isAssignableFrom(clazz)) ? MAP_SET : null;
    }
}
