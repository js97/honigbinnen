package de.tum.in.l4k.binnenvokalapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Arrays;
import java.util.Random;

/**
 * Klasse fuer Level 1. Dient auch als Vorlage fuer die anderen Levels.
 */
public class PlayMS_1Activity extends AppCompatActivity {

    public static final int number_of_rounds = 5;
    protected static int number_of_words = 2;

    ImageButton[] opts;
    ImageButton check;
    ImageButton vocal_iv;
    Word[] words;
    ImageButton[] speakers;
    ImageButton speaker_vocal;
    TextView[] tvwords;
    //TODO Vocal instead of Word
    Word current_vocal;
    protected boolean[] guess;
    protected int round;

    //Calendar time = new GregorianCalendar();
    long timestart;
    long timetotal = 0;

    int errors_by_player = 0;

    //PopupWindow correct_answer_popup;
    //ConstraintLayout popup_layout;

    LinearLayout starbar;
    ImageView[] stars;

    ImageView starPopup;

    //ImageView wrong_popup;

    Level currentLevel;
    ConstraintLayout activitylayout;

    ImageButton home;
    ImageButton help;

    MediaPlayer currentlyPlaying;

    /**
     * Initialisiert einen Level.
     * Dabei werden zunaechst findViews() und setLevel() aufgerufen.
     * Ebenfalls die Worter und Vokale werden mithilfe der updateWordsAndVocals()-Methode
     * initialisiert und entsprechende OnClickListener gesetzt. Das gewaehlte Farbschema wird geladen.
     * Die OnClickListener des Bestaetigungs-, des Zurueck- und des Hinweis-ImageButtons werden dann
     * gesetzt, anschliessend die Sterne-Leiste.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View vfs = getWindow().getDecorView();
        vfs.setSystemUiVisibility(View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);


        //LayoutInflater lf = (LayoutInflater)(this.getSystemService(Context.LAYOUT_INFLATER_SERVICE));
        //View v = lf.inflate(R.layout.popup_ms1_correct, null);
        //correct_answer_popup = new PopupWindow(this);
        //correct_answer_popup.setContentView(v);

        opts = new ImageButton[number_of_words];
        speakers = new ImageButton[number_of_words];
        stars = new ImageView[number_of_rounds];
        findViews();
        setLevel();
        check.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                confirm();
            }
        });

        int colorID = SharedPrefManager.loadColor(this);
        ColorActivity.setBackgroundColor(activitylayout,colorID, this);

        updateWordsAndVocals();

        for(int i = 0; i < number_of_words; i++) setOCL(speakers[i], i);
        setSpeakerOCL(vocal_iv, current_vocal);

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(currentlyPlaying != null && currentlyPlaying.isPlaying()) currentlyPlaying.stop();
                startActivity(new Intent(PlayMS_1Activity.this, PlayMSActivity.class));
                finish();
            }
        });
        help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(currentlyPlaying != null && currentlyPlaying.isPlaying()) currentlyPlaying.stop();
                currentlyPlaying = MediaPlayer.create(PlayMS_1Activity.this, R.raw.hint_playing);
                currentlyPlaying.start();
            }
        });

        round = 0;

        for(int i = 0; i < number_of_rounds; i++){
            //LinearLayout.LayoutParams lllp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            ImageView newview = new ImageView(this);//Drawable.createFromPath("drawable/btn_star_holo_light.xml")
            Drawable toDraw = getResources().getDrawable(R.drawable.star_off_own);
            //toDraw = Drawable.createFromPath("drawable/btn_star_holo_light.xml");
            newview.setImageDrawable(toDraw);
            newview.setLayoutParams(new android.view.ViewGroup.LayoutParams(100,100));
            newview.setMaxHeight(60);
            newview.setMaxWidth(60);
            starbar.addView(newview);
            stars[i] = newview;
        }
        //starbar.showContextMenu();
        starPopup.setVisibility(View.INVISIBLE);
        //wrong_popup.setVisibility(View.INVISIBLE);

        if(!SharedPrefManager.passed(currentLevel, this)){
            int resid = R.raw.intro_l1;
            switch(currentLevel){
                case l1:
                    resid = R.raw.intro_l1;
                    break;
                case l2:
                    resid = R.raw.intro_l2;
                    break;
                case l3:
                    resid = R.raw.intro_l3;
                    break;
                case l4:
                    resid = R.raw.intro_l4;
                    break;
                case l5:
                    resid = R.raw.intro_l5;
                    break;
                default:
                    resid = R.raw.intro_l1;
                    break;
            }
            currentlyPlaying = MediaPlayer.create(this, resid);
            currentlyPlaying.start();
        }
    }

    /**
     * Weist den Variablen die Views der ContentView zu.
     * Je nach ContentView sind auch die Resource IDs der Views unterschiedlich. Wenn ein Level eine
     * andere ContentView als die in PlayMS_1Activity verwendet, koennen die Variablen mit den
     * im Bezug auf die geaenderte ContentView korrekten Views belegt werden, indem diese Methode
     * ueberschrieben wird.
     */
    protected void findViews(){
        setContentView(R.layout.activity_play_ms_2w);

        opts[0] = findViewById(R.id.ms1b1);
        opts[1] = findViewById(R.id.ms1b2);

        speakers[0] = findViewById(R.id.ms1play1);
        speakers[1] = findViewById(R.id.ms1play2);

        tvwords = new TextView[]{
                findViewById(R.id.ms1eword1),
                findViewById(R.id.ms1eword2)
        };
        check = findViewById(R.id.ms1bcheck);
        vocal_iv = findViewById(R.id.ms1vocal);
        speaker_vocal = findViewById(R.id.ms1playvocal);
        starPopup = findViewById(R.id.starPopup);
        starbar = (LinearLayout)findViewById(R.id.ms1starbar);
        //wrong_popup = findViewById(R.id.ms1wrong);
        activitylayout = findViewById(R.id.ms1_layout);
        home = findViewById(R.id.ms1home);
        help = findViewById(R.id.ms1help);
    }

