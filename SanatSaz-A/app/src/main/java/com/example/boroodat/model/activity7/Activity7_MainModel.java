package com.example.boroodat.model.activity7;

public class Activity7_MainModel extends Activity7_ParentModel
{
    private String id;
    private String title,accountNumber,balance;

    public Activity7_MainModel(String id, String title, String accountNumber, String balance)
    {
        super(Activity7_ParentModel.Main);
        this.id = id;
        this.title = title;
        this.accountNumber = accountNumber;
        this.balance = balance;
    }

    public String getId()
    {
        return id;
    }

    public void setId(String id)
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
