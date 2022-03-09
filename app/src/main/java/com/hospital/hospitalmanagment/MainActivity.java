package com.hospital.hospitalmanagment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;
import com.hospital.hospitalmanagment.doctor.DoctorActivity;
import com.hospital.hospitalmanagment.doctor.DoctorHomeActivity;
import com.hospital.hospitalmanagment.loginsignup.LoginActivity;
import com.hospital.hospitalmanagment.loginsignup.UserOptionActivity;
import com.hospital.hospitalmanagment.user.AskMobileAndprofileActivity;
import com.hospital.hospitalmanagment.user.UserHomeActivity;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private CoordinatorLayout coordinatorLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        coordinatorLayout = findViewById(R.id.splashcoordinate);

//        UserOptionActivity
//        startActivity(new Intent(MainActivity.this, AdminActivity.class));
//        finish();
        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

        if(firebaseUser != null){
//            Snackbar.make(coordinatorLayout, "userlogin "+firebaseUser.getUid(), Snackbar.LENGTH_SHORT).show();

            FirebaseDatabase.getInstance().getReference("Doctors").child(firebaseUser.getUid())
                    .get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DataSnapshot> task) {
                    if (task.isSuccessful()){
                        if(task.getResult().child("Admin").exists()){
//                            Snackbar.make(coordinatorLayout, "user found in doctor login and admin ", Snackbar.LENGTH_SHORT).show();
                            startActivity(new Intent(MainActivity.this, AdminActivity.class));
                            finish();
                        }else if(task.getResult().child("Special").exists()){
//                            Snackbar.make(coordinatorLayout, "user  found in doctor login and doctor ", Snackbar.LENGTH_SHORT).show();
                            startActivity(new Intent(MainActivity.this, DoctorHomeActivity.class));
                            finish();
                        }else{
//                            Snackbar.make(coordinatorLayout, "user not found in doctor login", Snackbar.LENGTH_SHORT).show();
                            FirebaseDatabase.getInstance().getReference("Patiens").child(firebaseUser.getUid())
                                    .get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DataSnapshot> task) {
                                    if(task.isSuccessful()){
                                        if(task.getResult().child("Admin").exists()){
//                                            Snackbar.make(coordinatorLayout, "user found in Patiens login and admin ", Snackbar.LENGTH_SHORT).show();
                                            startActivity(new Intent(MainActivity.this, AdminActivity.class));
                                            finish();
                                        }else if(!(task.getResult().child("Phone").exists())){
//                                            Snackbar.make(coordinatorLayout, "user  found in Patiens login and patien not phone ", Snackbar.LENGTH_SHORT).show();
                                            startActivity(new Intent(MainActivity.this, AskMobileAndprofileActivity.class));
                                            finish();
                                        }else{
//                                            Snackbar.make(coordinatorLayout, "user  found in Patiens login and patien ", Snackbar.LENGTH_SHORT).show();
                                            startActivity(new Intent(MainActivity.this, UserHomeActivity.class));
                                            finish();
                                        }
                                    }else{
                                        Snackbar.make(coordinatorLayout, "inner Getting Error please contact admin!", Snackbar.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                    }else {
                        Snackbar.make(coordinatorLayout, "outer Getting Error please contact admin!", Snackbar.LENGTH_SHORT).show();
                    }
                }
            });
        }else {
//            Snackbar.make(coordinatorLayout, "user not login", Snackbar.LENGTH_SHORT).show();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    startActivity(new Intent(MainActivity.this, UserOptionActivity.class));
                    finish();
                }
            },2000);
        }

    }
}