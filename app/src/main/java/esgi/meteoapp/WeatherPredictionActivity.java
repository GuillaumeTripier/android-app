package esgi.meteoapp;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import esgi.meteoapp.city.CityContent;
import esgi.meteoapp.weather.WeatherPredictionContent;

public class WeatherPredictionActivity extends AppCompatActivity implements WeatherPredictionFragment.OnListFragmentInteractionListener {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_prediction);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }
    @Override
    public void onListFragmentInteraction(WeatherPredictionContent.WeatherPrediction item) {
        finish();
    }
}
