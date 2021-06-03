package com.example.boroodat.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.boroodat.R;
import com.example.boroodat.adapter.Activity14_Adapter;
import com.example.boroodat.databinding.A14AddBinding;
import com.example.boroodat.databinding.Activity14PersonnelBinding;
import com.example.boroodat.general.AppController;
import com.example.boroodat.general.ClearError;
import com.example.boroodat.general.Date;
import com.example.boroodat.general.Internet;
import com.example.boroodat.general.User_Info;
import com.example.boroodat.model.Activity14_LoadingModel;
import com.example.boroodat.model.Activity14_MainModel;
import com.example.boroodat.model.Activity14_NotFoundModel;
import com.example.boroodat.model.Activity14_ParentModel;
import com.example.boroodat.model.Activity14_RetryModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dmax.dialog.SpotsDialog;
import io.realm.Realm;
import io.realm.RealmResults;

public class Activity14_Personnel extends AppCompatActivity
{
    Activity14PersonnelBinding binding;
    private List<Activity14_ParentModel> models =new ArrayList<>(  );
    private Activity14_Adapter adapter;
    private Context context=this;
    private AlertDialog.Builder alertDialogBuilder=null;
    private android.app.AlertDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        binding = Activity14PersonnelBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        //----------------------------------------------------------------------------------------------------------

        progressDialog = new SpotsDialog(this, R.style.Custom);
        progressDialog.setCancelable(false);

        //----------------------------------------------------------------------------------------------------------

        binding.recyclerView.setLayoutManager ( new LinearLayoutManager( Activity14_Personnel.this ) );
        adapter = new Activity14_Adapter(models, Activity14_Personnel.this,1 );
        binding.recyclerView.setAdapter (adapter);
        getPersonnel();

        //----------------------------------------------------------------------------------------------------------

