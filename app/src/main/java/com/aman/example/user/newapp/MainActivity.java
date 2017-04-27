package com.aman.example.user.newapp;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import com.nostra13.universalimageloader.utils.L;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements ServiceConnection {
    EditText editText;
    ListView listView;
    ImageView imageView;
    ArrayList<Items> arrayList;
    private final ServiceConnection mConnection = this;
    private Messenger mServiceMessenger = null;
    private boolean mIsBound;
    CustomAdapter customAdapter;
    Message msg;
    private final Messenger mMessenger = new Messenger(new IncomingMessageHandler());
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editText = (EditText)findViewById(R.id.editText);
        listView = (ListView)findViewById(R.id.listview);
        imageView =(ImageView)findViewById(R.id.img);
//        Items items = new Items(getIntent().getStringExtra("rcvmsg"),editText.getText().toString());
//        items.setReceivedmsg(getIntent().getStringExtra("rcvmsg"));

            arrayList =new ArrayList<>();

            customAdapter =new CustomAdapter(this,arrayList);
            listView.setAdapter(customAdapter);



        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                arrayList.add(new Items(editText.getText().toString()));
                new Items(editText.getText().toString()).getSendmsg();
               // new Items(getIntent().getStringExtra("rcvmsg"),getIntent().getStringExtra("rcvmsg")).getReceivedmsg();
                String string = editText.getText().toString();
                sendToService(string);

                customAdapter.notifyDataSetChanged();
            }
        });
        doBind();
    }
    private void sendToService( String message)
    {
        if (mIsBound)
        {
            if (mServiceMessenger != null)
            {
                try
                {
                    // Send data as a String
                    Bundle bundle = new Bundle();
                    bundle.putString("message", message);
                    android.os.Message msg = android.os.Message.obtain(null, 3);
                    msg.setData(bundle);
                    mServiceMessenger.send(msg);
                }
                catch (Exception e)
                {
                    System.out.println(e.toString());
                }

            }
        }
    }
    private void doBind() {
        // Client Bind to Service, Where  BIND_AUTO_CREATE is to create the service if it's not already alive.
        Intent intent = new Intent(this,Chatservice.class);
        //Connect to an application service
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
        mIsBound = true;
        sendMessageToService(0);
    }
    private void doUnBind() {
        if (mIsBound)
        {
            if (mServiceMessenger != null)
            {
                try
                {
                    android.os.Message msg = android.os.Message.obtain(null, Chatservice.MSG_UNREGISTER_CLIENT);
                    msg.replyTo = mMessenger;
                    mServiceMessenger.send(msg);
                }
                catch (RemoteException e)
                {
                    // There is nothing special we need to do if the service has crashed.
                }
            }
            unbindService(mConnection);
            mIsBound = false;
        }
    }
    private class IncomingMessageHandler extends Handler
    {
        @Override
        public void handleMessage(Message msg)
        {
            switch (msg.what)
            {
                case Chatservice.MSG_SET_STRING_VALUE:
                    break;
                case Chatservice.CHAT_RECEIVE:
                    //contentArrayList.clear();
                    updateChat(msg.getData().getString("mll"));


                case 5:
                    //contentArrayList.clear();
//                    updateChat(msg.getData().getString("userid") , msg.getData().getString("messageid") , msg.getData().getInt("type") , msg.getData().getString("username") ,msg.getData().getString("message") , msg.getData().getString("time"));
                    break;

                default:
                    super.handleMessage(msg);
            }
        }
    }
    private void updateChat(String message)
    {
       // arrayList.add(new Items(message,editText.getText().toString()));
       try {
           Items items = new Items(message, null);
           items.setReceivedmsg(message);
           arrayList.add(items);
           items.getReceivedmsg();
           customAdapter.notifyDataSetChanged();
           Log.e("aman", message);
       }catch (Exception e)
       {
           Log.e("rcv error", String.valueOf(e));
       }
    }
    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        mServiceMessenger = new Messenger(service);
        try
        {
            android.os.Message msg = android.os.Message.obtain(null, Chatservice.MSG_REGISTER_CLIENT);
            msg.replyTo = mMessenger;
            mServiceMessenger.send(msg);
        }
        catch (RemoteException e)
        {
            // In this case the service has crashed before we could even do anything with it
        }
        sendMessageToService(0);
    }
    private void sendMessageToService(int intvaluetosend)
    {
        if (mIsBound)
        {
            if (mServiceMessenger != null)
            {
                try
                {
                    Message msg = Message.obtain(null, 10, intvaluetosend, 0);
                    msg.replyTo = mMessenger;
                    mServiceMessenger.send(msg);
                }
                catch (RemoteException e)
                {
                    Log.e("BIND", e.toString());
                }
            }
        }
    }
    @Override
    public void onServiceDisconnected(ComponentName name) {
        mServiceMessenger = null;
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        doUnBind();
    }
}
