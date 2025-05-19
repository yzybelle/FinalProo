package entity;


import main.GamePanel;
import main.KeyHandler;

import java.awt.*;

public class Player extends Entity{
    KeyHandler keyH;
    int health;
    int lives;

    public Player(GamePanel gp, KeyHandler keyH){
        super(gp);
        this.keyH = keyH;
        setDefaultValues();
    }

    public void setDefaultValues(){
        x = 100;
        y = 100;
        speed = 4;
        lives = 3;
        health = 100;
    }

    public void update(){
        if (keyH.upPressed){
            y-=speed;
        }
        else if (keyH.downPressed) {
            y+= speed;
        } else if (keyH.rightPressed) {
            x+= speed;
        } else if (keyH.leftPressed) {
            x-=speed;
        }

    }

    public void draw(Graphics2D g2){
        g2.setColor(Color.GRAY);
        g2.fillRect(x,y,gp.tileSize,gp.tileSize);

    }

    //public void damage(int dam){
    // health-=dam;
    //
    // }

    public int getHealth(){
        return health;
    }

    public int getLives(){
        return lives;
    }

}
