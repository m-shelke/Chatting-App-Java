package com.example.chattingappjava;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.squareup.picasso.Picasso;

import org.checkerframework.checker.units.qual.C;
import org.w3c.dom.Text;

public class ChatFragment extends Fragment {

    FirebaseFirestore firebaseFirestore;
    LinearLayoutManager linearLayoutManager;
    FirebaseAuth firebaseAuth;
    ImageView imageView;
    RecyclerView recyclerView;

    FirestoreRecyclerAdapter<firabaseModel,NoteViewHolder> chatAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat,container,false);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        recyclerView = view.findViewById(R.id.recyclerView);

        Query query = firebaseFirestore.collection("Users");
        FirestoreRecyclerOptions<firabaseModel> allUserName = new FirestoreRecyclerOptions.Builder<firabaseModel>().setQuery(query,firabaseModel.class).build();

        chatAdapter = new FirestoreRecyclerAdapter<firabaseModel, NoteViewHolder>(allUserName) {
            @Override
            protected void onBindViewHolder(@NonNull NoteViewHolder holder, int position, @NonNull firabaseModel model) {

               holder.particularUsername.setText(model.getName());
               String uri = model.getImage();

               Picasso.get().load(uri).into(imageView);

               if (model.getStatus().equals("Online")){
                   holder.statusOfUser.setText(model.getStatus());
                   holder.statusOfUser.setTextColor(Color.GREEN);
               }else {
                   holder.statusOfUser.setText(model.getStatus());
               }

               holder.itemView.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View v) {
                       Toast.makeText(getActivity(), "ItemView get Clicked", Toast.LENGTH_SHORT).show();
                   }
               });
            }

            @NonNull
            @Override
            public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view1 = LayoutInflater.from(parent.getContext()).inflate(R.layout.chatviewlayout,parent,false);
                return new NoteViewHolder(view1);
            }
        };

        recyclerView.setHasFixedSize(true);
        linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(chatAdapter);

        return view;
    }

    public class NoteViewHolder extends RecyclerView.ViewHolder{

        private TextView particularUsername;
        private TextView statusOfUser;

        public NoteViewHolder(@NonNull View itemView) {
            super(itemView);

            particularUsername = itemView.findViewById(R.id.nameOfUserr);
            statusOfUser = itemView.findViewById(R.id.statusOfUser);
            imageView = itemView.findViewById(R.id.imageViewOfUser);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        chatAdapter.startListening();
    }

    @Override
    public void onPause() {
        super.onPause();
        chatAdapter.stopListening();
    }
}