package com.agapple.mapping.convert.internal;

import com.agapple.mapping.convert.Convertor;

/**
 * object <-> String 之间的转化器，目前只实现object -> String的转化
 * 
 * @author jianghang 2011-5-25 下午10:26:30
 */
public class StringAndObjectConvetor {

    /**
     * object -> string 转化
     */
    public static class ObjectToString implements Convertor {

        @Override
        public Object convert(Object src, Class destClass) {
            return src != null ? src.toString() : null;
        }

    }

}
