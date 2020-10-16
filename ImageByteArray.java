package com.visione.amndd.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;

import java.io.ByteArrayOutputStream;

public class ImageByteArray {

    public static byte[] convertBitmapToByteArray(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, baos);

        return baos.toByteArray();
    }

    public static Bitmap convertByteArrayBitmap(byte[] bytes){
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }

    public static Bitmap rotateBitmap(Bitmap bitmap){
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        Matrix matrix = new Matrix();
        matrix.postRotate(90);
        //matrix.postScale(scaleWidth, scaleHeight);

        Bitmap b = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);

        float bitmapRatio = (float)width / (float) height;
        if (bitmapRatio > 1) {
            width = 500;
            height = (int) (width / bitmapRatio);
        } else {
            height = 500;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(b, width, height, true);
    }
}