    /**
     * Setzt den programmatischen Level dieses Levels.
     * In der meist nicht ueberschriebenen onCreate()-Methode wird setLevel() aufgerufen, da der
     * Level bei den vererbten Klassen unterschiedlich ist. Somit reduziert auch diese Methode
     * Redundanz und kann in jedem anderen Level einfach ueberschrieben werden.
     */
    protected void setLevel(){
        currentLevel = Level.l1;
    }

    /**
     * Fuer das Markieren eines Wortes: Setzt den OnClickListener eines ImageButtons.
     * Bei einem Klick wird das Drawable des ImageButtons auf das entsprechende markiert-
     * bzw. unmarkiert-Template gesetzt werden. Die Markierung wird auch im guess-Array gespeichert.
     * @param ib Markierungs-Button.
     * @param guess_index Index des dem Markierungs-Button zugehoerigen Wortes im guess-Array.
     */
    protected void setOCL(final ImageButton ib, final int guess_index){
        ib.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                guess[guess_index] = !guess[guess_index];
                //int color = Color.argb(guess[guess_index] ? 255 : 0, 0, 255, 0);
                //ib.setBackgroundColor(color);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    ib.setImageDrawable(getDrawable(guess[guess_index] ? R.drawable.box_own_on : R.drawable.box_own_off));
                }
                //dToast.makeText(PlayMS_1Activity.this,Arrays.toString(guess),Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Fuer das Vorlesen eines Wortes: Setzt den OnClickListener eines ImageButtons.
     * Bei einem Klick wird das dem ImageButton zugehoerige Wort vorgelesen werden.
     * @param ib ImageButton, dessen Klick das Wort vorlesen soll.
     * @param word Das Wort, das dabei vorgelesen werden soll.
     */
    protected void setSpeakerOCL(final ImageButton ib, final Word word){
        ib.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if(currentlyPlaying != null && currentlyPlaying.isPlaying()) currentlyPlaying.stop();
                word.play(PlayMS_1Activity.this);
            }
        });
    }

    /**
     * Fuer das Klicken des gruenen Bestaetigungs-Knopfs.
     * Es wird ueberprueft, ob der Nutzer jedes richtige Wort markiert und jedes falsche nicht
     * markiert hat. Wenn nicht korrekt, wird dem Nutzer entsprechendes Feedback gegeben.
     * Wenn korrekt, wird ebenfalls das entsprechende Feedback gegeben und ein Stern vergeben.
     * Der Uebergang in das Hauptmenue wurde hier in die Feedback-Animation showCorrectAimation()
     * ausgelagert, da es einfacher war, so mithilfe deren onAnimationEnd()-Methode erst nach Ende
     * der Animation dies zu erwirken.
     */
    protected void confirm(){
        boolean correct = true;
        for(int i = 0; i < words.length; i++){
            if(!words[i].containsVocal(current_vocal.toChar()) == guess[i]) {
                correct = false;
                break;
            }
        }
        if(correct){
            //StorageSelectingManager.pass(currentLevel, this);
            stars[round].setImageDrawable(getResources().getDrawable(R.drawable.star_on_own));
            showHighlightedWords();
            if(round == 2){
                //Motivation nach drei Runden
                Random random = new Random();
                switch(errors_by_player){
                    case 0:
                        MediaPlayer.create(this, random.nextInt() % 2 == 0 ? R.raw.bestaetigung_sehrstark : R.raw.bestaetigung_sehrstark2).start();
                        break;
                    case 1:
                        MediaPlayer.create(this, random.nextInt() % 2 == 0 ? R.raw.bestaetigung_stark : R.raw.bestaetigung_stark2).start();
                        break;
                    case 2:
                        MediaPlayer.create(this, random.nextInt() % 2 == 0 ? R.raw.bestaetigung_mittel : R.raw.bestaetigung_mittel2).start();
                        break;
                    case 3:
                        MediaPlayer.create(this, random.nextInt() % 2 == 0 ? R.raw.bestaetigung_schwach : R.raw.bestaetigung_schwach2).start();
                        break;
                    default:
                        //Mehr als 3 Fehler
                        MediaPlayer.create(this, R.raw.bestaetigung_sehrschwach).start();
                }
            }
            //showPopup();
            showCorrectAnimation();
            MediaPlayer.create(this, R.raw.correct_sound).start();
        } else {
            MediaPlayer.create(this, R.raw.incorrect_sound).start();
            //showAnimation(R.anim.cartoon_popup, wrong_popup);
            if(errors_by_player % 2 == 0){
                //Jeden zweiten Fehler soll Sprachfeedback erfolgen
                Random random = new Random();
                switch(errors_by_player){
                    case 0:
                        MediaPlayer.create(this, R.raw.falsch_sehrschwach).start();
                        break;
                    case 2:
                        int rand2 = random.nextInt() % 3;
                        MediaPlayer.create(this, rand2 == 0 ? R.raw.falsch_schwach : rand2 == 1 ? R.raw.falsch_schwach2 : R.raw.falsch_schwach3).start();
                        break;
                    case 4:
                        int rand4 = random.nextInt() % 3;
                        MediaPlayer.create(this, rand4 == 0 ? R.raw.falsch_mittel : rand4 == 1 ? R.raw.falsch_mittel2 : R.raw.falsch_mittel3).start();
                        break;
                    case 6:
                        int rand6 = random.nextInt() % 3;
                        MediaPlayer.create(this, rand6 == 0 ? R.raw.falsch_stark : rand6 == 1 ? R.raw.falsch_stark2 : R.raw.falsch_stark3).start();
                        break;
                    default:
                        //Mehr als 7 Fehler
                        MediaPlayer.create(this, R.raw.falsch_sehrstark).start();
                }
            }
            errors_by_player++;
        }
    }
