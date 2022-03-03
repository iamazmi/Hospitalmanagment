package com.hospital.hospitalmanagment.loginsignup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AnimationUtils;

import com.hospital.hospitalmanagment.R;
import com.hospital.hospitalmanagment.user.UserHomeActivity;

public class UserOptionActivity extends AppCompatActivity {

    private CardView drcview,pcview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_option);

        drcview = findViewById(R.id.drcardview);
        pcview = findViewById(R.id.patientcardview);

        drcview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drcview.startAnimation(AnimationUtils.loadAnimation(UserOptionActivity.this,R.anim.clickeffect));
                Intent pintent = new Intent(UserOptionActivity.this,LoginActivity.class);
                pintent.putExtra("loginType","Docter");
                startActivity(pintent);
                finish();
            }
        });


        pcview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pcview.startAnimation(AnimationUtils.loadAnimation(UserOptionActivity.this,R.anim.clickeffect));
                Intent pintent = new Intent(UserOptionActivity.this,LoginActivity.class);
                pintent.putExtra("loginType","Patient");
                startActivity(pintent);
                finish();
            }
        });

    }
}