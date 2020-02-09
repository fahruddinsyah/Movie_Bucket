package my.tech.moviebucket.service;

import android.content.Context;
import android.content.SharedPreferences;

public class AppPreference {

    private static final String PREF_NAME = "user_pref";

    private static final String KEY_RELEASE = "release";
    private static final String KEY_DAILY = "daily";

    private final SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    public AppPreference(Context context) {
        preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public void setupRelease(Boolean status) {
        editor = preferences.edit();
        editor.putBoolean(KEY_RELEASE, status);
        editor.apply();
    }

    public void setupDaily(Boolean status) {
        editor = preferences.edit();
        editor.putBoolean(KEY_DAILY, status);
        editor.apply();
    }

    public boolean isReleaseToday() {
        return preferences.getBoolean(KEY_RELEASE, false);
    }

    public boolean isDailyNotif() {
        return preferences.getBoolean(KEY_DAILY, false);
    }
}
