package com.example.boroodat.model;

public class Activity4_Model
{
    private int id;
    private String name,phone,role;

    public Activity4_Model(int id, String name, String phone, String role)
    {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.role = role;
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

    public String getPhone()
    {
        return phone;
    }

    public void setPhone(String phone)
    {
        this.phone = phone;
    }

    public String getRole()
    {
        return role;
    }

    public void setRole(String role)
    {
        this.role = role;
    }
}
