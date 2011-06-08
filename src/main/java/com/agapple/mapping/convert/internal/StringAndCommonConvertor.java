package com.agapple.mapping.convert.internal;

import java.math.BigDecimal;
import java.math.BigInteger;

import com.agapple.mapping.convert.Convertor;
import com.agapple.mapping.core.BeanMappingException;

/**
 * string <-> common对象 之间的转化
 * 
 * <pre>
 * common对象范围：8种Primitive和对应的Java类型，BigDecimal, BigInteger
 * 
 * </pre>
 * 
 * @author jianghang 2011-5-25 下午11:11:25
 */
public class StringAndCommonConvertor {

    /**
     * string -> common对象的转化
     */
    public static class StringToCommon implements Convertor {

        @Override
        public Object convert(Object src, Class destClass) {
            if (String.class.isInstance(src)) { // src必须是String
                String str = (String) src;
                if (destClass == Double.class || destClass == double.class) {
                    return Double.valueOf(str);
                }

                if (destClass == Float.class || destClass == float.class) {
                    return Float.valueOf(str);
                }

                if (destClass == Boolean.class || destClass == boolean.class) {
                    return Boolean.valueOf(str);
                }

                if (destClass == Integer.class || destClass == int.class) {
                    return Integer.valueOf(str);
                }

                if (destClass == Short.class || destClass == short.class) {
                    return Short.valueOf(str);
                }

                if (destClass == Long.class || destClass == long.class) {
                    return Long.valueOf(str);
                }

                if (destClass == Byte.class || destClass == byte.class) {
                    return Byte.valueOf(str);
                }

                if (destClass == Character.class || destClass == char.class) {
                    return Character.valueOf(str.charAt(0)); // 只取第一个字符
                }

                if (destClass == BigDecimal.class) {
                    return new BigDecimal(str);
                }

                if (destClass == BigInteger.class) {
                    return new BigInteger(str);
                }
            }

            throw new BeanMappingException("Unsupported convert: [" + src + "," + destClass.getName() + "]");
        }
    }
}
