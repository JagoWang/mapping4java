/*
 * Copyright 1999-2004 Alibaba.com All right reserved. This software is the confidential and proprietary information of
 * Alibaba.com ("Confidential Information"). You shall not disclose such Confidential Information and shall use it only
 * in accordance with the terms of the license agreement you entered into with Alibaba.com.
 */
package com.agapple.mapping.performace;

import java.lang.management.ManagementFactory;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DecimalFormat;

import junit.framework.TestCase;
import net.sf.cglib.beans.BeanCopier;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.PropertyUtils;

import com.agapple.mapping.BeanMappingUtil;

/**
 * BeanCopier , Beanutils/PropertyUtils , BeanMapping几种机制的copy操作的性能测试
 * 
 * @author jianghang 2011-5-31 下午02:44:40
 */
public class CopyPerformance extends TestCase {

    private static final DecimalFormat integerFormat = new DecimalFormat("#,###");

    public static void main(String args[]) {
        testCopy();
    }

    public static void testCopy() {
        final int testCount = 1000 * 100 * 2;
        CopyBean bean = getBean();
        // BeanMapping copy测试
        final CopyBean beanMappingTarget = new CopyBean();
        testTemplate(new TestCallback() {

            public String getName() {
                return "BeanMapping.copy";
            }

            public CopyBean call(CopyBean source) {
                try {
                    BeanMappingUtil.copy(source, beanMappingTarget);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return beanMappingTarget;
            }

        }, bean, testCount);

        // BeanMapping simpleCopy测试
        final CopyBean beanMappingSimpleTarget = new CopyBean();
        testTemplate(new TestCallback() {

            public String getName() {
                return "BeanMapping.simpleCopy";
            }

            public CopyBean call(CopyBean source) {
                try {
                    BeanMappingUtil.simpleCopy(source, beanMappingSimpleTarget);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return beanMappingSimpleTarget;
            }

        }, bean, testCount);
        // beanCopier测试
        final BeanCopier beanCopier = BeanCopier.create(CopyBean.class, CopyBean.class, false);
        final CopyBean beanCopierTarget = new CopyBean();
        testTemplate(new TestCallback() {

            public String getName() {
                return "BeanCopier";
            }

            public CopyBean call(CopyBean source) {
                beanCopier.copy(source, beanCopierTarget, null);
                return beanCopierTarget;
            }
        }, bean, testCount);
        // PropertyUtils测试
        final CopyBean propertyUtilsTarget = new CopyBean();
        testTemplate(new TestCallback() {

            public String getName() {
                return "PropertyUtils";
            }

            public CopyBean call(CopyBean source) {
                try {
                    PropertyUtils.copyProperties(propertyUtilsTarget, source);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return propertyUtilsTarget;
            }

        }, bean, testCount);
        // BeanUtils测试
        final CopyBean beanUtilsTarget = new CopyBean();
        testTemplate(new TestCallback() {

            public String getName() {
                return "BeanUtils";
            }

            public CopyBean call(CopyBean source) {
                try {
                    BeanUtils.copyProperties(beanUtilsTarget, source);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return beanUtilsTarget;
            }

        }, bean, testCount);

    }

    public static void testTemplate(TestCallback callback, CopyBean source, int count) {
        int warmup = 10070;
        // 先进行预热，加载一些类，避免影响测试
        for (int i = 0; i < warmup; i++) {
            callback.call(source);
        }
        restoreJvm(); // 进行GC回收
        // 进行测试
        long start = System.nanoTime();
        for (int i = 0; i < count; i++) {
            callback.call(source);
        }
        long nscost = (System.nanoTime() - start);
        System.out.println(callback.getName() + " total cost=" + integerFormat.format(nscost) + "ns , each cost="
                           + nscost / count + "ns");
        restoreJvm();// 进行GC回收

    }

    private static CopyBean getBean() {
        CopyBean bean = new CopyBean();
        bean.setIntValue(1);
        bean.setBoolValue(false);
        bean.setFloatValue(1.0f);
        bean.setDoubleValue(1.0d);
        bean.setLongValue(1l);
        bean.setCharValue('a');
        bean.setShortValue((short) 1);
        bean.setByteValue((byte) 1);
        bean.setIntegerValue(new Integer("1"));
        bean.setBoolObjValue(new Boolean("false"));
        bean.setFloatObjValue(new Float("1.0"));
        bean.setDoubleObjValue(new Double("1.0"));
        bean.setLongObjValue(new Long("1"));
        bean.setShortObjValue(new Short("1"));
        bean.setByteObjValue(new Byte("1"));
        bean.setBigIntegerValue(new BigInteger("1"));
        bean.setBigDecimalValue(new BigDecimal("1"));
        bean.setStringValue("1");
        return bean;
    }

    private static void restoreJvm() {
        int maxRestoreJvmLoops = 10;
        long memUsedPrev = memoryUsed();
        for (int i = 0; i < maxRestoreJvmLoops; i++) {
            System.runFinalization();
            System.gc();

            long memUsedNow = memoryUsed();
            // break early if have no more finalization and get constant mem used
            if ((ManagementFactory.getMemoryMXBean().getObjectPendingFinalizationCount() == 0)
                && (memUsedNow >= memUsedPrev)) {
                break;
            } else {
                memUsedPrev = memUsedNow;
            }
        }
    }

    private static long memoryUsed() {
        Runtime rt = Runtime.getRuntime();
        return rt.totalMemory() - rt.freeMemory();
    }

    public static class CopyBean {

        private int        intValue;
        private boolean    boolValue;
        private float      floatValue;
        private double     doubleValue;
        private long       longValue;
        private char       charValue;
        private byte       byteValue;
        private short      shortValue;
        private Integer    integerValue;
        private Boolean    boolObjValue;
        private Float      floatObjValue;
        private Double     doubleObjValue;
        private Long       longObjValue;
        private Short      shortObjValue;
        private Byte       byteObjValue;
        private BigInteger bigIntegerValue;
        private BigDecimal bigDecimalValue;
        private String     stringValue;

        public int getIntValue() {
            return intValue;
        }

        public void setIntValue(int intValue) {
            this.intValue = intValue;
        }

        public float getFloatValue() {
            return floatValue;
        }

        public void setFloatValue(float floatValue) {
            this.floatValue = floatValue;
        }

        public double getDoubleValue() {
            return doubleValue;
        }

        public void setDoubleValue(double doubleValue) {
            this.doubleValue = doubleValue;
        }

        public long getLongValue() {
            return longValue;
        }

        public void setLongValue(long longValue) {
            this.longValue = longValue;
        }

        public char getCharValue() {
            return charValue;
        }

        public void setCharValue(char charValue) {
            this.charValue = charValue;
        }

        public byte getByteValue() {
            return byteValue;
        }

        public void setByteValue(byte byteValue) {
            this.byteValue = byteValue;
        }

        public short getShortValue() {
            return shortValue;
        }

        public void setShortValue(short shortValue) {
            this.shortValue = shortValue;
        }

        public Integer getIntegerValue() {
            return integerValue;
        }

        public void setIntegerValue(Integer integerValue) {
            this.integerValue = integerValue;
        }

        public Float getFloatObjValue() {
            return floatObjValue;
        }

        public void setFloatObjValue(Float floatObjValue) {
            this.floatObjValue = floatObjValue;
        }

        public Double getDoubleObjValue() {
            return doubleObjValue;
        }

        public void setDoubleObjValue(Double doubleObjValue) {
            this.doubleObjValue = doubleObjValue;
        }

        public Long getLongObjValue() {
            return longObjValue;
        }

        public void setLongObjValue(Long longObjValue) {
            this.longObjValue = longObjValue;
        }

        public Short getShortObjValue() {
            return shortObjValue;
        }

        public void setShortObjValue(Short shortObjValue) {
            this.shortObjValue = shortObjValue;
        }

        public Byte getByteObjValue() {
            return byteObjValue;
        }

        public void setByteObjValue(Byte byteObjValue) {
            this.byteObjValue = byteObjValue;
        }

        public boolean isBoolValue() {
            return boolValue;
        }

        public void setBoolValue(boolean boolValue) {
            this.boolValue = boolValue;
        }

        public Boolean getBoolObjValue() {
            return boolObjValue;
        }

        public void setBoolObjValue(Boolean boolObjValue) {
            this.boolObjValue = boolObjValue;
        }

        public BigInteger getBigIntegerValue() {
            return bigIntegerValue;
        }

        public void setBigIntegerValue(BigInteger bigIntegerValue) {
            this.bigIntegerValue = bigIntegerValue;
        }

        public BigDecimal getBigDecimalValue() {
            return bigDecimalValue;
        }

        public void setBigDecimalValue(BigDecimal bigDecimalValue) {
            this.bigDecimalValue = bigDecimalValue;
        }

        public String getStringValue() {
            return stringValue;
        }

        public void setStringValue(String stringValue) {
            this.stringValue = stringValue;
        }
    }
}

interface TestCallback {

    String getName();

    CopyPerformance.CopyBean call(CopyPerformance.CopyBean source);
}
