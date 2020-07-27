package de.tum.in.l4k.binnenvokalapp;

import android.content.Context;
import android.media.MediaPlayer;
import android.util.Pair;
import android.widget.ImageView;

import androidx.annotation.IntRange;
import androidx.core.content.res.ResourcesCompat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Random;
import java.util.Set;
import java.util.function.Predicate;

/**
 * Implementiert ein Wort.
 * Zu einem Wort gehoert die Ausschrift, eine Aussprache, ein Bild und eine Menge an Vokalen.
 * Auch Vokale sind hier als Woerter implementiert.
 */
public class Word {
    public final String WORD;
    private final Set<Character> INNER_VOCALS = new HashSet<>();
    public final int RESOURCE_ID_AUDIO, RESOURCE_ID_IMG;

    /**
     * Wort-Konstruktor. Beispiel-Aufruf: Word("Katze", R.raw.katze, R.drawable.katze, 'a', 'e')
     * @param WORD Ausschrift des Wortes. Beispiel: "Katze"
     * @param resource_id_audio Resource-ID der Aussprache des Wortes. Beispiel: R.raw.katze
     * @param resource_id_img Resource-ID des Bildes des Wortes. Beispiel: R.drawable.katze
     * @param inner_vocals Menge an Vokalen im Wort. Flexible Anzahl an Parametern.
     */
    public Word(String WORD, int resource_id_audio, int resource_id_img, char... inner_vocals){
        this.WORD = WORD;
        this.RESOURCE_ID_AUDIO = resource_id_audio;
        this.RESOURCE_ID_IMG = resource_id_img;
        for(char c : inner_vocals){
            this.INNER_VOCALS.add(c);
        }
    }

    /**
     * Erzeugt zufaelligen Vokal.
     * @return Erzeugter Vokal.
     */
    public static Word random_vocal() {
        return VOCAL._ALL[(new Random()).nextInt(5)];
    }

    /**
     * Spielt dieses Wort ab.
     * @param c Context, in dem der MediaPlayer kreiert werden soll (notwendiger Parameter).
     */
    public void play(Context c){
        MediaPlayer.create(c, RESOURCE_ID_AUDIO).start();
    }

    /**
     * Gibt an, ob ein Vokal in diesem Wort enthalten ist.
     * @param c Vokal, dessen Vorkommen ueberprueft werden soll.
     * @return true genau dann, wenn der Vokal im Wort vorkommt.
     */
    public boolean containsVocal(char c){
        return INNER_VOCALS.contains(c);
    }

    /**
     * Gibt alle enthaltenen Vokale (out-of-place) zurueck.
     * @return Enthaltene Vokale.
     */
    public Set<Character> containedCharacters(){
        Set<Character> ccopy = new HashSet<>();
        for(char c : INNER_VOCALS) ccopy.add(c);
        return ccopy;
    }

    /**
     * Setzt das Drawable einer ImageView auf das Drawable dieses Wortes.
     * @param iv ImageView, in der das Bild dieses Wortes angezeigt werden soll.
     */
    public void showOnImageView(ImageView iv){
        iv.setImageDrawable(ResourcesCompat.getDrawable(iv.getResources(),this.RESOURCE_ID_IMG, null));
    }
    /* ImageButton extends ImageView:
    public void showOnImageButton(ImageButton ib){
        ib.setImageDrawable(ResourcesCompat.getDrawable(ib.getResources(),this.RESOURCE_ID_IMG,null));
    }*/
    //TODO only for Vocals!

    /**
     * Konvertiert das Wort in einen Character.
     * Gedacht ist diese Methode fuer Vokale.
     * @return Erster Buchstabe des Wortes.
     */
    protected char toChar(){
        return this.WORD.charAt(0);
    }

    /**
     * Generiert eine zufaellige Menge an Wortern.
     * @param number Anzahl an Woertern, die generiert werden sollen.
     * @return Zufaellig generierte Menge an Woertern.
     */
    public static Word[] random_predefined_words(@IntRange(from = 0) int number){
        if (number > PREDEFINED._ALL.length) number = PREDEFINED._ALL.length;
        Word[] result = new Word[number];
        ArrayList<Integer> randoms = new ArrayList<>();
        Random r = new Random();
        for(int i = 0; i < number; i++){
            int rnd;
            do { rnd = r.nextInt(PREDEFINED._ALL.length); } while (randoms.contains(rnd));
            randoms.add(rnd);
            result[i] = PREDEFINED._ALL[rnd];
        }
        return shuffle(result);
    }

    /**
     * Generiert eine zufaellige Menge an Woertern, die eine bestimmte Anzahl an Woertern enthaelt,
     * die einen bestimmten Vokal enthalten.
     * @param number Anzahl an Woertern, die generiert werden soll.
     * @param positives Anzahl an Woertern, die den angegebenen Vokal enthalten sollen.
     * @param containing Vokal, der in den positiven Woertern enthalten sein soll.
     * @return Generierte Woerter.
     */
    public static Word[] random_predefined_words_positives(@IntRange(from=0) int number, @IntRange(from=0) int positives, char containing){
        return random_predefined_words_positives_diff(number, positives, containing, PREDEFINED._ALL);
    }

    /**
     * Generiert eine zufaellige Menge an Woertern aus einer angegebenen Menge an Woertern.
     * @param number Anzahl an zu generierenden Woertern.
     * @param drawn_from Menge an Woertern, aus der Woerter gezogen werden sollen.
     * @return Generierte Menge an Woertern.
     */
    public static Word[] random_predefined_words_diff(@IntRange(from=0) int number, Word[] drawn_from){
        if (number > drawn_from.length) number = drawn_from.length;
        Word[] result = new Word[number];
        ArrayList<Integer> randoms = new ArrayList<>();
        Random r = new Random();
        for(int i = 0; i < number; i++){
            int rnd;
            do { rnd = r.nextInt(drawn_from.length); } while (randoms.contains(rnd));
            randoms.add(rnd);
            result[i] = drawn_from[rnd];
        }
        return shuffle(result);
    }

