package my.tech.moviebucket.service;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import my.tech.moviebucket.MainActivity;
import my.tech.moviebucket.R;
import my.tech.moviebucket.api.ApiClient;
import my.tech.moviebucket.api.GetService;
import my.tech.moviebucket.entity.Movie;
import my.tech.moviebucket.entity.MovieResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReleaseReminderReceiver extends BroadcastReceiver {

    private static final String GROUP_KEY_RELEASE = "group_key_emails";
    private static final String EXTRA_MESSAGE = "message";
    private static final String EXTRA_TITLE = "title";
    private final int NOTIF_ID_RELEASE = 100;
    private final static int MAX_RELEASE = 15;
    private static int MIN_RELEASE = 0;

    private ArrayList<Movie> listMovies = new ArrayList<>();

    public ReleaseReminderReceiver() {

    }

    @Override
    public void onReceive(final Context context, Intent intent) {
        final String message = intent.getStringExtra(EXTRA_MESSAGE);
        final String title = intent.getStringExtra(EXTRA_TITLE);

        final int notifId = NOTIF_ID_RELEASE;

        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        final String DATE = dateFormat.format(date);

        GetService service = ApiClient.getClient().create(GetService.class);
        Call<MovieResponse> call = service.getReleaseMovies(ApiClient.API_KEY, DATE, DATE);
        call.enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                listMovies = response.body().getResults();
                for (int i = 0; i < listMovies.size(); i++) {
                    String movieTitle = listMovies.get(i).getTitle();
                    MIN_RELEASE++;
                    showAlarmReleaseNotification(context, title, movieTitle, message, notifId);
                }
            }

            @Override
            public void onFailure(Call<MovieResponse> call, Throwable t) {

            }
        });
    }

    private void showAlarmReleaseNotification(Context context, String title, String movieTitle, String message, int notifId) {
        String CHANNEL_ID = "channel_02";
        String CHANNEL_NAME = "release_channel";

        NotificationManager notificationManagerCompat = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Bitmap largeIcon = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_movie);
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        Intent intent = new Intent(context, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, NOTIF_ID_RELEASE, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder builder;

        if (MIN_RELEASE < MAX_RELEASE) {
            builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                    .setContentTitle(movieTitle)
                    .setContentText(title + " - " + message)
                    .setSmallIcon(R.drawable.ic_movie)
                    .setContentIntent(pendingIntent)
                    .setLargeIcon(largeIcon)
                    .setGroup(GROUP_KEY_RELEASE)
                    .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000})
                    .setAutoCancel(true)
                    .setSound(alarmSound);
        } else {

            NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();
            for (Movie movie : listMovies) {
                inboxStyle.addLine(movie.getTitle());
            }
            inboxStyle.setBigContentTitle(MIN_RELEASE + " Movies Release Today ");
            inboxStyle.setSummaryText("New Movie");

            builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                    .setContentTitle(MIN_RELEASE + " new Movie")
                    .setContentText(message)
                    .setSmallIcon(R.drawable.ic_movie)
                    .setGroup(GROUP_KEY_RELEASE)
                    .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000})
                    .setGroupSummary(true)
                    .setContentIntent(pendingIntent)
                    .setStyle(inboxStyle)
                    .setAutoCancel(true);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_DEFAULT);

            channel.enableVibration(true);
            channel.setVibrationPattern(new long[]{1000, 1000, 1000, 1000, 1000});

            builder.setChannelId(CHANNEL_ID);

            if (notificationManagerCompat != null) {
                notificationManagerCompat.createNotificationChannel(channel);
            }
        }

        Notification notification = builder.build();

        if (notificationManagerCompat != null) {
            notificationManagerCompat.notify(notifId, notification);
        }

    }

    public void setRepeatingAlarm(Context context, String title, String time, String message) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(context, ReleaseReminderReceiver.class);
        intent.putExtra(EXTRA_MESSAGE, message);
        intent.putExtra(EXTRA_TITLE, title);

        String[] timeArray = time.split(":");

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(timeArray[0]));
        calendar.set(Calendar.MINUTE, Integer.parseInt(timeArray[1]));
        calendar.set(Calendar.SECOND, 0);

        int requestCode = NOTIF_ID_RELEASE;
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, requestCode, intent, 0);
        if (alarmManager != null) {
            alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
        }

        Toast.makeText(context, "Release Reminder Enable", Toast.LENGTH_SHORT).show();
    }

    public void cancelAlarm(Context context) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, ReleaseReminderReceiver.class);
        int requestCode = NOTIF_ID_RELEASE;
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, requestCode, intent, 0);
        if (alarmManager != null) {
            alarmManager.cancel(pendingIntent);
        }
        Toast.makeText(context, "Turn off Reminder", Toast.LENGTH_SHORT).show();
    }

}
