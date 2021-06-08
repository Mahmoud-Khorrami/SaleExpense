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
import com.example.boroodat.adapter.Activity8_Adapter;
import com.example.boroodat.databinding.A8AddBinding;
import com.example.boroodat.databinding.Activity8BuyerBinding;
import com.example.boroodat.databinding.DeleteDialog2Binding;
import com.example.boroodat.general.AppController;
import com.example.boroodat.general.ClearError;
import com.example.boroodat.general.Internet;
import com.example.boroodat.general.User_Info;
import com.example.boroodat.interfaces.RetryListener;
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

import dmax.dialog.SpotsDialog;

public class Activity8_Buyer extends AppCompatActivity
{
    Activity8BuyerBinding binding;
    private List<Activity8_ParentModel> models =new ArrayList<>(  );
    private Activity8_Adapter adapter;
    private Context context=this;
    private AlertDialog.Builder alertDialogBuilder=null;
    private android.app.AlertDialog progressDialog;
    final JSONArray buyer_ids=new JSONArray();


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        binding = Activity8BuyerBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        //----------------------------------------------------------------------------------------------------------

        binding.toolbar.setTitle("");
        setSupportActionBar(binding.toolbar);

        //----------------------------------------------------------------------------------------------------------

        progressDialog = new SpotsDialog(this, R.style.Custom);
        progressDialog.setCancelable(false);

        //----------------------------------------------------------------------------------------------------------

        binding.recyclerView.setLayoutManager ( new LinearLayoutManager( Activity8_Buyer.this ) );
        adapter = new Activity8_Adapter(models, Activity8_Buyer.this,1,"manager");
        binding.recyclerView.setAdapter (adapter);
        getBuyer();

        //----------------------------------------------------------------------------------------------------------

        adapter.setRetryListener(new RetryListener()
        {
            @Override
            public void retry1()
            {
                getBuyer();
            }
        });

        //-------------------------------------------------------------------------------------------------------

        ArrayList<String> searchItem=new ArrayList<>();
        searchItem.add("نام و نام خانوادگی");
        searchItem.add("شماره همراه");
        searchItem.add("مقصد");

