package com.example.chatroom;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
    private ArrayList<String> mDataset;
    protected static String username;
    protected static Client client ;
    protected static ThreadSend ThreadSend = null;
    protected static ThreadReceive ThreadReceive = null;
    protected static String friend;
    protected static String aux;




    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final Context context;

        /*private Client client = new Client();
        ThreadConnect ThreadConnect = new ThreadConnect(client);
        ThreadSend ThreadSend = new ThreadSend(client);
        ThreadReceive ThreadReceive = null;
        String friend;
        String aux;*/


        public TextView textView;

        public MyViewHolder(Context context, View v) {
            super(v);
            textView = v.findViewById(R.id.person_name);
            this.context = context;
            v.setOnClickListener(this);


        }



        @Override
        public void onClick(View view) {


            int position = getAdapterPosition(); // gets item position
            if (position != RecyclerView.NO_POSITION) { // Check if an item was deleted, but the user clicked it before the UI removed it

                System.out.println("******************* " + client + " ***********************");

                friend = textView.getText().toString().trim();
                aux = client.getMsg + username + " " +friend;

                //ThreadReceive = new ThreadReceive(friend);
                ThreadSend = new ThreadSend(client);
                ThreadReceive = new ThreadReceive(client, friend, context);

                /*System.out.println("******************* " + aux.toUpperCase() + " ***********************");
                ThreadSend.setMessage(aux);
                ThreadSend.startRunning();
                ThreadReceive.startRunning();*/


                Intent gotoconversation = new Intent( context.getApplicationContext() ,TesteActivity.class );
                gotoconversation.putExtra("user",username + " " + textView.getText().toString().trim());
                context.startActivity(gotoconversation);

            }
        }

    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public MyAdapter(ArrayList myDataset , String username , Client client ) {
        mDataset = myDataset;
        this.username = username;
        this.client = client;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public MyAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View nameView = inflater.inflate(R.layout.my_text_view, parent, false);

        MyViewHolder myViewHolder = new MyViewHolder(context, (nameView));
        return myViewHolder;
    }


    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        String myuser = mDataset.get(position);
        TextView tv = holder.textView;
        tv.setText(myuser);


    }


    @Override
    public int getItemCount() {
        return mDataset.size();
    }



    static class ThreadReceive implements Runnable{

        Thread thread;
        String server_resp = null;
        String corretct_string;
        char[] converter;
        char[] aux_converter;
        String friend;
        Client client;
        Context context;
        ObjectOutputStream outStream;
        ObjectInputStream inStream;
        Conversation aux;
        JSONObject json;
        JSONArray jarray;
        JSONParser parser = new JSONParser();

        public ThreadReceive( Client client, String friend, Context context){
            this.client = client;
            this.friend = friend;
            this.context = context;
        }

        @Override
        public void run() {


            try {
                //inStream = new ObjectInputStream(client.clientSocket.getInputStream());
                //System.out.println("instream!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!"+inStream);
                //aux = (Conversation) inStream.readObject();
                server_resp = client.receiveMessage();

                System.out.println("***********************");
                System.out.println(server_resp);
                System.out.println("***********************");

            } catch ( IOException e) {
                e.printStackTrace();
            }

            converter = server_resp.toCharArray();
            int l = 0;
            aux_converter = new char[converter.length-4];
            for(int i = 4 ; i < converter.length ; i++){

                aux_converter[l] = converter[i];
                l++;

            }
            System.out.println();
            corretct_string = new String (aux_converter);
            System.out.println("ouuuuuuu"+ corretct_string);
            try {
                json = (JSONObject) parser.parse(corretct_string);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            String nome = null;
            Map<String, String> m = new LinkedHashMap<String, String>(2);
            m = (Map<String, String>) json.get("user_info");
            JSONArray array = new JSONArray();
            array = (JSONArray) json.get("messages");

            aux.user_send = m.get("user_send");
            aux.user_rcv = m.get("user_rcv");
            for(int i = 0; i < array.length(); i++) {
                try {
                    aux.msg_list.add((Message) array.get(i));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            System.out.println("NOMEEEE" + nome);
            System.out.println("***********************");
            System.out.println(aux);
            System.out.println("***********************");

            Intent gotoconversation = new Intent( context.getApplicationContext() ,TesteActivity.class );
            gotoconversation.putExtra("Conversation", aux);
            context.startActivity(gotoconversation);



            thread.interrupt();
        }

        public void startRunning() {

            thread = new Thread (this);
            thread.start();
        }

    }






}


