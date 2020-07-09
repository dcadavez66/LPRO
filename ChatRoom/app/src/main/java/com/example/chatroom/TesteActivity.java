package com.example.chatroom;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.ArrayList;

public class TesteActivity extends AppCompatActivity {

    Intent j;
    EditText msg_input;
    //TextView mymsg;
    //TextView yourmsg;
    ImageButton send;
    Button button_chatbox_send;
    String message;
    String users;
    String[] u_split;
    String[] rabbit_split;
    String user_send;
    String user_rcv;
    String server_resp;
    String messageSent;
    String input;
    Client client;
    //TextView error;
    ThreadConnect ThreadConnect = null;
    ThreadSend  ThreadSend = null;
    ThreadReceive ThreadReceive = null;
    String [] messages = new String[100];
    String messageRabbit;
    int messages_index = 0;
    RecyclerView mMessageRecycler;
    //MessageListAdapter mMessageAdapter;
    MyMessageAdapter mMessageAdapter;
    Conversation conversation;
    TextView convname;
    TextView textView0;
    TextView textView1;
    TextView textView2;
    TextView textView3;
    TextView textView4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_teste);

        client = new Client();
        j = getIntent();
        //conversation = (Conversation)j.getSerializableExtra("user");
        users = j.getStringExtra("user");
        u_split = users.split(" ");
        user_send = u_split[0];
        user_rcv = u_split[1];
        button_chatbox_send = findViewById(R.id.button_chatbox_send);
        msg_input = findViewById(R.id.edittext_chatbox);
        //send = findViewById(R.id.send);
        //mymsg = findViewById(R.id.mymsg);
        //error = findViewById((R.id.error));
        //yourmsg = findViewById(R.id.yourmsg);
        ThreadConnect = new ThreadConnect(client);
        ThreadConnect.startRunning();
        ThreadSend = new ThreadSend(client);
        ThreadReceive = new ThreadReceive();
        /*textView0 = findViewById(R.id.textView0);
        textView1 = findViewById(R.id.textView1);
        textView2 = findViewById(R.id.textView2);
        textView3 = findViewById(R.id.textView3);*/
        convname = findViewById(R.id.convname);
        convname.setText(user_rcv);
        ArrayList<String> message_list = new ArrayList<>();
        final RabbitMQ rabbit = new RabbitMQ(user_send , user_rcv);
        ConnectionFactory factory = new ConnectionFactory();

        rabbit.setupConnectionFactory(factory);
        rabbit.publishToAMQP();


        mMessageRecycler =  findViewById(R.id.reyclerview_message_list);
        System.out.println("user_rcv:" + user_rcv );
        if(user_rcv.equals("public")){
            mMessageAdapter = new MyMessageAdapter( message_list, user_send , user_rcv ,true );
        }
        else{
            mMessageAdapter = new MyMessageAdapter( message_list, user_send , user_rcv ,false );
        }
        mMessageRecycler.setAdapter(mMessageAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setStackFromEnd(true);
        layoutManager.isAutoMeasureEnabled();
        mMessageRecycler.setLayoutManager(layoutManager);


        final Handler messagehandler = new Handler(){
            @Override
            public void handleMessage(Message msg){
                String message_received = msg.getData().getString("msg");
                rabbit_split = message_received.split(" ",2);
                String infomessage =  rabbit_split[0] + " " + rabbit_split[1];;
                //mMessageAdapter.setMessage(infomessage);
                System.out.println(infomessage);
                message_list.add(infomessage);
                //mMessageAdapter.notifyDataSetChanged();
                mMessageAdapter.notifyItemInserted(message_list.size()-1);
                /*if(user_send.equals(rabbit_split[0])){
                    infomessage =  user_send + " " + rabbit_split[0] + " " + rabbit_split[2];
                    //textView0.setText(infomessage);
                }
                else{
                    infomessage = rabbit_split[0] + ":" + rabbit_split[2];
                    //textView1.setText(infomessage);
                }
                /*else if(user_send.equals(rabbit_split[1])){

                    infomessage = rabbit_split[1] + ":" + rabbit_split[2];
                    textView1.setText(infomessage);
                }*/


            }


        };

        rabbit.subscribe(messagehandler);



        button_chatbox_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                input = msg_input.getText().toString().trim();
                message = client.send + user_rcv + " " + user_send + " " + input;
                messageRabbit = user_send + " " + user_rcv + " " + input;
                messageSent = user_send + ": " + input;
                System.out.println(message);
                messages[messages_index] = message;
                messages_index++;
                //textView0.setText(messageSent);


                if(!input.isEmpty()){
                    rabbit.publishMessage(messageRabbit);
                    ThreadSend.setMessage(message);
                    ThreadSend.startRunning();
                    ThreadReceive.startRunning();
                }


            }
        });

        //////////////////////////////////////

        /////////////////////////////////////

    }

    class ThreadReceive implements Runnable{

        Thread thread;
        @Override
        public void run(){

            try {
                server_resp = client.receiveMessage();

            } catch (IOException e) {
                e.printStackTrace();
            }

            if (server_resp.equals(client.ack)) {

                //Intent intent = new Intent(getApplicationContext(), TesteActivity.class);
                //intent.putExtra("user",user_send + " " + user_rcv);
                //startActivity(intent);
                //TesteActivity.this.finish();
                //mymsg.setText(messageSent);
                msg_input.getText().clear();

            } else if (server_resp.equals(client.nack)) {

                //error.setText("MESSAGE NOT SENT");

            }

            //thread.interrupt();
        }

        public void startRunning() {

            thread = new Thread (this);
            thread.start();
        }

    }



}
