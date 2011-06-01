package com.agapple.mapping.config;

import java.io.Serializable;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import com.agapple.mapping.introspect.GetExecutor;
import com.agapple.mapping.introspect.SetExecutor;

/**
 * 解析完成后的一个BeanMapping的field配置对象
 * 
 * @author jianghang 2011-5-23 下午04:25:06
 */
public class BeanMappingField implements Serializable {

    private static final long serialVersionUID = 3673414855182044438L;
    private String            targetName;                             // 目标数据的name
    private Class             targetClass;                            // 目标数据的class
    private String            srcName;                                // 源数据的name
    private Class             srcClass;                               // 源数据的class
    private String            defaultValue;                           // 默认值,配置文件中定义的字符串
    private String            convertor;                              // 自定义conveterName
    private String            script;                                 // format script字符串
    private boolean           mapping          = false;               // 是否深度递归mapping

    // ======================= 内部数据，外部请勿直接操作 ==================
    private GetExecutor       getExecutor      = null;                // get操作的执行引擎
    private SetExecutor       setExecutor      = null;                // set操作的执行引擎

    public String getTargetName() {
        return targetName;
    }

    public void setTargetName(String targetName) {
        this.targetName = targetName;
    }

    public Class getTargetClass() {
        return targetClass;
    }

    public void setTargetClass(Class targetClass) {
        this.targetClass = targetClass;
    }

    public String getSrcName() {
        return srcName;
    }

    public void setSrcName(String srcName) {
        this.srcName = srcName;
    }

    public Class getSrcClass() {
        return srcClass;
    }

    public void setSrcClass(Class srcClass) {
        this.srcClass = srcClass;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public String getScript() {
        return script;
    }

    public void setScript(String script) {
        this.script = script;
    }

    public boolean isMapping() {
        return mapping;
    }

    public void setMapping(boolean mapping) {
        this.mapping = mapping;
    }

    public String getConvertor() {
        return convertor;
    }

    public void setConvertor(String convertor) {
        this.convertor = convertor;
    }

    public GetExecutor getGetExecutor() {
        return getExecutor;
    }

    public void setGetExecutor(GetExecutor getExecutor) {
        this.getExecutor = getExecutor;
    }

    public SetExecutor getSetExecutor() {
        return setExecutor;
    }

    public void setSetExecutor(SetExecutor setExecutor) {
        this.setExecutor = setExecutor;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
