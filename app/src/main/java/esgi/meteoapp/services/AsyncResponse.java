package esgi.meteoapp.services;

import org.json.JSONObject;

public interface AsyncResponse {
    void processFinish(JSONObject output);
}
