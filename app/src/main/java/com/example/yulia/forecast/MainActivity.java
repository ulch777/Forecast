package com.example.yulia.forecast;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.yulia.forecast.communication.ObjectRetrieverListener;
import com.example.yulia.forecast.communication.VolleyClient;
import com.example.yulia.forecast.model.Forecast;
import com.example.yulia.forecast.utils.AppLogger;

import java.net.URL;

public class MainActivity extends AppCompatActivity {
    private static final String TEMP = "temp";
    private static final String TEMP_MIN = "temp_min";
    private static final String TEMP_MAX = "temp_max";
    private static final String MAIN = "main";
    private static final String CITY = "city";
    private static final String ICON = "icon";

    private ImageView imvIcon;
    private TextView tvCity;
    private TextView tvTemp;
    private TextView tvTempMax;
    private TextView tvTempMin;
    private TextView tvMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        imvIcon = (ImageView) findViewById(R.id.imvIcon);
        tvCity = (TextView) findViewById(R.id.tvCity);
        tvTemp = (TextView) findViewById(R.id.tvTemp);
        tvTempMax = (TextView) findViewById(R.id.tvTempMax);
        tvTempMin = (TextView) findViewById(R.id.tvTempMin);
        tvMain = (TextView) findViewById(R.id.tvMain);
        if (savedInstanceState != null) {
            tvCity.setText(savedInstanceState.getString(CITY));
            tvTemp.setText(savedInstanceState.getString(TEMP));
            tvTempMax.setText(savedInstanceState.getString(TEMP_MAX));
            tvTempMin.setText(savedInstanceState.getString(TEMP_MIN));
            tvMain.setText(savedInstanceState.getString(MAIN));
            imvIcon.setImageBitmap((Bitmap) savedInstanceState.getParcelable(ICON));
        } else
            VolleyClient.getInstance().getForecast("Dnipropetrovsk", getForecastListener);
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
        switch (item.getItemId()){
            case R.id.action_dnipro :
                VolleyClient.getInstance().getForecast(getResources().getString(R.string.action_dnipro), getForecastListener);
                break;
            case R.id.action_zaporizhzhya :
                VolleyClient.getInstance().getForecast(getResources().getString(R.string.action_zaporizhzhya), getForecastListener);
                break;
            case R.id.action_kharkiv :
                VolleyClient.getInstance().getForecast(getResources().getString(R.string.action_kharkiv), getForecastListener);
                break;
        }
        return super.onOptionsItemSelected(item);
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
            String iconURL = "http://openweathermap.org/img/w/" + object.getWeather()[0].getIcon() + ".png";
            DownloadImageTask downloadImageTask = new DownloadImageTask();
            downloadImageTask.execute(iconURL);
        }

        @Override
        public void error(int err) {
            AppLogger.LogCut(err + "");
        }

        @Override
        public void exception(String e) {
            AppLogger.LogCut(e.toString());
        }
    };

    private String getStringTemp(String temp) {
        String t = "";
        int tmp = (int) Double.parseDouble(temp);
        if (tmp > 0) {
            t += "+" + tmp;
        } else t = tmp + "";
        return t += "\u00b0";

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
