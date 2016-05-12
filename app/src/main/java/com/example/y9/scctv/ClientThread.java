package com.example.y9.scctv;

/*
 * Created by y9 on 2016-03-20.
 */

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ClientThread  extends Thread{
    static DataOutputStream outputStream;
    static Socket socket;
    private SendService sendService;
    private ReceiverService receiverService;
    private String name;

    public ClientThread(Handler handler, String ip, int port, String name){
    //
        try{
            socket = new Socket(ip, port);
        }catch (IOException e){
            e.printStackTrace();
        }
        /*
        sendService = new SendService();
        sendService.start();
        receiverService = new ReceiverService(handler, ip, port);
        receiverService.start();
        this.name = name;
        try{
            outputStream.writeUTF(name);
        } catch(IOException e){
            e.printStackTrace();
        }
        */
    }

    public void writemessage(String msg){
        sendService.writeMessage(msg);
    }

    class SendService extends Thread{
        public void writeMessage(String msg){
            try{
                if(ClientThread.socket!=null && ClientThread.socket.isConnected()) {
                    outputStream.writeUTF("[" + name + "]" + msg);
                    outputStream.flush();
                }
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }

    class ReceiverService extends Thread{
        private DataInputStream inputStream;
        private Handler handler;

        public ReceiverService(Handler handler, String ip, int port){
            this.handler = handler;
            try{
                socket = new Socket(ip, port);
                inputStream = new DataInputStream(socket.getInputStream());
                outputStream = new DataOutputStream(socket.getOutputStream());
            } catch(IOException e){
                e.printStackTrace();
            }
        }
        @Override
        public void run(){
            try{
                while(socket!=null && socket.isConnected()){
                    while(inputStream != null){
                        String receiveMessage=(String) inputStream.readUTF().toString();
                        Message message = handler.obtainMessage();
                        Bundle bundle = new Bundle();
                        bundle.putString("msg", receiveMessage);
                        message.setData(bundle);
                        handler.sendMessage(message);
                    }
                }
            } catch(IOException e ){
                e.printStackTrace();
            }
        }
        public void cancle(){
            try{
                socket.close();
            } catch(IOException e){
                e.printStackTrace();
            }
        }
    }
}

















