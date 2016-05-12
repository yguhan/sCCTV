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

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientUser extends AppCompatActivity {

    EditText editText;
    Button requestBtn;
    Handler handler = new Handler();
    TextView textView;
    String myName = "yguhan";
    String serverip = "ubuntu.poapper.com";
    //String serverip = "141.223.202.186";

    int portNum = 3600;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.connect_cctv);

        editText = (EditText) findViewById(R.id.editText);
        requestBtn = (Button) findViewById(R.id.button2);
        textView = (TextView) findViewById(R.id.textView2);

        final TCPClient tcpClient = new TCPClient();
        Thread cThread = new Thread(tcpClient);
        cThread.start();

        handler = new Handler(){
            @Override
            public void handleMessage(Message msg){
                textView.append(msg.getData().getString("msg")+ "\r\n");
            }
        };

        requestBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tcpClient.msg = '0'+editText.getText().toString();
                //    Log.v("MSG", tcpClient.msg);

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
}

