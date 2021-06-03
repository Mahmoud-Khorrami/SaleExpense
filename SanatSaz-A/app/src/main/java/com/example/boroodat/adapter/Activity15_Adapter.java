package com.example.boroodat.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.boroodat.fragment.Activity15Fragments;

public class Activity15_Adapter extends FragmentStatePagerAdapter
{
    private Activity15Fragments activity15Fragments;
    private int frgNum;
    private String[] title;
    private String from;

    public Activity15_Adapter(@NonNull FragmentManager fm, Activity15Fragments activity15Fragments, int frgNum, String[] title, String from)
    {
        super(fm);
        this.activity15Fragments = activity15Fragments;
        this.frgNum = frgNum;
        this.title =title;
        this.from = from;
    }

    @NonNull
    @Override
    public Fragment getItem(int position)
    {
        return activity15Fragments.newInstance(position,frgNum,from);
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
