package de.tum.in.l4k.binnenvokalapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

/**
 * Beim Start der App implementiert diese Klasse den Willkommensbildschirm.
 */
public class SplashScreenActivity extends AppCompatActivity {

    /**
     * Willkommensbildschirm wird angezeigt.
     * @param savedInstanceState Wird an Superklasse weitergegeben.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View v = getWindow().getDecorView();
        v.setSystemUiVisibility(View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);

        //int colorID = SharedPrefManager.loadColor(this);
        //ColorActivity.setBackgroundColor((ConstraintLayout)findViewById(R.id.colorlayout),colorID, this);
        //SharedPrefManager.reset_all_progress(this);

        startActivity(new Intent(SplashScreenActivity.this, PlayMSActivity.class));
        finish();
    }
}
