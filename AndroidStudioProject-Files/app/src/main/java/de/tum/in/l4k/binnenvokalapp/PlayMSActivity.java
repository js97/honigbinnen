package de.tum.in.l4k.binnenvokalapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import java.util.Arrays;


/**
 * Klasse fuer das Hauptmenue.
 */
public class PlayMSActivity extends AppCompatActivity {

    ImageButton l1, l2, l3, l4, l5, ltut;
    ImageView[] locks;
    ImageButton exit, achievements, colors;
    boolean[] level_passes;
    MediaPlayer currentlyPlaying;

    PopupWindow confirm_exit_window;

    ImageButton parentarea;
    /**
     * Initialisiert das Hauptmenue.
     * Geht in den Vollbildmodus, setzt das Farbschema.
     * Ueberprueft die bisherigen Freischaltungen und sperrt noch nicht freigeschaltete Level.
     * Setzt die Views aus der ContentView.
     * Ueberprueft auch die Pushnotification ueber neue Achievements und zeigt ggf. einen graphischen
     * Hinweis an.
     * @param savedInstanceState Wird an Superklasse weitergegeben.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_ms);

        View v = getWindow().getDecorView();
        v.setSystemUiVisibility(View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);

        //Toast.makeText(this, ""+SharedPrefManager.passes(Level.l1,this),Toast.LENGTH_LONG).show();

        int colorID = SharedPrefManager.loadColor(this);
        ColorActivity.setBackgroundColor((ConstraintLayout)findViewById(R.id.mslayout),colorID, this);

        //final MediaPlayer mp = MediaPlayer.create(this, R.raw.shortclick);
        //mp.start();
        instantiate_popup_windows();

        level_passes = new boolean[6];
        Arrays.fill(level_passes, false);
        locks = new ImageView[]{
            findViewById(R.id.ms1lockl),
            findViewById(R.id.ms2lockl),
            findViewById(R.id.ms3lockl),
            findViewById(R.id.ms4lockl),
            findViewById(R.id.ms5lockl)
        };
        for(int i = 0; i < 6; i++){
            level_passes[i] = StorageSelectingManager.passed(StorageSelectingManager.levelFromID(i),this);
            if(i < 5){
                locks[i].setVisibility(level_passes[i] ? View.INVISIBLE : View.VISIBLE);
            }
        }
        //Toast.makeText(this, ""+level_passes[0], Toast.LENGTH_LONG).show();

        ltut = findViewById(R.id.mstButton);
        ltut.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if(currentlyPlaying != null && currentlyPlaying.isPlaying()) currentlyPlaying.stop();
                startActivity(new Intent(PlayMSActivity.this, TutorialMSActivity.class));
                finish();
            }
        });
        l1 = findViewById(R.id.msl1button);
        l2 = findViewById(R.id.msl2button);
        l3 = findViewById(R.id.msl3button);
        l4 = findViewById(R.id.msl4button);
        l5 = findViewById(R.id.msl5button);
        for(int i = 0; i < level_passes.length-1; i++){
            final int finalI = i;
            new ImageButton[]{l1,l2,l3,l4,l5}[i].setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    if(PlayMSActivity.this.level_passes[finalI]) {
                        if(currentlyPlaying != null && currentlyPlaying.isPlaying()) currentlyPlaying.stop();
                        startActivity(new Intent(PlayMSActivity.this, new Class[]{
                                PlayMS_1Activity.class, PlayMS_2Activity.class, PlayMS_3Activity.class, PlayMS_4Activity.class, PlayMS_5Activity.class}[finalI]));
                        finish();
                    }
                    else {
                        wiggleImageView(locks[finalI]);
                        currentlyPlaying = MediaPlayer.create(PlayMSActivity.this, R.raw.locked);
                        currentlyPlaying.start();
                    }
                }
            });
        }
        exit = findViewById(R.id.msexit);
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(currentlyPlaying != null && currentlyPlaying.isPlaying()) currentlyPlaying.stop();
                currentlyPlaying = MediaPlayer.create(PlayMSActivity.this, R.raw.spielwirklichverlassen);
                currentlyPlaying.start();
                confirm_exit_window.showAtLocation(v, Gravity.CENTER, 0, 0);
                /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    PlayMSActivity.this.finishAndRemoveTask();
                } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    PlayMSActivity.this.finishAffinity();
                } else {
                    PlayMSActivity.this.finish();
                }*/
            }
        });
        colors = findViewById(R.id.mscolors);
        colors.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(currentlyPlaying != null && currentlyPlaying.isPlaying()) currentlyPlaying.stop();
                startActivity(new Intent(PlayMSActivity.this, ColorActivity.class));
                finish();
            }
        });
        achievements = findViewById(R.id.msach);
        achievements.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(currentlyPlaying != null && currentlyPlaying.isPlaying()) currentlyPlaying.stop();
                startActivity(new Intent(PlayMSActivity.this, AchievementActivity.class));
                finish();
            }
        });

        parentarea = findViewById(R.id.msparentarea);
        parentarea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(currentlyPlaying != null && currentlyPlaying.isPlaying()) currentlyPlaying.stop();
                startActivity(new Intent(PlayMSActivity.this, ParentArea.class));
                finish();
            }
        });

        if(SharedPrefManager.readAchievementPushInfo(this)) {
            ImageView achievementPushInfo = findViewById(R.id.newachievement);
            achievementPushInfo.setAlpha(1f);
            wiggleImageView(achievementPushInfo);
            currentlyPlaying = MediaPlayer.create(this, R.raw.new_trophy_message);
            currentlyPlaying.start();
        }


    }

    protected void instantiate_popup_windows(){
        LayoutInflater lf = (LayoutInflater)(this.getSystemService(Context.LAYOUT_INFLATER_SERVICE));
        View cewv = lf.inflate(R.layout.popup_confirm_closure, null);
        TextView tvc = cewv.findViewById(R.id.exit_confirm_question);
        tvc.setText(R.string.exit_confirm);
        confirm_exit_window = new PopupWindow(this);
        confirm_exit_window.setContentView(cewv);
        ImageButton cew_yes = cewv.findViewById(R.id.exit_confirm_button);
        ImageButton cew_no = cewv.findViewById(R.id.exit_cancel_button);
        //ImageButton cew_back = cewv.findViewById(R.id.exit_backbutton);
        //Somehow, the image is not loaded, this dynamic call is just a workaround
        //TODO: Fix image loading error
        cew_no.setImageDrawable(getResources().getDrawable(R.drawable.pokal, null));
        cew_yes.setImageDrawable((getResources().getDrawable(R.drawable.exit, null)));
        cew_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(currentlyPlaying != null && currentlyPlaying.isPlaying()) currentlyPlaying.stop();
                //TODO: Eventuell noch vorlesen, dass verlassen wird, damit Kinder das koennen
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    PlayMSActivity.this.finishAndRemoveTask();
                } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    PlayMSActivity.this.finishAffinity();
                } else {
                    PlayMSActivity.this.finish();
                }
            }
        });
        View.OnClickListener ocl_no = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(currentlyPlaying != null && currentlyPlaying.isPlaying()) currentlyPlaying.stop();
                confirm_exit_window.dismiss();
            }
        };
        cew_no.setOnClickListener(ocl_no);
        //cew_back.setOnClickListener(ocl_no);
    }

    /**
     * Animiert eine ImageView mit der wiggle-Animation.
     * @param iv Zu animierende ImageView.
     */
    protected void wiggleImageView(ImageView iv){
        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.wiggle);
        iv.startAnimation(animation);
    }

    /**
     * Aktualisiert die Farbe.
     */
    @Override
    protected void onResume() {
        super.onResume();
        int colorID = SharedPrefManager.loadColor(this);
        ColorActivity.setBackgroundColor((ConstraintLayout)findViewById(R.id.mslayout),colorID, this);
    }
}
