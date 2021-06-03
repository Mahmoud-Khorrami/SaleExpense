package com.example.boroodat.fragment;


import android.content.Context;
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
import com.example.boroodat.databinding.Fragment3Binding;
import com.example.boroodat.general.AppController;
import com.example.boroodat.general.NumberTextWatcherForThousand;
import com.example.boroodat.general.User_Info;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

import io.realm.Realm;


public class Fragment3
{

    private Fragment3Binding binding;
    private Context context;
    private DecimalFormat df = new DecimalFormat ( "#,###" );

    public Fragment3(Context context)
    {
        this.context = context;
    }

    public View getView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        binding = Fragment3Binding.inflate(inflater, container, false);
        View view = binding.getRoot();

        //-------------------------------------------------------------------------------------------------------

        getReport();

        //-------------------------------------------------------------------------------------------------------
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


                        double fund = sale + deposit - expense - salary - material;
                        binding.rFund.setText(df.format(Math.round(fund)) + " ریال");

                        //---------------------------------------------------------

                        double balance = sale_payment + deposit - expense_payment - salary - material_payment;
                        binding.rBalance.setText(df.format(Math.round(balance)) + " ریال");

                        //---------------------------------------------------------

                        double demand = sale - sale_payment;
                        binding.rDemand.setText(df.format(Math.round(demand)) + " ریال");

                        //---------------------------------------------------------

                        double dept = expense - expense_payment + material - material_payment;
                        binding.rDept.setText(df.format(Math.round(dept)) + " ریال");

                        //---------------------------------------------------------

                        double incom = sale + deposit;
                        binding.rIncom.setText(df.format(Math.round(incom)) + " ریال");

                        //---------------------------------------------------------

                        double cost = salary + expense + material;
                        binding.rCost.setText(df.format(Math.round(cost)) + " ریال");

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
