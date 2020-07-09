package com.example.chatroom;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.chatroom.R;

public class SentMessageHolder extends RecyclerView.ViewHolder {
    TextView messageText, timeText, nameText;
    ImageView profileImage;

    SentMessageHolder(View itemView) {
        super(itemView);
        //messageText = (TextView) itemView.findViewById(R.id.text_message_body);
        //timeText = (TextView) itemView.findViewById(R.id.text_message_time);
        //nameText = (TextView) itemView.findViewById(R.id.text_message_name);
        //profileImage = (ImageView) itemView.findViewById(R.id.image_message_profile);
        messageText = (TextView) itemView.findViewById(R.id.person_name);
    }

    void bind(String message ) {

        String[] message_split = message.split(" ");
        messageText.setText(message_split[2]);

        // Format the stored timestamp into a readable String using method.
        //timeText.setText(Utils.formatDateTime(message.getCreatedAt()));
        //nameText.setText(message_split[1]);

        // Insert the profile image from the URL into the ImageView.
        //Utils.displayRoundImageFromUrl(mContext, message.getSender().getProfileUrl(), profileImage);



    }
}
