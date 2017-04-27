package com.aman.example.user.newapp;

/**
 * Created by User on 26-04-2017.
 */

public class Items {
    private  String receivedmsg;
    private  String sendmsg;



    public String getReceivedmsg() {
        return receivedmsg;
    }

    public void setReceivedmsg(String receivedmsg) {
        this.receivedmsg = receivedmsg;
    }

    public String getSendmsg() {
        return sendmsg;
    }

    public void setSendmsg(String sendmsg) {
        this.sendmsg = sendmsg;
    }

    public Items(String receivedmsg, String sendmsg) {
        this.receivedmsg = receivedmsg;
        this.sendmsg = sendmsg;
    }

    public Items(String  sendmsg)
    {
        this.sendmsg = sendmsg;
    }

}
