package com.company;

/**
 * Created by Patryk on 02.12.2020.
 */
public class Sektor {

    int x;
    int y;


    public Sektor(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    @Override
    public String toString() {
        return "Sektor{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
}