    /**
     * Generiert eine zufaellige Menge an Woertern aus einer angegebenen Menge an Woertern,
     * die eine bestimmte Anzahl an Woertern enthaelt, die einen bestimmten Vokal enthalten.
     * @param number Anzahl zu generierender Woerter.
     * @param positives Anzahl der Woerter, die den Vokal enthalten sollen.
     * @param containing Entsprechender Vokal.
     * @param drawn_from Menge an Woertern, aus der Woerter gezogen werden.
     * @return Generierte Menge an Woertern.
     */
    public static Word[] random_predefined_words_positives_diff(@IntRange(from=0) int number, @IntRange(from=0) int positives, char containing, Word[] drawn_from){
        if(drawn_from == null || drawn_from.length < 10){
            drawn_from = PREDEFINED._ALL;
        }

        if(number > drawn_from.length) number = drawn_from.length;
        if(positives > number) positives = number;

        Word[] result = new Word[number];
        ArrayList<Integer> randoms = new ArrayList<>();
        Random r = new Random();

        LinkedList<Word> poswords = new LinkedList<>();
        LinkedList<Word> negwords = new LinkedList<>();
        for(int i = 0; i < drawn_from.length; i++){
            if (drawn_from[i].containsVocal(containing)){
                poswords.add(drawn_from[i]);
            } else {
                negwords.add(drawn_from[i]);
            }
        }
        Word[] posdrawn = random_predefined_words_diff(positives, poswords.toArray(new Word[]{}));
        Word[] negdrawn = random_predefined_words_diff(number - positives, negwords.toArray(new Word[]{}));
        for (int i = 0, j = 0; i + j < number;) {
            result[i + j] = i < posdrawn.length ? posdrawn[i++] : negdrawn[j++];
        }

        /*int CRASH_AVOIDER = 1000;
        for(int p = 0, n = 0, i = 0; p + n < number && CRASH_AVOIDER > 0; CRASH_AVOIDER--){
            int rnd;
            do { rnd = r.nextInt(drawn_from.length); } while (randoms.contains(rnd));
            if(drawn_from[rnd].containsVocal(containing) && p < positives){
                randoms.add(rnd);
                result[i++] = drawn_from[rnd];
                p++;
            } else if (!drawn_from[rnd].containsVocal(containing) && n < number - positives){
                randoms.add(rnd);
                result[i++] = drawn_from[rnd];
                n++;
            }
        }*/
        return shuffle(result);
    }

    /**
     * Permutiert zufaellig ein Array.
     * Diese Implementierung ist out-of-place.
     * @param clazz Klasse der Felder des Arrays.
     * @param out_of_place_shuffle_array Zu permutierendes Array.
     * @param <T> Typ der Felder des Arrays.
     * @return Out-of-place permutiertes Array.
     */
    static<T> T[] shuffle(Class<T> clazz, T[] out_of_place_shuffle_array){
        Random r = new Random();
        Pair<Long, T>[] indexed = new Pair[out_of_place_shuffle_array.length];
        for(int i = 0; i < indexed.length; i++){
            indexed[i] = new Pair<>(r.nextLong(), out_of_place_shuffle_array[i]);
        }
        Arrays.sort(indexed, new Comparator<Pair<Long, T>>() {
            @Override
            public int compare(Pair<Long, T> o1, Pair<Long, T> o2) {
                return o1.first < o2.first ? -1 : o1.first > o2.first ? 1 : 0;
            }
        });
        T[] ret = (T[])new Object[indexed.length];
        for(int i = 0; i < ret.length; i++){
            ret[i] = indexed[i].second;
        }
        return (T[])ret;
    }

    /**
     * Permutiert zufaellig ein Word-Array.
     * Diese Implementierung ist out-of-place.
     * @param out_of_place_shuffle_array Zu permutierendes Word-Array.
     * @return Out-of-place permutiertes Word-Array.
     */
    static Word[] shuffle(Word[] out_of_place_shuffle_array){
        Random r = new Random();
        Pair<Long, Word>[] indexed = new Pair[out_of_place_shuffle_array.length];
        for(int i = 0; i < indexed.length; i++){
            indexed[i] = new Pair<>(r.nextLong(), out_of_place_shuffle_array[i]);
        }
        Arrays.sort(indexed, new Comparator<Pair<Long, Word>>() {
            @Override
            public int compare(Pair<Long, Word> o1, Pair<Long, Word> o2) {
                return o1.first < o2.first ? -1 : o1.first > o2.first ? 1 : 0;
            }
        });
        Word[] ret = new Word[indexed.length];
        for(int i = 0; i < ret.length; i++){
            ret[i] = indexed[i].second;
        }
        return ret;
    }

    /**
     * Vereinigt zwei Wortmengen.
     * Woerter aus der zweiten Menge kommen in der Vereinigung nicht als Duplikate vor.
     * @param w1 Erste Menge.
     * @param w2 Zweite Menge.
     * @return Vereinigung der beiden Mengen.
     */
    static Word[] union(Word[] w1, Word[] w2){
        ArrayList<Word> united = new ArrayList<>();
        for(Word w : w1){
            united.add(w);
        }
        for(Word w : w2){
            if(!united.contains(w)) united.add(w);
        }
        Word[] ws = new Word[united.size()];
        for (int i = 0; i < united.size(); i++) {
            ws[i] = united.get(i);
        }
        return ws;
    }

    /**
     * Vorgefertigte Woerter.
     */
    public static abstract class PREDEFINED {
        //public static final Word AFFE = new Word("Affe", R.raw.affe, R.drawable.affe,'a','e');
        //public static final Word ANGEL = new Word("Angel", R.raw.angel, R.drawable.angel, 'a', 'e');
        //public static final Word APFEL = new Word("Apfel", R.raw.apfel, R.drawable.apfel, 'a', 'e');
        //public static final Word BANANE = new Word("Banane", R.raw.banane, R.drawable.banane, 'a','e');
        //public static final Word BLAUBEEREN = new Word("Blaubeeren", R.raw.blaubeeren, R.drawable.blaubeeren, 'a', 'u', 'e');
        //public static final Word BROMBEEREN = new Word("Brombeeren", R.raw.brombeeren, R.drawable.brombeeren, 'o', 'e');
        //public static final Word ELEFANT = new Word("Elefant", R.raw.elefant, R.drawable.elefant, 'e', 'a');
        //public static final Word ERDBEEREN = new Word("Erdbeeren", R.raw.erdbeeren, R.drawable.erdbeeren, 'e');
        //public static final Word GIRAFFE = new Word("Giraffe", R.raw.giraffe, R.drawable.giraffe, 'i', 'a','e');
        //public static final Word HIMBEEREN = new Word("Himbeeren", R.raw.himbeeren, R.drawable.himbeeren, 'i', 'e');
        //public static final Word JOHANNISBEEREN = new Word("Johannisbeeren", R.raw.johannisbeeren, R.drawable.johannisbeeren, 'o','a', 'i', 'e');
        //public static final Word KAMEL = new Word("Kamel", R.raw.kamel, R.drawable.kamel, 'a', 'e');
        //public static final Word KATZE = new Word("Katze", R.raw.katze, R.drawable.katze, 'a','e');
        //public static final Word KUCHEN = new Word("Kuchen", R.raw.kuchen, R.drawable.kuchen, 'u', 'e');
        //public static final Word LEOPARD = new Word("Leopard", R.raw.leopard, R.drawable.leopard, 'e', 'o', 'a');
        //public static final Word MANDARINE = new Word("Mandarine", R.raw.mandarine, R.drawable.mandarine, 'a', 'i','e');
        //public static final Word MAUS = new Word("Maus", R.raw.maus, R.drawable.maus, 'a', 'u');
        //public static final Word NACHTISCH = new Word("Nachtisch", R.raw.nachtisch, R.drawable.nachtisch, 'a', 'i');
        //public static final Word NASHORN = new Word("Nashorn", R.raw.nashorn, R.drawable.nashorn, 'a', 'o');
        //public static final Word NILPFERD = new Word("Nilpferd", R.raw.nilpferd, R.drawable.nilpferd, 'i', 'e');
        //public static final Word PANDA = new Word("Panda", R.raw.panda, R.drawable.panda, 'a');
        //public static final Word PFAU = new Word("Pfau", R.raw.pfau, R.drawable.pfau, 'a','u');
        //public static final Word PINGUIN = new Word("Pinguin", R.raw.pinguin, R.drawable.pinguin, 'i', 'u');
        //public static final Word STACHELBEEREN = new Word("Stachelbeeren", R.raw.stachelbeeren, R.drawable.stachelbeeren, 'a', 'e');
        //public static final Word TORTE = new Word("Torte", R.raw.torte, R.drawable.torte, 'o','e');
        //public static final Word WALROSS = new Word("Walross", R.raw.walross, R.drawable.walross, 'a', 'o');
        //public static final Word ZEBRA = new Word("Zebra", R.raw.zebra, R.drawable.zebra, 'e','a');

