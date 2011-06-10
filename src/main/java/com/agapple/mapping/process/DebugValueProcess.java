/*
 * Copyright 1999-2004 Alibaba.com All right reserved. This software is the confidential and proprietary information of
 * Alibaba.com ("Confidential Information"). You shall not disclose such Confidential Information and shall use it only
 * in accordance with the terms of the license agreement you entered into with Alibaba.com.
 */
package com.agapple.mapping.process;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agapple.mapping.core.BeanMappingException;
import com.agapple.mapping.core.config.BeanMappingField;
import com.agapple.mapping.core.process.ValueProcess;
import com.agapple.mapping.core.process.ValueProcessInvocation;

/**
 * 输出一些日志信息，方便排查问题
 * 
 * @author jianghang 2011-6-10 下午02:24:00
 */
public class DebugValueProcess implements ValueProcess {

    private final static Logger logger = LoggerFactory.getLogger(DebugValueProcess.class);

    @Override
    public Object process(Object value, ValueProcessInvocation invocation) throws BeanMappingException {
        BeanMappingField field = invocation.getContext().getCurrentField();
        if (logger.isDebugEnabled()) {
            StringBuilder builder = new StringBuilder();
            builder.append("srcName[" + field.getSrcName());
            builder.append("],targetName[" + field.getTargetName());
            if (StringUtils.isNotEmpty(field.getDefaultValue())) {
                builder.append("],[defaultValue=" + field.getDefaultValue());
            }
            if (StringUtils.isNotEmpty(field.getConvertor())) {
                builder.append("],[convetor=" + field.getConvertor());
            }
            if (StringUtils.isNotEmpty(field.getScript())) {
                builder.append("],[script=" + field.getScript());
            }
            builder.append("], Value = " + ObjectUtils.toString(value, "null"));
            logger.debug(builder.toString());
        }
        return invocation.proceed(value); // 继续传递
    }
}
