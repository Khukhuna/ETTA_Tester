package com.example.etta_tester;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ModeAdapter extends RecyclerView.Adapter<ModeAdapter.MyViewHolder> {
    private List<Mode> mModes = new ArrayList<>();
    private ArrayList<MyViewHolder> views = new ArrayList<>();

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        View view;

        ImageView layout;

        public MyViewHolder(View v) {
            super(v);
            view = v;
            layout = v.findViewById(R.id.mode_img);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public ModeAdapter(List<Mode> myModes) {
        mModes = myModes;
    }
    public ModeAdapter(){}

    // Create new views (invoked by the layout manager)
    @NonNull
    @Override
    public ModeAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                                       int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.mode_cell, parent, false);

        return new MyViewHolder(v);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        views.add(holder);
        holder.layout.setImageDrawable(mModes.get(position).img);
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chosenMode(position);
            }
        });

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mModes.size();
    }


    public void chosenMode(int index){
        MyViewHolder chosenHolder = views.get(index);
        // Change outline color to all recyclerview item
        for(MyViewHolder holder : views){
            holder.layout.setBackground(holder.view.getResources().getDrawable(R.drawable.mode));
        }
        chosenHolder.layout.setBackground(views.get(index).view.getResources().getDrawable(R.drawable.mode_actived));
    }



    public void changeData(List<Mode> mode){
        mModes = mode;
        notifyDataSetChanged();
    }

}
