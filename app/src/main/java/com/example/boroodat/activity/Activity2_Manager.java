package com.example.boroodat.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.boroodat.fragment.Fragment0_Setting;
import com.example.boroodat.fragment.Fragment1_Expense;
import com.example.boroodat.fragment.Fragment2_Sale;
import com.example.boroodat.fragment.Fragment3_Home;
import com.example.boroodat.general.AppController;
import com.example.boroodat.general.FragmentUtil;
import com.example.boroodat.R;
import com.example.boroodat.databinding.Activity2ManagerBinding;
import com.example.boroodat.general.NavigationIconClickListener;
import com.example.boroodat.general.RuntimePermissionsActivity;
import com.example.boroodat.general.User_Info;
import com.example.boroodat.general.WriteToExcel;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.card.MaterialCardView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import dmax.dialog.SpotsDialog;

public class Activity2_Manager extends RuntimePermissionsActivity implements BottomNavigationView.OnNavigationItemSelectedListener
{
    private Activity2ManagerBinding binding;
    private android.app.AlertDialog progressDialog;
    private Fragment0_Setting fragment0Setting;
    private Fragment1_Expense fragment1Expense;
    private Fragment2_Sale fragment2Sale;
    private Fragment3_Home fragment3Home;
    private boolean doubleBackToExitPressedOnce=false;
    private Context context=this;
    private int code = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        binding = Activity2ManagerBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        //-------------------------------------------------------------------------------------------------------

        binding.appBar.setTitle("");
        binding.title.setText(new User_Info().company_name());
        setSupportActionBar(binding.appBar);

        binding.appBar.setNavigationOnClickListener(new NavigationIconClickListener(context, view.findViewById(R.id.pager_1)));

        //-------------------------------------------------------------------------------------------------------

        View view1 = binding.lnr1;
        MaterialCardView xls=view1.findViewById(R.id.xls);
        MaterialCardView about=view1.findViewById(R.id.about);

        xls.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if (Environment.getExternalStorageDirectory().canWrite())
                    getData14();

                else
                    Activity2_Manager.super.requestAppPermissions ( new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, code );
            }
        });

        about.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                startActivity(new Intent(Activity2_Manager.this,Activity24_AboutUs.class));
            }
        });
        //-------------------------------------------------------------------------------------------------------

        progressDialog = new SpotsDialog(this,R.style.Custom);
        progressDialog.setCancelable(false);

        //----------------------------------------------------------------------------------------------------------

        fragment0Setting = new Fragment0_Setting();
        fragment1Expense = new Fragment1_Expense();
        fragment2Sale = new Fragment2_Sale();
        fragment3Home = new Fragment3_Home();

        //----------------------------------------------------------------------------------------------------------

        setupViewPager(binding.pager1);
        binding.pager1.setCurrentItem(1);
        binding.pager1.setPagingEnabled(false);

        //----------------------------------------------------------------------------------------------------------

        binding.bnv1.setOnNavigationItemSelectedListener(this);
        binding.bnv1.setSelectedItemId(R.id.menu_1_4);

        //----------------------------------------------------------------------------------------------------------

        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent intent=new Intent(Activity2_Manager.this, Activity6_RecordSales.class);
                intent.putExtra("company_id",new User_Info().company_id());
                intent.putExtra("token",new User_Info().token());
                intent.putExtra("from","manager");
                startActivity(intent);
            }
        });

        //----------------------------------------------------------------------------------------------------------

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item)
    {
        if (item.getItemId() == R.id.menu_1_1)
        {
            binding.pager1.setCurrentItem(0);
            return true;
        }

        if (item.getItemId() == R.id.menu_1_2)
        {
            binding.pager1.setCurrentItem(1);
            return true;
        }

        if (item.getItemId() == R.id.menu_1_3)
        {
            binding.pager1.setCurrentItem(2);
            return true;
        }

        if (item.getItemId() == R.id.menu_1_4)
        {
            binding.pager1.setCurrentItem(3);
            return true;
        }

        return false;
    }

    private void setupViewPager(ViewPager viewPager)
    {
        FragmentUtil.ViewPagerAdapter adapter = new FragmentUtil.ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(fragment0Setting);
        adapter.addFragment(fragment1Expense);
        adapter.addFragment(fragment2Sale);
        adapter.addFragment(fragment3Home);
        viewPager.setAdapter(adapter);
    }

    public void getData14()
    {
        String url = getString(R.string.domain) + "api/general/data14";
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
                new WriteToExcel(context).export(response);
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

    @Override
    protected void onResume()
    {
        super.onResume();
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
    }

    @Override
    public void onBackPressed()
    {
        if (doubleBackToExitPressedOnce)
        {
            super.onBackPressed();
            finish();
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "برای خروج مجددا دکمه بازگشت را فشار دهید.", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable()
        {

            @Override
            public void run()
            {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }

}
