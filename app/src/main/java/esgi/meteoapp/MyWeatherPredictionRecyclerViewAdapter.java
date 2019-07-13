package esgi.meteoapp;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.ColorMatrixColorFilter;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONException;

import esgi.meteoapp.WeatherPredictionFragment.OnListFragmentInteractionListener;
import esgi.meteoapp.weather.WeatherPredictionContent.WeatherPrediction;

import java.util.List;

public class MyWeatherPredictionRecyclerViewAdapter extends RecyclerView.Adapter<MyWeatherPredictionRecyclerViewAdapter.ViewHolder> {

    public static final int DARK_BACKGROUND = 0xFF888888;
    private final List<WeatherPrediction> mValues;
    private final OnListFragmentInteractionListener mListener;
    private Context context;

    public MyWeatherPredictionRecyclerViewAdapter(List<WeatherPrediction> items, OnListFragmentInteractionListener listener, Context context) {
        mValues = items;
        mListener = listener;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_weather_prediction, parent, false);
        return new ViewHolder(view);
    }

    private static final float[] NEGATIVE = {
            -1.0f,     0,     0,    0, 255, // red
            0, -1.0f,     0,    0, 255, // green
            0,     0, -1.0f,    0, 255, // blue
            0,     0,     0, 1.0f,   0  // alpha
    };

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        String hour = mValues.get(position).hour_txt;
        String date = mValues.get(position).date_txt;
        try {
            holder.mHourView.setText("  " + hour + "h");
            holder.mDateView.setText(" " + date.split("-")[2] + "/" + date.split("-")[1]);
            holder.mTemperatureView.setText(mValues.get(position).main.get("temp").toString().split("\\.")[0] + "°C");
            holder.mDescriptionView.setText(mValues.get(position).weather.get("description").toString());
            Resources resources = context.getResources();
            int resourceId = resources.getIdentifier("ic_" + mValues.get(position).weather.get("icon").toString(), "drawable",
                    context.getPackageName());
            holder.mIconView.setImageDrawable(resources.getDrawable(resourceId));
            if(Integer.parseInt(hour) > 20 || Integer.parseInt(hour) < 6){
                holder.mIconView.setColorFilter(new ColorMatrixColorFilter(NEGATIVE));
                //holder.mIconView.setBackgroundColor(DARK_BACKGROUND);
                holder.mTemperatureView.setTextColor(0xFFFFFFFF);
                //holder.mTemperatureView.setBackgroundColor(DARK_BACKGROUND);
                holder.mHourView.setTextColor(0xFFFFFFFF);
                //holder.mHourView.setBackgroundColor(DARK_BACKGROUND);
                holder.mDateView.setTextColor(0xFFFFFFFF);
                //holder.mDateView.setBackgroundColor(DARK_BACKGROUND);
                holder.mDescriptionView.setTextColor(0xFFFFFFFF);
                //holder.mDescriptionView.setBackgroundColor(DARK_BACKGROUND);
                holder.itemView.setBackgroundColor(DARK_BACKGROUND);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.mItem);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mHourView;
        public final TextView mDateView;
        public final TextView mTemperatureView;
        public final TextView mDescriptionView;
        public final ImageView mIconView;
        public WeatherPrediction mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mHourView = (TextView) view.findViewById(R.id.item_number);
            mTemperatureView = (TextView) view.findViewById(R.id.content);
            mIconView = (ImageView) view.findViewById(R.id.weatherPredictionIcon);
            mDescriptionView = (TextView) view.findViewById(R.id.details);
            mDateView = (TextView) view.findViewById(R.id.dateDay);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mTemperatureView.getText() + "'";
        }
    }
}
