package com.example.boroodat.model.activity23;

public class Activity23_MainModel extends Activity23_ParentModel
{
    private String id;
    private String seller_name,shop_name,phone_number,address,archive;
    private boolean isSelected = false;

    public Activity23_MainModel( String id, String seller_name, String shop_name, String phone_number, String address, String archive)
    {
        super(Activity23_ParentModel.Main);
        this.id = id;
        this.seller_name = seller_name;
        this.shop_name = shop_name;
        this.phone_number = phone_number;
        this.address = address;
        this.archive = archive;
    }

    public String getArchive()
    {
        return archive;
    }

    public void setArchive(String archive)
    {
        this.archive = archive;
    }

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getSeller_name()
    {
        return seller_name;
    }

    public void setSeller_name(String seller_name)
    {
        this.seller_name = seller_name;
    }

    public String getShop_name()
    {
        return shop_name;
    }

    public void setShop_name(String shop_name)
    {
        this.shop_name = shop_name;
    }

    public String getPhone_number()
    {
        return phone_number;
    }

    public void setPhone_number(String phone_number)
    {
        this.phone_number = phone_number;
    }

    public String getAddress()
    {
        return address;
    }

    public void setAddress(String address)
    {
        this.address = address;
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
