package com.visione.amndd.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import uk.co.samuelwall.materialtaptargetprompt.MaterialTapTargetPrompt;

import static com.visione.amndd.utils.Constant.CAMERA;
import static com.visione.amndd.utils.Constant.DELETE;
import static com.visione.amndd.utils.Constant.FIRST_RUN;
import static com.visione.amndd.utils.Constant.INTRODUCTION;
import static com.visione.amndd.utils.Constant.PHOTO;
import static com.visione.amndd.utils.Constant.REPORT;
import static com.visione.amndd.utils.Constant.THREAD;
import static com.visione.amndd.utils.Constant.UPLOAD;

public class Help{
    SharedPreferences preferences;
    SharedPreferences.Editor editor;

    public Help(Context context){
        preferences = context.getSharedPreferences(INTRODUCTION, Context.MODE_PRIVATE);
        editor = preferences.edit();

    }

    public static MaterialTapTargetPrompt.Builder showHelpPrompt(Activity activity, int id, String title, String subtitle) {
        MaterialTapTargetPrompt.Builder mttp = new MaterialTapTargetPrompt.Builder(activity);
        mttp.setTarget(id)
                .setPrimaryText(title)
                .setSecondaryText(subtitle)
                .setBackButtonDismissEnabled(true)
                .show();

        return mttp;

    }

    public void setFirstRun(){
        if(!getFirstRun()) {
            editor.putBoolean(FIRST_RUN, true);
            editor.putBoolean(CAMERA, false);
            editor.putBoolean(DELETE, false);
            editor.putBoolean(UPLOAD, false);
            editor.putBoolean(REPORT, false);
            editor.putBoolean(PHOTO, false);
            editor.putBoolean(THREAD, false);
            editor.apply();
        }
    }

    public boolean getFirstRun(){
        return preferences.getBoolean(FIRST_RUN, false);
    }

    public void saveIntroCamera(boolean state) {
        editor.putBoolean(CAMERA, state);
        editor.apply();
    }

    public void saveIntroDelete(boolean state) {
        editor.putBoolean(DELETE, state);
        editor.apply();
    }

    public void saveIntroUpload(boolean state) {
        editor.putBoolean(UPLOAD, state);
        editor.apply();
    }

    public void saveIntroReport(boolean state) {
        editor.putBoolean(REPORT, state);
        editor.apply();
    }

    public void saveIntroPhoto(boolean state) {
        editor.putBoolean(PHOTO, state);
        editor.apply();
    }

    public void saveIntroThread(boolean state) {
        editor.putBoolean(THREAD, state);
        editor.apply();
    }

    public boolean getIntroCamera() {
        return preferences.getBoolean(CAMERA, false);
    }

    public boolean getIntroDelete() {
        return preferences.getBoolean(DELETE, true);
    }

    public boolean getIntroUpload() {
        return preferences.getBoolean(UPLOAD, true);
    }

    public boolean getIntroReport() {
        return preferences.getBoolean(REPORT, true);
    }

    public boolean getIntroPhoto() {
        return preferences.getBoolean(PHOTO, true);
    }

    public boolean getIntroThread() {
        return preferences.getBoolean(THREAD, true);
    }
}
