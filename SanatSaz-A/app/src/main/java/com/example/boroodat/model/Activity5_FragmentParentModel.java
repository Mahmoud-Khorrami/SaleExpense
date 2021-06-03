package com.example.boroodat.model;

public class Activity5_FragmentParentModel
{
    public static final int Main = 106;
    public static final int Loading = 206;
    public static final int Retry = 306;
    public static final int NotFound = 406;


    private int currentType;

    public Activity5_FragmentParentModel(int currentType)
    {
        this.currentType = currentType;
    }

    public int getCurrentType()
    {
        return currentType;
    }

    public void setCurrentType(int currentType)
    {
        this.currentType = currentType;
    }
}
