package com.example.edukit;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

public class MySavedNotesAdapter extends RecyclerView.Adapter<MySavedNotesAdapter.MyViewHolder> {
    List<SavedNotesModel> mList;
    DatabaseReference pubRef, testRef;
    Context context;

    public MySavedNotesAdapter(List<SavedNotesModel> mList, Context context) {
        this.mList = mList;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_saved_notes_row, parent, false);
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
        testRef = FirebaseDatabase.getInstance().getReference().child("QUESTIONS").child("Question_Info").child(mList.get(position).getNotesId());
        testRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //Toast.makeText(context,String.valueOf(dataSnapshot.child("Name").getValue()), Toast.LENGTH_SHORT).show();
                holder.tTitle.setText(String.valueOf(dataSnapshot.child("Title").getValue()));
                holder.tLanguage.setText(String.valueOf(dataSnapshot.child("Language").getValue()));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        holder.tDateTime.setText(mList.get(position).getDate() + "  :: " + mList.get(position).getTime());
        holder.btnGetNotes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String publisherID = mList.get(position).getPubId();
                String notesID = mList.get(position).getNotesId();
                String backTo = "3";
                ((FragmentActivity) context).getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new NotesView(publisherID, notesID, backTo)).commit();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        LinearLayout btnGetNotes;
        private TextView tTitle, tPublisher, tDateTime, tLanguage;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tPublisher = itemView.findViewById(R.id.txtInstitute);
            tTitle = itemView.findViewById(R.id.txtTitle);
            tDateTime = itemView.findViewById(R.id.txtDateTime);
            tLanguage = itemView.findViewById(R.id.txtLanguage);
            btnGetNotes = itemView.findViewById(R.id.btnGetNotes);

        }


    }
}
