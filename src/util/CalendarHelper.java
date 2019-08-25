package util;

import java.util.Calendar;

/**
 * @author Kano
 * @createTime 2019-08-21 11:21
 * @description 日历助手
 */
public class CalendarHelper {
    private Calendar calendar;
    //初始化日历
    public CalendarHelper(){
        calendar = Calendar.getInstance();
    }
    //获取显示的年份
    public int getYear(){
        return calendar.get(Calendar.YEAR);
    }
    //获取显示的月份
    public int getMonth(){
        return calendar.get(Calendar.MONTH) + 1;
    }
    //改变年份
    public void changeYear(int yearChange){
        calendar.roll(Calendar.YEAR,yearChange);
    }
    //改变月份
    public void changeMonth(int monthChange){
        calendar.roll(Calendar.MONTH,monthChange);
    }
    //获取构成日历的二维数组，每周是从周日开始
    public String[][] getCalendar(int year, int month){
       String[][] calendarForm = new String[6][7];
       Calendar tempCalendar = Calendar.getInstance();
       tempCalendar.set(Calendar.YEAR,year);
       tempCalendar.set(Calendar.MONTH,month - 1);

       tempCalendar.set(Calendar.DAY_OF_MONTH,1);
       for(int i = 0; i < tempCalendar.getActualMaximum(Calendar.DAY_OF_MONTH); i++){
           calendarForm[tempCalendar.get(tempCalendar.WEEK_OF_MONTH) - 1][tempCalendar.get(Calendar.DAY_OF_WEEK) - 1] = String.valueOf(i + 1);
           tempCalendar.roll(Calendar.DAY_OF_MONTH,1);
       }
       return calendarForm;
    }
}
