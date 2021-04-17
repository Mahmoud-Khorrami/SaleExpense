package com.example.boroodat.database;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class SaleDetail_DB extends RealmObject
{
    @PrimaryKey
    private int id;

    private String description;

    public SaleDetail_DB()
    {
    }

    public SaleDetail_DB(int id, String description)
    {
        this.id = id;
        this.description = description;
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
}
