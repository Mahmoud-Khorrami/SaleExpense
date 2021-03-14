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
import com.example.boroodat.adapter.Fragment4_Adapter;
import com.example.boroodat.database.Fragment4_DB;
import com.example.boroodat.databinding.Fragment4Binding;
import com.example.boroodat.general.NumberTextWatcherForThousand;
import com.example.boroodat.model.Fragment4_Model;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

public class Fragment4_DepositReports extends Fragment
{

    private Fragment4Binding binding;
    private List<Fragment4_Model> models=new ArrayList<>();
    private Fragment4_Adapter adapter;
    private Realm realm;

    public Fragment4_DepositReports()
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
        binding = Fragment4Binding.inflate(inflater, container, false);
        View view = binding.getRoot();

        realm=Realm.getDefaultInstance();
        //-------------------------------------------------------------------------------------------------------

        ArrayList<String> searchItem=new ArrayList<>();
        searchItem.add("عنوان");
        searchItem.add("مبلغ");
        searchItem.add("تاریخ");

        ArrayAdapter<String> adp = new ArrayAdapter<String>(getContext(), R.layout.spinner_item, searchItem);
        binding.spinner.setAdapter(adp);
        binding.spinner.setSelection(1);

        //-------------------------------------------------------------------------------------------------------

        setAdapter();

        //-------------------------------------------------------------------------------------------------------

        binding.totalPrice.addTextChangedListener(new NumberTextWatcherForThousand(binding.totalPrice));

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
                List<Fragment4_Model> newList = new ArrayList<>();
                double totalPrice=0;

                for (int i=0;i<models.size();i++)
                {
                    if (binding.spinner.getSelectedItem().toString().equals("تاریخ")
                            && models.get(i).getDate().toLowerCase().contains(newText))
                    {
                        newList.add(models.get(i));
                        totalPrice = totalPrice + Double.parseDouble(models.get(i).getAmount());
                    }

                    else if (binding.spinner.getSelectedItem().toString().equals("مبلغ")
                            && models.get(i).getAmount().toLowerCase().contains(newText))
                    {
                        newList.add(models.get(i));
                        totalPrice = totalPrice + Double.parseDouble(models.get(i).getAmount());
                    }
                    else if (binding.spinner.getSelectedItem().toString().equals("عنوان")
                            && models.get(i).getTitle().toLowerCase().contains(newText))
                    {
                        newList.add(models.get(i));
                        totalPrice = totalPrice + Double.parseDouble(models.get(i).getAmount());
                    }
                }

                adapter.setFilter(newList);
                binding.totalPrice.setText(Math.round(totalPrice)+"");
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
        adapter = new Fragment4_Adapter(models, getContext(),binding.totalPrice );
        binding.recyclerView.setLayoutManager ( new LinearLayoutManager( getContext() ) );
        binding.recyclerView.setAdapter (adapter);

        RealmResults<Fragment4_DB> res = realm.where(Fragment4_DB.class).findAll();

        models.clear();
        double tPrice=0;

        for (int i=res.size()-1;i>=0;i--)
        {
            int id=res.get(i).getId();
            String title=res.get(i).getTitle();
            String amount=res.get(i).getAmount();
            String date=res.get(i).getDate();
            String account_id=res.get(i).getAccount_id();

            models.add(new Fragment4_Model(id,title,amount,date,account_id));

            tPrice=tPrice+Double.parseDouble(amount);
        }

        adapter.notifyDataSetChanged();
        binding.totalPrice.setText(Math.round(tPrice)+"");

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
