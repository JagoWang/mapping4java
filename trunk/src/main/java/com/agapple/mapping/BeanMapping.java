/*
 * Copyright 1999-2004 Alibaba.com All right reserved. This software is the confidential and proprietary information of
 * Alibaba.com ("Confidential Information"). You shall not disclose such Confidential Information and shall use it only
 * in accordance with the terms of the license agreement you entered into with Alibaba.com.
 */
package com.agapple.mapping;

import java.util.Arrays;

import com.agapple.mapping.core.BeanMappingException;
import com.agapple.mapping.core.BeanMappingExecutor;
import com.agapple.mapping.core.BeanMappingParam;
import com.agapple.mapping.core.config.BeanMappingConfigHelper;
import com.agapple.mapping.core.config.BeanMappingObject;
import com.agapple.mapping.process.BeanCreatorValueProcess;
import com.agapple.mapping.process.ConvetorValueProcess;
import com.agapple.mapping.process.DefaultValueValueProcess;
import com.agapple.mapping.process.ScriptValueProcess;
import com.agapple.mapping.process.internal.GetValueProcess;
import com.agapple.mapping.process.internal.SetValueProcess;

/**
 * Bean Mapping操作的处理单元
 * 
 * <pre>
 * <code>
 * 使用例子：
 *  BeanMapping beanMapping = BeanMapping.create(srcClass,targetClass);
 *  beanMapping.mapping(src,target);// 将src的属性mapping到target
 *  
 *  注意：srcClass/targetClass的映射关系必须实现通过{@linkplain BeanMappingConfigHelper}的registerConfig方法注册mapping配置
 * </code>
 * </pre>
 * 
 * @author jianghang 2011-6-8 上午11:10:24
 */
public class BeanMapping {

    private static final SetValueProcess beanCreatorValueProcess  = new BeanCreatorValueProcess();
    private static final SetValueProcess convetorValueProcess     = new ConvetorValueProcess();
    private static final GetValueProcess scriptValueProcess       = new ScriptValueProcess();
    private static final GetValueProcess defaultValueValueProcess = new DefaultValueValueProcess();
    private BeanMappingObject            config;                                                   // 对应的Bean Mapping配置

    public BeanMapping(BeanMappingObject config){
        this.config = config;
    }

    /**
     * 创建srcClass和targetClass之间的BeanMapping操作
     */
    public static BeanMapping create(Class srcClass, Class targetClass) {
        BeanMappingObject config = BeanMappingConfigHelper.getInstance().getBeanMappingObject(srcClass, targetClass);
        return new BeanMapping(config);
    }

    /**
     * 根据定义的bean-mapping配置进行对象属性的mapping拷贝 , 允许自定义{@linkplain GetValueProcess} {@linkplain SetValueProcess}
     * 
     * @param src
     * @param target
     * @throws BeanMappingException
     */
    public void mapping(Object src, Object target) throws BeanMappingException {
        BeanMappingParam param = new BeanMappingParam();
        param.setSrcRef(src);
        param.setTargetRef(target);
        param.setConfig(this.config);
        param.setSetProcesses(Arrays.asList(beanCreatorValueProcess, convetorValueProcess));
        param.setGetProcesses(Arrays.asList(scriptValueProcess, defaultValueValueProcess));
        // 执行mapping处理
        BeanMappingExecutor.execute(param);
    }

}
