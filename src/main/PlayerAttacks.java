package main;

import java.awt.*;

public class PlayerAttacks extends Entity{


    public PlayerAttacks(GamePanel gp, Player player) {
        super(gp);
        this.speed = 7;
        this.x = player.x;
        this.y = player.y;
    }

    public void update() {
        x += speed;
    }

    public void draw(Graphics2D g2, Color color) {
        g2.setColor(color);
        g2.fillRect(x, y, gp.tileSize / 2, gp.tileSize / 2);
    }
}
