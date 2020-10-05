package com.example.edukit;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class CourseListAdapter extends RecyclerView.Adapter<CourseListAdapter.MyViewHolder> {
    private Context context;
    private List<CourseDataModel> courseList;

    public CourseListAdapter(Context context, List<CourseDataModel> courseList) {
        this.context = context;
        this.courseList = courseList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.course_list_row, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        Picasso.with(context).load(courseList.get(position).getThumbnail_url()).into(holder.thumbNail);
        holder.title.setText(courseList.get(position).getTitle());
        holder.publishedOn.setText(courseList.get(position).getDate_of_publish());
        holder.publishedBy.setText(courseList.get(position).getPublished_by());
        if (Integer.valueOf(courseList.get(position).getPrice()) == 0) {
            holder.price.setText("FREE");
        } else {
            holder.price.setText("₹" + courseList.get(position).getPrice());
        }
        holder.rating.setRating(Float.valueOf(courseList.get(position).getRating()));
        if (courseList.get(position).getLanguage().equals("1")) {
            holder.language.setText("Bengali");
        } else if (courseList.get(position).getLanguage().equals("2")) {
            holder.language.setText("Hindi");
        } else if (courseList.get(position).getLanguage().equals("3")) {
            holder.language.setText("English");
        }


        holder.cardRow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, MockPreviewActivity.class);
                intent.putExtra("CourseID", String.valueOf(courseList.get(position).getCourseID()));
                intent.putExtra("PublisherID", String.valueOf(courseList.get(position).getPublisherID()));
                context.startActivity(intent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return courseList.size();
    }


    class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView title, publishedOn, publishedBy, price, language;
        private RatingBar rating;
        private ImageView thumbNail;
        private CardView cardRow;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.txtCourseTitle);
            language = itemView.findViewById(R.id.txtLanguage);
            rating = itemView.findViewById(R.id.txtRating);
            publishedOn = itemView.findViewById(R.id.txtPublisheOn);
            publishedBy = itemView.findViewById(R.id.txtPublishedBy);
            price = itemView.findViewById(R.id.txtPrice);
            thumbNail = itemView.findViewById(R.id.txtThumbNail);
            cardRow = itemView.findViewById(R.id.cardRow);
        }
    }
}
