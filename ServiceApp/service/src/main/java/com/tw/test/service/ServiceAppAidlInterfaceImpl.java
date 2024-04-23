package com.tw.test.service;

import android.annotation.SuppressLint;

public class ServiceAppAidlInterfaceImpl extends IServiceAppAidlInterface.Stub {

    @SuppressLint("StaticFieldLeak")
    private static ServiceAppAidlInterfaceImpl instance;

    public static ServiceAppAidlInterfaceImpl instance() {
        synchronized (ServiceAppAidlInterfaceImpl.class) {
            if (instance == null) {
                instance = new ServiceAppAidlInterfaceImpl();
            }
        }
        return instance;
    }

    public static void onDestroy() {
        instance = null;
    }

    @Override
    public String mailGreeting() {
        return "Greeting from TW Service";
    }
}
