package com.example.boroodat.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.boroodat.R;
import com.example.boroodat.adapter.Activity9_Adapter;
import com.example.boroodat.database.Activity9_DB;
import com.example.boroodat.databinding.A9AddBinding;
import com.example.boroodat.databinding.Activity9DriverBinding;
import com.example.boroodat.general.AppController;
import com.example.boroodat.general.ClearError;
import com.example.boroodat.general.Internet;
import com.example.boroodat.general.SaveData;
import com.example.boroodat.general.User_Info;
import com.example.boroodat.model.Activity9_Model;

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

public class Activity9_Driver extends AppCompatActivity
{
    Activity9DriverBinding binding;
    private List<Activity9_Model> models =new ArrayList<>(  );
    private Activity9_Adapter adapter;
    private Context context=this;
    private AlertDialog.Builder alertDialogBuilder=null;
    private android.app.AlertDialog progressDialog;
    private Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        binding = Activity9DriverBinding.inflate(getLayoutInflater());
        final View view = binding.getRoot();
        setContentView(view);

        realm=Realm.getDefaultInstance();
        //----------------------------------------------------------------------------------------------------------

        binding.toolbar.setTitle("");
        setSupportActionBar(binding.toolbar);

        //----------------------------------------------------------------------------------------------------------

        progressDialog = new SpotsDialog(this, R.style.Custom);
        progressDialog.setCancelable(false);

        //----------------------------------------------------------------------------------------------------------

        adapter = new Activity9_Adapter(models, Activity9_Driver.this,1,"manager");
        binding.recyclerView.setLayoutManager ( new LinearLayoutManager( Activity9_Driver.this ) );
        binding.recyclerView.setAdapter (adapter);
        addUnArchiveDriver();

        //----------------------------------------------------------------------------------------------------------

        ArrayList<String> searchItem=new ArrayList<>();
        searchItem.add("نام و نام خانوادگی");
        searchItem.add("شماره همراه");
        searchItem.add("مشخصات خودرو");

