package com.agapple.mapping;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import junit.framework.TestCase;

import org.junit.Test;

import com.agapple.mapping.convert.Convertor;
import com.agapple.mapping.convert.ConvertorHelper;

/**
 * convertor相关的单元测试
 * 
 * @author jianghang 2011-5-26 上午11:17:36
 */
public class ConvertorTest extends TestCase {

    private ConvertorHelper helper = new ConvertorHelper();

    @Test
    public void testObjectToString() {
        Convertor ct = helper.getConvertor(Object.class, String.class);
        String VALUE = "1";

        Object integer = ct.convert(Integer.valueOf(VALUE), String.class); // 数字
        assertEquals(integer, VALUE);
        Object bigDecimal = ct.convert(new BigDecimal(VALUE), String.class); // BigDecimal
        assertEquals(bigDecimal, VALUE);
        Object bigInteger = ct.convert(new BigInteger(VALUE), String.class); // BigInteger
        assertEquals(bigInteger, VALUE);

        ConvertorModel model = new ConvertorModel();
        model.setI(1);
        model.setInteger(1);
        model.setBigDecimal(new BigDecimal(VALUE));
        Object modelStr = ct.convert(model, String.class); // ConvertorModel
        assertNotNull(modelStr);
    }

    @Test
    public void testStringToCommon() {
        Convertor intConvetor = helper.getConvertor(String.class, int.class);
        Convertor integerConvetor = helper.getConvertor(String.class, Integer.class);
        String VALUE = "1";
        // 基本变量
        Object intValue = intConvetor.convert(VALUE, int.class);
        assertEquals(intValue, 1);
        Object integerValue = integerConvetor.convert(VALUE, Integer.class);
        assertEquals(integerValue, 1);
        // BigDecimal/BigInteger
        Convertor bigDecimalConvetor = helper.getConvertor(String.class, BigDecimal.class);
        Convertor bigIntegerConvetor = helper.getConvertor(String.class, BigInteger.class);
        Object bigDecimalValue = bigDecimalConvetor.convert(VALUE, BigDecimal.class);
        assertEquals(bigDecimalValue, BigDecimal.ONE);
        Object bigIntegerValue = bigIntegerConvetor.convert(VALUE, BigInteger.class);
        assertEquals(bigIntegerValue, BigInteger.ONE);
    }

    @Test
    public void testStringAndDateDefault() {
        Convertor stringDate = helper.getConvertor(String.class, Date.class);
        Convertor dateString = helper.getConvertor(Date.class, String.class);

        Convertor stringCalendar = helper.getConvertor(String.class, Calendar.class);
        Convertor calendarString = helper.getConvertor(Calendar.class, String.class);

        String time = "2010-10-01 23:59:59";
        Calendar c1 = Calendar.getInstance();
        c1.set(2010, 10 - 1, 01, 23, 59, 59);
        c1.set(Calendar.MILLISECOND, 0);
        Date timeDate = c1.getTime();

        // 验证默认的转化器
        Object stringDateValue = stringDate.convert(time, Date.class);
        assertTrue(timeDate.equals(stringDateValue));
        Object dateStringValue = dateString.convert(timeDate, String.class);
        assertTrue(time.equals(dateStringValue));

        Object stringCalendarValue = stringCalendar.convert(time, Calendar.class);
        assertTrue(c1.equals(stringCalendarValue));
        Object calendarStringValue = calendarString.convert(c1, String.class);
        assertTrue(time.equals(calendarStringValue));
    }

