package com.example.chatroom;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;

public class Register_Activity extends AppCompatActivity {


    Intent i;
    Button btnext;
    TextView Wmsg;
    EditText firstname;
    EditText lastname;
    EditText username;
    EditText myemail;
    EditText password;
    String ctrl_string;
    String username_out;
    String firstname_out;
    String lastname_out;
    String email_out;
    String pass_out;
    String server_resp = null;
    int n_info = 0;
    String finalmessage;
    String register = "reg";
    Client client;
    ThreadConnect Threadconnect = null;
    ThreadSend ThreadSend = null;
    ThreadReceive ThreadReceive = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_);

         i = getIntent();
         //client = (Client)i.getSerializableExtra("Client");
         client = new Client();
         firstname = findViewById(R.id.firstname);
         lastname = findViewById(R.id.lastname);
         username = findViewById(R.id.username);
         myemail = findViewById(R.id.myemail);
         password = findViewById(R.id.mypass);
         btnext = findViewById(R.id.btnext);
         Wmsg = findViewById(R.id.wmsg);
         Threadconnect = new ThreadConnect(client);
         Threadconnect.startRunning();
         ThreadSend = new ThreadSend(client);
         ThreadReceive = new ThreadReceive();




        btnext.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                System.out.println("INICIO DO REG");
                ctrl_string = register;

                //primeiro parametro a ser enviado é o username para verificar logo se existe!

                //user_info[n_info] = username.getText().toString().trim();
                username_out = username.getText().toString().trim();
                n_info++;

                //user_info[n_info] = firstname.getText().toString().trim();
                firstname_out = firstname.getText().toString().trim();
                n_info++;

                //user_info[n_info] = lastname.getText().toString().trim();
                lastname_out = lastname.getText().toString().trim();
                n_info++;

                //user_info[n_info] = myemail.getText().toString().trim();
                email_out = myemail.getText().toString().trim();
                n_info++;

                //user_info[n_info] = password.getText().toString().trim();
                pass_out = password.getText().toString().trim();
                n_info++;



                finalmessage = ctrl_string + username_out + " " + firstname_out + " " + lastname_out + " " + email_out + " " + pass_out;

                System.out.println(finalmessage);

                if(!finalmessage.isEmpty()){
                    ThreadSend.setMessage(finalmessage);
                    ThreadSend.startRunning();
                    ThreadReceive.startRunning();
                }

            }
        });


    }



    class ThreadReceive implements Runnable{

        Thread thread;
        @Override
        public void run(){

            try {
                System.out.println("antes recebido");
                server_resp = client.receiveMessage();
                System.out.println(server_resp + "depois");
            } catch (IOException e) {
                e.printStackTrace();
            }

            if(server_resp.equals(client.ack)){

                Intent nextScreen_add_friend = new Intent(getApplicationContext(), AddFriendActivity.class);
                nextScreen_add_friend.putExtra("username", username_out);
                startActivity(nextScreen_add_friend);


            }
            else if(server_resp.equals(client.nack)){

                Wmsg.setText("this username isn't valid!");

            }

            thread.interrupt();
        }

        public void startRunning() {

            thread = new Thread (this);
            thread.start();
        }

    }




}
