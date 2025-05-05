package entity;

import main.GamePanel;

import java.awt.*;
import java.util.concurrent.TimeUnit;

public class Projectiles extends Entity {
    GamePanel gp;
    int shipXPos;
    int shipYPos;


    public Projectiles(GamePanel gp, AttackShips temp){
        this.gp = gp;
        this.shipXPos= temp.x;
        this.shipYPos = temp.y;
        setDefaultValues(temp.x, temp.y);
    }

    public void setDefaultValues(int x, int y){
        this.x = x;
        this.y = y;
    }

    public void update() {
        if(shipYPos ==200) {
            shipXPos -= 2;
        }
    }
    public void draw(Graphics2D g2, AttackShips temp, Color color){
        g2.setColor(color);
        g2.fillRect(shipXPos,shipYPos,gp.tileSize/2,gp.tileSize/2);

    }
}
