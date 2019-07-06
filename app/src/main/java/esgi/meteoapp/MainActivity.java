package esgi.meteoapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
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
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import esgi.meteoapp.favourite.FavouriteContent;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    public static final String MY_PREF = "my_pref";
    public static final String MY_PREF_KEY = "selected_city";
    private SharedPreferences sharedPreferences;

    @Override
    protected void onResume() {
        super.onResume();
        final String val = sharedPreferences.getString(MY_PREF_KEY, "Nothing");
        TextView textViewCity = findViewById(R.id.textViewCity);
        TextView textViewDateMaj = findViewById(R.id.textViewDateMaj);
        TextView textViewTemperature = findViewById(R.id.textViewTemperature);
        final ToggleButton toggleButton = findViewById(R.id.addToFavourite);
        toggleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(toggleButton.isChecked()){
                    FavouriteContent.FavouriteItem favouriteItem = new FavouriteContent.FavouriteItem(val, "1");
                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                    db.collection("favouriteCities").document(val).set(favouriteItem);
                }else{
                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                    db.collection("favouriteCities").document(val).delete();
                }
            }
        });
        if(val.equals("Nothing")){
            textViewCity.setText("Choose a City");
            textViewDateMaj.setText("??/?? ??:?? MAJ");
            textViewDateMaj.setVisibility(View.INVISIBLE);
            textViewTemperature.setText("-00°C");
            textViewTemperature.setVisibility(View.INVISIBLE);
            toggleButton.setVisibility(View.INVISIBLE);
        }else{
            textViewCity.setText(val);
            Date date = new Date();
            SimpleDateFormat formater = new SimpleDateFormat("dd/MM hh:mm");
            textViewDateMaj.setText(formater.format(date) + " MAJ");
            textViewDateMaj.setVisibility(View.VISIBLE);
            textViewTemperature.setText("29°C");
            textViewTemperature.setVisibility(View.VISIBLE);
            toggleButton.setVisibility(View.VISIBLE);
            toggleButton.setChecked(true);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        sharedPreferences = getSharedPreferences(MY_PREF, MODE_PRIVATE);
        sharedPreferences.edit().remove(MY_PREF_KEY).commit();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.search);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FavouriteContent.FavouriteItem favouriteItem = new FavouriteContent.FavouriteItem("v521", "1");
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                db.collection("favouriteCities").document("v521").set(favouriteItem);
                Snackbar.make(view, "Replace with search City action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
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
        if (id == R.id.action_settings) {
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
            Log.i("ACTIVITY_START", "Start Second Activity");

            Intent intent = new Intent(this, FavouriteCityActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_settings) {
            Toast.makeText(this, "Settings Button", Toast.LENGTH_SHORT).show();

        } else if (id == R.id.nav_about) {
            Toast.makeText(this, "About Button", Toast.LENGTH_SHORT).show();

        } else if (id == R.id.nav_logout) {
            Toast.makeText(this, "Logout Button", Toast.LENGTH_SHORT).show();

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


}
