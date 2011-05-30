package com.agapple.mapping;

import java.math.BigDecimal;

import junit.framework.TestCase;

import org.junit.Test;

import com.agapple.mapping.object.NestedSrcMappingObject;
import com.agapple.mapping.object.NestedTargetMappingObject;
import com.agapple.mapping.object.SrcMappingObject;
import com.agapple.mapping.object.TargetMappingObject;

/**
 * bean copy的相关测试
 * 
 * @author jianghang 2011-5-29 上午01:08:46
 */
public class BeanCopyTest extends TestCase {

    @Test
    public void testCopy_ok() {
        SrcMappingObject srcRef = new SrcMappingObject();
        srcRef.setIntegerValue(1);
        srcRef.setIntValue(1);
        srcRef.setName("ljh");
        srcRef.setStart(true);

        NestedSrcMappingObject nestedSrcRef = new NestedSrcMappingObject();
        nestedSrcRef.setBigDecimalValue(BigDecimal.ONE);
        srcRef.setMapping(nestedSrcRef);

        TargetMappingObject targetRef = new TargetMappingObject();// 测试一下mapping到一个Object对象
        BeanMappingUtil.copy(srcRef, targetRef);
        System.out.println(targetRef);

        SrcMappingObject newSrcRef = new SrcMappingObject();// 反过来再mapping一次
        BeanMappingUtil.copy(targetRef, newSrcRef);
        System.out.println(newSrcRef);
    }

    @Test
    public void testSimpleCopy_ok() {
        NestedSrcMappingObject nestedSrcRef = new NestedSrcMappingObject();
        nestedSrcRef.setBigDecimalValue(BigDecimal.ONE);
        nestedSrcRef.setName("ljh");

        NestedTargetMappingObject nestedTargetRef = new NestedTargetMappingObject();// 测试一下mapping到一个Object对象
        BeanMappingUtil.simpleCopy(nestedSrcRef, nestedTargetRef);
        System.out.println(nestedTargetRef);

        NestedSrcMappingObject newNestedSrcRef = new NestedSrcMappingObject();// 反过来再mapping一次
        BeanMappingUtil.simpleCopy(nestedTargetRef, newNestedSrcRef);
        System.out.println(newNestedSrcRef);
    }
}