        public static final Word AFFE = new Word("Affe", R.raw.affe, R.drawable.affe, 'a', 'e');
        //public static final Word AUGE = new Word("Auge", R.raw.auge, R.drawable.auge, 'a', 'u', 'e');
        public static final Word BADEANZUG = new Word("Badeanzug", R.raw.badeanzug, R.drawable.badeanzug, 'a', 'e', 'u');
        public static final Word BANANE = new Word("Banane", R.raw.bananen, R.drawable.banane, 'a', 'e');
        public static final Word BASILIKUM = new Word("Basilikum", R.raw.basilikum, R.drawable.basilikum, 'a', 'i', 'u');
        public static final Word BASKETBALL = new Word("Basketball", R.raw.basketball, R.drawable.basketball, 'a', 'e');
        //public static final Word BAUSTELLE = new Word("Baustelle", R.raw.baustelle, R.drawable.baustelle, 'a', 'u', 'e');
        public static final Word BELGIEN = new Word("Belgien", R.raw.belgien, R.drawable.belgien, 'e', 'i');
        public static final Word BETT = new Word("Bett", R.raw.bett, R.drawable.bett, 'e');
        public static final Word BIOTONNE = new Word("Biotonne", R.raw.biotonne, R.drawable.biotonne, 'i', 'o', 'e');
        public static final Word BLATT = new Word("Blatt", R.raw.blatt, R.drawable.blatt, 'a');
        //public static final Word BLAUBEEREN = new Word("Blaubeeren", R.raw.blaubeeren, R.drawable.blaubeeren, 'a', 'u', 'e');
        //public static final Word BLAUWAL = new Word("Blauwal", R.raw.blauwal, R.drawable.blauwal, 'a', 'u');
        public static final Word BLITZ = new Word("Blitz", R.raw.blitz, R.drawable.blitz, 'i');
        public static final Word BLUMENKOHL = new Word("Blumenkohl", R.raw.blumenkohl, R.drawable.blumenkohl, 'u', 'e', 'o');
        public static final Word BLUTTROPFEN = new Word("Bluttropfen", R.raw.bluttropfen, R.drawable.bluttropfen, 'u', 'o', 'e');
        public static final Word BOHNE = new Word("Bohne", R.raw.bohne, R.drawable.bohne, 'o', 'e');
        public static final Word BRILLE = new Word("Brille", R.raw.brille, R.drawable.brille, 'i', 'e');
        public static final Word BROKKOLI = new Word("Brokkoli", R.raw.brokkoli, R.drawable.brokkoli, 'o', 'i');
        public static final Word BROMBEEREN = new Word("Brombeeren", R.raw.brombeeren, R.drawable.brombeeren, 'o', 'e');
        public static final Word BROTKORB = new Word("Brotkorb", R.raw.brotkorb, R.drawable.brotkorb, 'o');
        public static final Word BROT = new Word("Brot", R.raw.brot, R.drawable.brot, 'o');
        public static final Word BRUNNEN = new Word("Brunnen", R.raw.brunnen, R.drawable.brunnen, 'u', 'e');
        public static final Word BUS = new Word("Bus", R.raw.bus, R.drawable.bus, 'u');
        public static final Word DAMPFLOK = new Word("Dampflok", R.raw.dampflok, R.drawable.dampflok, 'a', 'o');
        //public static final Word DART = new Word("Dart", R.raw.darts, R.drawable.dart, 'a');
        //public static final Word DAUMEN = new Word("Daumen", R.raw.daumen, R.drawable.daumen, 'a', 'u', 'e');
        public static final Word DELFIN = new Word("Delfin", R.raw.delfin, R.drawable.delfin, 'e', 'i');
        public static final Word DIAMANT = new Word("Diamant", R.raw.diamant, R.drawable.diamant, 'i', 'a');
        public static final Word DILL = new Word("Dill", R.raw.dill, R.drawable.dill, 'i');
        public static final Word DISKETTE = new Word("Diskette", R.raw.diskette, R.drawable.diskette, 'i', 'e');
        public static final Word DOZENT = new Word("Dozent", R.raw.dozent, R.drawable.dozent, 'o', 'e');
        public static final Word DUDELSACK = new Word("Dudelsack", R.raw.dudelsack, R.drawable.dudelsack, 'u', 'e', 'a');
        public static final Word DUSCHE = new Word("Dusche", R.raw.duschen, R.drawable.dusche, 'u', 'e');
        public static final Word ELEFANT = new Word("Elefant", R.raw.elefant, R.drawable.elefant, 'e', 'a');
        public static final Word ERDBEEREN = new Word("Erdbeeren", R.raw.erdbeeren, R.drawable.erdbeeren, 'e');
        public static final Word FAHRRAD = new Word("Fahrrad", R.raw.fahrrad, R.drawable.fahrrad, 'a');
        public static final Word FARBIG = new Word("Farbig", R.raw.farbig, R.drawable.farbig, 'a', 'i');
        public static final Word FARBPALETTE = new Word("Farbpalette", R.raw.farbpalette, R.drawable.farbpalette, 'a', 'e');
        //public static final Word FAUST = new Word("Faust", R.raw.faust, R.drawable.faust, 'a', 'u');
        public static final Word FERNSEHEN = new Word("Fernsehen", R.raw.fernsehen, R.drawable.fernsehen, 'e');
        public static final Word FILZSTIFT = new Word("Filzstift", R.raw.filzstift, R.drawable.filzstift, 'i');
        public static final Word FISCH = new Word("Fisch", R.raw.fisch, R.drawable.fisch, 'i');
        public static final Word FROSCH = new Word("Frosch", R.raw.frosch, R.drawable.frosch, 'o');
        public static final Word FRUCHTSAFT = new Word("Fruchtsaft", R.raw.fruchtsaft, R.drawable.fruchtsaft, 'u', 'a');
        public static final Word FUCHS = new Word("Fuchs", R.raw.fuchs, R.drawable.fuchs, 'u');
        public static final Word FUSS = new Word("Fuss", R.raw.fuss, R.drawable.fuss, 'u');
        public static final Word FUSSBALL = new Word("Fussball", R.raw.fussball, R.drawable.fussball, 'u', 'a');
        public static final Word GABEL = new Word("Gabel", R.raw.gabel, R.drawable.gabel, 'a', 'e');
        public static final Word GESCHENK = new Word("Geschenk", R.raw.geschenk, R.drawable.geschenk, 'e');
        public static final Word GIRAFFE = new Word("Giraffe", R.raw.giraffe, R.drawable.giraffe, 'i', 'a', 'e');
        public static final Word GITARRE = new Word("Gitarre", R.raw.gitarre, R.drawable.gitarre, 'i', 'a', 'e');
        public static final Word GOLD = new Word("Gold", R.raw.gold, R.drawable.gold, 'o');
        public static final Word GOLFBALL = new Word("Golfball", R.raw.golfball, R.drawable.golfball, 'o', 'a');
        public static final Word GRAS = new Word("Gras", R.raw.gras, R.drawable.gras, 'a');
        public static final Word GROSSBRITANNIEN = new Word("Grossbritannien", R.raw.grossbritannien, R.drawable.grossbritannien, 'o', 'i', 'a', 'e');
        public static final Word GURKE = new Word("Gurke", R.raw.gurke, R.drawable.gurke, 'u', 'e');
        public static final Word HAND = new Word("Hand", R.raw.hand, R.drawable.hand, 'a');
        public static final Word HANDBALL = new Word("Handball", R.raw.handball, R.drawable.handball, 'a');
        public static final Word HANDSCHUHE = new Word("Handschuhe", R.raw.handschuhe, R.drawable.handschuhe, 'a', 'u', 'e');
        //public static final Word HAUS = new Word("Haus", R.raw.haus, R.drawable.haus, 'a', 'u');
        //public static final Word HAUSAUFGABEN = new Word("Hausaufgaben", R.raw.hausaufgaben, R.drawable.hausaufgaben, 'a', 'u', 'e');
        public static final Word HEFT = new Word("Heft", R.raw.heft, R.drawable.heft, 'e');
        public static final Word HEMD = new Word("Hemd", R.raw.hemd, R.drawable.hemd, 'e');
        public static final Word HERZ = new Word("Herz", R.raw.herz, R.drawable.herz, 'e');
        public static final Word HEXE = new Word("Hexe", R.raw.hexe, R.drawable.hexe, 'e');
        public static final Word HIMBEEREN = new Word("Himbeeren", R.raw.himbeeren, R.drawable.himbeeren, 'i', 'e');
        public static final Word HOCHSTUHL = new Word("Hochstuhl", R.raw.hochstuhl, R.drawable.hochstuhl, 'o', 'u');
        public static final Word HOLZBANK = new Word("Holzbank", R.raw.holzbank, R.drawable.holzbank, 'o', 'a');
        public static final Word HOLZSTIFT = new Word("Holzstift", R.raw.holzstift, R.drawable.holzstift, 'o', 'i');
        public static final Word HONIG = new Word("Honig", R.raw.honig, R.drawable.honig, 'o', 'i');
        public static final Word HUND = new Word("Hund", R.raw.hund, R.drawable.hund, 'u');
        public static final Word JOHANNISBEEREN = new Word("Johannisbeeren", R.raw.johannisbeeren, R.drawable.johannisbeeren, 'o', 'a', 'i', 'e');
        public static final Word KAKTUS = new Word("Kaktus", R.raw.kaktus, R.drawable.kaktus, 'a', 'u');
        public static final Word KALMAR = new Word("Kalmar", R.raw.kalmar, R.drawable.kalmar, 'a');
        public static final Word KAMEL = new Word("Kamel", R.raw.kamel, R.drawable.kamel, 'a', 'e');
        public static final Word KANINCHEN = new Word("Kaninchen", R.raw.kaninchen, R.drawable.kaninchen, 'a', 'i', 'e');
        public static final Word KAROTTE = new Word("Karotte", R.raw.karotte, R.drawable.karotte, 'a', 'o', 'e');
        public static final Word KARTOFFEL = new Word("Kartoffel", R.raw.kartoffel, R.drawable.kartoffel, 'a', 'o', 'e');
        public static final Word KARTON = new Word("Karton", R.raw.karton, R.drawable.karton, 'a', 'o');
        public static final Word KASACHSTAN = new Word("Kasachstan", R.raw.kasachstan, R.drawable.kasachstan, 'a');
        public static final Word KATZE = new Word("Katze", R.raw.katze, R.drawable.katze, 'a', 'e');
        //public static final Word KATZE2 = new Word("Katze2", R.raw.katze2, R.drawable.katze2, 'a', 'e');
        public static final Word KEGEL = new Word("Kegel", R.raw.kegel, R.drawable.kegel, 'e');
        public static final Word KIND = new Word("Kind", R.raw.kind, R.drawable.kind, 'i');
        public static final Word KIRSCHEN = new Word("Kirschen", R.raw.kirschen, R.drawable.kirschen, 'i', 'e');
        public static final Word KLAPPSTUHL = new Word("Klappstuhl", R.raw.klappstuhl, R.drawable.klappstuhl, 'a', 'u');
        public static final Word KLEBEBAND = new Word("Klebeband", R.raw.klebeband, R.drawable.klebeband, 'e', 'a');
        public static final Word KLEBESTIFT = new Word("Klebestift", R.raw.klebestift, R.drawable.klebestift, 'e', 'i');
        //public static final Word KLEBSTOFF = new Word("Klebstoff", R.raw.klebstoff, R.drawable.klebstoff, 'e', 'o');
        public static final Word KLINGELTON = new Word("Klingelton", R.raw.klingelton, R.drawable.klingelton, 'i', 'e', 'o');
        //public static final Word KNOBLAUCH = new Word("Knoblauch", R.raw.knoblauch, R.drawable.knoblauch, 'o', 'a', 'u');
        public static final Word KNOPF = new Word("Knopf", R.raw.knopf, R.drawable.knopf, 'o');
        public static final Word KOBRA = new Word("Kobra", R.raw.kobra, R.drawable.kobra, 'o', 'a');
        public static final Word KOCHTOPF = new Word("Kochtopf", R.raw.kochtopf, R.drawable.kochtopf, 'o');
        public static final Word KONDITOR = new Word("Konditor", R.raw.konditor, R.drawable.konditor, 'o', 'i');
        public static final Word KOPF = new Word("Kopf", R.raw.kopf, R.drawable.kopf, 'o');
        public static final Word KRISTALL = new Word("Kristall", R.raw.kristall, R.drawable.kristall, 'i', 'a');
        public static final Word KROKODIL = new Word("Krokodil", R.raw.krokodil, R.drawable.krokodil, 'o', 'i');
        public static final Word KUCHEN = new Word("Kuchen", R.raw.kuchen, R.drawable.kuchen, 'u', 'e');
        public static final Word KUGEL = new Word("Kugel", R.raw.kugel, R.drawable.kugel, 'u', 'e');
        public static final Word LASTKRAFTWAGEN = new Word("Lastkraftwagen", R.raw.lastkraftwagen, R.drawable.lastkraftwagen, 'a', 'e');
        public static final Word LATERNE = new Word("Laterne", R.raw.laterne, R.drawable.laterne, 'a', 'e');
        //public static final Word LAUB = new Word("Laub", R.raw.laub, R.drawable.laub, 'a', 'u');
        //public static final Word LAUCH = new Word("Lauch", R.raw.lauch, R.drawable.lauch, 'a', 'u');
        //public static final Word LAUFBAND = new Word("Laufband", R.raw.laufband, R.drawable.laufband, 'a', 'u');
        public static final Word LEGUAN = new Word("Leguan", R.raw.leguan, R.drawable.leguan, 'e', 'u', 'a');
        public static final Word LEOPARD = new Word("Leopard", R.raw.leopard, R.drawable.leopard, 'e', 'o', 'a');
        public static final Word LID = new Word("Lid", R.raw.lid, R.drawable.lid, 'i');
        public static final Word LIMETTEN = new Word("Limetten", R.raw.limetten, R.drawable.limetten, 'i', 'e');
        public static final Word LINEAL = new Word("Lineal", R.raw.lineal, R.drawable.lineal, 'i', 'e', 'a');
        public static final Word LIPPE = new Word("Lippe", R.raw.lippe, R.drawable.lippe, 'i', 'e');
        public static final Word LIPPENSTIFT = new Word("Lippenstift", R.raw.lippenstift, R.drawable.lippenstift, 'i', 'e');
        public static final Word LUFTMATRATZE = new Word("Luftmatratze", R.raw.luftmatratze, R.drawable.luftmatratze, 'u', 'a', 'e');
        public static final Word MADAGASKAR = new Word("Madagaskar", R.raw.madagaskar, R.drawable.madagaskar, 'a');
        //public static final Word MAIS = new Word("Mais", R.raw.mais, R.drawable.mais, 'a', 'i');
        //public static final Word MAISKOLBEN = new Word("Maiskolben", R.raw.maiskolben, R.drawable.maiskolben, 'a', 'i', 'o', 'e');
        public static final Word MANDARINE = new Word("Mandarine", R.raw.mandarine, R.drawable.mandarine, 'a', 'i', 'e');
        public static final Word MANTEL = new Word("Mantel", R.raw.mantel, R.drawable.mantel, 'a', 'e');
        //public static final Word MAUS = new Word("Maus", R.raw.maus, R.drawable.maus, 'a', 'u');
        public static final Word MEHL = new Word("Mehl", R.raw.mehl, R.drawable.mehl, 'e');
        public static final Word MINZE = new Word("Minze", R.raw.minze, R.drawable.minze, 'i', 'e');
        public static final Word MOBILTELEFON = new Word("Mobiltelefon", R.raw.mobiltelefon, R.drawable.mobiltelefon, 'o', 'i', 'e');
        public static final Word MOND = new Word("Mond", R.raw.mond, R.drawable.mond, 'o');
        public static final Word MOTORBOOT = new Word("Motorboot", R.raw.motorboot, R.drawable.motorboot, 'o');
        public static final Word MOTORRAD = new Word("Motorrad", R.raw.motorrad, R.drawable.motorrad, 'o', 'a');
        public static final Word MUND = new Word("Mund", R.raw.mund, R.drawable.mund, 'u');
        public static final Word MUSIK = new Word("Musik", R.raw.musik, R.drawable.musik, 'u', 'i');
        public static final Word NACHRICHT = new Word("Nachricht", R.raw.nachricht, R.drawable.nachricht, 'a', 'i');
        public static final Word NACHTISCH = new Word("Nachtisch", R.raw.nachtisch, R.drawable.nachtisch, 'a', 'i');
        public static final Word NAGEL = new Word("Nagel", R.raw.nagel, R.drawable.nagel, 'a', 'e');
        public static final Word NARWAL = new Word("Narwal", R.raw.narwal, R.drawable.narwal, 'a');
        public static final Word NASE = new Word("Nase", R.raw.nase, R.drawable.nase, 'a', 'e');
        public static final Word NASHORN = new Word("Nashorn", R.raw.nashorn, R.drawable.nashorn, 'a', 'o');
        public static final Word NILPFERD = new Word("Nilpferd", R.raw.nilpferd, R.drawable.nilpferd, 'i', 'e');
        public static final Word NOTIZBUCH = new Word("Notizbuch", R.raw.notizbuch, R.drawable.notizbuch, 'o', 'i', 'u');
        //public static final Word ORANGEN = new Word("Orangen", R.raw.orangen, R.drawable.orangen, 'o', 'a', 'e');
        public static final Word PANDA = new Word("Panda", R.raw.panda, R.drawable.panda, 'a');
        public static final Word PAPRIKA = new Word("Paprika", R.raw.paprika, R.drawable.paprika, 'a', 'i');
        public static final Word PARKBANK = new Word("Parkbank", R.raw.parkbank, R.drawable.parkbank, 'a');
        public static final Word PELIKAN = new Word("Pelikan", R.raw.pelikan, R.drawable.pelikan, 'e', 'i', 'a');
        public static final Word PETERSILIE = new Word("Petersilie", R.raw.petersilie, R.drawable.petersilie, 'e', 'i');
        //public static final Word PFAU = new Word("Pfau", R.raw.pfau, R.drawable.pfau, 'a', 'u');
        public static final Word PFERD = new Word("Pferd", R.raw.pferd, R.drawable.pferd, 'e');
        public static final Word PFLANZE = new Word("Pflanze", R.raw.pflanze, R.drawable.pflanze, 'a', 'e');
        public static final Word PFOTE = new Word("Pfote", R.raw.pfote, R.drawable.pfote, 'o', 'e');
        public static final Word PIANO = new Word("Piano", R.raw.piano, R.drawable.piano, 'i', 'a', 'o');
        public static final Word PILZ = new Word("Pilz", R.raw.pilz, R.drawable.pilz, 'i');
        public static final Word PINGUIN = new Word("Pinguin", R.raw.pinguin, R.drawable.pinguin, 'i', 'u');
        public static final Word PINSEL = new Word("Pinsel", R.raw.pinsel, R.drawable.pinsel, 'i', 'e');
        public static final Word PLASTIKTONNE = new Word("Plastiktonne", R.raw.plastiktonne, R.drawable.plastiktonne, 'a', 'i', 'o', 'e');
        public static final Word PLATIN = new Word("Platin", R.raw.platin, R.drawable.platin, 'a', 'i');
        public static final Word POKAL = new Word("Pokal", R.raw.pokal, R.drawable.pokal, 'o', 'a');
        public static final Word PORTAL = new Word("Portal", R.raw.portal, R.drawable.portal, 'o', 'a');
        public static final Word POST = new Word("Post", R.raw.post, R.drawable.post, 'o');
        public static final Word POTTWAL = new Word("Pottwal", R.raw.pottwal, R.drawable.pottwal, 'o', 'a');
        public static final Word PRISMA = new Word("Prisma", R.raw.prisma, R.drawable.prisma, 'i', 'a');
        public static final Word PUDEL = new Word("Pudel", R.raw.pudel, R.drawable.pudel, 'u', 'e');
        public static final Word PUSTEBLUMEN = new Word("Pusteblumen", R.raw.pusteblumen, R.drawable.pusteblumen, 'u', 'e');
        public static final Word PYRAMIDE = new Word("Pyramide", R.raw.pyramide, R.drawable.pyramide, 'a', 'i', 'e');
        public static final Word RAKETE = new Word("Rakete", R.raw.rakete, R.drawable.rakete, 'a', 'e');
        public static final Word RASSEL = new Word("Rassel", R.raw.rassel, R.drawable.rassel, 'a', 'e');
        public static final Word REGAL = new Word("Regal", R.raw.regal, R.drawable.regal, 'e', 'a');
        public static final Word RETTUNGSRING = new Word("Rettungsring", R.raw.rettungsring, R.drawable.rettungsring, 'e', 'u', 'i');
        public static final Word RING = new Word("Ring", R.raw.ring, R.drawable.ring, 'i');
        public static final Word ROSMARIN = new Word("Rosmarin", R.raw.rosmarin, R.drawable.rosmarin, 'o', 'a', 'i');
        public static final Word RUCOLA = new Word("Rucola", R.raw.rucola, R.drawable.rucola, 'u', 'o', 'a');
        public static final Word RUNDFUNK = new Word("Rundfunk", R.raw.rundfunk, R.drawable.rundfunk, 'u');
        public static final Word RUSSLAND = new Word("Russland", R.raw.russland, R.drawable.russland, 'u', 'a');
        public static final Word SANDUHR = new Word("Sanduhr", R.raw.sanduhr, R.drawable.sanduhr, 'a', 'u');
        public static final Word SCHACH = new Word("Schach", R.raw.schach, R.drawable.schach, 'a');
        public static final Word SCHAF = new Word("Schaf", R.raw.schaf, R.drawable.schaf, 'a');
        public static final Word SCHAL = new Word("Schal", R.raw.schal, R.drawable.schal, 'a');
        public static final Word SCHERE = new Word("Schere", R.raw.schere, R.drawable.schere, 'e');
        public static final Word SCHIFFSRAD = new Word("Schiffsrad", R.raw.schiffsrad, R.drawable.schiffsrad, 'i', 'a');
        public static final Word SCHILD = new Word("Schild", R.raw.schild, R.drawable.schild, 'i');
        public static final Word SCHLANGE = new Word("Schlange", R.raw.schlange, R.drawable.schlange, 'a', 'e');
        public static final Word SCHNEEMANN = new Word("Schneemann", R.raw.schneemann, R.drawable.schneemann, 'e', 'a');
        public static final Word SCHNELLZUG = new Word("Schnellzug", R.raw.schnellzug, R.drawable.schnellzug, 'e', 'u');
        public static final Word SCHRANK = new Word("Schrank", R.raw.schrank, R.drawable.schrank, 'a');
        //public static final Word SCHRAUBE = new Word("Schraube", R.raw.schraube, R.drawable.schraube, 'a', 'u', 'e');
        public static final Word SCHUHE = new Word("Schuhe", R.raw.schuhe, R.drawable.schuhe, 'u', 'e');
        public static final Word SCHWAN = new Word("Schwan", R.raw.schwan, R.drawable.schwan, 'a');
        public static final Word SCHWIMMBAD = new Word("Schwimmbad", R.raw.schwimmbad, R.drawable.schwimmbad, 'i', 'a');
        public static final Word SCHWIMMBECKEN = new Word("Schwimmbecken", R.raw.schwimmbecken, R.drawable.schwimmbecken, 'i', 'e');
        public static final Word SCHWIMMWESTE = new Word("Schwimmweste", R.raw.schwimmweste, R.drawable.schwimmweste, 'i', 'e');
        public static final Word SEGELBOOT = new Word("Segelboot", R.raw.segelboot, R.drawable.segelboot, 'e', 'o');
        public static final Word SERBIEN = new Word("Serbien", R.raw.serbien, R.drawable.serbien, 'e', 'i');
        //public static final Word SICHEL = new Word("Sichel", R.raw.sichel, R.drawable.sichel, 'i', 'e');
        public static final Word SKORPION = new Word("Skorpion", R.raw.skorpion, R.drawable.skorpion, 'o', 'i');
        public static final Word SOCKEN = new Word("Socken", R.raw.socken, R.drawable.socken, 'o', 'e');
        public static final Word SONNENSCHIRM = new Word("Sonnenschirm", R.raw.sonnenschirm, R.drawable.sonnenschirm, 'o', 'e', 'i');
        public static final Word SPANIEN = new Word("Spanien", R.raw.spanien, R.drawable.spanien, 'a', 'i', 'e');
        public static final Word SPHALERIT = new Word("Sphalerit", R.raw.sphalerit, R.drawable.sphalerit, 'a', 'e', 'i');
        public static final Word SPINAT = new Word("Spinat", R.raw.spinat, R.drawable.spinat, 'i', 'a');
        public static final Word SPINNENNETZ = new Word("Spinnennetz", R.raw.spinnennetz, R.drawable.spinnennetz, 'i', 'e');
        public static final Word SPRINGBRUNNEN = new Word("Springbrunnen", R.raw.springbrunnen, R.drawable.springbrunnen, 'i', 'u', 'e');
        public static final Word SPRUNGBRETT = new Word("Sprungbrett", R.raw.sprungbrett, R.drawable.sprungbrett, 'u', 'e');
        public static final Word STACHELBEEREN = new Word("Stachelbeeren", R.raw.stachelbeeren, R.drawable.stachelbeeren, 'a', 'e');
        public static final Word STECKNADEL = new Word("Stecknadel", R.raw.stecknadel, R.drawable.stecknadel, 'e', 'a');
        public static final Word STEMPEL = new Word("Stempel", R.raw.stempel, R.drawable.stempel, 'e');
        public static final Word STORCH = new Word("Storch", R.raw.storch, R.drawable.storch, 'o');
        public static final Word STRASSENLATERNE = new Word("Strassenlaterne", R.raw.strassenlaterne, R.drawable.strassenlaterne, 'a', 'e');
        public static final Word STUHL = new Word("Stuhl", R.raw.stuhl, R.drawable.stuhl, 'u');
        //public static final Word TAUBE = new Word("Taube", R.raw.taube, R.drawable.taube, 'a', 'u', 'e');
        //public static final Word TAUCHGANG = new Word("Tauchgang", R.raw.tauchgang, R.drawable.tauchgang, 'a', 'u');
        public static final Word TELEFAX = new Word("Telefax", R.raw.telefax, R.drawable.telefax, 'e', 'a');
        public static final Word TELEFON = new Word("Telefon", R.raw.telefon, R.drawable.telefon, 'e', 'o');
        public static final Word TELEFONBUCH = new Word("Telefonbuch", R.raw.telefonbuch, R.drawable.telefonbuch, 'e', 'o', 'u');
        public static final Word TENNIS = new Word("Tennis", R.raw.tennis, R.drawable.tennis, 'e', 'i');
        public static final Word THYMIAN = new Word("Thymian", R.raw.thymian, R.drawable.thymian, 'i', 'a');
        public static final Word TIGER = new Word("Tiger", R.raw.tiger, R.drawable.tiger, 'i', 'e');
        public static final Word TISCH = new Word("Tisch", R.raw.tisch, R.drawable.tisch, 'i');
        public static final Word TISCHLAMPE = new Word("Tischlampe", R.raw.tischlampe, R.drawable.tischlampe, 'i', 'a', 'e');
        public static final Word TOMATE = new Word("Tomate", R.raw.tomate, R.drawable.tomate, 'o', 'a', 'e');
        public static final Word TORNADO = new Word("Tornado", R.raw.tornado, R.drawable.tornado, 'o', 'a');
        public static final Word TORTE = new Word("Torte", R.raw.torte, R.drawable.torte, 'o', 'e');
        public static final Word TORUS = new Word("Torus", R.raw.torus, R.drawable.torus, 'o', 'u');
        public static final Word TRAKTOR = new Word("Traktor", R.raw.traktor, R.drawable.traktor, 'a', 'o');
        public static final Word TRIANGEL = new Word("Triangel", R.raw.triangel, R.drawable.triangel, 'i', 'a', 'e');
        public static final Word TROMMEL = new Word("Trommel", R.raw.trommel, R.drawable.trommel, 'o', 'e');
        public static final Word TROMPETE = new Word("Trompete", R.raw.trompete, R.drawable.trompete, 'o', 'e');
        public static final Word TULPE = new Word("Tulpe", R.raw.tulpe, R.drawable.tulpe, 'u', 'e');
        public static final Word VIOLINE = new Word("Violine", R.raw.violine, R.drawable.violine, 'i', 'o', 'e');
        //public static final Word A = new Word("A", R.raw.a, R.drawable.a, 'a');
        //public static final Word E = new Word("E", R.raw.e, R.drawable.e, 'e');
        //public static final Word I = new Word("I", R.raw.i, R.drawable.i, 'i');
        //public static final Word O = new Word("O", R.raw.o, R.drawable.o, 'o');
        //public static final Word U = new Word("U", R.raw.u, R.drawable.u, 'u');
        public static final Word WACHSMALSTIFT = new Word("Wachsmalstift", R.raw.wachsmalstift, R.drawable.wachsmalstift, 'a', 'i');
        //public static final Word WAGGON = new Word("Waggon", R.raw.waggon, R.drawable.waggon, 'a', 'o');
        public static final Word WALROSS = new Word("Walross", R.raw.walross, R.drawable.walross, 'a', 'o');
        public static final Word WIND = new Word("Wind", R.raw.wind, R.drawable.wind, 'i');
        public static final Word WOLF = new Word("Wolf", R.raw.wolf, R.drawable.wolf, 'o');
        public static final Word WURM = new Word("Wurm", R.raw.wurm, R.drawable.wurm, 'u');
        public static final Word XYLOPHON = new Word("Xylophon", R.raw.xylophon, R.drawable.xylophon, 'o');
        public static final Word ZEBRA = new Word("Zebra", R.raw.zebra, R.drawable.zebra, 'e', 'a');
        public static final Word ZIMT = new Word("Zimt", R.raw.zimt, R.drawable.zimt, 'i');
        public static final Word ZIMTSTANGE = new Word("Zimtstange", R.raw.zimtstange, R.drawable.zimtstange, 'i', 'a', 'e');
        public static final Word ZITRONE = new Word("Zitrone", R.raw.zitrone, R.drawable.zitrone, 'i', 'o', 'e');


