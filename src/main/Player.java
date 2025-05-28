package main;


import java.awt.*;
import java.util.ArrayList;

public class Player extends Entity {
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

    public void update(boolean f, ArrayList<PlayerAttacks> array, PlayerAttacks object){
       if (f)
       {if (keyH.upPressed){
           y-=speed;
       }
       else if (keyH.downPressed) {
           y+= speed;
       } else if (keyH.rightPressed) {
           x+= speed;
       } else if (keyH.leftPressed) {
           x-=speed;
       }
       else if (keyH.shoot) {
           array.add(object);
       }

       }
       else  {
           if (keyH.upPressed){

       }
       else if (keyH.downPressed) {

       } else if (keyH.rightPressed) {

       } else if (keyH.leftPressed) {

       }}

    }

    public void draw(Graphics2D g2){
        g2.setColor(Color.GRAY);
        g2.fillRect(x,y,gp.tileSize,gp.tileSize);

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
    public int getLives(){
        return lives;
    }
    public void changeLives(int damage){
        lives = lives+damage;
    }

}
