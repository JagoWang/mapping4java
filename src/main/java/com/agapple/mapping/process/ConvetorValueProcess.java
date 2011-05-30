package com.agapple.mapping.process;

import org.apache.commons.lang.StringUtils;

import com.agapple.mapping.BeanMappingException;
import com.agapple.mapping.config.BeanMappingField;
import com.agapple.mapping.convert.Convertor;
import com.agapple.mapping.convert.ConvertorHelper;
import com.agapple.mapping.process.internal.SetProcessInvocation;
import com.agapple.mapping.process.internal.ValueProcessSupport;

/**
 * {@linkplain Convetor}转化的处理器,set流程处理
 * 
 * @author jianghang 2011-5-27 下午09:30:40
 */
public class ConvetorValueProcess extends ValueProcessSupport {

    @Override
    public Object setProcess(Object value, SetProcessInvocation setInvocation) throws BeanMappingException {
        if (value != null) {
            BeanMappingField currentField = setInvocation.getContext().getCurrentField();
            String customConvertorName = currentField.getConvertor();
            Convertor convertor = null;
            if (StringUtils.isNotEmpty(customConvertorName)) { // 判断是否有自定义的convertor
                convertor = ConvertorHelper.getInstance().getConvertor(customConvertorName);
            } else {
                convertor = ConvertorHelper.getInstance().getConvertor(value.getClass(), currentField.getTargetClass());
            }

            if (convertor != null) {
                value = convertor.convert(value, currentField.getTargetClass());
            }
        }

        // 继续下一步的调用
        return setInvocation.proceed(value);
    }

}
