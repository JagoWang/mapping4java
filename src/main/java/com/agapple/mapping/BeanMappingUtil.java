package com.agapple.mapping;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.cglib.beans.BeanCopier;

import org.springframework.beans.BeanUtils;

import com.agapple.mapping.config.BeanMappingConfigHelper;
import com.agapple.mapping.config.BeanMappingObject;
import com.agapple.mapping.process.BeanCreatorValueProcess;
import com.agapple.mapping.process.ClassCastValueProcess;
import com.agapple.mapping.process.ConvetorValueProcess;
import com.agapple.mapping.process.DefaultValueValueProcess;
import com.agapple.mapping.process.ScriptValueProcess;
import com.agapple.mapping.process.internal.ValueProcess;

/**
 * Bean mapping处理的一些常用方法
 * 
 * @author jianghang 2011-5-27 下午12:27:12
 */
public class BeanMappingUtil {

    private static BeanMappingExecutor executor            = new BeanMappingExecutor();
    private static List<ValueProcess>  mappingProcesses    = new ArrayList<ValueProcess>(2);
    private static List<ValueProcess>  copyProcesses       = new ArrayList<ValueProcess>(2);
    private static List<ValueProcess>  simpleCopyProcesses = new ArrayList<ValueProcess>(2);
    private static List<ValueProcess>  mapProcesses        = new ArrayList<ValueProcess>(2);

    static {
        // 注意保持特定的顺序非常重要，请别随意变更
        // field get
        mappingProcesses.add(new ScriptValueProcess());
        mappingProcesses.add(new DefaultValueValueProcess());
        // field set
        mappingProcesses.add(new BeanCreatorValueProcess());
        mappingProcesses.add(new ConvetorValueProcess());

        // field set
        copyProcesses.add(new BeanCreatorValueProcess());
        copyProcesses.add(new ConvetorValueProcess());
        copyProcesses.add(new ClassCastValueProcess());

        // field set
        simpleCopyProcesses.add(new BeanCreatorValueProcess());
        simpleCopyProcesses.add(new ClassCastValueProcess());

        // field set
        mapProcesses.add(new BeanCreatorValueProcess());
    }

    /**
     * 根据定义的bean-mapping配置进行对象属性的mapping拷贝 , 允许自定义{@linkplain ValueProcess}
     * 
     * @param src
     * @param target
     */
    public static void mapping(Object src, Object target) throws BeanMappingException {
        BeanMappingObject object = BeanMappingConfigHelper.getInstance().getBeanMappingObject(src.getClass(),
                                                                                              target.getClass());

        if (object == null) {// 可能没有配置
            throw new BeanMappingException("there is no BeanMapping config for src class[" + src.getClass().getName()
                                           + "] target class[" + target.getClass().getName() + "]");
        }

        BeanMappingParam param = new BeanMappingParam();
        param.setSrcRef(src);
        param.setTargetRef(target);
        param.setConfig(object);
        // 设置valueProcess，进行插件扩展
        param.setProcesses(mappingProcesses);
        // 执行mapping处理
        executor.execute(param);
    }

    /**
     * 对象属性的拷贝，与{@linkplain BeanUtils} {@linkplain BeanCopier} 功能类似
     * 
     * @param src
     * @param target
     */
    public static void copy(Object src, Object target) throws BeanMappingException {
        BeanMappingObject object = BeanMappingConfigHelper.getInstance().getBeanMappingObject(src.getClass(),
                                                                                              target.getClass(), true);
        BeanMappingParam param = new BeanMappingParam();
        param.setSrcRef(src);
        param.setTargetRef(target);
        param.setConfig(object);
        param.setProcesses(copyProcesses);
        // 执行mapping处理
        executor.execute(param);
    }

    /**
     * 简单的拷贝模式，不做属性转化处理，与{@linkplain PropertyUtils}功能类似
     * 
     * @param src
     * @param target
     */
    public static void simpleCopy(Object src, Object target) throws BeanMappingException {
        BeanMappingObject object = BeanMappingConfigHelper.getInstance().getBeanMappingObject(src.getClass(),
                                                                                              target.getClass(), true);
        BeanMappingParam param = new BeanMappingParam();
        param.setSrcRef(src);
        param.setTargetRef(target);
        param.setConfig(object);
        param.setProcesses(simpleCopyProcesses);
        // 执行mapping处理
        executor.execute(param);
    }

    public static Map describe(Object src) throws BeanMappingException {
        BeanMappingObject object = BeanMappingConfigHelper.getInstance().getBeanMapObject(src.getClass(),
                                                                                          HashMap.class, true);
        Map result = new HashMap();
        BeanMappingParam param = new BeanMappingParam();
        param.setSrcRef(src);
        param.setTargetRef(result);
        param.setConfig(object);
        param.setProcesses(mapProcesses);
        // 执行mapping处理
        executor.execute(param);
        return result;
    }

    public static void populate(Object target, Map properties) throws BeanMappingException {
        BeanMappingObject object = BeanMappingConfigHelper.getInstance().getBeanMapObject(HashMap.class,
                                                                                          target.getClass(), true);
        BeanMappingParam param = new BeanMappingParam();
        param.setSrcRef(properties);
        param.setTargetRef(target);
        param.setConfig(object);
        param.setProcesses(mapProcesses);
        // 执行mapping处理
        executor.execute(param);
    }
}
