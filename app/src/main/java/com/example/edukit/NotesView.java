package com.example.edukit;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.DownloadManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.NOTIFICATION_SERVICE;
import static com.example.edukit.App.CHANNEL_1_ID;

public class NotesView extends Fragment {

    final int UPI_PAYMENT = 0;
    public String publisherID, notesID, sampleURL, mainURL, backTo, localFileUrl, sample_link, notesAmount, payto;
    boolean isPaid;
    Toolbar toolbar;
    TextView tvHeading, tvPublisherName, tvNotesDescription;
    Button btnDownload;
    BottomNavigationView bottomNavigationView;
    StorageReference mStorageRef;
    CircleImageView publisherDP;
    LinearLayout btnViewSample;
    ImageView tvPubIsverified, btnBack;
    String fileName;
    DatabaseReference mRef;
    CustomDialog customDialog;
    BroadcastReceiver onComplete = new BroadcastReceiver() {
        public void onReceive(Context ctxt, Intent intent) {
            customDialog.dismissDialog();
            Toast toast = Toast.makeText(ctxt, "Notes Downloaded successfully", Toast.LENGTH_LONG);
            toast.show();
        }
    };

    public NotesView(String publisherID, String notesID, String backTo) {
        this.publisherID = publisherID;
        this.notesID = notesID;
        this.backTo = backTo;

    }

