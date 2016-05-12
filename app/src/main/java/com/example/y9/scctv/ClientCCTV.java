package com.example.y9.scctv;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class ClientCCTV extends AppCompatActivity {

    TextView textView;
    EditText editText;
    String myName = "yguhan";
    String serverip = "ubuntu.poapper.com";
    //String serverip = "141.223.202.186";

    int portNum = 3600;

    Handler handler = new Handler();

    //ClientThread thread;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.send_text);

        textView = (TextView) findViewById(R.id.textView2);
        editText = (EditText) findViewById(R.id.editText);
        Button sendTxtBtn = (Button) findViewById(R.id.sendTxt);
        Button sendImgBtn = (Button) findViewById(R.id.sendImg);

        handler = new Handler(){
            @Override
            public void handleMessage(Message msg){
                textView.append(msg.getData().getString("msg")+ "\r\n");
            }
        };

        final TCPClient tcpClient = new TCPClient();
        Thread cThread = new Thread(tcpClient);
        cThread.start();

        sendTxtBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tcpClient.msg = "10"+editText.getText().toString();
                //    Log.v("MSG", tcpClient.msg);
            }
        });

        sendImgBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                //tcpClient.img = "11"+editText.getText().toString();
                tcpClient.img = editText.getText().toString();
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public class TCPClient implements Runnable{

        private String msg;
        private String img;

        public TCPClient(){
//            this.msg="YUNGU";
        }

        public void run(){
            try{
                Socket socket = new Socket(serverip, portNum);
                try {
                    while (true) {
                        if(msg!=null) {
                            Log.v("MSG", msg);
                            PrintWriter out = new PrintWriter(
                                    new BufferedWriter(new OutputStreamWriter(
                                            socket.getOutputStream())), true
                            );
                            out.println(msg);
                            msg = null;
                        }
                        else if(img!=null){
                            Log.v("IMG", img);


                            DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
                            //File f = new File("/storage/emulated/0/DCIM/Camera/"+ img + ".jpg"); //"/storage/emulated/0/DCIM/camera/"+ "a" + ".txt" 퍼미션문제
                            //저 경로가 내부메모리 기본 사진저장 경로인데, 왠지 모르겠지만 외장메모리 permission이 필요해. 인터넷이랑 외장permission만 있으면 되
                            //layout은 원래꺼 그대로 썼어

                            //String fileContents = readFileAsString("/storage/emulated/0/DCIM/Camera/"+ img + ".jpg");
                            try{
/***
                                PrintWriter out = new PrintWriter(
                                        new BufferedWriter(new OutputStreamWriter(
                                                socket.getOutputStream())), true
                                );
                                out.println("11");
                                out.println(img + ".jpg");
    ***/
                                dos.writeUTF("11");
                                dos.writeUTF(img + ".jpg");

                                //FileInputStream fis = new FileInputStream(f);
                                //BufferedInputStream bis = new BufferedInputStream(fis);

                                /***
                                PrintWriter out = new PrintWriter(
                                        new BufferedWriter(new OutputStreamWriter(
                                                socket.getOutputStream())), true
                                );
                                ***/
                                //out.println("11");


                                byte[] buf = new byte[1024];    //받을때도 같은 버퍼크기로 받아

                                /***
                                int jpgSize = (int)f.length();
                                dos.write(buf, 0, jpgSize);
                                ***/


                                //int sizeOfJpg =(int) f.length();
                                //out.println(String.valueOf(sizeOfJpg));

                                /***
                                int len;
                                while((len = bis.read(buf)) != -1)  //파일의 남은 부분이 없을때까지 보내
                                {
                                    dos.write(buf, 0, len);
                                }
                                ***/
                                dos.writeUTF("12");
                                //out.println("12");
                                //out.println("12");
                                img = null;

                                //dos.flush();
                                //dos.close();
                                //bis.close();
                                //fis.close();    //닫닫

                            }
                            catch(Exception e){
                                e.printStackTrace();
                            }
                        }
                    }
                }catch (Exception e){
                    Log.e("TCP","S :Error",e);
                }finally {
                    //socket.close();
                }
            }catch(IOException e){
                e.printStackTrace();
            }
        }
    }
    private String readFileAsString(String filePath) throws IOException {
        StringBuffer fileData = new StringBuffer();
        BufferedReader reader = new BufferedReader(
                new FileReader(filePath));
        char[] buf = new char[1024];
        int numRead=0;
        while((numRead=reader.read(buf)) != -1){
            String readData = String.valueOf(buf, 0, numRead);
            fileData.append(readData);
        }
        reader.close();
        return fileData.toString();
    }
}
