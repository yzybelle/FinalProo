package entity;

import main.GamePanel;

import java.awt.*;

public class AttackShips extends Entity {
    GamePanel gp;
    private int check = 0;
    private int health;
    private int xPos;
    private int yPos;


    public AttackShips(GamePanel gp){
        this.gp = gp;
        this.speed = 2;
        setDefaultValues(300, 600);
    }

    public AttackShips(GamePanel gp, int x, int y){
        this.gp = gp;
        health = 100;
        this.speed=2;
        setDefaultValues(x,y);
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

    public void draw(Graphics2D g2, Color color){
        g2.setColor(color);
        g2.fillRect(x,y,gp.tileSize,gp.tileSize);

    }



}
