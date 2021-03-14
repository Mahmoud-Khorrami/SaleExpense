package com.example.boroodat.general;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.example.boroodat.R;
import com.example.boroodat.databinding.InternetBinding;


public class Internet
{
    private Context context;

    public Internet(Context context)
    {
        this.context = context;
    }

    public boolean check()
    {
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

            if (networkInfo == null || !networkInfo.isAvailable() || !networkInfo.isConnected())
            {

                return false;
            } else
                return true;
    }

    public void enable()
    {
        final InternetBinding binding1 = InternetBinding.inflate(LayoutInflater.from(context));
        View view = binding1.getRoot();
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setView(view);

        //-------------------------------------------------------------------------------------------------------


        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setPositiveButton("اینترنت WI-FI", null);
        alertDialogBuilder.setNegativeButton("اینترنت موبایل",null);
        alertDialogBuilder.setNeutralButton("لغو", null);
        final AlertDialog alertDialog = alertDialogBuilder.create();

        //-------------------------------------------------------------------------------------------------------

        binding1.txtDialog.setText("اتصال شما به اینترنت برقرار نیست.برای استفاده از نرم افزار به اینترنت متصل شوید.");

        //-------------------------------------------------------------------------------------------------------

        alertDialog.setOnShowListener(new DialogInterface.OnShowListener()
        {
            @Override
            public void onShow(DialogInterface dialogInterface)
            {
                Button WiFi = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
                WiFi.setTextColor(context.getResources().getColor(R.color.black));
                WiFi.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View view)
                    {
                        Intent intent = new Intent(WifiManager.ACTION_PICK_WIFI_NETWORK);
                        context.startActivity(intent);
                        alertDialog.dismiss();
                    }
                });

                Button Data1 = alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE);
                Data1.setTextColor(context.getResources().getColor(R.color.black));
                Data1.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        Intent intent = new Intent(Settings.ACTION_WIRELESS_SETTINGS);
                        context.startActivity(intent);
                        alertDialog.dismiss();
                    }
                });

                Button Cancel = alertDialog.getButton(AlertDialog.BUTTON_NEUTRAL);
                Cancel.setTextColor(context.getResources().getColor(R.color.black));
                Cancel.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        alertDialog.dismiss();
                    }
                });
            }
        });

        //....................................................................................................

        alertDialog.getWindow().setBackgroundDrawable(context.getResources().getDrawable(R.drawable.rounded_linear));
        alertDialog.show();
        DisplayMetrics display = context.getResources().getDisplayMetrics();
        int width = display.widthPixels;
        width = (int) ((width) * ((double) 4 / 5));
        alertDialog.getWindow().setLayout(width, LinearLayout.LayoutParams.WRAP_CONTENT);
    }

}
