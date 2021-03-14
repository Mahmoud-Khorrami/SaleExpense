package com.example.boroodat.model;

public class Activity14_Model
{
    private int id;

    private String name,phone_number,register_date,role,credit_card,exit_date;

    public Activity14_Model(int id, String name, String phone_number, String register_date, String role, String credit_card, String exit_date)
    {
        this.id = id;
        this.name = name;
        this.phone_number = phone_number;
        this.register_date = register_date;
        this.role = role;
        this.credit_card = credit_card;
        this.exit_date = exit_date;
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

    public String getRegister_date()
    {
        return register_date;
    }

    public void setRegister_date(String register_date)
    {
        this.register_date = register_date;
    }

    public String getRole()
    {
        return role;
    }

    public void setRole(String role)
    {
        this.role = role;
    }

    public String getCredit_card()
    {
        return credit_card;
    }

    public void setCredit_card(String credit_card)
    {
        this.credit_card = credit_card;
    }

    public String getExit_date()
    {
        return exit_date;
    }

    public void setExit_date(String exit_date)
    {
        this.exit_date = exit_date;
    }
}
