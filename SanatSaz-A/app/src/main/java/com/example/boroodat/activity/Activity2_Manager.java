package com.example.boroodat.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.boroodat.adapter.Activity2_FragmentsAdapter;
import com.example.boroodat.database.User_Info_DB;
import com.example.boroodat.databinding.Dialog1Binding;
import com.example.boroodat.fragment.Activity2Fragments;
import com.example.boroodat.general.AppController;
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
import io.realm.Realm;
import io.realm.RealmResults;

public class Activity2_Manager extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener
{
    private Activity2ManagerBinding binding;
    private android.app.AlertDialog progressDialog;
    private boolean doubleBackToExitPressedOnce=false;
    private Context context=this;

    private Activity2Fragments fragments;
    private Activity2_FragmentsAdapter adapter;

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

        //-------------------------------------------------------------------------------------------------------

        progressDialog = new SpotsDialog(this,R.style.Custom);
        progressDialog.setCancelable(false);

        //----------------------------------------------------------------------------------------------------------

        adapter = new Activity2_FragmentsAdapter(getSupportFragmentManager(), fragments,4);
        binding.pager1.setOffscreenPageLimit(3);
        binding.pager1.setAdapter(adapter);

        //----------------------------------------------------------------------------------------------------------

        binding.bnv1.setOnNavigationItemSelectedListener(this);
        binding.bnv1.setSelectedItemId(R.id.menu_1_4);
        binding.pager1.setPagingEnabled(false);

        //----------------------------------------------------------------------------------------------------------

        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent intent=new Intent(Activity2_Manager.this, Activity6_RecordSales.class);
                intent.putExtra("from","manager");
                startActivity(intent);
            }
        });

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
