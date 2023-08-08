package com.example.v_chat.Adaptor;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.v_chat.ChatDetailsActivity;
import com.example.v_chat.R;
import com.example.v_chat.models.users;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.Viewholder>{

    ArrayList<users> list;
    Context context;

    public UsersAdapter(ArrayList<users> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.sample_show_user,parent,false);
        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, @SuppressLint("RecyclerView") int position) {
        users users=list.get(position);
        Picasso.get().load(users.getProfilepic()).placeholder(R.drawable.avatar3).into(holder.image);
        holder.userName.setText(users.getUsername());

        // removed visibility of last msg because the logic should be corrected
        holder.lastMessage.setVisibility(position == getItemCount()? View.VISIBLE : View.GONE);

        // To set last message
//        FirebaseDatabase.getInstance().getReference().child("chats")
//                .child(FirebaseAuth.getInstance().getUid() + users.getUserid())
//                .orderByChild("timestamp")
//                .limitToLast(1)
//                .addListenerForSingleValueEvent(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot snapshot) {
//                        if (snapshot.hasChildren()) {
//                            for (DataSnapshot snapshot1 : snapshot.getChildren()) {
//                                holder.lastMessage.setText(snapshot1.child("message").getValue().toString());
//                            }
//                        }
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError error) {
//                        // Handle onCancelled event
//                    }
//                });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String receiverId = list.get(position).getUserid();
                String receiverName = list.get(position).getUsername();
                String profilePicUrl = list.get(position).getProfilepic();

                Intent intent = new Intent(context, ChatDetailsActivity.class);
                intent.putExtra("userid", receiverId);
                intent.putExtra("username", receiverName);
                intent.putExtra("profilepic", profilePicUrl);
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder{
        ImageView image;
        TextView userName,lastMessage;

        public Viewholder(@NonNull View itemView) {
            super(itemView);

            image=itemView.findViewById(R.id.profile_img);
            userName=itemView.findViewById(R.id.UserNameList);
            lastMessage=itemView.findViewById(R.id.LastMessage);
        }

    }
}
