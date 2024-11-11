package com.example.newsmreader.Jobservice;


import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

public class MyJobService extends JobService {

    private static final String TAG = "MyJobService";

    @Override
    public boolean onStartJob(JobParameters params) {
        Log.i(TAG, "onStartJob: Job started");




        // Notify the system when the task is complete
        jobFinished(params, false);

        return false; // Return false if the job is not rescheduled
    }


    private void updateSharedPreferences() {
        // Obtain SharedPreferences instance
        SharedPreferences sharedPreferences = getSharedPreferences("My pref", Context.MODE_PRIVATE);

        // Edit the values in SharedPreferences
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("key1", "New Value");  // Change the value as needed
        editor.apply();
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        Log.i(TAG, "onStopJob: Job stopped");
        // Perform background task
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                // Your background task goes here

                // Change the value in SharedPreferences
                updateSharedPreferences();

                // Notify the system when the task is complete
                jobFinished(params, false);

                return null;
            }
        }.execute();
        return true; // Return true to reschedule the job if necessary
    }
}
