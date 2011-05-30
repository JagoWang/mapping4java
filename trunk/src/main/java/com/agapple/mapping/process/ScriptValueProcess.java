package com.agapple.mapping.process;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.agapple.mapping.BeanMappingException;
import com.agapple.mapping.config.BeanMappingField;
import com.agapple.mapping.config.BeanMappingObject;
import com.agapple.mapping.process.internal.GetProcessInvocation;
import com.agapple.mapping.process.internal.ValueProcessSupport;
import com.agapple.mapping.script.JexlScriptContext;
import com.agapple.mapping.script.JexlScriptExecutor;
import com.agapple.mapping.script.ScriptContext;
import com.agapple.mapping.script.ScriptExecutor;

/**
 * 自定义script脚本的处理器 , get流程处理
 * 
 * @author jianghang 2011-5-27 下午09:25:17
 */
public class ScriptValueProcess extends ValueProcessSupport {

    private ScriptExecutor scriptExecutor = new JexlScriptExecutor();
    public final String    SCRIPT_CONTEXT = "_script_context";

    @Override
    public Object getProcess(GetProcessInvocation getInvocation) throws BeanMappingException {
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
            return scriptExecutor.evaluate(scriptContext, currentField.getScript());
        } else {
            return getInvocation.proceed();
        }

    }
}
