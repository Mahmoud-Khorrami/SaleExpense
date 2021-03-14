package com.example.boroodat.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;

import com.example.boroodat.R;
import com.example.boroodat.database.Activity7_DB;
import com.example.boroodat.database.Fragment4_DB;
import com.example.boroodat.database.Fragment5_DB;
import com.example.boroodat.database.Fragment7_DB;
import com.example.boroodat.database.Fragment8_DB;
import com.example.boroodat.database.Fragment9_DB;
import com.example.boroodat.databinding.Activity11AccountDetailsBinding;
import com.example.boroodat.fragment.Fragment4_DepositReports;
import com.example.boroodat.fragment.Fragment5_SaleReports;
import com.example.boroodat.fragment.Fragment7_SalaryReports;
import com.example.boroodat.fragment.Fragment8_MaterialReports;
import com.example.boroodat.fragment.Fragment9_OtherExpenseReports;
import com.example.boroodat.general.FragmentUtil;
import com.example.boroodat.general.NumberTextWatcherForThousand;

import dmax.dialog.SpotsDialog;
import io.realm.Realm;
import io.realm.RealmResults;

public class Activity11_AccountDetails extends AppCompatActivity
{

    Activity11AccountDetailsBinding binding;
    private AlertDialog progressDialog;
    private String account_id=null;
    private Fragment4_DepositReports fragment4DepositReports;
    private Fragment5_SaleReports fragment5SaleReports;
    private Fragment7_SalaryReports fragment7SalaryReport;
    private Fragment8_MaterialReports fragment8MaterialReports;
    private Fragment9_OtherExpenseReports fragment9OtherExpenseReports;
    private Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);
        binding = Activity11AccountDetailsBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        realm=Realm.getDefaultInstance();
        //-----------------------------------------------------------------------------

        progressDialog = new SpotsDialog(this, R.style.Custom);
        progressDialog.setCancelable(false);

        //-----------------------------------------------------------------------------

        final Bundle extras=getIntent().getExtras();
        account_id=extras.getString("account_id");

        //-----------------------------------------------------------------------------

        binding.balance.addTextChangedListener(new NumberTextWatcherForThousand(binding.balance));
        binding.sales.addTextChangedListener(new NumberTextWatcherForThousand(binding.sales));
        binding.deposits.addTextChangedListener(new NumberTextWatcherForThousand(binding.deposits));
        binding.salary.addTextChangedListener(new NumberTextWatcherForThousand(binding.salary));
        binding.rawMaterial.addTextChangedListener(new NumberTextWatcherForThousand(binding.rawMaterial));
        binding.otherExpenses.addTextChangedListener(new NumberTextWatcherForThousand(binding.otherExpenses));
        setData();

        //-----------------------------------------------------------------------------

        fragment4DepositReports =new Fragment4_DepositReports();
        fragment5SaleReports =new Fragment5_SaleReports("manager");
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
        adapter.addFragment (fragment4DepositReports,"سایر دریافت ها" );
        adapter.addFragment (fragment5SaleReports,"فروش ها" );
        adapter.addFragment (fragment7SalaryReport,"حقوق و دستمزد" );
        adapter.addFragment (fragment8MaterialReports,"مواد اولیه" );
        adapter.addFragment (fragment9OtherExpenseReports,"سایر هزینه ها" );
        viewPager.setAdapter ( adapter );
    }

    public void setData()
    {
        RealmResults<Activity7_DB> res1 = realm.where(Activity7_DB.class).equalTo("id",Integer.parseInt(account_id)).findAll();
        binding.balance.setText(res1.get(0).getBalance());

        //-----------------------------------------------------------------

        RealmResults<Fragment4_DB> res2 = realm.where(Fragment4_DB.class).findAll();

        double deposits=0;

        for (int i=0;i<res2.size();i++)
            deposits=deposits+Double.parseDouble(res2.get(i).getAmount());

        binding.deposits.setText(Math.round(deposits)+"");

        //-----------------------------------------------------------------

        RealmResults<Fragment5_DB> res3 = realm.where(Fragment5_DB.class).findAll();

        double sales=0;

        for (int i=0;i<res3.size();i++)
            sales=sales+Double.parseDouble(res3.get(i).getPayment());

        binding.sales.setText(Math.round(sales)+"");

        //-----------------------------------------------------------------

        RealmResults<Fragment7_DB> res4 = realm.where(Fragment7_DB.class).findAll();

        double salaries=0;

        for (int i=0;i<res4.size();i++)
            salaries=salaries+(Double.parseDouble(res4.get(i).getSalary()) + Double.parseDouble(res4.get(i).getEarnest()) + Double.parseDouble(res4.get(i).getInsurance_tax()));

        binding.salary.setText(Math.round(salaries)+"");

        //-----------------------------------------------------------------

        RealmResults<Fragment9_DB> res5 = realm.where(Fragment9_DB.class).findAll();

        double otherExpenses=0;

        for (int i=0;i<res5.size();i++)
            otherExpenses=otherExpenses+Double.parseDouble(res5.get(i).getPayment());

        binding.otherExpenses.setText(Math.round(otherExpenses)+"");

        //-----------------------------------------------------------------

        RealmResults<Fragment8_DB> res6 = realm.where(Fragment8_DB.class).findAll();

        double rawMaterial=0;

        for (int i=0;i<res6.size();i++)
            rawMaterial=rawMaterial+Double.parseDouble(res6.get(i).getPayment());

        binding.rawMaterial.setText(Math.round(rawMaterial)+"");
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        setData();
    }
}
