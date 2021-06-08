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
import com.example.boroodat.database.User_Info_DB;
import com.example.boroodat.general.AppController;
import com.example.boroodat.R;
import com.example.boroodat.databinding.Activity1LoginBinding;
import com.example.boroodat.general.ClearError;
import com.example.boroodat.general.Internet;
import com.example.boroodat.general.Prefs;
import com.example.boroodat.general.User_Info;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import dmax.dialog.SpotsDialog;
import io.realm.Realm;

public class Activity1_Login extends AppCompatActivity
{
    private Activity1LoginBinding binding;
    private AlertDialog progressDialog;
    private Context context=this;
    private Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        binding = Activity1LoginBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        realm = Realm.getDefaultInstance();

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
                        new User_Info(response).save();

                         if (i==1)
                         {
                             realm.executeTransaction(new Realm.Transaction()
                             {
                                 @Override
                                 public void execute(Realm realm)
                                 {
                                     User_Info_DB infoDb = realm.where(User_Info_DB.class).findFirst();
                                     infoDb.setPassword(password);
                                     Toast.makeText(getApplicationContext(), "رمز عبور ذخیره شد.", Toast.LENGTH_SHORT).show();
                                 }
                             });

                         }

                        if (new User_Info().role().equals("Developer"))
                        {
                            Intent intent = new Intent(Activity1_Login.this, Activity0_Developer.class);
                            startActivity(intent);
                            finish();
                        }

                        else if (new User_Info().role().equals("مدیر"))
                        {
                            Intent intent = new Intent(Activity1_Login.this, Activity2_Manager.class);
                            startActivity(intent);
                            finish();
                        }

                        else if (new User_Info().role().equals("کارمند"))
                        {
                            Intent intent = new Intent(Activity1_Login.this, Activity20_User.class);
                            startActivity(intent);
                            finish();
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

}
