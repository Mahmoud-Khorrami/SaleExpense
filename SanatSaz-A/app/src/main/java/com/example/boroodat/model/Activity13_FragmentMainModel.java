package com.example.boroodat.model;

public class Activity13_FragmentMainModel extends Activity13_FragmentParentModel
{
    private String id;

    private String personnel_name,personnel_id,salary,earnest,insurance_tax,account_id,account_title,date,description;

    public Activity13_FragmentMainModel(String id, String personnel_name, String personnel_id, String salary, String earnest, String insurance_tax, String account_id, String account_title, String date, String description)
    {
        super(Activity13_FragmentParentModel.Main);
        this.id = id;
        this.personnel_name = personnel_name;
        this.personnel_id = personnel_id;
        this.salary = salary;
        this.earnest = earnest;
        this.insurance_tax = insurance_tax;
        this.account_id = account_id;
        this.account_title = account_title;
        this.date = date;
        this.description = description;
    }

    public String getAccount_title()
    {
        return account_title;
    }

    public void setAccount_title(String account_title)
    {
        this.account_title = account_title;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public String getPersonnel_id()
    {
        return personnel_id;
    }

    public void setPersonnel_id(String personnel_id)
    {
        this.personnel_id = personnel_id;
    }

    public String getAccount_id()
    {
        return account_id;
    }

    public void setAccount_id(String account_id)
    {
        this.account_id = account_id;
    }

    public String getDate()
    {
        return date;
    }

    public void setDate(String date)
    {
        this.date = date;
    }

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getPersonnel_name()
    {
        return personnel_name;
    }

    public void setPersonnel_name(String personnel_name)
    {
        this.personnel_name = personnel_name;
    }

    public String getSalary()
    {
        return salary;
    }

    public void setSalary(String salary)
    {
        this.salary = salary;
    }

    public String getEarnest()
    {
        return earnest;
    }

    public void setEarnest(String earnest)
    {
        this.earnest = earnest;
    }

    public String getInsurance_tax()
    {
        return insurance_tax;
    }

    public void setInsurance_tax(String insurance_tax)
    {
        this.insurance_tax = insurance_tax;
    }
}
