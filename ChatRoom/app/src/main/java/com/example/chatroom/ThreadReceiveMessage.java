package com.example.chatroom;
import android.content.Intent;

import java.io.IOException;

class ThreadReceiveMessage implements Runnable{
    private Thread thread;
    private String server_resp;
    private String message;
    Client client;

    public ThreadReceiveMessage(){

        client = new Client();
        ThreadConnect t_con = new ThreadConnect(client);
        ThreadSend send = new ThreadSend(client);
        t_con.startRunning();
        send.setMessage(message);
        send.startRunning();
    }
    @Override
    public void run(){

        try {
            server_resp = client.receiveMessage();
        } catch (IOException e) {
            e.printStackTrace();
        }



        if (server_resp.equals(client.ack)) {
            /*
            Intent intent = new Intent(getApplicationContext(), TesteActivity.class);
            intent.putExtra("user",user_send + " " + user_rcv);
            startActivity(intent);
            TesteActivity.this.finish();*/

        } else if (server_resp.equals(client.nack)) {



        }


    }

    public void setMessage(String msg) {
        message = msg;
    }
    public void startRunning() {

        thread = new Thread (this);
        thread.start();
    }


}
