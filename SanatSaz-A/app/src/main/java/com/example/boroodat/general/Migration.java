package com.example.boroodat.general;


import com.example.boroodat.database.DefaultItems_DB;

import io.realm.DynamicRealm;
import io.realm.FieldAttribute;
import io.realm.RealmMigration;
import io.realm.RealmObjectSchema;
import io.realm.RealmSchema;
import io.realm.annotations.PrimaryKey;

public class Migration implements RealmMigration
{

    @Override
    public void migrate(DynamicRealm realm, long oldVersion, long newVersion)
    {
        RealmSchema schema = realm.getSchema();

        if (oldVersion == 0)
        {
            schema.create("DefaultItems_DB")
                    .addField("id", String.class, FieldAttribute.PRIMARY_KEY)
                    .addField("account_title", String.class)
                    .addField("account_id", String.class)
                    .addField("buyer_name", String.class)
                    .addField("buyer_id", String.class)
                    .addField("driver_name", String.class)
                    .addField("driver_id", String.class)
                    .addField("seller_name", String.class)
                    .addField("seller_id", String.class);

            oldVersion++;
        }
    }
}