    @Test
    public void testStringAndDateAlias() {
        Convertor stringDateDay = helper.getConvertor(ConvertorHelper.ALIAS_STRING_TO_DATE_DAY);
        Convertor stringDateTime = helper.getConvertor(ConvertorHelper.ALIAS_STRING_TO_DATE_TIME);
        Convertor dateDayString = helper.getConvertor(ConvertorHelper.ALIAS_DATE_DAY_TO_STRING);
        Convertor dateTimeString = helper.getConvertor(ConvertorHelper.ALIAS_DATE_TIME_TO_STRING);

        Convertor stringCalendarDay = helper.getConvertor(ConvertorHelper.ALIAS_STRING_TO_CALENDAR_DAY);
        Convertor stringCalendarTime = helper.getConvertor(ConvertorHelper.ALIAS_STRING_TO_CALENDAR_TIME);
        Convertor calendarDayString = helper.getConvertor(ConvertorHelper.ALIAS_CALENDAR_DAY_TO_STRING);
        Convertor calendarTimeString = helper.getConvertor(ConvertorHelper.ALIAS_CALENDAR_TIME_TO_STRING);

        String day = "2010-10-01";
        String time = "2010-10-01 23:59:59";
        Calendar timeCalendar = Calendar.getInstance();
        timeCalendar.set(2010, 10 - 1, 01, 23, 59, 59);
        timeCalendar.set(Calendar.MILLISECOND, 0);
        Date timeDate = timeCalendar.getTime();
        Calendar dayCalendar = Calendar.getInstance();
        dayCalendar.set(2010, 10 - 1, 1, 0, 0, 0);
        dayCalendar.set(Calendar.MILLISECOND, 0);
        Date dayDate = dayCalendar.getTime();

        // date转化验证
        Object stringDateDayValue = stringDateDay.convert(day, Date.class);
        assertTrue(dayDate.equals(stringDateDayValue));
        Object stringDateTimeValue = stringDateTime.convert(time, Date.class);
        assertTrue(timeDate.equals(stringDateTimeValue));
        Object dateDayStringValue = dateDayString.convert(dayDate, String.class);
        assertTrue(day.equals(dateDayStringValue));
        Object dateTimeStringValue = dateTimeString.convert(timeDate, String.class);
        assertTrue(time.equals(dateTimeStringValue));
        // calendar转化验证
        Object stringCalendarDayValue = stringCalendarDay.convert(day, Calendar.class);
        assertTrue(dayCalendar.getTime().equals(((Calendar) stringCalendarDayValue).getTime()));
        Object stringCalendarTimeValue = stringCalendarTime.convert(time, Calendar.class);
        assertTrue(timeCalendar.getTime().equals(((Calendar) stringCalendarTimeValue).getTime()));
        Object calendarDayStringValue = calendarDayString.convert(dayCalendar, String.class);
        assertTrue(day.equals(calendarDayStringValue));
        Object calendarTimeStringValue = calendarTimeString.convert(timeCalendar, String.class);
        assertTrue(time.equals(calendarTimeStringValue));

    }

    @Test
    public void testStringAndListAlias() {
        Convertor intList = helper.getConvertor(int[].class, List.class);
        Convertor integerList = helper.getConvertor(Integer[].class, List.class);

        int[] intArray = new int[] { 1, 2 };
        Integer[] integerArray = new Integer[] { 1, 2 };
        List intListValue = (List) intList.convert(intArray, List.class);
        List integerListValue = (List) integerList.convert(integerArray, List.class);
        assertEquals(intListValue.size(), intArray.length);
        assertEquals(integerListValue.size(), integerArray.length);

        // BigDecimal & BigInteger
        Convertor bigDecimalList = helper.getConvertor(BigDecimal[].class, List.class);
        Convertor bigIntegerList = helper.getConvertor(BigInteger[].class, List.class);

        BigDecimal[] bigDecimalArray = new BigDecimal[] { BigDecimal.ZERO, BigDecimal.ONE };
        BigInteger[] bigIntegerArray = new BigInteger[] { BigInteger.ZERO, BigInteger.ONE };
        List bigDecimalListValue = (List) bigDecimalList.convert(bigDecimalArray, List.class);
        List bigIntegerListValue = (List) bigIntegerList.convert(bigIntegerArray, List.class);
        assertEquals(bigDecimalListValue.size(), bigDecimalArray.length);
        assertEquals(bigIntegerListValue.size(), bigIntegerArray.length);

        // Object Array
        Convertor modelList = helper.getConvertor(ConvertorModel[].class, List.class);
        ConvertorModel[] modelArray = new ConvertorModel[] { new ConvertorModel(), new ConvertorModel() };
        List modelListValue = (List) modelList.convert(modelArray, List.class);
        assertEquals(modelListValue.size(), modelArray.length);
    }

    public static class ConvertorModel {

        private int        i;
        private Integer    integer;
        private BigDecimal bigDecimal;

        public int getI() {
            return i;
        }

        public void setI(int i) {
            this.i = i;
        }

        public Integer getInteger() {
            return integer;
        }

        public void setInteger(Integer integer) {
            this.integer = integer;
        }

        public BigDecimal getBigDecimal() {
            return bigDecimal;
        }

        public void setBigDecimal(BigDecimal bigDecimal) {
            this.bigDecimal = bigDecimal;
        }

        @Override
        public String toString() {
            return "ConvertorModel [bigDecimal=" + bigDecimal + ", i=" + i + ", integer=" + integer + "]";
        }
    }
}
