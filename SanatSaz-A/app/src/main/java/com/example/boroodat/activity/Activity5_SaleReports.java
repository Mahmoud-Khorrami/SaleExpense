package com.example.boroodat.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;

import com.example.boroodat.R;
import com.example.boroodat.databinding.ReportsBinding;
import com.example.boroodat.fragment.Fragment4_DepositReports;
import com.example.boroodat.fragment.Fragment5_SaleReports;
import com.example.boroodat.general.FragmentUtil;

import dmax.dialog.SpotsDialog;

public class Activity5_SaleReports extends AppCompatActivity
{

    private ReportsBinding binding;
    private AlertDialog progressDialog;
    private Fragment4_DepositReports fragment4DepositReports;
    private Fragment5_SaleReports fragment5SaleReports;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        binding = ReportsBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        //-----------------------------------------------------------------------------

        progressDialog = new SpotsDialog(this, R.style.Custom);
        progressDialog.setCancelable(false);

        //-----------------------------------------------------------------------------

        fragment4DepositReports =new Fragment4_DepositReports();
        fragment5SaleReports =new Fragment5_SaleReports("manager");

        //-----------------------------------------------------------------------------

        setupViewPager ( binding.pager );
        binding.tabs.setupWithViewPager ( binding.pager );
        binding.tabs.getTabAt(1).select();

    }

    private void setupViewPager(ViewPager viewPager)
    {
        FragmentUtil.ViewPagerAdapter adapter=new FragmentUtil.ViewPagerAdapter ( getSupportFragmentManager () );
        adapter.addFragment (fragment4DepositReports,"سایر دریافت ها" );
        adapter.addFragment (fragment5SaleReports,"فروش ها" );
        viewPager.setAdapter ( adapter );
    }

    @Override
    protected void onResume()
    {
        super.onResume();
    }
}
