package entity;

import main.GamePanel;

import java.awt.*;

public class Projectiles extends Entity {
    int shipXPos;
    int shipYPos;


    public Projectiles(GamePanel gp, AttackShips attackShip) {
        super(gp);
        this.speed = 4;
        this.x = attackShip.x;
        this.y = attackShip.y;

    }


    public void update() {
        x -= speed;
    }

    public void draw(Graphics2D g2, Color color) {
        g2.setColor(color);
        g2.fillRect(x, y, gp.tileSize / 2, gp.tileSize / 2);
    }
}
