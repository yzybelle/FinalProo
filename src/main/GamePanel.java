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
    private ImageIcon space;



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


    AttackShips attackShipTwo = new AttackShips(this, 300, 800);
    AttackShips attackShipOne = new AttackShips(this, 500, 600);

    AttackShips attackShipThree = new AttackShips(this, 300, 800);
    AttackShips attackShipFour = new AttackShips(this, 500, 600);
    AttackShips attackShipFive = new AttackShips(this, 300, 800);
    AttackShips attackShipSix= new AttackShips(this, 500, 600);



    Player player = new Player(this, keyH);

    // Set player's default position

    int playerX = 100;
    int playerY = 100;
    int playerSpeed = 4;

    public ArrayList<Projectiles> attackSOPOne = new ArrayList<>();
    public ArrayList<Projectiles> attackSOPTwo = new ArrayList<>();
    public ArrayList<Projectiles> attackSOPThree = new ArrayList<>();
    public ArrayList<Projectiles> attackSOPFour = new ArrayList<>();
    public ArrayList<Projectiles> attackSOPFive = new ArrayList<>();
    public ArrayList<Projectiles> attackSOPSix = new ArrayList<>();


    public GamePanel() {
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        space = new ImageIcon("res/space.gif");
        this.setDoubleBuffered(true); //Better rendering??? idk
        this.setFocusable((true)); //sets GamePanel to be "focused" to receive key input
        this.addKeyListener(keyH);
        this.requestFocusInWindow();
    }


    public void attackShipGenerator(int round){
        if(currentRound==1){
            roundGenerator.add(attackShipOne);
            roundGenerator.add(attackShipTwo);

        }
        else if (currentRound==2){
            roundGenerator.add(attackShipThree);
            roundGenerator.add(attackShipFour);
            roundGenerator.add(attackShipFive);
            roundGenerator.add(attackShipSix);
        }


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

        player.update(canMove, defense, new PlayerAttacks(this,player));
        for (int i = 0; i < defense.size(); i++) {
            defense.get(i).update();
        }

        long currentTime = System.nanoTime();

        if (currentTime - lastProjectileTime >= 2_000_000_000L) // 2 sec
        {
            if(attackShipOne.isAlive()){
                attackSOPOne.add(new Projectiles( this,attackShipOne));}
            if(attackShipTwo.isAlive()){
                attackSOPTwo.add(new Projectiles(this, attackShipTwo));}


            if(attackShipThree.isAlive()){
                attackSOPThree.add(new Projectiles( this,attackShipThree));}
            if(attackShipFour.isAlive()){
                attackSOPFour.add(new Projectiles(this, attackShipFour));}
            if(attackShipFive.isAlive()){
                attackSOPFive.add(new Projectiles( this,attackShipFive));}
            if(attackShipSix.isAlive()){
                attackSOPSix.add(new Projectiles(this, attackShipSix));}



            lastProjectileTime = currentTime;
        }

        if (attackShipOne.isAlive()){
            attackShipOne.update();}
        if(attackShipTwo.isAlive()){
            attackShipTwo.update();}
        if (attackShipThree.isAlive()){
            attackShipThree.update();}
        if(attackShipFour.isAlive()){
            attackShipFour.update();}
        if (attackShipFive.isAlive()){
            attackShipFive.update();}
        if(attackShipSix.isAlive()){
            attackShipSix.update();}



        for (int i = 0; i < attackSOPOne.size(); i++) {
            attackSOPOne.get(i).update();
        }
        for (int i = 0; i < attackSOPTwo.size(); i++) {

            attackSOPTwo.get(i).update();
        }



        for (int j = 0; j < attackSOPTwo.size(); j++) {
            if(general.collided(attackSOPTwo.get(j), player)&&attackShipTwo.isAlive()){
                System.out.println("temp");
                attackSOPTwo.remove(j);
                j--;
                takeHit(player,10);

            };
        }
        for (int j = 0; j < attackSOPOne.size(); j++) {
            if(general.collided(attackSOPOne.get(j), player)&&attackShipOne.isAlive()){
                System.out.println("temp");
                attackSOPOne.remove(j);
                j--;
                takeHit(player,10);

            }

            for (int i = 0; i < defense.size(); i++) {
                if(general.collided(defense.get(i), attackShipTwo)&&attackShipTwo.isAlive()){
                    defense.remove(i);
                    i--;
                    attackShipTwo.damage(10);
                    if (attackShipTwo.getHealth()<=0){
                        attackShipTwo.setAlive(false);
                        roundGenerator.remove(attackShipTwo);
                    }

                }
            }
            for (int i = 0; i < defense.size(); i++) {
                if(general.collided(defense.get(i), attackShipOne)&&attackShipOne.isAlive()){
                    defense.remove(i);
                    i--;
                    attackShipOne.damage(10);
                    if (attackShipOne.getHealth()<=0){
                        attackShipOne.setAlive(false);
                        roundGenerator.remove(attackShipOne);
                    }

                }
            }

        }





    }

    //This is a built in method in java where you can draw in Java
    // Graphics class draws objects on screen
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(space.getImage(), 0, 0, screenWidth, screenHeight, this);

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




        }

        player.draw(g2);
        if(attackShipOne.isAlive()){
            attackShipOne.draw(g2, Color.green);
            for (Projectiles p : attackSOPOne) {
                p.draw(g2, Color.green);
            }
        }
        if(attackShipTwo.isAlive()){
            attackShipTwo.draw(g2, Color.yellow);
            for (Projectiles p : attackSOPTwo) {
                p.draw(g2, Color.yellow);
            }

        }



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



}