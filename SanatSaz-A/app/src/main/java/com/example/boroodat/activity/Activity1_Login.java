package com.example.boroodat.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.boroodat.database.Activity7_DB;
import com.example.boroodat.database.UserPass_DB;
import com.example.boroodat.general.AppController;
import com.example.boroodat.R;
import com.example.boroodat.databinding.Activity1LoginBinding;
import com.example.boroodat.general.ClearError;
import com.example.boroodat.general.Internet;
import com.example.boroodat.general.Prefs;
import com.example.boroodat.general.RuntimePermissionsActivity;
import com.example.boroodat.general.SaveData;
import com.example.boroodat.general.User_Info;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import dmax.dialog.SpotsDialog;
import io.realm.Realm;
import io.realm.RealmResults;

public class Activity1_Login extends RuntimePermissionsActivity
{
    private Activity1LoginBinding binding;
    private AlertDialog progressDialog;
    private Context context=this;
    private Realm realm;
    private int code = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        binding = Activity1LoginBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        realm=Realm.getDefaultInstance();
        //-------------------------------------------------------------------------------------------------------

        if (Prefs.with ( this ).isFirstLoad ())
        {
            Activity1_Login.super.requestAppPermissions ( new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE}, code );

            Prefs.with ( this ).firstLoadIsDone ();
        }

        //-------------------------------------------------------------------------------------------------------

        progressDialog = new SpotsDialog(this,R.style.Custom);
        progressDialog.setCancelable(true);

        //-------------------------------------------------------------------------------------------------------

        binding.password.addTextChangedListener(new ClearError(binding.passwordTil));
        binding.phoneNumber.addTextChangedListener(new ClearError(binding.phonNumberTil));

        //-------------------------------------------------------------------------------------------------------

        binding.login.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {

                if (binding.phoneNumber.getText().toString().equals(""))
                    binding.phonNumberTil.setError("شماره همراه را وارد کنید.");

                else if (binding.password.getText().toString().equals(""))
                    binding.passwordTil.setError("رمز عبور را وارد کنید.");

                else if (new Internet(context).check())
                    login(binding.phoneNumber.getText().toString(), binding.password.getText().toString(),0);
                else
                    new Internet(context).enable();

            }
        });

        //-------------------------------------------------------------------------------------------------------

        binding.saveAndLogin.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if (binding.phoneNumber.getText().toString().equals(""))
                    binding.phonNumberTil.setError("شماره همراه را وارد کنید.");

                else if (binding.password.getText().toString().equals(""))
                    binding.passwordTil.setError("رمز عبور را وارد کنید.");

                else if (new Internet(context).check())
                    login(binding.phoneNumber.getText().toString(), binding.password.getText().toString(),1);
                else
                    new Internet(context).enable();
            }
        });
    }

    public void login(final String phone_number, final String password, final int i)
    {
        String url = getString(R.string.domain) + "api/login";
        progressDialog.show();

        final JSONObject object = new JSONObject();
        try
        {
            object.put("phone_number", phone_number);
            object.put("password", password);
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
                try
                {
                    String code = response.getString("code");

                    if (code.equals("200"))
                    {
                        if (i==1)
                        {

                            realm.beginTransaction();
                            realm.copyToRealmOrUpdate(new UserPass_DB(0,phone_number,password));
                            realm.commitTransaction();

                            Toast.makeText(getApplicationContext(),"شماره همراه و رمز عبور ذخیره شد.", Toast.LENGTH_SHORT).show();
                        }

                        new User_Info(response).save();

                        if (new User_Info().role().equals("Developer"))
                        {
                            Intent intent = new Intent(Activity1_Login.this, Activity0_Developer.class);
                            intent.putExtra("token", new User_Info().token());
                            intent.putExtra("token_id", new User_Info().token_id());
                            startActivity(intent);
                            finish();
                        }

                        else if (new User_Info().role().equals("مدیر"))
                        {
                            getData1();
                        }

                        else if (new User_Info().role().equals("کارمند"))
                        {
                            getData7();
                        }
                    }

                    else
                        Toast.makeText(getApplicationContext(),"کد "+response.getString("code")+ ":" + response.getString("message"), Toast.LENGTH_SHORT).show();

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
                return headers;
            }
        };
        request.setRetryPolicy(new DefaultRetryPolicy(3000, 2, DefaultRetryPolicy.DEFAULT_MAX_RETRIES));
        AppController.getInstance().addToRequestQueue(request);

    }

    public void getData1()
    {
        String url = getString(R.string.domain) + "api/general/data1";
        progressDialog.show();

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
                progressDialog.dismiss();

                try
                {
                    JSONArray array1 = response.getJSONArray("accounts");
                    JSONArray array2 = response.getJSONArray("buyers");
                    JSONArray array3 = response.getJSONArray("drivers");
                    JSONArray array4 = response.getJSONArray("personnel");
                    JSONArray array5 = response.getJSONArray("reports");

                    boolean b1 = new SaveData(array1).toActivity7DB();
                    boolean b2 = new SaveData(array2).toActivity8DB();
                    boolean b3 = new SaveData(array3).toActivity9DB();
                    boolean b4 = new SaveData(array4).toActivity14DB();
                    boolean b5 = new SaveData(array5).toReportDB();

                    if (b1 & b2 & b3 & b4 & b5)
                    {
                        Intent intent = new Intent(Activity1_Login.this, Activity2_Manager.class);
                        intent.putExtra("token", new User_Info().token());
                        intent.putExtra("company_id", new User_Info().company_id());
                        intent.putExtra("company_name", new User_Info().company_name());
                        startActivity(intent);
                        finish();
                    }

                    else
                        Toast.makeText(getApplicationContext(), "مجددا تلاش کنید.", Toast.LENGTH_LONG).show();

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
        request.setRetryPolicy(new DefaultRetryPolicy(3000, 3, DefaultRetryPolicy.DEFAULT_MAX_RETRIES));
        AppController.getInstance().addToRequestQueue(request);
    }

    public void getData7()
    {
        String url = getString(R.string.domain) + "api/general/data7";
        progressDialog.show();

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
                progressDialog.dismiss();

                try
                {

                    JSONArray array1 = response.getJSONArray("accounts");
                    JSONArray array2 = response.getJSONArray("buyers");
                    JSONArray array3 = response.getJSONArray("drivers");

                    boolean b1 = new SaveData(array1).toActivity7DB();
                    boolean b2 = new SaveData(array2).toActivity8DB();
                    boolean b3 = new SaveData(array3).toActivity9DB();

                    if (b1 & b2 & b3)
                    {
                        Intent intent = new Intent(Activity1_Login.this, Activity20_User.class);
                        intent.putExtra("token", new User_Info().token());
                        intent.putExtra("company_id", new User_Info().company_id());
                        intent.putExtra("company_name", new User_Info().company_name());
                        startActivity(intent);
                        finish();
                    }

                    else
                        Toast.makeText(getApplicationContext(), "مجددا تلاش کنید.", Toast.LENGTH_LONG).show();

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
        request.setRetryPolicy(new DefaultRetryPolicy(3000, 3, DefaultRetryPolicy.DEFAULT_MAX_RETRIES));
        AppController.getInstance().addToRequestQueue(request);
    }

    @Override
    public void onPermissionsGranted(int requestCode)
    {
        if (requestCode == code)
            Toast.makeText ( getApplicationContext (),
                    "مجوز دسترسی به حافظه داده شد.", Toast.LENGTH_SHORT ).show ();
    }

    @Override
    public void onPermissionsDeny(int requestCode)
    {
        Toast.makeText ( getApplicationContext (),
                "مجوز دسترسی به حافظه داده نشد.", Toast.LENGTH_LONG ).show ();
    }
}
