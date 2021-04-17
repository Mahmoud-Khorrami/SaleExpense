package com.example.boroodat.model;

public class Activity16_Model
{
    private int id;

    private String row,description,number,unitPrice,totalPrice;

    public Activity16_Model(int id,String row, String description, String number, String unitPrice, String totalPrice)
    {
        this.id = id;
        this.row = row;
        this.description = description;
        this.number = number;
        this.unitPrice = unitPrice;
        this.totalPrice = totalPrice;
    }

    public String getRow()
    {
        return row;
    }

    public void setRow(String row)
    {
        this.row = row;
    }

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public String getNumber()
    {
        return number;
    }

    public void setNumber(String number)
    {
        this.number = number;
    }

    public String getUnitPrice()
    {
        return unitPrice;
    }

    public void setUnitPrice(String unitPrice)
    {
        this.unitPrice = unitPrice;
    }

    public String getTotalPrice()
    {
        return totalPrice;
    }

    public void setTotalPrice(String totalPrice)
    {
        this.totalPrice = totalPrice;
    }
}
