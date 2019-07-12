package esgi.meteoapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;

import esgi.meteoapp.favourite.FavouriteContent;
import esgi.meteoapp.services.AsyncResponse;
import esgi.meteoapp.services.MeteoApiService;
import esgi.meteoapp.weather.WeatherPrediction;
import esgi.meteoapp.weather.WeatherPredictionContent;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    public static final String MY_PREF = "my_pref";
    public static final String MY_PREF_KEY = "selected_city";
    private SharedPreferences sharedPreferences;
    private String email;
    private List<WeatherPredictionContent.WeatherPrediction> weatherPredictionList;

    @Override
    protected void onResume() {
        super.onResume();
        final String val = sharedPreferences.getString(MY_PREF_KEY, "Nothing");
        final TextView tVCity = findViewById(R.id.tVCity);
        final TextView tVTemperature = findViewById(R.id.tVTemperature);
        final TextView tVDescription = findViewById(R.id.tVDescription);
        final TextView tVWindSpeed = findViewById(R.id.tVWindSpeed);
        final TextView tVHumidity = findViewById(R.id.tVHumidity);
        final TextView tVDateMaj = findViewById(R.id.tVDateMaj);
        final ImageView iVWindSpeed = findViewById(R.id.iVWindSpeed);
        final ImageView iVHumidity = findViewById(R.id.iVHumidity);
        final ToggleButton toggleButton = findViewById(R.id.addToFavourite);
        ImageView weatherIcon = findViewById(R.id.weatherIcon);
        if(val.equals("Nothing")){
            tVCity.setText(getString(R.string.choose_a_city));
            tVDateMaj.setText("??/?? ??:?? MAJ");
            tVDateMaj.setVisibility(View.INVISIBLE);
            tVTemperature.setText("-00°C");
            tVTemperature.setVisibility(View.INVISIBLE);
            tVWindSpeed.setText(" 000km/h");
            tVWindSpeed.setVisibility(View.INVISIBLE);
            tVHumidity.setText("0");
            tVHumidity.setVisibility(View.INVISIBLE);
            tVDescription.setText("Nothing");
            tVDescription.setVisibility(View.INVISIBLE);
            toggleButton.setVisibility(View.INVISIBLE);
            iVWindSpeed.setVisibility(View.INVISIBLE);
            iVHumidity.setVisibility(View.INVISIBLE);
            Resources resources = this.getResources();
            final int resourceId = resources.getIdentifier("logo", "drawable",
                    this.getPackageName());
            weatherIcon.setImageDrawable(resources.getDrawable(resourceId));
        }else{
            final MainActivity mainActivity = this;
            toggleButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(toggleButton.isChecked()){
                        FavouriteContent.FavouriteItem favouriteItem = new FavouriteContent.FavouriteItem(val);
                        FirebaseFirestore db = FirebaseFirestore.getInstance();
                        db.collection("users").document(mainActivity.email).collection("favouriteCities").document(val).set(favouriteItem).addOnSuccessListener(new OnSuccessListener<Object>() {
                            @Override
                            public void onSuccess(Object o) {
                                Toast.makeText(mainActivity, getString(R.string.city_added), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }else{
                        FirebaseFirestore db = FirebaseFirestore.getInstance();
                        db.collection("users").document(mainActivity.email).collection("favouriteCities").document(val).delete().addOnSuccessListener(new OnSuccessListener<Object>() {
                            @Override
                            public void onSuccess(Object o) {
                                Toast.makeText(mainActivity, getString(R.string.city_removed), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            });
            tVCity.setText(val);
            String[] params = {val};

            MeteoApiService asyncTask =new MeteoApiService(new AsyncResponse() {

                @Override
                public void processFinish(JSONObject data) {
                    try {
                        boolean isConnected;
                        ImageView weatherIcon = findViewById(R.id.weatherIcon);
                        Resources resources = mainActivity.getResources();
                        if(data != null) {
                            JSONObject data0 = (JSONObject) data.getJSONArray("list").get(0);
                            Log.i("API", data0.toString());
                            WeatherPrediction weatherPrediction = new WeatherPrediction(data0);
                            tVTemperature.setText(weatherPrediction.date_txt + "h " + weatherPrediction.main.get("temp").toString().split("\\.")[0] + "°C");
                            tVDescription.setText(weatherPrediction.weather.get("description").toString());
                            tVWindSpeed.setText(" " + weatherPrediction.wind.get("speed").toString() + "km/h");
                            tVHumidity.setText(" " + weatherPrediction.main.get("humidity").toString());

                            final int resourceId = resources.getIdentifier("ic_" + weatherPrediction.weather.get("icon").toString(), "drawable",
                                    mainActivity.getPackageName());
                            weatherIcon.setImageDrawable(resources.getDrawable(resourceId));
                            weatherIcon.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent intent = new Intent(mainActivity, WeatherPredictionActivity.class);
                                    startActivity(intent);
                                }
                            });
                            tVDateMaj.setVisibility(View.VISIBLE);
                            tVTemperature.setVisibility(View.VISIBLE);
                            tVWindSpeed.setVisibility(View.VISIBLE);
                            tVHumidity.setVisibility(View.VISIBLE);
                            iVWindSpeed.setVisibility(View.VISIBLE);
                            iVHumidity.setVisibility(View.VISIBLE);
                            isConnected = true;
                        }else{
                            final int resourceId = resources.getIdentifier("ic_not_connected", "drawable",
                                    mainActivity.getPackageName());
                            weatherIcon.setImageDrawable(resources.getDrawable(resourceId));

                            tVCity.setText(getString(R.string.network_error));
                            tVDateMaj.setVisibility(View.INVISIBLE);
                            tVTemperature.setVisibility(View.INVISIBLE);
                            tVDescription.setText(getString(R.string.weather_data_unreachable));
                            iVWindSpeed.setVisibility(View.INVISIBLE);
                            iVHumidity.setVisibility(View.INVISIBLE);
                            isConnected = false;
                        }

                        favouriteIconManager(val, isConnected);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            });
            asyncTask.execute(params);







            //AsyncTask<String, String, JSONObject> data  = new MeteoApiService().execute(params);

            Date date = new Date();
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM HH:mm");
            tVDateMaj.setText(formatter.format(date) + " MAJ");

            tVDateMaj.setVisibility(View.INVISIBLE);
            tVTemperature.setVisibility(View.INVISIBLE);
            tVWindSpeed.setVisibility(View.INVISIBLE);
            tVHumidity.setVisibility(View.INVISIBLE);
            tVDescription.setText(getString(R.string.loading));
            iVWindSpeed.setVisibility(View.INVISIBLE);
            iVHumidity.setVisibility(View.INVISIBLE);
            toggleButton.setVisibility(View.INVISIBLE);

            tVDescription.setVisibility(View.VISIBLE);
            Resources resources = mainActivity.getResources();
            final int resourceId = resources.getIdentifier("ic_waiting", "drawable",
                    mainActivity.getPackageName());
            weatherIcon.setImageDrawable(resources.getDrawable(resourceId));
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        sharedPreferences = getSharedPreferences(MY_PREF, MODE_PRIVATE);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.search);
        final MainActivity mainActivity = this;
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {Log.i("ACTIVITY_START", "Start City Activity");

                Intent intent = new Intent(mainActivity, CityActivity.class);
                startActivity(intent);
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        this.email = FirebaseAuth.getInstance().getCurrentUser().getEmail();

        Log.i("EMAIL-" , this.email);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_clear) {
            sharedPreferences.edit().remove(MY_PREF_KEY).commit();
            Toast.makeText(this, getString(R.string.data_cleared), Toast.LENGTH_SHORT).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_favourite) {
            Log.i("ACTIVITY_START", "Start Favourite Activity");

            Intent intent = new Intent(this, FavouriteCityActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_settings) {
            Toast.makeText(this, "Settings Button", Toast.LENGTH_SHORT).show();

        } else if (id == R.id.nav_about) {
            Toast.makeText(this, "About Button", Toast.LENGTH_SHORT).show();

        } else if (id == R.id.nav_logout) {
            FirebaseAuth.getInstance().signOut();
            finish();

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void favouriteIconManager(String val, boolean isConnected){
        final ToggleButton toggleButton = findViewById(R.id.addToFavourite);

        if(isConnected) {
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("users").document(this.email).collection("favouriteCities").whereEqualTo("cityId", val).get()
                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            final boolean empty = queryDocumentSnapshots.getDocuments().isEmpty();
                            toggleButton.setChecked(!empty);
                            toggleButton.setVisibility(View.VISIBLE);
                        }
                    });
        }
    }

    public void reloadActivity(View view){
        onResume();
    }
}
