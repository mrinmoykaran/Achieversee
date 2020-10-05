package com.example.edukit;

import android.app.Dialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.example.edukit.App.CHANNEL_1_ID;

public class MockPreviewActivity extends AppCompatActivity {
    final int UPI_PAYMENT = 0;
    boolean isPaid;
    CustomDialog customDialog;
    private TextView mTitle, mPrice, mTotalRating, mLangugae, mDescription;
    private RatingBar mRating;
    private TextView pName, pName2, pAdress;
    private ImageView pVerified;
    private DatabaseReference mRef;
    private Button btnPay;
    private CircleImageView pImage;
    private String tTitle, tTime, tFM, tNM, amount, customer_name, note, payto, q_set_id;
    private String PublisherID;
    private String CourseID;
    private NotificationManagerCompat notificationManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mock_preview);

        mRef = FirebaseDatabase.getInstance().getReference();
        mTitle = findViewById(R.id.tvMockTitle);
        mPrice = findViewById(R.id.tvMockPrice);
        mRating = findViewById(R.id.tvMockRating);
        mTotalRating = findViewById(R.id.tvMockTotalRating);
        mLangugae = findViewById(R.id.tvMockLanguage);
        mDescription = findViewById(R.id.tvMockDescription);
        pName = findViewById(R.id.tvPublisherName);
        pImage = findViewById(R.id.tvPubImage);
        pName2 = findViewById(R.id.tvPublisherTitle);
        pAdress = findViewById(R.id.tvPubAddress);
        pVerified = findViewById(R.id.tvPubIsverified);
        btnPay = findViewById(R.id.btnPay);
        customDialog = new CustomDialog(MockPreviewActivity.this);
        customDialog.showDialog("Loading...");
        ImageView btnBack = findViewById(R.id.btnBack);
        notificationManager = NotificationManagerCompat.from(getApplicationContext());
        btnPay.setEnabled(false);

        PublisherID = getIntent().getExtras().getString("PublisherID");
        CourseID = getIntent().getExtras().getString("CourseID");
        //Getting Mock Test Details
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        DatabaseReference pRef = FirebaseDatabase.getInstance().getReference().child("ADMIN").child("ADMIN_UPI_ID");
        pRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                payto = String.valueOf(dataSnapshot.getValue());
                btnPay.setEnabled(true);
                customDialog.dismissDialog();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        mRef.child("MOCK_TEST").child("CourseList").child(CourseID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    tTitle = String.valueOf(dataSnapshot.child("Title").getValue());
                    mTitle.setText(tTitle);
                    tTime = String.valueOf(dataSnapshot.child("Duration").getValue());
                    tFM = String.valueOf(dataSnapshot.child("FullMarks").getValue());
                    tNM = String.valueOf(dataSnapshot.child("NegetiveMarking").getValue());
                    q_set_id = String.valueOf(dataSnapshot.child("QSetID").getValue());
                    mPrice.setText("Rs " + String.valueOf(dataSnapshot.child("Price").getValue()));
                    switch (String.valueOf(dataSnapshot.child("Language").getValue())) {
                        case "1":
                            mLangugae.setText("BENGALI");
                            break;
                        case "2":
                            mLangugae.setText("HINDI");
                            break;
                        case "3":
                            mLangugae.setText("ENGLISH");
                            break;
                    }
                    mDescription.setText(String.valueOf(dataSnapshot.child("Description").getValue()));
                    mTotalRating.setText("(" + String.valueOf(dataSnapshot.child("TotalRating").getValue()) + ")");
                    mRating.setRating(Float.valueOf(dataSnapshot.child("Rating").getValue().toString()));
                    amount = String.valueOf(dataSnapshot.child("Price").getValue());
                    note = "Purchasing Mock Test (ID :" + CourseID + ")";
                    customer_name = FirebaseAuth.getInstance().getCurrentUser().getEmail().toString();
                    if (Integer.valueOf(amount) > 0) {
                        btnPay.setText("PAY ₹ " + amount);
                    } else {
                        btnPay.setText("START TEST");
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        //Check is the user has already paid or not
        pRef = mRef.child("TRANSACTIONS");
        pRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    if (String.valueOf(ds.child("EMAIL").getValue()).equals(FirebaseAuth.getInstance().getCurrentUser().getEmail())
                            && String.valueOf(ds.child("CONTENT_ID").getValue()).equals(CourseID)) {
                        btnPay.setText("START TEST");
                        isPaid = true;
                    } else {
                       // btnPay.setText("PAY ₹ " + amount);
                        isPaid = false;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        //Getting Publisher Details
        mRef.child("PUBLISHER").child("PUBLISHER_INFO").child(PublisherID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    pName.setText(String.valueOf(dataSnapshot.child("Name").getValue()));
                    pName2.setText(String.valueOf(dataSnapshot.child("Name").getValue()));
                    if (Integer.valueOf(dataSnapshot.child("Verified").getValue().toString()) == 0) {
                        pVerified.setVisibility(View.GONE);
                    } else {
                        pVerified.setVisibility(View.VISIBLE);
                    }
                    Picasso.with(getApplicationContext()).load(String.valueOf(dataSnapshot.child("Image").getValue())).into(pImage);
                    pAdress.setText(String.valueOf(dataSnapshot.child("Address").getValue()));
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        btnPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ((Integer.valueOf(amount) == 0) | (isPaid == true)) {
                    startTest();

                } else {
                    startPay();
                }

            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /*
       E/main: response -1
       E/UPI: onActivityResult: txnId=AXI4a3428ee58654a938811812c72c0df45&responseCode=00&Status=SUCCESS&txnRef=922118921612
       E/UPIPAY: upiPaymentDataOperation: txnId=AXI4a3428ee58654a938811812c72c0df45&responseCode=00&Status=SUCCESS&txnRef=922118921612
       E/UPI: payment successfull: 922118921612
         */
        switch (requestCode) {
            case UPI_PAYMENT:
                if ((RESULT_OK == resultCode) || (resultCode == 11)) {
                    if (data != null) {
                        String trxt = data.getStringExtra("response");
                        Log.e("UPI", "onActivityResult: " + trxt);
                        ArrayList<String> dataList = new ArrayList<>();
                        dataList.add(trxt);
                        upiPaymentDataOperation(dataList);
                    } else {
                        Log.e("UPI", "onActivityResult: " + "Return data is null");
                        ArrayList<String> dataList = new ArrayList<>();
                        dataList.add("nothing");
                        upiPaymentDataOperation(dataList);
                    }
                } else {
                    //when user simply back without payment
                    Log.e("UPI", "onActivityResult: " + "Return data is null");
                    ArrayList<String> dataList = new ArrayList<>();
                    dataList.add("nothing");
                    upiPaymentDataOperation(dataList);
                }
                break;
        }
    }

    private void upiPaymentDataOperation(ArrayList<String> data) {
        if (isConnectionAvailable(MockPreviewActivity.this)) {
            String str = data.get(0);
            Log.e("UPIPAY", "upiPaymentDataOperation: " + str);
            String paymentCancel = "";
            if (str == null) str = "discard";
            String status = "";
            String approvalRefNo = "";
            String response[] = str.split("&");
            for (int i = 0; i < response.length; i++) {
                String equalStr[] = response[i].split("=");
                if (equalStr.length >= 2) {
                    if (equalStr[0].toLowerCase().equals("Status".toLowerCase())) {
                        status = equalStr[1].toLowerCase();
                    } else if (equalStr[0].toLowerCase().equals("ApprovalRefNo".toLowerCase()) || equalStr[0].toLowerCase().equals("txnRef".toLowerCase())) {
                        approvalRefNo = equalStr[1];
                    }
                } else {
                    paymentCancel = "Payment cancelled by user.";
                }
            }
            if (status.equals("success")) {
                //Code to handle successful transaction here.
                //storing the details
                DatabaseReference CoreRef = FirebaseDatabase.getInstance().getReference();
                DatabaseReference tRef = CoreRef.child("TRANSACTIONS");
                String key = String.valueOf(tRef.push().getKey());
                tRef.child(key).child("CONTENT_ID").setValue(CourseID);
                tRef.child(key).child("COST").setValue(amount);
                tRef.child(key).child("DATE").setValue(getCurrentDate());
                tRef.child(key).child("EMAIL").setValue(customer_name);
                tRef.child(key).child("PAID_FOR").setValue("Mock Test");
                tRef.child(key).child("REN_NO").setValue(approvalRefNo);
                tRef.child(key).child("TIME").setValue(getCurrentTime());
                tRef.child(key).child("PUBLISHER_ID").setValue(PublisherID);
                //storing the details
                showSucessDialog();
                startTest();


                Log.e("UPI", "payment successfull: " + approvalRefNo);
            } else if ("Payment cancelled by user.".equals(paymentCancel)) {
                // Toast.makeText(MockPreviewActivity.this, "Payment cancelled by user.", Toast.LENGTH_SHORT).show();
                showFailureDialog();
                Log.e("UPI", "Cancelled by user: " + approvalRefNo);
            } else {
                // Toast.makeText(MockPreviewActivity.this, "Transaction failed.Please try again", Toast.LENGTH_SHORT).show();
                showFailureDialog();
                Log.e("UPI", "failed payment: " + approvalRefNo);
            }
        } else {
            Log.e("UPI", "Internet issue: ");
            showFailureDialog();
            //  Toast.makeText(MockPreviewActivity.this, "Internet connection is not available. Please check and try again", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isConnectionAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo netInfo = connectivityManager.getActiveNetworkInfo();
            if (netInfo != null && netInfo.isConnected()
                    && netInfo.isConnectedOrConnecting()
                    && netInfo.isAvailable()) {
                return true;
            }
        }
        return false;
    }

    public String getCurrentTime() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat mdformat = new SimpleDateFormat("hh:mm a");
        String strDate = mdformat.format(calendar.getTime());
        return strDate;
    }

    private String getCurrentDate() {
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        String date = df.format(Calendar.getInstance().getTime());
        return date;
    }

    private void startTest() {
        Intent intent = new Intent(MockPreviewActivity.this, MockTestActivity.class);
        intent.putExtra("mid", CourseID);
        intent.putExtra("pid", PublisherID);
        intent.putExtra("title", tTitle);
        intent.putExtra("fm", tFM);
        intent.putExtra("nm", tNM);
        intent.putExtra("time", tTime);
        intent.putExtra("q_set_id", q_set_id);
        intent.putExtra("CourseID", CourseID);

        startActivity(intent);
    }

    private void startPay() {
        float tAmount = Float.valueOf(amount);
        String fAmount = String.valueOf(tAmount);
        Uri uri =
                new Uri.Builder()
                        .scheme("upi")
                        .authority("pay")
                        .appendQueryParameter("pa", payto)
                        .appendQueryParameter("pn", "Edukit")
                        .appendQueryParameter("tr", "25584584")
                        .appendQueryParameter("tn", note)
                        .appendQueryParameter("am", fAmount)
                        .appendQueryParameter("cu", "INR")
                        .build();
        Intent upiPayIntent = new Intent(Intent.ACTION_VIEW);
        upiPayIntent.setData(uri);
        Intent chooser = Intent.createChooser(upiPayIntent, "Pay with");
        if (null != chooser.resolveActivity(getPackageManager())) {

            startActivityForResult(chooser, UPI_PAYMENT);
        } else {
            Toast.makeText(MockPreviewActivity.this, "No UPI app found, please install one to continue", Toast.LENGTH_SHORT).show();
        }

    }

    private void showSucessDialog() {
        final Dialog epicDialog = new Dialog(this);
        epicDialog.setContentView(R.layout.payment_sucess_popup);
        Button btnOk = epicDialog.findViewById(R.id.btnOk);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                epicDialog.dismiss();
            }
        });
        epicDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                epicDialog.dismiss();
            }
        }, 2000);
        epicDialog.show();
        notifyAlert();

    }

    private void showFailureDialog() {
        final Dialog epicDialog;
        epicDialog = new Dialog(this);
        epicDialog.setContentView(R.layout.payment_failed_popup);
        Button button = epicDialog.findViewById(R.id.btnFFaliedOk);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                epicDialog.dismiss();
            }
        });
        epicDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        epicDialog.show();


    }

    private void notifyAlert() {
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(MockPreviewActivity.this, CHANNEL_1_ID);
        mBuilder.setContentTitle("PAYMENT SUCCESSFULL");
        mBuilder.setColor(getResources().getColor(R.color.colorPrimary));
        mBuilder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.savedtest));
        mBuilder.setSmallIcon(R.drawable.ic_open_book);
        mBuilder.setWhen(System.currentTimeMillis());
        mBuilder.setStyle(new NotificationCompat.BigTextStyle()
                .bigText("Mock test( ID : " + CourseID + ") has been added to your account."));
        mBuilder.setAutoCancel(true);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_1_ID, "NOTIFICATION_CHANNEL_NAME", importance);
            mBuilder.setChannelId(CHANNEL_1_ID);
            assert mNotificationManager != null;
            mNotificationManager.createNotificationChannel(notificationChannel);
        }
        assert mNotificationManager != null;
        mNotificationManager.notify((int) System.currentTimeMillis(), mBuilder.build());
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
