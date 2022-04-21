package com.ywfsw.domain;

import java.io.Serializable;

public class Fruit implements Serializable {
    private String id;
    private String name;
    private String price;
    private String unit;
    private int count;

    public Fruit() {
    }

    public Fruit(String id, String name, String price, String unit, int count) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.unit = unit;
        this.count = count;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Fruit fruit = (Fruit) o;

        if (count != fruit.count) return false;
        if (id != null ? !id.equals(fruit.id) : fruit.id != null) return false;
        if (name != null ? !name.equals(fruit.name) : fruit.name != null) return false;
        if (price != null ? !price.equals(fruit.price) : fruit.price != null) return false;
        return unit != null ? unit.equals(fruit.unit) : fruit.unit == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (price != null ? price.hashCode() : 0);
        result = 31 * result + (unit != null ? unit.hashCode() : 0);
        result = 31 * result + count;
        return result;
    }

    @Override
    public String toString() {
        return id+","+name+","+price+","+unit+","+count;
    }
}
