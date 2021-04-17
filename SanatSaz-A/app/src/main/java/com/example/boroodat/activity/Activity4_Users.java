package com.example.boroodat.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
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
import com.example.boroodat.adapter.Activity4_Adapter;
import com.example.boroodat.databinding.A4AddBinding;
import com.example.boroodat.general.AppController;
import com.example.boroodat.general.ClearError;
import com.example.boroodat.general.Internet;
import com.example.boroodat.general.User_Info;
import com.example.boroodat.model.Activity4_Model;
import com.example.boroodat.R;
import com.example.boroodat.databinding.Activity4UsersBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dmax.dialog.SpotsDialog;
import io.realm.Realm;

public class Activity4_Users extends AppCompatActivity
{
    private List<Activity4_Model> models =new ArrayList<>(  );
    private Activity4_Adapter adapter;
    private Context context=this;
    private AlertDialog.Builder alertDialogBuilder=null;
    private android.app.AlertDialog progressDialog;
    private Activity4UsersBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        binding = Activity4UsersBinding.inflate(getLayoutInflater());
        View view= binding.getRoot();
        setContentView(view);

        //----------------------------------------------------------------------------------------------------------

        progressDialog = new SpotsDialog(this,R.style.Custom);
        progressDialog.setCancelable(false);

        //----------------------------------------------------------------------------------------------------------

        adapter = new Activity4_Adapter(models, Activity4_Users.this );
        binding.recyclerView.setLayoutManager ( new LinearLayoutManager( Activity4_Users.this ) );
        binding.recyclerView.setAdapter (adapter);
        addUser();

        //----------------------------------------------------------------------------------------------------------

        binding.fab.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if (alertDialogBuilder == null)
                    dialog();
            }
        });

        //----------------------------------------------------------------------------------------------------------

    }

    private void dialog()
    {
        final A4AddBinding binding1 = A4AddBinding.inflate(LayoutInflater.from(context));
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
        binding1.password.addTextChangedListener(new ClearError(binding1.til2));
        binding1.phone.addTextChangedListener(new ClearError(binding1.til3));

        //----------------------------------------------------------------------------------------------------------

        ArrayList<String> roles=new ArrayList<>();
        roles.add("");
        roles.add("مدیر");
        roles.add("کارمند");

        ArrayAdapter<String> adp = new ArrayAdapter<String>(context, R.layout.spinner_item, roles);
        binding1.spinner.setAdapter(adp);
        binding1.spinner.setSelection(0);

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
                            binding1.til1.setError("نام کاربر را وارد کنید.");
                        }

                        else if (binding1.password.getText().toString().length() < 8)
                        {
                            binding1.scrollView.scrollTo(0,binding1.til2.getTop());
                            binding1.til2.setError("رمز عبور باید حداقل 8 رقم باشد.");
                        }

                        else if (binding1.phone.getText().toString().length() != 11)
                        {
                            binding1.scrollView.scrollTo(0,binding1.til3.getTop());
                            binding1.til3.setError("شماره همراه کاربر باید 11 رقم باشد.");
                        }

                        else if (binding1.spinner.getSelectedItem().toString().equals(""))
                        {
                            binding1.scrollView.scrollTo(0,binding1.role.getTop());
                            Toast.makeText(getApplicationContext(),"سمت کاربر را انتخاب کنید.",Toast.LENGTH_LONG).show();
                        }

                        else
                        {
                            if (new Internet(context).check())
                                registerUser(binding1.name.getText().toString(),binding1.password.getText().toString(),binding1.phone.getText().toString(),binding1.spinner.getSelectedItem().toString(),alertDialog);
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

    public void registerUser(String name, String password, String phone_number, String role, final AlertDialog alertDialog)
    {
        String url = getString(R.string.domain) + "api/user/register";
        progressDialog.show();

        JSONObject object = new JSONObject();
        try
        {
            object.put("name", name);
            object.put("password", password);
            object.put("phone_number", phone_number);
            object.put("role", role);
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
                progressDialog.dismiss();
                try
                {
                    String code = response.getString("code");

                    if (code.equals("200"))
                    {
                        addUser();
                        Toast.makeText(getApplicationContext(), "ثبت کاربر با موفقیت انجام شد." , Toast.LENGTH_SHORT).show();
                        alertDialog.dismiss();
                        alertDialogBuilder = null;
                    }

                    else
                        Toast.makeText(getApplicationContext(),response.getString("message"), Toast.LENGTH_LONG).show();

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

    public void addUser()
    {
        String url = getString(R.string.domain) + "api/user/show";
        progressDialog.show();

        final JSONObject object = new JSONObject();
        try
        {
            object.put("company_id",new User_Info().company_id());
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

                models.clear();
                try
                {
                    JSONArray array = response.getJSONArray("users");

                    for (int i = array.length() - 1; i >= 0; i--)
                    {
                        JSONObject object1 = array.getJSONObject(i);

                        int id = Integer.parseInt(object1.getString("id"));
                        String name = object1.getString("name");
                        String phone = object1.getString("phone_number");
                        String role = object1.getString("role");


                        models.add(new Activity4_Model(id, name, phone, role));

                    }

                    adapter.notifyDataSetChanged();

                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
                progressDialog.dismiss();

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
}
