package com.example.boroodat.model;

public class Activity9_Model
{
    private int id;
    private String name,phone_number,car_type,number_plate,archive;
    private boolean isSelected = false;

    public Activity9_Model(int id, String name, String phone_number, String car_type, String number_plate,String archive)
    {
        this.id = id;
        this.name = name;
        this.phone_number = phone_number;
        this.car_type = car_type;
        this.number_plate = number_plate;
        this.archive=archive;
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

    public String getCar_type()
    {
        return car_type;
    }

    public void setCar_type(String car_type)
    {
        this.car_type = car_type;
    }

    public String getNumber_plate()
    {
        return number_plate;
    }

    public void setNumber_plate(String number_plate)
    {
        this.number_plate = number_plate;
    }

    public boolean isSelected()
    {
        return isSelected;
    }

    public void setSelected(boolean selected)
    {
        isSelected = selected;
    }

    public String getArchive()
    {
        return archive;
    }

    public void setArchive(String archive)
    {
        this.archive = archive;
    }
}
