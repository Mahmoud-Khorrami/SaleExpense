package com.example.boroodat.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.boroodat.R;
import com.example.boroodat.adapter.Activity12_FragmentsAdapter;
import com.example.boroodat.adapter.Activity3_FragmentsAdapter;
import com.example.boroodat.databinding.Activity5FragmentsBinding;
import com.example.boroodat.general.AppController;
import com.example.boroodat.general.User_Info;
import com.example.boroodat.model.Activity12_FragmentLoadingModel;
import com.example.boroodat.model.Activity12_FragmentMainModel;
import com.example.boroodat.model.Activity3_FragmentLoadingModel;
import com.example.boroodat.model.Activity3_FragmentMainModel;
import com.example.boroodat.model.Activity3_FragmentParentModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Activity3Fragments extends Fragment
{
    private int frgNum;
    private Activity5FragmentsBinding binding;
    private int paginate = 50;
    private List<Activity3_FragmentParentModel> models=new ArrayList<>();
    private Activity3_FragmentsAdapter adapter;

    public static Activity3Fragments newInstance(int num )
    {
        Activity3Fragments activity3Fragments = new Activity3Fragments();
        Bundle args = new Bundle ();

        args.putInt ( "num", num );
        activity3Fragments.setArguments ( args );
        return activity3Fragments;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        if (getArguments () != null)
        {
            frgNum = getArguments ().getInt ( "num" );
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {

        binding = Activity5FragmentsBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        getDeposits();
        return view;
    }

    private void getDeposits()
    {

        adapter = new Activity3_FragmentsAdapter(models, getContext() );
        binding.recyclerView.setLayoutManager ( new LinearLayoutManager( getContext() ) );
        binding.recyclerView.setAdapter (adapter);

        //---------------------------------------------------------

        String url = getString(R.string.domain) + "api/deposit/get-all-deposits?page=" + (frgNum+1);

        models.clear();
        models.add(new Activity3_FragmentLoadingModel());
        adapter.notifyDataSetChanged();

        JSONObject object = new JSONObject();
        try
        {
            object.put("paginate", paginate);
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

                            JSONObject deposit = object1.getJSONObject("deposit");

                            String id = deposit.getString("id");
                            String title = deposit.getString("title");
                            String amount = deposit.getString("amount");
                            String date = deposit.getString("date");
                            String account_id = deposit.getString("account_id");
                            String account_title = object1.getString("account_title");
                            String description = deposit.getString("description");

                            models.add(new Activity3_FragmentMainModel(id,title,amount,date,account_id,account_title,description));
                        }

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
}
