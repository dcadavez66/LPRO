package com.example.chatroom;

import java.io.IOException;

class ThreadSend implements Runnable{
    private Thread thread;
    private String message;
    Client client;
    public ThreadSend( Client c){
            this.client = c;
        }
    @Override
    public void run(){

        try {
            client.sendMessage(message);
        } catch (IOException e) {
            e.printStackTrace();
        }

        thread.interrupt();


    }

    public void setMessage(String msg) {
        message = msg;
    }
    public void startRunning() {

        thread = new Thread (this);
        thread.start();
    }


}
