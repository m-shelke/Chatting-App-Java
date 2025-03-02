package com.example.chattingappjava;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.chattingappjava.databinding.ActivityMainBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.hbb20.CountryCodePicker;

import java.util.HashMap;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;

    private ProgressDialog progressDialog;

    private FirebaseAuth firebaseAuth;
    private PhoneAuthProvider.ForceResendingToken forceResendingToken;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks Callbacks;
    private String VerificationId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        binding.phoneInputRl.setVisibility(View.VISIBLE);
        binding.optInputRl.setVisibility(View.GONE);

        progressDialog=new ProgressDialog(this);
        progressDialog.setTitle("Please Wait...");
        progressDialog.setCanceledOnTouchOutside(false);

        firebaseAuth = FirebaseAuth.getInstance();

        phoneLoginCallBack();

        binding.sendOtpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validateData();
            }
        });

        binding.resendotpTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                resendVerificationCode(forceResendingToken);
            }
        });


        binding.verifyOtpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String otp=binding.otpEt.getText().toString().trim();

                if (otp.isEmpty()){
                    binding.otpEt.setError("Enter OTP..");
                    binding.otpEt.requestFocus();
                } else if (otp.length()<6) {
                    binding.otpEt.setError("OTP must be 6 Digit..");
                    binding.otpEt.requestFocus();
                }else {
                    verifyPhoneNumberWithCode(VerificationId,otp);
                }
            }
        });
    }





    private String phoneCode="",phoneNumber="",phoneNumberWithCode="";

    private void validateData() {

        phoneCode=binding.phoneCodeTil.getSelectedCountryCodeWithPlus();
        phoneNumber=binding.phoneNumberEt.getText().toString().trim();
        phoneNumberWithCode=phoneCode+phoneNumber;

        if (phoneNumber.isEmpty()){
            Toast.makeText(this, "Please Enter Phone Number..!!", Toast.LENGTH_SHORT).show();
        } else if (phoneNumber.length()<10) {
            Toast.makeText(this, "Number Must Be 10 Digits..!!", Toast.LENGTH_SHORT).show();
        } else {
            startPhoneNumberVerificaton();
        }
    }


    private void startPhoneNumberVerificaton() {

        progressDialog.setMessage("Sending OTP to "+phoneNumberWithCode);
        progressDialog.show();

        PhoneAuthOptions phoneAuthOptions=PhoneAuthOptions.newBuilder(firebaseAuth)  //Firebase instance
                .setPhoneNumber(phoneNumberWithCode)                                 //Phone Number with Country Code e.g:-+91 xxxxxxxxxx
                .setTimeout(60L, TimeUnit.SECONDS)                            //TimeOut and Unit
                .setActivity(this)                                                   //Activity (For callback binding)
                .setCallbacks(Callbacks)
                .build();

        PhoneAuthProvider.verifyPhoneNumber(phoneAuthOptions);

    }

    private void phoneLoginCallBack() {

        Callbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential credential) {

                signInWithPhoneAuthCredential(credential);
            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                progressDialog.dismiss();

                Toast.makeText(MainActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCodeSent(@NonNull String verificationId, @NonNull PhoneAuthProvider.ForceResendingToken token) {
                super.onCodeSent(verificationId, token);

                VerificationId=verificationId;
                forceResendingToken = token;

                progressDialog.dismiss();

                binding.phoneInputRl.setVisibility(View.INVISIBLE);
                binding.optInputRl.setVisibility(View.VISIBLE);

                Toast.makeText(MainActivity.this, "OTP Send To "+phoneNumberWithCode, Toast.LENGTH_SHORT).show();

                binding.verificatinTv.setText("Please enter the Verification Code sent to the "+phoneNumberWithCode);
            }
        };

    }

    private void verifyPhoneNumberWithCode(String verificationId, String opt) {

        progressDialog.setMessage("Verifying OTP..");
        progressDialog.show();

        PhoneAuthCredential credential=PhoneAuthProvider.getCredential(verificationId,opt);
        signInWithPhoneAuthCredential(credential);
    }

    private void resendVerificationCode(PhoneAuthProvider.ForceResendingToken forceResendingToken) {

        progressDialog.setMessage("Resending OTP..");
        progressDialog.show();

        PhoneAuthOptions phoneAuthOptions=PhoneAuthOptions.newBuilder(firebaseAuth)  //Firebase instance
                .setPhoneNumber(phoneNumberWithCode)                                 //Phone Number with Country Code e.g:-+91 xxxxxxxxxx
                .setTimeout(120L, TimeUnit.SECONDS)                            //TimeOut and Unit
                .setActivity(this)                                                   //Activity (For callback binding)
                .setCallbacks(Callbacks)
                .setForceResendingToken(forceResendingToken)
                .build();

        PhoneAuthProvider.verifyPhoneNumber(phoneAuthOptions);
    }


    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {

        progressDialog.setMessage("Logging In..");

        firebaseAuth.signInWithCredential(credential)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {

                        if (Objects.requireNonNull(authResult.getAdditionalUserInfo()).isNewUser()){

                            startActivity(new Intent(MainActivity.this, SetupProfileActivity.class));
                            finishAffinity();
                        }else if (FirebaseAuth.getInstance().getCurrentUser() == null){
                            startLoginOption();
                        }else {
                            startActivity(new Intent(MainActivity.this, SetupProfileActivity.class));
                            finishAffinity();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();

                        Toast.makeText(MainActivity.this, "Failed Log In due to "+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (FirebaseAuth.getInstance().getCurrentUser() != null){
            Intent intent = new Intent(MainActivity.this, ChattingActivity2.class);
            startActivity(intent);
            finish();
        }
    }

    private void startLoginOption(){
        //MainActivity to LoginActivity
        startActivity(new Intent(this, SetupProfileActivity.class));
    }
}