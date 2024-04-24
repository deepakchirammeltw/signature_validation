package com.tw.test.service;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.util.Log;

import java.io.ByteArrayInputStream;
import java.security.MessageDigest;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

public class ServiceAppAidlInterfaceImpl extends IServiceAppAidlInterface.Stub {

    @SuppressLint("StaticFieldLeak")
    private static ServiceAppAidlInterfaceImpl instance;
    private static final String SIGNATURE_SHA = "90:42:FA:E9:3D:F9:00:2D:8B:2C:FF:DD:15:C8:8E:01:B5:96:A9:8E";
    private final Context context;

    private ServiceAppAidlInterfaceImpl(Context context) {
        this.context = context;
    }

    public static ServiceAppAidlInterfaceImpl instance(Context context) {
        synchronized (ServiceAppAidlInterfaceImpl.class) {
            if (instance == null) {
                instance = new ServiceAppAidlInterfaceImpl(context);
            }
        }
        return instance;
    }

    public static void onDestroy() {
        instance = null;
    }

    @Override
    public String mailGreeting() {
        validateSignatureSHA();
        return "Greeting from TW Service";
    }

    private void validateSignatureSHA() {
        String callerSignatureSHA = "";
        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo packageInfo = pm.getPackageInfo(pm.getNameForUid(getCallingUid()), PackageManager.GET_SIGNATURES);
            Signature[] signatures = packageInfo.signatures;
            byte[] certBytes = signatures[0].toByteArray();
            CertificateFactory cf = CertificateFactory.getInstance("X509");
            X509Certificate cert = (X509Certificate) cf.generateCertificate(new ByteArrayInputStream(certBytes));
            MessageDigest md = MessageDigest.getInstance("SHA1");
            byte[] encodedCert = md.digest(cert.getEncoded());
            callerSignatureSHA = byte2HexFormatted(encodedCert);
        } catch (Exception e) {
            Log.e("ServiceApp", "Unable to get certificate from connecting client");
        }
        if (!SIGNATURE_SHA.equals(callerSignatureSHA))
            throw new SecurityException("Signature Mismatch: Unable to access ServiceApp AIDL APIs");
    }

    private static String byte2HexFormatted(byte[] arr) {
        StringBuilder str = new StringBuilder(arr.length * 2);
        for (int i = 0; i < arr.length; i++) {
            String h = Integer.toHexString(arr[i]);
            int l = h.length();
            if (l == 1) h = "0" + h;
            if (l > 2) h = h.substring(l - 2, l);
            str.append(h.toUpperCase());
            if (i < (arr.length - 1)) str.append(':');
        }
        return str.toString();
    }
}
