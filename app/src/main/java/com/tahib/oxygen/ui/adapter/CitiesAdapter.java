package com.tahib.oxygen.ui.adapter;

import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tahib.oxygen.R;
import com.tahib.oxygen.data.City;
import com.tahib.oxygen.ui.DetailActivity;

public class CitiesAdapter extends RecyclerView.Adapter<CitiesAdapter.ViewHolder> {

    private List<City> values;
    private String stateofdataset;
    private String countryofdataset;

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

    public CitiesAdapter(List<City> myDataset,String state,String country) {
        values = myDataset;
        stateofdataset = state;
        countryofdataset = country;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public CitiesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
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
        final City content = values.get(position);
        holder.txtHeader.setText(content.getCity());
        holder.layout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

            Intent intent = new Intent(v.getContext(), DetailActivity.class);
            Bundle extras = new Bundle();
            extras.putString("city", content.getCity());
            extras.putString("state", stateofdataset);
            extras.putString("country", countryofdataset);
            intent.putExtras(extras);
            v.getContext().startActivity(intent);

            notifyDataSetChanged();//notifier quand un élément est supprimé
            }
        });

        holder.txtFooter.setText("State: " + stateofdataset);
    }


    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return values.size();
    }

}

