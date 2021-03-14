package com.example.boroodat.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.boroodat.R;
import com.example.boroodat.databinding.Activity23SettingBinding;
import com.example.boroodat.general.AppController;
import com.example.boroodat.general.Backup;
import com.example.boroodat.general.Date;
import com.example.boroodat.general.RuntimePermissionsActivity;
import com.example.boroodat.general.SaveData;
import com.example.boroodat.general.TodayDate;
import com.example.boroodat.general.User_Info;
import com.example.boroodat.general.WriteToExcel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import dmax.dialog.SpotsDialog;
import io.realm.Realm;
import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.read.biff.BiffException;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

public class Activity23_Setting extends RuntimePermissionsActivity
{

    private Activity23SettingBinding binding;
    private Context context=this;
    private AlertDialog progressDialog;
    private int code = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        binding = Activity23SettingBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        //---------------------------------------------------------------------------

        progressDialog = new SpotsDialog(this,R.style.Custom);
        progressDialog.setCancelable(true);

        //---------------------------------------------------------------------------

        binding.excelExport.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if (Environment.getExternalStorageDirectory().canWrite())
                    getData14();

                else
                    Activity23_Setting.super.requestAppPermissions ( new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, code );
            }
        });

        //---------------------------------------------------------------------------

        binding.aboutUs.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                startActivity(new Intent(Activity23_Setting.this,Activity24_AboutUs.class));
            }
        });

    }

    public void getData14()
    {
        String url = getString(R.string.domain) + "api/general/data14";
        progressDialog.show();

        JSONObject object = new JSONObject();
        try
        {
            object.put("company_id", new User_Info().company_id());
            object.put("secret_key", getString(R.string.secret_key));
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }

        Response.Listener<JSONObject> listener = new Response.Listener<JSONObject>()
        {
            @Override
            public void onResponse(JSONObject response)
            {
                progressDialog.dismiss();
                new WriteToExcel(context).export(response);
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {

                Toast.makeText(getApplicationContext(), "مجددا تلاش کنید.", Toast.LENGTH_LONG).show();
                progressDialog.dismiss();

            }
        };


        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, object, listener, errorListener)
        {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError
            {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Accept", "application/json");
                headers.put("Authorization", "Bearer "+ new User_Info().token());
                return headers;
            }
        };
        request.setRetryPolicy(new DefaultRetryPolicy(3000, 3, DefaultRetryPolicy.DEFAULT_MAX_RETRIES));
        AppController.getInstance().addToRequestQueue(request);
    }

    @Override
    public void onPermissionsGranted(int requestCode)
    {
        if (requestCode == code)
            Toast.makeText ( getApplicationContext (),
                    "مجوز دسترسی به حافظه داده شد.", Toast.LENGTH_SHORT ).show ();
    }

    @Override
    public void onPermissionsDeny(int requestCode)
    {
        Toast.makeText ( getApplicationContext (),
                "مجوز دسترسی به حافظه داده نشد.", Toast.LENGTH_LONG ).show ();
    }
}
