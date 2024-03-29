//K. Chun 8/2018

//*******************************************************************************
//Import Section
//Add Java libraries needed for the game
//import java.awt.Canvas;

//Graphics Libraries

import com.sun.org.apache.xerces.internal.impl.xs.SchemaSymbols;

import java.awt.Graphics2D;
import java.awt.image.BufferStrategy;
import java.awt.*;
import javax.swing.*;

/***
 * Step 0 for keyboard control - Import
 */
import java.awt.event.*;

/***
 * Step 1 for keyboard control - implements KeyListener
 */
public class CheeseWorld implements Runnable, KeyListener {

    //Variable Definition Section

    //Sets the width and height of the program window
    final int WIDTH = 1000;
    final int HEIGHT = 650;

    //Declare the variables needed for the graphics
    public JFrame frame;
    public Canvas canvas;
    public JPanel panel;
    public BufferStrategy bufferStrategy;

    //Declare the variables needed for images
    public Image slingshotPic;
    public Image enemyPic;
    public Image tomPic;

    public Image campPic;

    public Image rockPic;

    public Image chipPic;

    public Image bearPic;

    public Image gamestart;

    public Image gameover;
    public int score;


    //Declare the character objects
    //public Mouse mouse1;
    public Enemy sally;
    public Enemy grizzly;

    public Enemy milly;
    public Rock pal;
    public Rock[] bunch;
    public int rocknumber = 0;

    public boolean gameStart = true;

    public slingshot theslingshot;
    public Player user;

    // Main method definition
    // This is the code that runs first and automatically
    public static void main(String[] args) {
        CheeseWorld myApp = new CheeseWorld(),World;   //creates a new instance of the game
        new Thread(myApp).start();               //creates a threads & starts up the code in the run( ) method
    }

    // Constructor Method - setup portion of the program
    // Initialize your variables and construct your program objects here.
    public CheeseWorld() {

        setUpGraphics();

        /***
         * Step 2 for keyboard control - addKeyListener(this) to the canvas
         */
        canvas.addKeyListener(this);

        //load images

        enemyPic = Toolkit.getDefaultToolkit().getImage("rabbit.png");
        rockPic = Toolkit.getDefaultToolkit().getImage("rock.png");
        bearPic= Toolkit.getDefaultToolkit().getImage("bear.png");
        chipPic = Toolkit.getDefaultToolkit().getImage("chip.png");
        tomPic = Toolkit.getDefaultToolkit().getImage("tomCat.png");
        slingshotPic= Toolkit.getDefaultToolkit().getImage("slingshot.png");
        campPic= Toolkit.getDefaultToolkit().getImage("camp.png");
        gamestart=Toolkit.getDefaultToolkit().getImage("gamestart.jpeg");
        gameover = Toolkit.getDefaultToolkit().getImage("game over.jpeg");



        //create (construct) the objects needed for the game
        //mouse1 = new Mouse(200, 300, 4, 4, mousePic);
        sally= new Enemy( 400, 500, 3, 3, enemyPic);
        milly= new Enemy( 200, 650, 3, 3, chipPic);
        grizzly= new Enemy( 300, 600, 3, 3, bearPic);
        theslingshot = new slingshot( 100, 300, 0, 0, slingshotPic);
        pal= new Rock(theslingshot.xpos+10, theslingshot.ypos+25,0, 0, rockPic);
        user = new Player(250, 250, 0, 0, tomPic);

        bunch= new Rock[10];
        for(int a=0;a<bunch.length; a=a+1)
        {
            bunch[a]= new Rock(theslingshot.xpos+10, theslingshot.ypos+25,0, 0, rockPic);

        }

    } // CheeseWorld()


//*******************************************************************************
//User Method Section

    // main thread
    // this is the code that plays the game after you set things up
    public void moveThings() {
        //mouse1.move();
        theslingshot.move();
        user.move();
       // pal.move();
        sally.move();
        milly.move();
        grizzly.move();

        for(int a=0;a<bunch.length; a=a+1) {
            if (bunch[a].isAlive == true) {
                bunch[a].move();
            }
        }
    }

    //collision
    public void collision () {

        for(int a=0;a<bunch.length; a=a+1)
        {
            if (bunch[a].rec.intersects(grizzly.rec)&& bunch[a].dx>0 && grizzly.isIntersecting==false){
                if(grizzly.health==0){
                grizzly.isAlive=false;}
                grizzly.health--;//this means grizzly.health=grizzly.health-1;
                grizzly.isIntersecting=true;
                score=score+5;
                grizzly.width = grizzly.width- 5;
                grizzly.height = grizzly.height- 5;
            }
            if(bunch[a].rec.intersects(grizzly.rec)==false){
                grizzly.isIntersecting=false;

            }
        }//loops through all the rocks in the bunch and checks if they intersect with grizzly
        for(int a=0;a<10; a=a+1)
        {
            if(bunch[a].rec.intersects(sally.rec)&&bunch[a].dx>0 && sally.isIntersecting==false){
                score=score+5;
                if(sally.health==0){
                    sally.isAlive=false;
                }
                sally.health--;//this means grizzly.health=grizzly.health-1;
                sally.width = sally.width- 5;
                sally.height = sally.height- 5;

            }
        }
        for(int a=0;a<10; a=a+1) {
            if (bunch[a].rec.intersects(milly.rec) && bunch[a].dx > 0 && milly.isIntersecting==false) {
                score = score + 5;
                if(milly.health==0){
                    milly.isAlive=false;
                }
                milly.health--;//this means grizzly.health=grizzly.health-1;
                milly.width = milly.width- 5;
                milly.height = milly.height- 5;

            }
        }

    }





