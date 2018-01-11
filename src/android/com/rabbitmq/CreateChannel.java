package com.example.rabbitmqplugin;

import android.util.Log;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import org.json.JSONException;
import org.json.JSONObject;

public class CreateChannel {
    protected static Channel comChannel=null;

    protected void createConnection(String token) throws IOException, TimeoutException, JSONException{

        String stringCredentials = httpRequest(token, "http://zastel.com/api/v1/getRabbitCredentials");
        JSONObject credentials = new JSONObject(stringCredentials);
        String host = credentials.getString("host");
        String username = credentials.getString("username");
        String password = credentials.getString("password");
        String vHost = credentials.getString("vhost");
        String port = credentials.getString("port");


        if(comChannel == null){
            ConnectionFactory connectionFactory = new ConnectionFactory();
            connectionFactory.setHost(host);
            connectionFactory.setPort(Integer.parseInt(port));
            connectionFactory.setVirtualHost(vHost);
            connectionFactory.setUsername(username);
            connectionFactory.setPassword(password);
            Connection conn = connectionFactory.newConnection();

            comChannel = conn.createChannel();

            Log.d("CHANNEL", "Channel Created!");
        }
    }

    protected String httpRequest(String token, String stringUrl){
        HttpURLConnection httpCon = null;
        String credentials = null;
        try {
            //String stringUrl = "http://zastel.com/api/v1/getRabbitCredentials";
            URL url = new URL(stringUrl);
            httpCon = (HttpURLConnection) url.openConnection();

            httpCon.setRequestMethod("GET");
            httpCon.setRequestProperty("x-access-token", token);

            DataInputStream in = new DataInputStream(httpCon.getInputStream());
            String output;
            StringBuffer response = new StringBuffer();
            while ((output = in.readLine()) != null) {
                response.append(output);
            }
            in.close();

            credentials = String.valueOf(response);
            //jsonObject = new JSONObject(credentials);
        }catch (Exception e){
            e.printStackTrace();
        } finally {
            if (httpCon != null){
                httpCon.disconnect();
            }
            return credentials;
        }
    }

    protected void closeConnection() throws IOException, TimeoutException{
        comChannel.close();
    }
}
