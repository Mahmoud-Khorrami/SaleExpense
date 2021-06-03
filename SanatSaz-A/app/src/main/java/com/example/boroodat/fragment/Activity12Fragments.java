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
import com.example.boroodat.databinding.Activity5FragmentsBinding;
import com.example.boroodat.general.AppController;
import com.example.boroodat.general.User_Info;
import com.example.boroodat.model.Activity12_FragmentLoadingModel;
import com.example.boroodat.model.Activity12_FragmentMainModel;
import com.example.boroodat.model.Activity12_FragmentParentModel;
import com.example.boroodat.model.Activity15_FragmentLoadingModel;
import com.example.boroodat.model.Activity15_FragmentsMainModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Activity12Fragments extends Fragment
{
    private int frgNum;
    private int count;
    private Activity5FragmentsBinding binding;
    private int paginate = 50;
    private List<Activity12_FragmentParentModel> models=new ArrayList<>();
    private Activity12_FragmentsAdapter adapter;
    private String from;

    public static Activity12Fragments newInstance(int num, int count, String from )
    {
        Activity12Fragments activity12Fragments = new Activity12Fragments();
        Bundle args = new Bundle ();

        args.putInt ( "num", num );
        args.putInt("count",count);
        args.putString("from", from);
        activity12Fragments.setArguments ( args );
        return activity12Fragments;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        if (getArguments () != null)
        {
            frgNum = getArguments ().getInt ( "num" );
            count = getArguments().getInt("count");
            from = getArguments().getString("from");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {

        binding = Activity5FragmentsBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        getMaterials();
        return view;
    }

    private void getMaterials()
    {

        adapter = new Activity12_FragmentsAdapter(models, getContext(), from );
        binding.recyclerView.setLayoutManager ( new LinearLayoutManager( getContext() ) );
        binding.recyclerView.setAdapter (adapter);

        //---------------------------------------------------------

        String url = getString(R.string.domain) + "api/material/get-all-materials?page=" + (frgNum+1);

        models.clear();
        models.add(new Activity12_FragmentLoadingModel());
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

                            JSONObject material = object1.getJSONObject("material");

                            String id = material.getString("id");
                            String factor_number = material.getString("factor_number");
                            String date = material.getString("date");
                            String sum = material.getString("sum");
                            String payment = material.getString("payment");
                            String account_id = material.getString("account_id");
                            String account_title = object1.getString("account_title");
                            String description = material.getString("description");
                            JSONArray material_details = object1.getJSONArray("material_details");

                            models.add(new Activity12_FragmentMainModel(id,factor_number,date,sum,payment,account_id,account_title,description,material_details));
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
