package com.example.boroodat.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;

import com.example.boroodat.R;
import com.example.boroodat.databinding.ReportsBinding;
import com.example.boroodat.fragment.Fragment7_SalaryReports;
import com.example.boroodat.fragment.Fragment8_MaterialReports;
import com.example.boroodat.fragment.Fragment9_OtherExpenseReports;
import com.example.boroodat.general.FragmentUtil;

import dmax.dialog.SpotsDialog;

public class Activity15_ExpenseReports extends AppCompatActivity
{
    ReportsBinding binding;
    private AlertDialog progressDialog;
    private Fragment7_SalaryReports fragment7SalaryReport;
    private Fragment8_MaterialReports fragment8MaterialReports;
    private Fragment9_OtherExpenseReports fragment9OtherExpenseReports;

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

        fragment7SalaryReport =new Fragment7_SalaryReports();
        fragment8MaterialReports =new Fragment8_MaterialReports("manager");
        fragment9OtherExpenseReports =new Fragment9_OtherExpenseReports("manager");

        //-----------------------------------------------------------------------------

        setupViewPager ( binding.pager );
        binding.tabs.setupWithViewPager ( binding.pager );
        binding.tabs.getTabAt(1).select();

    }

    private void setupViewPager(ViewPager viewPager)
    {
        FragmentUtil.ViewPagerAdapter adapter=new FragmentUtil.ViewPagerAdapter ( getSupportFragmentManager () );
        adapter.addFragment (fragment7SalaryReport,"حقوق و دستمزد" );
        adapter.addFragment (fragment8MaterialReports,"خرید مواد اولیه" );
        adapter.addFragment (fragment9OtherExpenseReports,"سایر هزینه ها" );
        viewPager.setAdapter ( adapter );
    }

    @Override
    protected void onResume()
    {
        super.onResume();
    }
}
