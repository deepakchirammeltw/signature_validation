package com.tw.test.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;

public class ServiceApp extends Service {

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return ServiceAppAidlInterfaceImpl.instance(this);
    }

    @Override
    public void onDestroy() {
        ServiceAppAidlInterfaceImpl.onDestroy();
        super.onDestroy();
    }
}
