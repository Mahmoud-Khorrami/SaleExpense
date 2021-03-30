package com.example.boroodat.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;

import com.example.boroodat.R;
import com.example.boroodat.fragment.Fragment5_SaleReports;
import com.example.boroodat.general.FragmentUtil;

import dmax.dialog.SpotsDialog;

public class Activity12_BuyerDetails extends AppCompatActivity
{
    com.example.boroodat.databinding.ReportsBinding binding;
    private AlertDialog progressDialog;
    private String buyer_name =null;
    private Fragment5_SaleReports fragment5SaleReports;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);
        binding = com.example.boroodat.databinding.ReportsBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        //-----------------------------------------------------------------------------

        progressDialog = new SpotsDialog(this, R.style.Custom);
        progressDialog.setCancelable(false);

        //-----------------------------------------------------------------------------

        final Bundle extras=getIntent().getExtras();
        buyer_name =extras.getString("buyer_name");

        //-----------------------------------------------------------------------------

        fragment5SaleReports =new Fragment5_SaleReports("manager");

        //-----------------------------------------------------------------------------

        setupViewPager ( binding.pager );
        binding.tabs.setupWithViewPager ( binding.pager );
        binding.tabs.getTabAt(0).select();

    }

    private void setupViewPager(ViewPager viewPager)
    {
        FragmentUtil.ViewPagerAdapter adapter=new FragmentUtil.ViewPagerAdapter ( getSupportFragmentManager () );
        adapter.addFragment (fragment5SaleReports,"لیست خریدهای " + buyer_name );
        viewPager.setAdapter ( adapter );
    }

    @Override
    protected void onResume()
    {
        super.onResume();
    }
}
