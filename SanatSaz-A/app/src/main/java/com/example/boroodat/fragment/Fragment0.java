package com.example.boroodat.fragment;


import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.boroodat.R;
import com.example.boroodat.activity.Activity14_Personnel;
import com.example.boroodat.activity.Activity22_DefaultSetting;
import com.example.boroodat.activity.Activity24_AboutUs;
import com.example.boroodat.activity.Activity4_Users;
import com.example.boroodat.activity.Activity7_Account;
import com.example.boroodat.activity.Activity8_Buyer;
import com.example.boroodat.activity.Activity9_Driver;
import com.example.boroodat.database.User_Info_DB;
import com.example.boroodat.databinding.Dialog1Binding;
import com.example.boroodat.databinding.Fragment0Binding;
import com.example.boroodat.general.AppController;
import com.example.boroodat.general.CheckPermission;
import com.example.boroodat.general.RuntimePermissionsActivity;
import com.example.boroodat.general.User_Info;
import com.example.boroodat.general.WriteToExcel;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import dmax.dialog.SpotsDialog;
import io.realm.Realm;


public class Fragment0 extends RuntimePermissionsActivity
{

    private Fragment0Binding binding;
    private Context context;
    private android.app.AlertDialog progressDialog;
    private Realm realm;

    public Fragment0(Context context)
    {
        this.context = context;

        //----------------------------------------------

        realm = Realm.getDefaultInstance();

        progressDialog = new SpotsDialog(context,R.style.Custom);
        progressDialog.setCancelable(false);

    }

    public View getView(@NonNull LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        binding = Fragment0Binding.inflate(inflater, container, false);
        View view = binding.getRoot();

        //-------------------------------------------------------------------------------------------------------

        binding.users.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent=new Intent(context, Activity4_Users.class);
                context.startActivity(intent);
            }
        });

        //-------------------------------------------------------------------------------------------------------

        binding.accounts.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent=new Intent(context, Activity7_Account.class);
                context.startActivity(intent);
            }
        });

        //-------------------------------------------------------------------------------------------------------

        binding.buyers.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent=new Intent(context, Activity8_Buyer.class);
                context.startActivity(intent);
            }
        });

        //-------------------------------------------------------------------------------------------------------

        binding.drivers.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent=new Intent(context, Activity9_Driver.class);
                context.startActivity(intent);
            }
        });

        //-------------------------------------------------------------------------------------------------------

        binding.personnel.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent=new Intent(context, Activity14_Personnel.class);
                context.startActivity(intent);
            }
        });

        //-------------------------------------------------------------------------------------------------------

        binding.xls.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if (new CheckPermission(context,Manifest.permission.READ_EXTERNAL_STORAGE).check() &&
                new CheckPermission(context,Manifest.permission.WRITE_EXTERNAL_STORAGE).check())
                    getXls();

                else
                    Fragment0.super.requestAppPermissions(new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, 10, context);
            }
        });

        //-------------------------------------------------------------------------------------------------------

        binding.deletePass.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {

                final User_Info_DB info = realm.where(User_Info_DB.class).findFirst();

                if (!info.getPassword().equals(""))
                {
                    final Dialog1Binding binding1 = Dialog1Binding.inflate(LayoutInflater.from(context));
                    View view1 = binding1.getRoot();
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                    alertDialogBuilder.setView(view1);

                    //-------------------------------------------------------------------------------------------------------

                    alertDialogBuilder.setCancelable(false);
                    alertDialogBuilder.setPositiveButton("تایید", null);
                    alertDialogBuilder.setNeutralButton("لغو", null);
                    final AlertDialog alertDialog = alertDialogBuilder.create();

                    //-------------------------------------------------------------------------------------------------------

                    binding1.message.setText("آیا مایلید رمز عبور را از حافظه حذف کنید؟");

                    //-------------------------------------------------------------------------------------------------------

                    alertDialog.setOnShowListener(new DialogInterface.OnShowListener()
                    {
                        @Override
                        public void onShow(DialogInterface dialogInterface)
                        {
                            Button approve = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
                            approve.setTextColor(context.getResources().getColor(R.color.black));
                            approve.setOnClickListener(new View.OnClickListener()
                            {
                                @Override
                                public void onClick(View view)
                                {

                                    realm.executeTransaction(new Realm.Transaction()
                                    {
                                        @Override
                                        public void execute(Realm realm)
                                        {
                                            User_Info_DB infoDb = realm.where(User_Info_DB.class).findFirst();
                                            infoDb.setPassword("");
                                            Toast.makeText(context,"حذف رمز عبور از حافظه با موفقیت انجام شد.",Toast.LENGTH_LONG).show();
                                            alertDialog.dismiss();
                                        }
                                    });
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

                    //---------------------------------------------------------------------------------------------

                    alertDialog.show();
                    DisplayMetrics display = context.getResources().getDisplayMetrics();
                    int width = display.widthPixels;
                    width = (int) ((width) * ((double) 4 / 5));
                    alertDialog.getWindow().setLayout(width, LinearLayout.LayoutParams.WRAP_CONTENT);
                }

                else
                    Toast.makeText(context,"رمز عبور ذخیره نشده است.",Toast.LENGTH_LONG).show();
            }
        });

        //-------------------------------------------------------------------------------------------------------

        binding.aboutUs.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                context.startActivity(new Intent(context, Activity24_AboutUs.class));
            }
        });
        //-------------------------------------------------------------------------------------------------------

        binding.defaultSetting.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(context, Activity22_DefaultSetting.class);
                context.startActivity(intent);
            }
        });
        //-------------------------------------------------------------------------------------------------------

        return view;
    }

    public void getXls()
    {
        String url = context.getString(R.string.domain) + "api/general/xls";
        progressDialog.show();

        JSONObject object = new JSONObject();
        try
        {
            object.put("company_id", new User_Info().company_id());
            object.put("secret_key", context.getString(R.string.secret_key));
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

                Toast.makeText(context, "مجددا تلاش کنید.", Toast.LENGTH_LONG).show();
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
        request.setRetryPolicy(new DefaultRetryPolicy(3000, 1, DefaultRetryPolicy.DEFAULT_MAX_RETRIES));
        AppController.getInstance().addToRequestQueue(request);
    }

    @Override
    public void onPermissionsGranted(int requestCode)
    {
        if (requestCode == 10)
            getXls();
    }

    @Override
    public void onPermissionsDeny(int requestCode)
    {
        Toast.makeText ( getApplicationContext (),
                "مجوز دسترسی به حافظه داده نشد.", Toast.LENGTH_LONG ).show ();
    }
}
