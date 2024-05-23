package org.udg.pds.todoandroid.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import org.udg.pds.todoandroid.R;
import org.udg.pds.todoandroid.entity.Chat;
import org.udg.pds.todoandroid.entity.Message;
import org.udg.pds.todoandroid.util.UserUtils;

import java.time.format.DateTimeFormatter;
import java.util.List;


public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {

    private List<Message> mMessages;
    private final Long currentUserId;

    public MessageAdapter(Context context, List<Message> messages) {
        mMessages = messages;
        this.currentUserId = UserUtils.getCurrentUserId(context);
    }

    @Override
    public int getItemViewType(int position) {
        Message message = mMessages.get(position);
        if (message.getSenderId().equals(currentUserId)) {
            return R.layout.message_item_sent;
        } else {
            return R.layout.message_item_received;
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        Message message = mMessages.get(position);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        String formattedTimestamp = message.getTimestamp().format(formatter);
        holder.messageTimestamp.setText(formattedTimestamp);

        String messageDisplay = "User" + message.getSenderId() + ": " + message.getContent();
        holder.messageContent.setText(messageDisplay);
    }

    @Override
    public int getItemCount() {
        return mMessages != null ? mMessages.size() : 0;
    }

    public void updateMessages(List<Message> newMessages) {
        mMessages = newMessages;
        notifyDataSetChanged();
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
