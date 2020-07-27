package de.tum.in.l4k.binnenvokalapp;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.PersistableBundle;

import androidx.annotation.Nullable;

/**
 * Klasse fuer Level 4. Die gleiche ContentView wie bei Level 3 wird verwendet, findViews muss also
 * nicht ueberschrieben werden.
 */
public class PlayMS_4Activity  extends PlayMS_3Activity {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_ms_3w);
    }

    @Override
    protected void setLevel() {
        currentLevel = Level.l4;
    }

    @Override
    protected void updateWordsAndVocals() {
        updateVocal(Word.random_vocal());
        updateWords(Word.random_predefined_words_diff(3,Word.union(Word.PREDEFINED._MEDIUM_LENGTH(),Word.PREDEFINED._HARD_LENGTH())));
    }
}
