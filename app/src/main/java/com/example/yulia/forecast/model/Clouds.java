package com.example.yulia.forecast.model;

/**
 * Created by Yulia on 12.05.2017.
 */

public class Clouds
{
    private String all;

    public String getAll ()
    {
        return all;
    }

    public void setAll (String all)
    {
        this.all = all;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [all = "+all+"]";
    }
}