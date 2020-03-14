package gr.stratego.patrastournament.me;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDexApplication;

import com.google.firebase.FirebaseApp;

import gr.stratego.patrastournament.me.Models.AppSettings;
import gr.stratego.patrastournament.me.Models.User;
import timber.log.Timber;

public class StrategoApplication extends MultiDexApplication {

    private static Context mContext;
    private static AppSettings mAppSettings;
    private static User currentUser;


    @Override
    public void onCreate(){
        super.onCreate();
        FirebaseApp.initializeApp(this);

        initTimber();
        mContext = this;
    }

    public static Context getContext(){
        return mContext;
    }

    private void initTimber() {
        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }
    }

    public static AppSettings getAppSettings() {
        return mAppSettings;
    }

    public static void setAppSettings(AppSettings appSettings) {
        mAppSettings = appSettings;
    }

    public static User getCurrentUser() {
        return currentUser;
    }

    public static void setCurrentUser(User currentUser) {
        StrategoApplication.currentUser = currentUser;
    }
}
