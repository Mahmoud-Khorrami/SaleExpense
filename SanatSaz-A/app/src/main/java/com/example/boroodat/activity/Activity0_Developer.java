package com.example.boroodat.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.boroodat.adapter.Activity0_Adapter;
import com.example.boroodat.database.User_Info_DB;
import com.example.boroodat.databinding.A0AddBinding;
import com.example.boroodat.databinding.Activity0DeveloperBinding;
import com.example.boroodat.general.AppController;
import com.example.boroodat.general.Date;
import com.example.boroodat.general.Internet;
import com.example.boroodat.general.User_Info;
import com.example.boroodat.interfaces.RecyclerViewItemClickInterface1;
import com.example.boroodat.model.Activity0_Model;
import com.example.boroodat.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dmax.dialog.SpotsDialog;
import io.realm.Realm;
import io.realm.RealmResults;

public class Activity0_Developer extends AppCompatActivity
{
    private List<Activity0_Model> models =new ArrayList<>(  );
    private Activity0_Adapter adapter;
    private Context context=this;
    private AlertDialog.Builder alertDialogBuilder=null;
    private android.app.AlertDialog progressDialog;
    private Activity0DeveloperBinding binding;
    private Realm realm;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        binding = Activity0DeveloperBinding.inflate(getLayoutInflater());
        View view= binding.getRoot();
        setContentView(view);

        realm=Realm.getDefaultInstance();
        //----------------------------------------------------------------------------------------------------------

        progressDialog = new SpotsDialog(context,R.style.Custom);
        progressDialog.setCancelable(false);

        //----------------------------------------------------------------------------------------------------------

        adapter = new Activity0_Adapter(models, Activity0_Developer.this );
        binding.recyclerView.setLayoutManager ( new LinearLayoutManager( Activity0_Developer.this ) );
        binding.recyclerView.setAdapter (adapter);

        if (new Internet(context).check())
            addCompany();
        else
            new Internet(context).enable();

        //----------------------------------------------------------------------------------------------------------