        ArrayAdapter<String> adp = new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_item, searchItem);
        binding.spinner.setAdapter(adp);
        binding.spinner.setSelection(0);

        //-------------------------------------------------------------------------------------------------------

        binding.lnr2.setVisibility(View.GONE);
        binding.lnr3.setVisibility(View.GONE);

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

                else if (binding.spinner.getSelectedItem().toString().equals("مقصد"))
                    searchQuery("destination", newText);

                return true;
            }
        });

        //----------------------------------------------------------------------------------------------------------


        binding.searchView.setOnCloseListener(new SearchView.OnCloseListener()
        {
            @Override
            public boolean onClose()
            {
                binding.toolbar.setVisibility(View.VISIBLE);
                binding.lnr2.setVisibility(View.GONE);
                getBuyer();
                return true;
            }
        });

    }

    public void onClick(View view)
    {
        if (view.getId() == R.id.search)
        {
            binding.toolbar.setVisibility(View.GONE);
            binding.lnr3.setVisibility(View.GONE);
            binding.lnr2.setVisibility(View.VISIBLE);

            adapter.changeStatusS1();
        }

        if (view.getId() == R.id.delete)
        {
            archiveDialog();
        }

        if (view.getId() == R.id.fab)
        {
            if (new Internet(context).check())
            {
                if (alertDialogBuilder == null)
                    dialog();
            }
            else
                new Internet(context).enable();
        }

        if (view.getId() == R.id.closeLnr3)
        {
            binding.toolbar.setVisibility(View.VISIBLE);
            binding.lnr3.setVisibility(View.GONE);
            adapter.changeStatusS1();
        }
    }

    private void dialog()
    {
        final A8AddBinding binding1 = A8AddBinding.inflate(LayoutInflater.from(context));
        View view = binding1.getRoot();
        alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setView(view);

        //----------------------------------------------------------------------------------------------------------

        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setPositiveButton("تایید", null);
        alertDialogBuilder.setNeutralButton("لغو", null);
        final AlertDialog alertDialog = alertDialogBuilder.create();

        //----------------------------------------------------------------------------------------------------------

        binding1.name.addTextChangedListener(new ClearError(binding1.til1));

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
                            binding1.til1.setError("نام خریدار را وارد کنید.");

                        else
                        {

                            String phone_number1="-";
                            String destination1="-";

                            if (!binding1.phoneNumber.getText().toString().equals(""))
                                phone_number1= binding1.phoneNumber.getText().toString();

                            if (!binding1.destination.getText().toString().equals(""))
                                destination1=binding1.destination.getText().toString();

                            if (new Internet(context).check())
                            {
                                create(binding1.name.getText().toString(),phone_number1,destination1,alertDialog);
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

        //------------------------------------------------------------------------------------------

        alertDialog.getWindow().setBackgroundDrawable(context.getResources().getDrawable(R.drawable.bkg127));
        alertDialog.show();
        DisplayMetrics display = context.getResources().getDisplayMetrics();
        int width = display.widthPixels;
        width = (int) ((width) * ((double) 4 / 5));
        alertDialog.getWindow().setLayout(width, LinearLayout.LayoutParams.WRAP_CONTENT);
    }

    public void create(final String name, final String phone_number, final String destination, final AlertDialog alertDialog)
    {
        String url = getString(R.string.domain) + "api/buyer/create";
        progressDialog.show();

        JSONObject object = new JSONObject();
        try
        {
            object.put("company_id", new User_Info().company_id());
            object.put("name", name);
            object.put("phone_number",phone_number);
            object.put("destination", destination);
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
                        String id = response.getString("result");

                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), "ایجاد خریدار جدید با موفقیت انجام شد.", Toast.LENGTH_SHORT).show();
                        alertDialog.dismiss();
                        alertDialogBuilder = null;

                        if (models.get(0).getCurrentType() == Activity8_ParentModel.NotFound)
                            models.remove(0);

                        models.add(new Activity8_MainModel(id,name,phone_number,destination,null));
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

    public void archive(final AlertDialog alertDialog)
    {
        String url = getString(R.string.domain) + "api/buyer/archive";
        progressDialog.show();

        JSONObject object = new JSONObject();
        try
        {
            object.put("buyer_ids", buyer_ids);
            object.put("secret_key", getString(R.string.secret_key));
        } catch (JSONException e)
        {
            e.printStackTrace();
        }

        Response.Listener<JSONObject> listener = new Response.Listener<JSONObject>()
        {
            @Override
            public void onResponse(JSONObject response)
            {

                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(), "آیتم های انتخاب شده از لیست حذف شدند.", Toast.LENGTH_SHORT).show();
                binding.toolbar.setVisibility(View.VISIBLE);
                binding.lnr3.setVisibility(View.GONE);
                alertDialog.dismiss();
                alertDialogBuilder = null;
                getBuyer();
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
                headers.put("Authorization", "Bearer " + new User_Info().token());
                return headers;
            }
        };
        request.setRetryPolicy(new DefaultRetryPolicy(10000, 0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES));
        AppController.getInstance().addToRequestQueue(request);
    }

    private void archiveDialog()
    {
        for (int i=0; i<models.size();i++)
        {
            if (models.get(i).getCurrentType() == Activity8_ParentModel.Main)
            {
                Activity8_MainModel model = (Activity8_MainModel) models.get(i);
                if (model.isSelected())
                    buyer_ids.put(model.getId());
            }
        }

        if (buyer_ids.length()>0)
        {
            final DeleteDialog2Binding binding1 = DeleteDialog2Binding.inflate(LayoutInflater.from(context));
            View view = binding1.getRoot();
            alertDialogBuilder = new AlertDialog.Builder(context);
            alertDialogBuilder.setView(view);

            //----------------------------------------------------------------------------------------------------------

            alertDialogBuilder.setCancelable(false);
            alertDialogBuilder.setPositiveButton("تایید", null);
            alertDialogBuilder.setNeutralButton("لغو", null);
            final AlertDialog alertDialog = alertDialogBuilder.create();

            //----------------------------------------------------------------------------------------------------------

            alertDialog.setOnShowListener(new DialogInterface.OnShowListener()
            {
                @Override
                public void onShow(DialogInterface dialogInterface)
                {
                    Button add = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
                    add.setTextColor(context.getResources().getColor(R.color.black));
                    add.setOnClickListener(new View.OnClickListener()
                    {
                        @Override
                        public void onClick(View v)
                        {
                            if (new Internet(context).check())
                                archive(alertDialog);
                            else
                                new Internet(context).enable();
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
                            alertDialogBuilder = null;
                        }
                    });
                }
            });

            //---------------------------------------------------------------------------------------------------------

            alertDialog.getWindow().setBackgroundDrawable(context.getResources().getDrawable(R.drawable.bkg129));
            alertDialog.show();
            DisplayMetrics display = context.getResources().getDisplayMetrics();
            int width = display.widthPixels;
            width = (int) ((width) * ((double) 4 / 5));
            alertDialog.getWindow().setLayout(width, LinearLayout.LayoutParams.WRAP_CONTENT);
        }

        else
            Toast.makeText(getApplicationContext(), "هیچ آیتمی انتخاب نشده است.", Toast.LENGTH_SHORT).show();
    }

    public void getBuyer()
    {
        String url = getString(R.string.domain) + "api/buyer/get-buyers";

        models.clear();
        models.add(new Activity8_LoadingModel());
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

                            models.add(new Activity8_MainModel(object1.getString("id"),object1.getString("name"),object1.getString("phone_number"),object1.getString("destination"),object1.getString("archive")));
                        }

                        adapter.notifyDataSetChanged();
                    }

                    else if (code.equals("207"))
                    {
                        models.add(new Activity8_NotFoundModel());
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
                models.add(new Activity8_RetryModel());
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
        String url = getString(R.string.domain) + "api/buyer/search-query";

        models.clear();
        models.add(new Activity8_LoadingModel());
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
                        JSONArray result = response.getJSONArray("result");

                        if (result.length()>0)
                        {
                            for (int i=result.length()-1; i>=0; i--)
                            {
                                JSONObject object1 = result.getJSONObject(i);

                                models.add(new Activity8_MainModel(object1.getString("id"), object1.getString("name"), object1.getString("phone_number"), object1.getString("destination"), object1.getString("archive")));
                            }

                            adapter.notifyDataSetChanged();
                        }
                        else
                        {
                            models.clear();
                            models.add(new Activity8_NotFoundModel());
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
                models.add(new Activity8_RetryModel());
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

    public void changeStatusLnr3()
    {
        binding.toolbar.setVisibility(View.GONE);
        binding.lnr2.setVisibility(View.GONE);
        binding.lnr3.setVisibility(View.VISIBLE);
    }

}
