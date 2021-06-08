package com.example.boroodat.model.activity4;

public class Activity4_ParentModel
{
    public static final int Main = 106;
    public static final int Loading = 206;
    public static final int Retry = 306;
    public static final int NotFound = 406;


    private int currentType;

    public Activity4_ParentModel(int currentType)
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
