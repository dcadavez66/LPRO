package com.example.chatroom;

import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.IOException;
import java.util.ArrayList;

public class CoversatioList extends AppCompatActivity {


    FloatingActionButton addconversation;
    Toolbar toolbar;
    TextView public_chat;
    Client client = new Client();
    ThreadConnect ThreadConnect = null;
    ThreadSend ThreadSend = null;
    ThreadReceive ThreadReceive = null;
    Intent j;
    String username;
    String server_resp;
    //recebe as conversas que o utilizador tem
    String[] received = new String[10];
    ArrayList<String> friends_added = new ArrayList<String>();
    int k = 0;
    String update = "upd";
    private RecyclerView recview;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coversation_list);

        j = getIntent();
        username = j.getStringExtra("username");

        received = username.split(" ");
        username = received[0];

        ThreadConnect = new ThreadConnect(client);
        ThreadReceive = new ThreadReceive();
        ThreadConnect.startRunning();


        toolbar = findViewById(R.id.tool);
        setSupportActionBar(toolbar);


        //k = friends_added.size();

        for(int i = 1 ; i < received.length ; i++){

            friends_added.add(received[i]);
            //k++;
        }
        addconversation = findViewById(R.id.addconversation);
        public_chat = findViewById(R.id.public_channel);
        recview = findViewById(R.id.recview);
        recview.setHasFixedSize(true);

        mAdapter = new MyAdapter(friends_added , username, client);
        recview.setAdapter(mAdapter);

        //layoutManager = new LinearLayoutManager(this);
        //recview.setLayoutManager(layoutManager);

        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recview.setLayoutManager(llm);

        recview.addItemDecoration(new VerticalSpacingDecoration(35));
        /*recview.addItemDecoration(
                new DividerItemDecoration(ContextCompat.getDrawable(getApplicationContext(),
                        R.drawable.item_decorator)));*/

        public_chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent gotoconversation = new Intent( getApplicationContext() ,TesteActivity.class );
                gotoconversation.putExtra("user",username + " " + public_chat.getText().toString().trim());
                startActivity(gotoconversation);
            }
        });

        addconversation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String old_friends = "";
                for(int i = 0 ; i < friends_added.size(); i++){

                    old_friends += friends_added.get(i) + " ";
                }
                Intent newconversation = new Intent(getApplicationContext(), AddFriendActivity.class);
                newconversation.putExtra("username", username + " " + old_friends );
                startActivity(newconversation);
                finish();

            }
        });



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_conversation_list , menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){

        //ThreadConnect.startRunning();
        ThreadSend = new ThreadSend(client);
        String log_out_message = "out";

        switch (item.getItemId()) {
            case R.id.action_settings:

                ThreadSend.setMessage(log_out_message);
                ThreadSend.startRunning();
                Intent gobacktologin = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(gobacktologin);
                finish();

                return true;

            default:
                return super.onOptionsItemSelected(item);

        }
    }


    public class DividerItemDecoration extends RecyclerView.ItemDecoration {

        private Drawable mDivider;

        public DividerItemDecoration(Drawable divider) {
            this.mDivider = divider;
        }

        @Override
        public void onDrawOver(Canvas canvas, RecyclerView parent, RecyclerView.State state) {
            final int left = parent.getPaddingLeft();
            final int right = parent.getWidth() - parent.getPaddingRight();

            final int childCount = parent.getChildCount();
            for (int i = 0; i < childCount; i++) {
                final View child = parent.getChildAt(i);
                final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams)
                        child.getLayoutParams();
                final int top = child.getBottom() + params.bottomMargin;
                final int bottom = top + mDivider.getIntrinsicHeight();
                mDivider.setBounds(left, top, right, bottom);
                mDivider.draw(canvas);
            }
        }
    }


    public class VerticalSpacingDecoration extends RecyclerView.ItemDecoration {

        private int spacing;

        public VerticalSpacingDecoration(int spacing) {
            this.spacing = spacing;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view,
                                   RecyclerView parent,
                                   RecyclerView.State state) {
            outRect.bottom = spacing;
        }
    }

    class ThreadReceive implements Runnable{

        Thread thread;
        String[] aux;


        @Override
        public void run(){

            try {

                server_resp = client.receiveMessage();
                System.out.println("recebeu do server" + server_resp);
                //received = server_resp.split(" ");
                aux = server_resp.split(" ");
                for(int i = 0 ; i < aux.length ; i++){
                    friends_added.add(aux[i]);
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

    @Override
    public void onBackPressed() {
        return;
    }



}
