package esgi.meteoapp.weather;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WeatherPredictionContent {

    /**
     * An array of sample (dummy) items.
     */
    public static final List<WeatherPrediction> ITEMS = new ArrayList<WeatherPrediction>();

    /**
     * A map of sample (dummy) items, by ID.
     */
    public static final Map<String, WeatherPrediction> ITEM_MAP = new HashMap<String, WeatherPrediction>();

    private static void addItem(WeatherPrediction weatherPrediction) {
        ITEMS.add(weatherPrediction);
        ITEM_MAP.put(weatherPrediction.hour_txt, weatherPrediction);
    }

    private static String makeDetails(int position) {
        StringBuilder builder = new StringBuilder();
        builder.append("Details about Item: ").append(position);
        for (int i = 0; i < position; i++) {
            builder.append("\nMore details information here.");
        }
        return builder.toString();
    }

    /**
     * A dummy item representing a piece of content.
     */
    public static class WeatherPrediction {
        public String hour_txt;
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
            this.hour_txt = ((String) data.get("dt_txt")).split(" ")[1].split(":")[0];
            this.date_txt = ((String) data.get("dt_txt")).split(" ")[0];
        }

        public static List<WeatherPrediction> getWeatherPredictionList(JSONArray data) throws JSONException {
            List<WeatherPrediction> weatherPredictionList = new ArrayList<WeatherPrediction>();
            for(int i = 0; i < data.length(); i++){
                weatherPredictionList.add(new WeatherPrediction((JSONObject) data.get(i)));
            }
            return weatherPredictionList;
        }
    }
    /*public static class DummyItem {
        public final String id;
        public final String content;
        public final String details;

        public DummyItem(String id, String content, String details) {
            this.id = id;
            this.content = content;
            this.details = details;
        }

        @Override
        public String toString() {
            return content;
        }
    }*/
}
