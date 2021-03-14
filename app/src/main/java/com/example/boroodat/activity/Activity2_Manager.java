package com.example.boroodat.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.boroodat.fragment.Fragment0_Setting;
import com.example.boroodat.fragment.Fragment1_Expense;
import com.example.boroodat.fragment.Fragment2_Sale;
import com.example.boroodat.fragment.Fragment3_Home;
import com.example.boroodat.general.FragmentUtil;
import com.example.boroodat.R;
import com.example.boroodat.databinding.Activity2ManagerBinding;
import com.example.boroodat.general.User_Info;
import com.github.amlcurran.showcaseview.ShowcaseView;
import com.github.amlcurran.showcaseview.targets.Target;
import com.github.amlcurran.showcaseview.targets.ViewTarget;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import dmax.dialog.SpotsDialog;
import io.realm.Realm;

public class Activity2_Manager extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener
{
    private Activity2ManagerBinding binding;
    private android.app.AlertDialog progressDialog;
    private Fragment0_Setting fragment0Setting;
    private Fragment1_Expense fragment1Expense;
    private Fragment2_Sale fragment2Sale;
    private Fragment3_Home fragment3Home;
    private boolean doubleBackToExitPressedOnce=false;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        binding = Activity2ManagerBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        binding.title.setText(new User_Info().company_name());
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

        binding.setting.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                startActivity(new Intent(Activity2_Manager.this,Activity23_Setting.class));
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
