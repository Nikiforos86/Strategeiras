package gr.stratego.patrastournament.me.Utils;

import android.content.Context;
import android.content.SharedPreferences;

import gr.stratego.patrastournament.me.StrategoApplication;

public class SharedPreferencesUtil {

    public static final String PACKAGE = "gr.stratego.patrastournament.";

    public static final String UserId = PACKAGE+"UserId";
    public static final String UserLogged = PACKAGE+"UserLogged";
//    public static final

    public static void saveSharedPreference(String valueToSave, String dir, String fileName) {
        try {
            fileName = checkFilename(dir, fileName);
            SharedPreferences.Editor editor = getSharedEditor(dir);
            editor.putString(fileName, valueToSave);
            editor.commit();
        } catch (Exception e) {
            String error = e.getMessage();
        }
    }

    public static void saveSharedPreference(boolean valueToSave, String dir, String fileName) {
        try {
            fileName = checkFilename(dir, fileName);
            SharedPreferences.Editor editor = getSharedEditor(dir);
            editor.putBoolean(fileName, valueToSave);
            editor.commit();
        } catch (Exception e) {
            String error = e.getMessage();
        }
    }

    public static void saveSharedPreference(int valueToSave, String dir, String fileName) {
        try {
            fileName = checkFilename(dir, fileName);
            SharedPreferences.Editor editor = getSharedEditor(dir);
            editor.putInt(fileName, valueToSave);
            editor.commit();
        } catch (Exception e) {
            String error = e.getMessage();
        }
    }

    public static void saveSharedPreference(long valueToSave, String dir, String fileName) {
        try {
            fileName = checkFilename(dir, fileName);
            SharedPreferences.Editor editor = getSharedEditor(dir);
            editor.putLong(fileName, valueToSave);
            editor.commit();
        } catch (Exception e) {
            String error = e.getMessage();
        }
    }

    public static String loadSharedPreference(String dir, String fileName) {
        String returnValue = "";
        try {
            fileName = checkFilename(dir, fileName);
            SharedPreferences sharedPref = StrategoApplication.getContext().getSharedPreferences(dir, Context.MODE_PRIVATE);
            returnValue = sharedPref.getString(fileName, returnValue);
        } catch (Exception e) {
            returnValue = "";
        }
        return returnValue;
    }

    public static boolean loadSharedPreference(String dir, String fileName, boolean defaultValue) {
        boolean returnValue = defaultValue;
        try {
            fileName = checkFilename(dir, fileName);
            SharedPreferences sharedPref = StrategoApplication.getContext().getSharedPreferences(dir, Context.MODE_PRIVATE);
            returnValue = sharedPref.getBoolean(fileName, defaultValue);
        } catch (Exception e) {
            String error = e.getMessage();
        }
        return returnValue;
    }

    public static int loadSharedPreference(String dir, String fileName, int defaultValue) {
        int returnValue = defaultValue;
        try {
            fileName = checkFilename(dir, fileName);
            SharedPreferences sharedPref = StrategoApplication.getContext().getSharedPreferences(dir, Context.MODE_PRIVATE);
            returnValue = sharedPref.getInt(fileName, defaultValue);
        } catch (Exception e) {
            String error = e.getMessage();
        }
        return returnValue;
    }

    public static long loadSharedPreference(String dir, String fileName, long defaultValue) {
        long returnValue = defaultValue;
        try {
            fileName = checkFilename(dir, fileName);
            SharedPreferences sharedPref = StrategoApplication.getContext().getSharedPreferences(dir, Context.MODE_PRIVATE);
            returnValue = sharedPref.getLong(fileName, defaultValue);
        } catch (Exception e) {
            String error = e.getMessage();
        }
        return returnValue;
    }


    private static String checkFilename(String dir, String fileName) {
        if (StringUtils.isNullOrEmpty(fileName)) {
            fileName = dir;
        }
        return fileName;
    }

    private static SharedPreferences.Editor getSharedEditor(String dir) {
        SharedPreferences sharedPref = StrategoApplication.getContext().getSharedPreferences(dir, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        return editor;
    }

    /**
     * This is used is order to delete every user data we have. It's used is user's logout.
     */
    public static void deleteUserData() {

    }
}