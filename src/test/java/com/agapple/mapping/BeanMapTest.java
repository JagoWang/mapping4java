package com.agapple.mapping;

import java.math.BigDecimal;
import java.util.Map;

import junit.framework.TestCase;

import org.junit.Test;

import com.agapple.mapping.object.NestedSrcMappingObject;
import com.agapple.mapping.object.SrcMappingObject;

/**
 * @author jianghang 2011-5-29 上午01:08:39
 */
public class BeanMapTest extends TestCase {

    @Test
    public void testDescribe_ok() {
        SrcMappingObject srcRef = new SrcMappingObject();
        srcRef.setIntegerValue(1);
        srcRef.setIntValue(1);
        srcRef.setName("ljh");
        srcRef.setStart(true);

        NestedSrcMappingObject nestedSrcRef = new NestedSrcMappingObject();
        nestedSrcRef.setBigDecimalValue(BigDecimal.ONE);
        srcRef.setMapping(nestedSrcRef);

        Map map = BeanMappingUtil.describe(srcRef);
        System.out.println(map);

        SrcMappingObject newSrcRef = new SrcMappingObject();// 反过来再mapping一次
        BeanMappingUtil.populate(newSrcRef, map);
        System.out.println(newSrcRef);
    }
}
