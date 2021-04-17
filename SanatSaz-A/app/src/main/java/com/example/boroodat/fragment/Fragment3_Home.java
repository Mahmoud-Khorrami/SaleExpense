package com.example.boroodat.fragment;


import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.PointF;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.example.boroodat.database.Report_DB;
import com.example.boroodat.databinding.Fragment3Binding;
import com.example.boroodat.general.DecoViewChart;
import com.example.boroodat.general.NumberTextWatcherForThousand;
import com.hookedonplay.decoviewlib.charts.EdgeDetail;
import com.hookedonplay.decoviewlib.charts.SeriesItem;
import com.hookedonplay.decoviewlib.events.DecoEvent;

import io.realm.Realm;
import io.realm.RealmResults;

public class Fragment3_Home extends Fragment
{

    private Fragment3Binding binding;
    private Realm realm;

    public Fragment3_Home()
    {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        binding = Fragment3Binding.inflate(inflater, container, false);
        View view = binding.getRoot();

        realm=Realm.getDefaultInstance();
        //-------------------------------------------------------------------------------------------------------

        binding.rFund.addTextChangedListener(new NumberTextWatcherForThousand(binding.rFund));
        binding.rBalance.addTextChangedListener(new NumberTextWatcherForThousand(binding.rBalance));
        binding.rDemand.addTextChangedListener(new NumberTextWatcherForThousand(binding.rDemand));
        binding.rDept.addTextChangedListener(new NumberTextWatcherForThousand(binding.rDept));
        binding.rIncom.addTextChangedListener(new NumberTextWatcherForThousand(binding.rIncom));
        binding.rCost.addTextChangedListener(new NumberTextWatcherForThousand(binding.rCost));

        setData();

        //-------------------------------------------------------------------------------------------------------

        return view;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser)
    {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser)
        {
            getFragmentManager().beginTransaction().detach(this).attach(this).commit();
        }
    }


    public void setData()
    {
        RealmResults<Report_DB> results=realm.where(Report_DB.class).findAll();

        String salary=results.get(0).getSalary();
        String material=results.get(0).getMaterial();
        String material_payment=results.get(0).getMaterial_payment();
        String expense=results.get(0).getExpense();
        String expense_payment=results.get(0).getExpense_payment();
        String sale=results.get(0).getSale();
        String sale_payment=results.get(0).getSale_payment();
        String deposit=results.get(0).getDeposit();

        double d1=Double.parseDouble(sale) + Double.parseDouble(deposit) - Double.parseDouble(expense) - Double.parseDouble(material) - Double.parseDouble(salary);
        binding.rFund.setText(Math.round(d1)+"");

        double d2=Double.parseDouble(sale_payment) + Double.parseDouble(deposit) - Double.parseDouble(expense_payment) - Double.parseDouble(material_payment) - Double.parseDouble(salary);
        binding.rBalance.setText(Math.round(d2)+"");

        double d3=Double.parseDouble(sale)-Double.parseDouble(sale_payment);
        binding.rDemand.setText(Math.round(d3)+"");

        double d4 = Double.parseDouble(expense) - Double.parseDouble(expense_payment) + Double.parseDouble(material)-Double.parseDouble(material_payment);
        binding.rDept.setText(Math.round(d4)+"");

        double d5=Double.parseDouble(sale) + Double.parseDouble(deposit);
        binding.rIncom.setText(Math.round(d5)+"");

        double d6 = Double.parseDouble(expense_payment) + Double.parseDouble(material_payment) + Double.parseDouble(salary);
        binding.rCost.setText(Math.round(d6)+"");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onResume()
    {
        super.onResume();

        realm=Realm.getDefaultInstance();
        setData();
    }

}
