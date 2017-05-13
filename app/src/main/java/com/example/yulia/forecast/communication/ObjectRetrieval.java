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
            JsonReader reader = new JsonReader(new StringReader(response));
            reader.setLenient(true);
            objectRetrieverListener.receivedObject((T) new Gson().fromJson(reader, tClass));
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