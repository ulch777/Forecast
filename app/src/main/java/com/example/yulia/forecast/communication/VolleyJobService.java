//package com.example.yulia.forecast.communication;
//
//import android.annotation.TargetApi;
//import android.app.job.JobParameters;
//import android.app.job.JobService;
//import android.os.AsyncTask;
//import android.os.Build;
//import android.os.Handler;
//
//import com.example.yulia.forecast.model.Forecast;
//import com.example.yulia.forecast.utils.AppLogger;
//import com.example.yulia.forecast.utils.Program;
//
//
//@TargetApi(Build.VERSION_CODES.LOLLIPOP)
//public class VolleyJobService extends JobService {
//
//    private static final String TAG = "VolleyJobService";
////    private UpdateAppsAsyncTask updateTask;
//
//    /**
//     * If the return value is false,
//     * the system assumes that the task is Done.
//     *
//     * If the return value is true,
//     * then the system assumes that the task isn't Done.
//     * to tell the system when the given task is done call jobFinished(JobParameters params, boolean needsRescheduled).
//     */
//    @Override
//    public boolean onStartJob(JobParameters params) {
//        // Note: this is preformed on the main thread.
//
//        new UpdateAppsAsyncTask(this).execute(params);
//
//        return true;
//    }
//
//    @Override
//    public boolean onStopJob(JobParameters params) {
//        // Note: return true to reschedule this job.
////        boolean shouldReschedule = updateTask.hasJobBeenStopped(params);
////        if(shouldReschedule){
////            jobFinished(params, true);
////        }
//        return true;
//    }
//
//    public static class UpdateAppsAsyncTask extends AsyncTask<JobParameters, Void, JobParameters> {
//
//        VolleyJobService volleyJobService;
//        static boolean  isJobFinished = false;
//
//        UpdateAppsAsyncTask(VolleyJobService volleyJobService) {
//            this.volleyJobService = volleyJobService;
//        }
//
////        static JobParameters params;
//
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//        }
//
//        @Override
//        protected JobParameters doInBackground(JobParameters... params) {
//            // Do updating and stopping logical here.
////            if (Program.getInstance().isConnected()) {
////                JobParameters parameters = params[0];
////
//////                for (final JobParameters params : result) {
////                AppLogger.LogCut("params", String.valueOf(parameters.getJobId()));
////                if (parameters.getExtras() != null && parameters.getExtras().getString(ServerKeys.TAG) != null) {
////                    String str = parameters.getExtras().getString(ServerKeys.TAG);
////                    switch (str) {
////                        case ServerKeys.TAG_GET_SCORE:
////                            VolleyClient.getInstance().newUser(parameters.getExtras().getString(ServerKeys.COUNT),
////                                    parameters.getExtras().getString(ServerKeys.OFFSET), objectRetrieverListener);
////                            break;
////                    }
////                }
////            }
////
////            AppLogger.LogCut(TAG, "UpdateAppsAsyncTask: doInBackground");
//
//            return params[0];
//        }
//
//        @Override
//        protected void onPostExecute(final JobParameters result) {
//            Handler handler = new Handler();
//            handler.postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    if(isJobFinished) {
//                        volleyJobService.jobFinished(result, false);
//                        isJobFinished = false;
//                    }else volleyJobService.jobFinished(result, true);
//                }
//            },4000);
//        }
//
//
//        public static ObjectRetrieverListener<Forecast> objectRetrieverListener = new ObjectRetrieverListener<Forecast>() {
//            @Override
//            public void receivedObject(Forecast object) {
//                AppLogger.LogCut("Success scheduled send", object.toString());
////                EventBus.getDefault().post(new MessageEvent(MainActivity.SCORES));
//                isJobFinished = true;
////                volleyJobService.jobFinished();
////            VolleyJobService.UpdateAppsAsyncTask.stopJob(VolleyJobService.UpdateAppsAsyncTask.getParams(), Program.getInstance().getApplicationContext());
//            }
//
//            @Override
//            public void error(int err) {
//                AppLogger.LogCut("error", String.valueOf(err));
//                isJobFinished = false;
//            }
//
//            @Override
//            public void exception(String e) {
//                isJobFinished = false;
//                AppLogger.LogCut("exception", e);
//            }
//        };
//
//    }
//}
