package com.example.yulia.forecast;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yulia.forecast.communication.MyVolley;
import com.example.yulia.forecast.communication.ObjectRetrieverListener;
import com.example.yulia.forecast.communication.VolleyClient;
import com.example.yulia.forecast.model.Forecast;
import com.example.yulia.forecast.utils.AppLogger;
import com.example.yulia.forecast.utils.Utils;

import java.net.URL;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    private static final String TEMP = "temp";
    private static final String TEMP_MIN = "temp_min";
    private static final String TEMP_MAX = "temp_max";
    private static final String MAIN = "main";
    private static final String CITY = "city";
    private static final String ICON = "icon";
    private static final String DEFAULT_CITY = "Dnipropetrovsk";
    private static final String DEGREES_SIGN = "\u00b0";
    private static final String ICON_URL = "http://openweathermap.org/img/w/";
    private static final String ICON_EXT = ".png";
    private static final long UPDATE_TIME = 30*60*1000;


    private ImageView imvIcon;
    private TextView tvCity;
    private TextView tvTemp;
    private TextView tvTempMax;
    private TextView tvTempMin;
    private TextView tvMain;
    private String cityName;
    private Timer mTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init(savedInstanceState);

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(CITY, tvCity.getText().toString());
        outState.putString(TEMP, tvTemp.getText().toString());
        outState.putString(TEMP_MIN, tvTempMin.getText().toString());
        outState.putString(TEMP_MAX, tvTempMax.getText().toString());
        outState.putString(MAIN, tvMain.getText().toString());
        Bitmap bitmap = ((BitmapDrawable) imvIcon.getDrawable()).getBitmap();
        outState.putParcelable(ICON, bitmap);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_dnipro:
                cityName = getResources().getString(R.string.action_dnipro);
                break;
            case R.id.action_zaporizhzhya:
                cityName = getResources().getString(R.string.action_zaporizhzhya);
                break;
            case R.id.action_kharkiv:
                cityName = getResources().getString(R.string.action_kharkiv);
                break;
        }
        getForecast(cityName);
        return super.onOptionsItemSelected(item);
    }

    private void init(Bundle args) {
        MyVolley.init(this);
        imvIcon = (ImageView) findViewById(R.id.imvIcon);
        tvCity = (TextView) findViewById(R.id.tvCity);
        tvTemp = (TextView) findViewById(R.id.tvTemp);
        tvTempMax = (TextView) findViewById(R.id.tvTempMax);
        tvTempMin = (TextView) findViewById(R.id.tvTempMin);
        tvMain = (TextView) findViewById(R.id.tvMain);
        if (args != null) {
            tvCity.setText(args.getString(CITY));
            tvTemp.setText(args.getString(TEMP));
            tvTempMax.setText(args.getString(TEMP_MAX));
            tvTempMin.setText(args.getString(TEMP_MIN));
            tvMain.setText(args.getString(MAIN));
            imvIcon.setImageBitmap((Bitmap) args.getParcelable(ICON));
            cityName = args.getString(CITY);
        } else {
            cityName = DEFAULT_CITY;
            getForecast(cityName);
        }
        mTimer = new Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                getForecast(cityName);
            }
        };
        mTimer.schedule(timerTask, UPDATE_TIME , UPDATE_TIME);
    }

    private void getForecast(String city) {
        if (Utils.isConnected(this)) {
            if (!TextUtils.isEmpty(city)) {
                VolleyClient.getInstance().getForecast(city, getForecastListener);
            }
        } else
            Toast.makeText(MainActivity.this, "No internet connection", Toast.LENGTH_SHORT).show();

    }

    private String getStringTemp(String temp) {
        String t;
        int tmp = (int) Double.parseDouble(temp);
        if (tmp > 0) {
            t = "+" + tmp + DEGREES_SIGN;
        } else t = tmp + DEGREES_SIGN;
        return t;

    }

    ObjectRetrieverListener<Forecast> getForecastListener = new ObjectRetrieverListener<Forecast>() {
        @Override
        public void receivedObject(Forecast object) {
            AppLogger.LogCut(object.toString());
            tvCity.setText(object.getName());
            tvTemp.setText(getStringTemp(object.getMain().getTemp()));
            tvTempMax.setText(getStringTemp(object.getMain().getTemp_max()));
            tvTempMin.setText(getStringTemp(object.getMain().getTemp_min()));
            tvMain.setText(object.getWeather()[0].getMain());
            String iconURL = ICON_URL + object.getWeather()[0].getIcon() + ICON_EXT;
            DownloadImageTask downloadImageTask = new DownloadImageTask();
            downloadImageTask.execute(iconURL);
        }

        @Override
        public void error(int err) {
            AppLogger.LogCut(err + "");
        }

        @Override
        public void exception(String e) {
            AppLogger.LogCut(e);
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mTimer.cancel();
    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap icon = null;
            try {
                icon = BitmapFactory.decodeStream(new URL(urldisplay).openConnection().getInputStream());
            } catch (Exception e) {
                e.printStackTrace();
            }
            return icon;
        }

        protected void onPostExecute(Bitmap result) {
            imvIcon.setImageBitmap(result);
        }
    }
}