    public void checkIntersections() {

    }

    public void run() {
        while (true) {
            moveThings();
            //move all the game objects
            collision();
            checkIntersections();   // check character crashes
            render();               // paint the graphics
            pause(20);         // sleep for 20 ms
        }
    }

    //paints things on the screen using bufferStrategy
    public void render() {
        //boolean startscreen=true;
        Graphics2D g = (Graphics2D) bufferStrategy.getDrawGraphics();
        g.clearRect(0, 0, WIDTH, HEIGHT);
      /*  if(startscreen==true){
            g.setColor(Color.RED);
            g.drawRect(0,0,WIDTH,HEIGHT);

        }*/
        //draw characters to the screen

        if(gameStart){
            g.drawImage(gamestart, 0, 0, 1000, 700, null);

        }else{
            g.drawImage(campPic, 0, 0, 1000, 700, null);
            if(grizzly.isAlive==true) {
                g.drawImage(bearPic, grizzly.xpos, grizzly.ypos,grizzly.width, grizzly.height, null);
            }

            if(milly.isAlive == true) {
                g.drawImage(chipPic, milly.xpos, milly.ypos, milly.width, milly.height, null);
            }

            if(sally.isAlive == true) {
                g.drawImage(enemyPic, sally.xpos, sally.ypos, sally.width, sally.height, null);
            }
            g.drawImage(rockPic, pal.xpos, pal.ypos, pal.width, pal.height,  null);

            for(int a=0;a<bunch.length; a=a+1) {
                if (bunch[a].isAlive == true) {
                    g.drawImage(rockPic, bunch[a].xpos, bunch[a].ypos, bunch[a].width, bunch[a].height, null);
                }
            }
            g.setColor(Color.BLUE);
            g.setFont(new Font("TimesRoman", Font.BOLD, 25));
            g.drawString("score" + score, 900, 50);

            // g.drawImage(mouse1.pic, mouse1.xpos, mouse1.ypos, mouse1.width, mouse1.height, null);
            //g.drawImage(user.pic, user.xpos, user.ypos, user.width, user.height, null);
            g.drawImage(theslingshot.pic, theslingshot.xpos, theslingshot.ypos, theslingshot.width, theslingshot.height, null);

            if(rocknumber==10)
            {
                g.drawImage(gameover, 0, 0, 1000, 700, null);



            }


        }



        g.dispose();
        bufferStrategy.show();
    }


    /***
     * Step 3 for keyboard control - add required methods
     * You need to have all 3 even if you aren't going to use them all
     */
    public void keyPressed(KeyEvent event) {
        //This method will do something whenever any key is pressed down.
        //Put if( ) statements here
        char key = event.getKeyChar();     //gets the character of the key pressed
        int keyCode = event.getKeyCode();  //gets the keyCode (an integer) of the key pressed
        System.out.println("Key Pressed: " + key + "  Code: " + keyCode);

        if (keyCode == 68) { // d
            user.right = true;

        }
        if (keyCode == 65) { // a
            user.left = true;
        }

        if (keyCode == 83) { // s
            user.down = true;
        }
        if (keyCode == 87) { // w
            user.up = true;
        }

        if (keyCode == 32) { // spacebar
//            pal.dx = 40;
            bunch[rocknumber].isAlive = true;
            bunch[rocknumber].dx = 40;
            System.out.println("rocknumber test: " +rocknumber);
            rocknumber++;

        }

        if(keyCode==83) {
            gameStart=false;
        }



    }//keyPressed()

    public void keyReleased(KeyEvent event) {
        char key = event.getKeyChar();
        int keyCode = event.getKeyCode();
        //This method will do something when a key is released
        if (keyCode == 68) { // d
            user.right = false;
        }
        if (keyCode == 65) { // a
            user.left = false;
        }
        if (keyCode == 83) { // s
            user.down = false;
        }
        if (keyCode == 87) { // w
            user.up = false;
        }

    }//keyReleased()

    public void keyTyped(KeyEvent event) {
        // handles a press of a character key (any key that can be printed but not keys like SHIFT)
        // we won't be using this method, but it still needs to be in your program
    }//keyTyped()


    //Graphics setup method
    public void setUpGraphics() {
        frame = new JFrame("CheeseWorld");   //Create the program window or frame.  Names it.

        panel = (JPanel) frame.getContentPane();  //sets up a JPanel which is what goes in the frame
        panel.setPreferredSize(new Dimension(WIDTH, HEIGHT));  //sizes the JPanel
        panel.setLayout(null);   //set the layout

        // creates a canvas which is a blank rectangular area of the screen onto which the application can draw
        // and trap input events (Mouse and Keyboard events)
        canvas = new Canvas();
        canvas.setBounds(0, 0, WIDTH, HEIGHT);
        canvas.setIgnoreRepaint(true);

        panel.add(canvas);  // adds the canvas to the panel.

        // frame operations
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  //makes the frame close and exit nicely
        frame.pack();  //adjusts the frame and its contents so the sizes are at their default or larger
        frame.setResizable(false);   //makes it so the frame cannot be resized
        frame.setVisible(true);      //IMPORTANT!!!  if the frame is not set to visible it will not appear on the screen!

        // sets up things so the screen displays images nicely.
        canvas.createBufferStrategy(2);
        bufferStrategy = canvas.getBufferStrategy();
        canvas.requestFocus();
        System.out.println("DONE graphic setup");


    }

    //Pauses or sleeps the computer for the amount specified in milliseconds
    public void pause(int time) {
        //sleep
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {

        }
    }

}//class
