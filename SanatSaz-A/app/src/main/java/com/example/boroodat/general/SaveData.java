package com.example.boroodat.general;

import com.example.boroodat.database.Activity14_DB;
import com.example.boroodat.database.Activity7_DB;
import com.example.boroodat.database.Activity8_DB;
import com.example.boroodat.database.Activity9_DB;
import com.example.boroodat.database.Fragment4_DB;
import com.example.boroodat.database.Fragment5_DB;
import com.example.boroodat.database.Fragment7_DB;
import com.example.boroodat.database.Fragment8_DB;
import com.example.boroodat.database.Fragment9_DB;
import com.example.boroodat.database.Report_DB;
import com.example.boroodat.database.SaleDetail_DB;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import io.realm.Realm;
import io.realm.RealmResults;

public class SaveData
{
    private JSONArray array;
    private Realm realm;

    public SaveData(JSONArray array)
    {
        this.array = array;

        //------------------------------

        realm = Realm.getDefaultInstance();
    }

    public boolean toActivity7DB()
    {
        realm.executeTransaction(new Realm.Transaction()
        {
            @Override
            public void execute(Realm realm)
            {
                RealmResults<Activity7_DB> res = realm.where(Activity7_DB.class)
                        .findAll();

                if (res.size() > 0)
                    res.deleteAllFromRealm();
            }
        });

        //--------------------------------------------------------------------------------------------

        try
        {

            for (int i = 0; i < array.length(); i++)
            {
                JSONObject object1 = array.getJSONObject(i);
                realm.beginTransaction();
                realm.copyToRealmOrUpdate(new Activity7_DB(Integer.parseInt(object1.getString("id")), object1.getString("title"), object1.getString("account_number"), object1.getString("balance")));
                realm.commitTransaction();
            }

            return true;
        } catch (JSONException e)
        {
            return false;
        }
    }

    public boolean toActivity8DB()
    {
        realm.executeTransaction(new Realm.Transaction()
        {
            @Override
            public void execute(Realm realm)
            {
                RealmResults<Activity8_DB> res = realm.where(Activity8_DB.class)
                        .findAll();

                if (res.size() > 0)
                    res.deleteAllFromRealm();
            }
        });

        //--------------------------------------------------------------------------------------------

        try
        {
            for (int i = 0; i < array.length(); i++)
            {
                JSONObject object1 = array.getJSONObject(i);
                realm.beginTransaction();
                realm.copyToRealmOrUpdate(new Activity8_DB(Integer.parseInt(object1.getString("id")), object1.getString("name"), object1.getString("phone_number"), object1.getString("destination"),object1.getString("archive")));
                realm.commitTransaction();
            }

            return true;
        } catch (JSONException e)
        {
            return false;
        }
    }

    public boolean toActivity9DB()
    {
        realm.executeTransaction(new Realm.Transaction()
        {
            @Override
            public void execute(Realm realm)
            {
                RealmResults<Activity9_DB> res = realm.where(Activity9_DB.class)
                        .findAll();

                if (res.size() > 0)
                    res.deleteAllFromRealm();
            }
        });

        //--------------------------------------------------------------------------------------------

        try
        {
            for (int i = 0; i < array.length(); i++)
            {
                JSONObject object1 = array.getJSONObject(i);
                realm.beginTransaction();
                realm.copyToRealmOrUpdate(new Activity9_DB(Integer.parseInt(object1.getString("id")), object1.getString("name"), object1.getString("phone_number"), object1.getString("car_type"), object1.getString("number_plate"),object1.getString("archive")));
                realm.commitTransaction();
            }

            return true;
        } catch (JSONException e)
        {
            return false;
        }
    }

    public boolean toActivity14DB()
    {
        realm.executeTransaction(new Realm.Transaction()
        {
            @Override
            public void execute(Realm realm)
            {
                RealmResults<Activity14_DB> res = realm.where(Activity14_DB.class)
                        .findAll();

                if (res.size() > 0)
                    res.deleteAllFromRealm();
            }
        });

        //--------------------------------------------------------------------------------------------

        try
        {
            for (int i = 0; i < array.length(); i++)
            {
                JSONObject object1 = array.getJSONObject(i);
                realm.beginTransaction();
                realm.copyToRealmOrUpdate(new Activity14_DB(Integer.parseInt(object1.getString("id")), object1.getString("name"), object1.getString("phone_number"), object1.getString("register_date"), object1.getString("role"), object1.getString("credit_card"), object1.getString("exit_date")));
                realm.commitTransaction();
            }

            return true;
        } catch (JSONException e)
        {
            return false;
        }
    }

    public boolean toFragment4DB()
    {
        realm.executeTransaction(new Realm.Transaction()
        {
            @Override
            public void execute(Realm realm)
            {
                RealmResults<Fragment4_DB> res = realm.where(Fragment4_DB.class).findAll();

                if (res.size() > 0)
                    res.deleteAllFromRealm();
            }
        });

        //-----------------------------------------------------------------------------

        try
        {
            for (int i = 0; i < array.length(); i++)
            {
                JSONObject object = array.getJSONObject(i);

                realm.beginTransaction();
                realm.copyToRealmOrUpdate(new Fragment4_DB(Integer.parseInt(object.getString("id")), object.getString("title"), object.getString("amount"), object.getString("date"), object.getString("account_id")));
                realm.commitTransaction();
            }

            return true;
        } catch (JSONException e)
        {
            return false;
        }
    }

