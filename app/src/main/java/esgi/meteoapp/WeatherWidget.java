package esgi.meteoapp;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.res.Resources;
import android.widget.RemoteViews;

import org.json.JSONException;
import org.json.JSONObject;

import esgi.meteoapp.services.AsyncResponse;
import esgi.meteoapp.services.MeteoApiService;
import esgi.meteoapp.weather.WeatherPredictionContent;

/**
 * Implementation of App Widget functionality.
 * App Widget Configuration implemented in {@link WeatherWidgetConfigureActivity WeatherWidgetConfigureActivity}
 */
public class WeatherWidget extends AppWidgetProvider {

    static void updateAppWidget(final Context context, final AppWidgetManager appWidgetManager,
                                final int appWidgetId) {

        final CharSequence widgetText = WeatherWidgetConfigureActivity.loadPref(context, appWidgetId);
        // Construct the RemoteViews object
        final RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.weather_widget);

        final String[] temperature = new String[1];
        final String hour;
        final int[] resourceId = new int[1];
        String[] params = {widgetText.toString()};

        MeteoApiService asyncTask =new MeteoApiService(new AsyncResponse() {

            @Override
            public void processFinish(JSONObject data) {
                try {
                    String temperature = "??°C";
                    int resourceId = R.drawable.ic_not_connected;;
                    if(data != null) {
                        JSONObject data0 = (JSONObject) data.getJSONArray("list").get(0);
                        WeatherPredictionContent.WeatherPrediction weatherPrediction = new WeatherPredictionContent.WeatherPrediction(data0);
                        temperature = weatherPrediction.hour_txt + "h " + weatherPrediction.main.get("temp").toString().split("\\.")[0] + "°C";
                        Resources resources = context.getResources();
                        resourceId = resources.getIdentifier("ic_" + weatherPrediction.weather.get("icon").toString(), "drawable",
                                context.getPackageName());
                    }
                    views.setTextViewText(R.id.appwidget_text, widgetText);
                    views.setTextViewText(R.id.widgetTemperature, temperature);
                    views.setImageViewResource(R.id.widgetImage, resourceId);
                    appWidgetManager.updateAppWidget(appWidgetId, views);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
        asyncTask.execute(params);

        views.setTextViewText(R.id.appwidget_text, widgetText);
        views.setTextViewText(R.id.widgetTemperature, " ");
        views.setImageViewResource(R.id.widgetImage, R.drawable.ic_waiting);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);


        /*Intent intent = new Intent(context, WeatherWidgetConfigureActivity.class);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        views.setOnClickPendingIntent(R.id.appwidget_text, pendingIntent);
        views.setOnClickPendingIntent(R.id.widgetTemperature, pendingIntent);
        views.setOnClickPendingIntent(R.id.widgetImage, pendingIntent);*/
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        // When the user deletes the widget, delete the preference associated with it.
        for (int appWidgetId : appWidgetIds) {
            WeatherWidgetConfigureActivity.deletePref(context, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

