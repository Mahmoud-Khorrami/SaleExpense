package com.example.boroodat.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class Activity2Fragments extends Fragment
{
    private int frgNum;

    public static Activity2Fragments newInstance(int num)
    {
        Activity2Fragments activity2Fragments = new Activity2Fragments ();
        Bundle args = new Bundle ();

        args.putInt ( "num", num );
        activity2Fragments.setArguments ( args );
        return activity2Fragments;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        if (getArguments () != null)
        {
            frgNum = getArguments ().getInt ( "num" );
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {

        if (frgNum == 0)
            return new Fragment0(getContext()).getView(inflater, container, savedInstanceState);

        else if (frgNum == 1)
            return new Fragment1(getContext()).getView(inflater, container, savedInstanceState);

        else if (frgNum == 2)
            return new Fragment2(getContext()).getView(inflater, container, savedInstanceState);

        else
        {
            return new Fragment3(getContext()).getView(inflater, container, savedInstanceState);
        }
    }
}
