package com.example.boroodat.general;

import android.content.Context;
import android.content.DialogInterface;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.boroodat.R;
import com.example.boroodat.adapter.Activity23_Adapter;
import com.example.boroodat.databinding.A23AddBinding;
import com.example.boroodat.databinding.SellerBinding;
import com.example.boroodat.interfaces.RetryListener;
import com.example.boroodat.model.activity23.Activity23_LoadingModel;
import com.example.boroodat.model.activity23.Activity23_MainModel;
import com.example.boroodat.model.activity23.Activity23_NotFoundModel;
import com.example.boroodat.model.activity23.Activity23_ParentModel;
import com.example.boroodat.model.activity23.Activity23_RetryModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Seller
{
    private Context context;
    private String from;
    private TextView txt_seller_name, txt_seller_id;


    private Activity23_Adapter adapter;
    private List<Activity23_ParentModel> models = new ArrayList<>();
    private AlertDialog.Builder alertDialogBuilder1 = null;

    public Seller(Context context, String from, TextView txt_seller_name, TextView txt_seller_id)
    {
        this.context = context;
        this.from = from;
        this.txt_seller_name = txt_seller_name;
        this.txt_seller_id = txt_seller_id;
    }

    public void show()
    {
        final SellerBinding binding = SellerBinding.inflate(LayoutInflater.from(context));
        View view = binding.getRoot();
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setView(view);

        //----------------------------------------------------------------------------------------------------------

        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setPositiveButton("ایجاد فروشنده جدید", null);
        alertDialogBuilder.setNeutralButton("لغو", null);
        final AlertDialog alertDialog = alertDialogBuilder.create();

        //----------------------------------------------------------------------------------------------------------

        binding.recyclerView.setLayoutManager ( new LinearLayoutManager( context ) );
        adapter = new Activity23_Adapter(models, context,2, txt_seller_name, txt_seller_id,alertDialog,from);
        binding.recyclerView.setAdapter (adapter);
        getSellers();

        //----------------------------------------------------------------------------------------------------------

        adapter.setRetryListener(new RetryListener()
        {
            @Override
            public void retry1()
            {
                getSellers();
            }
        });

        //-------------------------------------------------------------------------------------------------------

        ArrayList<String> searchItem=new ArrayList<>();
        searchItem.add("نام فروشنده");
        searchItem.add("نام فروشگاه");
        searchItem.add("شماره همراه");
        searchItem.add("آدرس");

        final ArrayAdapter<String> adp = new ArrayAdapter<String>(context, R.layout.spinner_item, searchItem);
        binding.spinner.setAdapter(adp);
        binding.spinner.setSelection(0);

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


                if (binding.spinner.getSelectedItem().toString().equals("نام فروشنده"))
                    searchQuery("seller_name", newText);

                else if (binding.spinner.getSelectedItem().toString().equals("نام فروشگاه"))
                    searchQuery("shop_name", newText);

                else if (binding.spinner.getSelectedItem().toString().equals("شماره همراه"))
                    searchQuery("phone_number", newText);

                else if (binding.spinner.getSelectedItem().toString().equals("آدرس"))
                    searchQuery("address", newText);

                return true;
            }
        });

        //----------------------------------------------------------------------------------------------------------

        binding.searchView.setOnCloseListener(new androidx.appcompat.widget.SearchView.OnCloseListener()
        {
            @Override
            public boolean onClose()
            {
                getSellers();
                return true;
            }
        });

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
                        if (alertDialogBuilder1 == null)
                            dialog();
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

        //----------------------------------------------------------------------------------------------------------

        alertDialog.getWindow().setBackgroundDrawable(context.getResources().getDrawable(R.drawable.bkg127));
        alertDialog.show();
        DisplayMetrics display = context.getResources().getDisplayMetrics();
        int width = display.widthPixels;
        width = (int) ((width) * ((double) 4 / 5));
        alertDialog.getWindow().setLayout(width, LinearLayout.LayoutParams.WRAP_CONTENT);
    }

    private void dialog()
    {
        final A23AddBinding binding1 = A23AddBinding.inflate(LayoutInflater.from(context));
        View view = binding1.getRoot();
        alertDialogBuilder1 = new AlertDialog.Builder(context);
        alertDialogBuilder1.setView(view);

        //----------------------------------------------------------------------------------------------------------

        alertDialogBuilder1.setCancelable(false);
        alertDialogBuilder1.setPositiveButton("تایید", null);
        alertDialogBuilder1.setNeutralButton("لغو", null);
        final AlertDialog alertDialog = alertDialogBuilder1.create();

        //----------------------------------------------------------------------------------------------------------

        binding1.sellerName.addTextChangedListener(new ClearError(binding1.til1));

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
                        if (binding1.sellerName.getText().toString().equals(""))
                            binding1.til1.setError("نام فروشنده را وارد کنید.");

                        else
                        {
                            String shop_name1="-";
                            String phone_number1="-";
                            String address1="-";

                            if (!binding1.shopName.getText().toString().equals(""))
                                shop_name1= binding1.shopName.getText().toString();

                            if (!binding1.phoneNumber.getText().toString().equals(""))
                                phone_number1=binding1.phoneNumber.getText().toString();

                            if (!binding1.address.getText().toString().equals(""))
                                address1=binding1.address.getText().toString();

                            if (new Internet(context).check())
                            {
                                createSeller(binding1.sellerName.getText().toString(),shop_name1,phone_number1,address1,alertDialog);
                            }
                            else
                                new Internet(context).enable();

                        }
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
                        alertDialogBuilder1 = null;
                    }
                });
            }
        });

        //------------------------------------------------------------------------------------------------------

        alertDialog.getWindow().setBackgroundDrawable(context.getResources().getDrawable(R.drawable.bkg127));
        alertDialog.show();
        DisplayMetrics display = context.getResources().getDisplayMetrics();
        int width = display.widthPixels;
        width = (int) ((width) * ((double) 4 / 5));
        alertDialog.getWindow().setLayout(width, LinearLayout.LayoutParams.WRAP_CONTENT);
    }

    private void createSeller(final String seller_name, final String shop_name, final String phone_number, final String address, final AlertDialog alertDialog)
    {
        String url = context.getString(R.string.domain) + "api/seller/create";

        JSONObject object = new JSONObject();
        try
        {
            object.put("company_id", new User_Info().company_id());
            object.put("seller_name", txt_seller_name);
            object.put("shop_name", shop_name);
            object.put("phone_number",phone_number);
            object.put("address", address);
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
                    if (code.equals("200"))
                    {
                        String id = response.getString("id");

                        if (models.get(0).getCurrentType() == Activity23_ParentModel.NotFound)
                            models.remove(0);

                        models.add(new Activity23_MainModel(id,seller_name,shop_name,phone_number,address,null));
                        Toast.makeText(context, "ایجاد خریدار جدید با موفقیت انجام شد.", Toast.LENGTH_SHORT).show();
                        alertDialog.dismiss();
                        alertDialogBuilder1 = null;
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

                Toast.makeText(context, "مجددا تلاش کنید.", Toast.LENGTH_LONG).show();

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

    private void getSellers()
    {
        String url = context.getString(R.string.domain) + "api/seller/get-sellers";

        models.clear();
        models.add(new Activity23_LoadingModel());
        adapter.notifyDataSetChanged();

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
                        JSONArray result = response.getJSONArray("result");

                        for (int i=result.length()-1; i>=0; i--)
                        {
                            JSONObject object1 = result.getJSONObject(i);


                            models.add(new Activity23_MainModel(object1.getString("id"),object1.getString("seller_name"),object1.getString("shop_name"),object1.getString("phone_number"),object1.getString("address"),object1.getString("archive")));
                        }

                        adapter.notifyDataSetChanged();
                    }

                    else if (code.equals("207"))
                    {
                        models.add(new Activity23_NotFoundModel());
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
                models.add(new Activity23_RetryModel());
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
        String url = context.getString(R.string.domain) + "api/seller/search-query";

        models.clear();
        models.add(new Activity23_LoadingModel());
        adapter.notifyDataSetChanged();

        JSONObject object = new JSONObject();
        try
        {
            object.put("type",type);
            object.put("value",value);
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

                        if (result.length()>0)
                        {
                            for (int i=result.length()-1; i>=0; i--)
                            {
                                JSONObject object1 = result.getJSONObject(i);

                                models.add(new Activity23_MainModel(object1.getString("id"),object1.getString("seller_name"),object1.getString("shop_name"),object1.getString("phone_number"),object1.getString("address"),object1.getString("archive")));
                            }

                            adapter.notifyDataSetChanged();
                        }
                        else
                        {
                            models.clear();
                            models.add(new Activity23_NotFoundModel());
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
                models.add(new Activity23_RetryModel());
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
