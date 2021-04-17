package com.example.boroodat.general;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.widget.TextView;

import com.mohamadamin.persianmaterialdatetimepicker.date.DatePickerDialog;
import com.mohamadamin.persianmaterialdatetimepicker.utils.PersianCalendar;


public class Date
{
    public TextView textView;
    private Context context;

    public Date(TextView textView, Context context)
    {
        this.textView = textView;
        this.context = context;
    }

    public void setDate()
    {
        FragmentManager fm = ((Activity) context).getFragmentManager();

        PersianCalendar now = new PersianCalendar();
        DatePickerDialog dpd = DatePickerDialog.newInstance(
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth)
                    {
                        monthOfYear=monthOfYear+1;
                        String day=dayOfMonth < 10 ? "0" + dayOfMonth : "" + dayOfMonth;
                        String month=monthOfYear < 10 ? "0" + monthOfYear : "" +  monthOfYear;
                        String date=year+"/"+month+"/"+day;
                        textView.setText(date);
                        textView.setError(null);
                    }

                },
                now.getPersianYear(),
                now.getPersianMonth(),
                now.getPersianDay()
        );
        dpd.setThemeDark(false);
        dpd.show(fm,"FamilyFund");
    }


   /* public void time()
    {
        PersianCalendar now = new PersianCalendar();
        TimePickerDialog tpd = TimePickerDialog.newInstance(
                new TimePickerDialog.OnTimeSetListener()
                {
                    @Override
                    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute)
                    {
                        String hourString = hourOfDay < 10 ? "0" + hourOfDay : "" + hourOfDay;
                        String minuteString = minute < 10 ? "0" + minute : "" + minute;
                        String time = "You picked the following time: " + hourString + ":" + minuteString;
                        Toast.makeText(getApplicationContext(), time, Toast.LENGTH_LONG).show();
                    }
                },
                now.get(PersianCalendar.HOUR_OF_DAY),
                now.get(PersianCalendar.MINUTE),
                true
        );
        tpd.setThemeDark(false);
        tpd.setOnCancelListener(new DialogInterface.OnCancelListener()
        {
            @Override
            public void onCancel(DialogInterface dialogInterface)
            {
                Toast.makeText(getApplicationContext(), "TimePickerDialog Canceled", Toast.LENGTH_LONG).show();
            }
        });
        tpd.show(getFragmentManager(), "FuLLKade");
    }*/


}
