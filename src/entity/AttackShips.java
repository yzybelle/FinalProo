package entity;

import main.GamePanel;

import java.awt.*;

public class AttackShips extends Entity {
    GamePanel gp;


    public AttackShips(GamePanel gp){
        this.gp = gp;
        setDefaultValues();
    }
    public void setDefaultValues(){
        x = 300;
        y = 200;
    }

    public void update(int check){
        if (check == 0) {
            y -= 2;
            if (y < 0) {
                check = 1;
            }
        } else if (check == 1) {
            y += 2;
            if (y > gp.screenHeight - gp.tileSize) {
                check = 0;
            }
        }


    }

    public void draw(Graphics2D g2){
        g2.setColor(Color.GRAY);
        g2.fillRect(x,y,gp.tileSize,gp.tileSize);

    }






}
