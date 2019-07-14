package esgi.meteoapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import esgi.meteoapp.favourite.FavouriteContent.FavouriteItem;

import static android.content.Context.MODE_PRIVATE;
import static esgi.meteoapp.MainActivity.MY_PREF;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class FavouriteFragment extends Fragment {

    private int mColumnCount = 1;
    private OnListFragmentInteractionListener mListener;
    private MyFavouriteRecyclerViewAdapter adapter;
    private SharedPreferences sharedPreferences;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public FavouriteFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favourite_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }

            sharedPreferences = context.getSharedPreferences(MY_PREF, MODE_PRIVATE);

            String val;
            try {
                if (FirebaseAuth.getInstance().getCurrentUser().getEmail() != null) {
                    val = FirebaseAuth.getInstance().getCurrentUser().getEmail();
                } else {
                    val = "weatherapp.esgi@gmail.com";
                }
            }catch(Exception e){
                val = "notAuthenticatedUser";
            }

            FirebaseFirestore db = FirebaseFirestore.getInstance();
            Query query = db.collection("users").document(val).collection("favouriteCities").orderBy("cityId");
            //Query query = db.collection("favouriteCities").orderBy("cityId");
            adapter = new MyFavouriteRecyclerViewAdapter(query, mListener);
            recyclerView.setAdapter(adapter);
        }
        return view;
    }

    @Override
    public void onStart(){
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
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
        void onListFragmentInteraction(FavouriteItem item);
    }
}
