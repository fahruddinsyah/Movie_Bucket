package my.tech.moviebucket.service;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.widget.Toast;

public class ReleaseTodayTask {

    private int jobId = 10;

    public void startJob(Context context) {
        if (isJobRunning(context)) {
            Toast.makeText(context, "Daily release already scheduled", Toast.LENGTH_SHORT).show();
            return;
        }

        ComponentName mServiceComponent = new ComponentName(context, GetReleaseTodayJobService.class);
        JobInfo.Builder builder = new JobInfo.Builder(jobId, mServiceComponent);
        builder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY);
        builder.setRequiresDeviceIdle(false);
        builder.setRequiresCharging(false);
        builder.setPeriodic(900000);
        builder.setPersisted(true);

        JobScheduler jobScheduler = (JobScheduler) context.getSystemService(Context.JOB_SCHEDULER_SERVICE);
        if (jobScheduler != null) {
            jobScheduler.schedule(builder.build());
        }

        Toast.makeText(context, "Job Service started", Toast.LENGTH_SHORT).show();
    }

    public void cancelJob(Context context) {
        JobScheduler tm = (JobScheduler) context.getSystemService(Context.JOB_SCHEDULER_SERVICE);
        if (tm != null) {
            tm.cancel(jobId);
        }
        Toast.makeText(context, "Job Service canceled", Toast.LENGTH_SHORT).show();

    }

    private boolean isJobRunning(Context context) {
        boolean isScheduled = false;

        JobScheduler scheduler = (JobScheduler) context.getSystemService(Context.JOB_SCHEDULER_SERVICE);

        if (scheduler != null) {
            for (JobInfo jobInfo : scheduler.getAllPendingJobs()) {
                if (jobInfo.getId() == jobId) {
                    isScheduled = true;
                    break;
                }
            }
        }

        return isScheduled;
    }
}
