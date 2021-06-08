package com.example.boroodat.model.activity12;

import org.json.JSONArray;

public class Activity12_FragmentMainModel extends Activity12_FragmentParentModel
{
    private String id;

    private String factorNumber, date, sum,payment,account_id,account_title,description;
    private JSONArray details;

    public Activity12_FragmentMainModel(String id, String factorNumber, String date, String sum, String payment, String account_id, String account_title, String description, JSONArray details)
    {
        super(Activity12_FragmentParentModel.Main);
        this.id = id;
        this.factorNumber = factorNumber;
        this.date = date;
        this.sum = sum;
        this.payment = payment;
        this.account_id = account_id;
        this.account_title = account_title;
        this.description = description;
        this.details = details;
    }

    public String getAccount_title()
    {
        return account_title;
    }

    public void setAccount_title(String account_title)
    {
        this.account_title = account_title;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public JSONArray getDetails()
    {
        return details;
    }

    public void setDetails(JSONArray details)
    {
        this.details = details;
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

    public String getId()
    {
        return id;
    }

    public void setId(String id)
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
