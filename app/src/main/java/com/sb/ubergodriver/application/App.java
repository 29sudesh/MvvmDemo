package com.sb.ubergodriver.application;

import android.app.Application;
import android.arch.lifecycle.ProcessLifecycleOwner;
import android.content.Context;
import com.crashlytics.android.Crashlytics;
import com.sb.ubergodriver.di.AppComponent;
import com.sb.ubergodriver.di.AppModule;
import com.sb.ubergodriver.di.DaggerAppComponent;
import com.sb.ubergodriver.di.UtilsModule;
import com.sb.ubergodriver.socket.interfaces.util.AppLifeCycleObserver;
import dagger.Provides;
import io.fabric.sdk.android.Fabric;



/**
 * Created by Sudesh on 05-02-2018.
 */

public class App extends Application {
    public AppComponent appComponent;
    public Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics());
        context = getApplicationContext();
        appComponent = DaggerAppComponent.builder().appModule(new AppModule(this)).utilsModule(new UtilsModule()).build();

        // Observer to detect if the app is in background or foreground.
        AppLifeCycleObserver lifeCycleObserver
                = new AppLifeCycleObserver(getApplicationContext());

        // Adding the above observer to process lifecycle
        ProcessLifecycleOwner.get()
                .getLifecycle()
                .addObserver(lifeCycleObserver);
    }

    public AppComponent getAppComponent() {
        return appComponent;
    }

    public Context getContext()
    {
        return context;
    }

    @Override
    protected void attachBaseContext(Context context) {
        super.attachBaseContext(context);
    }
}
