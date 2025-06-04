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
    public int currentRound=1;



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
    ArrayList<AttackShips> roundGenerator = new ArrayList<AttackShips>();
    ArrayList<PlayerAttacks> defense = new ArrayList<PlayerAttacks>();
    ArrayList<ArrayList<Projectiles>> attackPro = new ArrayList<>();

    AttackShips nextButton = new AttackShips(this, screenWidth/2, screenHeight/2);
    Player player = new Player(this, keyH);

    // Set player's default position

    int playerX = 100;
    int playerY = 100;
    int playerSpeed = 4;



    public GamePanel() {
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.MAGENTA); //Sets BackGround Color
        this.setDoubleBuffered(true); //Better rendering??? idk
        this.setFocusable((true)); //sets GamePanel to be "focused" to receive key input
        this.addKeyListener(keyH);
        this.requestFocusInWindow();
    }


    public void attackShipGenerator(int round){
        // Decides which ships are in which rounds and gets rid of the variable attackships

        if (round == 1) {
            roundGenerator.add(new AttackShips(this, 500, 600));
            roundGenerator.add(new AttackShips(this, 300, 800));
        } else if (round == 2) {
            roundGenerator.add(new AttackShips(this, 500, 600));
            roundGenerator.add(new AttackShips(this, 300, 800));
            roundGenerator.add(new AttackShips(this, 400, 700));
            roundGenerator.add(new AttackShips(this, 200, 600));
        }
        for (int i = 0; i < attackPro.size(); i++) {
            attackPro.add(new ArrayList<>());
        }
        nextButton = new AttackShips(this, screenWidth/2, screenHeight/2); // This is the next button that the player has to shoot

    }
    public void startGameThread() {
        gameThread = new Thread(this);
        gameThread.start();
        attackShipGenerator(currentRound);

    }

    @Override
    public void run() {


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

        player.update(canMove, defense, new PlayerAttacks(this, player));
        for (AttackShips ship : roundGenerator) {
            if (ship.isAlive()) ship.update();
        }

        for (PlayerAttacks attack : defense) {
            attack.update();
        }


        long currentTime = System.nanoTime();

        if (currentTime - lastProjectileTime >= 2_000_000_000L) // 2 sec
        {

            for (int i = 0; i < roundGenerator.size(); i++) {
                if (roundGenerator.get(i).isAlive()) {
                    //if ship is alive, add a projectile to the arraylist that represents the ship
                    ArrayList<Projectiles> projectiles = attackPro.get(i);
                    for (int j = 0; j < projectiles.size(); j++) {
                        projectiles.get(j).update();

                        // Check projectile hits player
                        if (general.collided(projectiles.get(j), player)) {
                            projectiles.remove(j);
                            j--;
                            player.damage(100);
                        }
                    }
                    for (int j = 0; j < defense.size(); j++) {
                        //check if player missle hits the attackSHip
                        if (general.collided(defense.get(j), roundGenerator.get(i))) {
                            defense.remove(j);
                            j--;
                            roundGenerator.get(i).damage(100);
                            if (roundGenerator.get(i).getHealth() <= 0) {
                                roundGenerator.get(i).setAlive(false);
                                roundGenerator.remove(i);
                                attackPro.remove(i);
                                i--;
                            }
                        }
                    }
                }



                lastProjectileTime = currentTime;
            }


        }
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

        if(roundGenerator.isEmpty()){
            g2.setColor(new Color(0, 0, 0, 0)); // semi-transparent overlay
            g2.fillRect(0, 0, screenWidth, screenHeight);
            g2.setColor(Color.WHITE);
            g2.setFont(new Font("Times New Roman", Font.BOLD, 50));
            g2.drawString("You won round one!", screenWidth / 2 - 240, screenHeight / 2);
            if (nextButton.isAlive()){
                nextButton.draw(g2,Color.BLACK);
            }

            for (int i = 0; i < defense.size(); i++) {
                if(general.collided(defense.get(i), nextButton)){
                    defense.remove(i);
                    i--;
                    nextButton.damage(1000);
                    if (nextButton.getHealth()<=0){
                        nextButton.setAlive(false);
                    }


                }
            }

        }

        player.draw(g2);




        for (PlayerAttacks p : defense) {
            p.draw(g2, Color.yellow);
        }


        g2.dispose();


    }
    public void takeHit(Player player, int num){
        player.damage(num);
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
    }

    public void roundOneGo(){

    }

    public void roundTwoGo(){}

    public void roundThreeGo(){}


}
