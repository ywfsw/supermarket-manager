package com.ywfsw.controller;

import com.ywfsw.domain.Fruit;
import com.ywfsw.domain.Option;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

public class Controller {
    Scanner sc = new Scanner(System.in);
    private static double total = 0;

    private void start(String name) {
        while (true) {
            System.out.println("-----☀️----- 欢迎你" + name + " -----☀️-----");
            findAllFruits();
            System.out.println("1.添加水果");
            System.out.println("2.删除水果");
            System.out.println("3.修改水果");
            System.out.println("4.查看水果");
            System.out.println("5.退出");
            String option = sc.next();
            switch (option) {
                case "1":
                    addFruit();
                    break;
                case "2":
                    deleteFruit();
                    break;
                case "3":
                    updateFruit();
                    break;
                case "4":
                    findAllFruits();
                    break;
                case "5":
                    System.out.println("感谢使用，白白～");
                    System.exit(0);
                default:
                    System.out.println("输入有误，请重新输入");
                    break;
            }
        }
    }

    private void userStart(String name) {
        while (true) {
            System.out.println("-----🌈----- 欢迎你" + name + " -----🌈-----");
            findAllFruits();
            System.out.println("1.查看水果");
            System.out.println("2.开始购买");
            System.out.println("3.结账");
            System.out.println("4.退出");
            String option = sc.next();
            switch (option) {
                case "1":
                    findAllFruits();
                    break;
                case "2":
                    buyFruit();
                    break;
                case "3":
                    pay();
                    break;
                case "4":
                    System.out.println("感谢使用，白白～");
                    System.exit(0);
                default:
                    System.out.println("输入有误，请重新输入");
                    break;
            }
        }
    }



