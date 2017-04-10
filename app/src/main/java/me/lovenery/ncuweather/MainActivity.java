package me.lovenery.ncuweather;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.view.View;

import java.net.URL;

import me.lovenery.ncuweather.utilities.NetworkUtils;
import me.lovenery.ncuweather.utilities.JsonUtils;

public class MainActivity extends AppCompatActivity {

    private TextView weatherTextView;
    private TextView errorMessageTextView;
    private ProgressBar loadingIndicator;
    public Boolean isPending;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        weatherTextView = (TextView) findViewById(R.id.tv_weather_data);
        errorMessageTextView = (TextView) findViewById(R.id.tv_error_message_display);
        loadingIndicator = (ProgressBar) findViewById(R.id.pb_loading_indicator);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadWeatherData();
    }

    private void loadWeatherData() {
        isPending = true;
        weatherTextView.setText("");
        showWeatherDataView(); // show before AsyncTask
        new FetchWeatherTask().execute();
    }

    private void showWeatherDataView() {
        errorMessageTextView.setVisibility(View.INVISIBLE);
        weatherTextView.setVisibility(View.VISIBLE);
    }

    private void showErrorMessage() {
        weatherTextView.setVisibility(View.INVISIBLE);
        errorMessageTextView.setVisibility(View.VISIBLE);
    }

    public class FetchWeatherTask extends AsyncTask<String, Void, String[]> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loadingIndicator.setVisibility(View.VISIBLE);
        }

        @Override
        protected String[] doInBackground(String... params) {
            URL weatherRequestUrl = NetworkUtils.buildUrl();

            try {
                String jsonWeatherResponse = NetworkUtils.getResponseFromHttpUrl(weatherRequestUrl);
                String[] jsonWeatherData = JsonUtils.getWeatherStringsFromJson(MainActivity.this, jsonWeatherResponse);
                return jsonWeatherData;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String[] weatherData) {
            loadingIndicator.setVisibility(View.INVISIBLE);
            if (weatherData != null) {
                showWeatherDataView();
                for (String weatherString : weatherData) {
                    weatherTextView.append((weatherString) + "\n\n\n");
                }
            } else {
                showErrorMessage();
            }
            isPending = false;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_refresh && !isPending) {
            loadWeatherData();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
