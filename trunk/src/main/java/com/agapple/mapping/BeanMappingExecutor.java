package com.agapple.mapping;

import java.util.HashMap;

import com.agapple.mapping.config.BeanMappingConfigHelper;
import com.agapple.mapping.config.BeanMappingField;
import com.agapple.mapping.config.BeanMappingObject;
import com.agapple.mapping.introspect.AbstractExecutor;
import com.agapple.mapping.introspect.GetExecutor;
import com.agapple.mapping.introspect.MapSetExecutor;
import com.agapple.mapping.introspect.SetExecutor;
import com.agapple.mapping.introspect.Uberspector;
import com.agapple.mapping.process.internal.GetProcessInvocation;
import com.agapple.mapping.process.internal.SetProcessInvocation;
import com.agapple.mapping.process.internal.ValueProcessContext;

/**
 * Bean mapping具体的执行器
 * 
 * @author jianghang 2011-5-26 下午04:27:35
 */
public class BeanMappingExecutor {

    /**
     * 根据传递的param，进行mapping处理
     */
    public void execute(BeanMappingParam param) {
        BeanMappingObject config = param.getConfig();
        for (final BeanMappingField beanField : config.getBeanFields()) {
            if (beanField.isMapping()) {
                doBeanMapping(param, beanField);
            } else {
                doFieldMapping(param, beanField);
            }
        }
    }

    /**
     * 处理下模型的field的mapping动作
     */
    private void doFieldMapping(BeanMappingParam param, final BeanMappingField beanField) {
        // 定义valueContext
        ValueProcessContext valueContext = new ValueProcessContext(param, param.getConfig(), beanField,
                                                                   param.getCustomValueContext());
        // 设置getExecutor
        GetExecutor getExecutor = Uberspector.getInstance().getGetExecutor(param.getSrcRef(), beanField.getSrcName());

        // 设置setExecutor
        SetExecutor setExecutor = Uberspector.getInstance().getSetExecutor(param.getTargetRef(),
                                                                           beanField.getTargetName(),
                                                                           beanField.getTargetClass());
        // 处理Convertor
        if (beanField.getTargetClass() == null) {
            // 设置为自动提取的targetClasss
            if (setExecutor instanceof MapSetExecutor) {
                beanField.setTargetClass(String.class);// 针对Map处理,属性的赋值默认设置为String.class
            } else {
                beanField.setTargetClass(getTargetClass(setExecutor));
            }
        }

        // 获取get结果
        GetProcessInvocation getInvocation = new GetProcessInvocation(getExecutor, valueContext, param.getProcesses());
        Object getResult = getInvocation.proceed();

        // 执行set
        SetProcessInvocation setInvocation = new SetProcessInvocation(setExecutor, valueContext, param.getProcesses());
        setInvocation.proceed(getResult);
    }

    /**
     * 处理下子模型的嵌套mapping动作
     */
    private void doBeanMapping(BeanMappingParam param, final BeanMappingField beanField) {
        // 定义valueContext
        ValueProcessContext valueContext = new ValueProcessContext(param, param.getConfig(), beanField,
                                                                   param.getCustomValueContext());
        // 检查一下targetClass是否有设置，针对bean对象有效
        // 如果目标对象是map，需要客户端强制设定targetClass
        SetExecutor setExecutor = Uberspector.getInstance().getSetExecutor(param.getTargetRef(),
                                                                           beanField.getTargetName(),
                                                                           beanField.getTargetClass());
        GetExecutor getExecutor = Uberspector.getInstance().getGetExecutor(param.getSrcRef(), beanField.getSrcName());
        if (beanField.getTargetClass() == null) {
            if (setExecutor instanceof MapSetExecutor) {
                beanField.setTargetClass(HashMap.class);// 针对Map处理,嵌套代码的复制默认设置为HashMap.class
            } else {
                beanField.setTargetClass(getTargetClass(setExecutor));
            }
        }
        // 获取新的srcRef
        // 获取get结果
        GetProcessInvocation getInvocation = new GetProcessInvocation(getExecutor, valueContext, param.getProcesses());
        Object srcRef = getInvocation.proceed();
        if (srcRef == null) { // 如果嵌套对象为null，则直接略过该对象处理，目标对象也为null
            return;
        }

        beanField.setSrcClass(srcRef.getClass());// 设置为srcClass
        if (beanField.getSrcClass() == null || beanField.getTargetClass() == null) {
            throw new BeanMappingException("srcClass or targetClass is null , " + beanField.toString());
        }
        BeanMappingObject object = BeanMappingConfigHelper.getInstance().getBeanMappingObject(
                                                                                              beanField.getSrcClass(),
                                                                                              beanField.getTargetClass());
        if (object == null) {
            throw new BeanMappingException("no bean mapping config for " + beanField.toString());
        }

        // 执行set,反射构造一个子Model
        SetProcessInvocation setInvocation = new SetProcessInvocation(setExecutor, valueContext, param.getProcesses());
        Object value = setInvocation.proceed(null); // 在目标节点对象上，创建一个子节点

        BeanMappingParam newParam = new BeanMappingParam();
        newParam.setTargetRef(value);// 为新创建的子model，注意使用value，可以在SetValueProcess中会创建新对象
        newParam.setSrcRef(srcRef);
        newParam.setConfig(object);
        // 复制并传递
        newParam.setProcesses(param.getProcesses());
        // 进行递归调用
        execute(newParam);
    }

    /**
     * 根据{@linkplain SetExecutor}获取对应的目标targetClass
     */
    private Class getTargetClass(SetExecutor setExecutor) {
        Class[] params = ((AbstractExecutor) setExecutor).getMethod().getParameterTypes();
        if (params == null || params.length != 1) {
            throw new BeanMappingException("illegal set method[" + ((AbstractExecutor) setExecutor).getMethodName()
                                           + "] for ParameterType");
        }
        return params[0];
    }

}
