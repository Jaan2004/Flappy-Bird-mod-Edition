import java.awt.*;
import java.util.Random;
import java.util.ArrayList;
import java.awt.event.*;
import javax.swing.*;
import java.lang.Override;



public class flappybird extends JPanel implements  ActionListener,KeyListener {
    int boardheight = 640;
    int boardwidth = 360;
 
    Image backgroundi;
    Image birdimg;
    Image tpi;
    Image bpi;
    Image dragonimg;
    private boolean showDragon = false; // Flag to show/hide dragon
private int dragonX, dragonY;

//   bird
int birdx= boardwidth/8;
int birdy= boardheight/2;
int birdwidth= 34;
int birdheight=24;


class Bird {
    int x=birdx;
    int y=birdy;
    int width =birdwidth;
    int height=birdheight;
    Image img;

    Bird(Image img){
        this.img= img;
    }

}

 //   pipes
int pipex=boardwidth;
    int pipey=0;
    int pipewidth=64;  //scaled by 1/6
    int pipeheight=512;

    class Pipe {
        int x =pipex;
        int y= pipey;
        int width=pipewidth;
        int height=pipeheight;
        Image img;
        boolean passed=false;

        Pipe(Image img){
          this.img=img;    
        }

    }
    
    class Dragon {
        int x;
        int y;
        int width = 50; // Set the width of the dragon
        int height = 50; // Set the height of the dragon
        Image img;

        Dragon(Image img) {
            this.img = img;
            this.x = boardwidth; // Start off-screen
            this.y = random.nextInt(boardheight - height); // Random vertical position
        }
    }

// game logic
      Bird bird1;
      int velocityx=-4;
      int velocityY=0;
      int gravity=1;

      ArrayList<Pipe> pipes;
      Random random= new Random();

      Timer gameloop;
      Timer placePipesTimer;
      boolean gameover=false;
      double score=0;
    
    
      Dragon dragon; // Declare the dragon instance

      

     
   flappybird() {

    

        setPreferredSize(new Dimension(boardwidth,boardheight));
        // setBackground(Color.BLUE);
        setFocusable(true);
        addKeyListener(this);
    //    load images
    backgroundi= new ImageIcon(getClass().getResource("./flappybirdbg.png")).getImage();
    birdimg=  new ImageIcon(getClass().getResource("./flappybird.png")).getImage();
    tpi=new ImageIcon(getClass().getResource("./toppipe.png")).getImage();
    bpi= new ImageIcon(getClass().getResource("./bottompipe.png")).getImage();
    dragonimg = new ImageIcon(getClass().getResource("./dragon.png")).getImage();

      bird1=new Bird(birdimg);
      pipes=new ArrayList<Pipe>();
      dragon = new Dragon(dragonimg);
      

      placePipesTimer=new Timer(1500,new ActionListener() {
        @Override
        public void  actionPerformed(ActionEvent e) {
            placePipes();
         
        }
      });

      placePipesTimer.start();

      gameloop= new Timer(1000/60, this);
      gameloop.start(); 

    }

    int openingspace=boardheight/4;
    public void placePipes(){
        int randompipeY= (int)(pipey - pipeheight/4- Math.random()*pipeheight/2);
       

        Pipe toPipe=new Pipe(tpi);
        toPipe.y=randompipeY;
        pipes.add(toPipe);

        Pipe boPipe=new Pipe(bpi);
        boPipe.y= toPipe.y + openingspace + pipeheight;
        pipes.add(boPipe);
        
    }
    public void gameLoop() {
        // Game loop logic...
        Random random = new Random();
int dragonAppearanceInterval = 100;
        // Randomly determine if the dragon should appear
        if (random.nextInt(dragonAppearanceInterval) == 0) {
            showDragon = true;
            dragonX = random.nextInt(boardwidth - dragonimg.getWidth(null));
            dragonY = random.nextInt(boardheight / 2);
        } else {
            showDragon = false;
        }
    
        // Update game state and repaint
        repaint();
    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);
        draw(g);

    }
    public void draw(Graphics g){
        g.drawImage(backgroundi,0, 0,boardwidth,boardheight,null);
        g.drawImage(bird1.img,bird1.x,bird1.y,bird1.width,bird1.height,null);
        g.drawImage(dragon.img, dragon.x, dragon.y, dragon.width, dragon.height, null ); // Draw the dragon

        for (int i=0;i<pipes.size();i++){
     Pipe pipe= pipes.get(i);
     g.drawImage(pipe.img, pipe.x, pipe.y, pipe.width, pipe.height, null);
     
        }
        if (showDragon) {
            g.drawImage(dragonimg, dragonX, dragonY, null); // Draw the dragon at the random position
        }

        //score
        g.setColor(Color.white);
        g.setFont(new Font("Arial", Font.PLAIN, 32));
        if(gameover){
            g.drawString("GAME OVER : "+  String.valueOf((int )score),10,35);
        }
        else{
            g.drawString(String.valueOf((int) score),10,35);
        }
    }

   public void  move(){
    velocityY+= gravity;
    bird1.y += velocityY;
    bird1.y = Math.max(bird1.y,0);

    dragon.x += velocityx; // Move the dragon left

        // Check for collision with the dragon
        if (collisionWithDragon(bird1, dragon)) {
            gameover = true; // End the game if the bird hits the dragon
        }

    // pipes
    for(int i=0; i<pipes.size(); i++){
        Pipe pipe=pipes.get(i);
        pipe.x+=velocityx;
 
if (!pipe.passed && bird1.x>pipe.x+ pipe.width){
    pipe.passed=true;
    score+=0.5;
    
} 
 if(score>99){
    openingspace=0;
    
 }

        if(collision(bird1,pipe)){
            gameover=true;
        }
    }

    if(bird1.y>boardheight){
        gameover=true;
    }
    
   }

   public boolean collision(Bird a,Pipe b){
  return a.y>b.y &&
   a.y<b.y + b.height && 
   a.x>b.x &&
  a.x<b.x+ b.width
   && a.y>0;
   }
  
//     if(){
//         gameover=true;
//    }
 public boolean collisionWithDragon(Bird a, Dragon d) {
        return a.y > d.y &&
               a.y < d.y + d.height &&
               a.x > d.x &&
               a.x < d.x + d.width &&
               a.y > 0;
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        move();
        repaint();
        if(gameover){
            placePipesTimer.stop();
            gameloop.stop();
        }
    }

    

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode()== KeyEvent.VK_SPACE){
            velocityY=-9;
            if (gameover){
                bird1.y= birdy;
                velocityY=0;
                pipes.clear();
                score=0;
                gameover=false;
                gameloop.start();
                placePipesTimer.start();

            }
        }

    }


    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {
    
    }
}

//Throughout the tutorial, you will learn how to create the game loop, create a jframe and jpanel, 
// draw images on the jpanel, add click handlers to make the flappy bird jump, randomly generate pipes 
// and move them across the screen, detect collisions between the flappy bird and each pipe, and add a running score. 
