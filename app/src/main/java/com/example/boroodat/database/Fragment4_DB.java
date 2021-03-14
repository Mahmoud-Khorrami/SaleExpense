package com.example.boroodat.database;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Fragment4_DB extends RealmObject
{
    @PrimaryKey
    private int id;

    private String title,amount,date,account_id;

    public Fragment4_DB()
    {
    }

    public Fragment4_DB(int id, String title, String amount, String date, String account_id)
    {
        this.id = id;
        this.title = title;
        this.amount = amount;
        this.date = date;
        this.account_id = account_id;
    }

    public String getAccount_id()
    {
        return account_id;
    }

    public void setAccount_id(String account_id)
    {
        this.account_id = account_id;
    }

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public String getAmount()
    {
        return amount;
    }

    public void setAmount(String amount)
    {
        this.amount = amount;
    }

    public String getDate()
    {
        return date;
    }

    public void setDate(String date)
    {
        this.date = date;
    }
}
