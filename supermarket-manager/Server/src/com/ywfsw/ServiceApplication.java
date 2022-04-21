package com.ywfsw;

import com.ywfsw.service.Service;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ServiceApplication {
    public static void main(String[] args) {
        ServerSocket ss = null;
        try {
            ss = new ServerSocket(10086);
            ThreadPoolExecutor pool = new ThreadPoolExecutor(10,20,10, TimeUnit.SECONDS,new ArrayBlockingQueue<>(10), Executors.defaultThreadFactory(),new ThreadPoolExecutor.AbortPolicy());

            while (true){
                //监听客户端连接
                Socket socket = ss.accept();
                pool.submit(new Service(socket));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
