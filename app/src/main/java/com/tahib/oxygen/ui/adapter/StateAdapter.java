package com.tahib.oxygen.ui.adapter;

import java.util.List;
import java.util.Locale;

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

import com.tahib.oxygen.R;
import com.tahib.oxygen.data.State;
import com.tahib.oxygen.ui.DetailActivity;
import com.tahib.oxygen.ui.Tools;

public class StateAdapter extends RecyclerView.Adapter<StateAdapter.ViewHolder> {

    private List<State> valuesStates;
    Context ctx;

    String api;
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

    public StateAdapter(List<State> myDataset,Context context,String api) {
        valuesStates = myDataset;
        ctx=context;
        this.api=api;


    }

    // Create new views (invoked by the layout manager)
    @Override
    public StateAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.new_row, parent, false);
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
      Locale current = ctx.getResources().getConfiguration().locale;

        if (current.getDisplayName().contains("English")){
            holder.txtHeader.setText(content.getState());
            holder.txtFooter.setText("Country: " + retrieveCountry(holder.layout.getContext()));


        }else{
            holder.txtHeader.setText(content.getShar());
            holder.txtFooter.setText("کشور:  افغانستان" );


        }

        String img="https://tagcha-katab.ir/PoraanictProject/airqual/ProvencesImage/"+content.getState()+".jpg";

        Tools.displayImageOriginal(ctx, holder.image,img );

        holder.layout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(v.getContext(), DetailActivity.class);
                intent.putExtra("state", content.getState());
                intent.putExtra("per_city", content.getShar());
                intent.putExtra("lat", content.getLat());
                intent.putExtra("lon", content.getLon());
                intent.putExtra("api", api);
                v.getContext().startActivity(intent);

                notifyDataSetChanged();//notifier quand un élément est modifié
            }
        });
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
