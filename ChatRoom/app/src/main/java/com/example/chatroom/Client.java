package com.example.chatroom;

import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.text.format.Formatter;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.io.Serializable;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import static android.content.Context.WIFI_SERVICE;


public class Client  {

    protected Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;


     int port = 6666;
<<<<<<< Updated upstream
     String IP = "192.168.1.66";
=======
     String IP = "10.227.144.233";
>>>>>>> Stashed changes
     String signin = "sin";
     String ack = "ack";
     String nack = "nack";
     String send = "snd";
     String friend = "frd";
     String newconversation = "ncv";
     String update = "upd";
     String getMsg = "gms";
     String username ;
     int self_ip = 0;


    // static String[] identified_message = new String[2];

    public void startConnection() throws UnknownHostException, IOException {

        //String IP = getAddress();
        //System.out.println("Public IP Address: " + IP +"\n");
        //int port = 6666;
        //clientSocket = new Socket( IP, port);
        clientSocket = new Socket( IP , port);
        out = new PrintWriter(clientSocket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        //out.println(self_ip);



    }

    public static String getAddress() {

        String address = "";
        try {
            Socket socket = new Socket();
            socket.connect(new InetSocketAddress("google.com", 80));
            address = socket.getLocalAddress().getHostAddress();
            socket.close();
        }
        catch(Exception e){
            System.out.println("Failed to get local address");
        }
        return address;
    }

    public String sendMessage(String[] msg) throws IOException {
        out.println(msg);
        //String resp = in.readLine();
        return null;
    }

    public String sendMessage(String msg) throws IOException {
        out.println(msg);
        //String resp = in.readLine();
        return null;
    }

    public String receiveMessage() throws IOException {

        String resp = in.readLine();
        return resp;
    }



    public void stopConnection() throws IOException {
        in.close();
        out.close();
        clientSocket.close();
    }


    /*public static void main(String[] args) throws IOException {

        Scanner my_in = new Scanner(System.in);
        Client client = new Client();
        client.startConnection("192.168.56.1", 6666);

        System.out.println("Whats your username?\n");

        client.sendMessage(signin);
        username = my_in.nextLine();
        client.sendMessage(username);


        System.out.println("Type in your message:");

        while(true)
        {
            client.sendMessage(send);
            System.out.println("you better start typing motherfucker!\n");
            input = my_in.nextLine();
            client.sendMessage(username);
            client.sendMessage(input);

        }




    }*/

}

