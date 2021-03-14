package com.example.boroodat.general;

import android.content.Context;
import android.content.DialogInterface;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.boroodat.R;
import com.example.boroodat.adapter.Activity14_Adapter;
import com.example.boroodat.database.Activity14_DB;
import com.example.boroodat.databinding.A14AddBinding;
import com.example.boroodat.databinding.A6Add1Binding;
import com.example.boroodat.model.Activity14_Model;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dmax.dialog.SpotsDialog;
import io.realm.Realm;
import io.realm.RealmResults;

public class Personnel
{
    private List<Activity14_Model> models =new ArrayList<>(  );
    private Activity14_Adapter adapter;
    private Context context;
    private android.app.AlertDialog progressDialog;
    private Realm realm;

    public Personnel(Context context)
    {
        this.context = context;

        //--------------------------------------------

        realm=Realm.getDefaultInstance();

        progressDialog = new SpotsDialog(context, R.style.Custom);
        progressDialog.setCancelable(false);
    }

    public void dialog(TextView personnel_name, TextView personnel_id)
    {
        final A6Add1Binding binding1 = A6Add1Binding.inflate(LayoutInflater.from(context));
        View view = binding1.getRoot();
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setView(view);

        //----------------------------------------------------------------------------------------------------------

        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setPositiveButton("ایجاد پرسنل جدید", null);
        alertDialogBuilder.setNeutralButton("لغو", null);
        final AlertDialog alertDialog = alertDialogBuilder.create();

        //----------------------------------------------------------------------------------------------------------

        binding1.title.setText("لیست پرسنل");

        //----------------------------------------------------------------------------------------------------------

        progressDialog = new SpotsDialog(context, R.style.Custom);
        progressDialog.setCancelable(false);

        //----------------------------------------------------------------------------------------------------------

        adapter = new Activity14_Adapter(models, context,2,personnel_name,personnel_id,alertDialog );
        binding1.recyclerView.setLayoutManager ( new LinearLayoutManager(context) );
        binding1.recyclerView.setAdapter (adapter);
        addPersonnel();

        //----------------------------------------------------------------------------------------------------------
        alertDialog.setOnShowListener(new DialogInterface.OnShowListener()
        {
            @Override
            public void onShow(DialogInterface dialogInterface)
            {
                Button add = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
                add.setTextColor(context.getResources().getColor(R.color.black));
                add.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        if (new Internet(context).check())
                        {
                            addPersonnelDialog();
                        }
                        else
                            new Internet(context).enable();
                    }
                });


                Button cancel = alertDialog.getButton(AlertDialog.BUTTON_NEUTRAL);
                cancel.setTextColor(context.getResources().getColor(R.color.black));
                cancel.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        alertDialog.dismiss();
                    }
                });
            }
        });

        //----------------------------------------------------------------------------------------------------------

        alertDialog.getWindow().setBackgroundDrawable(context.getResources().getDrawable(R.drawable.rounded_linear));
        alertDialog.show();
        DisplayMetrics display = context.getResources().getDisplayMetrics();
        int width = display.widthPixels;
        width = (int) ((width) * ((double) 4 / 5));
        alertDialog.getWindow().setLayout(width, LinearLayout.LayoutParams.WRAP_CONTENT);
    }

    private void addPersonnelDialog()
    {
        final A14AddBinding binding1 = A14AddBinding.inflate(LayoutInflater.from(context));
        View view = binding1.getRoot();
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setView(view);

        //----------------------------------------------------------------------------------------------------------

        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setPositiveButton("تایید", null);
        alertDialogBuilder.setNeutralButton("لغو", null);
        final AlertDialog alertDialog = alertDialogBuilder.create();

        //----------------------------------------------------------------------------------------------------------

        binding1.registerDate.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                new Date(binding1.registerDate,context).setDate();
            }
        });

        //----------------------------------------------------------------------------------------------------------

        alertDialog.setOnShowListener(new DialogInterface.OnShowListener()
        {
            @Override
            public void onShow(DialogInterface dialogInterface)
            {
                Button add = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
                add.setTextColor(context.getResources().getColor(R.color.black));
                add.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        if (binding1.name.getText().toString().equals(""))
                            binding1.name.setError("نام و نام خانوادگی را وارد کنید.");

                        else if (binding1.phoneNumber.getText().toString().equals(""))
                            binding1.phoneNumber.setError("شماره همراه را وارد کنید.");

                        else if (binding1.registerDate.getText().toString().equals(""))
                            binding1.registerDate.setError("تاریخ عضویت را وارد کنید.");

                        else
                        {
                            String role1="-";
                            String credit_card1="-";

                            if (!binding1.role.getText().toString().equals(""))
                                role1=binding1.role.getText().toString();

                            if (!binding1.creditCard.getText().toString().equals(""))
                                credit_card1=binding1.creditCard.getText().toString();

                            if (new Internet(context).check())
                            {
                                createPersonnel(binding1.name.getText().toString(),binding1.phoneNumber.getText().toString(),binding1.registerDate.getText().toString(),role1,credit_card1,alertDialog);
                            }
                            else
                                new Internet(context).enable();

                        }
                    }
                });


                Button cancel = alertDialog.getButton(AlertDialog.BUTTON_NEUTRAL);
                cancel.setTextColor(context.getResources().getColor(R.color.black));
                cancel.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        alertDialog.dismiss();
                    }
                });
            }
        });

        //------------------------------------------------------------------------------------------------

        alertDialog.getWindow().setBackgroundDrawable(context.getResources().getDrawable(R.drawable.rounded_linear));
        alertDialog.show();
        DisplayMetrics display = context.getResources().getDisplayMetrics();
        int width = display.widthPixels;
        width = (int) ((width) * ((double) 4 / 5));
        alertDialog.getWindow().setLayout(width, LinearLayout.LayoutParams.WRAP_CONTENT);
    }

    public void createPersonnel(final String name, final String phone_number, final String register_date, final String role, final String credit_card, final AlertDialog alertDialog)
    {
        String url = context.getString(R.string.domain) + "api/personnel/create";
        progressDialog.show();

        JSONObject object = new JSONObject();
        try
        {
            object.put("company_id", new User_Info().company_id());
            object.put("name", name);
            object.put("phone_number",phone_number);
            object.put("register_date",register_date);
            object.put("role", role);
            object.put("credit_card",credit_card);
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
                try
                {
                    int id = Integer.parseInt(response.getString("id"));
                    realm.beginTransaction();
                    realm.copyToRealmOrUpdate(new Activity14_DB(id, name, phone_number,register_date, role,credit_card,"-"));
                    realm.commitTransaction();

                    //----------------------------------------------------

                    progressDialog.dismiss();
                    Toast.makeText(context, "ایجاد پرسنل جدید با موفقیت انجام شد.", Toast.LENGTH_SHORT).show();
                    alertDialog.dismiss();

                    addPersonnel();

                } catch (JSONException e)
                {
                    e.printStackTrace();
                }
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

    public void addPersonnel()
    {
        RealmResults<Activity14_DB> res = realm.where(Activity14_DB.class).findAll();

        models.clear();

        for (int i=0;i<res.size();i++)
        {
            models.add(new Activity14_Model(res.get(i).getId(),res.get(i).getName(),res.get(i).getPhone_number(),res.get(i).getRegister_date(),res.get(i).getRole(),res.get(i).getCredit_card(),res.get(i).getExit_date()));
        }

        adapter.notifyDataSetChanged();
    }
}
