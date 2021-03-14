package com.example.boroodat.general;

import com.example.boroodat.database.User_Info_DB;

import org.json.JSONException;
import org.json.JSONObject;

import io.realm.Realm;
import io.realm.RealmResults;

public class User_Info
{
    private JSONObject response;
    private Realm realm;

    public User_Info(JSONObject response)
    {
        this.response = response;

        //-----------------------------

        realm=Realm.getDefaultInstance();
    }

    public User_Info()
    {
        realm=Realm.getDefaultInstance();
    }

    public boolean save()
    {
        try
        {
            JSONObject object1 = response.getJSONObject("user");
            String user_id = object1.getString("user_id");
            String phone_number = object1.getString("phone_number");
            String username = object1.getString("name");
            String role = object1.getString("role");
            String company_id = object1.getString("company_id");
            String company_name=object1.getString("company_name");

            JSONObject object2 = response.getJSONObject("token");
            String token = object2.getString("plainTextToken");

            JSONObject object3 = object2.getJSONObject("accessToken");
            String token_id = object3.getString("id");

            realm.beginTransaction();
            realm.copyToRealmOrUpdate(new User_Info_DB(0,user_id,phone_number,username,role,company_id,company_name,token,token_id));
            realm.commitTransaction();

            return true;

        } catch (JSONException e)
        {
            return false;
        }

    }

    public  String role()
    {
        RealmResults<User_Info_DB> res = realm.where(User_Info_DB.class).findAll();

        if (res.size()>0)
            return res.get(0).getRole();
        else
            return "";
    }

    public  String token()
    {
        RealmResults<User_Info_DB> res = realm.where(User_Info_DB.class).findAll();

        if (res.size()>0)
            return res.get(0).getToken();
        else
            return "";
    }

    public  String token_id()
    {
        RealmResults<User_Info_DB> res = realm.where(User_Info_DB.class).findAll();

        if (res.size()>0)
            return res.get(0).getToken_id();
        else
            return "";
    }

    public  String company_id()
    {
        RealmResults<User_Info_DB> res = realm.where(User_Info_DB.class).findAll();

        if (res.size()>0)
            return res.get(0).getCompany_id();
        else
            return "";
    }

    public  String company_name()
    {
        RealmResults<User_Info_DB> res = realm.where(User_Info_DB.class).findAll();

        if (res.size()>0)
            return res.get(0).getCompany_name();
        else
            return "";
    }

    public  String user_id()
    {
        RealmResults<User_Info_DB> res = realm.where(User_Info_DB.class).findAll();

        if (res.size()>0)
            return res.get(0).getUser_id();
        else
            return "";
    }
}
