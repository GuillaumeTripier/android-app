package esgi.meteoapp;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

import esgi.meteoapp.services.AsyncResponse;
import esgi.meteoapp.services.MeteoApiService;
import esgi.meteoapp.weather.WeatherPredictionContent.WeatherPrediction;

public class WeatherPredictionFragment extends Fragment {

    private static final String ARG_COLUMN_COUNT = "column-count";
    private int mColumnCount = 1;
    private OnListFragmentInteractionListener mListener;
    public static final String MY_CACHE_TXT = "/my_cache.txt";

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public WeatherPredictionFragment() {
    }

    public static WeatherPredictionFragment newInstance(int columnCount) {
        WeatherPredictionFragment fragment = new WeatherPredictionFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_weather_prediction_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            final Context context = view.getContext();
            final RecyclerView recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            MeteoApiService asyncTask =new MeteoApiService(new AsyncResponse() {

                @Override
                public void processFinish(JSONObject data) {
                    if(data != null) {
                        MyWeatherPredictionRecyclerViewAdapter adapter = null;
                        try {
                            adapter = new MyWeatherPredictionRecyclerViewAdapter(WeatherPrediction.getWeatherPredictionList(data.getJSONArray("list")), mListener, context);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        recyclerView.setAdapter(adapter);
                    }

                }

            });

            File cacheDir = getActivity().getCacheDir();
            String filePath = cacheDir.getPath() + MY_CACHE_TXT;
            File cacheFile = new File(filePath);
            StringBuilder sb = new StringBuilder();
            if(cacheFile.exists()){
                try {
                    FileInputStream fileInputStream = new FileInputStream(cacheFile);
                    BufferedReader br = new BufferedReader(new InputStreamReader(fileInputStream));
                    String line;
                    while((line = br.readLine()) != null){
                        sb.append(line).append("\n");
                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            asyncTask.execute(sb.toString());
        }
        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnListFragmentInteractionListener {
        void onListFragmentInteraction(WeatherPrediction item);
    }
}
