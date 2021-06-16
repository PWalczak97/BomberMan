package com.company;

/**
 * Created by Patryk on 18.12.2020.
 */
public class Soldier extends Sektor {

    int speed;
    String obraz;
    public Soldier(int x, int y,int speed, String obraz) {
        super(x, y);
        this.speed=speed;
        this.obraz=obraz;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public String getObraz() {
        return obraz;
    }

    public void setObraz(String obraz) {
        this.obraz = obraz;
    }

    public int getSpeed() {
        return speed;
    }
}
