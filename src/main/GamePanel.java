package main;

import entity.AttackShips;
import entity.Player;
import entity.Projectiles;

import javax.swing.*;
import java.awt.*;


public class GamePanel extends JPanel implements Runnable {
    // SCREEN SETTINGS
    final int originalTileSize = 16; //16x16 tile
    final int scale = 3;

    public final int tileSize = originalTileSize * scale; //48x48 tile
    final int maxScreenCol = 16;
    final int maxScreenRow = 12;

    final int screenWidth = tileSize*maxScreenCol; //768 pixels
    public final int screenHeight = tileSize * maxScreenRow; //576 pixels
    //FPS
    int FPS = 60;

    KeyHandler keyH = new KeyHandler();
    //TO create time, or a game clock we use a class called Thread
    Thread gameThread;

    AttackShips attackShipTwo = new AttackShips(this, 300,600);
    AttackShips attackShipOne = new AttackShips(this, 500,600);
    Projectiles attackSOP = new Projectiles(this, attackShipOne);
    Player player = new Player(this,keyH);
    // Set player's default position

    int playerX = 100;
    int playerY = 100;
    int playerSpeed = 4;

    public GamePanel(){
        this.setPreferredSize(new Dimension(screenWidth,screenHeight));
        this.setBackground(Color.MAGENTA); //Sets BackGround Color
        this.setDoubleBuffered(true); //Better rendering??? idk
        this.setFocusable((true)); //sets GamePanel to be "focused" to receive key input
        this.addKeyListener(keyH);
        this.requestFocusInWindow();
    }

    public void startGameThread(){
        gameThread = new Thread(this);
        gameThread.start();
    }
    @Override
    public void run() {

        double drawInterval = 1000000000/FPS; // .0166666 seconds
        double nextDrawTime = System.nanoTime() + drawInterval;
        //Create game loop
        while(gameThread!=null){

            //this is the time
            long currentTime = System.nanoTime();
            System.out.println("current tune:" +currentTime);

            //Updates information such as character positions
            //Draw the screen with updated information
            update();
            repaint(); // This calls paintComponent

            try {
                double remainingTime = nextDrawTime-System.nanoTime();
                remainingTime = remainingTime/1000000;

                if(remainingTime<0){
                    remainingTime = 0;
                }

                Thread.sleep((long)remainingTime);

                nextDrawTime += drawInterval;
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
    //This method "updates" the screen, like how a game runs and changes frames at 20 FPS
    public void update(){
        player.update();
        attackShipOne.update();
        attackSOP.update();
        System.out.println("AttackSOP XPOS:" + attackSOP.x);
        attackShipTwo.update();
    }
    //This is a built in method in java where you can draw in Java
    // Graphics class draws objects on screen
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D)g; // This changes Graphics to graphics 2d which allows for more functions
        player.draw(g2);
        attackShipOne.draw(g2,Color.green);
        attackSOP.draw(g2, Color.green);
        attackShipTwo.draw(g2, Color.yellow);
        g2.dispose();


    }
}
