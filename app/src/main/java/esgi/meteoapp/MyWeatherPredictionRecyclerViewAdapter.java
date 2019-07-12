package esgi.meteoapp;

import android.content.Context;
import android.content.res.Resources;
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

/**
 * {@link RecyclerView.Adapter} that can display a {@link WeatherPrediction} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MyWeatherPredictionRecyclerViewAdapter extends RecyclerView.Adapter<MyWeatherPredictionRecyclerViewAdapter.ViewHolder> {

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

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mIdView.setText(mValues.get(position).date_txt);
        try {
            holder.mContentView.setText(mValues.get(position).main.get("temp").toString().split("\\.")[0] + "°C");

            Resources resources = context.getResources();
            int resourceId = resources.getIdentifier("ic_" + mValues.get(position).weather.get("icon").toString(), "drawable",
                    context.getPackageName());
            holder.mIconView.setImageDrawable(resources.getDrawable(resourceId));
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
        public final TextView mIdView;
        public final TextView mContentView;
        public final ImageView mIconView;
        public WeatherPrediction mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mIdView = (TextView) view.findViewById(R.id.item_number);
            mContentView = (TextView) view.findViewById(R.id.content);
            mIconView = (ImageView) view.findViewById(R.id.weatherPredictionIcon);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}
