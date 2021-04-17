package com.example.boroodat.general;

import com.example.boroodat.database.Report_DB;
import io.realm.Realm;
import io.realm.RealmResults;

public class Report
{
    private Realm realm;

    public Report()
    {
        realm=Realm.getDefaultInstance();
    }

    public void material(String material,String material_payment,String status)
    {
        RealmResults<Report_DB> results=realm.where(Report_DB.class).findAll();

        if (results.size()>0)
        {
            double material1=Double.parseDouble(results.get(0).getMaterial());
            double material_payment1=Double.parseDouble(results.get(0).getMaterial_payment());

            if (status.equals("i"))
            {
                material1 = material1 + Double.parseDouble(material);
                material_payment1 = material_payment1 + Double.parseDouble(material_payment);
            }

            if (status.equals("d"))
            {
                material1 = material1 - Double.parseDouble(material);
                material_payment1 = material_payment1 - Double.parseDouble(material_payment);
            }

            realm.beginTransaction();
            realm.copyToRealmOrUpdate(new Report_DB(0,Math.round(material1)+"",Math.round(material_payment1)+"",results.get(0).getSalary(),results.get(0).getExpense(),results.get(0).getExpense_payment(),results.get(0).getSale(),results.get(0).getSale_payment(),results.get(0).getDeposit()));
            realm.commitTransaction();
        }
    }

    public void salary(String salary,String status)
    {
        RealmResults<Report_DB> results=realm.where(Report_DB.class).findAll();

        if (results.size()>0)
        {
            double salary1=Double.parseDouble(results.get(0).getSalary());

            if (status.equals("i"))
                salary1=salary1 + Double.parseDouble(salary);
            else
                salary1=salary1 - Double.parseDouble(salary);


            realm.beginTransaction();
            realm.copyToRealmOrUpdate(new Report_DB(0,results.get(0).getMaterial(),results.get(0).getMaterial_payment(),Math.round(salary1)+"",results.get(0).getExpense(),results.get(0).getExpense_payment(),results.get(0).getSale(),results.get(0).getSale_payment(),results.get(0).getDeposit()));
            realm.commitTransaction();
        }
    }

    public void expense(String expense,String expense_payment,String status)
    {
        RealmResults<Report_DB> results=realm.where(Report_DB.class).findAll();

        if (results.size()>0)
        {
            double expense1=Double.parseDouble(results.get(0).getExpense());
            double expense_payment1=Double.parseDouble(results.get(0).getExpense_payment());

            if (status.equals("i"))
            {
                expense1 = expense1 + Double.parseDouble(expense);
                expense_payment1 = expense_payment1 + Double.parseDouble(expense_payment);
            }

            else
            {
                expense1 = expense1 - Double.parseDouble(expense);
                expense_payment1 = expense_payment1 - Double.parseDouble(expense_payment);
            }

            realm.beginTransaction();
            realm.copyToRealmOrUpdate(new Report_DB(0,results.get(0).getMaterial(),results.get(0).getMaterial_payment(),results.get(0).getSalary(),Math.round(expense1)+"",Math.round(expense_payment1)+"",results.get(0).getSale(),results.get(0).getSale_payment(),results.get(0).getDeposit()));
            realm.commitTransaction();
        }
    }

    public void sale(String sale,String sale_payment,String status)
    {
        RealmResults<Report_DB> results=realm.where(Report_DB.class).findAll();

        if (results.size()>0)
        {
            double sale1=Double.parseDouble(results.get(0).getSale());
            double sale_payment1=Double.parseDouble(results.get(0).getSale_payment());

            if (status.equals("i"))
            {
                sale1 = sale1 + Double.parseDouble(sale);
                sale_payment1 = sale_payment1 + Double.parseDouble(sale_payment);
            }

            else
            {
                sale1 = sale1 - Double.parseDouble(sale);
                sale_payment1 = sale_payment1 - Double.parseDouble(sale_payment);
            }

            realm.beginTransaction();
            realm.copyToRealmOrUpdate(new Report_DB(0,results.get(0).getMaterial(),results.get(0).getMaterial_payment(),results.get(0).getSalary(),results.get(0).getExpense(),results.get(0).getExpense_payment(),Math.round(sale1)+"",Math.round(sale_payment1)+"",results.get(0).getDeposit()));
            realm.commitTransaction();
        }
    }

    public void deposit(String deposit,String status)
    {
        RealmResults<Report_DB> results=realm.where(Report_DB.class).findAll();

        if (results.size()>0)
        {
            double deposit1=Double.parseDouble(results.get(0).getDeposit());

            if (status.equals("i"))
                deposit1=deposit1 + Double.parseDouble(deposit);
            else
                deposit1=deposit1 - Double.parseDouble(deposit);


            realm.beginTransaction();
            realm.copyToRealmOrUpdate(new Report_DB(0,results.get(0).getMaterial(),results.get(0).getMaterial_payment(),results.get(0).getSalary(),results.get(0).getExpense(),results.get(0).getExpense_payment(),results.get(0).getSale(),results.get(0).getSale_payment(),Math.round(deposit1)+""));
            realm.commitTransaction();
        }
    }
}
