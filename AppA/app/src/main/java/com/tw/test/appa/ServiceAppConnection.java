package com.tw.test.appa;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import com.tw.test.service.IServiceAppAidlInterface;

public class ServiceAppConnection implements ServiceConnection {

    private IServiceAppAidlInterface serviceAppAidlInterface;
    @Override
    public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
        serviceAppAidlInterface = IServiceAppAidlInterface.Stub.asInterface(iBinder);
        Log.d("AppA", "Service Connected!!!");
    }

    @Override
    public void onServiceDisconnected(ComponentName componentName) {
        serviceAppAidlInterface = null;
        Log.d("AppA", "Service Dis-Connected!!!");
    }

    public String mailGreeting() throws RemoteException {
        if (serviceAppAidlInterface != null)
            return serviceAppAidlInterface.mailGreeting();
        else
            return "Service Application is not connected to AppA";
    }
}
