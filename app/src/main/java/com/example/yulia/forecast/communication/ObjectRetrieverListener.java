package com.example.yulia.forecast.communication;

public interface ObjectRetrieverListener<T> {

    void receivedObject(T object);
    void error(int err);
    void exception(String e);
}