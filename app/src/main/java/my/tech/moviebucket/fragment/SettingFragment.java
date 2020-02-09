package my.tech.moviebucket.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Switch;

import androidx.fragment.app.Fragment;

import my.tech.moviebucket.R;
import my.tech.moviebucket.service.AppPreference;
import my.tech.moviebucket.service.DailyReminderReceiver;
import my.tech.moviebucket.service.ReleaseReminderReceiver;

/**
 * A simple {@link Fragment} subclass.
 */
public class SettingFragment extends Fragment implements View.OnClickListener {

    private LinearLayout settingLocale;
    private Switch switchDaily, switchRelease;

    private DailyReminderReceiver dailyReminderReceiver;
    private ReleaseReminderReceiver releaseReminderReceiver;
    private AppPreference appPreference;
    private boolean isReleaseToday = false;
    private boolean isDailyNotif = false;

    public SettingFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_setting, container, false);

        settingLocale = view.findViewById(R.id.setting_language);
        switchDaily = view.findViewById(R.id.switch_daily);
        switchRelease = view.findViewById(R.id.switch_release);

        appPreference = new AppPreference(getContext());
        dailyReminderReceiver = new DailyReminderReceiver();
        releaseReminderReceiver = new ReleaseReminderReceiver();

        settingLocale.setOnClickListener(this);
        switchDaily.setOnClickListener(this);
        switchRelease.setOnClickListener(this);

        setEnableDisableNotif();

        return view;
    }

    private void setEnableDisableNotif() {
        if (appPreference.isDailyNotif()) {
            switchDaily.setChecked(true);
        } else {
            switchDaily.setChecked(false);
        }

        if (appPreference.isReleaseToday()) {
            switchRelease.setChecked(true);
        } else {
            switchRelease.setChecked(false);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.setting_language:
                Intent settingLanguage = new Intent(Settings.ACTION_LOCALE_SETTINGS);
                startActivity(settingLanguage);
                break;
            case R.id.switch_daily:
                isDailyNotif = switchDaily.isChecked();
                if (isDailyNotif) {
                    switchDaily.setEnabled(true);
                    appPreference.setupDaily(isDailyNotif);
                    dailyReminderReceiver.setRepeatingAlarm(requireContext(), getString(R.string.app_name),
                            "07:00", "Hello, Check your favorite movie today");
                } else {
                    switchDaily.setChecked(false);
                    appPreference.setupDaily(isDailyNotif);
                    dailyReminderReceiver.cancelAlarm(requireContext());
                }
                break;
            case R.id.switch_release:
                isReleaseToday = switchRelease.isChecked();
                if (isReleaseToday) {
                    switchRelease.setEnabled(true);
                    appPreference.setupRelease(isReleaseToday);
                    releaseReminderReceiver.setRepeatingAlarm(requireContext(), getString(R.string.app_name),
                            "08:00", "Release today");
                } else {
                    switchRelease.setChecked(false);
                    appPreference.setupRelease(isReleaseToday);
                    releaseReminderReceiver.cancelAlarm(requireContext());
                }
                break;
        }

    }

}
















