package com.lily.androidkfysts.socket;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;

public class TCPServerService extends Service {

    private boolean isServiceDestoryed = false;
    private String[] definedMessage = new String[]{
            "你好",
            "你叫啥？",
            "今天天气不错",
            "你知道吗？我可以和很多人同时聊天",
            "给你讲个笑话吧"
    };

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        new Thread(new TCPServer());
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        isServiceDestoryed = true;
        super.onDestroy();

    }

    private class TCPServer implements Runnable {
        @Override
        public void run() {
            ServerSocket serverSocket = null;
            try {
                serverSocket = new ServerSocket(8688);
            } catch (IOException e) {
                System.err.println("establish tcp server failed,port 8688");
                e.printStackTrace();
            }

            while (!isServiceDestoryed){
                try {
                    final Socket client = serverSocket.accept();
                    System.out.println("accept");
                    new Thread(){
                        @Override
                        public void run() {
                            try {
                                responseClient(client);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }.start();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void responseClient(Socket client) throws IOException{
        BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
        PrintWriter out = new PrintWriter(new BufferedOutputStream(client.getOutputStream()),true);
        out.println("欢迎来到聊天室");
        while (!isServiceDestoryed){
            String str = in.readLine();
            System.out.println("msg from client  :" + str);
            if(str == null){
                break;
            }
            int i = new Random().nextInt(definedMessage.length);
            String msg = definedMessage[i];
            out.println(msg);
            System.out.println("send : " + msg);
        }
        System.out.println("client quit. ");
        out.close();
        in.close();
        client.close();

    }
}
