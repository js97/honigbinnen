package de.tum.in.l4k.binnenvokalapp;


import android.content.Context;
import android.content.SharedPreferences;
import android.util.Pair;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public abstract class StorageManager {
    public static void writeLines(Context c, String directoryName, String fileName, String... lines){
        File file = new File(c.getFilesDir(), directoryName);
        if(!file.exists()){
            file.mkdir();
        }
        try{
            File f = new File(file, fileName);
            FileWriter fw = new FileWriter(f);
            BufferedWriter bw = new BufferedWriter(fw);
            for(String s : lines) {bw.write(s.trim()); bw.newLine();}//fw.append(s.trim());
            bw.flush();
            bw.close();
        } catch (IOException ie){
            ie.printStackTrace();
        }
    }
    public static String STD_DIR = "Learning4Kids";
    public static void wrLines(Context c, String fileName, String... lines){
        writeLines(c, STD_DIR, fileName, lines);
    }
    public static String[] rdLines(Context c, String fileName){
        return readLines(c, STD_DIR, fileName);
    }
    /*
    @Deprecated
    public static void writeLines(String fileName, String... lines){
        File f = new File(STD_DIR);
        if(!f.exists()){
            f.mkdir();
        }
        File file = new File(STD_DIR, fileName);
        try {
            if(!file.exists()) file.createNewFile();
            FileWriter fw = new FileWriter(file);
            for(String s : lines) fw.append(s.trim());
            fw.flush();
            fw.close();
        } catch (IOException ie){
            ie.printStackTrace();
        }
    }
    @Deprecated
    public static String[] readLines(String fileName){
        ArrayList<String> sal = new ArrayList<>();
        File file = new File(fileName);
        try{
            //FileInputStream fis = new FileInputStream(file);
            //fis.available()
            BufferedReader br = new BufferedReader(new FileReader(file));
            for(String s = br.readLine(); s != null; s = br.readLine()){
                sal.add(s);
            }
        } catch(IOException io){
            io.printStackTrace();
        }
        return (String[])(sal.toArray(new String[]{}));
    }*/
    public static String[] readLines(Context c, String directoryName, String fileName){
        ArrayList<String> sal = new ArrayList<>();
        File file = new File(c.getFilesDir(), directoryName);
        try{
            //FileInputStream fis = new FileInputStream(file);
            //fis.available()
            File toRead = new File(file, fileName);
            BufferedReader br = new BufferedReader(new FileReader(toRead));
            for(String s = br.readLine(); s != null; s = br.readLine()){
                sal.add(s);
            }
        } catch(IOException io){
            io.printStackTrace();
        }
        return (String[])(sal.toArray(new String[]{}));
    }
    /*public static String STD_FILE = "saveData.txt";
    public static void storeSomehow(String... strings){
        writeLines(STD_FILE, strings);
    }*/
    public static class Table<Key, Value>{
        private ArrayList<Pair<Key, Value>> map;
        public Table(){
            map = new ArrayList<>();
        }
        public Value get(Key key){
            for(Pair<Key, Value> p : map){
                if(p.first.equals(key)){
                    return p.second;
                }
            }
            return null;
        }
        public int positionOf(Key key){
            //return get(key)==null; no -> you can't store null like this
            int i = 0;
            for(Pair<Key, Value> p : map){
                if(p.first.equals(key)){
                    return i;
                }
                i++;
            }
            return -1;
        }
        public boolean contains(Key key){
            return positionOf(key)!=-1;
        }
        //true, if contained before (overwritten)
        public boolean set(Key key, Value value){
            if(contains(key)){
                int pos = positionOf(key);
                map.set(pos, new Pair<>(key, value));
                return true;
            } else {
                map.add(new Pair<>(key, value));

            }
            return false;
        }
        @Override
        public String toString(){
            return map.toString();
        }
        public static<K,V> String[] toLines(Table<K,V> table){
            String[] s = new String[table.map.size()];
            int i = 0;
            for(Pair<K,V> p : table.map){
                s[i] = p.first.toString() + ":~:" + p.second.toString();
                i++;
            }
            return s;
        }
        public static Table<String,String> parse(String[] lines){
            Table<String, String> t = new Table<>();
            for(String s : lines){
                String[] ss = s.split(":~:");
                t.set(ss[0], ss[1]);
            }
            return t;
        }
    }
    //public abstract static class LevelProgress{
        public static final String fileName = "LevelProgress.txt";

        public static Level levelFromID(int i) {
            switch(i){
                case 0:
                    return Level.tutorial;
                case 1:
                    return Level.l1;
                case 2:
                    return Level.l2;
                case 3:
                    return Level.l3;
                case 4:
                    return Level.l4;
                case 5:
                    return Level.l5;
                default:
                    return null;
            }
        }

        //private Table<String, String> progressTable = new Table<>();
        //public static final LevelProgress INSTANCE = new LevelProgress();
        //private LevelProgress(){}
        static int level_ID(Level l){
            switch(l){
                case tutorial:
                    return 0;
                case l1:
                    return 1;
                case l2:
                    return 2;
                case l3:
                    return 3;
                case l4:
                    return 4;
                case l5:
                    return 5;
                default:
                    return -1;
            }
        }
        public static boolean passed(Level l, Context c){
            Table<String, String> readFile = Table.parse(StorageManager.rdLines(c, fileName));
            if(readFile.get(level_ID(l)+"") == null)
                return false;
            return readFile.get(level_ID(l)+"").equals("1");
        }
        public static void pass(Level l, Context c){
            Table<String, String> readFile = Table.parse(StorageManager.rdLines(c, fileName));
            readFile.set(level_ID(l)+"", "1");
            StorageManager.wrLines(c, fileName, Table.toLines(readFile));
        }
        static void unpass(Level l, Context c){
            Table<String, String> readFile = Table.parse(StorageManager.rdLines(c, fileName));
            readFile.set(level_ID(l)+"", "0");
            StorageManager.wrLines(c, fileName, Table.toLines(readFile));
        }
    //}
}
abstract class SharedPrefManager{
    static final int MODE = Context.MODE_PRIVATE;
    public static void store(String key, int val, AppCompatActivity calling){
        SharedPreferences sharedPreferences = calling.getSharedPreferences("Levels", MODE);
        SharedPreferences.Editor e = sharedPreferences.edit();
        e.putInt(key, val);
        e.commit();
    }
    public static int read(String key, AppCompatActivity calling){
        return calling.getSharedPreferences("Levels", MODE).getInt(key,0);
    }
    public static void store(Level l, int val, AppCompatActivity calling){
        store(""+level_ID(l),val,calling);
    }
    public static int read(Level l, AppCompatActivity calling){
        return read(""+level_ID(l), calling);
    }
    public static void pass(Level l, AppCompatActivity calling){
        store(l, read(l,calling)+1, calling);
    }
    public static boolean passed(Level l, AppCompatActivity calling){
        return read(l,calling) > 0;
    }
    public static int passes(Level l, AppCompatActivity calling){
        return read(l,calling);
    }
    static int level_ID(Level l){
        switch(l){
            case tutorial:
                return 0;
            case l1:
                return 1;
            case l2:
                return 2;
            case l3:
                return 3;
            case l4:
                return 4;
            case l5:
                return 5;
            default:
                return -1;
        }
    }
    public static Level levelFromID(int i) {
        switch(i){
            case 0:
                return Level.tutorial;
            case 1:
                return Level.l1;
            case 2:
                return Level.l2;
            case 3:
                return Level.l3;
            case 4:
                return Level.l4;
            case 5:
                return Level.l5;
            default:
                return null;
        }
    }
    public static void storeColor(int color, AppCompatActivity calling){
        store("Farbe", color, calling);
    }
    public static int loadColor(AppCompatActivity calling){
        return read("Farbe", calling);
    }
    public static void storePerfection(Level l, AppCompatActivity calling){
        store(""+level_ID(l)+"perfect",1,calling);
    }
    public static boolean readPerfection(Level l, AppCompatActivity calling){
        return read(""+level_ID(l)+"perfect", calling)==1;
    }

