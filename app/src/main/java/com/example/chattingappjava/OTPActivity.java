package com.example.chattingappjava;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

public class OTPActivity extends AppCompatActivity {

    TextView changeNumberTv;
    EditText getOTPEd;
    Button verifyBtn;
    String enterdOTP;

    FirebaseAuth firebaseAuth;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_otpactivity);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        changeNumberTv = findViewById(R.id.changeNumber);
        verifyBtn = findViewById(R.id.verifyOTPBt);
        getOTPEd = findViewById(R.id.getOTPEd);
        progressBar = findViewById(R.id.progressOTP);

        firebaseAuth = FirebaseAuth.getInstance();

        changeNumberTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OTPActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });

        verifyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                enterdOTP = getOTPEd.getText().toString();

                if (enterdOTP.isEmpty()){
                    Toast.makeText(OTPActivity.this, "Enter OTP First..!!", Toast.LENGTH_SHORT).show();
                }else {

                    progressBar.setVisibility(View.VISIBLE);

                    String otpCodeReceived = getIntent().getStringExtra("otp");
                    PhoneAuthCredential phoneAuthCredential = PhoneAuthProvider.getCredential(otpCodeReceived,enterdOTP);
                    singInWithPhoneAuthCredential(phoneAuthCredential);
                }
            }
        });
    }

    private void singInWithPhoneAuthCredential(PhoneAuthCredential phoneAuthCredential) {

        firebaseAuth.signInWithCredential(phoneAuthCredential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()){
                    progressBar.setVisibility(View.INVISIBLE);

                    Toast.makeText(OTPActivity.this, "Login Success..", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(OTPActivity.this,SetupProfileActivity.class);
                    startActivity(intent);
                }else {

                    if (task.getException() instanceof FirebaseAuthInvalidCredentialsException){
                        progressBar.setVisibility(View.INVISIBLE);
                        Toast.makeText(OTPActivity.this, "Login Failed..", Toast.LENGTH_SHORT).show();

                    }
                }
            }
        });
    }
}