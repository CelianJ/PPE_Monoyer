package com.example.ppemonoyer.ui;

import android.content.Context;
import android.content.Intent;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.os.Bundle;
import android.util.Log;

import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.ppemonoyer.R;
import com.example.ppemonoyer.utils.FindController;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ParametersActivity extends AppCompatActivity {

    private Button buttonValidation;
    private ImageView mImageEtatCapteur;
    private TextView NomControlleur;
    private Button validerButton;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parametre);

        validerButton = findViewById(R.id.activity_parametre_valider_button);
        mImageEtatCapteur = findViewById(R.id.parametre_etat_controlleur);
        NomControlleur = findViewById(R.id.activity_parametre_controlleur_name);
    }

    @Override
    protected void onStart() {
        super.onStart();
        String controllerName = getControllerName();

        if (!controllerName.equals("")){
            NomControlleur.setText(getControllerName());
        } else{
            NomControlleur.setText(R.string.NoControler);
        }

        UsbManager systemService = (UsbManager) this.getSystemService(Context.USB_SERVICE);

        ScheduledThreadPoolExecutor stpe = new ScheduledThreadPoolExecutor(1);

        FindController fc = new FindController(systemService, mImageEtatCapteur);
        ParametersActivity parametersActivity = this;
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                parametersActivity.runOnUiThread(fc);
            }
        };
        ScheduledFuture<?> scheduledFuture = stpe.scheduleAtFixedRate(runnable, 0, 2l, TimeUnit.SECONDS);

        validerButton.setOnClickListener(view -> {
            startMainActivity();
        });

    }

    private String getControllerName() {

        String device = "";

        UsbManager usbManager = (UsbManager) getSystemService(USB_SERVICE);
        HashMap<String, UsbDevice> deviceList = usbManager.getDeviceList();
        for (Map.Entry<String, UsbDevice> entry : deviceList.entrySet()) {
            device = entry.getValue().getProductName();
        }
        return device;
    }

    private void startMainActivity(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

}
