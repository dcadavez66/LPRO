package com.example.chatroom;

import java.io.IOException;

class ThreadConnect implements Runnable{
    Thread thread;
    Client client;
    public ThreadConnect(Client c){
        this.client = c;
    }
    public void run(){

        try {
            client.startConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void startRunning() {

        thread = new Thread (this);
        thread.start();
    }


}
