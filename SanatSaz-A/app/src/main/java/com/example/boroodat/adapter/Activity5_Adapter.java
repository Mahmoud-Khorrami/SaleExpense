package com.example.boroodat.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.boroodat.fragment.Activity2Fragments;
import com.example.boroodat.fragment.Activity5Fragments;


public class Activity5_Adapter extends FragmentStatePagerAdapter
{
    private Activity5Fragments activity5Fragments;
    private int frgNum;
    private String[] title;
    private String from;

    public Activity5_Adapter(@NonNull FragmentManager fm, Activity5Fragments activity5Fragments, int frgNum, String[] title,String from)
    {
        super(fm);
        this.activity5Fragments = activity5Fragments;
        this.frgNum = frgNum;
        this.title =title;
        this.from = from;
    }

    @NonNull
    @Override
    public Fragment getItem(int position)
    {
        return activity5Fragments.newInstance(position,frgNum,from);
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
