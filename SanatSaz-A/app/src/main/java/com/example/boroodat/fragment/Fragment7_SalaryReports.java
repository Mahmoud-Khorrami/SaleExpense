package com.example.boroodat.fragment;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.boroodat.R;
import com.example.boroodat.adapter.Fragment7_Adapter;
import com.example.boroodat.database.Fragment7_DB;
import com.example.boroodat.databinding.Fragment5Binding;
import com.example.boroodat.databinding.Fragment7Binding;
import com.example.boroodat.general.NumberTextWatcherForThousand;
import com.example.boroodat.model.Fragment7_Model;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

public class Fragment7_SalaryReports extends Fragment
{

    private Fragment7Binding binding;
    private List<Fragment7_Model> models=new ArrayList<>();
    private Fragment7_Adapter adapter;
    private Realm realm;

    public Fragment7_SalaryReports()
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
        binding = Fragment7Binding.inflate(inflater, container, false);
        View view = binding.getRoot();

        realm=Realm.getDefaultInstance();
        //-------------------------------------------------------------------------------------------------------

        ArrayList<String> searchItem=new ArrayList<>();
        searchItem.add("نام و نام خانوادگی");
        searchItem.add("تاریخ");

        ArrayAdapter<String> adp = new ArrayAdapter<String>(getContext(), R.layout.spinner_item, searchItem);
        binding.spinner.setAdapter(adp);
        binding.spinner.setSelection(1);

        //-------------------------------------------------------------------------------------------------------

        setAdapter();

        //-------------------------------------------------------------------------------------------------------

        binding.salary.addTextChangedListener(new NumberTextWatcherForThousand(binding.salary));
        binding.earnest.addTextChangedListener(new NumberTextWatcherForThousand(binding.earnest));
        binding.insuranceTax.addTextChangedListener(new NumberTextWatcherForThousand(binding.insuranceTax));
        binding.sum.addTextChangedListener(new NumberTextWatcherForThousand(binding.sum));

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

        binding.searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s)
            {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText)
            {
                newText = newText.toLowerCase();
                List<Fragment7_Model> newList = new ArrayList<>();
                double salary1=0;
                double earnest1=0;
                double insurance_tax1=0;

                for (int i=0;i<models.size();i++)
                {
                    if (binding.spinner.getSelectedItem().toString().equals("تاریخ")
                            && models.get(i).getDate().toLowerCase().contains(newText))
                    {
                        newList.add(models.get(i));
                        salary1 = salary1 + Double.parseDouble(models.get(i).getSalary());
                        earnest1 = earnest1 + Double.parseDouble(models.get(i).getEarnest());
                        insurance_tax1 = insurance_tax1 + Double.parseDouble(models.get(i).getInsurance_tax());
                    }

                    else if (binding.spinner.getSelectedItem().toString().equals("نام و نام خانوادگی")
                            && models.get(i).getName().toLowerCase().contains(newText))
                    {
                        newList.add(models.get(i));
                        salary1 = salary1 + Double.parseDouble(models.get(i).getSalary());
                        earnest1 = earnest1 + Double.parseDouble(models.get(i).getEarnest());
                        insurance_tax1 = insurance_tax1 + Double.parseDouble(models.get(i).getInsurance_tax());
                    }

                }

                adapter.setFilter(newList);
                binding.salary.setText(Math.round(salary1)+"");
                binding.earnest.setText(Math.round(earnest1)+"");
                binding.insuranceTax.setText(Math.round(insurance_tax1)+"");
                binding.sum.setText(Math.round(salary1+earnest1+insurance_tax1)+"");
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
        adapter = new Fragment7_Adapter(getContext(),models,binding.salary,binding.earnest,binding.insuranceTax,binding.sum );
        binding.recyclerView.setLayoutManager ( new LinearLayoutManager( getContext() ) );
        binding.recyclerView.setAdapter (adapter);

        RealmResults<Fragment7_DB> res = realm.where(Fragment7_DB.class).findAll();

        models.clear();

        double salary1=0;
        double earnest1=0;
        double insurance_tax1=0;

        for (int i=res.size()-1;i>=0;i--)
        {
            int id=res.get(i).getId();
            String name=res.get(i).getName();
            String personnel_id=res.get(i).getPersonnel_id();
            String salary=res.get(i).getSalary();
            String earnest=res.get(i).getEarnest();
            String insurance_tax=res.get(i).getInsurance_tax();
            String account_id=res.get(i).getAccount_id();
            String account_title=res.get(i).getAccount_title();
            String date=res.get(i).getDate();
            String description=res.get(i).getDescription();

            models.add(new Fragment7_Model(id,name,personnel_id,salary,earnest,insurance_tax,account_id,account_title,date,description));

            salary1 = salary1 + Double.parseDouble(salary);
            earnest1 = earnest1 + Double.parseDouble(earnest);
            insurance_tax1 = insurance_tax1 + Double.parseDouble(insurance_tax);
        }

        adapter.notifyDataSetChanged();
        binding.salary.setText(Math.round(salary1)+"");
        binding.earnest.setText(Math.round(earnest1)+"");
        binding.insuranceTax.setText(Math.round(insurance_tax1)+"");
        binding.sum.setText(Math.round(salary1+earnest1+insurance_tax1)+"");

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
    }
}
