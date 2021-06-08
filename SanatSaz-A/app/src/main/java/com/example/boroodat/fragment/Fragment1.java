package com.example.boroodat.fragment;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.boroodat.R;
import com.example.boroodat.activity.Activity11_BuyerReports;
import com.example.boroodat.activity.Activity12_MaterialReports;
import com.example.boroodat.activity.Activity13_SalaryReports;
import com.example.boroodat.activity.Activity15_ExpenseReports;
import com.example.boroodat.activity.Activity3_DepositReports;
import com.example.boroodat.activity.Activity5_SaleReports;
import com.example.boroodat.databinding.Fragment1Binding;
import com.example.boroodat.general.AppController;
import com.example.boroodat.general.NumberTextWatcherForThousand;
import com.example.boroodat.general.User_Info;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;


public class Fragment1
{

    private Fragment1Binding binding;
    private Context context;
    private DecimalFormat df = new DecimalFormat ( "#,###" );

    public Fragment1(Context context)
    {
        this.context = context;
    }

    public View getView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        binding = Fragment1Binding.inflate(inflater, container, false);
        View view = binding.getRoot();

        //-----------------------------------------------------------------

        getReport();

        //-----------------------------------------------------------------

        binding.sale.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(context, Activity5_SaleReports.class);
                intent.putExtra("from","manager");
                context.startActivity(intent);
            }
        });
        //-----------------------------------------------------------------

        binding.otherExpenses.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(context, Activity15_ExpenseReports.class);
                intent.putExtra("from","manager");
                context.startActivity(intent);
            }
        });
        //-----------------------------------------------------------------

        binding.rawMaterial.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(context, Activity12_MaterialReports.class);
                intent.putExtra("from","manager");
                context.startActivity(intent);
            }
        });
        //-----------------------------------------------------------------


        binding.deposits.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(context, Activity3_DepositReports.class);
                context.startActivity(intent);
            }
        });
        //-----------------------------------------------------------------

        binding.salary.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(context, Activity13_SalaryReports.class);
                context.startActivity(intent);
            }
        });
        //-----------------------------------------------------------------

        binding.buyer.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(context, Activity11_BuyerReports.class);
                context.startActivity(intent);
            }
        });
        //-----------------------------------------------------------------
        return view;
    }

    public void getReport()
    {

        String url = context.getString(R.string.domain) + "api/general/report1";

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
                try
                {
                    String code = response.getString("code");

                    if (code.equals("200"))
                    {
                        JSONArray result = response.getJSONArray("result");
                        JSONObject object1 = result.getJSONObject(0);
                        double material = Double.parseDouble(object1.getString("material"));
                        double material_payment = Double.parseDouble(object1.getString("material_payment"));
                        double salary = Double.parseDouble(object1.getString("salary"));
                        double expense = Double.parseDouble(object1.getString("expense"));
                        double expense_payment = Double.parseDouble(object1.getString("expense_payment"));
                        double sale = Double.parseDouble(object1.getString("sale"));
                        double sale_payment = Double.parseDouble(object1.getString("sale_payment"));
                        double deposit = Double.parseDouble(object1.getString("deposit"));


                        binding.rSale.setText(df.format(Math.round(sale)) + " ریال");
                        binding.rRecive.setText(df.format(Math.round(deposit)) + " ریال");
                        binding.rSalary.setText(df.format(Math.round(salary)) + " ریال");
                        binding.rOtherExpense.setText(df.format(Math.round(expense)) + " ریال");
                        binding.rRawMaterial.setText(df.format(Math.round(material)) + " ریال");

                        //---------------------------------------------------------
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
}
