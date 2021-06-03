package com.example.boroodat.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.boroodat.fragment.Activity2Fragments;


public class Activity2_FragmentsAdapter extends FragmentStatePagerAdapter
{
    private Activity2Fragments activity2Fragments;
    private int frgNum;

    public Activity2_FragmentsAdapter(@NonNull FragmentManager fm, Activity2Fragments activity2Fragments, int frgNum)
    {
        super(fm);
        this.activity2Fragments = activity2Fragments;
        this.frgNum = frgNum;
    }

    @NonNull
    @Override
    public Fragment getItem(int position)
    {
        return activity2Fragments.newInstance(position);
    }

    @Override
    public int getCount()
    {
        return frgNum;
    }

}
