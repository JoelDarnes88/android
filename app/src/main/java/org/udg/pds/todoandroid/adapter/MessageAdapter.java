package org.udg.pds.todoandroid.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import org.udg.pds.todoandroid.R;
import org.udg.pds.todoandroid.entity.Chat;
import org.udg.pds.todoandroid.entity.Message;

import java.util.List;


public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {

    private final List<Message> mMessages;

    public MessageAdapter(List<Message> messages) {
        mMessages = messages;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
            .inflate(R.layout.message_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        Message message = mMessages.get(position);
        //holder.messageTimestamp.setText(message.getTimestamp());
        String messageDisplay = "Enviat per UserId" + message.getSenderId() + ": " + message.getContent();
        holder.messageContent.setText(messageDisplay);
    }

    @Override
    public int getItemCount() {
        return mMessages != null ? mMessages.size() : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView messageContent;
        public final TextView messageTimestamp;

        public ViewHolder(View view) {
            super(view);
            messageContent = view.findViewById(R.id.message_content);
            messageTimestamp = view.findViewById(R.id.message_timestamp);
        }
    }
}