        binding.fab.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if (new Internet(context).check())
                {
                    if (alertDialogBuilder == null)
                        dialog();
                }
                else
                    new Internet(context).enable();
            }
        });

        //----------------------------------------------------------------------------------------------------------

        ArrayList<String> searchItem=new ArrayList<>();
        searchItem.add("نام و نام خانوادگی");
        searchItem.add("شماره همراه");

        ArrayAdapter<String> adp = new ArrayAdapter<String>(context, R.layout.spinner_item, searchItem);
        binding.spinner.setAdapter(adp);
        binding.spinner.setSelection(0);

        //-------------------------------------------------------------------------------------------------------

        binding.lnr2.setVisibility(View.GONE);

        binding.search.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                binding.lnr1.setVisibility(View.GONE);
                binding.lnr2.setVisibility(View.VISIBLE);
            }
        });

        //----------------------------------------------------------------------------------------------------------

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

                if (binding.spinner.getSelectedItem().toString().equals("نام و نام خانوادگی"))
                    searchQuery("name", newText);

                else if (binding.spinner.getSelectedItem().toString().equals("شماره همراه"))
                    searchQuery("phone_number", newText);

                return true;
            }
        });

        //----------------------------------------------------------------------------------------------------------


        binding.searchView.setOnCloseListener(new SearchView.OnCloseListener()
        {
            @Override
            public boolean onClose()
            {
                binding.lnr1.setVisibility(View.VISIBLE);
                binding.lnr2.setVisibility(View.GONE);
                getPersonnel();
                return true;
            }
        });
    }

    private void dialog()
    {
        final A14AddBinding binding1 = A14AddBinding.inflate(LayoutInflater.from(context));
        View view = binding1.getRoot();
        alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setView(view);

        //----------------------------------------------------------------------------------------------------------

        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setPositiveButton("تایید", null);
        alertDialogBuilder.setNeutralButton("لغو", null);
        final AlertDialog alertDialog = alertDialogBuilder.create();

        //----------------------------------------------------------------------------------------------------------

        binding1.registerDate.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                new Date(binding1.registerDate,context).setDate();
            }
        });

        //----------------------------------------------------------------------------------------------------------

        binding1.name.addTextChangedListener(new ClearError(binding1.til1));
        binding1.phoneNumber.addTextChangedListener(new ClearError(binding1.til2));
        binding1.registerDate.addTextChangedListener(new ClearError(binding1.til3));

        //----------------------------------------------------------------------------------------------------------

        alertDialog.setOnShowListener(new DialogInterface.OnShowListener()
        {
            @Override
            public void onShow(DialogInterface dialogInterface)
            {
                Button add = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
                add.setTextColor(getResources().getColor(R.color.black));
                add.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        if (binding1.name.getText().toString().equals(""))
                        {
                            binding1.scrollView.scrollTo(0,binding1.til1.getTop());
                            binding1.til1.setError("نام و نام خانوادگی را وارد کنید.");
                        }

                        else if (binding1.phoneNumber.getText().toString().equals(""))
                        {
                            binding1.scrollView.scrollTo(0,binding1.til2.getTop());
                            binding1.til2.setError("شماره همراه را وارد کنید.");
                        }

                        else if (binding1.registerDate.getText().toString().equals(""))
                        {
                            binding1.scrollView.scrollTo(0, binding1.til3.getTop());
                            binding1.til3.setError("تاریخ عضویت را وارد کنید.");
                        }

                        else
                        {
                            String role1="-";
                            String credit_card1="-";

                            if (!binding1.role.getText().toString().equals(""))
                                role1=binding1.role.getText().toString();

                            if (!binding1.creditCard.getText().toString().equals(""))
                                credit_card1=binding1.creditCard.getText().toString();

                            if (new Internet(context).check())
                            {
                                create(binding1.name.getText().toString(),binding1.phoneNumber.getText().toString(),binding1.registerDate.getText().toString(),role1,credit_card1,alertDialog);
                            }
                            else
                                new Internet(context).enable();

                        }
                    }
                });


                Button cancel = alertDialog.getButton(AlertDialog.BUTTON_NEUTRAL);
                cancel.setTextColor(getResources().getColor(R.color.black));
                cancel.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        alertDialog.dismiss();
                        alertDialogBuilder = null;
                    }
                });
            }
        });

        //---------------------------------------------------------------------------

        alertDialog.getWindow().setBackgroundDrawable(context.getResources().getDrawable(R.drawable.bkg127));
        alertDialog.show();
        DisplayMetrics display = context.getResources().getDisplayMetrics();
        int width = display.widthPixels;
        width = (int) ((width) * ((double) 4 / 5));
        alertDialog.getWindow().setLayout(width, LinearLayout.LayoutParams.WRAP_CONTENT);
    }

    public void create(final String name, final String phone_number, final String register_date, final String role, final String credit_card, final AlertDialog alertDialog)
    {
        String url = getString(R.string.domain) + "api/personnel/create";
        progressDialog.show();

        JSONObject object = new JSONObject();
        try
        {
            object.put("company_id", new User_Info().company_id());
            object.put("name", name);
            object.put("phone_number",phone_number);
            object.put("register_date",register_date);
            object.put("role", role);
            object.put("credit_card",credit_card);
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
                        JSONObject message = response.getJSONObject("message");
                        String id = message.getString("id");

                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), "ایجاد پرسنل جدید با موفقیت انجام شد.", Toast.LENGTH_SHORT).show();
                        alertDialog.dismiss();
                        alertDialogBuilder = null;

                        models.add(new Activity14_MainModel(id,name,phone_number,register_date,role,credit_card,""));
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
        request.setRetryPolicy(new DefaultRetryPolicy(10000, 0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES));
        AppController.getInstance().addToRequestQueue(request);

    }

    public void getPersonnel()
    {
        models.clear();
        models.add(new Activity14_LoadingModel());
        adapter.notifyDataSetChanged();

        String url = getString(R.string.domain) + "api/personnel/personnel-query1";

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
                        JSONObject message = response.getJSONObject("message");
                        JSONArray result = message.getJSONArray("result");

                        for (int i=0; i<result.length(); i++)
                        {
                            JSONObject object1 = result.getJSONObject(i);

                            models.add(new Activity14_MainModel(object1.getString("id"),object1.getString("name"),object1.getString("phone_number"),object1.getString("register_date"),object1.getString("role"),object1.getString("credit_card"),object1.getString("exit_date")));
                        }

                        adapter.notifyDataSetChanged();
                    }

                    else if (code.equals("207"))
                    {
                        models.add(new Activity14_NotFoundModel());
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
                models.add(new Activity14_RetryModel());
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

    public void searchQuery(String type, String value)
    {
        String url = getString(R.string.domain) + "api/personnel/search-query";

        models.clear();
        models.add(new Activity14_LoadingModel());
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

                    models.clear();

                    if (code.equals("200"))
                    {
                        JSONObject message = response.getJSONObject("message");
                        JSONArray result = message.getJSONArray("result");

                        if (result.length()>0)
                        {
                            for (int i = 0; i < result.length(); i++)
                            {
                                JSONObject object1 = result.getJSONObject(i);

                                models.add(new Activity14_MainModel(object1.getString("id"),object1.getString("name"),object1.getString("phone_number"),object1.getString("register_date"),object1.getString("role"),object1.getString("credit_card"),object1.getString("exit_date")));
                            }

                            adapter.notifyDataSetChanged();
                        }
                        else
                        {
                            models.clear();
                            models.add(new Activity14_NotFoundModel());
                            adapter.notifyDataSetChanged();
                        }
                    }
                } catch (JSONException e)
                {
                    e.printStackTrace();
                    Toast.makeText(context,"ok2",Toast.LENGTH_SHORT).show();
                }

            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {

                models.clear();
                models.add(new Activity14_RetryModel());
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
