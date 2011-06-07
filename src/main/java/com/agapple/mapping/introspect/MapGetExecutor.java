package com.agapple.mapping.introspect;

import java.util.Map;

import net.sf.cglib.reflect.FastClass;
import net.sf.cglib.reflect.FastMethod;

import com.agapple.mapping.BeanMappingException;

/**
 * 基于map的属性get动作处理
 * 
 * @author jianghang 2011-5-25 上午11:32:33
 */
public class MapGetExecutor extends AbstractExecutor implements GetExecutor {

    private static FastMethod MAP_GET;
    private final Object      property;

    static {
        // 初始化map的get方法
        FastClass ft = FastClass.create(Map.class);
        MAP_GET = ft.getMethod("get", new Class[] { Object.class });
    }

    public MapGetExecutor(Introspector is, Class<?> clazz, Object key){
        super(clazz, discover(clazz));
        property = key;
    }

    public Object getTargetProperty() {
        return property;
    }

    @Override
    public Object invoke(Object obj) throws BeanMappingException {
        final Map<Object, ?> map = (Map<Object, ?>) obj;
        return map.get(property);
    }

    public static FastMethod discover(Class<?> clazz) {
        return (Map.class.isAssignableFrom(clazz)) ? MAP_GET : null;
    }

}
