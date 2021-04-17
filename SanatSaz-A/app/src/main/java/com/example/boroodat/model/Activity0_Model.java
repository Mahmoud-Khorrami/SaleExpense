package com.example.boroodat.model;

public class Activity0_Model
{
    private int id;
    private String name,date,account;

    public Activity0_Model(int id, String name, String date, String account)
    {
        this.id = id;
        this.name = name;
        this.date = date;
        this.account = account;
    }

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getDate()
    {
        return date;
    }

    public void setDate(String date)
    {
        this.date = date;
    }

    public String getAccount()
    {
        return account;
    }

    public void setAccount(String account)
    {
        this.account = account;
    }
}
