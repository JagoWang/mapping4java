package com.agapple.mapping.process;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.agapple.mapping.core.BeanMappingException;
import com.agapple.mapping.core.config.BeanMappingField;
import com.agapple.mapping.core.config.BeanMappingObject;
import com.agapple.mapping.core.process.GetProcessInvocation;
import com.agapple.mapping.core.process.GetValueProcess;
import com.agapple.mapping.process.script.JexlScriptContext;
import com.agapple.mapping.process.script.JexlScriptExecutor;
import com.agapple.mapping.process.script.ScriptContext;
import com.agapple.mapping.process.script.ScriptExecutor;

/**
 * 自定义script脚本的处理器 , get流程处理
 * 
 * @author jianghang 2011-5-27 下午09:25:17
 */
public class ScriptValueProcess implements GetValueProcess {

    private ScriptExecutor scriptExecutor = new JexlScriptExecutor();
    public final String    SCRIPT_CONTEXT = "_script_context";

    @Override
    public Object process(GetProcessInvocation getInvocation) throws BeanMappingException {
        BeanMappingField currentField = getInvocation.getContext().getCurrentField();
        if (StringUtils.isNotEmpty(currentField.getScript())) {
            BeanMappingObject beanObject = getInvocation.getContext().getBeanObject();

            Map param = new HashMap();
            param.put(beanObject.getSrcKey(), getInvocation.getContext().getParam().getSrcRef());
            param.put(beanObject.getTargetKey(), getInvocation.getContext().getParam().getTargetRef());

            Map custom = getInvocation.getContext().getCustom();
            if (custom != null && custom.containsKey(SCRIPT_CONTEXT)) {
                Map newParam = (Map) custom.get(SCRIPT_CONTEXT);
                param.putAll(newParam);
            }

            ScriptContext scriptContext = new JexlScriptContext(param);
            // 进行值转化处理
            Object value = scriptExecutor.evaluate(scriptContext, currentField.getScript());
            if (value != null) {// 如果结果不为空，直接返回
                return value;
            }
        }

        // 继续走到下一步处理
        return getInvocation.proceed();

    }
}
