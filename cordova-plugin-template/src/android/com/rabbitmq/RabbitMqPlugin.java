package com.example.rabbitmqplugin;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CordovaWebView;
import org.apache.cordova.PluginResult;
import org.apache.cordova.PluginResult.Status;
import org.json.JSONObject;
import org.json.JSONArray;
import org.json.JSONException;

import android.util.Log;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;

import java.util.Date;

public class RabbitMqPlugin extends CordovaPlugin {
  private static final String TAG = "Rabbit MQ Plugin";
  private String token=null;

  private SConsumer sConsumer = null;

  public void initialize(CordovaInterface cordova, CordovaWebView webView) {
    super.initialize(cordova, webView);

    Log.d(TAG, "Initializing RabbitMqPlugin");
  }

  public boolean execute(String action, JSONArray args, final CallbackContext callbackContext) throws JSONException {
    if(action.equals("setToken")) {
      this.token = args.getString(0);
      // Echo back the first argument
      Log.d(TAG, token);
    } else if(action.equals("startConsumer")) {
      
      Handler mHandler = messageHandler(callbackContext);
      sConsumer = new SConsumer(messageHandler());
      // Start consuming
      new consumer().execute();
      // An example of returning data back to the web layer
      final PluginResult result = new PluginResult(PluginResult.Status.OK, "Consumer Started");
      callbackContext.sendPluginResult(result);
    }else if(action.equals("stopConsumer")) {
      // Stopping consumer
      sConsumer.disconnect();
      // An example of returning data back to the web layer
      final PluginResult result = new PluginResult(PluginResult.Status.OK, "Consumer Stopped");
      callbackContext.sendPluginResult(result);
    }
    return true;
  }

  private Handler messageHandler(CallbackContext callbackContext){
        Handler handler = new Handler(){
            @Override
            public void handleMessage(Message msg){
                Log.d("MESSAGE", msg.obj.toString());
                String myMessage = msg.obj.toString();
                final PluginResult result = new PluginResult(PluginResult.Status.OK, myMessage);
                callbackContext.sendPluginResult(result);
            }
        };
        return handler;
    }

  private class consumer extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... stirngs) {
            try{
                sConsumer.consume(token);
            }catch (Exception e){
                e.printStackTrace();
            }
            return null;
        }
    }

}
