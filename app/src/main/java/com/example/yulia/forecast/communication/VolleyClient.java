package com.example.yulia.forecast.communication;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.example.yulia.forecast.model.Forecast;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;


public class VolleyClient {

    public static String API_LINK = "http://api.openweathermap.org/data/2.5/weather";
    private static VolleyClient instance;

    private final String TAG = "Volley";

    private VolleyClient() { /* no instances */ }

    public static VolleyClient getInstance() {
        if (instance == null)
            instance = new VolleyClient();
        return instance;
    }
    public void getForecast(String city, ObjectRetrieverListener<Forecast> listener) {
        RequestQueue queue = MyVolley.getRequestQueue();
        Map<String, String> params = new HashMap<>();
        params.put(ServerKeys.TAG, city);
        params.put(ServerKeys.UNITS, ServerKeys.METRIC);
        params.put(ServerKeys.APPID, ServerKeys.API_KEY);
        ObjectRetrieval<Forecast> objectRetrieval = new ObjectRetrieval<Forecast>(listener, Forecast.class) {};
        StringRequest sReq = GetVolleyStringRequest(Request.Method.POST, API_LINK, params, objectRetrieval);
        queue.add(sReq);
    }

    private <T> StringRequest GetVolleyStringRequest(int method,
                                                     String baseUrl, final Map<String, String> params,
                                                     ObjectRetrieval<T> objectRetrieval) {
        String url = baseUrl;
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
        return new StringRequest(method, url, objectRetrieval, objectRetrieval) {

            protected Map<String, String> getParams() throws com.android.volley.AuthFailureError {
                return params;
            }
        };
    }
}