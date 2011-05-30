package com.agapple.mapping.process;

import com.agapple.mapping.BeanMappingException;
import com.agapple.mapping.config.BeanMappingField;
import com.agapple.mapping.helper.ReflectionHelper;
import com.agapple.mapping.process.internal.SetProcessInvocation;
import com.agapple.mapping.process.internal.ValueProcessSupport;

/**
 * set操作流程中, 尝试创建一下嵌套的bean实例，通过反射newInstance,
 * 
 * @author jianghang 2011-5-28 下午11:32:38
 */
public class BeanCreatorValueProcess extends ValueProcessSupport {

    @Override
    public Object setProcess(Object value, SetProcessInvocation setInvocation) throws BeanMappingException {
        BeanMappingField currentField = setInvocation.getContext().getCurrentField();
        if (value == null && currentField.isMapping()) {// 判断下是否在处理嵌套的mapping
            value = ReflectionHelper.newInstance(setInvocation.getContext().getCurrentField().getTargetClass());
        }

        // 继续下一步的调用
        return setInvocation.proceed(value);
    }

}
