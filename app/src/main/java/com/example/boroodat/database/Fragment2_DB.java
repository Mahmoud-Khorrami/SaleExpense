package com.example.boroodat.database;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Fragment2_DB extends RealmObject
{
    @PrimaryKey
    private int id;

    private String date,balance,basket_number,total_price,withdrawAmount,depositAmount;

    public Fragment2_DB()
    {
    }

    public Fragment2_DB(int id, String date, String balance, String basket_number, String total_price, String withdrawAmount, String depositAmount)
    {
        this.id = id;
        this.date = date;
        this.balance = balance;
        this.basket_number = basket_number;
        this.total_price = total_price;
        this.withdrawAmount = withdrawAmount;
        this.depositAmount = depositAmount;
    }

    public String getDate()
    {
        return date;
    }

    public void setDate(String date)
    {
        this.date = date;
    }

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public String getBalance()
    {
        return balance;
    }

    public void setBalance(String balance)
    {
        this.balance = balance;
    }

    public String getBasket_number()
    {
        return basket_number;
    }

    public void setBasket_number(String basket_number)
    {
        this.basket_number = basket_number;
    }

    public String getTotal_price()
    {
        return total_price;
    }

    public void setTotal_price(String total_price)
    {
        this.total_price = total_price;
    }

    public String getWithdrawAmount()
    {
        return withdrawAmount;
    }

    public void setWithdrawAmount(String withdrawAmount)
    {
        this.withdrawAmount = withdrawAmount;
    }

    public String getDepositAmount()
    {
        return depositAmount;
    }

    public void setDepositAmount(String depositAmount)
    {
        this.depositAmount = depositAmount;
    }
}
