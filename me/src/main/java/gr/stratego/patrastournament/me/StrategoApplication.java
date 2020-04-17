package gr.stratego.patrastournament.me;

import android.content.Context;
import androidx.multidex.MultiDexApplication;

import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.RequestConfiguration;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Arrays;
import java.util.List;

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
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);

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