        binding.fab.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                dialog();
            }
        });

        //----------------------------------------------------------------------------------------------------------

        adapter.setOnItemClickListener(new RecyclerViewItemClickInterface1()
        {
            @Override
            public void onItemClick(View v, final Activity0_Model model1)
            {
                realm.executeTransaction(new Realm.Transaction()
                {
                    @Override
                    public void execute(Realm realm)
                    {
                        RealmResults<User_Info_DB> res = realm.where(User_Info_DB.class).findAll();

                        if (res.size()>0)
                        {
                            res.get(0).setCompany_id(model1.getId() + "");
                            res.get(0).setCompany_name(model1.getName());
                        }
                    }
                });

                Intent intent = new Intent(Activity0_Developer.this, Activity2_Manager.class);
                startActivity(intent);
            }
        });
    }

    private void dialog()
    {
        final A0AddBinding binding1 = A0AddBinding.inflate(LayoutInflater.from(context));
        View view = binding1.getRoot();
        alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setView(view);

        //----------------------------------------------------------------------------------------------------------

        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setPositiveButton("تایید", null);
        alertDialogBuilder.setNeutralButton("لغو", null);
        final AlertDialog alertDialog = alertDialogBuilder.create();

        //----------------------------------------------------------------------------------------------------------

        binding1.date.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                new Date(binding1.date,context).setDate();
            }
        });

        //----------------------------------------------------------------------------------------------------------

        alertDialog.setOnShowListener(new DialogInterface.OnShowListener()
        {
            @Override
            public void onShow(DialogInterface dialogInterface)
            {
                Button add = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
                add.setTextColor(getResources().getColor(R.color.black));
                add.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        if (binding1.name.getText().toString().equals(""))
                            binding1.name.setError("نام شرکت را وارد کنید.");

                        else if (binding1.date.getText().toString().equals(""))
                            binding1.date.setError("تاریخ ایجاد شرکت را وارد کنید.");

                        else
                        {
                            if (new Internet(context).check())
                                createCompany(binding1.name.getText().toString(), binding1.date.getText().toString(),alertDialog);

                            else
                                new Internet(context).enable();
                        }
                    }
                });


                Button cancel = alertDialog.getButton(AlertDialog.BUTTON_NEUTRAL);
                cancel.setTextColor(getResources().getColor(R.color.black));
                cancel.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        alertDialog.dismiss();
                        alertDialogBuilder = null;
                    }
                });
            }
        });

        //------------------------------------------------------------------------------------------

        alertDialog.getWindow().setBackgroundDrawable(context.getResources().getDrawable(R.drawable.bkg127));
        alertDialog.show();
        DisplayMetrics display = context.getResources().getDisplayMetrics();
        int width = display.widthPixels;
        width = (int) ((width) * ((double) 4 / 5));
        alertDialog.getWindow().setLayout(width, LinearLayout.LayoutParams.WRAP_CONTENT);
    }

    public void createCompany(String name, String date, final AlertDialog alertDialog)
    {
        String url = getString(R.string.domain) + "api/company/create";
        progressDialog.show();

        JSONObject object = new JSONObject();
        try
        {
            object.put("name", name);
            object.put("date", date);
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
                alertDialog.dismiss();
                alertDialogBuilder=null;
                Toast.makeText(getApplicationContext(), "شرکت جدید با موفقیت ایجاد شد.", Toast.LENGTH_SHORT).show();
                addCompany();

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

    public void addCompany()
    {
        String url = getString(R.string.domain) + "api/company/show";
        progressDialog.show();

        final JSONObject object = new JSONObject();
        try
        {
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

                models.clear();
                progressDialog.dismiss();
                try
                {
                    JSONArray array=response.getJSONArray("companies");

                    for (int i=array.length()-1;i>=0;i--)
                    {
                        JSONObject object1=array.getJSONObject(i);

                        int id=Integer.parseInt(object1.getString("id"));
                        String name=object1.getString("name");
                        String date=object1.getString("date");

                        models.add(new Activity0_Model(id,name,date,"0"));
                    }

                    adapter.notifyDataSetChanged();

                }
                catch (Exception e)
                {

                }

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
        request.setRetryPolicy(new DefaultRetryPolicy(10000, 0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES));
        AppController.getInstance().addToRequestQueue(request);

    }

    /*
    public void getSale()
    {
        String url = getString(R.string.domain) + "api/general/get-sales";
        progressDialog.show();

        final JSONObject object = new JSONObject();
        try
        {
            object.put("company_id", "8");
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
                try
                {
                    JSONArray array=response.getJSONArray("result");

                    JSONArray payments = new JSONArray();

                    for (int i=0;i<array.length() ;i++)
                    {
                        JSONObject object1=array.getJSONObject(i);

                        if (!object1.getString("payment").equals("0"))
                        {
                            JSONObject object2 = new JSONObject();
                            object2.put("company_id", object1.getString("company_id"));
                            object2.put("sale_id", object1.getString("id"));
                            object2.put("amount", object1.getString("payment"));
                            object2.put("date", object1.getString("date"));
                            object2.put("description", object1.getString("description"));

                            payments.put(object2);
                        }

                    }

                    SavePayment(payments);
                }
                catch (Exception e)
                {

                }

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
        request.setRetryPolicy(new DefaultRetryPolicy(10000, 0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES));
        AppController.getInstance().addToRequestQueue(request);

    }

    public void SavePayment(JSONArray payments)
    {
        String url = getString(R.string.domain) + "api/general/save-payments";

        final JSONObject object = new JSONObject();
        try
        {
            object.put("payments", payments);
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


                Toast.makeText(getApplicationContext(), response+"", Toast.LENGTH_LONG).show();

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
        request.setRetryPolicy(new DefaultRetryPolicy(10000, 0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES));
        AppController.getInstance().addToRequestQueue(request);

    }*/
}