        /**
         * Menge aller Woerter.
         */
        public static final Word[] _ALL = new Word[]{
                AFFE,
                //ANGEL, APFEL,
                //AUGE,
                BADEANZUG, BANANE, BASILIKUM, BASKETBALL, /*BAUSTELLE*/ BELGIEN, BETT, BIOTONNE, BLATT, /*BLAUBEEREN*/ /*BLAUWAL*/ BLITZ, BLUMENKOHL, BLUTTROPFEN, BOHNE, BRILLE, BROKKOLI, BROMBEEREN, BROTKORB,
                BROT,
                BRUNNEN, BUS,
                DAMPFLOK,
                //DART,
                /*DAUMEN*/ DELFIN, DIAMANT, DILL, DISKETTE, DOZENT, DUDELSACK,
                DUSCHE,
                ELEFANT,
                ERDBEEREN,
                FAHRRAD, FARBIG, FARBPALETTE, /*FAUST*/ FERNSEHEN, FILZSTIFT, FISCH, FROSCH, FRUCHTSAFT, FUCHS, FUSS, FUSSBALL,
                GABEL, GESCHENK, GIRAFFE, GITARRE, GOLD, GOLFBALL, GRAS, GROSSBRITANNIEN, GURKE,
                HAND, HANDBALL, HANDSCHUHE, /*HAUS*/ /*HAUSAUFGABEN,*/ HEFT, HEMD, /*HERZ,*/ HEXE, HIMBEEREN, HOCHSTUHL, HOLZBANK, HOLZSTIFT, HONIG, HUND,
                JOHANNISBEEREN,
                KAKTUS, KALMAR, KAMEL, KANINCHEN, KAROTTE, KARTOFFEL, KARTON, KASACHSTAN, KATZE, /*KATZE2,*/ KEGEL, KIND, KIRSCHEN, KLAPPSTUHL, KLEBEBAND, KLEBESTIFT, /*KLEBSTOFF,*/ KLINGELTON /*KNOBLAUCH*/, KNOPF, KOBRA, KOCHTOPF, KONDITOR, KOPF, KRISTALL, KROKODIL, KUCHEN, KUGEL,
                LASTKRAFTWAGEN, LATERNE, /*LAUB, LAUCH, LAUFBAND,*/ LEGUAN, LEOPARD, LID, LIMETTEN, LINEAL, LIPPE, LIPPENSTIFT, LUFTMATRATZE,
                MADAGASKAR, /*MAIS, MAISKOLBEN,*/ MANDARINE, MANTEL, /*MAUS*/ MEHL, MINZE, MOBILTELEFON, MOND, MOTORBOOT, MOTORRAD, MUND, MUSIK,
                NACHRICHT, NACHTISCH, NAGEL, NARWAL, NASE, NASHORN, NILPFERD, NOTIZBUCH,
                //ORANGEN,
                PANDA, PAPRIKA, PARKBANK, PELIKAN, PETERSILIE, /*PFAU,*/ PFERD, PFLANZE, PFOTE, PIANO, PILZ, PINGUIN, PINSEL, PLASTIKTONNE, PLATIN, POKAL, PORTAL, POST, POTTWAL, PRISMA, PUDEL, PUSTEBLUMEN, PYRAMIDE,
                RAKETE, RASSEL, REGAL, RETTUNGSRING, RING, ROSMARIN, RUCOLA, RUNDFUNK, RUSSLAND,
                SANDUHR, SCHACH, SCHAF, SCHAL, SCHERE, SCHIFFSRAD, SCHILD, SCHLANGE, SCHNEEMANN, SCHNELLZUG, SCHRANK, /*SCHRAUBE, */SCHUHE, SCHWAN, SCHWIMMBAD, SCHWIMMBECKEN, SCHWIMMWESTE, SEGELBOOT, SERBIEN,
                //SICHEL,
                SKORPION, SOCKEN, SONNENSCHIRM, SPANIEN, SPHALERIT, SPINAT, SPINNENNETZ, SPRINGBRUNNEN, SPRUNGBRETT, STACHELBEEREN, STECKNADEL, STEMPEL, STORCH, STRASSENLATERNE, STUHL,
                /*TAUBE, TAUCHGANG,*/ TELEFAX, TELEFON, TELEFONBUCH, TENNIS, THYMIAN, TIGER, TISCH, TISCHLAMPE, TOMATE, TORNADO, TORTE, TORUS, TRAKTOR, TRIANGEL, TROMMEL, TROMPETE, TULPE,
                VIOLINE,
                WACHSMALSTIFT, WURM,
                //WAGGON,
                WALROSS, WIND, WOLF,
                XYLOPHON,
                ZEBRA, ZIMT, ZIMTSTANGE, ZITRONE,
        };
        /**
         * @Deprecated Not enough words.
         */
        @Deprecated
        public static final Word[] _EASY = new Word[]{
                WIND,
        };
        /**
         * @Deprecated Not enough words.
         */
        @Deprecated
        public static final Word[] _MEDIUM = new Word[]{
                ELEFANT,
        };
        /**
         * @Deprecated Not enough words.
         */
        @Deprecated
        public static final Word[] _HARD = new Word[]{
                MANDARINE,
        };

