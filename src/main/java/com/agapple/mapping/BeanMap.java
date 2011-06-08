/*
 * Copyright 1999-2004 Alibaba.com All right reserved. This software is the confidential and proprietary information of
 * Alibaba.com ("Confidential Information"). You shall not disclose such Confidential Information and shall use it only
 * in accordance with the terms of the license agreement you entered into with Alibaba.com.
 */
package com.agapple.mapping;

import java.util.HashMap;
import java.util.Map;

import com.agapple.mapping.core.BeanMappingException;
import com.agapple.mapping.core.BeanMappingExecutor;
import com.agapple.mapping.core.BeanMappingParam;
import com.agapple.mapping.core.config.BeanMappingConfigHelper;
import com.agapple.mapping.core.config.BeanMappingObject;

/**
 * Bean<->Map操作的处理单元
 * 
 * @author jianghang 2011-6-8 上午11:11:13
 */
public class BeanMap {

    private BeanMappingObject config; // 对应的Bean Mapping配置

    public BeanMap(BeanMappingObject config){
        this.config = config;
    }

    /**
     * 创建srcClass和targetClass之间的BeanMapping操作
     */
    public static BeanMap create(Class srcClass, Class targetClass) {
        BeanMappingObject config = BeanMappingConfigHelper.getInstance().getBeanMappingObject(srcClass, targetClass,
                                                                                              true);
        return new BeanMap(config);
    }

    /**
     * 将bean的属性转化为Map对象
     * 
     * @param src
     * @return
     * @throws BeanMappingException
     */
    public Map describe(Object src) throws BeanMappingException {
        Map result = new HashMap();
        BeanMappingParam param = new BeanMappingParam();
        param.setSrcRef(src);
        param.setTargetRef(result);
        param.setConfig(this.config);
        // 执行mapping处理
        BeanMappingExecutor.execute(param);
        return result;
    }

    /**
     * 将map的属性映射到bean对象
     * 
     * @param target
     * @param properties
     * @throws BeanMappingException
     */
    public void populate(Object target, Map properties) throws BeanMappingException {
        BeanMappingParam param = new BeanMappingParam();
        param.setSrcRef(properties);
        param.setTargetRef(target);
        param.setConfig(this.config);
        // 执行mapping处理
        BeanMappingExecutor.execute(param);
    }
}
