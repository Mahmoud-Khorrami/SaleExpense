package com.example.boroodat.database;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class ID_DB extends RealmObject
{
    @PrimaryKey
    private int row;

    private int id;

    public ID_DB()
    {
    }

    public ID_DB(int row, int id)
    {
        this.row = row;
        this.id = id;
    }

    public int getRow()
    {
        return row;
    }

    public void setRow(int row)
    {
        this.row = row;
    }

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }
}
