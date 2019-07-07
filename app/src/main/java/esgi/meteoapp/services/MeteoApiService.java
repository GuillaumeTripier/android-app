package esgi.meteoapp.services;

import android.os.AsyncTask;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MeteoApiService extends AsyncTask<String, String, JSONObject> {
    public AsyncResponse delegate = null;//Call back interface

    public MeteoApiService(AsyncResponse asyncResponse) {
        delegate = asyncResponse;//Assigning call back interfacethrough constructor
    }

    @Override
    protected void onPostExecute(JSONObject jsonObject) {
        delegate.processFinish(jsonObject);
    }

    protected JSONObject doInBackground(String[] parameters) {

        try {
            URL url = new URL("https://api.openweathermap.org/data/2.5/forecast?APPID=468576b059a8ebbce9f5aa780cc259fc&lang=fr&units=metric&q=" + parameters[0] + ",fr");

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setReadTimeout(10000);

            conn.connect();

            int responseCode = conn.getResponseCode();

            if (responseCode != 200)
                throw new Exception("Could not get meteo data from API");

            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line = null;
            StringBuilder stringBuilder = new StringBuilder();

            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line).append("\n");
            }

            return new JSONObject(stringBuilder.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
