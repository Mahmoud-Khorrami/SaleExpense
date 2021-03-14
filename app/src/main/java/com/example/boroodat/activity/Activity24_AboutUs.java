package com.example.boroodat.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.boroodat.R;
import com.example.boroodat.databinding.Activity24AboutUsBinding;

public class Activity24_AboutUs extends AppCompatActivity
{
    private Activity24AboutUsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        binding = Activity24AboutUsBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        //--------------------------------------------------------------------------------------

        binding.telegram.setOnClickListener ( new View.OnClickListener ()
        {
            @Override
            public void onClick(View v)
            {
                final String appName = "org.telegram.messenger";
                final boolean isAppInstalled = IsInstallPackManger ( getApplicationContext (), appName );
                if (isAppInstalled)
                {
                    Intent intent = new Intent ( Intent.ACTION_VIEW, Uri.parse ( "https://t.me/Mhdkh86" ) );
                    startActivity ( intent );
                } else
                {
                    Toast.makeText ( getApplicationContext (), "تگلرام روی گوشی اندرویدی شما نصب نمی باشد !", Toast.LENGTH_SHORT ).show ();
                }
            }
        } );

        //--------------------------------------------------------------------------------------

        binding.email.setOnClickListener ( new View.OnClickListener ()
        {
            @Override
            public void onClick(View v)
            {
                Intent send = new Intent ( Intent.ACTION_SENDTO );
                String uriText = "mailto:" + Uri.encode ( "telecom1387@gmail.com" ) +
                        "?subject=" + Uri.encode ( "نرم افزار " + getString(R.string.app_name) );
                Uri uri = Uri.parse ( uriText );

                send.setData ( uri );
                startActivity ( Intent.createChooser ( send, "Send mail..." ) );
            }
        } );

        //--------------------------------------------------------------------------------------

        binding.call.setOnClickListener ( new View.OnClickListener ()
        {
            @Override
            public void onClick(View v)
            {

                startActivity ( new Intent ( Intent.ACTION_DIAL, Uri.fromParts ( "tel", "+989179078518", null ) ) );
            }
        } );
    }

    public static boolean IsInstallPackManger(Context context, String appName)
    {
        PackageManager pm = context.getPackageManager ();
        try
        {
            pm.getPackageInfo ( appName, PackageManager.GET_ACTIVITIES );
            return true;
        } catch (PackageManager.NameNotFoundException e)
        {
            return false;
        }
    }
}
