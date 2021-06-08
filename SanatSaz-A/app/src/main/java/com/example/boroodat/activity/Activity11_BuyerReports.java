package com.example.boroodat.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.boroodat.R;
import com.example.boroodat.adapter.Activity11_Adapter;
import com.example.boroodat.adapter.Activity8_Adapter;
import com.example.boroodat.databinding.Activity11BuyerReportsBinding;
import com.example.boroodat.general.AppController;
import com.example.boroodat.general.User_Info;
import com.example.boroodat.interfaces.RetryListener;
import com.example.boroodat.model.activity11.Activity11_LoadingModel;
import com.example.boroodat.model.activity11.Activity11_MainModel;
import com.example.boroodat.model.activity11.Activity11_NotFoundModel;
import com.example.boroodat.model.activity11.Activity11_ParentModel;
import com.example.boroodat.model.activity11.Activity11_RetryModel;
import com.example.boroodat.model.activity8.Activity8_LoadingModel;
import com.example.boroodat.model.activity8.Activity8_MainModel;
import com.example.boroodat.model.activity8.Activity8_NotFoundModel;
import com.example.boroodat.model.activity8.Activity8_ParentModel;
import com.example.boroodat.model.activity8.Activity8_RetryModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Activity11_BuyerReports extends AppCompatActivity
{

    private Activity11BuyerReportsBinding binding;
    private List<Activity11_ParentModel> models =new ArrayList<>(  );
    private Activity11_Adapter adapter;
    private Context context=this;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        binding = Activity11BuyerReportsBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        //-----------------------------------------------------------------------

        binding.recyclerView.setLayoutManager ( new LinearLayoutManager( Activity11_BuyerReports.this ) );
        adapter = new Activity11_Adapter(models, Activity11_BuyerReports.this);
        binding.recyclerView.setAdapter (adapter);
        getBuyer();

        //-----------------------------------------------------------------------

        adapter.setRetryListener(new RetryListener()
        {
            @Override
            public void retry1()
            {
                getBuyer();
            }
        });

        //-----------------------------------------------------------------------

        binding.searchView.setOnQueryTextListener(new androidx.appcompat.widget.SearchView.OnQueryTextListener()
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

                searchQuery(newText);

                return true;
            }
        });
    }

    public void getBuyer()
    {
        String url = getString(R.string.domain) + "api/buyer/get-buyers";

        models.clear();
        models.add(new Activity11_LoadingModel());
        adapter.notifyDataSetChanged();

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
                try
                {
                    String code = response.getString("code");

                    models.clear();

                    if (code.equals("200"))
                    {
                        JSONArray result = response.getJSONArray("result");

                        for (int i=result.length()-1; i>=0; i--)
                        {
                            JSONObject object1 = result.getJSONObject(i);

                            models.add(new Activity11_MainModel(object1.getString("id"),object1.getString("name"),object1.getString("phone_number"),object1.getString("destination"),object1.getString("archive")));
                        }

                        adapter.notifyDataSetChanged();
                    }

                    else if (code.equals("207"))
                    {
                        models.add(new Activity11_NotFoundModel());
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
                models.add(new Activity11_RetryModel());
                adapter.notifyDataSetChanged();

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

    public void searchQuery(String value)
    {
        String url = getString(R.string.domain) + "api/buyer/search-query";

        models.clear();
        models.add(new Activity11_LoadingModel());
        adapter.notifyDataSetChanged();

        JSONObject object = new JSONObject();
        try
        {
            object.put("type","name");
            object.put("value",value);
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

                        if (result.length()>0)
                        {
                            for (int i=result.length()-1; i>=0; i--)
                            {
                                JSONObject object1 = result.getJSONObject(i);

                                models.add(new Activity11_MainModel(object1.getString("id"), object1.getString("name"), object1.getString("phone_number"), object1.getString("destination"), object1.getString("archive")));
                            }

                            adapter.notifyDataSetChanged();
                        }
                        else
                        {
                            models.clear();
                            models.add(new Activity11_NotFoundModel());
                            adapter.notifyDataSetChanged();
                        }
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
                models.add(new Activity11_RetryModel());
                adapter.notifyDataSetChanged();

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
