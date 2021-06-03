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

    public void save()
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
            realm.copyToRealmOrUpdate(new User_Info_DB(0,user_id,phone_number,"",username,role,company_id,company_name,token,token_id));
            realm.commitTransaction();

        } catch (JSONException e)
        {
            e.printStackTrace();
        }

    }

    public  String role()
    {
        RealmResults<User_Info_DB> res = realm.where(User_Info_DB.class).findAll();

        String role = "";

        if (res.size()>0)
            role = res.get(0).getRole();

        realm.close();
        return role;
    }

    public  String token()
    {
        RealmResults<User_Info_DB> res = realm.where(User_Info_DB.class).findAll();

        String token = "";

        if (res.size()>0)
            token = res.get(0).getToken();

        realm.close();
        return token;
    }

    public  String token_id()
    {
        RealmResults<User_Info_DB> res = realm.where(User_Info_DB.class).findAll();

        String token_id = "";

        if (res.size()>0)
            token_id = res.get(0).getToken_id();

        realm.close();
        return token_id;
    }

    public  String company_id()
    {
        RealmResults<User_Info_DB> res = realm.where(User_Info_DB.class).findAll();

        String company_id = "";

        if (res.size()>0)
            company_id = res.get(0).getCompany_id();

        realm.close();
        return company_id;
    }

    public  String company_name()
    {
        RealmResults<User_Info_DB> res = realm.where(User_Info_DB.class).findAll();

        String company_name = "";

        if (res.size()>0)
            company_name = res.get(0).getCompany_name();

        realm.close();
        return company_name;
    }

    public  String user_id()
    {
        RealmResults<User_Info_DB> res = realm.where(User_Info_DB.class).findAll();

        String user_id = "";

        if (res.size()>0)
            user_id = res.get(0).getUser_id();

        realm.close();
        return user_id;
    }
}
