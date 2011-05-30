package com.agapple.mapping;

import java.util.HashMap;

import junit.framework.TestCase;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.junit.Test;

import com.agapple.mapping.config.BeanMappingConfigHelper;
import com.agapple.mapping.config.BeanMappingField;
import com.agapple.mapping.config.BeanMappingObject;
import com.agapple.mapping.object.SrcMappingObject;
import com.agapple.mapping.object.TargetMappingObject;

/**
 * @author jianghang 2011-5-27 上午09:26:38
 */
public class ConfigTest extends TestCase {

    @Test
    public void testFileParse() {
        String file = "mapping/mapping.xml";
        BeanMappingConfigHelper.getInstance().registerConfig(file);
        BeanMappingObject object = BeanMappingConfigHelper.getInstance().getBeanMappingObject(HashMap.class,
                                                                                              HashMap.class);
        printObject(object);
        assertNotNull(object);
    }

    @Test
    public void testClassParse() {
        Class srcClass = SrcMappingObject.class;
        Class targetClass = TargetMappingObject.class;
        BeanMappingObject object = BeanMappingConfigHelper.getInstance().getBeanMappingObject(srcClass, targetClass,
                                                                                              true);// 自动注册
        printObject(object);
    }

    private BeanMappingObject printObject(BeanMappingObject object) {
        System.out.println(ToStringBuilder.reflectionToString(object, ToStringStyle.MULTI_LINE_STYLE));
        for (BeanMappingField field : object.getBeanFields()) {
            System.out.println(ToStringBuilder.reflectionToString(field, ToStringStyle.MULTI_LINE_STYLE));
        }
        return object;
    }

}
