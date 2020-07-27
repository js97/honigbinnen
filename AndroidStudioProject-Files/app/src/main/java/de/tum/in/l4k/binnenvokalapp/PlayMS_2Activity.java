package de.tum.in.l4k.binnenvokalapp;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.widget.Toast;

import androidx.annotation.Nullable;

/**
 * Klasse fuer Level 2. Die gleiche ContentView wie bei Level 1 wird verwendet, findViews muss also
 * nicht ueberschrieben werden.
 */
public class PlayMS_2Activity extends PlayMS_1Activity {
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    protected void setLevel() {
        currentLevel = Level.l2;
    }

    @Override
    protected void updateWordsAndVocals() {
        updateVocal(Word.random_vocal());
        updateWords(Word.random_predefined_words_diff(2,Word.union(Word.PREDEFINED._MEDIUM_LENGTH(),Word.PREDEFINED._EASY_LENGTH())));
    }
}
