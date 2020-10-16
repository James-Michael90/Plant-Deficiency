package com.visione.amndd;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Typeface;
import android.media.ImageReader.OnImageAvailableListener;
import android.os.AsyncTask;
import android.os.SystemClock;
import android.util.Size;
import android.util.TypedValue;

import com.visione.amndd.data.Deficiency;
import com.visione.amndd.env.BorderedText;
import com.visione.amndd.env.ImageUtils;
import com.visione.amndd.tflite.Classifier;
import com.visione.amndd.utils.CurrentDate;
import com.visione.amndd.utils.ImageByteArray;

import java.io.IOException;
import java.util.Date;
import java.util.List;

public class ClassifierActivity extends CameraActivity implements OnImageAvailableListener {
    private static final Size DESIRED_PREVIEW_SIZE = new Size(640, 480);
    private static final float TEXT_SIZE_DIP = 10;
    private Bitmap rgbFrameBitmap = null;
    private long lastProcessingTimeMs;
    private Integer sensorOrientation;
    private Classifier classifier;


    //for getting details
    String deficiency, cropInfo, rotation, inferenceTime;

    //

    @Override
    protected int getLayoutId() {
        return R.layout.camera_connection_fragment;
    }

    @Override
    protected Size getDesiredPreviewFrameSize() {
        return DESIRED_PREVIEW_SIZE;
    }

    @Override
    public void onPreviewSizeChosen(final Size size, final int rotation) {
        final float textSizePx =
                TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_DIP, TEXT_SIZE_DIP, getResources().getDisplayMetrics());
        BorderedText borderedText = new BorderedText(textSizePx);
        borderedText.setTypeface(Typeface.MONOSPACE);

        recreateClassifier(getNumThreads());
        if (classifier == null) {
            return;
        }

        previewWidth = size.getWidth();
        previewHeight = size.getHeight();

        sensorOrientation = rotation - getScreenOrientation();

        rgbFrameBitmap = Bitmap.createBitmap(previewWidth, previewHeight, Config.ARGB_8888);
    }

    @Override
    protected void processImage() {
        rgbFrameBitmap.setPixels(getRgbBytes(), 0, previewWidth, 0, 0, previewWidth, previewHeight);
        final int imageSizeX = classifier.getImageSizeX();
        final int imageSizeY = classifier.getImageSizeY();

        runInBackground(
                () -> {
                    if (classifier != null) {
                        final long startTime = SystemClock.uptimeMillis();
                        final List<Classifier.Recognition> results =
                                classifier.recognizeImage(rgbFrameBitmap, sensorOrientation);
                        lastProcessingTimeMs = SystemClock.uptimeMillis() - startTime;


                        //
                        Classifier.Recognition recognition = results.get(0);
                        if (recognition != null) {
                            if (recognition.getTitle() != null)
                                deficiency = recognition.getTitle();
                            cropInfo = imageSizeX + "x" + imageSizeY;
                            rotation = String.valueOf(sensorOrientation);
                            inferenceTime = lastProcessingTimeMs + "ms";


                            fabPhoto.setOnClickListener(view -> new AsyncTaskRunner().execute());


                        }
                        //end

                        runOnUiThread(
                                () -> {
                                    showResultsInBottomSheet(results);

                                    showCropInfo(imageSizeX + "x" + imageSizeY);
                                    showRotationInfo(String.valueOf(sensorOrientation));
                                    showInference(lastProcessingTimeMs + "ms");

                                });
                    }
                    readyForNextImage();
                });
    }

    @Override
    protected void onInferenceConfigurationChanged() {
        if (rgbFrameBitmap == null) {
            // Defer creation until we're getting camera frames.
            return;
        }
        final int numThreads = getNumThreads();
        runInBackground(() -> recreateClassifier(numThreads));
    }

    private void recreateClassifier(int numThreads) {
        if (classifier != null) {
            classifier.close();
            classifier = null;
        }

        try {
            classifier = Classifier.create(this, numThreads);
        } catch (IOException ignored) {
        }
    }



    @SuppressLint("StaticFieldLeak")
    private class AsyncTaskRunner extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            ImageUtils.saveBitmap(rgbFrameBitmap);

            Bitmap bmp = ImageByteArray.rotateBitmap(ImageUtils.loadBitmap());
            if (deficiency.equalsIgnoreCase("Nitrogen Deficiency")) {
                dbHelper.insertDeficiency(new Deficiency(CurrentDate.getDate(new Date()),
                        deficiency, getResources().getString(R.string.deficiency), "No",
                        ImageByteArray.convertBitmapToByteArray(
                                bmp
                        )));

            } else if (deficiency.equalsIgnoreCase("No deficiency/maize leaf detected")) {
                dbHelper.insertDeficiency(new Deficiency(CurrentDate.getDate(new Date()),
                        deficiency,
                        getResources().getString(R.string.no_deficiency), "No",
                        ImageByteArray.convertBitmapToByteArray(
                                bmp
                        )));
            }
            startActivity(new Intent(ClassifierActivity.this, MainActivity.class));
            finish();

            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog.setTitle("Processing");
            dialog.setMessage("Please wait while the data is populated...");
            dialog.show();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            dialog.dismiss();
        }
    }
}