    @SuppressLint("SetJavaScriptEnabled")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.view_notes, container, false);
        toolbar = getActivity().findViewById(R.id.toolbar);
        tvHeading = view.findViewById(R.id.txtHeading);
        btnDownload = view.findViewById(R.id.btnDownload);
        publisherDP = view.findViewById(R.id.publisherDP);
        tvPublisherName = view.findViewById(R.id.tvPublisherName);
        tvNotesDescription = view.findViewById(R.id.tvNotesDescription);
        btnViewSample = view.findViewById(R.id.btnViewSample);
        tvPubIsverified = view.findViewById(R.id.tvPubIsverified);
        btnBack = view.findViewById(R.id.btnBack);
        mRef = FirebaseDatabase.getInstance().getReference();
        customDialog = new CustomDialog(getContext());

        bottomNavigationView = getActivity().findViewById(R.id.bottom_navigation);
        toolbar.setVisibility(View.GONE);
        bottomNavigationView.setVisibility(View.GONE);
        loadNotesInfo();
        publisherInfo();
        DatabaseReference pRef = FirebaseDatabase.getInstance().getReference().child("ADMIN").child("ADMIN_UPI_ID");
        pRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                payto = String.valueOf(dataSnapshot.getValue());
                btnDownload.setEnabled(true);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        btnDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ((Integer.valueOf(notesAmount) == 0) | (isPaid == true)) {

                    download();

                } else {
                    startPay();
                }
            }
        });
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goBack();
            }
        });
        btnViewSample.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewSample();
            }
        });
        mRef.child("QUESTIONS").child("Question_Info").child(notesID)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
        mRef.child("TRANSACTIONS")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                            if (String.valueOf(ds.child("EMAIL").getValue()).equals(FirebaseAuth.getInstance().getCurrentUser().getEmail())
                                    && String.valueOf(ds.child("CONTENT_ID").getValue()).equals(notesID)) {
                                btnDownload.setText("DOWNLOAD FULL NOTES");
                                isPaid = true;
                            } else {
                                btnDownload.setText("DOWNLOAD FULL NOTES ( Rs-" + notesAmount + " )");
                                isPaid = false;
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

        onBackPress(view);

        return view;

    }

    private void onBackPress(View view) {
        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
                    goBack();
                    return true;
                }
                return false;
            }
        });
    }

    private void viewSample() {
        if (!sample_link.startsWith("http://") && !sample_link.startsWith("https://"))
            sample_link = "http://" + sample_link;
        final Intent intent = new Intent(Intent.ACTION_VIEW).setData(Uri.parse(sample_link));
        startActivity(intent);
    }

    private void download() {
        //Getting File Name
        customDialog.showDialog("Downloading...");
        DatabaseReference mRef = FirebaseDatabase.getInstance().getReference().child("QUESTIONS").child("Question_Info").child(notesID);
        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                fileName = String.valueOf(dataSnapshot.child("Full_Notes").getValue());
                mStorageRef = FirebaseStorage.getInstance().getReference().child(fileName);
                mStorageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        mainURL = String.valueOf(uri);
                        //Generate Unique Name
                        String AppName = getActivity().getApplicationInfo().loadLabel(getActivity().getPackageManager()).toString();
                        String currentDateTimeString = java.text.DateFormat.getDateTimeInstance().format(new Date());
                        //Generate Unique Name
                        downloadFile(getContext(), AppName + "_" + currentDateTimeString, ".pdf", Environment.getExternalStorageDirectory() + "/Edukit", mainURL);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getContext(), "Faild to get URl", Toast.LENGTH_SHORT).show();
                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        //Getting File Name

    }

    public long downloadFile(Context context, String fileName, String fileExtension, String destinationDirectory, String url) {
        DownloadManager downloadmanager = (DownloadManager) context.
                getSystemService(Context.DOWNLOAD_SERVICE);
        getActivity().registerReceiver(onComplete, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
        Uri uri = Uri.parse(url);
        DownloadManager.Request request = new DownloadManager.Request(uri);
        request.setDescription("DOWNLOADING NOTES...");
        request.setTitle(fileName + ".pdf");
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalFilesDir(context, destinationDirectory, fileName + fileExtension);
        localFileUrl = destinationDirectory + "/" + fileName + fileExtension;
        return downloadmanager.enqueue(request);
    }

    private void loadNotesInfo() {
        DatabaseReference mRef = FirebaseDatabase.getInstance().getReference().child("QUESTIONS").child("Question_Info").child(notesID);
        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                tvHeading.setText(String.valueOf(dataSnapshot.child("Title").getValue()));
                tvNotesDescription.setText(String.valueOf(dataSnapshot.child("Description").getValue()));
                sample_link = String.valueOf(dataSnapshot.child("Sample_Link").getValue());
                float amount = Long.valueOf(String.valueOf(dataSnapshot.child("Price").getValue()));
                notesAmount = String.valueOf(dataSnapshot.child("Price").getValue());
                if (amount == 0) {
                    btnDownload.setText("DOWNLOAD FULL NOTES");
                    btnViewSample.setVisibility(View.GONE);

                } else {
                    btnViewSample.setVisibility(View.VISIBLE);
                    btnDownload.setText("DOWNLOAD FULL NOTES ( Rs-" + String.valueOf(dataSnapshot.child("Price").getValue()) + " )");
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void publisherInfo() {
        DatabaseReference mRef = FirebaseDatabase.getInstance().getReference().child("PUBLISHER").child("PUBLISHER_INFO").child(publisherID);
        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                tvPublisherName.setText(String.valueOf(dataSnapshot.child("Name").getValue()));
                Picasso.with(getContext()).load(String.valueOf(dataSnapshot.child("Image").getValue())).into(publisherDP);
                if (String.valueOf(dataSnapshot.child("Verified").getValue()).equals("1")) {
                    tvPubIsverified.setVisibility(View.VISIBLE);
                } else {
                    tvPubIsverified.setVisibility(View.GONE);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void goBack() {
        if (backTo.equals("1")) {
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
            toolbar.setVisibility(View.VISIBLE);
            bottomNavigationView.setVisibility(View.VISIBLE);
        } else if (backTo.equals("2")) {
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new QuestionsFragment(false)).commit();
            toolbar.setVisibility(View.VISIBLE);
            bottomNavigationView.setVisibility(View.VISIBLE);
        } else if (backTo.equals("3")) {
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new MySavedFragment(2)).commit();
            toolbar.setVisibility(View.VISIBLE);
            bottomNavigationView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
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
        if (isConnectionAvailable(getContext())) {
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
                tRef.child(key).child("CONTENT_ID").setValue(notesID);
                tRef.child(key).child("COST").setValue(notesAmount);
                tRef.child(key).child("DATE").setValue(getCurrentDate());
                tRef.child(key).child("EMAIL").setValue(FirebaseAuth.getInstance().getCurrentUser().getEmail().toString());
                tRef.child(key).child("PAID_FOR").setValue("Notes");
                tRef.child(key).child("REN_NO").setValue(approvalRefNo);
                tRef.child(key).child("TIME").setValue(getCurrentTime());
                tRef.child(key).child("PUBLISHER_ID").setValue(publisherID);
                //storing the details
                notifyAlert();
                showSucessDialog("The payment has been sucessfully done.Notes will be downloaded shortly");
                download();


                Log.e("UPI", "payment successfull: " + approvalRefNo);
            } else if ("Payment cancelled by user.".equals(paymentCancel)) {
                // Toast.makeText(getContext(), "Payment cancelled by user.", Toast.LENGTH_SHORT).show();
                showFailureDialog();
                Log.e("UPI", "Cancelled by user: " + approvalRefNo);
            } else {
                //  Toast.makeText(getContext(), "Transaction failed.Please try again", Toast.LENGTH_SHORT).show();
                showFailureDialog();
                Log.e("UPI", "failed payment: " + approvalRefNo);
            }
        } else {
            Log.e("UPI", "Internet issue: ");
            showFailureDialog();
            // Toast.makeText(getContext(), "Internet connection is not available. Please check and try again", Toast.LENGTH_SHORT).show();
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
        java.text.SimpleDateFormat mdformat = new java.text.SimpleDateFormat("hh:mm a");
        String strDate = mdformat.format(calendar.getTime());
        return strDate;
    }

    private String getCurrentDate() {
        java.text.SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        String date = df.format(Calendar.getInstance().getTime());
        return date;
    }

    private void startPay() {
        float tAmount = Float.valueOf(notesAmount);
        String fAmount = String.valueOf(tAmount);
        Uri uri =
                new Uri.Builder()
                        .scheme("upi")
                        .authority("pay")
                        .appendQueryParameter("pa", payto)
                        .appendQueryParameter("pn", "Edukit")
                        .appendQueryParameter("tr", "25584584")
                        .appendQueryParameter("tn", "Purchasing Notes (ID :" + notesID + ")")
                        .appendQueryParameter("am", fAmount)
                        .appendQueryParameter("cu", "INR")
                        .build();
        Intent upiPayIntent = new Intent(Intent.ACTION_VIEW);
        upiPayIntent.setData(uri);
        Intent chooser = Intent.createChooser(upiPayIntent, "Pay with");
        if (null != chooser.resolveActivity(getActivity().getPackageManager())) {
            startActivityForResult(chooser, UPI_PAYMENT);
        } else {
            Toast.makeText(getContext(), "No UPI app found, please install one to continue", Toast.LENGTH_SHORT).show();
        }

    }

    private void showSucessDialog(String message) {
        final Dialog epicDialog = new Dialog(getContext());
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

    }

    private void showFailureDialog() {
        final Dialog epicDialog;
        epicDialog = new Dialog(getContext());
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
        NotificationManager mNotificationManager = (NotificationManager) getActivity().getSystemService(NOTIFICATION_SERVICE);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getContext(), CHANNEL_1_ID);
        mBuilder.setContentTitle("PAYMENT SUCCESSFULL");
        mBuilder.setColor(getResources().getColor(R.color.colorPrimary));
        mBuilder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.notes));
        mBuilder.setSmallIcon(R.drawable.ic_open_book);
        mBuilder.setWhen(System.currentTimeMillis());
        mBuilder.setStyle(new NotificationCompat.BigTextStyle()
                .bigText("Notes( ID : " + notesID + ") has been added to your account."));
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


}

