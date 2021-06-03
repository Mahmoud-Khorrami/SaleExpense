package com.example.boroodat.database;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class User_Info_DB extends RealmObject
{
    @PrimaryKey
    private int id;

    private String user_id,phone_number,password,username,role,company_id,company_name,token,token_id;

    public User_Info_DB()
    {
    }

    public User_Info_DB(int id, String user_id, String phone_number, String password, String username, String role, String company_id, String company_name, String token, String token_id)
    {
        this.id = id;
        this.user_id = user_id;
        this.phone_number = phone_number;
        this.password = password;
        this.username = username;
        this.role = role;
        this.company_id = company_id;
        this.company_name = company_name;
        this.token = token;
        this.token_id = token_id;
    }

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public String getUser_id()
    {
        return user_id;
    }

    public void setUser_id(String user_id)
    {
        this.user_id = user_id;
    }

    public String getPhone_number()
    {
        return phone_number;
    }

    public void setPhone_number(String phone_number)
    {
        this.phone_number = phone_number;
    }

    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    public String getUsername()
    {
        return username;
    }

    public void setUsername(String username)
    {
        this.username = username;
    }

    public String getRole()
    {
        return role;
    }

    public void setRole(String role)
    {
        this.role = role;
    }

    public String getCompany_id()
    {
        return company_id;
    }

    public void setCompany_id(String company_id)
    {
        this.company_id = company_id;
    }

    public String getCompany_name()
    {
        return company_name;
    }

    public void setCompany_name(String company_name)
    {
        this.company_name = company_name;
    }

    public String getToken()
    {
        return token;
    }

    public void setToken(String token)
    {
        this.token = token;
    }

    public String getToken_id()
    {
        return token_id;
    }

    public void setToken_id(String token_id)
    {
        this.token_id = token_id;
    }
}
