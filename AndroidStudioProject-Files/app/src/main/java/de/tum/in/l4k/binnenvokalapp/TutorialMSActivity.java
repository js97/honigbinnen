package de.tum.in.l4k.binnenvokalapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class TutorialMSActivity extends AppCompatActivity {

    ImageButton done, backhome, word1, word1e, word2, word2e, vocale;
    boolean w1sel = false, w2sel = false;
    Sequence[] sequences;

    static boolean sequences_done = false;
    static Sequence currentSequence;
    MediaPlayer currentPlayer;

    /**
     * Initiiert das Tutorial.
     * Analog zur onCreate-Methode der PlayMS_1Activity-Klasse.
     * @param savedInstanceState Wird an Superklasse weitergegeben.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial_ms);

        View vfs = getWindow().getDecorView();
        vfs.setSystemUiVisibility(View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);


        int colorID = SharedPrefManager.loadColor(this);
        ColorActivity.setBackgroundColor((ConstraintLayout) findViewById(R.id.mst_layout), colorID, this);

        //TODO
        backhome = findViewById(R.id.tuthome);
        backhome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentSequence.player.stop();
                currentSequence.player.release();
                if(currentPlayer != null && currentPlayer.isPlaying()) currentPlayer.stop();
                startActivity(new Intent(TutorialMSActivity.this, PlayMSActivity.class));
                finish();
            }
        });
        done = findViewById(R.id.mstbcheck);
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(sequences_done){
                    if(w1sel && !w2sel) {
                        //Richtige Antwort
                        final ImageView star = findViewById(R.id.tutcorrect);
                        star.setAlpha(1f);
                        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.drillpop);
                        animation.setAnimationListener(new Animation.AnimationListener() {
                            @Override
                            public void onAnimationStart(Animation animation) {
                                if(currentPlayer != null && currentPlayer.isPlaying()) currentPlayer.stop();
                                (currentPlayer=MediaPlayer.create(TutorialMSActivity.this, R.raw.correct_sound)).start();
                            }

                            @Override
                            public void onAnimationEnd(Animation animation) {
                                star.setAlpha(0f);
                                StorageSelectingManager.pass(Level.tutorial, TutorialMSActivity.this);
                                if(currentPlayer != null && currentPlayer.isPlaying()) currentPlayer.stop();
                                currentSequence.player.stop();
                                currentSequence.player.release();
                                startActivity(new Intent(TutorialMSActivity.this, PlayMSActivity.class));
                                finish();
                            }

                            @Override
                            public void onAnimationRepeat(Animation animation) {

                            }
                        });
                        star.startAnimation(animation);
                    } else {
                        //Falsche Antwort
                        final ImageView wrongsign = findViewById(R.id.tutwrong);
                        wrongsign.setAlpha(1f);
                        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.cartoon_popup);
                        animation.setAnimationListener(new Animation.AnimationListener() {
                            @Override
                            public void onAnimationStart(Animation animation) {

                            }

                            @Override
                            public void onAnimationEnd(Animation animation) {
                                wrongsign.setAlpha(0f);
                            }

                            @Override
                            public void onAnimationRepeat(Animation animation) {

                            }
                        });
                        wrongsign.startAnimation(animation);
                        MediaPlayer.create(TutorialMSActivity.this, R.raw.incorrect_sound).start();
                    }
                }
            }
        });
        word1e = findViewById(R.id.mstplay1);
        word2e = findViewById(R.id.mstplay2);
        word1 = findViewById(R.id.mstb1);
        word2 = findViewById(R.id.mstb2);
        word1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(currentPlayer != null && currentPlayer.isPlaying()) currentPlayer.stop();
                (currentPlayer=MediaPlayer.create(TutorialMSActivity.this, R.raw.katze)).start();
            }
        });
        word2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(currentPlayer != null && currentPlayer.isPlaying()) currentPlayer.stop();
                (currentPlayer=MediaPlayer.create(TutorialMSActivity.this, R.raw.pinguin)).start();
            }
        });
        word1e.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                w1sel = !w1sel;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    word1e.setImageDrawable(getDrawable(w1sel ? R.drawable.box_own_on : R.drawable.box_own_off));
                }
            }
        });
        word2e.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                w2sel = !w2sel;
                //int color = Color.argb(w2sel ? 255 : 0, 0, 255, 0);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    word2e.setImageDrawable(getDrawable(w2sel ? R.drawable.box_own_on : R.drawable.box_own_off));
                }
            }
        });
        vocale = findViewById(R.id.mstvocal);
        vocale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(currentPlayer != null && currentPlayer.isPlaying()) currentPlayer.stop();
                (currentPlayer=MediaPlayer.create(TutorialMSActivity.this, R.raw.vocal_a)).start();
            }
        });
        TextView t = findViewById(R.id.msteword1); t.setText(Html.fromHtml("K<font color='#00FF00'>a</font>tze"));
        Sequence.Mode mode = Sequence.Mode.AUTO;
        int resid_anim = R.anim.pulse;
        /* old sequence:
        Sequence sintro = new Sequence(mode).set_resids(R.raw.tut_intro);
        Sequence svocal = new Sequence(sintro, null, mode).set_resids(R.raw.tut_vocal, A(R.id.tutpointvoc), resid_anim);
        Sequence svocalrd = new Sequence(svocal, null, mode).set_resids(R.raw.tut_vocalread, A(R.id.tutpointvocrd), resid_anim);
        Sequence swords = new Sequence(svocalrd, null, mode).set_resids(R.raw.tut_words, A(R.id.tutpointw1, R.id.tutpointw2), resid_anim);
        Sequence swordsrd = new Sequence(swords, null, mode).set_resids(R.raw.tut_wordsread, A(R.id.tutpointw1rd, R.id.tutpointw2rd), resid_anim);
        Sequence swordsselect = new Sequence(swordsrd, null, mode).set_resids(R.raw.tut_wordsselect, A(R.id.tutpointw1click, R.id.tutpointw2click), resid_anim);
        Sequence sconfirm = new Sequence(swordsselect, null, mode).set_resids(R.raw.tut_confirm, A(R.id.tutpointconf), resid_anim);
        Sequence scorrect = new Sequence(sconfirm, null, mode).set_resids(R.raw.tut_correct, A(R.id.tutpointstars), resid_anim);
        Sequence slevel = new Sequence(scorrect, null, mode).set_resids(R.raw.tut_levelsuccess, A(R.id.tutpointstars), resid_anim);
        Sequence back = new Sequence(slevel, null, mode).set_resids(R.raw.tut_back, A(R.id.tutpointhome), resid_anim);*/
        Sequence s1 = new Sequence(mode).set_resids(R.raw.tuttask, A(R.id.tutpointvoc, R.id.tutpointw1, R.id.tutpointw2), resid_anim);
        Sequence sread = new Sequence(s1, null, mode).set_resids(R.raw.tut_v3_s1_vorlesen, A(R.id.tutpointvoc, R.id.tutpointw1, R.id.tutpointw2), resid_anim);
        Sequence s2 = new Sequence(sread, null, mode).set_resids(R.raw.tut_v3_s2_a, A(R.id.tutpointvoc, R.id.tutpointvocrd), resid_anim);
        Sequence s3 = new Sequence(s2, null, mode).set_resids(R.raw.tut_v3_s3_choose, A(R.id.tutpointw1rd, R.id.tutpointw2rd), resid_anim);
        //Sequence s4 = new Sequence(s3, null, mode).set_resids(R.raw.tutmulti, A(R.id.tutpointw1rd, R.id.tutpointw2rd), resid_anim);
        Sequence s5 = new Sequence(s3, null, mode).set_resids(R.raw.tut_v3_s4_confirm, A(R.id.tutpointconf), resid_anim);
        s1.start(this);
    }

    /**
     * Fasst Elemente in einem Array zusammen.
     * @param items Zusammenzufassende Elemente als Array oder als mehrere Parameter.
     * @param <T> Typ des Arrays.
     * @return Zusammengefasstes Array.
     */
    static<T> T[] A(T... items){
        return items;
    }

    /**
     * Klasse fuer eine Sequenz, die eine Audio-Datei wiedergibt, eine Menge an ImageViews
     * mithilfe einer bestimmten Animation animiert und optional eine Nachfolgersequenz automatisch
     * abspielt.
     */
    protected static class Sequence{
        protected Sequence next;
        protected Mode mode = Mode.SINGLE;

        protected int resid_audio;
        protected Integer[] resids_pointers = new Integer[0];
        protected int resid_anim;

        MediaPlayer player;
        /**
         * Modus: AUTO spielt naechste Sequenz automatisch ab,
         * SINGLE fuer einfache Sequenz
         */
        enum Mode {
                SINGLE, AUTO,
        }

        /**
         * Konstruktor einer Sequenz.
         * @param next Naechste Sequenz.
         */
        public Sequence(Sequence next){
            this.next = next;
        }

        /**
         * Konstruktor einer Sequenz.
         * @param previous Vorherige Sequenz.
         * @param next Naechste Sequenz.
         */
        public Sequence(Sequence previous, Sequence next){
            previous.next = this;
            this.next = next;
        }

        /**
         * Standardkonstruktor einer Sequenz.
         */
        public Sequence(){
            this.next = null;
        }
        /**
         * Konstruktor einer Sequenz.
         * @param next Naechste Sequenz.
         * @param mode Modus; Mode.AUTO fuer automatische Wiedergabe der naechsten Sequenz, sonst
         *             Mode.SINGLE bei einfacher Wiedergabe.
         */
        public Sequence(Sequence next, Mode mode){
            this.next = next;
            this.mode = mode;
        }
        /**
         * Konstruktor einer Sequenz.
         * @param mode Modus; Mode.AUTO fuer automatische Wiedergabe der naechsten Sequenz, sonst
         *             Mode.SINGLE bei einfacher Wiedergabe.
         */
        public Sequence(Sequence previous, Sequence next, Mode mode){
            previous.next = this;
            this.next = next;
            this.mode = mode;
        }

        /**
         * Konstruktor einer Sequenz.
         * @param mode Modus; Mode.AUTO fuer automatische Wiedergabe der naechsten Sequenz, sonst
         *             Mode.SINGLE bei einfacher Wiedergabe.
         */
        public Sequence(Mode mode){
            this.next = null;
            this.mode = mode;
        }
        /**
         * Setzt die Audiodatei-Resource-ID in-place.
         * @param resid_audio Audiodatei-Ressourcen-ID.
         * @return Dieses Objekt, um Verkettungen wie seq.set_resids().set_resids() zu erleichtern.
         */
        public Sequence set_resids(int resid_audio){
            this.resid_audio = resid_audio;
            this.resids_pointers = new Integer[0];
            return this;
        }

        /**
         * Setzt die Resource-IDs in-place.
         * @param resid_audio Audiodatei-Ressourcen-ID.
         * @param resids_imageButtonPointers ImageButton-Ressourcen-IDs.
         * @param resid_anim Animations-Ressourcen-ID.
         * @return Dieses Objekt, um Verkettungen wie seq.set_resids().set_resids() zu erleichtern.
         */
        public Sequence set_resids(int resid_audio, Integer[] resids_imageButtonPointers, int resid_anim){
            this.resid_audio = resid_audio;
            this.resids_pointers = resids_imageButtonPointers;
            this.resid_anim = resid_anim;
            return this;
        }

        /**
         * Startet die Sequenz.
         * Die angegebenen ImageViews werden sichtbar gestellt und mit der angegebenen Animation
         * animiert. Die angegebene Audiodatei wird abgespielt. Wenn der automatische Modus aktiviert
         * ist, wird automatisch die naechste Sequenz gestartet, sobald diese Sequenz vorbei ist.
         * @param activity Activity, in der die Sequenz gestartet werden soll. Diese muss auch die
         *                 angegebenen ImageViews in ihrer zugehoerigen ContentView enthalten.
         */
        public void start(final Activity activity){
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    TutorialMSActivity.currentSequence = Sequence.this;
                    final ImageView[] ivs = new ImageView[resids_pointers.length];
                    for (int i = 0; i < resids_pointers.length; i++) {
                        ivs[i] = activity.findViewById(resids_pointers[i]);
                        ivs[i].setVisibility(View.VISIBLE);
                        ivs[i].setAlpha(1f);
                        Animation animation = AnimationUtils.loadAnimation(activity, resid_anim);
                        ivs[i].startAnimation(animation);
                    }
                    MediaPlayer mp = MediaPlayer.create(activity, resid_audio);
                    player = mp;
                    mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mp) {
                            for(ImageView iv : ivs){
                                iv.setVisibility(View.INVISIBLE);
                                iv.setAlpha(0f);
                            }
                            if(Sequence.this.mode == Mode.AUTO){
                                if(Sequence.this.next == null){
                                    //Letzte Sequenz
                                    //activity.startActivity(new Intent(activity, PlayMSActivity.class));
                                    //activity.finish();
                                    sequences_done = true;
                                    //Toast.makeText(activity, "Sequence finished", Toast.LENGTH_LONG);
                                } else {
                                    Sequence.this.next.start(activity);
                                }
                            }
                        }
                    });
                    mp.start();
                }
            });

        }
    }

}
