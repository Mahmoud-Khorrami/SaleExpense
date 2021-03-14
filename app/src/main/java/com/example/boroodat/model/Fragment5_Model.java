package com.example.boroodat.model;

public class Fragment5_Model
{
    private int id;

    private String factorNumber, date, sum,payment,account_id;

    public Fragment5_Model(int id, String factorNumber, String date, String sum, String payment, String account_id)
    {
        this.id = id;
        this.factorNumber = factorNumber;
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

    public String getFactorNumber()
    {
        return factorNumber;
    }

    public void setFactorNumber(String factorNumber)
    {
        this.factorNumber = factorNumber;
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
