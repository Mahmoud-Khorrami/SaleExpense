package com.example.boroodat.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.boroodat.R;
import com.example.boroodat.databinding.Activity20UserBinding;
import com.example.boroodat.general.AppController;
import com.example.boroodat.general.Internet;
import com.example.boroodat.general.SaveData;
import com.example.boroodat.general.User_Info;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import dmax.dialog.SpotsDialog;

public class Activity20_User extends AppCompatActivity
{

    private Activity20UserBinding binding;
    private AlertDialog progressDialog;
    private Context context=this;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        binding = Activity20UserBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        binding.title.setText(new User_Info().company_name());
        //-------------------------------------------------------------------------------------------------------

        progressDialog = new SpotsDialog(this,R.style.Custom);
        progressDialog.setCancelable(true);

        //-------------------------------------------------------------------------------------------------------

        binding.sale.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent=new Intent(Activity20_User.this, Activity6_RecordSales.class);
                intent.putExtra("from","user");
                startActivity(intent);
            }
        });
        //-------------------------------------------------------------------------------------------------------

        binding.expense.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent=new Intent(Activity20_User.this, Activity16_RecordExpense.class);
                intent.putExtra("from","user");
                startActivity(intent);
            }
        });

        //-------------------------------------------------------------------------------------------------------

        binding.report.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if (new Internet(context).check())
                    getData8();
                else
                    new Internet(context).enable();
            }
        });

        //-------------------------------------------------------------------------------------------------------

        binding.setting.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                startActivity(new Intent(Activity20_User.this,Activity24_AboutUs.class));
            }
        });
    }

    public void getData8()
    {
        String url = getString(R.string.domain) + "api/general/data8";
        progressDialog.show();

        final JSONObject object = new JSONObject();
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

                try
                {
                    JSONArray array1=response.getJSONArray("accounts");
                    JSONArray array2=response.getJSONArray("expenses");
                    JSONArray array3=response.getJSONArray("sales");

                    boolean b1=new SaveData(array1).toActivity7DB();
                    boolean b2=new SaveData(array2).toFragment9DB();
                    boolean b3=new SaveData(array3).toFragment5DB();

                    if (b1 & b2 & b3)
                        startActivity(new Intent(getApplicationContext(), Activity21_UserReport.class));

                    else
                        Toast.makeText(getApplicationContext(), "مجددا تلاش کنید.", Toast.LENGTH_LONG).show();


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
        request.setRetryPolicy(new DefaultRetryPolicy(3000, 1, DefaultRetryPolicy.DEFAULT_MAX_RETRIES));
        AppController.getInstance().addToRequestQueue(request);

    }
}
