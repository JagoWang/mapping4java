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
import com.agapple.mapping.process.ClassCastValueProcess;
import com.agapple.mapping.process.ConvetorValueProcess;
import com.agapple.mapping.process.internal.SetValueProcess;

/**
 * Bean copy操作的处理单元
 * 
 * @author jianghang 2011-6-8 上午11:10:47
 */
public class BeanCopy {

    private static final SetValueProcess convetorValueProcess  = new ConvetorValueProcess();
    private static final SetValueProcess classCastValueProcess = new ClassCastValueProcess();
    private BeanMappingObject            config;                                             // 对应的Bean Mapping配置
    private boolean                      needConvetor;                                       // 是否需要进行convetor转化

    public BeanCopy(BeanMappingObject config, boolean needConvetor){
        this.config = config;
        this.needConvetor = needConvetor;
    }

    /**
     * 创建srcClass和targetClass之间的BeanCopy操作
     */
    public static BeanCopy create(Class srcClass, Class targetClass) {
        return create(srcClass, targetClass, false);
    }

    /**
     * 创建srcClass和targetClass之间的BeanCopy操作
     */
    public static BeanCopy create(Class srcClass, Class targetClass, boolean needConvetor) {
        BeanMappingObject config = BeanMappingConfigHelper.getInstance().getBeanMappingObject(srcClass, targetClass,
                                                                                              true);
        return new BeanCopy(config, needConvetor);
    }

    /**
     * 对象属性的拷贝，与BeanUtils , BeanCopier功能类似
     * 
     * @param src
     * @param target
     * @throws BeanMappingException
     */
    public void copy(Object src, Object target) throws BeanMappingException {
        BeanMappingParam param = new BeanMappingParam();
        param.setSrcRef(src);
        param.setTargetRef(target);
        param.setConfig(this.config);
        if (this.needConvetor) {
            param.setSetProcesses(Arrays.asList(convetorValueProcess, classCastValueProcess));
        }
        // 执行mapping处理
        BeanMappingExecutor.execute(param);
    }

    /**
     * 简单的拷贝模式，不做属性转化处理，与PropertyUtils功能类似
     * 
     * @param src
     * @param target
     */
    public void simpleCopy(Object src, Object target) throws BeanMappingException {
        BeanMappingParam param = new BeanMappingParam();
        param.setSrcRef(src);
        param.setTargetRef(target);
        param.setConfig(this.config);
        param.setSetProcesses(Arrays.asList(classCastValueProcess));
        // 执行mapping处理
        BeanMappingExecutor.execute(param);
    }

}
