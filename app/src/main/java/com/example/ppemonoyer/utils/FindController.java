package com.example.ppemonoyer.utils;

import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.widget.ImageView;

import com.example.ppemonoyer.R;

import java.util.HashMap;
import java.util.Map;

public class FindController extends Thread {

    public static boolean capteurEtat = false;
    private UsbManager manager;
    private ImageView mCapteurEtatImageView;

    public FindController(UsbManager manager, ImageView mCapteurEtatImageView) {
        this.mCapteurEtatImageView = mCapteurEtatImageView;
        this.manager = manager;
    }

    public void run() {

        String device = null;

        HashMap<String, UsbDevice> deviceList = manager.getDeviceList();
        if (deviceList != null && deviceList.size() > 0) {
            for (Map.Entry<String, UsbDevice> entry : deviceList.entrySet()) {
                device = entry.getValue().getProductName();
            }
        }

        if (device != null) {
            mCapteurEtatImageView.setImageResource(R.drawable.greendot);
        } else {
            mCapteurEtatImageView.setImageResource(R.drawable.reddot);
        }
    }
}