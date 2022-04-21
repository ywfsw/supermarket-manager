package com.ywfsw.dao;

import com.ywfsw.domain.Fruit;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;

public class FruitDao {
    private static ArrayList<Fruit> fruitArrayList = new ArrayList<>();

    Fruit fruit = new Fruit();

    static {
        System.out.println("----------loading----------");
        reload();
    }


    //封装方法：数据存档功能 -基于缓冲字符输出流
    private void reSave() {
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter("Server/Fruit.txt"));
            //遍历集合
            for (int i = 0; i < fruitArrayList.size(); i++) {
                Fruit fruit = fruitArrayList.get(i);
                bw.write(fruit.toString());
                bw.write(System.lineSeparator());
            }
            bw.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //封装方法：读取存档数据 -基于缓冲字符输入流
    private static void reload() {
        try {
            BufferedReader br = new BufferedReader(new FileReader("Server/Fruit.txt"));
            String fruitStr;
            String[] fruitStrArr = new String[0];
            while ((fruitStr = br.readLine()) != null) {
                String[] split = fruitStr.split(",");
                Fruit fruit = new Fruit(split[0], split[1], split[2], split[3],Integer.valueOf(split[4]));
                fruitArrayList.add(fruit);
            }
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //增
    public boolean addFruit(String msg) {
        String[] split = msg.split(",");
        fruit = new Fruit(split[0], split[1], split[2], split[3],Integer.valueOf(split[4]));
        boolean res = fruitArrayList.add(fruit);
        reSave();
        return res;
    }

    //查
    public ArrayList<Fruit> findAllFruits() {
        return fruitArrayList;
    }

    //删
    public boolean deleteFruit(String msg) {
        String deleteId = msg;
        boolean exists = isExists(deleteId);
        int index = getIndex(deleteId);
        if (!exists) {
            return false;
        } else {
            fruitArrayList.remove(index);
            reSave();
            return true;
        }
    }

    //改
    public boolean updateFruit(String msg) {
        String[] split = msg.split(",");
        String updateId = split[5];
        boolean exists = isExists(updateId);
        if (!exists){
            return false;
        }
        Fruit newFruit = new Fruit(split[0], split[1], split[2], split[3],Integer.valueOf(split[4]));
        int index = getIndex(updateId);
        if (index == -1) {
            return false;
        } else {
            fruitArrayList.set(index, newFruit);
            reSave();
            return true;
        }
    }

    //查找编号对应水果对象的集合位置
    private int getIndex(String id) {
        int index = -1;
        for (int i = 0; i < fruitArrayList.size(); i++) {
            fruit = fruitArrayList.get(i);
            if (id.equals(fruit.getId()) && id != null) {
                index = i;
                break;
            }
        }
        return index;
    }

    //判断编号是否存在方法
    public boolean isExists(String Id) {
        int index = getIndex(Id);
        return index != -1;
    }

    //结账
    public boolean pay(String msg) {
        boolean res = true;
        String[] split = msg.split(",");
        for (String s : split) {
            String[] split1 = s.split("=");
            String id = split1[0];
            String count = split1[1];
            int index = getIndex(id);
            Fruit fruit = fruitArrayList.get(index);
            int abCount = fruit.getCount();
            int newCount = abCount-Integer.valueOf(count);
            fruit.setCount(newCount);
            if (newCount<0){
                fruit.setCount(abCount);
                res = false;
            }
        }
        if (res){
            reSave();
        }
        return res;
    }
}
