package com.example.rabbitmqplugin;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.rabbitmq.client.*;

import org.json.JSONException;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class SConsumer extends CreateChannel{

    private static final String[] message = new String[1];
    private String rabbitQueue = null;
    private Handler mHandler = null;
    //private Consumer mConsumer = null;
    private String mToken = null;

    public SConsumer(Handler handler){
        this.mHandler = handler;
    }

    private void getMyQueue(){
        this.rabbitQueue = httpRequest(mToken,"http://zastel.com/api/v1/getMyQueue");
    }

    private void connect(){
        try{
            createConnection(mToken);
            getMyQueue();
        } catch (IOException|TimeoutException|JSONException e){
            e.printStackTrace();
        }
    }

    public void disconnect(){
        try{
            closeConnection();
        } catch (IOException|TimeoutException e){
            e.printStackTrace();
        }
    }

    public void consume(String token){
        if(comChannel == null){
            mToken = token;
            connect();
            while(comChannel == null){
            }
        }
        Log.d("CONSUMER", "Got the connection channel");
        try{
            Consumer mConsumer = new DefaultConsumer(comChannel) {
                @Override
                public void handleDelivery(String consumerTag, Envelope envelope,
                                           AMQP.BasicProperties properties, byte[] body) throws IOException {
                    if (body != null) {
                        message[0] = new String(body, "UTF-8");
                        Message msg = new Message();
                        msg.obj = message[0];
                        mHandler.sendMessage(msg);
                    }
                }
            };
            comChannel.basicConsume(rabbitQueue, true, mConsumer);
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
