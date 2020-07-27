package de.tum.in.l4k.binnenvokalapp;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

/**
 * Klasse fuer Level 3. Es wird eine andere ContentView als bei Level 1 verwendet, findViews muss
 * also ueberschrieben werden.
 */
public class PlayMS_3Activity extends PlayMS_1Activity {
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        this.number_of_words = 3;
        super.onCreate(savedInstanceState);

        View v = getWindow().getDecorView();
        v.setSystemUiVisibility(View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
    }

    @Override
    protected void findViews() {
        setContentView(R.layout.activity_play_ms_3w);

        opts = new ImageButton[] {
            findViewById(R.id.ms3b1),
            findViewById(R.id.ms3b2),
            findViewById(R.id.ms3b3)
        };

        speakers = new ImageButton[]{
            findViewById(R.id.ms3play1),
            findViewById(R.id.ms3play2),
            findViewById(R.id.ms3play3)
        };

        tvwords = new TextView[]{
            findViewById(R.id.ms3eword1),
            findViewById(R.id.ms3eword2),
            findViewById(R.id.ms3eword3)
        };
        check = findViewById(R.id.ms3bcheck);
        vocal_iv = findViewById(R.id.ms3vocal);
        speaker_vocal = findViewById(R.id.ms3playvocal);
        starPopup = findViewById(R.id.starPopup3);
        starbar = (LinearLayout)findViewById(R.id.ms3starbar);
        //wrong_popup = findViewById(R.id.ms3wrong);
        activitylayout = findViewById(R.id.ms3_layout);
        home = findViewById(R.id.ms3home);
        help = findViewById(R.id.ms3help);
    }
    @Override
    protected void setLevel() {
        currentLevel = Level.l3;
    }

    @Override
    protected void updateWordsAndVocals() {
        updateVocal(Word.random_vocal());
        updateWords(Word.random_predefined_words_positives_diff(3,1,current_vocal.toChar(),Word.PREDEFINED._MEDIUM_LENGTH()));
    }
}
