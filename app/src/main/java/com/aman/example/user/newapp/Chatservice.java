package com.aman.example.user.newapp;

import android.Manifest;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import org.jivesoftware.smack.packet.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import com.nostra13.universalimageloader.utils.L;
import com.vdurmont.emoji.Emoji;
import com.vdurmont.emoji.EmojiManager;
import com.vdurmont.emoji.EmojiParser;

import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.ConnectionListener;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.PresenceListener;
import org.jivesoftware.smack.ReconnectionManager;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.StanzaListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.filter.MessageTypeFilter;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.packet.Stanza;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;
import org.jivesoftware.smackx.xevent.MessageEventManager;
import org.jivesoftware.smackx.xevent.MessageEventNotificationListener;
import org.jivesoftware.smackx.xevent.MessageEventRequestListener;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by User on 26-04-2017.
 */

public class Chatservice extends Service implements StanzaListener, MessageListener, PresenceListener {
    private final String SMACK="SMACK";
    private final List<Messenger> mClients = new ArrayList<>();
    public static final int MSG_REGISTER_CLIENT = 1;
    public static final int MSG_UNREGISTER_CLIENT = 2;
    public static final int MSG_SET_STRING_VALUE = 3;
    public static final int CHAT_RECEIVE = 203;
    private final Messenger mMessenger = new Messenger(new serviceHandle());
    private static final String TAG ="ServiceExample" ;
    public static Boolean mIsServiceRunning = false;
    private static XMPPTCPConnection connection;
    private MessageEventManager m;
    private final ArrayList<String> notDeliverd = new ArrayList<>();

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate");
        XMPPTCPConnectionConfiguration config = XMPPTCPConnectionConfiguration
                .builder()
                .setHost("139.59.27.47")
                .setPort(5222)
                .setServiceName("localhost")
                .setSecurityMode(ConnectionConfiguration.SecurityMode.disabled)
                .setDebuggerEnabled(false)
                .build();
        connection=new XMPPTCPConnection(config);


        connection.setUseStreamManagement(true);
        connection.setUseStreamManagementResumption(true);
        connection.addAsyncStanzaListener(this, MessageTypeFilter.CHAT);
        XMPPTCPConnection.setUseStreamManagementDefault(true);
        new  connections().execute("ananth","ananth");

        connection.addConnectionListener(new ConnectionListener() {
            @Override
            public void connected(XMPPConnection xmppConnection) {

            }

            @Override
            public void authenticated(XMPPConnection xmppConnection, boolean b) {

            }

            @Override
            public void connectionClosed() {

            }

            @Override
            public void connectionClosedOnError(Exception e) {
                Log.e(SMACK, "Connection cloesd" + e.toString());
            }

            @Override
            public void reconnectionSuccessful() {

            }

            @Override
            public void reconnectingIn(int i) {

            }

            @Override
            public void reconnectionFailed(Exception e) {
                Log.e(SMACK, "Reconnection Failed");
            }
        });
        ReconnectionManager manager= ReconnectionManager.getInstanceFor(connection);
        manager.enableAutomaticReconnection();

        m= MessageEventManager.getInstanceFor(connection);
        m.addMessageEventNotificationListener(new MessageEventNotificationListener() {
            @Override
            public void deliveredNotification(String s, String s1) {

            }

            @Override
            public void displayedNotification(String s, String s1) {

            }

            @Override
            public void composingNotification(String s, String s1) {

            }

            @Override
            public void offlineNotification(String s, String s1) {

            }

            @Override
            public void cancelledNotification(String s, String s1) {

            }
        });

