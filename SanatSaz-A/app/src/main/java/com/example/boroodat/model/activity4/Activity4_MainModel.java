package com.example.boroodat.model.activity4;

public class Activity4_MainModel extends Activity4_ParentModel
{
    private String id;
    private String name,phone,role;

    public Activity4_MainModel(String id, String name, String phone, String role)
    {
        super(Activity4_ParentModel.Main);
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.role = role;
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
