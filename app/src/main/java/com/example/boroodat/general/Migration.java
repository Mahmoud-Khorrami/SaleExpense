package com.example.boroodat.general;


import io.realm.DynamicRealm;
import io.realm.RealmMigration;
import io.realm.RealmSchema;

public class Migration implements RealmMigration
{

    @Override
    public void migrate(DynamicRealm realm, long oldVersion, long newVersion)
    {
        RealmSchema schema = realm.getSchema();

        if (oldVersion == 0)
        {
            //RealmObjectSchema wageSchema = schema.get("WagePercentage");
           // wageSchema.addField("a", int.class);
        }
    }
}
