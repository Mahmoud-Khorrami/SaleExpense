package com.example.boroodat.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;

import com.example.boroodat.R;
import com.example.boroodat.fragment.Fragment7_SalaryReports;
import com.example.boroodat.general.FragmentUtil;

import dmax.dialog.SpotsDialog;

public class Activity22_PersonnelDetails extends AppCompatActivity
{
    com.example.boroodat.databinding.ReportsBinding binding;
    private AlertDialog progressDialog;
    private String personnel_name =null;
    private Fragment7_SalaryReports fragment7SalaryReports;

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
        personnel_name =extras.getString("personnel_name");

        //-----------------------------------------------------------------------------

        fragment7SalaryReports =new Fragment7_SalaryReports();

        //-----------------------------------------------------------------------------

        setupViewPager ( binding.pager );
        binding.tabs.setupWithViewPager ( binding.pager );
        binding.tabs.getTabAt(0).select();

    }

    private void setupViewPager(ViewPager viewPager)
    {
        FragmentUtil.ViewPagerAdapter adapter=new FragmentUtil.ViewPagerAdapter ( getSupportFragmentManager () );
        adapter.addFragment (fragment7SalaryReports,"لیست پرداختی ها به " + personnel_name );
        viewPager.setAdapter ( adapter );
    }

    @Override
    protected void onResume()
    {
        super.onResume();
    }
}
