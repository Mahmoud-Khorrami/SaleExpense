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
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.boroodat.R;
import com.example.boroodat.adapter.Activity22_Adapter;
import com.example.boroodat.database.DefaultItems_DB;
import com.example.boroodat.databinding.A22DialogBinding;
import com.example.boroodat.databinding.A8AddBinding;
import com.example.boroodat.databinding.Activity22DefaultSettingBinding;
import com.example.boroodat.general.AppController;
import com.example.boroodat.general.ClearError;
import com.example.boroodat.general.Internet;
import com.example.boroodat.general.User_Info;
import com.example.boroodat.model.activity22.Activity22_LoadingModel;
import com.example.boroodat.model.activity22.Activity22_MainModel;
import com.example.boroodat.model.activity22.Activity22_NotFoundModel;
import com.example.boroodat.model.activity22.Activity22_ParentModel;
import com.example.boroodat.model.activity22.Activity22_RetryModel;
import com.example.boroodat.model.activity23.Activity23_LoadingModel;
import com.example.boroodat.model.activity23.Activity23_MainModel;
import com.example.boroodat.model.activity23.Activity23_NotFoundModel;
import com.example.boroodat.model.activity23.Activity23_RetryModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.realm.Realm;
import io.realm.RealmResults;

public class Activity22_DefaultSetting extends AppCompatActivity
{
    private Activity22DefaultSettingBinding binding;
    private AlertDialog.Builder alertDialogBuilder;
    private Context context=this;
    private List<Activity22_ParentModel> models = new ArrayList<>();
    private Activity22_Adapter adapter;
    private Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        binding = Activity22DefaultSettingBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        realm=Realm.getDefaultInstance();
        getData();
        //-------------------------------------------------

