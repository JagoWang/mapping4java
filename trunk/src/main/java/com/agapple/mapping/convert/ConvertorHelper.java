package com.agapple.mapping.convert;

import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.agapple.mapping.convert.internal.ArrayAndListConvertor;
import com.agapple.mapping.convert.internal.StringAndCommonConvertor;
import com.agapple.mapping.convert.internal.StringAndDateConvertor;
import com.agapple.mapping.convert.internal.StringAndObjectConvetor;

/**
 * convert转化helper类，注册一些默认的convertor
 * 
 * @author jianghang 2011-5-20 下午04:44:38
 */
public class ConvertorHelper {

    public static final String              ALIAS_DATE_TIME_TO_STRING     = StringAndDateConvertor.DateTimeToString.class.getSimpleName();
    public static final String              ALIAS_DATE_DAY_TO_STRING      = StringAndDateConvertor.DateDayToString.class.getSimpleName();
    public static final String              ALIAS_STRING_TO_DATE_TIME     = StringAndDateConvertor.StringToDateTime.class.getSimpleName();
    public static final String              ALIAS_STRING_TO_DATE_DAY      = StringAndDateConvertor.StringToDateDay.class.getSimpleName();
    public static final String              ALIAS_CALENDAR_TIME_TO_STRING = StringAndDateConvertor.CalendarTimeToString.class.getSimpleName();
    public static final String              ALIAS_CALENDAR_DAY_TO_STRING  = StringAndDateConvertor.CalendarDayToString.class.getSimpleName();
    public static final String              ALIAS_STRING_TO_CALENDAR_TIME = StringAndDateConvertor.StringToCalendarTime.class.getSimpleName();
    public static final String              ALIAS_STRING_TO_CALENDAR_DAY  = StringAndDateConvertor.StringToCalendarDay.class.getSimpleName();

    private static volatile ConvertorHelper singleton                     = null;
    private ConvertorRepository             repository                    = null;

    public ConvertorHelper(){
        repository = new ConvertorRepository();
        initDefaultRegister();
    }

    public ConvertorHelper(ConvertorRepository repository){
        // 允许传入自定义仓库
        this.repository = repository;
        initDefaultRegister();
    }

    /**
     * 单例方法
     */
    public static ConvertorHelper getInstance() {
        if (singleton == null) {
            synchronized (ConvertorHelper.class) {
                if (singleton == null) { // double check
                    singleton = new ConvertorHelper();
                }
            }
        }
        return singleton;
    }

    /**
     * 根据class获取对应的convertor
     * 
     * @return
     */
    public Convertor getConvertor(Class src, Class dest) {
        if (src == dest) {
            // 对相同类型的直接忽略，不做转换
            return null;
        }

        // 按照src->dest来取映射
        Convertor convertor = repository.getConvertor(src, dest);

        // 如果src|dest是array类型，取一下Array.class的映射，因为默认数组处理的注册直接注册了Array.class
        if (convertor == null && src.isArray()) {
            convertor = repository.getConvertor(Array.class, dest);
        }
        if (convertor == null && dest.isArray()) {
            convertor = repository.getConvertor(src, Array.class);
        }

        // 如果dest是string，获取一下object->string. (系统默认注册了一个Object.class -> String.class的转化)
        if (convertor == null && dest == String.class) {
            convertor = repository.getConvertor(Object.class, String.class);
        }

        return convertor;
    }

    /**
     * 根据alias获取对应的convertor
     * 
     * @return
     */
    public Convertor getConvertor(String alias) {
        return repository.getConvertor(alias);
    }

    /**
     * 注册class对应的convertor
     */
    public void registerConvertor(Class src, Class dest, Convertor convertor) {
        repository.registerConvertor(src, dest, convertor);
    }

    /**
     * 注册alias对应的convertor
     */
    public void registerConvertor(String alias, Convertor convertor) {
        repository.registerConvertor(alias, convertor);
    }

    // ======================= register处理 ======================

    public void initDefaultRegister() {
        commonRegister();
        arrayListRegister();
        StringDateRegister();
        // 注册Objet -> String对象处理
        Convertor objectToString = new StringAndObjectConvetor.ObjectToString();
        repository.registerConvertor(Object.class, String.class, objectToString);
    }

