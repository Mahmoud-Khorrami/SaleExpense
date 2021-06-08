package com.example.boroodat.general;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import jxl.write.DateTime;

public class TodayDate
{
    public String get()
    {
        Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = (c.get(Calendar.MONTH))+1;
        int mDay = c.get(Calendar.DAY_OF_MONTH);

        DateConverter dateConverter=new DateConverter();
        dateConverter.GregorianToPersian(mYear,mMonth,mDay);

        String date=dateConverter.toString();
        date=date.replace("-","/");

        return date;
    }

    public String dateTime()
    {
        Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = (c.get(Calendar.MONTH))+1;
        int mDay = c.get(Calendar.DAY_OF_MONTH);

        DateConverter dateConverter=new DateConverter();
        dateConverter.GregorianToPersian(mYear,mMonth,mDay);
        String date=dateConverter.toString();

        int h=c.get ( Calendar.HOUR_OF_DAY );
        int mi=c.get ( Calendar.MINUTE );
        int s= c.get ( Calendar.SECOND );

        String time = "-" + h + "-" + mi + "-" + s;

        return date + time;
    }

    private String getMiladiDate(String shamsi_date)
    {
        DateConverter dateConverter = new DateConverter();

        //---------------------------------------------------

        String shamsi_year = shamsi_date.substring(0,4);
        String shamsi_month = shamsi_date.substring(5,7);
        String shamsi_day = shamsi_date.substring(8);
        dateConverter.PersianToGregorian(Integer.parseInt(shamsi_year),Integer.parseInt(shamsi_month),Integer.parseInt(shamsi_day));

        String miladi =dateConverter.getDay()+"/"+dateConverter.getMonth()+"/"+dateConverter.getYear();

        return miladi;
    }

    private int compareDate(String date1, String date2)
    {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        try
        {
            Date d1 = sdf.parse(getMiladiDate(date1));
            Date d2 = sdf.parse(getMiladiDate(date2));

            if (d1.compareTo(d2)>0)
                return 2;
            else if (d1.compareTo(d2)<0)
                return 1;
            else
                return 0;

        } catch (ParseException e)
        {
           return -1;
        }
    }

    public String addDay(String date, int number)
    {
        DateConverter dateConverter = new DateConverter();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        try
        {
            Date d1 = sdf.parse(getMiladiDate(date));
            Calendar calendar =Calendar.getInstance();
            calendar.setTime(d1);
            calendar.add(Calendar.DATE,number);

            int mYear = calendar.get(Calendar.YEAR);
            int mMonth = (calendar.get(Calendar.MONTH))+1;
            int mDay = calendar.get(Calendar.DAY_OF_MONTH);

            dateConverter.GregorianToPersian(mYear,mMonth,mDay);

            String s=dateConverter.toString().replace("-","/");

            return s;

        } catch (ParseException e)
        {
            return "";
        }
    }
}
