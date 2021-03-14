package com.example.boroodat.general;

import com.example.boroodat.database.ID_DB;

import io.realm.Realm;
import io.realm.RealmResults;

public class ID_Recorder
{
    public int row;
    public int id;
    private Realm realm;

    public ID_Recorder(int row)
    {
        this.row = row;

        realm=Realm.getDefaultInstance();
    }

    public ID_Recorder(int row, int id)
    {
        this.row = row;
        this.id = id;

        realm=Realm.getDefaultInstance();
    }

    public int getId()
    {
        RealmResults<ID_DB> results = realm.where ( ID_DB.class )
                .equalTo("row",row)
                .findAll();

        if (results.size()==0)
            return 0;

        else
            return results.get(0).getId();
    }

    public void setId()
    {
        realm.beginTransaction ();
        realm.copyToRealmOrUpdate ( new ID_DB( row,id ));
        realm.commitTransaction ();
    }

    // row 1 => activity2_manager => saveAutoData
}
