package com.agapple.mapping.process;

import org.apache.commons.lang.StringUtils;

import com.agapple.mapping.core.BeanMappingException;
import com.agapple.mapping.core.config.BeanMappingField;
import com.agapple.mapping.core.process.ValueProcess;
import com.agapple.mapping.core.process.ValueProcessInvocation;
import com.agapple.mapping.process.convetor.Convertor;
import com.agapple.mapping.process.convetor.ConvertorHelper;

/**
 * {@linkplain Convetor}转化的处理器,set流程处理
 * 
 * @author jianghang 2011-5-27 下午09:30:40
 */
public class ConvetorValueProcess implements ValueProcess {

    @Override
    public Object process(Object value, ValueProcessInvocation invocation) throws BeanMappingException {
        if (value != null) {
            BeanMappingField currentField = invocation.getContext().getCurrentField();
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
                if (currentField.getTargetClass() != null) {
                    // targetClass可能存在为空，比如这里的Value配置了DefaultValue，在MapSetExecutor解析时会无法识别TargetClass
                    // 无法识别后，就不做转化
                    convertor = ConvertorHelper.getInstance().getConvertor(srcClass, currentField.getTargetClass());
                }
            }

            if (convertor != null && currentField.getTargetClass() != null) {
                value = convertor.convert(value, currentField.getTargetClass());
            }
        }

        // 继续下一步的调用
        return invocation.proceed(value);
    }

}
