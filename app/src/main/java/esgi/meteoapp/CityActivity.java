package esgi.meteoapp;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import esgi.meteoapp.city.CityContent;

public class CityActivity extends AppCompatActivity implements CityFragment.OnListFragmentInteractionListener {
    public static final String MY_PREF = "my_pref";
    public static final String MY_PREF_KEY = "selected_city";
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        sharedPreferences = getSharedPreferences(MY_PREF, MODE_PRIVATE);
    }

    @Override
    public void onListFragmentInteraction(CityContent.CityItem item){
        sharedPreferences.edit().putString(MY_PREF_KEY, item.toString()).apply();
        finish();
    }

}
