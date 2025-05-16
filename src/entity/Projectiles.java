package entity;

import main.GamePanel;

import java.awt.*;

public class Projectiles extends Entity {
    GamePanel gp;
    int shipXPos;
    int shipYPos;


    public Projectiles(GamePanel gp, AttackShips attackShipOne) {
        this.gp = gp;
        this.speed = 4;

    }


    public void update() {
        x -= speed;
    }

    public void draw(Graphics2D g2, Color color) {
        g2.setColor(color);
        g2.fillRect(x, y, gp.tileSize / 2, gp.tileSize / 2);
    }
}
