package com.recoveryrecord.surveyandroid.example;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.recoveryrecord.surveyandroid.example.model.NewsModelOne;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

//import com.squareup.picasso.Picasso;

public class NewsRecycleViewAdapterOne extends RecyclerView.Adapter<NewsRecycleViewAdapterOne.ViewHolder> {
    private ArrayList<NewsModelOne> dataModelArrayList;
    private Context context;

    // constructor class for our Adapter
    public NewsRecycleViewAdapterOne(ArrayList<NewsModelOne> dataModalArrayList, Context context) {
        this.dataModelArrayList = dataModalArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public NewsRecycleViewAdapterOne.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // passing our layout file for displaying our card item
        return new NewsRecycleViewAdapterOne.ViewHolder(LayoutInflater.from(context).inflate(R.layout.rv_item, parent, false));
    }


    @Override
    public void onBindViewHolder(@NonNull NewsRecycleViewAdapterOne.ViewHolder holder, int position) {
        // setting data to our views in Recycler view items.
        NewsModelOne model = dataModelArrayList.get(position);
        holder.newsTitle.setText(model.getTitle());
        Date date = model.getPubdate().toDate();
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
        List<String> my_tt = new ArrayList<String>(Arrays.asList(formatter.format(date).split(" ")));
        holder.newsPubTime.setText(String.format("%s %s", my_tt.get(0), my_tt.get(2)));
//        holder.newsPubTime.setText("123");
        holder.newsMedia.setText(model.getMedia());

        // we are using Picasso to load images
        // from URL inside our image view.
//        Picasso.get().load(modal.getImgUrl()).into(holder.courseIV);
//        holder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // setting on click listener
//                // for our items of recycler items.
//                Toast.makeText(context, "Clicked item is " + modal.getName(), Toast.LENGTH_SHORT).show();
//            }
//        });
    }

    @Override
    public int getItemCount() {
        // returning the size of array list.
        return dataModelArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        // creating variables for our
        // views of recycler items.
        private TextView newsTitle, newsPubTime, newsMedia;
//        private ImageView courseIV;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // initializing the views of recycler views.
            newsTitle = itemView.findViewById(R.id.text_view_title);
            newsPubTime = itemView.findViewById(R.id.text_view_pubtime);
            newsMedia = itemView.findViewById(R.id.text_view_media);
//            courseIV = itemView.findViewById(R.id.idIVimage);
//            itemView.setCardBackgroundColor(Color.parseColor("#e0efff"));
            // 點擊項目時
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    Toast.makeText(view.getContext(), "click " +getAdapterPosition(),Toast.LENGTH_SHORT).show();
                    NewsModelOne model = dataModelArrayList.get(getAdapterPosition());
                    Intent intent = new Intent();
                    intent.setClass(context, NewsModuleActivity.class);
                    intent.putExtra("trigger_from", "self_trigger");
                    intent.putExtra("news_id", model.getId());
                    intent.putExtra("media", model.getMedia());

                    Log.d("log: onClick", model.getTitle());
//                    Toast.makeText(view.getContext(), "click " +model.getTitle(),Toast.LENGTH_SHORT).show();
                    context.startActivity(intent);
                    //MainActivity.this.finish();
                }
            });

        }
    }
}