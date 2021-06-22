package com.example.boroodat.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.boroodat.R;
import com.example.boroodat.adapter.Activity15_Adapter;
import com.example.boroodat.adapter.Activity15_FragmentsAdapter;
import com.example.boroodat.databinding.Activity5Binding;
import com.example.boroodat.fragment.Activity15Fragments;
import com.example.boroodat.general.AppController;
import com.example.boroodat.general.User_Info;
import com.example.boroodat.model.activity15.Activity15_FragmentLoadingModel;
import com.example.boroodat.model.activity15.Activity15_FragmentNotFoundModel;
import com.example.boroodat.model.activity15.Activity15_FragmentParentModel;
import com.example.boroodat.model.activity15.Activity15_FragmentsMainModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Activity15_ExpenseReports extends AppCompatActivity
{
    private Activity5Binding binding;
    private Activity15Fragments fragments;
    private Activity15_Adapter adapter;
    private String[] title =new String[1000000];
    private int paginate = 50;
    private Context context=this;
    private String from;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        binding = Activity5Binding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        //-------------------------------------------------------------

        Bundle bundle = getIntent().getExtras();
        from = bundle.getString("from");

        //-------------------------------------------------------------

        ArrayList<String> searchItem=new ArrayList<>();
        searchItem.add("شماره فاکتور");
        searchItem.add("تاریخ");

        ArrayAdapter<String> adp = new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_item, searchItem);
        binding.spinner.setAdapter(adp);
        binding.spinner.setSelection(0);

        //----------------------------------------------------------------------

        binding.recyclerView.setVisibility(View.GONE);
        binding.tabs.setVisibility(View.VISIBLE);
        binding.pager.setVisibility(View.VISIBLE);

        //----------------------------------------------------------------------

        binding.searchview.setOnQueryTextListener(new androidx.appcompat.widget.SearchView.OnQueryTextListener()
        {
            @Override
            public boolean onQueryTextSubmit(String query)
            {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText)
            {

                binding.recyclerView.setVisibility(View.VISIBLE);
                binding.tabs.setVisibility(View.GONE);
                binding.pager.setVisibility(View.GONE);

                newText = newText.toLowerCase();

                if (binding.spinner.getSelectedItem().toString().equals("شماره فاکتور"))
                    searchQuery("factor_number", newText);

                else if (binding.spinner.getSelectedItem().toString().equals("تاریخ"))
                    searchQuery("date", newText);

                return true;
            }
        });

        //----------------------------------------------------------------------

        binding.searchview.setOnCloseListener(new SearchView.OnCloseListener()
        {
            @Override
            public boolean onClose()
            {
                binding.recyclerView.setVisibility(View.GONE);
                binding.tabs.setVisibility(View.VISIBLE);
                binding.pager.setVisibility(View.VISIBLE);
                getExpensesCount();
                return true;
            }
        });
    }

    public void getExpensesCount()
    {

        String url = getString(R.string.domain) + "api/expense/get-expenses-count";

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

                    if (code.equals("200"))
                    {
                        int count = Integer.parseInt(response.getString("count"));

                        int frgNum = (count / paginate);

                        for (int i = 0; i < (count / paginate); i++)
                            title[i] = (i * paginate) + 1 + "-" + ((i * paginate) + paginate);

                        int x = count - ((count / paginate) * paginate);
                        if (x > 0)
                        {
                            frgNum = frgNum+1;
                            title[count / paginate] = (((count / paginate) * paginate) + 1) + "-" + count;
                        }

                        adapter = new Activity15_Adapter(getSupportFragmentManager(), fragments, frgNum, title, from);
                        binding.pager.setOffscreenPageLimit(1);
                        binding.pager.setAdapter(adapter);
                        binding.tabs.setupWithViewPager(binding.pager);
                        binding.pager.setCurrentItem(adapter.getCount());
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

    public void searchQuery(String type, String value)
    {

        final List<Activity15_FragmentParentModel> models=new ArrayList<>();
        final Activity15_FragmentsAdapter adapter2 = new Activity15_FragmentsAdapter(models,context, from );
        binding.recyclerView.setLayoutManager ( new LinearLayoutManager( context ) );
        binding.recyclerView.setAdapter (adapter2);

        //----------------------------------------------------------------

        String url = getString(R.string.domain) + "api/expense/search-query";


        models.clear();
        models.add(new Activity15_FragmentLoadingModel());
        adapter.notifyDataSetChanged();

        JSONObject object = new JSONObject();
        try
        {
            object.put("type",type);
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

                    if (code.equals("200"))
                    {
                        models.clear();

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

                        adapter2.notifyDataSetChanged();
                    }

                    else if (code.equals("207"))
                    {
                        models.clear();
                        models.add(new Activity15_FragmentNotFoundModel());
                        adapter2.notifyDataSetChanged();
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
                adapter2.notifyDataSetChanged();
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

    @Override
    protected void onResume()
    {
        super.onResume();

        getExpensesCount();
    }
}
