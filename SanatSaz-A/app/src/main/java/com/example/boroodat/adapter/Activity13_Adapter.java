package com.example.boroodat.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.boroodat.fragment.Activity13Fragments;
import com.example.boroodat.fragment.Activity3Fragments;

public class Activity13_Adapter extends FragmentStatePagerAdapter
{
    private Activity13Fragments activity13Fragments;
    private int frgNum;
    private String[] title;

    public Activity13_Adapter(@NonNull FragmentManager fm, Activity13Fragments activity13Fragments, int frgNum, String[] title)
    {
        super(fm);
        this.activity13Fragments = activity13Fragments;
        this.frgNum = frgNum;
        this.title =title;
    }

    @NonNull
    @Override
    public Fragment getItem(int position)
    {
        return activity13Fragments.newInstance(position);
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
