package com.agapple.mapping;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;

import org.junit.Test;

import com.agapple.mapping.config.BeanMappingConfigHelper;
import com.agapple.mapping.object.NestedSrcMappingObject;
import com.agapple.mapping.object.SrcMappingObject;
import com.agapple.mapping.object.TargetMappingObject;

/**
 * Bean Mapping的相关测试
 * 
 * @author jianghang 2011-5-27 上午11:27:27
 */
public class BeanMappingTest extends TestCase {

    static {
        BeanMappingConfigHelper.getInstance().registerConfig("mapping/mapping.xml");
    }

    @Test
    public void testBeanToBean_ok() {

        SrcMappingObject srcRef = new SrcMappingObject();
        srcRef.setIntegerValue(1);
        srcRef.setIntValue(1);
        srcRef.setName("ljh");
        srcRef.setStart(true);

        NestedSrcMappingObject nestedSrcRef = new NestedSrcMappingObject();
        nestedSrcRef.setBigDecimalValue(BigDecimal.ONE);
        srcRef.setMapping(nestedSrcRef);

        TargetMappingObject targetRef = new TargetMappingObject();// 测试一下mapping到一个Object对象
        BeanMappingUtil.mapping(srcRef, targetRef);
        System.out.println(targetRef);

        SrcMappingObject newSrcRef = new SrcMappingObject();// 反过来再mapping一次
        BeanMappingUtil.mapping(targetRef, newSrcRef);
        System.out.println(newSrcRef);
    }

    @Test
    public void testBeanToMap_ok() {
        SrcMappingObject srcRef = new SrcMappingObject();
        srcRef.setIntegerValue(1);
        srcRef.setIntValue(1);
        srcRef.setName("ljh");
        srcRef.setStart(true);

        NestedSrcMappingObject nestedSrcRef = new NestedSrcMappingObject();
        nestedSrcRef.setBigDecimalValue(BigDecimal.ONE);
        srcRef.setMapping(nestedSrcRef);

        Map targetRef = new HashMap();// 测试一下mapping到一个HashMap对象
        BeanMappingUtil.mapping(srcRef, targetRef);
        System.out.println(targetRef);

        SrcMappingObject newSrcRef = new SrcMappingObject();
        BeanMappingUtil.mapping(targetRef, newSrcRef); // 反过来再mapping一次
        System.out.println(newSrcRef);
    }

    @Test
    public void testBeanToMap_defaultValue() {
        SrcMappingObject srcRef = new SrcMappingObject();

        NestedSrcMappingObject nestedSrcRef = new NestedSrcMappingObject();
        srcRef.setMapping(nestedSrcRef);

        Map targetRef = new HashMap();// 测试一下mapping到一个HashMap对象
        BeanMappingUtil.mapping(srcRef, targetRef);
        assertNotNull(targetRef.get("mapping"));
        assertEquals(((Map) targetRef.get("mapping")).get("name"), "ljh");// 检查下default value

        SrcMappingObject newSrcRef = new SrcMappingObject();
        BeanMappingUtil.mapping(targetRef, newSrcRef); // 反过来再mapping一次
        assertNotNull(newSrcRef.getMapping());
        assertEquals(newSrcRef.getMapping().getName(), "ljh");// 检查下default value
    }

    @Test
    public void testBeanToMap_nested_null() {// 测试嵌套对象为null
        SrcMappingObject srcRef = new SrcMappingObject();
        srcRef.setIntegerValue(1);// 只复制一个属性

        Map targetRef = new HashMap();// 测试一下mapping到一个HashMap对象
        BeanMappingUtil.mapping(srcRef, targetRef);
        assertNull(targetRef.get("mapping"));
        assertEquals(targetRef.get("integerValue"), srcRef.getIntegerValue().toString());

        SrcMappingObject newSrcRef = new SrcMappingObject();
        BeanMappingUtil.mapping(targetRef, newSrcRef); // 反过来再mapping一次
        assertNull(newSrcRef.getMapping());
        assertEquals(targetRef.get("integerValue"), newSrcRef.getIntegerValue().toString());
    }

    @Test
    public void testBeanToMap_diff_fieldname_and_convertor() {// 测试名字不同 + 类型转化 + defaultValue
        SrcMappingObject srcRef = new SrcMappingObject();
        srcRef.setName("ljh"); // 名字不同

        NestedSrcMappingObject nestedSrcRef = new NestedSrcMappingObject();
        // nestedSrcRef.setBigDecimalValue(BigDecimal.ONE); // 类型不匹配
        srcRef.setMapping(nestedSrcRef);

        Map targetRef = new HashMap();// 测试一下mapping到一个HashMap对象
        BeanMappingUtil.mapping(srcRef, targetRef);
        assertNotNull(targetRef.get("mapping"));
        assertEquals(((Map) targetRef.get("mapping")).get("value"), "10");
        assertEquals(targetRef.get("targetName"), srcRef.getName());

        SrcMappingObject newSrcRef = new SrcMappingObject();
        BeanMappingUtil.mapping(targetRef, newSrcRef); // 反过来再mapping一次
        assertNotNull(newSrcRef.getMapping());
        assertEquals(((Map) targetRef.get("mapping")).get("value"),
                     newSrcRef.getMapping().getBigDecimalValue().toString());
        assertEquals(targetRef.get("integerValue"), newSrcRef.getIntegerValue());
    }
}
