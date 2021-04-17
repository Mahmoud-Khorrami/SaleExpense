package com.example.boroodat.fragment;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.boroodat.activity.Activity14_Personnel;
import com.example.boroodat.activity.Activity4_Users;
import com.example.boroodat.activity.Activity7_Account;
import com.example.boroodat.activity.Activity8_Buyer;
import com.example.boroodat.activity.Activity9_Driver;
import com.example.boroodat.databinding.Fragment0Binding;
import com.example.boroodat.general.User_Info;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import io.realm.Realm;

public class Fragment0_Setting extends Fragment
{

    private Fragment0Binding binding;
    private ProgressDialog progressDialog;

    public Fragment0_Setting()
    {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        binding = Fragment0Binding.inflate(inflater, container, false);
        View view = binding.getRoot();


        //-------------------------------------------------------------------------------------------------------

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);

        //-------------------------------------------------------------------------------------------------------

        binding.users.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent=new Intent(getContext(), Activity4_Users.class);
                intent.putExtra("token",new User_Info().token());
                intent.putExtra("company_id",new User_Info().company_id());
                startActivity(intent);
            }
        });

        //-------------------------------------------------------------------------------------------------------

        binding.accounts.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent=new Intent(getContext(), Activity7_Account.class);
                intent.putExtra("token",new User_Info().token());
                intent.putExtra("company_id",new User_Info().company_id());
                startActivity(intent);
            }
        });

        //-------------------------------------------------------------------------------------------------------

        binding.buyers.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent=new Intent(getContext(), Activity8_Buyer.class);
                intent.putExtra("token",new User_Info().token());
                intent.putExtra("company_id",new User_Info().company_id());
                startActivity(intent);
            }
        });

        //-------------------------------------------------------------------------------------------------------

        binding.drivers.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent=new Intent(getContext(), Activity9_Driver.class);
                intent.putExtra("token",new User_Info().token());
                intent.putExtra("company_id",new User_Info().company_id());
                startActivity(intent);
            }
        });

        //-------------------------------------------------------------------------------------------------------

        binding.personnel.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent=new Intent(getContext(), Activity14_Personnel.class);
                intent.putExtra("token",new User_Info().token());
                intent.putExtra("company_id",new User_Info().company_id());
                startActivity(intent);
            }
        });

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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onResume()
    {
        super.onResume();

//        usersOfFund();
    }
}
