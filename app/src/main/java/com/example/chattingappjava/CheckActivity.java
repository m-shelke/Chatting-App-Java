package com.example.chattingappjava;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.squareup.picasso.Picasso;

public class CheckActivity extends AppCompatActivity {

    FirebaseFirestore firebaseFirestore;
    LinearLayoutManager linearLayoutManager;
    FirebaseAuth firebaseAuth;
    ImageView imageVieww;
    RecyclerView recyclerView;

//    FirestoreRecyclerAdapter<firabaseModel,NoteViewHolder1> chatAdapter;
    FirestoreRecyclerAdapter<firabaseModel,CheckActivity.NoteViewHolder1> chatAdapter1;


    public View onCreate(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_check, container, false);



        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        recyclerView = view.findViewById(R.id.mRec);

        Query query1 = firebaseFirestore.collection("Users");
        FirestoreRecyclerOptions<firabaseModel> allUserName1 = new FirestoreRecyclerOptions.Builder<firabaseModel>().setQuery(query1, firabaseModel.class).build();

        chatAdapter1 = new FirestoreRecyclerAdapter<firabaseModel, CheckActivity.NoteViewHolder1>(allUserName1) {
            @Override
            protected void onBindViewHolder(@NonNull CheckActivity.NoteViewHolder1 holder, int position, @NonNull firabaseModel model) {

                holder.UsernameTvv.setText(model.getName());
                String uri = model.getImage();

                Picasso.get().load(uri).into(imageVieww);

                if (model.getStatus().equals("Online")) {
                    holder.StatusTvv.setText(model.getStatus());
                    holder.StatusTvv.setTextColor(Color.GREEN);
                } else {
                    holder.StatusTvv.setText(model.getStatus());
                }

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(getApplicationContext(), "ItemView get Clicked", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @NonNull
            @Override
            public CheckActivity.NoteViewHolder1 onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view1 = LayoutInflater.from(parent.getContext()).inflate(R.layout.chatviewlayout, parent, false);
                return new CheckActivity.NoteViewHolder1(view1);

            }
        };

        recyclerView.setHasFixedSize(true);
        linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(chatAdapter1);

        return view;

    }


    public class NoteViewHolder1 extends RecyclerView.ViewHolder{

        private TextView UsernameTvv;
        private TextView StatusTvv;

        public NoteViewHolder1(@NonNull View itemView1) {
            super(itemView1);

            UsernameTvv = itemView1.findViewById(R.id.imageViewOfUser);
            StatusTvv = itemView1.findViewById(R.id.nameOfUserr);
          //  imageVieww = itemView1.findViewById(R.id.statusOfUser);
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        chatAdapter1.startListening();
    }

    @Override
    protected void onPause() {
        super.onPause();
        chatAdapter1.stopListening();
    }
}