    public static int storeTimeIfImproving(Level currentLevel, long timetotal, AppCompatActivity calling) {
        int previous;
        if((previous = read(""+level_ID(currentLevel)+"time",calling)) > timetotal || read(""+level_ID(currentLevel)+"time",calling) <= 0) {
            store("" + level_ID(currentLevel) + "time", (int) timetotal, calling);
        }
        return previous;
    }
    public static int readTime(Level currentLevel, AppCompatActivity calling){
        return read(""+level_ID(currentLevel)+"time", calling);
    }

    static void reset_all_progress(AppCompatActivity calling){
        for(Level l : new Level[]{Level.tutorial,Level.l1, Level.l2, Level.l3, Level.l4, Level.l5}){
            store(l, 0, calling);
            if(l == Level.tutorial) continue;
            store(""+level_ID(l)+"time",0,calling);
            store(""+level_ID(l)+"perfect", 0, calling);
            for(AchievementType at : AchievementType.values()){
                resetAchievement(at, l, calling);
            }
        }
        resetAchievementPushInfo(calling);
    }
    static void storeAchievement(AchievementType at, Level level, AppCompatActivity calling){
        store(at.toString()+level_ID(level),1,calling);
    }
    private static void resetAchievement(AchievementType at, Level level, AppCompatActivity calling){
        store(at.toString()+level_ID(level),0,calling);
    }
    public static boolean readAchievement(AchievementType at, Level level, AppCompatActivity calling){
        return read(at.toString()+level_ID(level), calling) == 1;
    }
    static void storeAchievementPushInfo(AppCompatActivity calling){
        store("achievementupdate",1,calling);
    }
    static void resetAchievementPushInfo(AppCompatActivity calling){
        store("achievementupdate", 0, calling);
    }
    public static boolean readAchievementPushInfo(AppCompatActivity calling){
        return read("achievementupdate",calling)==1;
    }
}
abstract class StorageSelectingManager{
    public static Level levelFromID(int i) {
        return StorageManager.levelFromID(i);
    }

    enum StoreType {
            FILE, SHAREDPREF,
    }
    static final StoreType STORE_TYPE = StoreType.SHAREDPREF;
    public static void pass(Level l, Object calling){
        switch(STORE_TYPE){
            case FILE:
                StorageManager.pass(l, (Context)calling);
                break;
            case SHAREDPREF:
                SharedPrefManager.pass(l, (AppCompatActivity) calling);
                break;
        }
    }
    public static boolean passed(Level l, Object calling){
        switch(STORE_TYPE){
            case FILE:
                return StorageManager.passed(l, (Context)calling);
            case SHAREDPREF:
                return SharedPrefManager.passed(l, (AppCompatActivity)calling);
            default:
                return false;
        }
    }
}
enum Level{
    tutorial, l1, l2, l3, l4, l5
}
enum AchievementType{
    // Pokale:
    atp1, atp2, atp3, atp4,
    // Praezision:
    atprec,
    // Zeiten:
    att5, att10, att15, att20, att25, att30, att35, att40,
}