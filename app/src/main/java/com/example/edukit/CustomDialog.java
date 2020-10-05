package com.example.edukit;

import android.app.ProgressDialog;
import android.content.Context;
import android.widget.TextView;

public class CustomDialog {
    ProgressDialog progressDialog;
    Context context;
    String message;

    public CustomDialog(Context context) {
        this.context = context;
        this.message = message;
    }

    public void showDialog(String message) {
        progressDialog = new ProgressDialog(context);
        progressDialog.show();
        progressDialog.setContentView(R.layout.custom_progress_dialog);
        TextView tvMessage = progressDialog.findViewById(R.id.txtMessage);
        if (!message.isEmpty()) {
            tvMessage.setText(message + "...");
        }
        progressDialog.setCancelable(false);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
    }

    public void dismissDialog() {
        progressDialog.dismiss();
    }
}
