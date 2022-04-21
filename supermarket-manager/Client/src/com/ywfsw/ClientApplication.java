package com.ywfsw;

import com.ywfsw.controller.Controller;

import java.util.Scanner;

public class ClientApplication {
    public static void main(String[] args) {
        Controller controller = new Controller();
        Scanner sc = new Scanner(System.in);
        while (true) {
            System.out.println("-----🍭----- 水果超市 -----🍭-----");
            System.out.println("1.用户登录");
            System.out.println("2.用户注册");
            System.out.println("3.管理员登录");
            System.out.println("4.退出");
            String option = sc.next();
            switch (option){
                case "1":
                    controller.logIn();
                    break;
                case "2":
                    controller.signUp();
                    break;
                case "3":
                    controller.managerLogIn();
                    break;
                case "4":
                    System.out.println("4");
                    System.exit(0);
                default:
                    System.out.println("输入有误");
                    break;
            }
        }
    }
}
