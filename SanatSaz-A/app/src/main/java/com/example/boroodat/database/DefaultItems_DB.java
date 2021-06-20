package com.example.boroodat.database;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class DefaultItems_DB extends RealmObject
{
    @PrimaryKey
    private String id;

    private String account_title,account_id,buyer_name,buyer_id,driver_name,driver_id,seller_name,seller_id;

    public DefaultItems_DB()
    {
    }

    public DefaultItems_DB(String id, String account_title, String account_id, String buyer_name, String buyer_id, String driver_name, String driver_id, String seller_name, String seller_id)
    {
        this.id = id;
        this.account_title = account_title;
        this.account_id = account_id;
        this.buyer_name = buyer_name;
        this.buyer_id = buyer_id;
        this.driver_name = driver_name;
        this.driver_id = driver_id;
        this.seller_name = seller_name;
        this.seller_id = seller_id;
    }

    public String getSeller_name()
    {
        return seller_name;
    }

    public void setSeller_name(String seller_name)
    {
        this.seller_name = seller_name;
    }

    public String getSeller_id()
    {
        return seller_id;
    }

    public void setSeller_id(String seller_id)
    {
        this.seller_id = seller_id;
    }

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getAccount_title()
    {
        return account_title;
    }

    public void setAccount_title(String account_title)
    {
        this.account_title = account_title;
    }

    public String getAccount_id()
    {
        return account_id;
    }

    public void setAccount_id(String account_id)
    {
        this.account_id = account_id;
    }

    public String getBuyer_name()
    {
        return buyer_name;
    }

    public void setBuyer_name(String buyer_name)
    {
        this.buyer_name = buyer_name;
    }

    public String getBuyer_id()
    {
        return buyer_id;
    }

    public void setBuyer_id(String buyer_id)
    {
        this.buyer_id = buyer_id;
    }

    public String getDriver_name()
    {
        return driver_name;
    }

    public void setDriver_name(String driver_name)
    {
        this.driver_name = driver_name;
    }

    public String getDriver_id()
    {
        return driver_id;
    }

    public void setDriver_id(String driver_id)
    {
        this.driver_id = driver_id;
    }
}
