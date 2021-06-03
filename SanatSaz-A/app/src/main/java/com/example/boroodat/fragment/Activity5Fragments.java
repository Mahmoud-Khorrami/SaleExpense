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
import com.example.boroodat.adapter.Activity5_FragmentsAdapter;
import com.example.boroodat.databinding.Activity5FragmentsBinding;
import com.example.boroodat.general.AppController;
import com.example.boroodat.general.User_Info;
import com.example.boroodat.model.Activity5_FragmentLoadingModel;
import com.example.boroodat.model.Activity5_FragmentParentModel;
import com.example.boroodat.model.Activity5_FragmentsMainModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.realm.Realm;
import io.realm.RealmResults;

public class Activity5Fragments extends Fragment
{
    private int frgNum;
    private int count;
    private Activity5FragmentsBinding binding;
    private int paginate = 50;
    private List<Activity5_FragmentParentModel> models=new ArrayList<>();
    private Activity5_FragmentsAdapter adapter;
    private String from;

    public static Activity5Fragments newInstance(int num, int count, String from )
    {
        Activity5Fragments activity5Fragments = new Activity5Fragments();
        Bundle args = new Bundle ();

        args.putInt ( "num", num );
        args.putInt("count",count);
        args.putString("from", from);
        activity5Fragments.setArguments ( args );
        return activity5Fragments;
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

        //-------------------------------------------------------------

        getSales();
        return view;
    }

    private void getSales()
    {

        adapter = new Activity5_FragmentsAdapter(models, getContext(), from );
        binding.recyclerView.setLayoutManager ( new LinearLayoutManager( getContext() ) );
        binding.recyclerView.setAdapter (adapter);

        //---------------------------------------------------------

        String url = getString(R.string.domain) + "api/sale/get-all-sales?page=" + (frgNum+1);

        models.clear();
        models.add(new Activity5_FragmentLoadingModel());
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

                            String detail = "";

                            JSONArray sale_details = object1.getJSONArray("sale_details");

                            for (int j=0; j<sale_details.length(); j++)
                            {
                                JSONObject object2 = sale_details.getJSONObject(j);
                                detail = detail + object2.getString("description") + " " + object2.getString("number") + " عدد، ";
                            }

                            models.add(new Activity5_FragmentsMainModel(id,factor_number,date,sum,payment,account_id,account_title,buyer_id,buyer_name,driver_id,driver_name,description,detail,sale_details));
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
                headers.put("Authorization", "Bearer "+  new User_Info().token());
                return headers;
            }
        };
        request.setRetryPolicy(new DefaultRetryPolicy(10000, 0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES));
        AppController.getInstance().addToRequestQueue(request);

    }
}
