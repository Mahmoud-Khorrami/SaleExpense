package com.example.boroodat.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.boroodat.fragment.Activity12Fragments;
import com.example.boroodat.fragment.Activity3Fragments;

public class Activity3_Adapter extends FragmentStatePagerAdapter
{
    private Activity3Fragments activity3Fragments;
    private int frgNum;
    private String[] title;

    public Activity3_Adapter(@NonNull FragmentManager fm, Activity3Fragments activity3Fragments, int frgNum, String[] title)
    {
        super(fm);
        this.activity3Fragments = activity3Fragments;
        this.frgNum = frgNum;
        this.title =title;
    }

    @NonNull
    @Override
    public Fragment getItem(int position)
    {
        return activity3Fragments.newInstance(position);
    }

    @Override
    public int getCount()
    {
        return frgNum;
    }

    public CharSequence getPageTitle(int position)
    {
        return title[position] ;
    }
}