    public boolean toFragment5DB()
    {
        realm.executeTransaction(new Realm.Transaction()
        {
            @Override
            public void execute(Realm realm)
            {
                RealmResults<Fragment5_DB> res = realm.where(Fragment5_DB.class).findAll();

                if (res.size() > 0)
                    res.deleteAllFromRealm();
            }
        });

        //-------------------------------------------------------------------------

        try
        {
            for (int i = 0; i < array.length(); i++)
            {
                JSONObject object = array.getJSONObject(i);

                realm.beginTransaction();
                realm.copyToRealmOrUpdate(new Fragment5_DB(Integer.parseInt(object.getString("id")), object.getString("factor_number"), object.getString("date"), object.getString("sum"), object.getString("payment"), object.getString("account_id"),object.getString("buyer_id")));
                realm.commitTransaction();
            }

            return true;
        } catch (JSONException e)
        {
            return false;
        }
    }

    public boolean toFragment7DB()
    {
        realm.executeTransaction(new Realm.Transaction()
        {
            @Override
            public void execute(Realm realm)
            {
                RealmResults<Fragment7_DB> res = realm.where(Fragment7_DB.class).findAll();

                if (res.size() > 0)
                    res.deleteAllFromRealm();
            }
        });

        //--------------------------------------------------------------------------------------------
        try
        {
            for (int i = 0; i < array.length(); i++)
            {
                JSONObject object = array.getJSONObject(i);

                RealmResults<Activity14_DB> res1 = realm.where(Activity14_DB.class)
                        .equalTo("id", Integer.parseInt(object.getString("personnel_id"))).findAll();

                RealmResults<Activity7_DB> res2 = realm.where(Activity7_DB.class)
                        .equalTo("id", Integer.parseInt(object.getString("account_id"))).findAll();

                realm.beginTransaction();
                realm.copyToRealmOrUpdate(new Fragment7_DB(Integer.parseInt(object.getString("id")), res1.get(0).getName(), object.getString("personnel_id"), object.getString("salary"), object.getString("earnest"), object.getString("insurance_tax"), object.getString("account_id"), res2.get(0).getTitle(), object.getString("date"), object.getString("description")));
                realm.commitTransaction();

            }
            return true;
        } catch (JSONException e)
        {
            return false;
        }
    }

    public boolean toFragment8DB()
    {
        realm.executeTransaction(new Realm.Transaction()
        {
            @Override
            public void execute(Realm realm)
            {
                RealmResults<Fragment8_DB> res = realm.where(Fragment8_DB.class).findAll();

                if (res.size() > 0)
                    res.deleteAllFromRealm();
            }
        });

        //-------------------------------------------------------------------------

        try
        {
            for (int i = 0; i < array.length(); i++)
            {
                JSONObject object = array.getJSONObject(i);

                realm.beginTransaction();
                realm.copyToRealmOrUpdate(new Fragment8_DB(Integer.parseInt(object.getString("id")), object.getString("factor_number"), object.getString("date"), object.getString("sum"), object.getString("payment"), object.getString("account_id")));
                realm.commitTransaction();
            }

            return true;
        } catch (JSONException e)
        {
            return false;
        }
    }

    public boolean toFragment9DB()
    {
        realm.executeTransaction(new Realm.Transaction()
        {
            @Override
            public void execute(Realm realm)
            {
                RealmResults<Fragment9_DB> res = realm.where(Fragment9_DB.class).findAll();

                if (res.size() > 0)
                    res.deleteAllFromRealm();
            }
        });

        //-------------------------------------------------------------------------

        try
        {
            for (int i = 0; i < array.length(); i++)
            {
                JSONObject object = array.getJSONObject(i);

                realm.beginTransaction();
                realm.copyToRealmOrUpdate(new Fragment9_DB(Integer.parseInt(object.getString("id")), object.getString("factor_number"), object.getString("date"), object.getString("sum"), object.getString("payment"), object.getString("account_id")));
                realm.commitTransaction();
            }

            return true;
        } catch (JSONException e)
        {
            return false;
        }
    }

    public boolean toReportDB()
    {
        try
        {
            JSONObject object=array.getJSONObject(0);

            String material = object.getString("material");
            String material_payment = object.getString("material_payment");
            String salary = object.getString("salary");
            String expense = object.getString("expense");
            String expense_payment = object.getString("expense_payment");
            String sale = object.getString("sale");
            String sale_payment = object.getString("sale_payment");
            String deposit = object.getString("deposit");

            //----------------------------------------------------------

            realm.executeTransaction(new Realm.Transaction()
            {
                @Override
                public void execute(Realm realm)
                {
                    RealmResults<Report_DB> res = realm.where(Report_DB.class)
                            .findAll();

                    if (res.size() > 0)
                        res.deleteAllFromRealm();
                }
            });

            realm.beginTransaction();
            realm.copyToRealmOrUpdate(new Report_DB(0,material,material_payment,salary,expense,expense_payment,sale,sale_payment,deposit));
            realm.commitTransaction();

            return true;
        } catch (JSONException e)
        {
            return false;
        }
    }

    public boolean toSaleDetailDB()
    {
        realm.executeTransaction(new Realm.Transaction()
        {
            @Override
            public void execute(Realm realm)
            {
                RealmResults<SaleDetail_DB> res = realm.where(SaleDetail_DB.class).findAll();

                if (res.size() > 0)
                    res.deleteAllFromRealm();
            }
        });
        //-------------------------------------------------------------------------

        try
        {
            for (int i = 0; i < array.length(); i++)
            {
                JSONObject object = array.getJSONObject(i);

                realm.beginTransaction();
                realm.copyToRealmOrUpdate(new SaleDetail_DB(Integer.parseInt(object.getString("id")), object.getString("description")));
                realm.commitTransaction();
            }

            return true;
        } catch (JSONException e)
        {
            return false;
        }

    }
}