package com.example.edukit;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.MyHolder> {

    List<NotificationModel> nList;

    public NotificationAdapter(List<NotificationModel> nList) {
        this.nList = nList;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.notification_row, viewGroup, false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        holder.txttitle.setText("Paid for buying " + nList.get(position).getPaid_for() + " ₹" + nList.get(position).getCost());
        holder.txttime.setText(nList.get(position).getTransaction_time());
        holder.txtdate.setText(nList.get(position).getTransaction_date());
    }

    @Override
    public int getItemCount() {
        return nList.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        TextView txttitle, txtdate, txttime;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            txttitle = itemView.findViewById(R.id.txtNtitle);
            txtdate = itemView.findViewById(R.id.txtNDate);
            txttime = itemView.findViewById(R.id.txtNtime);
        }
    }
}
