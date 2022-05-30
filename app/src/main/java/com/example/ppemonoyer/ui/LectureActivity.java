package com.example.ppemonoyer.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.text.Layout;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintSet;

import com.example.ppemonoyer.R;
import com.example.ppemonoyer.utils.Global;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.Random;

import io.grpc.internal.ConnectionClientTransport;

public class LectureActivity extends AppCompatActivity {

    Class<R.array> res;
    Field field;

    int scaling;
    String myString;
    int mySize;
    String lettreCible;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lecture);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        myString = getResources().obtainTypedArray(R.array.suite_lettre).getString(Global.mCurrentPhase);
        scaling = getResources().getInteger(R.integer.scaling);
        mySize = getResources().obtainTypedArray(R.array.sizes).getInt(Global.mCurrentPhase,0)/scaling;

        final char lettreCible = lettre_aleat(myString);

        createCibleButton(lettreCible);

        int id=0;
        for (String letter : myString.split("")) {
            Button myButton = new Button(this);
            myButton.setId(id++);
            myButton.setText(letter);
            myButton.setTextSize(mySize);
            myButton.setBackgroundColor(getResources().getColor(R.color.white));
            LinearLayout ll1 = (LinearLayout) findViewById(R.id.layout);
            int width = getResources().getDisplayMetrics().widthPixels/myString.length();
            myButton.setLayoutParams(new RelativeLayout.LayoutParams(width, LinearLayout.LayoutParams.WRAP_CONTENT));
            ll1.addView(myButton);
            //create closure
            final char Lettre = letter.charAt(0);
            myButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    if (Lettre == lettreCible){

                        Global.mCurrentScore += 1;

                        // 2 success -> next level
                        if (Global.mCurrentScore == 2) {
                            Global.mCurrentScore = 0;
                            Global.mCurrentPhase += 1;
                            if (Global.mCurrentPhase >= 7) {
                                startDebutFinActivity();
                                return;
                            } // no more levels
                        }
                        recreate();
                    }else{
                        startDebutFinActivity();
                    }
                }

            }); //setOnClickListener
        } // for
    }

    private void createCibleButton(char lettreCible){
        Button mButton = new Button(LectureActivity.this);
        mButton.setText(Character.toString(lettreCible));
        mButton.setTextSize(200);
        mButton.setBackgroundColor(getResources().getColor(R.color.lightGrey));
        mButton.setTextColor(getResources().getColor(R.color.colorGreen));
        LinearLayout ll = (LinearLayout) findViewById(R.id.layout2);
        mButton.setLayoutParams(new RelativeLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        ll.addView(mButton);
    }

    private char lettre_aleat(String chaine) {

        String[] separated = chaine.split("");

        final int random = new Random().nextInt(separated.length);
        return separated[random].charAt(0);
    }

    public  void startDebutFinActivity(){
        Intent intent = new Intent(LectureActivity.this, DebutFin.class);
        startActivity(intent);
    }

    @Override
    public boolean onKeyDown(int keycode, KeyEvent event) {
        Log.d("TAG", "onKeyDown: ");
        if (keycode == KeyEvent.KEYCODE_BACK) {
            Global.mCurrentPhase = -1;
            startDebutFinActivity();
            return true; //intercept event back
        }else{
            return false;
        }
    }
}
