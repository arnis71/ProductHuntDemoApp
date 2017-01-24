package ru.arnis.producthuntdemoapp;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.Configuration;
import com.activeandroid.app.Application;

/**
 * Created by arnis on 24/01/2017.
 */

public class ProducthuntApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Configuration dbConfiguration = new Configuration.Builder(this).setDatabaseName("PHdata.db").setDatabaseVersion(2).create();
        ActiveAndroid.initialize(dbConfiguration);
    }
}
