package com.example.v_chat.Adaptor;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.v_chat.R;
import com.example.v_chat.models.MessageModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class  ChatAdapter extends RecyclerView.Adapter{

    ArrayList<MessageModel> messageModels;
    Context context;
    String recID;

    int SENDER_VIEW_TYPE=1;
    int RECEIVER_VIEW_TYPE=2;
    private RecyclerView.ViewHolder holder;
    private int position;

    public ChatAdapter(ArrayList<MessageModel> messageModels, Context context) {
        this.messageModels = messageModels;
        this.context = context;
    }

    public ChatAdapter(ArrayList<MessageModel> messageModels, Context context, String recID) {
        this.messageModels = messageModels;
        this.context = context;
        this.recID = recID;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType==SENDER_VIEW_TYPE){
            View view= LayoutInflater.from(context).inflate(R.layout.sample_sender,parent,false);
            return new SenderViewHolder(view);
        }
        else {
            View view = LayoutInflater.from(context).inflate(R.layout.sample_receiver,parent,false);
            return new ReceiverViewHolder(view);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (messageModels.get(position).getuID().equals(FirebaseAuth.getInstance().getUid())){
            return SENDER_VIEW_TYPE;
        }
        else {
            return RECEIVER_VIEW_TYPE;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        MessageModel messageModel = messageModels.get(position);

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                new AlertDialog.Builder(context)
                        .setTitle("Delete")
                        .setMessage("Are you sure you want to delete this message")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int i) {
                                FirebaseDatabase database = FirebaseDatabase.getInstance();
                                String senderId = FirebaseAuth.getInstance().getUid();
                                String receiverId = recID;
                                String senderRoom = senderId + "+" + receiverId;
                                database.getReference().child("chats").child(senderRoom)
                                        .child(messageModel.getMessageID())
                                        .setValue(null)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    messageModels.remove(messageModel);
                                                    notifyItemRemoved(position);
                                                    notifyItemRangeChanged(position, messageModels.size());
                                                }
                                            }
                                        });
                                dialog.dismiss();
                            }
                        })
                        .setNegativeButton("No", (dialog, i) -> dialog.dismiss())
                        .show();
                return false;
            }
        });

        // Set common message content
        if (holder instanceof SenderViewHolder) {
            // Set sender message
            ((SenderViewHolder) holder).senderMsg.setText(messageModel.getMessage());
            //removed the time stamp visibility because the time stamp code is not working properly
            ((SenderViewHolder) holder).senderTime.setVisibility(position == getItemCount()? View.VISIBLE : View.GONE);
        } else if (holder instanceof ReceiverViewHolder) {
            // Set receiver message
            ((ReceiverViewHolder) holder).receiverMsg.setText(messageModel.getMessage());
            //removed the time stamp visibility because the time stamp code is not working properly
            ((ReceiverViewHolder) holder).receiverTime.setVisibility(position == getItemCount()? View.VISIBLE : View.GONE);
        }

        // Set time only for the current message
//        if (position == getItemCount() - 1) {
//            if (holder instanceof SenderViewHolder) {
//                // Set sender time
//                Date date = new Date(messageModel.getTimestamp());
//                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("h:mm a");
//                String strDate = simpleDateFormat.format(date);
//                ((SenderViewHolder) holder).senderTime.setText(strDate);
//            } else if (holder instanceof ReceiverViewHolder) {
//                // Set receiver time
//                Date date = new Date(messageModel.getTimestamp());
//                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("h:mm a");
//                String strDate = simpleDateFormat.format(date);
//                ((ReceiverViewHolder) holder).receiverTime.setText(strDate);
//            }
//        }
    }


    @Override
    public int getItemCount() {
        return messageModels.size();
    }

    public class ReceiverViewHolder extends RecyclerView.ViewHolder{

        TextView receiverMsg, receiverTime;

        public ReceiverViewHolder(@NonNull View itemView) {
            super(itemView);

            receiverMsg = itemView.findViewById(R.id.receiverText);
            receiverTime = itemView.findViewById(R.id.receiverTime);
        }
    }

    public class SenderViewHolder extends RecyclerView.ViewHolder{

        TextView senderMsg, senderTime;
        public SenderViewHolder(@NonNull View itemView) {
            super(itemView);

            senderMsg = itemView.findViewById(R.id.senderText);
            senderTime= itemView.findViewById(R.id.senderTime);
        }
    }
}
