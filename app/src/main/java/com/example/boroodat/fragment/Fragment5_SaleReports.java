package com.example.boroodat.fragment;


import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.boroodat.R;
import com.example.boroodat.adapter.Fragment5_Adapter;
import com.example.boroodat.database.Fragment5_DB;
import com.example.boroodat.databinding.Fragment5Binding;
import com.example.boroodat.general.AppController;
import com.example.boroodat.general.NumberTextWatcherForThousand;
import com.example.boroodat.model.Fragment5_Model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dmax.dialog.SpotsDialog;
import io.realm.Realm;
import io.realm.RealmResults;

public class Fragment5_SaleReports extends Fragment
{

    private Fragment5Binding binding;
    private List<Fragment5_Model> models=new ArrayList<>();
    private Fragment5_Adapter adapter;
    private Realm realm;
    private String from;

    public Fragment5_SaleReports(String from)
    {
        this.from=from;
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
        binding = Fragment5Binding.inflate(inflater, container, false);
        View view = binding.getRoot();

        realm=Realm.getDefaultInstance();
        //-------------------------------------------------------------------------------------------------------

        ArrayList<String> searchItem=new ArrayList<>();
        searchItem.add("شماره فاکتور");
        searchItem.add("تاریخ");
        searchItem.add("مبلغ کل");

        ArrayAdapter<String> adp = new ArrayAdapter<String>(getContext(), R.layout.spinner_item, searchItem);
        binding.spinner.setAdapter(adp);
        binding.spinner.setSelection(1);

        //-------------------------------------------------------------------------------------------------------

        setAdapter();

        //-------------------------------------------------------------------------------------------------------

        binding.totalPrice.addTextChangedListener(new NumberTextWatcherForThousand(binding.totalPrice));
        binding.payment.addTextChangedListener(new NumberTextWatcherForThousand(binding.payment));
        binding.remain.addTextChangedListener(new NumberTextWatcherForThousand(binding.remain));

        //----------------------------------------------------------------------------------------------------------

        binding.lnr1.setVisibility(View.GONE);

        binding.fab.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                binding.fab.setVisibility(View.GONE);
                binding.lnr1.setVisibility(View.VISIBLE);
            }
        });


        //----------------------------------------------------------------------------------------------------------

        binding.searchView.setOnQueryTextListener(new android.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s)
            {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText)
            {
                newText = newText.toLowerCase();
                List<Fragment5_Model> newList = new ArrayList<>();
                double totalPrice=0;
                double payment1=0;

                for (int i=0;i<models.size();i++)
                {
                    if (binding.spinner.getSelectedItem().toString().equals("شماره فاکتور")
                            && models.get(i).getFactorNumber().toLowerCase().contains(newText))
                    {
                        newList.add(models.get(i));
                        totalPrice = totalPrice + Double.parseDouble(models.get(i).getSum());
                        payment1=payment1+Double.parseDouble(models.get(i).getPayment());
                    }

                    else if (binding.spinner.getSelectedItem().toString().equals("تاریخ")
                            && models.get(i).getDate().toLowerCase().contains(newText))
                    {
                        newList.add(models.get(i));
                        totalPrice = totalPrice + Double.parseDouble(models.get(i).getSum());
                        payment1=payment1+Double.parseDouble(models.get(i).getPayment());
                    }

                    else if (binding.spinner.getSelectedItem().toString().equals("مبلغ کل")
                            && models.get(i).getSum().toLowerCase().contains(newText))
                    {
                        newList.add(models.get(i));
                        totalPrice = totalPrice + Double.parseDouble(models.get(i).getSum());
                        payment1=payment1+Double.parseDouble(models.get(i).getPayment());
                    }

                }


                adapter.setFilter(newList);
                binding.totalPrice.setText(Math.round(totalPrice)+"");
                binding.payment.setText(Math.round(payment1)+"");
                binding.remain.setText(Math.round(totalPrice-payment1)+"");

                return true;
            }
        });

        //----------------------------------------------------------------------------------------------------------

        binding.searchView.setOnCloseListener(new SearchView.OnCloseListener()
        {
            @Override
            public boolean onClose()
            {
                adapter.setFilter(models);
                binding.lnr1.setVisibility(View.GONE);
                binding.fab.setVisibility(View.VISIBLE);
                return true;
            }
        });

        //----------------------------------------------------------------------------------------------------------

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

    public void setAdapter()
    {
        adapter = new Fragment5_Adapter(models, getContext(),binding.totalPrice,binding.payment,binding.remain,from );
        binding.recyclerView.setLayoutManager ( new LinearLayoutManager( getContext() ) );
        binding.recyclerView.setAdapter (adapter);


        RealmResults<Fragment5_DB> res = realm.where(Fragment5_DB.class).findAll();

        models.clear();
        double tPrice=0;
        double payment1=0;

        for (int i=res.size()-1;i>=0;i--)
        {
            int id=res.get(i).getId();
            String factorNumber=res.get(i).getFactor_number();
            String date=res.get(i).getDate();
            String sum=res.get(i).getSum();
            String payment=res.get(i).getPayment();
            String account_id=res.get(i).getAccount_id();
            String buyer_id=res.get(i).getBuyer_id();

            models.add(new Fragment5_Model(id,factorNumber,date,sum,payment,account_id,buyer_id));

            tPrice=tPrice+Double.parseDouble(sum);
            payment1=payment1+Double.parseDouble(payment);
        }

        adapter.notifyDataSetChanged();
        binding.totalPrice.setText(Math.round(tPrice)+"");
        binding.payment.setText(Math.round(payment1)+"");
        binding.remain.setText(Math.round(tPrice-payment1)+"");

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

        setAdapter();
    }
}
