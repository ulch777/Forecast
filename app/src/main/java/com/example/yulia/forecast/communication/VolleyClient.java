package com.example.yulia.forecast.communication;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.os.Build;
import android.os.PersistableBundle;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.yulia.forecast.model.Forecast;
import com.example.yulia.forecast.utils.AppLogger;
import com.example.yulia.forecast.utils.Program;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;


public class VolleyClient {
//    public static String API_LINK = "http://eggsy.vortex3d.com";
    public static String API_LINK = "http://api.openweathermap.org/data/2.5/weather";
    private static VolleyClient instance;

    private final String TAG = "Volley";

    private VolleyClient() { /* no instances */ }

    public static VolleyClient getInstance() {
        if (instance == null)
            instance = new VolleyClient();
        return instance;
    }
    public boolean getForecast(String city, ObjectRetrieverListener<Forecast> listener) {
        Map<String, String> params = new HashMap<>();
        params.put(ServerKeys.TAG, city);
        params.put(ServerKeys.UNITS, ServerKeys.METRIC);
        params.put(ServerKeys.APPID, ServerKeys.API_KEY);
        ObjectRetrieval<Forecast> objectRetrieval = new ObjectRetrieval<Forecast>(listener, Forecast.class) {
        };
        return sendStringRequest(ServerKeys.FILE_USER, params, objectRetrieval);
    }

//    public boolean newUser(String count, String offset, ObjectRetrieverListener<Scores> listener) {
//        Map<String, String> params = new HashMap<>();
//        params.put(ServerKeys.TAG, ServerKeys.TAG_GET_SCORE);
//        params.put(ServerKeys.COUNT, count);
//        params.put(ServerKeys.OFFSET, offset);
//        ObjectRetrieval<Scores> objectRetrieval = new ObjectRetrieval<Scores>(listener, Scores.class) {
//        };
//        return sendStringRequest(ServerKeys.FILE_USER, params, objectRetrieval);
//    }

    private <T> boolean sendStringRequest(String fileName, Map<String, String> params, ObjectRetrieval<T> listener) {
        AppLogger.LogCut(TAG, fileName + " : " + params);
        StringRequest sReq = GetVolleyStringRequest(Request.Method.POST, API_LINK + fileName, params, listener);
        // change the retry policy- change the timeout to 2*default. and set the retries times to 0
        sReq.setRetryPolicy(new DefaultRetryPolicy(2 * DefaultRetryPolicy.DEFAULT_TIMEOUT_MS, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        if (!Program.getInstance().isConnected()) {
            listener.onErrorResponse(new VolleyError("No Network Connection"));

//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
////                send the sReq to VolleyJobService to handle it latter
//                Context context = Program.getInstance().getApplicationContext();
//                ComponentName serviceName = new ComponentName(context, VolleyJobService.class);
//                Random r = new Random();
//                int jobId = r.nextInt(1000000 - 1) + 1;
//                PersistableBundle extras = new PersistableBundle();
//                for (Map.Entry<String,String> entry : params.entrySet()) {
//                    AppLogger.LogCut("send",entry.getKey()+ " "+entry.getValue());
//                    extras.putString(entry.getKey(),entry.getValue());
//                }
//                JobInfo jobInfo = new JobInfo.Builder(jobId, serviceName)
////                        .setPeriodic(5000)
//                        .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
//                        .setExtras(extras)
//                        .build();
//                JobScheduler scheduler = (JobScheduler) context.getSystemService(Context.JOB_SCHEDULER_SERVICE);
//                int result = scheduler.schedule(jobInfo);
//                if (result == JobScheduler.RESULT_SUCCESS) AppLogger.LogCut(TAG, "Job scheduled successfully!");
//                else if (result <= 0) AppLogger.LogCut(TAG, "JobScheduler: Something went wrong");
//            }
            return false;
        }else {

            RequestQueue queue = MyVolley.getRequestQueue();
            queue.add(sReq);
            return true;
        }
    }

    private <T> StringRequest GetVolleyStringRequest(int method,
                                                     String baseUrl, final Map<String, String> params,
                                                     ObjectRetrieval<T> objectRetrieval) {
        String url = baseUrl;
//        if (method == Request.Method.GET) {
            StringBuilder queryString = new StringBuilder();
            String prefix = "?";
            for (String key : params.keySet()) {
                try {
                    queryString.append(prefix);
                    queryString.append(key);
                    queryString.append("=");
                    queryString.append(URLEncoder.encode(params.get(key), "UTF-8"));
                } catch (UnsupportedEncodingException ignored) {
                }
                prefix = "&";
            }
            url += queryString.toString();
//        }
        StringRequest myStringReq = new StringRequest(method, url, objectRetrieval, objectRetrieval) {

            protected Map<String, String> getParams() throws com.android.volley.AuthFailureError {
                return params;
            }
        };
        return myStringReq;
    }
}