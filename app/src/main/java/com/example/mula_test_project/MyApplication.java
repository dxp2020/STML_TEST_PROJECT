package com.example.mula_test_project;

import com.mula.base.BaseApplication;

public class MyApplication extends BaseApplication {
    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public String getAppFlavor() {
        return BuildConfig.FLAVOR;
    }

    @Override
    public int getLevel() {
        return LEVEL_APP;
    }

    @Override
    public Class[] subDelegates() {
        return new Class[0];
    }

    @Override
    public void onCreateDelegate() {

    }
}
