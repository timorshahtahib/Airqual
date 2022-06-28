package com.tahib.airqual.ui.adapter;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import static android.content.Context.MODE_PRIVATE;

import com.tahib.airqual.R;
import com.tahib.airqual.data.State;
import com.tahib.airqual.ui.DetailActivity;

public class StateAdapter extends RecyclerView.Adapter<StateAdapter.ViewHolder> {

    private List<State> valuesStates;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView txtHeader;
        public TextView txtFooter;
        public ImageView image;
        public View layout;

        public ViewHolder(View v) {
            super(v);
            layout = v;
            image = (ImageView) v.findViewById(R.id.icon);
            txtHeader = (TextView) v.findViewById(R.id.firstLine);
            txtFooter = (TextView) v.findViewById(R.id.secondLine);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)

    public StateAdapter(List<State> myDataset) {
        valuesStates = myDataset;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public StateAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.row_layout, parent, false);
        // set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        final State content = valuesStates.get(position);
        holder.txtHeader.setText(content.getState());
        holder.layout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(v.getContext(), DetailActivity.class);
                intent.putExtra("state", content.getState());
                intent.putExtra("shar", content.getShar());
                intent.putExtra("lat", content.getLat());
                intent.putExtra("lon", content.getLon());
                v.getContext().startActivity(intent);

                notifyDataSetChanged();//notifier quand un élément est modifié
            }
        });
        holder.txtFooter.setText("Country: " + retrieveCountry(holder.layout.getContext()));
    }


    private String retrieveCountry(Context context){
        final String DEFAULT = "FRANCE";
        SharedPreferences sharedPreferences = context.getSharedPreferences("DataShared", MODE_PRIVATE);
        String country = sharedPreferences.getString("Country",DEFAULT);
        return country;
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return valuesStates.size();
    }

}
