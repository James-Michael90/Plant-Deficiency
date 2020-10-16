package com.visione.amndd.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.widget.Toast;

import androidx.core.content.FileProvider;

import java.io.File;
import java.util.List;

public class GenericFileProvider extends FileProvider {

    public void openPdfViewer(File file, Context context){
        PackageManager packageManager = context.getPackageManager();
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setType("application/pdf");
        List list = packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        if(list.size() > 0){
            Intent i = new Intent();
            i.setAction(Intent.ACTION_VIEW);
            Uri uri = FileProvider.getUriForFile(context, context.getApplicationContext().getPackageName() + ".provider", file);
            i.setDataAndType(uri, "application/pdf");
            i.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            context.startActivity(i);
        } else {
            Toast.makeText(getContext(), "Please make sure you have a PDF file viewer to view this file", Toast.LENGTH_LONG).show();
        }
    }
}
