package com.example.boroodat.model.activity8;

public class Activity8_MainModel extends Activity8_ParentModel
{
    private String id;
    private String name,phone_number,destination,archive;
    private boolean isSelected = false;

    public Activity8_MainModel(String id, String name, String phone_number, String destination, String archive)
    {
        super(Activity8_ParentModel.Main);
        this.id = id;
        this.name = name;
        this.phone_number = phone_number;
        this.destination = destination;
        this.archive=archive;
    }

    public String getId()
    {
        return id;
    }

    public void setId(String id)
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

    public String getArchive()
    {
        return archive;
    }

    public void setArchive(String archive)
    {
        this.archive = archive;
    }

    public boolean isSelected()
    {
        return isSelected;
    }

    public void setSelected(boolean selected)
    {
        isSelected = selected;
    }
}
