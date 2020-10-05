package com.example.edukit;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MySavedTestAdapter extends RecyclerView.Adapter<MySavedTestAdapter.MyViewHolder> {
    List<SavedTestModel> mList;
    DatabaseReference pubRef, testRef;
    Context context;
    private String tTitle, tTime, tFM, tNM, amount, q_set_id;

    public MySavedTestAdapter(List<SavedTestModel> mList, Context context) {
        this.mList = mList;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_saved_test_row, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        //Load Institute Details
        pubRef = FirebaseDatabase.getInstance().getReference().child("PUBLISHER").child("PUBLISHER_INFO").child(mList.get(position).getPubId());
        pubRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Toast.makeText(context,String.valueOf(dataSnapshot.child("Name").getValue()), Toast.LENGTH_SHORT).show();
                holder.tPublisher.setText(String.valueOf(dataSnapshot.child("Name").getValue()));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        //Load Test Details
        testRef = FirebaseDatabase.getInstance().getReference().child("MOCK_TEST").child("CourseList").child(mList.get(position).getTestId());
        testRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //Toast.makeText(context,String.valueOf(dataSnapshot.child("Name").getValue()), Toast.LENGTH_SHORT).show();
                holder.tTitle.setText(String.valueOf(dataSnapshot.child("Title").getValue()));
                if (String.valueOf(dataSnapshot.child("Language").getValue()).equals("1")) {
                    holder.tLanguage.setText("Bengali");
                } else if (String.valueOf(dataSnapshot.child("Language").getValue()).equals("2")) {
                    holder.tLanguage.setText("English");
                }
                Picasso.with(context).load(String.valueOf(dataSnapshot.child("Thumbnail").getValue())).into(
                        holder.tThumbnail);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        holder.tDateTime.setText(mList.get(position).getDate() + "  :: " + mList.get(position).getTime());
        holder.btnAttempt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startTest(position);
            }
        });
    }

    private void startTest(int position) {

        Intent intent = new Intent(context, MockPreviewActivity.class);
        intent.putExtra("CourseID", String.valueOf(mList.get(position).getTestId()));
        intent.putExtra("PublisherID", String.valueOf(mList.get(position).getPubId()));
        context.startActivity(intent);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        LinearLayout btnAttempt;
        private ImageView tThumbnail;
        private TextView tTitle, tPublisher, tDateTime, tLanguage;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tPublisher = itemView.findViewById(R.id.txtInstitute);
            tTitle = itemView.findViewById(R.id.txtTitle);
            tDateTime = itemView.findViewById(R.id.txtDateTime);
            tLanguage = itemView.findViewById(R.id.txtLanguage);
            tThumbnail = itemView.findViewById(R.id.txtTumbnail);
            btnAttempt = itemView.findViewById(R.id.btnAttempt);

        }


    }
}
