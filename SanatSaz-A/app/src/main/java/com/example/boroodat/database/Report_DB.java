package com.example.boroodat.database;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Report_DB extends RealmObject
{
    @PrimaryKey
    private int id;

    private String material,material_payment,salary, expense,expense_payment,sale,sale_payment,deposit;

    public Report_DB()
    {
    }

    public Report_DB(int id, String material, String material_payment, String salary, String expense, String expense_payment, String sale, String sale_payment, String deposit)
    {
        this.id = id;
        this.material = material;
        this.material_payment = material_payment;
        this.salary = salary;
        this.expense = expense;
        this.expense_payment = expense_payment;
        this.sale = sale;
        this.sale_payment = sale_payment;
        this.deposit = deposit;
    }

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public String getMaterial()
    {
        return material;
    }

    public void setMaterial(String material)
    {
        this.material = material;
    }

    public String getMaterial_payment()
    {
        return material_payment;
    }

    public void setMaterial_payment(String material_payment)
    {
        this.material_payment = material_payment;
    }

    public String getSalary()
    {
        return salary;
    }

    public void setSalary(String salary)
    {
        this.salary = salary;
    }

    public String getExpense()
    {
        return expense;
    }

    public void setExpense(String expense)
    {
        this.expense = expense;
    }

    public String getExpense_payment()
    {
        return expense_payment;
    }

    public void setExpense_payment(String expense_payment)
    {
        this.expense_payment = expense_payment;
    }

    public String getSale()
    {
        return sale;
    }

    public void setSale(String sale)
    {
        this.sale = sale;
    }

    public String getSale_payment()
    {
        return sale_payment;
    }

    public void setSale_payment(String sale_payment)
    {
        this.sale_payment = sale_payment;
    }

    public String getDeposit()
    {
        return deposit;
    }

    public void setDeposit(String deposit)
    {
        this.deposit = deposit;
    }
}
