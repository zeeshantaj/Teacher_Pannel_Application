package com.example.teacher_panel_application.Utils;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.provider.Settings;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.teacher_panel_application.EditDataFragments.EditDataFragment;
import com.example.teacher_panel_application.Home.Home_Activity;
import com.example.teacher_panel_application.Models.UploadClassModel;
import com.example.teacher_panel_application.Network.NetworkUtils;
import com.example.teacher_panel_application.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.checkerframework.checker.units.qual.C;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class MethodsUtils {
    public static void shareOnWhatsapp(Context context,String name, String loc, String dura, String dep, String sub, String top, String start){
        String teacherName = "Teacher: " + name + "\n";
        String location = "Location: " + loc + "\n";
        String duration = "Duration: " + dura + " minutes\n";
        String depart = "Major: " + dep + "\n";
        String subject = "Subject: " + sub + "\n";
        String topic = "Today's Topic: " + top + "\n";
        String started = "Started At: " + start;

        String message = teacherName + location + duration + depart + subject + topic + started;

        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, message);
        shareIntent.setPackage("com.whatsapp");
        context.startActivity(shareIntent);
    }
    public static void getData(Context context, TextInputEditText edName, TextInputEditText edDepartment,
                         TextInputEditText edLocation, TextInputEditText edSubject,
                         TextInputEditText edTopic, TextInputEditText edKey, EditText edMinute) {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        String uid = auth.getUid();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Teacher_Data").child(uid);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {

                    String name = snapshot.child("name").getValue(String.class);
                    String dep = snapshot.child("department").getValue(String.class);
                    String loc = snapshot.child("location").getValue(String.class);
                    String sub = snapshot.child("subject").getValue(String.class);
                    String topi = snapshot.child("topic").getValue(String.class);
                    String minute = snapshot.child("minutes").getValue(String.class);
                    String key = snapshot.child("key").getValue(String.class);
                    edName.setText(name);
                    edDepartment.setText(dep);
                    edLocation.setText(loc);
                    edSubject.setText(sub);
                    edTopic.setText(topi);
                    edMinute.setText(minute);
                    edKey.setText(key);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(context, "Error " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    public static void setCurrentUserYearAndSemester(Context context,String year,String semester){
        //DatabaseReference studentRef = FirebaseDatabase.getInstance().getReference().child("StudentsInfo");
        SharedPreferences sharedPreferences1 = context.getSharedPreferences("StudentInfoShared",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences1.edit();
        editor.putString("studentYear",year);
        editor.putString("studentSemester",semester);
        editor.apply();
    }
    public static void setFragment(FragmentManager fragmentManager, Fragment fragment){
        // Create an instance of the transparent fragment

        // Add the fragment to the fragment manager
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(android.R.id.content, fragment); // Use android.R.id.content to add the fragment above all views
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    public static void showFlawDialog(Context context, int imageView, String title, String content, int type) {
        Dialog errorDialog;
        MaterialButton btnContinue;
        TextView contentTextView, titleTextView;
        ImageView imgErrorType;
        errorDialog = new Dialog(context);
        errorDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        errorDialog.setContentView(R.layout.error_type_dialog);
        errorDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        imgErrorType = errorDialog.findViewById(R.id.imageView11);
        btnContinue = errorDialog.findViewById(R.id.button7);
        contentTextView = errorDialog.findViewById(R.id.pass);
        titleTextView = errorDialog.findViewById(R.id.textView32);

//        setText:
        titleTextView.setText("" + title);
        contentTextView.setText("" + content);

//        showImageType:


        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                errorDialog.dismiss();
            }
        });


        errorDialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        errorDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        errorDialog.setCancelable(false);
        errorDialog.show();

    }


    public static void showAlertDialogue(Context context){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("Internet is turned off. Do you want to turn it on?")
                .setCancelable(false)
                .setPositiveButton("Yes", (dialog, id) -> context.startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS)))
                .setNegativeButton("No", (dialog, id) -> dialog.cancel());
        AlertDialog alert = builder.create();
        alert.show();
    }
    public static void checkInternet(Context context, View rootView){
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            if (activeNetworkInfo != null && activeNetworkInfo.isConnected()) {
                // Device's internet is turned on
                NetworkUtils.hasInternetAccess(hasInternetAccess -> {
                    if (hasInternetAccess){
                        Toast.makeText(context, "Internet has service", Toast.LENGTH_SHORT).show();
                    }else {
                        Snackbar.make(rootView.findViewById(android.R.id.content), "You Lost Internet Connection!", Snackbar.LENGTH_INDEFINITE)
                                .setAction("Check Again", view -> {
                                    // Retry checking internet connectivity
                                    checkInternet(context,rootView);
                                }).show();

                    }
                });

            } else {
                // Device's internet is turned off
                // Show dialog to prompt user to turn on internet
                MethodsUtils.showAlertDialogue(context);

            }
        } else {
            // Error checking internet connection
            Toast.makeText(context, "Error checking internet connection", Toast.LENGTH_SHORT).show();
        }

    }


    public static void showSuccessDialog(Context context,String title, String content, int type) {

        final SweetAlertDialog sd = new SweetAlertDialog(context, type)
                .setTitleText(title)
                .setContentText(content);
        sd.show();
    }

    public static void putString(Context context,String key,String value){
        SharedPreferences sharedPreference = context.getSharedPreferences("SharedUserInfo", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreference.edit();
        editor.putString(key,value);
        editor.apply();
    }
    public static String getString(Context context,String key){
        SharedPreferences sharedPreferences = context.getSharedPreferences("loginType", Context.MODE_PRIVATE);
        return sharedPreferences.getString(key,"");
    }


    public static String getCurrentUID(){
        FirebaseAuth auth = FirebaseAuth.getInstance();
        return auth.getUid();
    }
    public static DatabaseReference getCurrentUserRef(String path){
        FirebaseAuth auth = FirebaseAuth.getInstance();
        String uid = auth.getUid();
        return FirebaseDatabase.getInstance().getReference().child(path).child(uid);
    }
}
