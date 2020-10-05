package com.example.edukit;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class MyQuestionAdapter extends RecyclerView.Adapter {

    List<QuestionDataModel> list;
    Context context;
    DatabaseReference mRef = FirebaseDatabase.getInstance().getReference().child("PUBLISHER").child("PUBLISHER_INFO");

    public MyQuestionAdapter(List<QuestionDataModel> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public int getItemViewType(int position) {
        if (String.valueOf(list.get(position).getType()).equals("1")) {
            return 1;
        }
        return 2;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view;
        if (viewType == 1) {
            view = layoutInflater.inflate(R.layout.question_row_card, parent, false);
            return new NotesView1(view);
        } else
            view = layoutInflater.inflate(R.layout.notes_list_row, parent, false);
        return new NotesView2(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        if (String.valueOf(list.get(position).getType()).equals("1")) {
            final MyQuestionAdapter.NotesView1 holder1 = (MyQuestionAdapter.NotesView1) holder;
            holder1.tvTitle.setText(list.get(position).getTitle());
            if (list.get(position).getPrice().equals("0")) {
                holder1.tvPrice.setText("FREE");
            } else {
                holder1.tvPrice.setText("₹ " + list.get(position).getPrice());
            }
            holder1.tvLanguage.setText(list.get(position).getLanguage());

            mRef.child(list.get(position).getPublisherID()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    holder1.tvPublisher.setText(String.valueOf(dataSnapshot.child("Name").getValue()));

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
            holder1.btnNotes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String publisherID = list.get(position).getPublisherID();
                    String notesID = list.get(position).getNotesID();
                    String backTo = "1";
                    ((FragmentActivity) context).getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                            new NotesView(publisherID, notesID, backTo)).commit();
                    //Toast.makeText(context, String.valueOf(list.get(position).getPublisherID()), Toast.LENGTH_SHORT).show();
                }
            });


        } else if (String.valueOf(list.get(position).getType()).equals("2")) {
            final MyQuestionAdapter.NotesView2 holder2 = (MyQuestionAdapter.NotesView2) holder;
            holder2.tvTitle.setText(list.get(position).getTitle());
            if (list.get(position).getPrice().equals("0")) {
                holder2.tvPrice.setText("FREE");
            } else {
                holder2.tvPrice.setText("₹ " + list.get(position).getPrice());
            }
            holder2.tvLanguage.setText(list.get(position).getLanguage());

            mRef.child(list.get(position).getPublisherID()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    holder2.tvPublisher.setText(String.valueOf(dataSnapshot.child("Name").getValue()));
                    if (String.valueOf(dataSnapshot.child("Verified").getValue()).equals("1")) {
                        holder2.tvPublisherImage.setVisibility(View.VISIBLE);
                    } else {
                        holder2.tvPublisherImage.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
            holder2.btnNotes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String publisherID = list.get(position).getPublisherID();
                    String notesID = list.get(position).getNotesID();
                    String backTo = "2";
                    ((FragmentActivity) context).getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                            new NotesView(publisherID, notesID, backTo)).commit();
                    //Toast.makeText(context, String.valueOf(list.get(position).getPublisherID()), Toast.LENGTH_SHORT).show();
                }
            });

        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class NotesView1 extends RecyclerView.ViewHolder {
        LinearLayout btnNotes;
        TextView tvTitle, tvPrice, tvPublisher, tvLanguage;

        public NotesView1(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            tvPublisher = itemView.findViewById(R.id.tvPublisher);
            tvLanguage = itemView.findViewById(R.id.tvLanguage);
            btnNotes = itemView.findViewById(R.id.btnNotes);
        }
    }

    class NotesView2 extends RecyclerView.ViewHolder {
        LinearLayout btnNotes;
        TextView tvTitle, tvPrice, tvPublisher, tvLanguage;
        ImageView tvPublisherImage;

        public NotesView2(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            tvPublisher = itemView.findViewById(R.id.tvPublisher);
            tvLanguage = itemView.findViewById(R.id.tvLanguage);
            tvPublisherImage = itemView.findViewById(R.id.tvPublisherImage);
            btnNotes = itemView.findViewById(R.id.btnNotes);
        }
    }

}