        binding.account.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                dialog("account",binding.account);
            }
        });

        //-------------------------------------------------

        binding.buyer.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                dialog("buyer",binding.buyer);
            }
        });

        //-------------------------------------------------

        binding.driver.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                dialog("driver",binding.driver);
            }
        });

        //-------------------------------------------------

        binding.seller.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                dialog("seller",binding.seller);
            }
        });
    }

    private void dialog(final String type, final TextView default_name)
    {
        final A22DialogBinding binding1 = A22DialogBinding.inflate(LayoutInflater.from(context));
        View view = binding1.getRoot();
        alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setView(view);

        //---------------------------------------------------------------

        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setPositiveButton("حذف", null);
        alertDialogBuilder.setNeutralButton("لغو", null);
        final AlertDialog alertDialog = alertDialogBuilder.create();

        //---------------------------------------------------------------

        binding1.searchView.setVisibility(View.GONE);
        binding1.search.setVisibility(View.VISIBLE);

        if (type.equals("account"))
            binding1.search.setVisibility(View.GONE);

        binding1.search.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                binding1.searchView.setVisibility(View.VISIBLE);
            }
        });

        binding1.searchView.setOnCloseListener(new SearchView.OnCloseListener()
        {
            @Override
            public boolean onClose()
            {
                binding1.searchView.setVisibility(View.GONE);

                if (type.equals("account"))
                    getAccount();
                if (type.equals("buyer"))
                    getBuyer();
                if (type.equals("driver"))
                    getDrivers();
                if (type.equals("seller"))
                    getSeller();

                return true;
            }
        });

        //---------------------------------------------------------------

        if (type.equals("account"))
            binding1.title.setText("انتخاب حساب بانکی پیش فرض");
        if (type.equals("buyer"))
            binding1.title.setText("انتخاب خریدار پیش فرض");
        if (type.equals("driver"))
            binding1.title.setText("انتخاب راننده پیش فرض");
        if (type.equals("seller"))
            binding1.title.setText("انتخاب فروشنده پیش فرض");

        //---------------------------------------------------------------

        adapter = new Activity22_Adapter(models,context,type,alertDialog, default_name);
        binding1.recyclerView.setLayoutManager ( new LinearLayoutManager( Activity22_DefaultSetting.this ) );
        binding1.recyclerView.setAdapter (adapter);

        if (type.equals("account"))
            getAccount();
        if (type.equals("buyer"))
            getBuyer();
        if (type.equals("driver"))
            getDrivers();
        if (type.equals("seller"))
            getSeller();

        //---------------------------------------------------------------

        binding1.searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener()
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

                List<Activity22_MainModel> models1 = new ArrayList<>();

                for (int i=0; i<models.size(); i++)
                {
                    if (models.get(i).getCurrentType() == Activity22_ParentModel.Main)
                    {
                        Activity22_MainModel model = (Activity22_MainModel) models.get(i);

                        if (model.getName().contains(newText))
                            models1.add(model);
                    }
                }

                models.clear();
                models.addAll(models1);
                adapter.notifyDataSetChanged();

                return true;
            }
        });

        //---------------------------------------------------------------

        alertDialog.setOnShowListener(new DialogInterface.OnShowListener()
        {
            @Override
            public void onShow(DialogInterface dialogInterface)
            {
                Button delete = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
                delete.setTextColor(context.getResources().getColor(R.color.black));
                delete.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        realm.executeTransaction(new Realm.Transaction()
                        {
                            @Override
                            public void execute(Realm realm)
                            {

                                RealmResults<DefaultItems_DB> results = realm.where(DefaultItems_DB.class)
                                        .equalTo("id",new User_Info().company_id())
                                        .findAll();

                                if (results.size()>0)
                                {
                                    if (type.equals("account"))
                                    {
                                        results.get(0).setAccount_title("-");
                                        results.get(0).setAccount_id("-");
                                    }


                                    if (type.equals("buyer"))
                                    {
                                        results.get(0).setBuyer_name("-");
                                        results.get(0).setBuyer_id("-");
                                    }

                                    if (type.equals("driver"))
                                    {
                                        results.get(0).setDriver_name("-");
                                        results.get(0).setDriver_id("-");
                                    }


                                    if (type.equals("seller"))
                                    {
                                        results.get(0).setSeller_name("-");
                                        results.get(0).setSeller_id("-");
                                    }

                                    default_name.setText("-");
                                    alertDialog.dismiss();
                                }
                            }
                        });

                    }
                });


                Button cancel = alertDialog.getButton(AlertDialog.BUTTON_NEUTRAL);
                cancel.setTextColor(context.getResources().getColor(R.color.black));
                cancel.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        alertDialog.dismiss();
                    }
                });
            }
        });

        //---------------------------------------------------------------

        alertDialog.getWindow().setBackgroundDrawable(context.getResources().getDrawable(R.drawable.bkg127));
        alertDialog.show();
        DisplayMetrics display = context.getResources().getDisplayMetrics();
        int width = display.widthPixels;
        width = (int) ((width) * ((double) 4 / 5));
        alertDialog.getWindow().setLayout(width, LinearLayout.LayoutParams.WRAP_CONTENT);
    }

    private void getData()
    {
        RealmResults<DefaultItems_DB> results = realm.where(DefaultItems_DB.class)
                .equalTo("id",new User_Info().company_id())
                .findAll();

        if (results.size()>0)
        {
            binding.account.setText(results.get(0).getAccount_title());
            binding.buyer.setText(results.get(0).getBuyer_name());
            binding.driver.setText(results.get(0).getDriver_name());
            binding.seller.setText(results.get(0).getSeller_name());
        }

        else
        {
            realm.beginTransaction();
            realm.copyToRealmOrUpdate(new DefaultItems_DB(new User_Info().company_id(),"-","-","-","-","-","-","-","-"));
            realm.commitTransaction();
        }

    }

    private void getAccount()
    {
        models.clear();
        models.add(new Activity22_LoadingModel());
        adapter.notifyDataSetChanged();

        String url = context.getString(R.string.domain) + "api/account/account-query1";

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

                    models.clear();

                    if (code.equals("200"))
                    {
                        JSONObject message = response.getJSONObject("message");
                        JSONArray result = message.getJSONArray("result");

                        for (int i=0; i<result.length(); i++)
                        {
                            JSONObject object1 = result.getJSONObject(i);

                            models.add(new Activity22_MainModel(object1.getString("id"),object1.getString("title")));
                        }

                        adapter.notifyDataSetChanged();
                    }

                    else if (code.equals("207"))
                    {
                        models.add(new Activity22_NotFoundModel());
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
                models.add(new Activity22_RetryModel());
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

    private void getBuyer()
    {
        String url = context.getString(R.string.domain) + "api/buyer/get-buyers";

        models.clear();
        models.add(new Activity22_LoadingModel());
        adapter.notifyDataSetChanged();

        JSONObject object = new JSONObject();
        try
        {
            object.put("paginate",50);
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

                    models.clear();

                    if (code.equals("200"))
                    {
                        JSONArray result = response.getJSONArray("result");

                        for (int i=result.length()-1; i>=0; i--)
                        {
                            JSONObject object1 = result.getJSONObject(i);

                            models.add(new Activity22_MainModel(object1.getString("id"),object1.getString("name")));
                        }

                        adapter.notifyDataSetChanged();
                    }

                    else if (code.equals("207"))
                    {
                        models.add(new Activity22_NotFoundModel());
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
                models.add(new Activity22_RetryModel());
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

    private void getDrivers()
    {
        String url = context.getString(R.string.domain) + "api/driver/get-drivers";

        models.clear();
        models.add(new Activity22_LoadingModel());
        adapter.notifyDataSetChanged();

        JSONObject object = new JSONObject();
        try
        {
            object.put("paginate",50);
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

                    models.clear();

                    if (code.equals("200"))
                    {
                        JSONArray result = response.getJSONArray("result");

                        for (int i=result.length()-1; i>=0; i--)
                        {
                            JSONObject object1 = result.getJSONObject(i);

                            models.add(new Activity22_MainModel(object1.getString("id"),object1.getString("name")));
                        }

                        adapter.notifyDataSetChanged();
                    }

                    else if (code.equals("207"))
                    {
                        models.add(new Activity22_NotFoundModel());
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
                models.add(new Activity22_RetryModel());
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

    public void getSeller()
    {
        String url = getString(R.string.domain) + "api/seller/get-sellers";

        models.clear();
        models.add(new Activity22_LoadingModel());
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

                            models.add(new Activity22_MainModel(object1.getString("id"),object1.getString("seller_name")));
                        }

                        adapter.notifyDataSetChanged();
                    }

                    else if (code.equals("207"))
                    {
                        models.add(new Activity22_NotFoundModel());
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
                models.add(new Activity22_RetryModel());
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
