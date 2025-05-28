package main;


public class Entity {
    public int x, y;
    public int speed;
    GamePanel gp;

    public Entity(GamePanel gp) {
        this.gp = gp;
    }

    public boolean collided (Projectiles x, Player y){
        if (x.x >= y.x && x.x <= y.x + gp.tileSize &&
                x.y >= y.y && x.y <= y.y + gp.tileSize){
            System.out.println("collided");
            return true;};
        return false;
    }

    public boolean collided (PlayerAttacks x, AttackShips y){
        if (x.x >= y.x && x.x <= y.x + gp.tileSize &&
                x.y >= y.y && x.y <= y.y + gp.tileSize){
            System.out.println("collided");
            return true;};
        return false;
    }


}
