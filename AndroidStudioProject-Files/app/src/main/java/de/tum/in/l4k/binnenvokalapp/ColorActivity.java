package de.tum.in.l4k.binnenvokalapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

/**
 * Klasse fuer das Farbauswahlmenue.
 */
public class ColorActivity extends AppCompatActivity {

    static final int[] colorresids = new int[]{
            R.id.colordarkest, R.id.colorblack, R.id.colorwhite, R.id.colorbrightest,
            R.id.colordefault, R.id.colorpink, R.id.colormagenta,
            R.id.colorred, R.id.colororange, R.id.coloryellow, R.id.colorgreen,
            R.id.colorcyan, R.id.colorblue, R.id.colorpurple
    };
    static final int[] colorresdrawables = new int[]{
            R.drawable.l4k_background_darkest, R.drawable.l4k_background_black, R.drawable.l4k_background_white, R.drawable.l4k_background_brightest,
            R.drawable.l4k_background, R.drawable.l4k_background_pink, R.drawable.l4k_background_magenta,
            R.drawable.l4k_background_red, R.drawable.l4k_background_orange, R.drawable.l4k_background_yellow, R.drawable.l4k_background_green,
            R.drawable.l4k_background_cyan, R.drawable.l4k_background_blue, R.drawable.l4k_background_purple
    };
    ImageButton[] colorButtons;
    ImageButton home;
    ImageButton help;

    MediaPlayer currentlyPlaying;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_color);

        View vfs = getWindow().getDecorView();
        vfs.setSystemUiVisibility(View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);

        colorButtons = new ImageButton[colorresids.length];
        for(int i = 0; i < colorresids.length; i++){
            colorButtons[i] = findViewById(colorresids[i]);
            final int finalI = i;
            colorButtons[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SharedPrefManager.storeColor(finalI, ColorActivity.this);
                    setBackgroundColor((ConstraintLayout)ColorActivity.this.findViewById(R.id.colorlayout),finalI, ColorActivity.this);
                }
            });
        }

        int colorID = SharedPrefManager.loadColor(this);
        setBackgroundColor((ConstraintLayout)ColorActivity.this.findViewById(R.id.colorlayout),colorID, ColorActivity.this);

        home = findViewById(R.id.activitycolorhome);
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(currentlyPlaying != null && currentlyPlaying.isPlaying()) currentlyPlaying.stop();
                startActivity(new Intent(ColorActivity.this, PlayMSActivity.class));
                finish();
            }
        });
        help = findViewById(R.id.colorhelp);
        help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                (currentlyPlaying = MediaPlayer.create(ColorActivity.this, R.raw.hint_colorselect)).start();
            }
        });
    }

    public static int getDrawableID(int colorID){
        return colorresdrawables[colorID];
    }
    public static void setBackgroundColor(ConstraintLayout activity_layout, int colorID, AppCompatActivity calling){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            activity_layout.setBackground(calling.getResources().getDrawable(getDrawableID(colorID)));
        }
    }
}
