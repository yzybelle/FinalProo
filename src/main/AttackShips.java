package main;

import javax.swing.*;
import java.awt.*;

public class AttackShips extends Entity {
    private int check = 0;
    private int health;
    private boolean isAlive;
    private Image ship;

    public AttackShips(GamePanel gp, int x, int y){
        super(gp);
        this.health = 100;
        this.speed=2;
        this.isAlive = true;
        setDefaultValues(x,y);
        ship = new ImageIcon("res/attackShip.png").getImage();
    }
    public void setDefaultValues(int x, int y){
        this.x = x;
        this.y = y;
    }

    public void update(){
        if (check == 0) {
            y -= speed;
            if (y < 0) {
                check = 1;
            }
        } else if (check == 1) {
            y += speed;
            if (y > gp.screenHeight - gp.tileSize) {
                check = 0;
            }
        }


    }
    public void damage(int dam){
        health-=dam;
        if (health < 0) health = 0;
    }



    public int getHealth(){

        return health;
    }

    public void setHealth(int h){
        health = h;
    }

    public void draw(Graphics2D g2, Color color){
        g2.drawImage(ship, x, y, gp.tileSize, gp.tileSize, null);

    }

    public boolean isAlive() {
        return isAlive;
    }

    public void setAlive(boolean alive) {
        isAlive = alive;
    }


}
