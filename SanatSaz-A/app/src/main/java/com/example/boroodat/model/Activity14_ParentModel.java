package com.example.boroodat.model;

public class Activity14_ParentModel
{
    public static final int Main = 106;
    public static final int Loading = 206;
    public static final int Retry = 306;
    public static final int NotFound = 406;


    private int currentType;

    public Activity14_ParentModel(int currentType)
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
