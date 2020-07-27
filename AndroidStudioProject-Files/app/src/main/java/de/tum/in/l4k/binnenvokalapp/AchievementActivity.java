package de.tum.in.l4k.binnenvokalapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

/**
 * Klasse fuer das Achiemenent-Menue.
 */
public class AchievementActivity extends AppCompatActivity {

    ImageButton home, help;
    int[][][] achievementButtonIDs;
    MediaPlayer currentlyPlaying;

    /**
     * Initialisiert das Achievement-Menue.
     * Die Achievement-Pushnotification wird zurueckgesetzt.
     * Views der ContentView werden zugewiesen.
     * Ersetzt die Silhouetten der Achievements durch farbige Drawables, falls das entsprechende
     * Achievement erreicht worden ist. Ruft setIDs() und setOnClickListeners() auf.
     * @param savedInstanceState Wird an Superklasse weitergegeben.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_achievement);

        View vfs = getWindow().getDecorView();
        vfs.setSystemUiVisibility(View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);

        SharedPrefManager.resetAchievementPushInfo(this);

        home = findViewById(R.id.achhome);
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(currentlyPlaying != null && currentlyPlaying.isPlaying()) currentlyPlaying.stop();
                startActivity(new Intent(AchievementActivity.this, PlayMSActivity.class));
                finish();
            }
        });
        help = findViewById(R.id.achhelp);
        help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(currentlyPlaying != null && currentlyPlaying.isPlaying()) currentlyPlaying.stop();
                (currentlyPlaying = MediaPlayer.create(AchievementActivity.this, R.raw.hint_badges)).start();
            }
        });

        int colorID = SharedPrefManager.loadColor(this);
        ColorActivity.setBackgroundColor((ConstraintLayout)findViewById(R.id.achievementlayout),colorID, this);

        setIDs();
        for (int i = 1; i < 6; i++) {
            int passes = SharedPrefManager.passes(SharedPrefManager.levelFromID(i),this);
            if(passes > 0){
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    ImageButton ib =  ((ImageButton)findViewById((achievementButtonIDs[i-1][0][0])));
                    if(!SharedPrefManager.readAchievement(AchievementType.atp1,SharedPrefManager.levelFromID(i),this)){
                        SharedPrefManager.storeAchievement(AchievementType.atp1, SharedPrefManager.levelFromID(i), this);
                       ib.setBackgroundColor(Color.rgb(10,190,250));
                    }
                    ib.setImageDrawable(getDrawable(R.drawable.pokal_bronze));
                }
                if(passes > 4){
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        ImageButton ib =  ((ImageButton)findViewById((achievementButtonIDs[i-1][0][1])));
                        if(!SharedPrefManager.readAchievement(AchievementType.atp2,SharedPrefManager.levelFromID(i),this)){
                            SharedPrefManager.storeAchievement(AchievementType.atp2, SharedPrefManager.levelFromID(i), this);
                            ib.setBackgroundColor(Color.rgb(10,190,250));
                        }
                        ib.setImageDrawable(getDrawable(R.drawable.pokal_silber));
                    }
                    if(passes > 9){
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            ImageButton ib =  ((ImageButton)findViewById((achievementButtonIDs[i-1][0][2])));
                            if(!SharedPrefManager.readAchievement(AchievementType.atp3,SharedPrefManager.levelFromID(i),this)){
                                SharedPrefManager.storeAchievement(AchievementType.atp3, SharedPrefManager.levelFromID(i), this);
                                ib.setBackgroundColor(Color.rgb(10,190,250));
                            }
                            ib.setImageDrawable(getDrawable(R.drawable.pokal_gold));
                        }
                        if(passes > 19) {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                ImageButton ib =  ((ImageButton)findViewById((achievementButtonIDs[i-1][0][3])));
                                if(!SharedPrefManager.readAchievement(AchievementType.atp4,SharedPrefManager.levelFromID(i),this)){
                                    SharedPrefManager.storeAchievement(AchievementType.atp4, SharedPrefManager.levelFromID(i), this);
                                    ib.setBackgroundColor(Color.rgb(10,190,250));
                                }
                                ib.setImageDrawable(getDrawable(R.drawable.pokal_diamant));
                            }
                        }
                    }
                }
            }
            if(SharedPrefManager.readPerfection(SharedPrefManager.levelFromID(i),this)) if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                ImageButton ib =  ((ImageButton)findViewById((achievementButtonIDs[i-1][1][0])));
                if(!SharedPrefManager.readAchievement(AchievementType.atprec,SharedPrefManager.levelFromID(i),this)){
                    SharedPrefManager.storeAchievement(AchievementType.atprec, SharedPrefManager.levelFromID(i), this);
                    ib.setBackgroundColor(Color.rgb(10,190,250));
                }
                ib.setImageDrawable(getDrawable(R.drawable.dart));
            }
            int time = SharedPrefManager.readTime(SharedPrefManager.levelFromID(i), this);
            //Falls die Zeit unter 0 ist, wurde schneller abgeschlossen als die Woerter vorgelesen wurden.
            //Folgende Zeile soll fuer diesen Fall die Achievements bereitstellen.
            if(time < 0) time = 1;
            //Eventuell kommt es zu einem Overflow. Dafuer folgende Zeile:
            if(time > 0x3fff_ffff) time = 1;
            if(time > 0 && time <= 40000){
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    ImageButton ib =  ((ImageButton)findViewById((achievementButtonIDs[i-1][2][7])));
                    if(!SharedPrefManager.readAchievement(AchievementType.att40,SharedPrefManager.levelFromID(i),this)){
                        SharedPrefManager.storeAchievement(AchievementType.att40, SharedPrefManager.levelFromID(i), this);
                        ib.setBackgroundColor(Color.rgb(10,190,250));
                    }
                    ib.setImageDrawable(getDrawable(R.drawable.stopwatch40));
                }
                if(time <= 35000){
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        ImageButton ib =  ((ImageButton)findViewById((achievementButtonIDs[i-1][2][6])));
                        if(!SharedPrefManager.readAchievement(AchievementType.att35,SharedPrefManager.levelFromID(i),this)){
                            SharedPrefManager.storeAchievement(AchievementType.att35, SharedPrefManager.levelFromID(i), this);
                            ib.setBackgroundColor(Color.rgb(10,190,250));
                        }
                        ib.setImageDrawable(getDrawable(R.drawable.stopwatch35));
                    }
                    if(time <= 30000){
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            ImageButton ib =  ((ImageButton)findViewById((achievementButtonIDs[i-1][2][5])));
                            if(!SharedPrefManager.readAchievement(AchievementType.att30,SharedPrefManager.levelFromID(i),this)){
                                SharedPrefManager.storeAchievement(AchievementType.att30, SharedPrefManager.levelFromID(i), this);
                                ib.setBackgroundColor(Color.rgb(10,190,250));
                            }
                            ib.setImageDrawable(getDrawable(R.drawable.stopwatch30));
                        }
                        if(time <= 25000) {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                ImageButton ib =  ((ImageButton)findViewById((achievementButtonIDs[i-1][2][4])));
                                if(!SharedPrefManager.readAchievement(AchievementType.att25,SharedPrefManager.levelFromID(i),this)){
                                    SharedPrefManager.storeAchievement(AchievementType.att25, SharedPrefManager.levelFromID(i), this);
                                    ib.setBackgroundColor(Color.rgb(10,190,250));
                                }
                                ib.setImageDrawable(getDrawable(R.drawable.stopwatch25));
                            }
                            if(time <= 20000) {
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                    ImageButton ib =  ((ImageButton)findViewById((achievementButtonIDs[i-1][2][3])));
                                    if(!SharedPrefManager.readAchievement(AchievementType.att20,SharedPrefManager.levelFromID(i),this)){
                                        SharedPrefManager.storeAchievement(AchievementType.att20, SharedPrefManager.levelFromID(i), this);
                                        ib.setBackgroundColor(Color.rgb(10,190,250));
                                    }
                                    ib.setImageDrawable(getDrawable(R.drawable.stopwatch20));
                                }
                                if(time <= 15000) {
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                        ImageButton ib =  ((ImageButton)findViewById((achievementButtonIDs[i-1][2][2])));
                                        if(!SharedPrefManager.readAchievement(AchievementType.att15,SharedPrefManager.levelFromID(i),this)){
                                            SharedPrefManager.storeAchievement(AchievementType.att15, SharedPrefManager.levelFromID(i), this);
                                            ib.setBackgroundColor(Color.rgb(10,190,250));
                                        }
                                        ib.setImageDrawable(getDrawable(R.drawable.stopwatch15));
                                    }
                                    if(time <= 10000) {
                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                            ImageButton ib =  ((ImageButton)findViewById((achievementButtonIDs[i-1][2][1])));
                                            if(!SharedPrefManager.readAchievement(AchievementType.att10,SharedPrefManager.levelFromID(i),this)){
                                                SharedPrefManager.storeAchievement(AchievementType.att10, SharedPrefManager.levelFromID(i), this);
                                                ib.setBackgroundColor(Color.rgb(10,190,250));
                                            }
                                            ib.setImageDrawable(getDrawable(R.drawable.stopwatch10));
                                        }
                                        if(time <= 5000) {
                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                                ImageButton ib =  ((ImageButton)findViewById((achievementButtonIDs[i-1][2][0])));
                                                if(!SharedPrefManager.readAchievement(AchievementType.att5,SharedPrefManager.levelFromID(i),this)){
                                                    SharedPrefManager.storeAchievement(AchievementType.att5, SharedPrefManager.levelFromID(i), this);
                                                    ib.setBackgroundColor(Color.rgb(10,190,250));
                                                }
                                                ib.setImageDrawable(getDrawable(R.drawable.stopwatch05));
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        setOnClickListeners();
    }

    /**
     * Speichert die Resource-IDs der Achievement-ImageButtons in achievementButtonIDs.
     */
    protected void setIDs(){
        achievementButtonIDs = new int[][][]{
                {
                        {R.id.ach1bronze, R.id.ach1silber, R.id.ach1gold, R.id.ach1diamant},
                        {R.id.ach1precision},
                        {R.id.ach1time5, R.id.ach1time10, R.id.ach1time15, R.id.ach1time20, R.id.ach1time25, R.id.ach1time30, R.id.ach1time35, R.id.ach1time40}
                },
                {
                        {R.id.ach2bronze, R.id.ach2silber, R.id.ach2gold, R.id.ach2diamant},
                        {R.id.ach2precision},
                        {R.id.ach2time5, R.id.ach2time10, R.id.ach2time15, R.id.ach2time20, R.id.ach2time25, R.id.ach2time30, R.id.ach2time35, R.id.ach2time40}
                },
                {
                        {R.id.ach3bronze, R.id.ach3silber, R.id.ach3gold, R.id.ach3diamant},
                        {R.id.ach3precision},
                        {R.id.ach3time5, R.id.ach3time10, R.id.ach3time15, R.id.ach3time20, R.id.ach3time25, R.id.ach3time30, R.id.ach3time35, R.id.ach3time40}
                },
                {
                        {R.id.ach4bronze, R.id.ach4silber, R.id.ach4gold, R.id.ach4diamant},
                        {R.id.ach4precision},
                        {R.id.ach4time5, R.id.ach4time10, R.id.ach4time15, R.id.ach4time20, R.id.ach4time25, R.id.ach4time30, R.id.ach4time35, R.id.ach4time40}
                },
                {
                        {R.id.ach5bronze, R.id.ach5silber, R.id.ach5gold, R.id.ach5diamant},
                        {R.id.ach5precision},
                        {R.id.ach5time5, R.id.ach5time10, R.id.ach5time15, R.id.ach5time20, R.id.ach5time25, R.id.ach5time30, R.id.ach5time35, R.id.ach5time40}
                }
        };
    }
    protected int[][] achievement_raws = new int[][]{
            {R.raw.hint_pokbronze, R.raw.hint_poksilber, R.raw.hint_pokgold, R.raw.hint_pokdiamant},
            {R.raw.hint_perfect},
            {R.raw.hint_time05, R.raw.hint_time10, R.raw.hint_time15, R.raw.hint_time20, R.raw.hint_time25, R.raw.hint_time30, R.raw.hint_time35, R.raw.hint_time40}
    };

    /**
     * Setzt die OnClickListeners der Achievement-Buttons.
     */
    protected void setOnClickListeners(){
        for(int[][] ia : achievementButtonIDs){
            for(int i = 0; i < ia.length; i++){
                for(int j = 0; j < ia[i].length; j++){
                    ImageButton ib = findViewById(ia[i][j]);
                    final int finalI = i;
                    final int finalJ = j;
                    ib.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(currentlyPlaying != null && currentlyPlaying.isPlaying()) currentlyPlaying.stop();
                            (currentlyPlaying = MediaPlayer.create(AchievementActivity.this, achievement_raws[finalI][finalJ])).start();
                        }
                    });
                }
            }
        }
    }
}
