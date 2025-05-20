package main;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;


public class GamePanel extends JPanel implements Runnable {
    // SCREEN SETTINGS
    final int originalTileSize = 16; //16x16 tile
    final int scale = 3;
    public boolean gameOver;
    public boolean canMove = true;
    public boolean AS1Alive = true;

    public final int tileSize = originalTileSize * scale; //48x48 tile
    final int maxScreenCol = 16;
    final int maxScreenRow = 12;
    long lastProjectileTime = System.nanoTime(); // this is the timer for when the rockets are shot



    final int screenWidth = tileSize * maxScreenCol; //768 pixels
    public final int screenHeight = tileSize * maxScreenRow; //576 pixels
    //FPS
    int FPS = 60;

    KeyHandler keyH = new KeyHandler();
    //TO create time, or a game clock we use a class called Thread
    Thread gameThread;
    Entity general = new Entity(this);
    ArrayList<AttackShips> all = new ArrayList<AttackShips>();
    ArrayList<PlayerAttacks> defense = new ArrayList<PlayerAttacks>();
    AttackShips attackShipTwo = new AttackShips(this, 300, 600);
    AttackShips attackShipOne = new AttackShips(this, 500, 600);
    Player player = new Player(this, keyH);

    // Set player's default position

    int playerX = 100;
    int playerY = 100;
    int playerSpeed = 4;

    public ArrayList<Projectiles> attackSOPOne = new ArrayList<>();
    public ArrayList<Projectiles> attackSOPTwo = new ArrayList<>();


    public GamePanel() {
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.MAGENTA); //Sets BackGround Color
        this.setDoubleBuffered(true); //Better rendering??? idk
        this.setFocusable((true)); //sets GamePanel to be "focused" to receive key input
        this.addKeyListener(keyH);
        this.requestFocusInWindow();
    }

    public void startGameThread() {
        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public void run() {
        all.add(attackShipOne);
        all.add(attackShipTwo);
        double drawInterval = 1000000000 / FPS; // .0166666 seconds
        double nextDrawTime = System.nanoTime() + drawInterval;
        //Create game loop
        while (gameThread != null) {


            //Updates information such as character positions
            //Draw the screen with updated information
            update();
            repaint(); // This calls paintComponent

            try {
                double remainingTime = nextDrawTime - System.nanoTime();
                remainingTime = remainingTime / 1000000;

                if (remainingTime < 0) {
                    remainingTime = 0;
                }

                Thread.sleep((long) remainingTime);

                nextDrawTime += drawInterval;
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    //This method "updates" the screen, like how a game runs and changes frames at 20 FPS
    public void update() {
        player.update(canMove, defense, new PlayerAttacks(this,player));
        attackShipOne.update();
        long currentTime = System.nanoTime();

        if (currentTime - lastProjectileTime >= 2_000_000_000L) // 2 sec
        {
            attackSOPOne.add(new Projectiles( this,attackShipOne));
            attackSOPTwo.add(new Projectiles(this, attackShipTwo));
            lastProjectileTime = currentTime;
        }
        for (int i = 0; i < attackSOPOne.size(); i++) {
            attackSOPOne.get(i).update();
        }
        for (int i = 0; i < attackSOPTwo.size(); i++) {

            attackSOPTwo.get(i).update();
        }
        for (int i = 0; i < defense.size(); i++) {
            defense.get(i).update();
        }


            for (int j = 0; j < attackSOPTwo.size(); j++) {
                if(general.collided(attackSOPTwo.get(j), player)){
                    System.out.println("temp");
                    attackSOPTwo.remove(j);
                    j--;
                    player.damage(100);
                    if (player.getHealth()==0&& player.getLives()==0){
                        canMove = false;
                        gameOver=true;
                        System.out.println("lives"+player.lives);
                    }
                    else if (player.getHealth()==0&& player.getLives()>0){
                        canMove = true;
                        player.setHealth(100);
                        player.changeLives(-1);
                        System.out.println("lives"+player.lives);

                    }

                };
            }
        for (int j = 0; j < attackSOPOne.size(); j++) {
            if(general.collided(attackSOPOne.get(j), player)){
                System.out.println("temp");
                attackSOPOne.remove(j);
                j--;
                player.damage(100);
                if (player.getHealth()==0&& player.getLives()==0){
                    canMove = false;
                    gameOver=true;
                    System.out.println("lives"+player.lives);

                }
                else if (player.getHealth()==0&& player.getLives()>0){
                    canMove = true;
                    player.setHealth(100);
                    player.changeLives(-1);
                    System.out.println("lives: "+player.lives);
                    System.out.println("health: "+player.health);


                }

            }
            for (int i = 0; i < defense.size(); i++) {
                if(general.collided(defense.get(i), attackShipOne)){
                    defense.remove(i);
                    j--;
                    attackShipOne.damage(100);
                    if (attackShipOne.getHealth()<=0){
                        AS1Alive=false;
                    }

                }
            }

        }



        attackShipTwo.update();

    }

    //This is a built in method in java where you can draw in Java
    // Graphics class draws objects on screen
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g; // This changes Graphics to graphics 2d which allows for more functions
        if (gameOver) {
            g2.setColor(new Color(0, 0, 0, 0)); // semi-transparent overlay
            g2.fillRect(0, 0, screenWidth, screenHeight);
            g2.setColor(Color.WHITE);
            g2.setFont(new Font("Times New Roman", Font.BOLD, 50));
            g2.drawString("GAME OVER", screenWidth / 2 - 200, screenHeight / 2);
        }

        player.draw(g2);
        if(AS1Alive){
            attackShipOne.draw(g2, Color.green);
            }

        attackShipTwo.draw(g2, Color.yellow);
        for (Projectiles p : attackSOPOne) {
            p.draw(g2, Color.green);
        }
        for (Projectiles p : attackSOPTwo) {
            p.draw(g2, Color.yellow);
        }
        for (PlayerAttacks p : defense) {
            p.draw(g2, Color.yellow);
        }



        g2.dispose();


    }
}
