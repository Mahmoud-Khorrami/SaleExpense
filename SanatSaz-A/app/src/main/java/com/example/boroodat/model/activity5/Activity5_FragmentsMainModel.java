package com.example.boroodat.model.activity5;

import org.json.JSONArray;

public class Activity5_FragmentsMainModel extends Activity5_FragmentParentModel
{
    private String id;

    private String factorNumber, date, sum,payment,account_id,account_title,buyer_id,buyer_name,driver_id,driver_name,description,detail;
    private JSONArray sale_details;

    public Activity5_FragmentsMainModel(String id, String factorNumber, String date, String sum, String payment, String account_id, String account_title, String buyer_id, String buyer_name, String driver_id, String driver_name, String description, String detail, JSONArray sale_details)
    {
        super(Activity5_FragmentParentModel.Main);
        this.id = id;
        this.factorNumber = factorNumber;
        this.date = date;
        this.sum = sum;
        this.payment = payment;
        this.account_id = account_id;
        this.account_title = account_title;
        this.buyer_id = buyer_id;
        this.buyer_name = buyer_name;
        this.driver_id = driver_id;
        this.driver_name = driver_name;
        this.description = description;
        this.detail = detail;
        this.sale_details = sale_details;
    }

    public String getAccount_id()
    {
        return account_id;
    }

    public void setAccount_id(String account_id)
    {
        this.account_id = account_id;
    }

    public String getBuyer_id()
    {
        return buyer_id;
    }

    public void setBuyer_id(String buyer_id)
    {
        this.buyer_id = buyer_id;
    }

    public String getDriver_id()
    {
        return driver_id;
    }

    public void setDriver_id(String driver_id)
    {
        this.driver_id = driver_id;
    }

    public String getDetail()
    {
        return detail;
    }

    public void setDetail(String detail)
    {
        this.detail = detail;
    }

    public JSONArray getSale_details()
    {
        return sale_details;
    }

    public void setSale_details(JSONArray sale_details)
    {
        this.sale_details = sale_details;
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

    public String getPayment()
    {
        return payment;
    }

    public void setPayment(String payment)
    {
        this.payment = payment;
    }

    public String getAccount_title()
    {
        return account_title;
    }

    public void setAccount_title(String account_title)
    {
        this.account_title = account_title;
    }

    public String getBuyer_name()
    {
        return buyer_name;
    }

    public void setBuyer_name(String buyer_name)
    {
        this.buyer_name = buyer_name;
    }

    public String getDriver_name()
    {
        return driver_name;
    }

    public void setDriver_name(String driver_name)
    {
        this.driver_name = driver_name;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }
}
