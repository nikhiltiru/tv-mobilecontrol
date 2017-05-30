package com.example.android.nsdchatclient;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class ControlActivity extends Activity {

    private final static String TAG = "ControlActivity";
    ChatConnection mConnection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.controls);

        Intent intent = getIntent();

        mConnection = new ChatConnection();
        try {
            InetAddress address = InetAddress.getByName(intent.getStringExtra("host"));
            int port = intent.getIntExtra("port", 0);
            Log.v(TAG, "addr: " + address + ", port: " + port);
            mConnection.connectToServer(address, port);
        } catch (UnknownHostException e) {
            Log.e(TAG, "Ïnetaddr exception");
        }

        final EditText messageView = (EditText) this.findViewById(R.id.chatInput);
        ImageButton imageButton = (ImageButton) findViewById(R.id.videoplayer);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ControlActivity.this, LocalPlayerActivity.class);
                startActivity(intent);
            }
        });

        ImageButton send_btn = (ImageButton) findViewById(R.id.send_btn);
        send_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg = messageView.getText().toString();
                mConnection.sendMessage(msg);
                messageView.setText("");
            }
        });

        ImageButton back = (ImageButton) findViewById(R.id.imageButton3);
        ImageButton forward = (ImageButton) findViewById(R.id.imageButton4);
        ImageButton up = (ImageButton) findViewById(R.id.imageButton5);
        ImageButton down = (ImageButton) findViewById(R.id.imageButton6);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mConnection.sendMessage("back");
            }
        });
        forward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mConnection.sendMessage("forward");
            }
        });
        up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mConnection.sendMessage("up");
            }
        });
        down.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mConnection.sendMessage("down");
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mConnection.tearDown();
    }
}
