package de.tum.in.l4k.binnenvokalapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

/**
 * Alte Klasse, die das Hauptmenue fuer den Singleword-Multivocal-Modus sein sollte.
 */
@Deprecated
public class PlaySMActivity extends AppCompatActivity {

    ImageButton exit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_sm);

        View v = getWindow().getDecorView();
        v.setSystemUiVisibility(View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);

        final MediaPlayer mp = MediaPlayer.create(this, R.raw.telephone);
        mp.start();

        exit = (ImageButton)findViewById(R.id.smExitButton);
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PlaySMActivity.this, MainActivity.class));
                mp.stop();
                finish();
            }
        });
    }
}
