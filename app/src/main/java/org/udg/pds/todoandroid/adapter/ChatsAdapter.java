package org.udg.pds.todoandroid.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import org.udg.pds.todoandroid.R;
import org.udg.pds.todoandroid.activity.ChatMessagesActivity;
import org.udg.pds.todoandroid.entity.Chat;
import org.udg.pds.todoandroid.entity.Message;
import org.udg.pds.todoandroid.util.UserUtils;

import java.util.ArrayList;
import java.util.List;


public class ChatsAdapter extends RecyclerView.Adapter<ChatsAdapter.ViewHolder> {

    private final List<Chat> mChats;
    private Context mContext;
    private final Long currentUserId;

    public ChatsAdapter(Context context, List<Chat> chats) {
        mContext = context;
        mChats = chats;
        currentUserId = UserUtils.getCurrentUserId(context);

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
            .inflate(R.layout.chat_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        Chat chat = mChats.get(position);
        Long chatPartnerId = chat.getUserTargetId().equals(currentUserId) ? chat.getUserId() : chat.getUserTargetId();
        Long postId = chat.getPostId();
        holder.chatPartnerName.setText("Chat amb userId: " + chatPartnerId + " --- postId: " + postId);

        if (chat.getMessages() != null && !chat.getMessages().isEmpty()) {
            Message lastMessage = chat.getMessages().get(chat.getMessages().size() - 1);
            holder.lastMessage.setText(lastMessage.getContent());
        } else {
            holder.lastMessage.setText("Sense missatges"); //mai passara
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ChatMessagesActivity.class);
                intent.putExtra("CHAT_ID", chat.getId());
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mChats != null ? mChats.size() : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView chatPartnerName;
        public final TextView lastMessage;

        public ViewHolder(View view) {
            super(view);
            chatPartnerName = view.findViewById(R.id.chat_partner_name);
            lastMessage = view.findViewById(R.id.last_message);
        }
    }
}
