package com.example.udpchat;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import androidx.annotation.NonNull;
import java.net.InetSocketAddress;
import java.net.SocketException;

public class ChatActivity extends Activity implements View.OnClickListener, OnSocketListener, Handler.Callback {

    private String name;
    private int sourcePort;

    private InetSocketAddress address;

    private Channel channel;

    private EditText messageEditText;

    private ListView messageListView;

    private ArrayAdapter<String> messageAdapter;

    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        Intent intent = getIntent();
        name = intent.getStringExtra("name");
        sourcePort = intent.getIntExtra("sourcePort", 1111);
        String destinationIP = intent.getStringExtra("destinationIP");
        int destinationPort = intent.getIntExtra("destinationPort", 2222);

        address = new InetSocketAddress(destinationIP, destinationPort);

        messageEditText = findViewById(R.id.messageEditText);
        Button sendButton = findViewById(R.id.sendButton);
        sendButton.setOnClickListener(this);

        messageAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.layout_message, R.id.messageTextView);

        messageListView = (ListView) findViewById(R.id.messageListView);
        messageListView.setAdapter(messageAdapter);

        handler = new Handler(this);

    }

    @Override
    protected void onStart() {
        super.onStart();
        if(null == channel) {
            try {
                channel = new Channel(this);
                channel.bind(sourcePort);
                channel.start();
            } catch (SocketException e) {
                e.printStackTrace();
                finish();
            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(null != channel) {
            channel.stop();
        }
    }

    @Override
    public boolean handleMessage(@NonNull Message message) {
        return false;
    }

    @Override
    public void onClick(View view) {
        String text = messageEditText.getText().toString();
        text = name + " >> " + text;

        messageEditText.setText("");

        channel.sendTo(address, text);

        messageAdapter.add(text);

        messageListView.smoothScrollToPosition(messageAdapter.getCount() - 1);
    }


    @Override
    public void onReceived(String text) {
        Bundle bundle = new Bundle();
        bundle.putString("text", text);

        Message msg = new Message();
        msg.setData(bundle);

        handler.sendMessage(msg);
    }
}