        final ArrayAdapter<String> adp = new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_item, searchItem);
        binding.spinner.setAdapter(adp);
        binding.spinner.setSelection(0);

        //-------------------------------------------------------------------------------------------------------

        binding.lnr2.setVisibility(View.GONE);
        binding.lnr3.setVisibility(View.GONE);
        binding.lnr4.setVisibility(View.GONE);
        binding.unArchiveImg.setVisibility(View.GONE);

        //----------------------------------------------------------------------------------------------------------

        binding.searchView.setOnQueryTextListener(new android.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s)
            {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText)
            {
                newText = newText.toLowerCase();
                List<Activity9_Model> newList = new ArrayList<>();

                for (int i=0;i<models.size();i++)
                {
                    if (binding.spinner.getSelectedItem().toString().equals("نام و نام خانوادگی")
                            && models.get(i).getName().toLowerCase().contains(newText))
                        newList.add(models.get(i));

                    else if (binding.spinner.getSelectedItem().toString().equals("شماره همراه")
                            && models.get(i).getPhone_number().toLowerCase().contains(newText))
                        newList.add(models.get(i));

                    else if (binding.spinner.getSelectedItem().toString().equals("مشخصات خودرو")
                            && models.get(i).getCar_type().toLowerCase().contains(newText))
                        newList.add(models.get(i));
                }

                adapter.setFilter(newList);
                return true;
            }
        });

        //----------------------------------------------------------------------------------------------------------

        binding.searchView.setOnCloseListener(new SearchView.OnCloseListener()
        {
            @Override
            public boolean onClose()
            {
                adapter.setFilter(models);
                binding.toolbar.setVisibility(View.VISIBLE);
                binding.lnr2.setVisibility(View.GONE);
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
            deleteDialog();
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

        if (view.getId() == R.id.archive)
        {
            archive();
        }

        if (view.getId() == R.id.closeLnr3)
        {
            binding.toolbar.setVisibility(View.VISIBLE);
            binding.lnr3.setVisibility(View.GONE);
            adapter.changeStatusS1();
        }

        if (view.getId() == R.id.unArchive)
        {
            unArchive();
        }

        if (view.getId() == R.id.closeLnr4)
        {
            binding.toolbar.setVisibility(View.VISIBLE);
            binding.lnr4.setVisibility(View.GONE);
            binding.unArchiveImg.setVisibility(View.GONE);
            binding.fab.setVisibility(View.VISIBLE);
            adapter.changeStatusS1();
            addUnArchiveDriver();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater ();
        inflater.inflate ( R.menu.menu_2, menu );
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item)
    {
        if (item.getItemId() == R.id.archive)
        {
            binding.toolbar.setVisibility(View.GONE);
            binding.lnr4.setVisibility(View.VISIBLE);
            binding.fab.setVisibility(View.GONE);
            addArchiveDriver();
        }
        return super.onOptionsItemSelected(item);
    }

    private void dialog()
    {
        final A9AddBinding binding1 = A9AddBinding.inflate(LayoutInflater.from(context));
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
                            {
                                create(binding1.name.getText().toString(), phone_number1, car_type1, number_plate1, alertDialog);
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

        //....................................................................................................

        alertDialog.getWindow().setBackgroundDrawable(context.getResources().getDrawable(R.drawable.rounded_linear));
        alertDialog.show();
        DisplayMetrics display = context.getResources().getDisplayMetrics();
        int width = display.widthPixels;
        width = (int) ((width) * ((double) 4 / 5));
        alertDialog.getWindow().setLayout(width, LinearLayout.LayoutParams.WRAP_CONTENT);
    }

    public void create(final String name, final String phone_number, final String car_type,final String number_plate, final AlertDialog alertDialog)
    {
        String url = getString(R.string.domain) + "api/driver/create";
        progressDialog.show();

        JSONObject object = new JSONObject();
        try
        {
            object.put("company_id", new User_Info().company_id());
            object.put("name", name);
            object.put("phone_number",phone_number);
            object.put("car_type", car_type);
            object.put("number_plate",number_plate);
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
                    int id = Integer.parseInt(response.getString("id"));
                    realm.beginTransaction();
                    realm.copyToRealmOrUpdate(new Activity9_DB(id, name, phone_number, car_type,number_plate,""));
                    realm.commitTransaction();

                    //----------------------------------------------------

                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(), "ایجاد راننده جدید با موفقیت انجام شد.", Toast.LENGTH_SHORT).show();
                    alertDialog.dismiss();
                    alertDialogBuilder = null;

                    addUnArchiveDriver();

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
        request.setRetryPolicy(new DefaultRetryPolicy(3000, 1, DefaultRetryPolicy.DEFAULT_MAX_RETRIES));
        AppController.getInstance().addToRequestQueue(request);

    }

    public void addUnArchiveDriver()
    {
        RealmResults<Activity9_DB> res = realm.where(Activity9_DB.class).findAll();

        models.clear();

        for (int i=0;i<res.size();i++)
        {
            if (!res.get(i).getArchive().equals("done"))
                models.add(new Activity9_Model(res.get(i).getId(),res.get(i).getName(),res.get(i).getPhone_number()
                    ,res.get(i).getCar_type(),res.get(i).getNumber_plate(),res.get(i).getArchive()));
        }

        adapter.notifyDataSetChanged();
    }

    public void addArchiveDriver()
    {
        RealmResults<Activity9_DB> res = realm.where(Activity9_DB.class).findAll();

        models.clear();

        for (int i=0;i<res.size();i++)
        {
            if (res.get(i).getArchive().equals("done"))
                models.add(new Activity9_Model(res.get(i).getId(),res.get(i).getName(),res.get(i).getPhone_number()
                        ,res.get(i).getCar_type(),res.get(i).getNumber_plate(),res.get(i).getArchive()));
        }

        adapter.notifyDataSetChanged();
    }

    public void changeStatusLnr3()
    {
        binding.toolbar.setVisibility(View.GONE);
        binding.lnr2.setVisibility(View.GONE);
        binding.lnr3.setVisibility(View.VISIBLE);
    }

    public void archive()
    {

       JSONArray driver_ids=new JSONArray();

        for (int i=0; i<models.size();i++)
        {
            if (models.get(i).isSelected())
            {
                driver_ids.put(models.get(i).getId());

                RealmResults<Activity9_DB> res = realm.where(Activity9_DB.class).equalTo("id",models.get(i).getId()).findAll();

                realm.beginTransaction();
                realm.copyToRealmOrUpdate(new Activity9_DB(models.get(i).getId(), res.get(0).getName(), res.get(0).getPhone_number(), res.get(0).getCar_type(), res.get(0).getNumber_plate(),"done"));
                realm.commitTransaction();
            }
        }

        String url = getString(R.string.domain) + "api/driver/archive";
        progressDialog.show();

        JSONObject object = new JSONObject();
        try
        {
            object.put("driver_ids", driver_ids);
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

                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(), "آیتم های انتخاب شده در آرشیو قرار گرفت.", Toast.LENGTH_SHORT).show();
                binding.toolbar.setVisibility(View.VISIBLE);
                binding.lnr3.setVisibility(View.GONE);
                addUnArchiveDriver();
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
        request.setRetryPolicy(new DefaultRetryPolicy(3000, 1, DefaultRetryPolicy.DEFAULT_MAX_RETRIES));
        AppController.getInstance().addToRequestQueue(request);
    }

    public void unArchive()
    {

        JSONArray driver_ids=new JSONArray();

        for (int i=0; i<models.size();i++)
        {
            if (models.get(i).isSelected())
            {
                driver_ids.put(models.get(i).getId());

                RealmResults<Activity9_DB> res = realm.where(Activity9_DB.class).equalTo("id",models.get(i).getId()).findAll();

                realm.beginTransaction();
                realm.copyToRealmOrUpdate(new Activity9_DB(models.get(i).getId(), res.get(0).getName(), res.get(0).getPhone_number(), res.get(0).getCar_type(), res.get(0).getNumber_plate(),""));
                realm.commitTransaction();
            }
        }

        String url = getString(R.string.domain) + "api/driver/un-archive";
        progressDialog.show();

        JSONObject object = new JSONObject();
        try
        {
            object.put("driver_ids", driver_ids);
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

                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(), "آیتم های انتخاب شده از ارشیو خارج شد.", Toast.LENGTH_SHORT).show();
                binding.toolbar.setVisibility(View.VISIBLE);
                binding.lnr4.setVisibility(View.GONE);
                binding.unArchiveImg.setVisibility(View.GONE);
                binding.fab.setVisibility(View.VISIBLE);
                adapter.changeStatusS1();
                addUnArchiveDriver();
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
        request.setRetryPolicy(new DefaultRetryPolicy(3000, 1, DefaultRetryPolicy.DEFAULT_MAX_RETRIES));
        AppController.getInstance().addToRequestQueue(request);
    }

    public void changeUnArchiveImgStatus()
    {
        binding.unArchiveImg.setVisibility(View.VISIBLE);
    }

    private void deleteDialog()
    {
        final com.example.boroodat.databinding.DeleteDialog1Binding binding1 = com.example.boroodat.databinding.DeleteDialog1Binding.inflate(LayoutInflater.from(context));
        View view = binding1.getRoot();
        alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setView(view);

        //----------------------------------------------------------------------------------------------------------

        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setPositiveButton("تایید", null);
        alertDialogBuilder.setNeutralButton("لغو", null);
        final AlertDialog alertDialog = alertDialogBuilder.create();

        //----------------------------------------------------------------------------------------------------------

        binding1.text.setText(context.getString(R.string.driver_delete));

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
                        if (binding1.password.getText().toString().equals(""))
                            binding1.password.setError("رمز عبور را وارد کنید.");

                        else
                        {
                            if (new Internet(context).check())
                                delete(binding1.password.getText().toString(),alertDialog);
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
                        alertDialogBuilder = null;
                    }
                });
            }
        });

        //---------------------------------------------------------------------------------------------------------

        alertDialog.getWindow().setBackgroundDrawable(context.getResources().getDrawable(R.drawable.rounded_linear));
        alertDialog.show();
        DisplayMetrics display = context.getResources().getDisplayMetrics();
        int width = display.widthPixels;
        width = (int) ((width) * ((double) 4 / 5));
        alertDialog.getWindow().setLayout(width, LinearLayout.LayoutParams.WRAP_CONTENT);
    }

    public void delete( final String password, final AlertDialog alertDialog)
    {
        JSONArray driver_ids=new JSONArray();

        for (int i=0; i<models.size();i++)
        {
            if (models.get(i).isSelected())
                driver_ids.put(models.get(i).getId());
        }

        String url = context.getString(R.string.domain) + "api/driver/delete";
        progressDialog.show();

        JSONObject object = new JSONObject();
        try
        {
            object.put("driver_ids",driver_ids);
            object.put("password", password);
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

                progressDialog.dismiss();

                try
                {
                    if (response.getString("code").equals("200"))
                    {
                        alertDialog.dismiss();
                        alertDialogBuilder = null;
                        Toast.makeText(context,"حذف راننده ها با موفقیت انجام شد.",Toast.LENGTH_LONG).show();
                        binding.toolbar.setVisibility(View.VISIBLE);
                        binding.lnr3.setVisibility(View.GONE);
                        adapter.changeStatusS1();
                        getData13();
                    }

                    else
                    {
                        Toast.makeText(context, "کد " + response.getString("code") + ":" + response.getString("message"), Toast.LENGTH_LONG).show();
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
        request.setRetryPolicy(new DefaultRetryPolicy(3000, 1, DefaultRetryPolicy.DEFAULT_MAX_RETRIES));
        AppController.getInstance().addToRequestQueue(request);

    }

    public void getData13()
    {
        String url = context.getString(R.string.domain) + "api/general/data13";

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
                    JSONArray array1 = response.getJSONArray("accounts");
                    JSONArray array2 = response.getJSONArray("reports");
                    JSONArray array3 = response.getJSONArray("drivers");

                    new SaveData(array1).toActivity7DB();
                    new SaveData(array2).toReportDB();
                    new SaveData(array3).toActivity9DB();

                    addUnArchiveDriver();

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
        request.setRetryPolicy(new DefaultRetryPolicy(3000, 3, DefaultRetryPolicy.DEFAULT_MAX_RETRIES));
        AppController.getInstance().addToRequestQueue(request);
    }
}
