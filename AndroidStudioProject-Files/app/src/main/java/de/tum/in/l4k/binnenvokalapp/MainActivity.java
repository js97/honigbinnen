package de.tum.in.l4k.binnenvokalapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

/**
 * Altes Hauptmenue, geplant waren mehr Modi als Multiword-Singlevocal.
 */
@Deprecated
public class MainActivity extends AppCompatActivity {

    //ImageButton play, exit, settings;
    ImageButton level1Button, level2Button, level3Button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        View v = getWindow().getDecorView();
        v.setSystemUiVisibility(View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);

        //final MediaPlayer mp = MediaPlayer.create(this, R.raw.shortclick);

        level1Button = findViewById(R.id.level1Button);
        level1Button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                //Toast clicked = Toast.makeText(MainActivity.this, "Clicked", Toast.LENGTH_SHORT);
                //clicked.show();
                //mp.start();
                startActivity(new Intent(MainActivity.this, PlaySSActivity.class));
                //finish();
            }
        });
        level2Button = findViewById(R.id.level2Button);
        level2Button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                //Toast clicked = Toast.makeText(MainActivity.this, "Clicked", Toast.LENGTH_SHORT);
                //clicked.show();
                //mp.start();
                startActivity(new Intent(MainActivity.this, PlaySMActivity.class));
                //finish();
            }
        });
        level3Button = findViewById(R.id.level3Button);
        level3Button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                //Toast clicked = Toast.makeText(MainActivity.this, "Clicked", Toast.LENGTH_SHORT);
                //clicked.show();
                //mp.start();
                startActivity(new Intent(MainActivity.this, PlayMSActivity.class));
                //finish();
            }
        });
    }
}
