package com.example.boroodat.model;

public class Activity7_Model
{
    private int id;
    private String title,accountNumber,balance;

    public Activity7_Model(int id, String title, String accountNumber, String balance)
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
