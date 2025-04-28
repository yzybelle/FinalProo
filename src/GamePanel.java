import javax.swing.*;
import java.awt.*;


public class GamePanel extends JPanel implements Runnable {
    // SCREEN SETTINGS
    final int originalTileSize = 16; //16x16 tile
    final int scale = 3;

    final int tileSize = originalTileSize * scale; //48x48 tile
    final int maxScreenCol = 16;
    final int maxScreenRow = 12;

    final int screenWidth = tileSize*maxScreenCol; //768 pixels
    final int screenHeight = tileSize * maxScreenRow; //576 pixels
    //FPS
    int FPS = 60;

    KeyHandler keyH = new KeyHandler();
    //TO create time, or a game clock we use a class called Thread
    Thread gameThread;

    // Set player's default position

    int playerX = 100;
    int playerY = 100;
    int playerSpeed = 4;

    public GamePanel(){
        this.setPreferredSize(new Dimension(screenWidth,screenHeight));
        this.setBackground(Color.MAGENTA); //Sets BackGround Color
        this.setDoubleBuffered(true); //Better rendering??? idk
        this.setFocusable((true)); //sets GamePanel to be "focused" to receive key input
    }

    public void startGameThread(){
        gameThread = new Thread(this);
        gameThread.start();
    }
    @Override
    public void run() {

        double drawInterval = 1000000000/FPS;
        //Create game loop
        while(gameThread!=null){

            //this is the time
            long currentTime = System.nanoTime();
            System.out.println("current tune:" +currentTime);

            //Updates information such as character positions
            //Draw the screen with updated information
            update();
            repaint(); // This calls paintComponent
        }
    }
    //This method "updates" the screen, like how a game runs and changes frames at 20 FPS
    public void update(){
      if (keyH.upPressed==true){
          playerY-=playerSpeed;
      }
      else if (keyH.downPressed == true) {
          playerY+= playerSpeed;
      } else if (keyH.rightPressed == true) {
          playerX+= playerSpeed;
      } else if (keyH.leftPressed == true) {
          playerX-=playerSpeed;
      }
    }
    //This is a built in method in java where you can draw in Java
    // Graphics class draws objects on screen
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D)g; // This changes Graphics to graphics 2d which allows for more functions
        g2.setColor(Color.GRAY);
        g2.fillRect(playerX,playerY,tileSize,tileSize);
        g2.dispose();
    }
}
