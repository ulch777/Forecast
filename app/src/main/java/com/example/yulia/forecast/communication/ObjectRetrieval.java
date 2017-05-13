package com.example.yulia.forecast.communication;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.yulia.forecast.utils.AppLogger;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

import org.json.JSONObject;

import java.io.StringReader;

public abstract class ObjectRetrieval<T> implements Response.Listener<String>, Response.ErrorListener {

    private final String TAG = "ObjectRetrieval";
    private ObjectRetrieverListener<T> objectRetrieverListener;
    private Class<T> tClass;

    public ObjectRetrieval(ObjectRetrieverListener<T> objectRetrieverListener, Class<T> tClass) {
        super();
        this.tClass = tClass;
        this.objectRetrieverListener = objectRetrieverListener;
    }

    @Override
    public void onResponse(String response) {
        AppLogger.LogCut(TAG, "onResponse: " + response);
        try {
            JSONObject jsonResponse = new JSONObject(response);
//            if (jsonResponse.getBoolean(ServerKeys.SUCCESS_TAG)) {
//                String obj = jsonResponse.getString(ServerKeys.DATA_TAG);
//                JsonReader reader = new JsonReader(new StringReader(obj));
            JsonReader reader = new JsonReader(new StringReader(response));
            reader.setLenient(true);
            objectRetrieverListener.receivedObject((T) new Gson().fromJson(reader, tClass));
//            } else {
//                objectRetrieverListener.error(jsonResponse.getInt(ServerKeys.ERROR_TAG));
//            }
        } catch (Exception e) {
            AppLogger.LogCut(TAG, "onResponse Exception: " + e);
            objectRetrieverListener.exception(e.getMessage());
        }
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        AppLogger.LogCut(TAG, "onErrorResponse Exception: " + error);
        objectRetrieverListener.exception(error.getMessage());
    }
}