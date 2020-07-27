package de.tum.in.l4k.binnenvokalapp;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

/**
 * Klasse fuer Level 5. Es wird eine andere ContentView als bei Level 1 verwendet, findViews muss
 * also ueberschrieben werden.
 */
public class PlayMS_5Activity  extends PlayMS_1Activity {
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        this.number_of_words = 4;
        super.onCreate(savedInstanceState);
        //View v = getWindow().getDecorView();
        //v.setSystemUiVisibility(View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
    }

    @Override
    protected void findViews() {
        setContentView(R.layout.activity_play_ms_4w);

        opts = new ImageButton[]{
            findViewById(R.id.ms4b1),
            findViewById(R.id.ms4b2),
            findViewById(R.id.ms4b3),
            findViewById(R.id.ms4b4)
        };

        speakers = new ImageButton[]{
            findViewById(R.id.ms4play1),
            findViewById(R.id.ms4play2),
            findViewById(R.id.ms4play3),
            findViewById(R.id.ms4play4)
        };

        tvwords = new TextView[]{
                findViewById(R.id.ms4eword1),
                findViewById(R.id.ms4eword2),
                findViewById(R.id.ms4eword3),
                findViewById(R.id.ms4eword4)
        };
        check = findViewById(R.id.ms4bcheck);
        vocal_iv = findViewById(R.id.ms4vocal);
        speaker_vocal = findViewById(R.id.ms4playvocal);
        starPopup = findViewById(R.id.starPopup4);
        starbar = (LinearLayout)findViewById(R.id.ms4starbar);
        //wrong_popup = findViewById(R.id.ms4wrong);
        activitylayout = findViewById(R.id.ms4_layout);
        home = findViewById(R.id.ms4home);
        help = findViewById(R.id.ms4help);
    }

    @Override
    protected void setLevel() {
        currentLevel = Level.l5;
    }
    @Override
    protected void updateWordsAndVocals() {
        updateVocal(Word.random_vocal());
        //updateWords(Word.random_predefined_words_positives_diff(4,2,current_vocal.toChar(),Word.PREDEFINED._ALL));
        updateWords(Word.random_predefined_words_diff(4,Word.PREDEFINED._HARD_LENGTH()));
    }
}
