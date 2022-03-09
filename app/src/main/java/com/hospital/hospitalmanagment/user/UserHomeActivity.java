package com.hospital.hospitalmanagment.user;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.hospital.hospitalmanagment.R;
import com.hospital.hospitalmanagment.loginsignup.UserOptionActivity;

public class UserHomeActivity extends AppCompatActivity {

    private AppBarLayout appBarLayout;
    private  MaterialToolbar materialToolbar;
    Context context;
    private Menu menu;
    private CardView bookappoint,historyappointmentv,Hospitalistv,faqv;

    private FirebaseAuth firebaseAuth;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_home);
        context = this;
        materialToolbar = findViewById(R.id.topAppBarHome);

        bookappoint = findViewById(R.id.bookappointmentbtn);
        historyappointmentv = findViewById(R.id.historyappointment);
        Hospitalistv = findViewById(R.id.Hospitalist);
        faqv = findViewById(R.id.faqbtn);

        firebaseAuth = FirebaseAuth.getInstance();

        bookappoint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bookappoint.startAnimation(AnimationUtils.loadAnimation(UserHomeActivity.this,R.anim.clickeffect));
                startActivity(new Intent(UserHomeActivity.this,DoctorsListActivity.class));
            }
        });
        historyappointmentv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                historyappointmentv.startAnimation(AnimationUtils.loadAnimation(UserHomeActivity.this,R.anim.clickeffect));
                startActivity(new Intent(UserHomeActivity.this,HistoryActivity.class));
            }
        });
        Hospitalistv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Hospitalistv.startAnimation(AnimationUtils.loadAnimation(UserHomeActivity.this,R.anim.clickeffect));

            }
        });
        faqv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                faqv.startAnimation(AnimationUtils.loadAnimation(UserHomeActivity.this,R.anim.clickeffect));

            }
        });


        materialToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.Profile:
                        startActivity(new Intent(UserHomeActivity.this, UserProfileActivity.class));
//                        Toast.makeText(UserHomeActivity.this,"Profile",Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.logout:
                        if(firebaseAuth.getCurrentUser() != null){
                            firebaseAuth.signOut();
                            startActivity(new Intent(UserHomeActivity.this, UserOptionActivity.class));
                            finish();
                        }
                        break;
                    default:
                        return false;
                }
                return false;
            }
        });



    }//

}//