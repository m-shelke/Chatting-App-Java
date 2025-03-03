package com.example.chattingappjava;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chattingappjava.databinding.ActivitySpecificChatBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class SpecificChatActivity extends AppCompatActivity {

    ActivitySpecificChatBinding binding;

    private String enteredMessage;
    Intent intent;
    private String receiverName,senderName,receiverId,senderId;
    private FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    String receiverRoom,senderRoom;
    RecyclerView recyclerView;

    String currentTime;
    Calendar calendar;
    SimpleDateFormat simpleDateFormat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        binding = ActivitySpecificChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        intent = getIntent();

     //   setSupportActionBar(binding.toolBar);

        binding.toolBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(SpecificChatActivity.this, "Toolbar get Clicked By User", Toast.LENGTH_SHORT).show();
            }
        });

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        calendar = Calendar.getInstance();

        simpleDateFormat = new SimpleDateFormat("hh:mm a");

        senderId = firebaseAuth.getUid();
        receiverId = getIntent().getStringExtra("receiverId");
        receiverName = getIntent().getStringExtra("name");

        binding.userNameTv.setText(receiverName);

        senderRoom = senderId+receiverId;
        receiverRoom = receiverId+senderId;

        binding.toolbarBackBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        binding.userNameTv.setText(receiverName);
        String uri = intent.getStringExtra("imageUrl");

        if (uri.isEmpty()){
            Toast.makeText(this, "Null Image Reference get", Toast.LENGTH_SHORT).show();
        }else {
            Picasso.get().load(uri).into(binding.profileImage);
        }

    }
}