        /**
         * Bildet ein Wort auf eine Schwierigkeit ab.
         * @param w Wort, dessen Schwierigkeit berechnet werden soll.
         * @return Berechnete Schwierigkeit des Worts.
         */
        static final double difficulty(Word w){
            ArrayList<Character> contained_chars = new ArrayList<>();
            int num_vocals = 0;
            for(char c : w.WORD.toCharArray()){
                if(!contained_chars.contains(c)) contained_chars.add(c);
                if(isVocal(c)) num_vocals++;
            }
            return contained_chars.size()-num_vocals;
        }

        /**
         * Gibt an, ob ein Character ein Vokal ist.
         * @param c Vokal, der ueberprueft werden soll.
         * @return true genau dann, wenn der angegebene Character ein Vokal ist.
         */
        private static boolean isVocal(char c){
            switch(c){
                case 'a':
                case 'A':
                case 'e':
                case 'E':
                case 'i':
                case 'I':
                case 'o':
                case 'O':
                case 'u':
                case 'U':
                    return true;
                default:
                    return false;
            }
        }

        /**
         * Generiert automatisch eine Menge an einfachen Woertern.
         * Definiert sind einfache Woerter derzeit als solche, die eine Schwierigkeit unter 5 (exkl.)
         * nach der difficulty(Word)-Methode haben.
         * @return Automatisch generierte Menge an einfachen Woertern.
         */
        public static final Word[] _EASY_AUTO(){
            ArrayList<Word> words = new ArrayList<>();
            for(Word w : _ALL){
                if(difficulty(w) < 5)
                    words.add(w);
            }
            return words.toArray(new Word[_ALL.length/3]);
        }
        /**
         * Generiert automatisch eine Menge an mittelschweren Woertern.
         * Definiert sind mittelschwere Woerter derzeit als solche, die eine Schwierigkeit zwischen
         * 3 (exkl.) und 7 (exkl.) nach der difficulty(Word)-Methode haben.
         * @return Automatisch generierte Menge an mittelschweren Woertern.
         */
        public static final Word[] _MEDIUM_AUTO(){
            ArrayList<Word> words = new ArrayList<>();
            for(Word w : _ALL){
                if(difficulty(w) > 3 && difficulty(w) < 7)
                    words.add(w);
            }
            return words.toArray(new Word[_ALL.length/3]);
        }
        /**
         * Generiert automatisch eine Menge an schweren Woertern.
         * Definiert sind schwere Woerter derzeit als solche, die eine Schwierigkeit ueber 5 (exkl.)
         * nach der difficulty(Word)-Methode haben.
         * @return Automatisch generierte Menge an schweren Woertern.
         */
        public static final Word[] _HARD_AUTO(){
            ArrayList<Word> words = new ArrayList<>();
            for(Word w : _ALL){
                if(difficulty(w) > 5)
                    words.add(w);
            }
            return words.toArray(new Word[_ALL.length/3]);
        }
        static final Predicate<Word> predicate_easy_length = new Predicate<Word>() {
            @Override
            public boolean test(Word word) {
                return word.WORD.length() < 6;
            }
        };
        static final Predicate<Word> predicate_medium_length = new Predicate<Word>() {
            @Override
            public boolean test(Word word) {
                return word.WORD.length() > 4 && word.WORD.length() < 9;
            }
        };
        static final Predicate<Word> predicate_hard_length = new Predicate<Word>(){
            @Override
            public boolean test(Word word) {
                return word.WORD.length() > 6;
            }
        };
        public static final Word[] _EASY_LENGTH(){
            return _PREDICATE_WORDS(predicate_easy_length);
        }
        public static final Word[] _MEDIUM_LENGTH(){
            return _PREDICATE_WORDS(predicate_medium_length);
        }
        public static final Word[] _HARD_LENGTH(){
            return _PREDICATE_WORDS(predicate_hard_length);
        }

        public static final Word[] _PREDICATE_WORDS(Predicate<Word> predicate){
            ArrayList<Word> words = new ArrayList<>();
            words.addAll(Arrays.asList(_ALL));
            words.removeIf(predicate.negate());
            return words.toArray(new Word[]{});
        }
    }

    /**
     * Klasse fuer Vokale. Derzeit nur Vokale als vordefinierte Woerter.
     */
    public static abstract class VOCAL {
        public static final Word A = new Word("a",R.raw.vocal_a, R.drawable.vocal_a);
        public static final Word E = new Word("e",R.raw.vocal_e, R.drawable.vocal_e);
        public static final Word I = new Word("i",R.raw.vocal_i, R.drawable.vocal_i);
        public static final Word O = new Word("o",R.raw.vocal_o, R.drawable.vocal_o);
        public static final Word U = new Word("u",R.raw.vocal_u, R.drawable.vocal_u);
        public static final Word[] _ALL = new Word[]{A, E, I, O, U};
    }
}
