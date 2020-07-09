package com.example.chatroom;

import android.content.Intent;
import android.content.res.Configuration;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import java.io.IOException;

//import com.google.android.material.floatingactionbutton.FloatingActionButton;
//import com.google.android.material.snackbar.Snackbar;
//import androidx.appcompat.widget.Toolbar;




public class MainActivity extends AppCompatActivity {


    Client client ;
    EditText username;
    EditText password;
    TextView resposta;
    Button btLogIn;
    String message;
    String ctrl_string;
    String[] user_info = new String[2] ;
    String username_size;
    String password_size;
    String server_resp = null;
    //Thread1 Thread1 = null;
    ThreadConnect ThreadConnect = null;
    ThreadSend ThreadSend = null;
    ThreadReceive ThreadReceive = null;
    //WifiManager wifimanager = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
    //int ipAddress = wifimanager.getConnectionInfo().getIpAddress();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        client = new Client();
        //client.self_ip = ipAddress;
        username = findViewById(R.id.input_username);
        password = findViewById(R.id.input_password);
        resposta = findViewById(R.id.resposta);
        btLogIn = findViewById(R.id.btLogIn);
        ThreadConnect = new ThreadConnect(client);
        ThreadSend = new ThreadSend(client);
        ThreadReceive = new ThreadReceive();
        ThreadConnect.startRunning();


        //Toolbar toolbar = findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        btLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ctrl_string = client.signin;
                user_info[0] = username.getText().toString().trim();
                user_info[1] = password.getText().toString().trim();
                message = ctrl_string + user_info[0] + " " + user_info[1]  ;
                System.out.println(message);

                if( user_info[0].isEmpty() || user_info[1].isEmpty() ){

                    Toast.makeText(v.getContext(), "Insert Username and Password!" , Toast.LENGTH_SHORT).show();
                    //resposta.setText("Insert Username and Password!");

                }
                else{
                    if(!message.isEmpty() ){
                        ThreadSend.setMessage(message);
                        ThreadSend.startRunning();
                        ThreadReceive.setView(v);
                        ThreadReceive.startRunning();
                    }

                }
                //message = ctrl_string + username.getText().toString().trim();




            }
        });


        Button btregister1 = findViewById(R.id.btregister);
        btregister1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                try {
                    client.stopConnection();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                //Intent nextScreen = new Intent(getApplicationContext(), Register_Activity.class);
                Intent nextScreen = new Intent(getApplicationContext(), Register_Activity.class);

                startActivity(nextScreen);

            }
        });



    }

    /*public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        // Checks the orientation of the screen
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {


        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){

        }
    }*/



    class ThreadReceive implements Runnable{

        Thread thread;
        View view;
        @Override
            public void run(){

            try {
                server_resp = client.receiveMessage();
            } catch (IOException e) {
                e.printStackTrace();
            }

            if(!server_resp.equals(client.nack)){


               // Toast.makeText(view.getContext(), "Welcome" , Toast.LENGTH_SHORT).show();
                resposta.setText("Welcome");
                System.out.println("***********" + server_resp + "********");

                Intent nextScreenteste = new Intent(getApplicationContext(), CoversatioList.class);
                //nextScreenteste.putExtra("user",user_info[0]);
                nextScreenteste.putExtra("username",user_info[0]+" " + server_resp);
                startActivity(nextScreenteste);
                finish();

            }
            else if(server_resp.equals(client.nack)){

                resposta.setText("Try Again");
                //Toast.makeText(view.getContext(), "Try Again" , Toast.LENGTH_SHORT).show();


                //resposta.setText(" ");
            }

            thread.interrupt();
        }

        public void startRunning() {

            thread = new Thread (this);
            thread.start();
        }
        public void setView(View view){
            this.view = view;
        }

    }






    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
