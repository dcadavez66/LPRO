package com.example.chatroom;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.util.ArrayList;

public class AddFriendActivity extends AppCompatActivity {

    Client client;
    Button btnext;
    Intent from_register_activity;
    SearchView search;
    CharSequence query;
    ListView lista_layout;
    ArrayList<String> list;
    ArrayList<String> friend_list;
    ArrayAdapter<String > adapter;
    ThreadConnect ThreadConnect = null;
    ThreadSend ThreadSend = null;
    ThreadReceive ThreadReceive = null;
    String message;
    String username;
    String[] received_info_array;
    String received_info_string;
    String server_resp;
    //primeiro elemento é o username deste cliente
    String[] friends_added = new String[100];
    int friends_added_index=0;
    int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friend);



        from_register_activity = getIntent();
        //client = (Client)from_register_activity.getSerializableExtra("Client");
        received_info_string = from_register_activity.getStringExtra("username");
        received_info_array = received_info_string.split(" ");
        username = received_info_array[0];
        friends_added[friends_added_index] = username;
        friends_added_index++;
        client = new Client();
        ThreadConnect = new ThreadConnect(client);
        ThreadSend = new ThreadSend(client);
        ThreadReceive = new ThreadReceive();
        ThreadConnect.startRunning();
        btnext = findViewById(R.id.btnext);
        search = findViewById(R.id.search);
        lista_layout = findViewById(R.id.lista);
        list = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, list);
        friend_list = new ArrayList<>();

        //lista_layout.setAdapter(adapter);



        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                if(count != 0 ){
                    adapter.clear();
                    adapter.notifyDataSetChanged();
                }
                //search.clearFocus();
                message = client.friend + query;
                if(!message.isEmpty()){
                    ThreadSend.setMessage(message);
                    ThreadSend.startRunning();
                    ThreadReceive.startRunning();
                }


                lista_layout.setAdapter(adapter);
                count++;
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {


                return false;
            }
        });



        search.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {

                //adapter.clear();
                return false;
            }
        });

        lista_layout.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = (String) parent.getItemAtPosition(position);
                //friends_added[friends_added_index++] = selectedItem;
                friend_list.add(selectedItem);

            }
        });

        btnext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String friend_add = "fad";
                String friend;
                String new_friends = "";
                message = friend_add + username + " " ;



                for(int i = 0 ; i < friend_list.size() ; i++){
                    friend = friend_list.get(i);
                    new_friends += friend + " ";
                    message += friend + " ";
                }

                if(!message.isEmpty()){
                    ThreadSend.setMessage(message);
                    ThreadSend.startRunning();
                    //ThreadReceive.startRunning();
                }

                //System.out.println("************************* " + received_info_array.length + " *******************************");


                if(received_info_array.length > 1){
                    for(int i = 1 ; i < received_info_array.length ; i++){
                            new_friends += received_info_array[i] + " ";
                    }
                }

                Intent nextscreen = new Intent(getApplicationContext(), CoversatioList.class);
                nextscreen.putExtra("username", username + " " + new_friends);
                startActivity(nextscreen);
                finish();

            }
        });






    }

    class ThreadReceive implements Runnable{

        Thread thread;
        String[] aux;

        @Override
        public void run(){

            try {

                server_resp = client.receiveMessage();
                System.out.println("recebeu do server" + server_resp);
                aux = server_resp.split(" ");


                if( !server_resp.equals(null)){
                    for(int i = 0; i < aux.length; i++){
                        if(aux[i] != null){
                            list.add(aux[i]);
                        }

                    }

                    System.out.println("recebeu do server");

                }

            } catch (IOException e) {
                e.printStackTrace();
            }


            thread.interrupt();
        }

        public void startRunning() {

            thread = new Thread (this);
            thread.start();
        }

        public void killThread(){
            thread.interrupt();
        }

    }


}
