package com.sb.ubergodriver.di;

import android.content.Context;
import com.sb.ubergodriver.apiConstants.ViewModelFactory;
import dagger.Module;
import dagger.Provides;

import javax.inject.Singleton;

/**
 * Created by ${Saquib} on 03-05-2018.
 */

@Module
public class AppModule {
    public Context context;
    public AppModule(Context context) {
        this.context = context;
    }

    @Provides
    @Singleton
    public Context provideContext() {
        return context;
    }

}
