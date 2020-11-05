package util;

import android.app.Activity;
import android.content.SharedPreferences;

public class Prefs {
    private SharedPreferences preferences;

    public Prefs(Activity activity) {
        this.preferences = activity.getPreferences(activity.MODE_PRIVATE);
    }

    public void saveHighScore(int score) {
        int currentScore = score;
        int lastScore = preferences.getInt("high_score", 0);

        if(currentScore > lastScore) {
            preferences.edit().putInt("high_score", currentScore).apply();
        }
    }

    public int getHighScore(){
        return preferences.getInt("high_score", 0);
    }

    public void setState (int index){
        preferences.edit().putInt("index_state", index).apply();
    }

    public int getState () {
        return preferences.getInt("index_state", 0);
    }

    public void saveCount (int count) {
        preferences.edit().putInt("sumCount", count).apply();
    }

    public int getCount () {
        return preferences.getInt("sumCount", 0);
    }
}
