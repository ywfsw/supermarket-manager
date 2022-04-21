package com.ywfsw.dao;

import com.ywfsw.domain.User;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;

public class UserDao {
    private static ArrayList<User> userArrayList = new ArrayList<>();

    User user = new User();
    static {
        System.out.println("----------loading----------");
        reload();
        System.out.println("111");
        System.out.println(userArrayList);
    }




    //封装方法：数据存档功能 -基于缓冲字符输出流
    private void reSave() {
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter("Server/User.txt"));
            //遍历集合
            for (int i = 0; i < userArrayList.size(); i++) {
                User user = userArrayList.get(i);
                bw.write(user.toString());
                bw.write(System.lineSeparator());
            }
            bw.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //封装方法：读取存档数据 -基于缓冲字符输入流
    private static void reload(){
        try {
            BufferedReader br = new BufferedReader(new FileReader("Server/User.txt"));
            String userStr;
            String[] userStrArr = new String[0];
            while ((userStr = br.readLine())!=null) {
                String[] split = userStr.split("=");
                User user = new User(split[0],split[1]);
                userArrayList.add(user);
            }
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    //登录功能
    public boolean logIn(String msg) {
        String[] split = msg.split("=");
        User user = new User(split[0], split[1]);
        boolean res = false;
        for (User u : userArrayList) {
            if (u.equals(user)){
                res = true;
            }
        }
        return res;
    }

    //注册功能
    public boolean signUp(String msg) {
        String[] split = msg.split("=");
        user = new User(split[0], split[1]);
        boolean res = userArrayList.add(user);
        reSave();
        return res;
    }

}


