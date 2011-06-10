package com.agapple.mapping.process.convetor;

import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import com.agapple.mapping.core.BeanMappingException;

/**
 * Array <-> list 的转化器
 * 
 * @author jianghang 2011-5-26 上午09:23:00
 */
public class ArrayAndListConvertor {

    /**
     * array -> list 转化
     */
    public static class ArrayToList implements Convertor {

        @Override
        public Object convert(Object src, Class destClass) {
            if (src.getClass().isArray()) { // 必须是数组
                List result = new ArrayList();
                int size = Array.getLength(src);
                Class compoentType = src.getClass().getComponentType();
                for (int i = 0; i < size; i++) {
                    Object obj = null;

                    if (compoentType == double.class) {
                        obj = Array.getDouble(src, i);
                    }

                    if (compoentType == float.class) {
                        obj = Array.getFloat(src, i);
                    }

                    if (compoentType == boolean.class) {
                        obj = Array.getBoolean(src, i);
                    }

                    if (compoentType == int.class) {
                        obj = Array.getInt(src, i);
                    }

                    if (compoentType == short.class) {
                        obj = Array.getShort(src, i);
                    }

                    if (compoentType == long.class) {
                        obj = Array.getLong(src, i);
                    }

                    if (compoentType == byte.class) {
                        obj = Array.getByte(src, i);
                    }

                    if (compoentType == char.class) {
                        obj = Array.getChar(src, i);
                    }

                    if (compoentType == BigDecimal.class || compoentType == BigInteger.class) { // 如果是通用的对象
                        obj = Array.get(src, i);
                    } else {
                        // if (obj.getClass().isArray()) {// 如果对象继续是array
                        // result.add(convert(obj, destClass));
                        // }
                        // TODO : 后续可支持嵌套的Object，包括复杂对象的处理
                        obj = Array.get(src, i);
                    }

                    result.add(obj);

                }
                return result;
            }

            throw new BeanMappingException("Unsupported convert: [" + src + "," + destClass.getName() + "]");
        }
    }

    /**
     * list -> array 转化，处理多级数组
     */
    public static class ListToArray implements Convertor {

        @Override
        public Object convert(Object src, Class destClass) {
            if (List.class.isAssignableFrom(src.getClass())) { // 必须是List的子类
                List list = (List) src;
                Object[] obj = (Object[]) Array.newInstance(destClass.getComponentType(), list.size());
                return list.toArray(obj);
            }

            throw new BeanMappingException("Unsupported convert: [" + src + "," + destClass.getName() + "]");
        }
    }

}