/*
    @Deprecated
    protected void showPopup() {
        correct_answer_popup.showAtLocation(findViewById(R.id.ms1_layout),Gravity.CENTER, 10, 10);
        final boolean won = (number_of_rounds <= round + 1);

        ((ImageButton)correct_answer_popup.getContentView().findViewById(R.id.ms1popupnext)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!won) {
                    round++;
                    updateWordsAndVocals();
                    correct_answer_popup.dismiss();
                } else {
                    StorageManager.LevelProgress.pass(getApplicationContext(), StorageManager.LevelProgress.Level.single1);
                    startActivity(new Intent(PlayMS_1Activity.this, PlayMSActivity.class));
                    PlayMS_1Activity.this.finish();
                }
            }
        });
    }*/

    /**
     * Setzt neue Woerter ein.
     * Dabei werden die Woerter der Reihe nach in die entsprechenden ImageViews opts[] eingefuegt
     * (Drawable und Raw). Die Markierungen werden in dieser Methode ebenfalls zurueckgesetzt.
     * @param newWords Die neuen Woerter.
     */
    protected void updateWords(Word... newWords){
        number_of_words = newWords.length;
        for(int i = 0; i < number_of_words; i++){
            newWords[i].showOnImageView(opts[i]);
            speakers[i].setBackgroundColor(Color.argb(0,0,255,0));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                speakers[i].setImageDrawable(getDrawable(R.drawable.box_own_off));
            }
            setSpeakerOCL(opts[i], newWords[i]);
            tvwords[i].setText(newWords[i].WORD);

            MediaPlayer mp = MediaPlayer.create(this, newWords[i].RESOURCE_ID_AUDIO);
            currentlyPlaying = mp;
            timetotal -= mp.getDuration();
        }
        words = newWords;
        guess = new boolean[words.length];
        Arrays.fill(guess, false);
        //TODO
    }

    /**
     * Aktualisiert den Vokal.
     * Dabei wird Drawable und Raw des Vokal-ImageButtons aktualisiert.
     * @param newVocal Der neue Vokal.
     */
    protected void updateVocal(Word newVocal){
        this.current_vocal = newVocal;
        newVocal.showOnImageView(vocal_iv);
        hideWords();
        setSpeakerOCL(vocal_iv, newVocal);
        setSpeakerOCL(speaker_vocal, newVocal);
    }

    /**
     * Zeigt die schriftliche Form der Woerter an.
     * Dabei wird, falls der aktuelle Vokal enthalten ist, dieser gruen markiert.
     */
    protected void showHighlightedWords(){
        for(int k = 0; k < tvwords.length; k++) {
            String s = tvwords[k].getText().toString();
            String hl = "";//+s.charAt(0);
            for (int i = 1-1; i < s.length()-1+1; i++) {
                char c = s.charAt(i);
                if (c == current_vocal.toChar() || c-'A'+'a' == current_vocal.toChar()) {
                    hl += "<font color='#00FF00'>";
                    hl += c;
                    hl += "</font>";
                } else {
                    hl += c;
                }
            }
            //hl += s.charAt(s.length()-1);
            tvwords[k].setText(Html.fromHtml(hl));
            tvwords[k].setVisibility(View.VISIBLE);
        }
    }

    /**
     * Blendet die schriftliche Form der Woerter wieder aus.
     */
    protected void hideWords(){
        for (int k = 0; k < tvwords.length; k++){
            tvwords[k].setVisibility(View.INVISIBLE);
        }
    }

    /**
     * Zeigt die Animation fuer eine korrekte Antwort und erledigt den Grossteil der folgenden
     * Aktionen.
     * Die Zeit wird aktualisiert.
     * Falls die Runde noch nicht zu Ende ist, werden die Woerter und der Vokal aktualisiert.
     * Falls die Runde zu Ende ist, werden die Achievements aktualisiert, eine Push-Notification
     * ueber neue Achievements gespeichert und zurueck ins Hauptmenue gewechselt.
     */
    protected void showCorrectAnimation(){
        ImageView star = starPopup;
        star.setVisibility(View.VISIBLE);
        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.drillpop);
        star.startAnimation(animation);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                timetotal += System.currentTimeMillis() - timestart;
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                final boolean won = (number_of_rounds <= round + 1);
                if(!won) {
                    round++;
                    updateWordsAndVocals();
                    //correct_answer_popup.dismiss();
                } else {
                    // SUCCESSFUL FINISH OF THE ROUND
                    if(timetotal < 0) timetotal = 1;
                    int prevtime = SharedPrefManager.storeTimeIfImproving(currentLevel, timetotal, PlayMS_1Activity.this);
                    if((timetotal <= 5000 && prevtime > 5000)
                        || (timetotal <= 10000 && prevtime > 10000)
                        || (timetotal <= 15000 && prevtime > 15000)
                        || (timetotal <= 20000 && prevtime > 20000)
                        || (timetotal <= 25000 && prevtime > 25000)
                        || (timetotal <= 30000 && prevtime > 30000)
                        || (timetotal <= 35000 && prevtime > 35000)
                        || (timetotal <= 40000 && prevtime > 40000)
                    ){
                        SharedPrefManager.storeAchievementPushInfo(PlayMS_1Activity.this);
                        //Toast.makeText(PlayMS_1Activity.this, "Time: "+prevtime+" -> "+timetotal, Toast.LENGTH_LONG).show();
                    }
                    if(errors_by_player == 0 && !SharedPrefManager.readPerfection(currentLevel, PlayMS_1Activity.this)) {
                        SharedPrefManager.storePerfection(currentLevel, PlayMS_1Activity.this);
                        SharedPrefManager.storeAchievementPushInfo(PlayMS_1Activity.this);
                        //Toast.makeText(PlayMS_1Activity.this, "Perfect",Toast.LENGTH_LONG).show();
                    }
                    StorageSelectingManager.pass(currentLevel, PlayMS_1Activity.this);
                    //Toast.makeText(PlayMS_1Activity.this, "Length "+timetotal, Toast.LENGTH_LONG).show();
                    if(currentlyPlaying != null && currentlyPlaying.isPlaying()) currentlyPlaying.stop();

                    int newpasses = SharedPrefManager.passes(currentLevel, PlayMS_1Activity.this);
                    if(newpasses == 1 || newpasses == 5 || newpasses == 10 || newpasses == 20){
                        SharedPrefManager.storeAchievementPushInfo(PlayMS_1Activity.this);
                        //Toast.makeText(PlayMS_1Activity.this, "Passes: "+newpasses, Toast.LENGTH_LONG).show();
                    }

                    startActivity(new Intent(PlayMS_1Activity.this, PlayMSActivity.class));
                    PlayMS_1Activity.this.finish();
                }
                starPopup.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    /**
     * Vereinfachende Methode zum Animieren einer ImageView.
     * Erstellt ein Animation-Objekt, macht die ImageView sichtbar und startet die Animation darauf.
     * Nach der Animation wird die ImageView wieder unsichtbar gemacht.
     * @param resid Resource ID der Animation.
     * @param on ImageView, die animiert werden soll.
     */
    protected void showAnimation(int resid, final ImageView on){
        on.setVisibility(View.VISIBLE);
        Animation anim = AnimationUtils.loadAnimation(getApplicationContext(), resid);
        anim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                on.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        on.startAnimation(anim);
    }

    /**
     * Regelt die Aktualisierungen der Woerter und Vokale.
     * Die Methode ist konzipiert, um sehr einfach in anderen Levels Woerter und Vokale zu
     * aktualisieren. updateVocal() und updateWords() muessen meist nicht ueberschrieben werden.
     * Diese Methode wird ebenfalls aus einer meist nicht ueberschriebenen Methode aufgerufen
     * (showCorrectAnimation()). Diese Methode kann einfach ueberschrieben werden und verbessert
     * die Redundanzfreiheit des Codes.
     */
    protected void updateWordsAndVocals(){
        updateVocal(Word.random_vocal());
        updateWords(Word.random_predefined_words_positives_diff(2,1,current_vocal.toChar(),Word.PREDEFINED._EASY_LENGTH()));
        timestart = System.currentTimeMillis();
    }

    protected void playHint_PossiblyNoWordsCorrect(AppCompatActivity calling){
        currentlyPlaying = MediaPlayer.create(calling, R.raw.hint_noanswer);
        currentlyPlaying.start();
    }
    protected void playHint_PossiblyAllWordsCorrect(AppCompatActivity calling){
        currentlyPlaying = MediaPlayer.create(calling, R.raw.hint_multianswer);
        currentlyPlaying.start();
    }
}
