package esgi.meteoapp.weather;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class WeatherPrediction {
    public String date_txt;
    public JSONObject main;
    public JSONObject weather;
    public JSONObject clouds;
    public JSONObject wind;


    public WeatherPrediction(){
    }

    public WeatherPrediction(JSONObject data) throws JSONException {
        this.main = (JSONObject) data.get("main");
        this.wind = (JSONObject) data.get("wind");
        this.clouds = (JSONObject) data.get("wind");
        this.weather = (JSONObject) ((JSONArray) data.get("weather")).get(0);
    }
}
