package com.agapple.mapping.core;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import com.agapple.mapping.core.config.BeanMappingObject;
import com.agapple.mapping.process.internal.GetValueProcess;
import com.agapple.mapping.process.internal.SetValueProcess;

/**
 * bean mapping传递的参数
 * 
 * @author jianghang 2011-5-26 下午06:39:29
 */
public class BeanMappingParam implements Serializable {

    private static final long     serialVersionUID   = 2371233083866029415L;
    private Object                srcRef;                                   // 待转化src
    private Object                targetRef;                                // 转化的目标dest
    private BeanMappingObject     config;                                   // bean mapping相关配置

    // =========================== ValueProcess 扩展参数==============================
    private List<GetValueProcess> getProcesses;                             // 自定义的get valueProcess
    private List<SetValueProcess> setProcesses;                             // 自定义的set valueProcess
    private Map                   customValueContext = new HashMap();       // 自定义的valueProcess上下文处理

    public Object getSrcRef() {
        return srcRef;
    }

    public void setSrcRef(Object srcRef) {
        this.srcRef = srcRef;
    }

    public Object getTargetRef() {
        return targetRef;
    }

    public void setTargetRef(Object targetRef) {
        this.targetRef = targetRef;
    }

    public BeanMappingObject getConfig() {
        return config;
    }

    public void setConfig(BeanMappingObject config) {
        this.config = config;
    }

    public Map getCustomValueContext() {
        return customValueContext;
    }

    public void setCustomValueContext(Map customValueContext) {
        this.customValueContext = customValueContext;
    }

    public List<GetValueProcess> getGetProcesses() {
        return getProcesses;
    }

    public void setGetProcesses(List<GetValueProcess> getProcesses) {
        this.getProcesses = getProcesses;
    }

    public List<SetValueProcess> getSetProcesses() {
        return setProcesses;
    }

    public void setSetProcesses(List<SetValueProcess> setProcesses) {
        this.setProcesses = setProcesses;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

}
