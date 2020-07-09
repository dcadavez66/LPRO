package com.example.chatroom;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;


public class RabbitMQ {

    ConnectionFactory factory ;
    private BlockingDeque<String> queue = new LinkedBlockingDeque<String>();
    String user_send;
    String user_receive;
    String channel_name;

    public RabbitMQ(String user_send , String user_receive){
            this.user_send = user_send;
            this.user_receive = user_receive;
            int check_alpha = user_send.compareTo(user_receive);
            if(check_alpha < 0){

                channel_name = user_receive + "_" + user_send;
                System.out.println("channel_name:" + channel_name);
            }
            else{
                channel_name = user_send + "_" + user_receive;
                System.out.println("channel_name:" + channel_name);
            }
    }

    void publishMessage(String message) {
        try {
            Log.d("", "[q] " + message);
            queue.putLast(message);

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void setupConnectionFactory(ConnectionFactory factory ) {
        this.factory = factory;
        String uri = "amqp://pgtokvgm:1Q58JvBDqea8VmExNCiMUFbjE4o5jqCS@stingray.rmq.cloudamqp.com/pgtokvgm";


        try {
            factory.setAutomaticRecoveryEnabled(false);
            factory.setUri(uri);
        } catch (KeyManagementException | NoSuchAlgorithmException | URISyntaxException e1) {
            e1.printStackTrace();
        }
    }


    public void publishToAMQP() {
        Thread publishThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Connection connection = factory.newConnection();
                        Channel ch = connection.createChannel();
                        //ch.exchangeDeclare(channel_name , "direct" , true , false , false , null);
                       // ch.queueDeclare("msg" , true , false , false , null );
                        //ch.queueBind("msg" , channel_name , null );
                        ch.confirmSelect();

                        while (true) {
                            String message = queue.takeFirst();
                            try {
                                ch.basicPublish("amq.fanout", "chat", null, message.getBytes());
                                //ch.basicPublish(channel_name, "chat", null, message.getBytes());
                                Log.d("", "[s] " + message);
                                ch.waitForConfirmsOrDie();
                            } catch (Exception e) {
                                Log.d("", "[f] " + message);
                                queue.putFirst(message);
                                throw e;
                            }
                        }
                    } catch (InterruptedException e) {
                        break;
                    } catch (Exception e) {
                        Log.d("", "Connection broken: " + e.getClass().getName());
                        try {
                            Thread.sleep(5000); //sleep and then try again
                        } catch (InterruptedException e1) {
                            break;
                        }
                    }
                }
            }
        });
        publishThread.start();
    }


    void subscribe(final Handler handler) {
        Thread subscribeThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Connection connection = factory.newConnection();
                        Channel channel = connection.createChannel();
                        channel.basicQos(1);
                        AMQP.Queue.DeclareOk q = channel.queueDeclare();
                        //AMQP.Queue.DeclareOk q = channel.queueDeclare("msg" , true , false , false , null);
                        channel.queueBind(q.getQueue(), "amq.fanout", "chat");
                        //channel.queueBind(q.getQueue() , channel_name , "chat");
                        QueueingConsumer consumer = new QueueingConsumer(channel);
                        channel.basicConsume(q.getQueue(), true, consumer);
                        //channel.basicConsume("msg", true, consumer);

                        // Process deliveries
                        while (true) {
                            QueueingConsumer.Delivery delivery = consumer.nextDelivery();

                            String message = new String(delivery.getBody());
                            Log.d("", "[r] " + message);

                            Message msg = handler.obtainMessage();
                            Bundle bundle = new Bundle();

                            bundle.putString("msg", message);
                            msg.setData(bundle);
                            handler.sendMessage(msg);
                        }
                    } catch (InterruptedException e) {
                        break;
                    } catch (Exception e1) {
                        Log.d("", "Connection broken: " + e1.getClass().getName());
                        try {
                            Thread.sleep(4000); //sleep and then try again
                        } catch (InterruptedException e) {
                            break;
                        }
                    }
                }
            }
        });
        subscribeThread.start();


    }

}


