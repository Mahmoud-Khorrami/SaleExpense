package com.example.boroodat.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.boroodat.R;
import com.example.boroodat.adapter.Activity21_Adapter;
import com.example.boroodat.databinding.Activity21BuyerDetailsBinding;
import com.example.boroodat.general.AppController;
import com.example.boroodat.general.CheckPermission;
import com.example.boroodat.general.RuntimePermissionsActivity;
import com.example.boroodat.general.Share;
import com.example.boroodat.general.TodayDate;
import com.example.boroodat.general.User_Info;
import com.example.boroodat.model.activity21.Activity21_LoadingModel;
import com.example.boroodat.model.activity21.Activity21_MainModel;
import com.example.boroodat.model.activity21.Activity21_NotFoundModel;
import com.example.boroodat.model.activity21.Activity21_ParentModel;
import com.example.boroodat.model.activity21.Activity21_RetryModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Activity21_BuyerDetails extends RuntimePermissionsActivity
{

    private Activity21BuyerDetailsBinding binding;
    private List<Activity21_ParentModel> models=new ArrayList<>();
    private Activity21_Adapter adapter;
    private String buyer_id;
    private Context context = this;
    private DecimalFormat df = new DecimalFormat ( "#,###" );

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        binding = Activity21BuyerDetailsBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        //-----------------------------------------------------------------------

        binding.select.setVisibility(View.GONE);
        binding.refresh.setVisibility(View.GONE);
        binding.lnr2.setVisibility(View.GONE);

        //-----------------------------------------------------------------------

        Bundle bundle = getIntent().getExtras();
        buyer_id =bundle.getString("buyer_id");
        String buyer_name = bundle.getString("buyer_name");
        String phone_number = bundle.getString("phone_number");

        binding.buyerName.setText(buyer_name);
        binding.phoneNumber.setText(phone_number);

        //-----------------------------------------------------------------------

        adapter = new Activity21_Adapter(models, Activity21_BuyerDetails.this,binding.select);
        binding.recyclerView.setLayoutManager ( new LinearLayoutManager( Activity21_BuyerDetails.this ) );
        binding.recyclerView.setAdapter (adapter);
        getSales();

        //---------------------------------------------------------


        ArrayList<String> searchItem=new ArrayList<>();
        searchItem.add("شماره فاکتور");
        searchItem.add("تاریخ");

        ArrayAdapter<String> adp = new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_item, searchItem);
        binding.spinner.setAdapter(adp);
        binding.spinner.setSelection(0);

        //----------------------------------------------------------------------

        binding.share.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if (new CheckPermission(context,Manifest.permission.READ_EXTERNAL_STORAGE).check() &&
                        new CheckPermission(context,Manifest.permission.WRITE_EXTERNAL_STORAGE).check())
                    share();

                else
                    Activity21_BuyerDetails.super.requestAppPermissions(new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, 10, context);
            }
        });

        //---------------------------------------------------------

        binding.select.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                List<Activity21_ParentModel> models1 = new ArrayList<>();

                double sum1 =0;
                double payment1 =0;
                for (int i=0; i<models.size(); i++)
                {
                    if (models.get(i).getCurrentType() == Activity21_ParentModel.Main)
                    {
                        Activity21_MainModel model = (Activity21_MainModel) models.get(i);
                        if (model.isSelect())
                        {
                            model.setSelect(false);
                            models1.add(model);
                            sum1 = sum1 + Double.parseDouble(model.getSum());
                            payment1 = payment1 + Double.parseDouble(model.getPayment());
                        }
                    }
                }

                adapter.setCounter();

                models.clear();
                for (int i=0; i<models1.size(); i++)
                    models.add(models1.get(i));
                adapter.notifyDataSetChanged();

                binding.select.setVisibility(View.GONE);
                binding.refresh.setVisibility(View.VISIBLE);

                binding.sum.setText(df.format(Math.round(sum1)) + " ریال");
                binding.payment.setText(df.format(Math.round(payment1)) + " ریال");
                binding.remain.setText(df.format(Math.round(sum1-payment1)) + " ریال");
            }
        });

        //---------------------------------------------------------

        binding.refresh.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                getSales();
                binding.refresh.setVisibility(View.GONE);
            }
        });


        //---------------------------------------------------------

        binding.search.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                binding.lnr1.setVisibility(View.GONE);
                binding.lnr2.setVisibility(View.VISIBLE);
            }
        });


        //---------------------------------------------------------

        binding.searchview.setOnCloseListener(new SearchView.OnCloseListener()
        {
            @Override
            public boolean onClose()
            {
                binding.lnr1.setVisibility(View.VISIBLE);
                binding.lnr2.setVisibility(View.GONE);
                return true;
            }
        });

        //---------------------------------------------------------

        binding.searchview.setOnQueryTextListener(new SearchView.OnQueryTextListener()
        {
            @Override
            public boolean onQueryTextSubmit(String query)
            {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText)
            {
                newText = newText.toLowerCase();

                if (binding.spinner.getSelectedItem().toString().equals("شماره فاکتور"))
                    searchQuery("factor_number", newText);

                else if (binding.spinner.getSelectedItem().toString().equals("تاریخ"))
                    searchQuery("date", newText);

                return true;
            }
        });
    }

    private void getSales()
    {

        String url = getString(R.string.domain) + "api/sale/get-buyer-sales";

        models.clear();
        models.add(new Activity21_LoadingModel());
        adapter.notifyDataSetChanged();

        JSONObject object = new JSONObject();
        try
        {
            object.put("buyer_id", buyer_id);
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
                try
                {
                    String code = response.getString("code");

                    models.clear();

                    if (code.equals("200"))
                    {

                        JSONArray result = response.getJSONArray("result");

                        double sum1 = 0;
                        double payment1 = 0;

                        for (int i=result.length()-1; i>=0; i--)
                        {
                            JSONObject object1 = result.getJSONObject(i);

                            JSONObject sale = object1.getJSONObject("sale");

                            String id = sale.getString("id");
                            String factor_number = sale.getString("factor_number");
                            String date = sale.getString("date");
                            String sum = sale.getString("sum");
                            String payment = sale.getString("payment");
                            String account_id = sale.getString("account_id");
                            String account_title = object1.getString("account_title");
                            String buyer_id = sale.getString("buyer_id");
                            String buyer_name = object1.getString("buyer_name");
                            String driver_id = sale.getString("driver_id");
                            String driver_name = object1.getString("driver_name");
                            String description = sale.getString("description");
                            String detailsDescription = object1.getString("description");

                            models.add(new Activity21_MainModel(id,factor_number,date,sum,payment,account_id,account_title,buyer_id,buyer_name,driver_id,driver_name,description,detailsDescription));

                            //------------------------------------------------------

                            sum1 = sum1 + Double.parseDouble(sum);
                            payment1 = payment1 + Double.parseDouble(payment);
                        }

                        adapter.notifyDataSetChanged();

                        binding.sum.setText(df.format(Math.round(sum1)) + " ریال");
                        binding.payment.setText(df.format(Math.round(payment1)) + " ریال");
                        binding.remain.setText(df.format(Math.round(sum1 - payment1)) + " ریال");
                    }

                    else if (code.equals("207"))
                    {
                        models.add(new Activity21_NotFoundModel());
                        adapter.notifyDataSetChanged();
                    }


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
                models.clear();
            }
        };


        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, object, listener, errorListener)
        {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError
            {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Accept", "application/json");
                headers.put("Authorization", "Bearer "+  new User_Info().token());
                return headers;
            }
        };
        request.setRetryPolicy(new DefaultRetryPolicy(10000, 0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES));
        AppController.getInstance().addToRequestQueue(request);

    }

    public void searchQuery(String type, String value)
    {
        String url = getString(R.string.domain) + "api/sale/buyer-search-query";


        models.clear();
        models.add(new Activity21_LoadingModel());
        adapter.notifyDataSetChanged();

        JSONObject object = new JSONObject();
        try
        {
            object.put("type",type);
            object.put("value",value);
            object.put("buyer_id",buyer_id);
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
                try
                {
                    String code = response.getString("code");

                    models.clear();

                    if (code.equals("200"))
                    {

                        JSONArray result = response.getJSONArray("result");

                        double sum1 = 0;
                        double payment1 = 0;

                        for (int i=result.length()-1; i>=0; i--)
                        {
                            JSONObject object1 = result.getJSONObject(i);

                            JSONObject sale = object1.getJSONObject("sale");

                            String id = sale.getString("id");
                            String factor_number = sale.getString("factor_number");
                            String date = sale.getString("date");
                            String sum = sale.getString("sum");
                            String payment = sale.getString("payment");
                            String account_id = sale.getString("account_id");
                            String account_title = object1.getString("account_title");
                            String buyer_id = sale.getString("buyer_id");
                            String buyer_name = object1.getString("buyer_name");
                            String driver_id = sale.getString("driver_id");
                            String driver_name = object1.getString("driver_name");
                            String description = sale.getString("description");
                            String detailsDescription = object1.getString("description");

                            models.add(new Activity21_MainModel(id,factor_number,date,sum,payment,account_id,account_title,buyer_id,buyer_name,driver_id,driver_name,description,detailsDescription));

                            //------------------------------------------------------

                            sum1 = sum1 + Double.parseDouble(sum);
                            payment1 = payment1 + Double.parseDouble(payment);
                        }

                        adapter.notifyDataSetChanged();

                        binding.sum.setText(df.format(Math.round(sum1)) + " ریال");
                        binding.payment.setText(df.format(Math.round(payment1)) + " ریال");
                        binding.remain.setText(df.format(Math.round(sum1 - payment1)) + " ریال");
                    }

                    else if (code.equals("207"))
                    {
                        models.add(new Activity21_NotFoundModel());
                        adapter.notifyDataSetChanged();
                    }


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
                models.clear();
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

    public void share()
    {
        binding.share.setVisibility(View.GONE);
        binding.search.setVisibility(View.GONE);

        int s1 =0;
        int s2 =0;
        int s3 =0;

        if (binding.refresh.getVisibility() == View.VISIBLE)
        {
            s1 = 1;
            binding.refresh.setVisibility(View.GONE);
        }

        if (binding.select.getVisibility() == View.VISIBLE)
        {
            s2 = 1;
            binding.select.setVisibility(View.GONE);
        }

        if (binding.lnr2.getVisibility() == View.VISIBLE)
        {
            s3 = 1;
            binding.lnr2.setVisibility(View.GONE);
            binding.lnr1.setVisibility(View.VISIBLE);
        }

        View rootView = findViewById(android.R.id.content).getRootView();
        new Share(context, rootView).screenShot();

        binding.share.setVisibility(View.VISIBLE);
        binding.search.setVisibility(View.VISIBLE);

        if (s1 == 1)
            binding.refresh.setVisibility(View.VISIBLE);

        if (s2==1)
            binding.select.setVisibility(View.VISIBLE);


        if (s3 == 1)
        {
            binding.lnr2.setVisibility(View.VISIBLE);
            binding.lnr1.setVisibility(View.GONE);
        }
    }

    @Override
    public void onPermissionsGranted(int requestCode)
    {
        if (requestCode == 10)
            share();
    }

    @Override
    public void onPermissionsDeny(int requestCode)
    {
        Toast.makeText ( getApplicationContext (),
                "مجوز دسترسی به حافظه داده نشد.", Toast.LENGTH_LONG ).show ();
    }
}
