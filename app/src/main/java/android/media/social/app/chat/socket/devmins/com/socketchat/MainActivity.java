package android.media.social.app.chat.socket.devmins.com.socketchat;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int MESSAGE_SENDER_ME = 1;
    private static final int MESSAGE_SENDER_OTHER = 2;
    private RecyclerView mRecyclerViewChat;
    private Button mSendButton;
    private EditText mEditTextChatBox;
    private ChatAdapter chatAdapter;
    private ArrayList<ChatModel> mChatList = new ArrayList<>();
    private Socket mSocket;
    private boolean isConnected;
    private String myUserName = "devmins";

    {
        try {
            mSocket = IO.socket("https://socket-io-chat.now.sh/");
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mRecyclerViewChat = findViewById(R.id.rv_chat);
        mEditTextChatBox = findViewById(R.id.et_chat_box);
        mSendButton = findViewById(R.id.btn_send);
        mSendButton.setOnClickListener(this);
        onSocketConnect();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRecyclerViewChat.setLayoutManager(linearLayoutManager);
        chatAdapter = new ChatAdapter(mChatList);
        mRecyclerViewChat.setAdapter(chatAdapter);
    }

    private void onSocketConnect() {
        mSocket.on(Socket.EVENT_CONNECT, onConnect);
        mSocket.on(Socket.EVENT_DISCONNECT, onDisconnected);
        mSocket.on(Socket.EVENT_CONNECT_ERROR, onConnectionError);
        mSocket.on(Socket.EVENT_CONNECT_TIMEOUT, onConnectionError);
        mSocket.on("new message", onNewMessage);
        mSocket.connect();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mSocket.disconnect();
        mSocket.off(Socket.EVENT_CONNECT, onConnect);
        mSocket.off(Socket.EVENT_DISCONNECT, onDisconnected);
        mSocket.off(Socket.EVENT_CONNECT_ERROR, onConnectionError);
        mSocket.off(Socket.EVENT_CONNECT_TIMEOUT, onConnectionError);
        mSocket.off("new message", onNewMessage);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_send:
                String message = mEditTextChatBox.getText().toString().trim();
                if (message != null && !TextUtils.isEmpty(message)) {
                    mChatList.add(new ChatModel(message, myUserName, MESSAGE_SENDER_ME));
                    mEditTextChatBox.setText("");
                    mSocket.emit("new message", message);
                    if (chatAdapter != null)
                        chatAdapter.notifyDataSetChanged();
                }
                break;
        }
    }

    private Emitter.Listener onConnect = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (!isConnected) {
                        mSocket.emit("add user", myUserName);
                        Toast.makeText(getApplicationContext(), "Connected", Toast.LENGTH_SHORT).show();
                        isConnected = true;
                    }
                }
            });
        }
    };

    private Emitter.Listener onDisconnected = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                isConnected = false;
                Toast.makeText(getApplicationContext(), "Disconnected", Toast.LENGTH_SHORT).show();
                }
            });
        }
    };

    private Emitter.Listener onConnectionError = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getApplicationContext(), "Connection Error", Toast.LENGTH_SHORT).show();
                }
            });
        }
    };

    private Emitter.Listener onNewMessage = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject dataRecieved = (JSONObject) args[0];
                    String userName, message;
                    try{
                        userName = dataRecieved.getString("username");
                        message = dataRecieved.getString("message");
                    } catch (JSONException e) {
                        e.printStackTrace();
                        return;
                    }
                    Toast.makeText(getApplicationContext(), userName, Toast.LENGTH_SHORT).show();
                    mChatList.add(new ChatModel(message, userName, MESSAGE_SENDER_OTHER));
                    if (chatAdapter != null)
                        chatAdapter.notifyDataSetChanged();
                }
            });
        }
    };
}
