package com.example.boroodat.database;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Activity8_DB extends RealmObject
{
    @PrimaryKey
    private int id;

    private String name,phone_number,destination;

    public Activity8_DB()
    {
    }

    public Activity8_DB(int id, String name, String phone_number, String destination)
    {
        this.id = id;
        this.name = name;
        this.phone_number = phone_number;
        this.destination = destination;
    }

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getPhone_number()
    {
        return phone_number;
    }

    public void setPhone_number(String phone_number)
    {
        this.phone_number = phone_number;
    }

    public String getDestination()
    {
        return destination;
    }

    public void setDestination(String destination)
    {
        this.destination = destination;
    }
}
