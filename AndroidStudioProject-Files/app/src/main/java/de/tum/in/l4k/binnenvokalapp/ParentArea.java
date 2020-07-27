package de.tum.in.l4k.binnenvokalapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.Toast;

public class ParentArea extends AppCompatActivity {

    PopupWindow pw;
    PopupWindow confirmResetPW;
    ConstraintLayout layout;

    //ImageButton backbutton_impressum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent_area);

        View vfs = getWindow().getDecorView();
        vfs.setSystemUiVisibility(View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);

        layout = findViewById(R.id.layout_parent_area);

        int colorID = SharedPrefManager.loadColor(this);
        ColorActivity.setBackgroundColor(layout,colorID, this);

        LayoutInflater lf = (LayoutInflater)(this.getSystemService(Context.LAYOUT_INFLATER_SERVICE));
        View v = lf.inflate(R.layout.popup_impressum, null);
        pw = new PopupWindow(this);
        pw.setContentView(v);

        LayoutInflater lf2 = (LayoutInflater)(this.getSystemService(Context.LAYOUT_INFLATER_SERVICE));
        View v2 = lf2.inflate(R.layout.popup_confirm_reset_progress, null);
        confirmResetPW = new PopupWindow(this);
        confirmResetPW.setContentView(v2);


        Button impressum_button = findViewById(R.id.parentarea_impressum);
        impressum_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pw.showAtLocation(layout, Gravity.CENTER, 10, 10);
                //backbutton_impressum = pw.getContentView().findViewById(R.id.impressum_backbutton);
            }
        });
        Button resetButton = findViewById(R.id.resetbutton);
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //SharedPrefManager.reset_all_progress(ParentArea.this);
                //Toast.makeText(ParentArea.this, "Fortschritt zurueckgesetzt!", Toast.LENGTH_LONG).show();
                confirmResetPW.showAtLocation(layout, Gravity.CENTER, 0, 0);
            }
        });


        ImageButton home = findViewById(R.id.parentarea_home);
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                startActivity(new Intent(ParentArea.this, PlayMSActivity.class));
                finish();
            }
        });
        ImageButton backbutton_impressum = pw.getContentView().findViewById(R.id.impressum_backbutton);
        backbutton_impressum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pw.dismiss();
            }
        });
        ImageButton backbutton_reset = confirmResetPW.getContentView().findViewById(R.id.reset_progr_backbutton);
        backbutton_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmResetPW.dismiss();
            }
        });
        Button reset_button_no = confirmResetPW.getContentView().findViewById(R.id.reset_progr_cancel_button);
        reset_button_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmResetPW.dismiss();
            }
        });
        Button reset_button_yes = confirmResetPW.getContentView().findViewById(R.id.reset_progr_confirm_button);
        reset_button_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPrefManager.reset_all_progress(ParentArea.this);
                Toast.makeText(ParentArea.this, "Fortschritt zurueckgesetzt!", Toast.LENGTH_LONG).show();
                confirmResetPW.dismiss();
            }
        });
    }
}
