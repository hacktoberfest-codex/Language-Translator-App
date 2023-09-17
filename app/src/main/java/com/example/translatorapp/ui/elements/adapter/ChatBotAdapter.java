package com.example.translatorapp.ui.elements.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.translatorapp.databinding.SampleReceiverBinding;
import com.example.translatorapp.databinding.SampleSenderMsgBinding;
import com.example.translatorapp.ui.elements.MainActivityFragments.ChatBotFragment;
import com.example.translatorapp.ui.elements.models.Message;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class ChatBotAdapter extends RecyclerView.Adapter {
   ArrayList<Message> messageList;
    int SENDER_VIEW_TYPE = 1, RECEIVER_VIEW_TYPE = 2;

    public ChatBotAdapter(ArrayList<Message> messageList) {
        this.messageList = messageList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if(viewType == SENDER_VIEW_TYPE)
        {
            return new Sender(SampleSenderMsgBinding.inflate(
                    LayoutInflater.from(parent.getContext())
            ));
        }
        return new Receiver(SampleReceiverBinding.inflate(
                LayoutInflater.from(parent.getContext())
        ));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        Message message = messageList.get(position);
        if(holder.getClass() == Sender.class)
        {
            ((Sender) holder).senderTxt.setText(message.getMessage());
        }
        else{
            ((Receiver)holder).recieverTxt.setText(message.getMessage());
        }
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    @Override
    public int getItemViewType(int position) {

        if(messageList.get(position).getSentBy().equals(Message.SENT_BY_ME))
        {
            return SENDER_VIEW_TYPE;
        }
        return RECEIVER_VIEW_TYPE;
    }

    public class Receiver extends RecyclerView.ViewHolder
    {
    TextView recieverTxt;
        public Receiver(@NonNull SampleReceiverBinding sampleReceiverBinding) {
            super(sampleReceiverBinding.getRoot());

            recieverTxt = sampleReceiverBinding.receiverMsg;

        }
    }
    public class Sender extends RecyclerView.ViewHolder
    {
        TextView senderTxt;

        public Sender(@NonNull SampleSenderMsgBinding sampleSenderMsgBinding) {
            super(sampleSenderMsgBinding.getRoot());
            senderTxt = sampleSenderMsgBinding.senderMsg;
        }
    }
}
