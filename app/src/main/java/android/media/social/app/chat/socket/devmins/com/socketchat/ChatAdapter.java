package android.media.social.app.chat.socket.devmins.com.socketchat;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatViewHolder> {

    private ArrayList<ChatModel> mChatList;

    public ChatAdapter(ArrayList<ChatModel> mChatList) {
        this.mChatList= mChatList;
    }

    @NonNull
    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        View view = layoutInflater.inflate(R.layout.item_chat_box, viewGroup, false);
        return new ChatViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatViewHolder chatViewHolder, int i) {
        switch (mChatList.get(i).getSender()){
            case 1:
                chatViewHolder.mTextViewRight.setText(mChatList.get(i).getMessage());
                chatViewHolder.mTextViewRight.setVisibility(View.VISIBLE);
                break;
            case 2:
                chatViewHolder.mTextViewLeft.setText(mChatList.get(i).getMessage());
                chatViewHolder.mTextViewLeft.setVisibility(View.VISIBLE);
                break;
        }

    }

    @Override
    public int getItemCount() {
        return mChatList.size();
    }

    public class ChatViewHolder extends RecyclerView.ViewHolder {

        TextView mTextViewLeft, mTextViewRight;
        public ChatViewHolder(@NonNull View itemView) {
            super(itemView);
            mTextViewLeft = (TextView)itemView.findViewById(R.id.tv_chat_message_left);
            mTextViewRight= (TextView)itemView.findViewById(R.id.tv_chat_message_right);
        }
    }
}
