package com.example.boroodat.database;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Activity7_DB extends RealmObject
{
    @PrimaryKey
    private int id;

    private String title,accountNumber,balance;

    public Activity7_DB()
    {
    }

    public Activity7_DB(int id, String title, String accountNumber, String balance)
    {
        this.id = id;
        this.title = title;
        this.accountNumber = accountNumber;
        this.balance = balance;
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

    public String getAccountNumber()
    {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber)
    {
        this.accountNumber = accountNumber;
    }

    public String getBalance()
    {
        return balance;
    }

    public void setBalance(String balance)
    {
        this.balance = balance;
    }
}
