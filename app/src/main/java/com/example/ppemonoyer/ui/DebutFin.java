package com.example.ppemonoyer.ui;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.ppemonoyer.R;
import com.example.ppemonoyer.utils.Global;

public class DebutFin extends AppCompatActivity {

    TextView texte;
    Button debut_fin;
    int myDizieme;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_debut_fin);

        texte = findViewById(R.id.text_debut_fin);
        debut_fin = findViewById(R.id.start_quit);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        if (Global.mCurrentPhase == -1){
            texte.setText(R.string.explications);
            debut_fin.setText(R.string.button_start_test);
            debut_fin.setOnClickListener(view -> {
                Intent intent = new Intent(this, LectureActivity.class);
                startActivity(intent);
            });
            Global.mCurrentPhase += 1;
        }else {
            myDizieme = getResources().obtainTypedArray(R.array.diziemes).getInt(Global.mCurrentPhase,0);
            texte.setText(getString(R.string.Score, myDizieme));
            debut_fin.setText(R.string.button_end_test);
            debut_fin.setOnClickListener(view -> {
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
            });
            Global.mCurrentPhase = -1;
        }
    }

}
