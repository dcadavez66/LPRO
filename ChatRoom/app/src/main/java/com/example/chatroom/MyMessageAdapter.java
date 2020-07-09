package com.example.chatroom;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import static android.view.View.*;

public class MyMessageAdapter extends RecyclerView.Adapter<MyMessageAdapter.MyViewHolder_sent> {
    private ArrayList<String> mDataset;
    protected static String user_send;
    protected static String user_rcv;
    protected static boolean conv;

    public static class MyViewHolder_sent extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final Context context;

        public TextView textView;

        public MyViewHolder_sent(Context context, View v) {
            super(v);
            textView = v.findViewById(R.id.text_message_body);
            this.context = context;
            v.setOnClickListener(this);


        }


       @Override
        public void onClick(View view) {
           /* int position = getAdapterPosition(); // gets item position
            if (position != RecyclerView.NO_POSITION) { // Check if an item was deleted, but the user clicked it before the UI removed it


                Toast.makeText(context, textView.getText(), Toast.LENGTH_SHORT).show();
                Intent gotoconversation = new Intent( context.getApplicationContext() ,TesteActivity.class );
                gotoconversation.putExtra("user",user_send + " " + textView.getText().toString().trim());
                context.startActivity(gotoconversation);

            }*/
        }



    }



    // Provide a suitable constructor (depends on the kind of dataset)
    public MyMessageAdapter(ArrayList myDataset , String user_send , String user_rcv , boolean conv){
        mDataset = myDataset;
        this.user_send = user_send;
        this.user_rcv = user_rcv;
        this.conv = conv;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public MyMessageAdapter.MyViewHolder_sent onCreateViewHolder(ViewGroup parent, int viewType ) {

        String msg = mDataset.get(getItemCount()-1);
        String[] msg_split = msg.split(" ", 3);
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        System.out.println("user_send = " + user_send);
        MyViewHolder_sent myViewHolder = null;
        System.out.println("valor de conv:" + conv);
        if(msg_split[0].equals(user_send)){
            System.out.println("minha mensagem ");
            View nameView = inflater.inflate(R.layout.item_message_sent, parent, false);
             myViewHolder = new MyViewHolder_sent(context, (nameView));
            return myViewHolder;
        }
        else{
            if(msg_split[0].equals(user_rcv) && msg_split[1].equals(user_send) && conv == false ){
                System.out.println(" recetor certo ");
                View nameView = inflater.inflate(R.layout.item_message_received, parent, false);
                 myViewHolder = new MyViewHolder_sent(context, (nameView));
                return myViewHolder;
            }
            else if(conv == true){
                System.out.println(" canal publico ");
                View nameView = inflater.inflate(R.layout.item_message_received, parent, false);
                 myViewHolder = new MyViewHolder_sent(context, (nameView));
                return myViewHolder;
            }

        }
        System.out.println(" nenhum dos outros");
        View nameView = inflater.inflate(R.layout.item_blank, parent, false);
        myViewHolder = new MyViewHolder_sent(context, (nameView));
        return myViewHolder ;


    }


    @Override
    public void onBindViewHolder(MyViewHolder_sent holder, int position) {

        String message = mDataset.get(position);
        String[] message_split = message.split(" " , 3);
        TextView tv = holder.textView;
        tv.setText(message_split[0] + ":" + message_split[2]);


    }


    @Override
    public int getItemCount() {
        return mDataset.size();
    }



}


