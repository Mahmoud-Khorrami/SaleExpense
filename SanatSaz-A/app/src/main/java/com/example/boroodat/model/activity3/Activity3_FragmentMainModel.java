package com.example.boroodat.model.activity3;

public class Activity3_FragmentMainModel extends Activity3_FragmentParentModel
{

    private String id;

    private String title, amount,date,account_id,account_title,description;

    public Activity3_FragmentMainModel( String id, String title, String amount, String date, String account_id, String account_title, String description)
    {
        super(Activity3_FragmentParentModel.Main);
        this.id = id;
        this.title = title;
        this.amount = amount;
        this.date = date;
        this.account_id = account_id;
        this.account_title = account_title;
        this.description = description;
    }

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public String getAmount()
    {
        return amount;
    }

    public void setAmount(String amount)
    {
        this.amount = amount;
    }

    public String getDate()
    {
        return date;
    }

    public void setDate(String date)
    {
        this.date = date;
    }

    public String getAccount_id()
    {
        return account_id;
    }

    public void setAccount_id(String account_id)
    {
        this.account_id = account_id;
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
}
