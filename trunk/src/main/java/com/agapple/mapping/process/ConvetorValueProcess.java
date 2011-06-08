package com.agapple.mapping.process;

import org.apache.commons.lang.StringUtils;

import com.agapple.mapping.BeanMappingException;
import com.agapple.mapping.config.BeanMappingField;
import com.agapple.mapping.convert.Convertor;
import com.agapple.mapping.convert.ConvertorHelper;
import com.agapple.mapping.process.internal.SetProcessInvocation;
import com.agapple.mapping.process.internal.SetValueProcess;

/**
 * {@linkplain Convetor}转化的处理器,set流程处理
 * 
 * @author jianghang 2011-5-27 下午09:30:40
 */
public class ConvetorValueProcess implements SetValueProcess {

    @Override
    public Object process(Object value, SetProcessInvocation setInvocation) throws BeanMappingException {
        if (value != null) {
            BeanMappingField currentField = setInvocation.getContext().getCurrentField();
            String customConvertorName = currentField.getConvertor();
            Convertor convertor = null;
            if (StringUtils.isNotEmpty(customConvertorName)) { // 判断是否有自定义的convertor
                convertor = ConvertorHelper.getInstance().getConvertor(customConvertorName);
            } else {
                // srcClass针对直接使用script的情况，会出现为空，这时候需要依赖value.getClass进行转化
                // 优先不选择使用value.getClass()的原因：原生类型会返回对应的Object类型，导出会出现不必要的convetor转化
                Class srcClass = currentField.getSrcClass();
                if (srcClass == null) {
                    srcClass = value.getClass();
                }
                convertor = ConvertorHelper.getInstance().getConvertor(srcClass, currentField.getTargetClass());
            }

            if (convertor != null) {
                value = convertor.convert(value, currentField.getTargetClass());
            }
        }

        // 继续下一步的调用
        return setInvocation.proceed(value);
    }

}
