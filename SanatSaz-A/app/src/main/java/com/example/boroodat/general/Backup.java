package com.example.boroodat.general;

import android.content.Context;
import android.os.Environment;
import android.os.StatFs;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import io.realm.Realm;

public class Backup
{
    private Context context;
    private Realm realm;
    private String exportRealmFileName;

    public Backup(Context context, String exportRealmFileName )
    {
        this.realm = Realm.getDefaultInstance ();
        this.context = context;
        this.exportRealmFileName=exportRealmFileName;
    }

    public void create()
    {
        File exportRealmFile = null;

        File exportRealmPATH = new File( Environment.getExternalStorageDirectory ()+"/boroodat" + "/Backup" );

        if (!exportRealmPATH.isDirectory ())
            exportRealmPATH.mkdirs ();

        try
        {
            exportRealmFile = new File( exportRealmPATH, exportRealmFileName );
            exportRealmFile.delete ();

            long fileSize=new File( realm.getPath () ).length ();

            if (fileSize<internalFreeSpace ())
            {
                realm.writeCopyTo ( exportRealmFile );
                Toast.makeText ( context, "پشتیبان گیری با موفقیت انجام شد.", Toast.LENGTH_LONG ).show ();
                realm.close ();
            }
            else
            {
                Toast.makeText ( context, "ظرفیت حافظه کافی نیست.", Toast.LENGTH_LONG ).show ();
            }
        }

        catch (Exception e)
        {
            Toast.makeText ( context, "پشتیبان گیری ناموفق", Toast.LENGTH_LONG ).show ();
        }
    }

    public void restore()
    {
        try
        {
            File output = new File( context.getFilesDir (), "default.realm" );

            File input = new File( Environment.getExternalStorageDirectory ()
                    +"/صندوق قرض الحسنه خانوادگی" + "/Backup",exportRealmFileName );

            FileOutputStream outputStream = new FileOutputStream( output );

            FileInputStream inputStream = new FileInputStream( input );

            byte[] buf = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read ( buf )) > 0)
            {
                outputStream.write ( buf, 0, bytesRead );
            }
            outputStream.close ();

            Toast.makeText ( context, "بازیابی اطلاعات با موفقیت انجام شد.", Toast.LENGTH_SHORT ).show ();
        }

        catch (IOException e)
        {
            Toast.makeText ( context, "بازیابی ناموفق", Toast.LENGTH_LONG ).show ();
        }
    }

    private long internalFreeSpace()
    {
        File path= Environment.getDataDirectory ();
        StatFs stat=new StatFs( path.getPath () );
        long blockSize;
        long availableBlocks;

        int currentAPIVersion=android.os.Build.VERSION.SDK_INT;
        if (currentAPIVersion<18)
        {
            blockSize=stat.getBlockSize ();
            availableBlocks=stat.getAvailableBlocks ();
        }
        else
        {
            blockSize=stat.getBlockSizeLong ();
            availableBlocks=stat.getAvailableBlocksLong ();
        }

        return (blockSize*availableBlocks);

    }
}