package com.ywfsw.service;

import com.ywfsw.dao.FruitDao;
import com.ywfsw.dao.UserDao;
import com.ywfsw.domain.Fruit;
import com.ywfsw.domain.Option;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class Service implements Runnable {
    UserDao userDao = new UserDao();
    FruitDao fruitDao = new FruitDao();
    private static final Logger LOGGER = LoggerFactory.getLogger(Service.class);
    private Socket socket;

    public Service(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        //读取报文数据
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String allMsg = br.readLine();
            LOGGER.info("接收报文：" + allMsg);
            //解析报文
            String[] split = allMsg.split("&");
            String optionStr = split[0];//操作
            Option option = Enum.valueOf(Option.class, optionStr);
            String msg = split[1];//数据
            //功能路由
            switch (option) {
                case LOG_IN:
                    LOGGER.info("用户登录");
                    logIn(msg);
                    break;
                case SIGN_UP:
                    LOGGER.info("用户注册");
                    signUp(msg);
                    break;
                case FRUIT_ADD:
                    LOGGER.info("添加水果");
                    addFruit(msg);
                    break;
                case FRUIT_DELETE:
                    LOGGER.info("删除水果");
                    deleteFruit(msg);
                    break;
                case FRUIT_UPDATE:
                    LOGGER.info("修改水果");
                    updateFruit(msg);
                    break;
                case FRUIT_FIND_ALL:
                    LOGGER.info("查看水果");
                    findAllFruits();
                    break;
                case PAY:
                    LOGGER.info("结账功能");
                    pay(msg);
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //结账
    private void pay(String msg) {
        boolean res = fruitDao.pay(msg);
        try {
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            bw.write(res+"");
            LOGGER.info("结账结果："+res);
            bw.newLine();
            bw.flush();
            bw.close();
            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //改
    private void updateFruit(String msg) {
        boolean res = fruitDao.updateFruit(msg);
        try {
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            bw.write(res+"");
            LOGGER.info("修改结果："+res);
            bw.newLine();
            bw.flush();
            bw.close();
            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //删
    private void deleteFruit(String msg) {
        boolean res = fruitDao.deleteFruit(msg);
        try {
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            bw.write(res+"");
            LOGGER.info("删除结果："+res);
            bw.newLine();
            bw.flush();
            bw.close();
            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //查
    private void findAllFruits() {
        ArrayList<Fruit> fruitArrayList = fruitDao.findAllFruits();
        try {
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            oos.writeObject(fruitArrayList);
            oos.flush();
            oos.close();
            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //增
    private void addFruit(String msg) {
        boolean res = fruitDao.addFruit(msg);
        try {
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            bw.write(res + "");
            LOGGER.info("添加结果：" + res);
            bw.newLine();
            bw.flush();
            bw.close();
            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //注册
    private void signUp(String msg) {
        boolean res = userDao.signUp(msg);
        try {
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            bw.write(res + "");
            LOGGER.info("注册结果：" + res);
            bw.newLine();
            bw.flush();
            bw.close();
            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //登录
    private void logIn(String msg) {
        boolean res = userDao.logIn(msg);
        try {
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            bw.write(res + "");
            LOGGER.info("登录结果：" + res);
            bw.newLine();
            bw.flush();
            bw.close();
            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