    private void StringDateRegister() {
        // 注册string<->date对象处理
        Convertor stringToDateDay = new StringAndDateConvertor.StringToDateDay();
        Convertor stringToDateTime = new StringAndDateConvertor.StringToDateTime();
        Convertor stringToCalendarDay = new StringAndDateConvertor.StringToCalendarDay();
        Convertor stringToCalendarTime = new StringAndDateConvertor.StringToCalendarTime();
        Convertor dateDayToString = new StringAndDateConvertor.DateDayToString();
        Convertor dateTimeToString = new StringAndDateConvertor.DateTimeToString();
        Convertor calendarDayToString = new StringAndDateConvertor.CalendarDayToString();
        Convertor calendarTimeToString = new StringAndDateConvertor.CalendarTimeToString();
        // 注册默认的String <-> Date的处理
        repository.registerConvertor(String.class, Date.class, stringToDateTime);
        repository.registerConvertor(Date.class, String.class, dateTimeToString);
        repository.registerConvertor(String.class, Calendar.class, stringToCalendarTime);
        repository.registerConvertor(Calendar.class, String.class, calendarTimeToString);
        // 注册为别名
        repository.registerConvertor(ALIAS_STRING_TO_DATE_DAY, stringToDateDay);
        repository.registerConvertor(ALIAS_STRING_TO_DATE_TIME, stringToDateTime);
        repository.registerConvertor(ALIAS_STRING_TO_CALENDAR_DAY, stringToCalendarDay);
        repository.registerConvertor(ALIAS_STRING_TO_CALENDAR_TIME, stringToCalendarTime);
        repository.registerConvertor(ALIAS_DATE_DAY_TO_STRING, dateDayToString);
        repository.registerConvertor(ALIAS_DATE_TIME_TO_STRING, dateTimeToString);
        repository.registerConvertor(ALIAS_CALENDAR_DAY_TO_STRING, calendarDayToString);
        repository.registerConvertor(ALIAS_CALENDAR_TIME_TO_STRING, calendarTimeToString);
    }

    private void arrayListRegister() {
        // 注册array <-> list对象处理
        Convertor arrayToList = new ArrayAndListConvertor.ArrayToList();
        Convertor listToArray = new ArrayAndListConvertor.ListToArray();
        repository.registerConvertor(Array.class, List.class, arrayToList);
        repository.registerConvertor(List.class, Array.class, listToArray);
    }

    private void commonRegister() {
        // 注册string->common对象处理
        Convertor stringToCommon = new StringAndCommonConvertor.StringToCommon();
        repository.registerConvertor(String.class, int.class, stringToCommon);
        repository.registerConvertor(String.class, Integer.class, stringToCommon);
        repository.registerConvertor(String.class, short.class, stringToCommon);
        repository.registerConvertor(String.class, Short.class, stringToCommon);
        repository.registerConvertor(String.class, long.class, stringToCommon);
        repository.registerConvertor(String.class, Long.class, stringToCommon);
        repository.registerConvertor(String.class, boolean.class, stringToCommon);
        repository.registerConvertor(String.class, Boolean.class, stringToCommon);
        repository.registerConvertor(String.class, byte.class, stringToCommon);
        repository.registerConvertor(String.class, Byte.class, stringToCommon);
        repository.registerConvertor(String.class, char.class, stringToCommon);
        repository.registerConvertor(String.class, Character.class, stringToCommon);
        repository.registerConvertor(String.class, float.class, stringToCommon);
        repository.registerConvertor(String.class, Float.class, stringToCommon);
        repository.registerConvertor(String.class, double.class, stringToCommon);
        repository.registerConvertor(String.class, Double.class, stringToCommon);

        repository.registerConvertor(String.class, BigDecimal.class, stringToCommon);
        repository.registerConvertor(String.class, BigInteger.class, stringToCommon);
    }

    // ========================= setter / getter ===================

    public void setRepository(ConvertorRepository repository) {
        this.repository = repository;
    }
}
