package com.example.boroodat.general;

import java.util.Calendar;

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
}
