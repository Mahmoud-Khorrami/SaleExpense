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
import com.example.boroodat.adapter.Activity13_FragmentsAdapter;
import com.example.boroodat.databinding.Activity5FragmentsBinding;
import com.example.boroodat.general.AppController;
import com.example.boroodat.general.User_Info;
import com.example.boroodat.model.activity13.Activity13_FragmentLoadingModel;
import com.example.boroodat.model.activity13.Activity13_FragmentMainModel;
import com.example.boroodat.model.activity13.Activity13_FragmentParentModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Activity13Fragments extends Fragment
{
    private int frgNum;
    private Activity5FragmentsBinding binding;
    private int paginate = 50;
    private List<Activity13_FragmentParentModel> models=new ArrayList<>();
    private Activity13_FragmentsAdapter adapter;

    public static Activity13Fragments newInstance(int num )
    {
        Activity13Fragments activity13Fragments = new Activity13Fragments();
        Bundle args = new Bundle ();

        args.putInt ( "num", num );
        activity13Fragments.setArguments ( args );
        return activity13Fragments;
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

        getSalary();
        return view;
    }

    private void getSalary()
    {

        adapter = new Activity13_FragmentsAdapter(models, getContext() );
        binding.recyclerView.setLayoutManager ( new LinearLayoutManager( getContext() ) );
        binding.recyclerView.setAdapter (adapter);

        //---------------------------------------------------------

        String url = getString(R.string.domain) + "api/salary/get-all-salaries?page=" + (frgNum+1);

        models.clear();
        models.add(new Activity13_FragmentLoadingModel());
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

                            JSONObject salary = object1.getJSONObject("salary");

                            String id=salary.getString("id");
                            String personnel_name=object1.getString("personnel_name");
                            String personnel_id=salary.getString("personnel_id");
                            String salary1=salary.getString("salary");
                            String earnest=salary.getString("earnest");
                            String insurance_tax=salary.getString("insurance_tax");
                            String account_id=salary.getString("account_id");
                            String account_title=object1.getString("account_title");
                            String date=salary.getString("date");
                            String description=salary.getString("description");

                            models.add(new Activity13_FragmentMainModel(id,personnel_name,personnel_id,salary1,earnest,insurance_tax,account_id,account_title,date,description));
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
