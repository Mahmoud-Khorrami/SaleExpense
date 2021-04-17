package com.example.boroodat.database;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Fragment9_DB extends RealmObject
{
    @PrimaryKey
    private int id;

    private String factor_number,date,sum,payment,account_id;

    public Fragment9_DB()
    {
    }

    public Fragment9_DB(int id, String factor_number, String date, String sum, String payment, String account_id)
    {
        this.id = id;
        this.factor_number = factor_number;
        this.date = date;
        this.sum = sum;
        this.payment = payment;
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

    public String getPayment()
    {
        return payment;
    }

    public void setPayment(String payment)
    {
        this.payment = payment;
    }

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public String getFactor_number()
    {
        return factor_number;
    }

    public void setFactor_number(String factor_number)
    {
        this.factor_number = factor_number;
    }

    public String getDate()
    {
        return date;
    }

    public void setDate(String date)
    {
        this.date = date;
    }

    public String getSum()
    {
        return sum;
    }

    public void setSum(String sum)
    {
        this.sum = sum;
    }
}
