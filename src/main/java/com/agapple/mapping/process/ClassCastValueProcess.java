package com.agapple.mapping.process;

import com.agapple.mapping.BeanMappingException;
import com.agapple.mapping.config.BeanMappingField;
import com.agapple.mapping.introspect.MapGetExecutor;
import com.agapple.mapping.introspect.PropertySetExecutor;
import com.agapple.mapping.process.internal.SetProcessInvocation;
import com.agapple.mapping.process.internal.SetValueProcess;

/**
 * set流程处理, {@linkplain PropertySetExecutor}和{@linkplain MapGetExecutor}操作时检查下目标的class和当前的value.getClass()是否相同
 * 
 * <pre>
 * 在copy模式下，使用该process，可直接忽略类型不匹配的属性
 * 在mapping模式下，<stong>不建议使用该process</strong>，对不能正常匹配的属性应该抛出ClassCastException，让使用者进行排查
 * </pre>
 * 
 * @author jianghang 2011-5-29 上午12:14:51
 */
public class ClassCastValueProcess implements SetValueProcess {

    public Object process(Object value, SetProcessInvocation setInvocation) throws BeanMappingException {
        BeanMappingField field = setInvocation.getContext().getCurrentField();
        if (field.getTargetClass() != null && value != null) {
            if (checkcast(value.getClass(), field.getTargetClass()) == false) {
                // 不调用invocation.proceed()，直接退出
                return value;
            }
        }
        return setInvocation.proceed(value);
    }

    private boolean checkcast(Class src, Class target) {
        if (target.isAssignableFrom(src)) {// 如果src是target的子类，可以向上转型，没问题
            return true;
        }

        if (src == target) { // 两个类相等
            return true;
        }
        // 进行原始类型转化
        src = mapper(src);
        target = mapper(target);

        if (src == target) { // 两个类相等
            return true;
        }

        return false;

    }

    private Class mapper(Class src) {
        if (src.isPrimitive()) {
            if (src == int.class) {
                return Integer.class;
            } else if (src == short.class) {
                return Short.class;
            } else if (src == long.class) {
                return Long.class;
            } else if (src == char.class) {
                return Character.class;
            } else if (src == double.class) {
                return Double.class;
            } else if (src == float.class) {
                return Float.class;
            } else if (src == byte.class) {
                return Byte.class;
            } else if (src == boolean.class) {
                return Boolean.class;
            }
        }
        return src;
    }

}
