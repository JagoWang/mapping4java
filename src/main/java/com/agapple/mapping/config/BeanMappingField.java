package com.agapple.mapping.config;

import java.io.Serializable;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

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
    private String            convetor;                               // 自定义conveterName
    private String            script;                                 // format script字符串
    private boolean           mapping          = false;               // 是否深度递归mapping

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

    public String getConvertor() {
        return convetor;
    }

    public void setConvertor(String convertor) {
        this.convetor = convertor;
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

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
