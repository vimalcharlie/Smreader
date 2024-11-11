package com.example.newsmreader.Jobservice;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.util.Log;

public class JobSchedulerHelper {

    public static void scheduleJob(Context context, int jobId, long desiredTimeInMillis) {
        ComponentName componentName = new ComponentName(context, MyJobService.class);

        JobInfo.Builder builder = new JobInfo.Builder(jobId, componentName)
                .setMinimumLatency(desiredTimeInMillis)
                .setPersisted(true);

        JobScheduler jobScheduler = (JobScheduler) context.getSystemService(Context.JOB_SCHEDULER_SERVICE);
        jobScheduler.schedule(builder.build());

        Log.i("JobSchedulerHelper", "Job scheduled at: " + desiredTimeInMillis);
    }
}
