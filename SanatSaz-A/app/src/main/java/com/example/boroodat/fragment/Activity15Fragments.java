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
import com.example.boroodat.adapter.Activity15_FragmentsAdapter;
import com.example.boroodat.databinding.Activity5FragmentsBinding;
import com.example.boroodat.general.AppController;
import com.example.boroodat.general.User_Info;
import com.example.boroodat.model.activity15.Activity15_FragmentLoadingModel;
import com.example.boroodat.model.activity15.Activity15_FragmentParentModel;
import com.example.boroodat.model.activity15.Activity15_FragmentsMainModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Activity15Fragments extends Fragment
{
    private int frgNum;
    private int count;
    private Activity5FragmentsBinding binding;
    private int paginate = 50;
    private List<Activity15_FragmentParentModel> models=new ArrayList<>();
    private Activity15_FragmentsAdapter adapter;
    private String from;

    public static Activity15Fragments newInstance(int num, int count, String from )
    {
        Activity15Fragments activity15Fragments = new Activity15Fragments();
        Bundle args = new Bundle ();

        args.putInt ( "num", num );
        args.putInt("count",count);
        args.putString("from", from);
        activity15Fragments.setArguments ( args );
        return activity15Fragments;
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

        getExpenses();
        return view;
    }

    private void getExpenses()
    {

        adapter = new Activity15_FragmentsAdapter(models, getContext(), from );
        binding.recyclerView.setLayoutManager ( new LinearLayoutManager( getContext() ) );
        binding.recyclerView.setAdapter (adapter);

        //---------------------------------------------------------

        String url = getString(R.string.domain) + "api/expense/get-all-expenses?page=" + (frgNum+1);

        models.clear();
        models.add(new Activity15_FragmentLoadingModel());
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

                            JSONObject expense = object1.getJSONObject("expense");

                            String id = expense.getString("id");
                            String factor_number = expense.getString("factor_number");
                            String date = expense.getString("date");
                            String sum = expense.getString("sum");
                            String payment = expense.getString("payment");
                            String account_id = expense.getString("account_id");
                            String account_title = object1.getString("account_title");
                            String description = expense.getString("description");
                            JSONArray expense_details = object1.getJSONArray("expense_details");

                            models.add(new Activity15_FragmentsMainModel(id,factor_number,date,sum,payment,account_id,account_title,description,expense_details));
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
