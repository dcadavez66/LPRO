package com.example.chatroom;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;

public class Conversationmaker extends AppCompatActivity {

    Intent j;
    EditText person;
    Button next1;
    TextView info;
    Client client;
    ThreadConnect ThreadConnect = null;
    ThreadSend ThreadSend = null;
    ThreadReceive ThreadReceive = null;
    String message;
    String server_resp;
    String username;
    String user_dest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversationmaker);
        j = getIntent();
        username = j.getStringExtra("user");
        person = findViewById(R.id.person);
        client = new Client();
        next1 = findViewById(R.id.next1);
        info = findViewById(R.id.info);
        ThreadConnect = new ThreadConnect(client);
        ThreadConnect.startRunning();
        ThreadSend = new ThreadSend(client);
        ThreadReceive = new ThreadReceive();




        next1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user_dest = person.getText().toString().trim();
                message = client.newconversation + username + " " + user_dest;

                if (!message.isEmpty()) {
                    ThreadSend.setMessage(message);
                    ThreadSend.startRunning();
                    ThreadReceive.startRunning();
                }

            }
        });



    }

    class ThreadReceive implements Runnable {

        Thread thread;
        //ThreadReceiveMessage rec = new ThreadReceiveMessage();

        @Override
        public void run() {

            try {

                server_resp = client.receiveMessage();

            } catch (IOException e) {
                e.printStackTrace();
            }

            if (server_resp.equals(client.ack)) {

                /*mes = username + user_dest;
                if (!mes.isEmpty()) {
                    rec.setMessage(mes);
                    rec.startRunning();
                }*/

                Intent sendmsg = new Intent(getApplicationContext(), TesteActivity.class);
                sendmsg.putExtra("user",username + " " + user_dest + " " + server_resp);
                startActivity(sendmsg);
                Conversationmaker.this.finish();


            } else if (server_resp.equals(client.nack)) {

                info.setText("Username doesn't exist");

            }

            thread.interrupt();
        }

        public void startRunning() {

            thread = new Thread(this);
            thread.start();
        }


    }


}
