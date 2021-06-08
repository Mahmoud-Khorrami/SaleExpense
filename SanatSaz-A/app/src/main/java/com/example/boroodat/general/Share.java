package com.example.boroodat.general;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.view.View;

import androidx.core.content.FileProvider;

import com.example.boroodat.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class Share
{
    private Context context;
    private View rootView;
    private Bitmap bitmap;
    private File file;

    public Share(Context context, View rootView)
    {
        this.context = context;
        this.rootView = rootView;
    }

    public void screenShot()
    {
        rootView.setDrawingCacheEnabled(true);
        bitmap = rootView.getDrawingCache();
        saveBitmap();
    }

    private void saveBitmap()
    {
        try
        {
            String path = Environment.getExternalStorageDirectory() + "/" + context.getString(R.string.app_name) + "/تصاویر/";
            File file1 = new File(path);

            if (!file1.isDirectory())
                file1.mkdirs();

            String file_name = "ScreenShot-" + new TodayDate().dateTime() + ".png";
            file = new File(path, file_name);

            if (!file.exists())
                file.createNewFile();

            FileOutputStream fos;
            fos = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();

            shareIt();

        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    private void shareIt()
    {
        Uri uri = FileProvider.getUriForFile(context, context.getApplicationContext().getPackageName()+".provider", file);
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("image/*");
        String shareBody = "صورت وضعیت";
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "My Catch score");
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
        sharingIntent.putExtra(Intent.EXTRA_STREAM, uri);

        context.startActivity(Intent.createChooser(sharingIntent, "Share via"));
    }
}
