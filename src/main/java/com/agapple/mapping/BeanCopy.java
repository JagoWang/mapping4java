package com.agapple.mapping;

import java.util.Arrays;

import com.agapple.mapping.core.BeanMappingException;
import com.agapple.mapping.core.BeanMappingExecutor;
import com.agapple.mapping.core.BeanMappingParam;
import com.agapple.mapping.core.config.BeanMappingConfigHelper;
import com.agapple.mapping.core.config.BeanMappingObject;
import com.agapple.mapping.core.process.SetValueProcess;
import com.agapple.mapping.process.ClassCastValueProcess;
import com.agapple.mapping.process.ConvetorValueProcess;

/**
 * Bean copy操作的处理单元
 * 
 * <pre>
 * <code>
 * 使用例子：
 *  BeanCopy beanCopy = BeanCopy.create(srcClass , targetClass);
 *  beanCopy.copy(src,target);//完成copy动作
 * </code>
 * </pre>
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
            param.setSetProcesses(Arrays.asList(convetorValueProcess));
        } else {
            param.setSetProcesses(Arrays.asList(classCastValueProcess));
        }
        // 执行mapping处理
        BeanMappingExecutor.execute(param);
    }

}
