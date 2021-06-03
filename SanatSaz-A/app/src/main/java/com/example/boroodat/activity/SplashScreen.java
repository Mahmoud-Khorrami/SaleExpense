package com.example.boroodat.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.boroodat.R;
import com.example.boroodat.database.User_Info_DB;
import com.example.boroodat.databinding.SplashScreenBinding;
import com.example.boroodat.general.AppController;
import com.example.boroodat.general.User_Info;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import dmax.dialog.SpotsDialog;
import io.realm.Realm;
import io.realm.RealmResults;

public class SplashScreen extends AppCompatActivity
{

    private SplashScreenBinding binding;
    private AlertDialog progressDialog;
    private Realm realm;
    private int s=0;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        binding = SplashScreenBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        realm = Realm.getDefaultInstance();

        //-------------------------------------------------------------------------------------------------------

        progressDialog = new SpotsDialog(this,R.style.Custom);
        progressDialog.setCancelable(true);

        //-------------------------------------------------------------------------------------------------------

        animation();

        //-------------------------------------------------------------------------------------------------------

        binding.ConstraintLayout.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if (s == 1)
                {
                    getUserPass();
                }
            }
        });
    }

    private void animation()
    {

        binding.appName.setVisibility(View.GONE);

        binding.icon.animate().scaleX(1f).scaleY(1f).setDuration(2000).setListener(new AnimatorListenerAdapter()
        {
            @Override
            public void onAnimationEnd(Animator animation)
            {
                super.onAnimationEnd(animation);

                binding.appName.setVisibility(View.VISIBLE);
                binding.appName.animate().rotationY(360).setDuration(2000);
                binding.logo.animate().alpha(1f).setDuration(3000).setListener(new AnimatorListenerAdapter()
                {
                    @Override
                    public void onAnimationEnd(Animator animation)
                    {
                        super.onAnimationEnd(animation);

                        getUserPass();
                    }
                });

            }
        });
    }

    private void getUserPass()
    {
        RealmResults<User_Info_DB> results = realm.where(User_Info_DB.class).findAll();

        if (results.size() == 0)
        {
            startActivity(new Intent(SplashScreen.this, Activity1_Login.class));
            finish();
        }

        else if (results.get(0).getPassword().equals(""))
        {
            startActivity(new Intent(SplashScreen.this, Activity1_Login.class));
            finish();
        }

        else
            login(results.get(0).getPhone_number(), results.get(0).getPassword());
    }

    public void login(String phone_number, String password)
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
                        if (new User_Info().role().equals("Developer"))
                        {
                            Intent intent = new Intent(SplashScreen.this, Activity0_Developer.class);
                            startActivity(intent);
                            finish();
                        }

                        else if (new User_Info().role().equals("مدیر"))
                        {
                            Intent intent = new Intent(SplashScreen.this, Activity2_Manager.class);
                            startActivity(intent);
                            finish();
                        }

                        else if (new User_Info().role().equals("کارمند"))
                        {
                            Intent intent = new Intent(SplashScreen.this, Activity20_User.class);
                            startActivity(intent);
                            finish();
                        }
                    }

                    else
                    {
                        startActivity(new Intent(SplashScreen.this, Activity1_Login.class));
                        finish();
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
                s=1;
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
        request.setRetryPolicy(new DefaultRetryPolicy(10000, 0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES));
        AppController.getInstance().addToRequestQueue(request);

    }
}
