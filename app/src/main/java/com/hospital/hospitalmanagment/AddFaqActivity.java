package com.hospital.hospitalmanagment;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

public class AddFaqActivity extends AppCompatActivity {

    private TextInputLayout questionl;
    private TextInputEditText questiont;
    private TextInputLayout ansl;
    private TextInputEditText anst;

    private Button Addfaq;
    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_faq);
       firebaseAuth = FirebaseAuth.getInstance();
       Addfaq = findViewById(R.id.addfaqbtn);
        questionl = findViewById(R.id.faqquestionlout);
        questiont = findViewById(R.id.faqquestion);

        ansl = findViewById(R.id.faqanslout);
        anst = findViewById(R.id.faqans);
        progressDialog = new ProgressDialog(this);
        Addfaq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(notempty()){
                    progressDialog.setMessage("Adding");
                    progressDialog.show();
                    String timestamp = (Calendar.getInstance().getTimeInMillis()+"");
                    FirebaseDatabase.getInstance().getReference("FAQ").child(timestamp)
                            .child("question").setValue(questiont.getText().toString().trim());
                    FirebaseDatabase.getInstance().getReference("FAQ").child(timestamp)
                            .child("ans").setValue(anst.getText().toString().trim());
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            questiont.setText("");
                            anst.setText("");
                            progressDialog.dismiss();
                        }
                    },500);
                }
            }
        });

    }//

    private boolean notempty() {
        boolean res = true;
        questionl.setError("");
        ansl.setError("");

        if(questiont.getText().toString().equals("")){
            res = false;
            questionl.setError("Field is Empty!");
        }

        if(anst.getText().toString().equals("")){
            res = false;
            ansl.setError("Field is Empty!");
        }
        return  res;
    }
}