package esgi.meteoapp;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import esgi.meteoapp.FavouriteFragment.OnListFragmentInteractionListener;
import esgi.meteoapp.favourite.FavouriteContent.FavouriteItem;

public class MyFavouriteRecyclerViewAdapter extends RecyclerView.Adapter<MyFavouriteRecyclerViewAdapter.ViewHolder> implements EventListener<QuerySnapshot> {

    private final List<FavouriteItem> mValues;
    private final OnListFragmentInteractionListener mListener;
    private Query query;
    private ListenerRegistration registration;

    public MyFavouriteRecyclerViewAdapter(Query query, OnListFragmentInteractionListener mListener) {
        this.mValues = new ArrayList<>();
        this.mListener = mListener;
        this.query = query;
    }

    public void startListening(){
        if(this.query != null && this.registration == null){
            this.registration = this.query.addSnapshotListener(this);
        }
    }

    public void stopListening(){
        if(this.registration != null){
            this.registration.remove();
            this.registration = null;
        }
        this.mValues.clear();
        // **************** synchronisation
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_favourite, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mCityView.setText(mValues.get(position).cityId);

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

    @Override
    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
        for (DocumentChange dc : queryDocumentSnapshots.getDocumentChanges()) {
            switch(dc.getType()){
                case ADDED:
                    this.mValues.add(dc.getNewIndex(), dc.getDocument().toObject(FavouriteItem.class));
                    notifyItemInserted(dc.getNewIndex());
                    break;
                case MODIFIED:
                    if(dc.getOldIndex() == dc.getNewIndex()){
                        this.mValues.set(dc.getNewIndex(), dc.getDocument().toObject(FavouriteItem.class));
                        notifyItemChanged(dc.getNewIndex());
                    }else{
                        this.mValues.remove(dc.getOldIndex());
                        this.mValues.add(dc.getNewIndex(), dc.getDocument().toObject(FavouriteItem.class));
                        notifyItemMoved(dc.getOldIndex(), dc.getNewIndex());
                    }
                    break;
                case REMOVED:
                    this.mValues.remove(dc.getOldIndex());
                    notifyItemRemoved(dc.getOldIndex());
                    break;
            }
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mCityView;
        public FavouriteItem mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mCityView = (TextView) view.findViewById(R.id.content);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mCityView.getText() + "'";
        }
    }
}
