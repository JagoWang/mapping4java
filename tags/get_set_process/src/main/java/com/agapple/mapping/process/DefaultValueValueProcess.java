package com.agapple.mapping.process;

import org.apache.commons.lang.StringUtils;

import com.agapple.mapping.core.BeanMappingException;
import com.agapple.mapping.core.config.BeanMappingField;
import com.agapple.mapping.core.process.GetProcessInvocation;
import com.agapple.mapping.core.process.GetValueProcess;
import com.agapple.mapping.process.convetor.Convertor;
import com.agapple.mapping.process.convetor.ConvertorHelper;

/**
 * mapping默认值的处理，get流程处理
 * 
 * @author jianghang 2011-5-27 下午09:19:46
 */
public class DefaultValueValueProcess implements GetValueProcess {

    @Override
    public Object process(GetProcessInvocation getInvocation) throws BeanMappingException {
        // 先调用executor
        Object value = getInvocation.proceed();
        // 处理下自己的业务
        BeanMappingField currentField = getInvocation.getContext().getCurrentField();
        if (value == null && StringUtils.isNotEmpty(currentField.getDefaultValue())) {
            if (currentField.getSrcClass() != null) {// 有指定对应的SrcClass
                Convertor convertor = ConvertorHelper.getInstance().getConvertor(String.class,
                                                                                 currentField.getSrcClass());
                if (convertor != null) {
                    // 先对String字符串进行一次转化
                    return convertor.convert(currentField.getDefaultValue(), currentField.getSrcClass());
                }
            }
            // 不存在对默认值处理的convertor，不予处理，后续尝试下自定义的convertor
            value = currentField.getDefaultValue();
        }

        return value;
    }
}
