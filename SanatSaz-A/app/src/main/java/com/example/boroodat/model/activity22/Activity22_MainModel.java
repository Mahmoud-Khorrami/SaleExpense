package com.example.boroodat.model.activity22;


public class Activity22_MainModel extends Activity22_ParentModel
{
    private String id,name;

    public Activity22_MainModel(String id, String name)
    {
        super(Activity22_ParentModel.Main);
        this.id = id;
        this.name = name;
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
}
