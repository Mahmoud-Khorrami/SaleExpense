package com.example.boroodat.general;

import android.content.Context;
import android.content.pm.PackageManager;

import androidx.core.content.ContextCompat;

public class CheckPermission
{
    private Context context;
    private String permission;

    public CheckPermission(Context context, String permission)
    {
        this.context = context;
        this.permission = permission;
    }

    public boolean check()
    {
        return ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED;
    }
}
