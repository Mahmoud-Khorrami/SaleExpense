package com.example.boroodat.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;

import com.example.boroodat.R;
import com.example.boroodat.databinding.Activity21UserReportBinding;
import com.example.boroodat.fragment.Fragment5_SaleReports;
import com.example.boroodat.fragment.Fragment9_OtherExpenseReports;
import com.example.boroodat.general.FragmentUtil;

import dmax.dialog.SpotsDialog;

public class Activity21_UserReport extends AppCompatActivity
{
    private Activity21UserReportBinding binding;
    private AlertDialog progressDialog;
    private Fragment9_OtherExpenseReports fragment9_otherExpenseReports;
    private Fragment5_SaleReports fragment5SaleReports;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        binding = Activity21UserReportBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        //-----------------------------------------------------------------------------

        progressDialog = new SpotsDialog(this, R.style.Custom);
        progressDialog.setCancelable(false);

        //-----------------------------------------------------------------------------

        fragment9_otherExpenseReports =new Fragment9_OtherExpenseReports("user");
        fragment5SaleReports =new Fragment5_SaleReports("user");

        //-----------------------------------------------------------------------------

        setupViewPager ( binding.pager );
        binding.tabs.setupWithViewPager ( binding.pager );
        binding.tabs.getTabAt(1).select();

    }

    private void setupViewPager(ViewPager viewPager)
    {
        FragmentUtil.ViewPagerAdapter adapter=new FragmentUtil.ViewPagerAdapter ( getSupportFragmentManager () );
        adapter.addFragment (fragment9_otherExpenseReports,"هزینه ها" );
        adapter.addFragment (fragment5SaleReports,"فروش ها" );
        viewPager.setAdapter ( adapter );
    }

    @Override
    protected void onResume()
    {
        super.onResume();
    }
}