    //增
    private void addFruit() {
        try {
            Socket socket = getSocket();
            System.out.println("输入水果编号：");
            String id = sc.next();
            Fruit fruit = inputFruitInfo(id);
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            bw.write(Option.FRUIT_ADD +"&" + fruit.toString());
            bw.newLine();
            bw.flush();

            BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String res = br.readLine();
            if ("true".equals(res)) {
                System.out.println("添加成功");
            } else {
                System.err.println("添加失败");
            }
            br.close();
            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    //删
    private void deleteFruit() {
        try {
            Socket socket = getSocket();
            System.out.println("输入要删除的水果编号：");
            String deleteId = sc.next();
/*            System.out.println("确定删除？Y/N");
            String option = sc.next();
            if (option.equalsIgnoreCase("N")){
                return;
            } else if (option.equalsIgnoreCase("Y")){

            }*/
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            bw.write(Option.FRUIT_DELETE+"&" + deleteId);
            bw.newLine();
            bw.flush();

            BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String res = br.readLine();
            if ("true".equals(res)) {
                System.out.println("删除成功");
            }
            else {
                System.err.println("编号不存在！");
            }
            br.close();
            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //改
    private void updateFruit() {
        System.out.println("输入修改水果编号：");
        String updateId = sc.next();
        Fruit fruit = inputFruitInfo(updateId);
        try {
            Socket socket = getSocket();
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            bw.write(Option.FRUIT_UPDATE+"&" + fruit.toString() + "," + updateId);
            bw.newLine();
            bw.flush();

            //根据服务器响应给用户提示
            BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String res = br.readLine();
            System.out.println("true".equals(res) ? "修改成功" : "修改失败");
            br.close();
            bw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //查
    private void findAllFruits() {
        System.out.println("编号\t名称\t价格\t单位\t库存");
        try {
            Socket socket = getSocket();
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            bw.write(Option.FRUIT_FIND_ALL+"&" + "findAll");
            bw.newLine();
            bw.flush();

            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            ArrayList<Fruit> fruitArrayList = (ArrayList<Fruit>) ois.readObject();
            if (fruitArrayList == null || fruitArrayList.size() == 0) {
                System.out.println("无数据");
            } else {
                fruitArrayList.forEach(fruit -> System.out.println(fruit.getId() + "\t" + fruit.getName() + "\t" + fruit.getPrice() + "\t" + fruit.getUnit() + "\t\t" + fruit.getCount()));
            }
            bw.close();
            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //买
    private void buyFruit() {

        try {
            Socket socket = getSocket();
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            bw.write(Option.FRUIT_FIND_ALL+"&" + "findAll");
            bw.newLine();
            bw.flush();

            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            ArrayList<Fruit> fruitArrayList = (ArrayList<Fruit>) ois.readObject();
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("Client/buy.txt"));
            if (fruitArrayList == null || fruitArrayList.size() == 0) {
            } else {
                for (Fruit fruit : fruitArrayList) {
                    System.out.println("输入购买 " + fruit.getName() + " 的数量：");
                    int count = sc.nextInt();
                    bufferedWriter.write(fruit.getId() + "=" + count + ",");
                    bufferedWriter.flush();
                    total += count * Double.valueOf(fruit.getPrice());
                }
            }
            if (total > 100 && total <= 200) {
                total = (total - 100) * 0.9 + 100;
                System.out.println("购物总额为：" + Math.round(total*100)/100 + "元，部分金额已享9折优惠");
            } else if (total > 200 && total <= 500) {
                total = (total - 200) * 0.8 + 90;
                System.out.println("购物总额为：" + Math.round(total*100)/100 + "元，部分金额已享8折优惠");
            } else if (total > 500) {
                total = (total - 500) * 0.7 + 330;
                System.out.println("购物总额为：" + Math.round(total*100)/100 + "元，部分金额已享7折优惠");
            } else {
                System.out.println("购物总额为：" + Math.round(total*100)/100 + "元");
            }
            bw.close();
            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //结账
    private void pay() {
        System.out.println("确认付款？ [y/n]？");
        while (true) {
            String option = sc.next();
            if ("y".equalsIgnoreCase(option)) {
                try {
                    Socket socket = getSocket();
                    BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                    BufferedInputStream bis = new BufferedInputStream(new FileInputStream("Client/buy.txt"));
                    byte[] bytes = new byte[1024*1024];
                    int len = bis.read(bytes);
                    String msg = new String(bytes,0,len);
                    bw.write(Option.PAY+"&" + msg);
                    bw.newLine();
                    bw.flush();

                    BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    String res = br.readLine();
                    System.out.println("true".equals(res) ? "结账成功" : "库存不足，结账失败");
                    bw.close();
                    br.close();
                    socket.close();
                    break;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if ("n".equalsIgnoreCase(option)) {
                break;
            } else {
                System.out.println("输入有误，请重新输入：");
            }
        }

    }


    //管理员登录
    public void managerLogIn() {
            System.out.println("用户名：");
            String nameStr = sc.next();
            System.out.println("密码：");
            String passwordStr = sc.next();
            if (nameStr.equals("admin") && passwordStr.equals("admin")) {
                System.out.println("管理员登录成功");
                start("管理员");
            } else {
                System.err.println("用户名或密码错误");
            }
    }

    //登录功能
    public void logIn() {
            System.out.println("用户名：");
            String nameStr = sc.next();
            System.out.println("密码：");
            String passwordStr = sc.next();
            String logInStr = nameStr + "=" + passwordStr;
            try {
                Socket socket = getSocket();
                BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                bw.write(Option.LOG_IN+"&" + logInStr);
                bw.newLine();
                bw.flush();

                BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                String res = br.readLine();
                if ("true".equals(res)) {
                    System.out.println("登录成功");
                    userStart(nameStr);
                } else {
                    System.err.println("用户名或密码错误");
                }
                bw.close();
                socket.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

    }

    //注册功能
    public void signUp() {
        System.out.println("用户名：");
        String nameStr = sc.next();
        System.out.println("密码：");
        String passwordStr = sc.next();
        String logInStr = nameStr + "=" + passwordStr;
        try {
            Socket socket = getSocket();
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            bw.write(Option.SIGN_UP+"&" + logInStr);
            bw.newLine();
            bw.flush();

            BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String res = br.readLine();
            if ("true".equals(res)) {
                System.out.println("注册成功");
            } else {
                System.err.println("注册失败");
            }
            bw.close();
            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    //封装方法，获取socket对象
    private Socket getSocket() throws IOException {
        Socket socket = new Socket("127.0.0.1", 10086);
        return socket;
    }

    //封装方法：键盘录入水果信息，封装水果对象并返回
    private Fruit inputFruitInfo(String id) {
        System.out.println("输入水果名称：");
        String name = sc.next();
        System.out.println("输入水果价格：");
        String price = sc.next();
        System.out.println("输入水果单位：");
        String unit = sc.next();
        System.out.println("输入水果库存：");
        int count = sc.nextInt();
        //封装水果对象
        Fruit fruit = new Fruit(id, name, price, unit, count);
        return fruit;
    }


}
