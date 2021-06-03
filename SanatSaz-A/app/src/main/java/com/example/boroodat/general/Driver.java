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
import com.example.boroodat.adapter.Activity9_Adapter;
import com.example.boroodat.databinding.A9AddBinding;
import com.example.boroodat.databinding.DriverBinding;
import com.example.boroodat.interfaces.OnLoadMoreListener;
import com.example.boroodat.interfaces.RetryListener;
import com.example.boroodat.model.Activity9_LoadingModel;
import com.example.boroodat.model.Activity9_MainModel;
import com.example.boroodat.model.Activity9_NotFoundModel;
import com.example.boroodat.model.Activity9_ParentModel;
import com.example.boroodat.model.Activity9_RetryModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Driver
{
    private Context context;
    private String from;
    private TextView driverName,driverId;


    private Activity9_Adapter adapter;
    private List<Activity9_ParentModel> models = new ArrayList<>();
    private AlertDialog.Builder alertDialogBuilder1 = null;

    public Driver(Context context, String from, TextView driverName, TextView driverId)
    {
        this.context = context;
        this.from = from;
        this.driverName = driverName;
        this.driverId = driverId;
    }

    public void show()
    {
        final DriverBinding binding = DriverBinding.inflate(LayoutInflater.from(context));
        View view = binding.getRoot();
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setView(view);

        //----------------------------------------------------------------------------------------------------------

        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setPositiveButton("ایجاد راننده جدید", null);
        alertDialogBuilder.setNeutralButton("لغو", null);
        final AlertDialog alertDialog = alertDialogBuilder.create();

        //----------------------------------------------------------------------------------------------------------

        binding.recyclerView.setLayoutManager ( new LinearLayoutManager( context ) );
        adapter = new Activity9_Adapter(models, context,2,driverName,driverId,alertDialog,from);
        binding.recyclerView.setAdapter (adapter);
        getDrivers();

        //----------------------------------------------------------------------------------------------------------

        adapter.setRetryListener(new RetryListener()
        {
            @Override
            public void retry1()
            {
                getDrivers();
            }
        });

        //-------------------------------------------------------------------------------------------------------

        ArrayList<String> searchItem=new ArrayList<>();
        searchItem.add("نام و نام خانوادگی");
        searchItem.add("شماره همراه");
        searchItem.add("نوع خودرو");
        searchItem.add("شماره پلاک");

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

                if (binding.spinner.getSelectedItem().toString().equals("نام و نام خانوادگی"))
                    searchQuery("name", newText);

                else if (binding.spinner.getSelectedItem().toString().equals("شماره همراه"))
                    searchQuery("phone_number", newText);

                else if (binding.spinner.getSelectedItem().toString().equals("نوع خودرو"))
                    searchQuery("car_type", newText);

                else if (binding.spinner.getSelectedItem().toString().equals("شماره پلاک"))
                    searchQuery("number_plate", newText);

                return true;
            }
        });

        //----------------------------------------------------------------------------------------------------------

        binding.searchView.setOnCloseListener(new androidx.appcompat.widget.SearchView.OnCloseListener()
        {
            @Override
            public boolean onClose()
            {
                getDrivers();
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
        final A9AddBinding binding1 = A9AddBinding.inflate(LayoutInflater.from(context));
        View view = binding1.getRoot();
        alertDialogBuilder1 = new AlertDialog.Builder(context);
        alertDialogBuilder1.setView(view);

        //----------------------------------------------------------------------------------------------------------

        alertDialogBuilder1.setCancelable(false);
        alertDialogBuilder1.setPositiveButton("تایید", null);
        alertDialogBuilder1.setNeutralButton("لغو", null);
        final AlertDialog alertDialog = alertDialogBuilder1.create();

        //----------------------------------------------------------------------------------------------------------

        binding1.name.addTextChangedListener(new ClearError(binding1.til1));

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
                        if (binding1.name.getText().toString().equals(""))
                            binding1.til1.setError("نام راننده را وارد کنید.");

                        else
                        {
                            String phone_number1 = "-";
                            String car_type1 = "-";
                            String number_plate1 = "-";

                            if (!binding1.phoneNumber.getText().toString().equals(""))
                                phone_number1 = binding1.phoneNumber.getText().toString();

                            if (!binding1.carType.getText().toString().equals(""))
                                car_type1 = binding1.carType.getText().toString();

                            if (!binding1.numberPlate.getText().toString().equals(""))
                                number_plate1 = binding1.numberPlate.getText().toString();

                            if (new Internet(context).check())
                                createDriver(binding1.name.getText().toString(), phone_number1, car_type1, number_plate1, alertDialog);
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

    private void createDriver(final String name, final String phone_number, final String car_type,final String number_plate, final AlertDialog alertDialog)
    {
        String url = context.getString(R.string.domain) + "api/driver/create";
        //progressDialog.show();

        JSONObject object = new JSONObject();
        try
        {
            object.put("company_id", new User_Info().company_id());
            object.put("name", name);
            object.put("phone_number",phone_number);
            object.put("car_type", car_type);
            object.put("number_plate",number_plate);
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
                        String id = response.getString("result");

                        if (models.get(0).getCurrentType() == Activity9_ParentModel.NotFound)
                            models.remove(0);

                        models.add(new Activity9_MainModel(id,name,phone_number,car_type,number_plate,null));
                        Toast.makeText(context, "ایجاد راننده جدید با موفقیت انجام شد.", Toast.LENGTH_SHORT).show();
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

    private void getDrivers()
    {
        String url = context.getString(R.string.domain) + "api/driver/get-drivers";

        models.clear();
        models.add(new Activity9_LoadingModel());
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

                            models.add(new Activity9_MainModel(object1.getString("id"),object1.getString("name"),object1.getString("phone_number"),object1.getString("car_type"),object1.getString("number_plate"),object1.getString("archive")));
                        }

                        adapter.notifyDataSetChanged();
                    }

                    else if (code.equals("207"))
                    {
                        models.add(new Activity9_NotFoundModel());
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
                models.add(new Activity9_RetryModel());
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
        String url = context.getString(R.string.domain) + "api/driver/search-query";

        models.clear();
        models.add(new Activity9_LoadingModel());
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

                                models.add(new Activity9_MainModel(object1.getString("id"), object1.getString("name"), object1.getString("phone_number"), object1.getString("car_type"), object1.getString("number_plate"), object1.getString("archive")));
                            }

                            adapter.notifyDataSetChanged();
                        }
                        else
                        {
                            models.clear();
                            models.add(new Activity9_NotFoundModel());
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
                models.add(new Activity9_RetryModel());
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