        m.addMessageEventRequestListener(new MessageEventRequestListener() {
            @Override
            public void deliveredNotificationRequested(String s, String s1, MessageEventManager messageEventManager) throws SmackException.NotConnectedException {

            }

            @Override
            public void displayedNotificationRequested(String s, String s1, MessageEventManager messageEventManager) {

            }

            @Override
            public void composingNotificationRequested(String s, String s1, MessageEventManager messageEventManager) {

            }

            @Override
            public void offlineNotificationRequested(String s, String s1, MessageEventManager messageEventManager) {

            }
        });
    }
    private class connections extends AsyncTask<String, Void, String>
    {
        @Override
        protected String doInBackground(String... params)
        {
            try
            {
                connection.connect();
                connection.login(params[0],params[1]);
            }
            catch(Exception e)
            {
                Log.e("test" , e.toString()+"COnnection");
            }
            return "";
        }
    }
    private void sendsosreqtoecontacts(String to)
    {
        Log.e("test" , to+"1111");

        final org.jivesoftware.smack.packet.Message message = new org.jivesoftware.smack.packet.Message("9443153157@localhost", org.jivesoftware.smack.packet.Message.Type.chat);
        message.setSubject("SOS");
        message.setBody(to);
        try
        {
            notDeliverd.add(message.getStanzaId());
            // startHandler(message , to);
            connection.sendStanza(message);

        }
        catch(Exception e)
        {
            Log.e("smackError" , e.toString());
        }
    }
    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        Toast.makeText(this, "ServiceExample Started", Toast.LENGTH_LONG).show();
        Log.d(TAG, "onStart");
    }
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mMessenger.getBinder();
    }

    @Override
    public void processMessage(org.jivesoftware.smack.packet.Message message) {

    }

    @Override
    public void processPresence(Presence presence) {

    }

    @Override
    public void processPacket(Stanza stanza) throws SmackException.NotConnectedException {
        Message msg = (Message) stanza;

        String message = msg.getBody();
//             Intent in = new Intent(this,MainActivity.class);
//             in.putExtra("rcvmsg",msg.getBody());
        updateUI(message);
//        Bundle bundle = new Bundle();
//        bundle.putString("mll",message);
//        android.os.Message mssg = android.os.Message.obtain(null,CHAT_RECEIVE);
//        mssg.setData(bundle);
//        try {
//            mClients.get(0).send(mssg);
//        } catch (RemoteException e) {
//            e.printStackTrace();
//        }
        //Log.e("aman",message);

    }
    private void updateUI( String message)
    {
        Iterator<Messenger> messengerIterator = mClients.iterator();
        while(messengerIterator.hasNext())
        {
            Messenger messenger = messengerIterator.next();
            try
            {
                Bundle bundle = new Bundle();
                bundle.putString("mll",message);
                android.os.Message msg = android.os.Message.obtain(null,CHAT_RECEIVE);
//                Items items = new Items(message,msg.getData().getString("message"));
//                items.setReceivedmsg(message);
//                items.getReceivedmsg();
                msg.setData(bundle);
                mClients.get(0).send(msg);
               // messenger.send(msg);
            }
            catch (RemoteException e)
            {
                mClients.remove(messenger);
                Log.e("rcv error", String.valueOf(e));
            }
        }
    }

    class serviceHandle extends Handler
     {
         @Override
         public void handleMessage(android.os.Message msg) {

         switch (msg.what)
            {
                case MSG_REGISTER_CLIENT:
                    mClients.add(msg.replyTo);
                    break;
                case MSG_UNREGISTER_CLIENT:
                    mClients.remove(msg.replyTo);
                    break;
                case MSG_SET_STRING_VALUE :
                   // new Items(msg.getData().getString("message"),msg.getData().getString("message")).setReceivedmsg(msg.getData().getString("message"));
                   new Items(msg.getData().getString("message")).setSendmsg(msg.getData().getString("message"));
                    sendsosreqtoecontacts(msg.getData().getString("message"));
                    break;
//                   Log.e("service",msg.getData().getString("message"));


                default:
                    super.handleMessage(msg);
             }
             super.handleMessage(msg);
         }
     }
    @Override
    public void onDestroy() {
        super.onDestroy();
        Toast.makeText(this, "ServiceExample Destroyed", Toast.LENGTH_LONG).show();
        Log.d(TAG, "onDestroy");
        mIsServiceRunning=false;
    }

}
