import javax.swing.JPanel;
import javax.swing.Timer;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 *
 * @author HP
 */

class GamePlay extends JPanel implements KeyListener,ActionListener
{
    private boolean play = true;
    private int score = 0;
    private int totalbricks = 21;
    private Timer timer;
    private int delay = 8;
    private int playerX = 310;
    private int ballposX = 120;
    private int ballposY = 350;
    private int ballXdir = -1;
    private int ballYdir = -2;
    private MapGenerator map;


    public GamePlay() {

        map = new MapGenerator(3,7);
        addKeyListener(this);
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);
        timer = new Timer(delay,this);
        timer.start();

    }
    public void paint(Graphics g)
    {
        g.setColor(Color.black);
        g.fillRect(1,1,692,592);

        map.draw((Graphics2D)g);

        //bordering yellow
        g.setColor(Color.yellow);
        g.fillRect(0,0,3,592);
        g.fillRect(0,0,692,3);
        g.fillRect(681,0,3,592);

        g.setColor(Color.white);
        g.setFont(new Font("serif" ,Font.BOLD,25));
        g.drawString("" + score,590,30);

        //slider
        g.setColor(Color.yellow);
        g.fillRect(playerX,550,100,8);

        //ball
        g.setColor(Color.green);
        g.fillOval(ballposX ,ballposY , 20, 20);

        if(ballposY>570)
        {
            play = false;
            ballXdir =0;
            ballYdir =0;
            g.setColor(Color.red);
            g.setFont(new Font("serif" ,Font.BOLD,30));
            g.drawString("  GAME OVER SCORE : " + score,190,420);

            g.setFont(new Font("serif" ,Font.BOLD,30));
            g.drawString(" PLEASE ENTER TO RESTART " ,230 ,340);
        }

        if(totalbricks <= 0)
        {
            play = false;
            ballXdir =0;
            ballYdir =0;
            g.setColor(Color.red);
            g.setFont(new Font("serif" ,Font.BOLD,30));
            g.drawString("  GAME OVER SCORE : " + score,190,420);

            g.setFont(new Font("serif" ,Font.BOLD,30));
            g.drawString(" PLEASE ENTER TO RESTART " ,230 ,340);
        }
        g.dispose();
    }


    @Override
    public void actionPerformed(ActionEvent arg0)
    {
        timer.start();

        if(play)
        {
            if(new Rectangle(ballposX , ballposY ,20 , 20).intersects(new Rectangle(playerX , 550 , 100, 8)))
            {
                ballYdir = -ballYdir;
            }

            for(int i =0;i<map.map.length;i++)
            {
                for(int j =0;j<map.map[0].length;j++)
                {
                    if(map.map[i][j]>0)
                    {
                        int brickX = j*map.bricksWidth + 80;
                        int brickY = i*map.bricksHeight + 50;
                        int brickWidth= map.bricksWidth;
                        int brickHeight = map.bricksHeight;

                        Rectangle rect = new Rectangle(brickX, brickY, brickWidth, brickHeight);
                        Rectangle ballRect = new Rectangle(ballposX, ballposY, 20,20);
                        Rectangle brickRect = rect;

                        if(ballRect.intersects(brickRect) )
                        {
                            map.setBricksValue(0, i, j);
                            totalbricks--;
                            score+=5;

                            if(ballposX + 19 <= brickRect.x || ballposX +1 >= brickRect.x + brickRect.width)
                            {
                                ballXdir = -ballXdir;
                            }
                            else
                            {
                                ballYdir = -ballYdir;
                            }
                        }

                    }
                }
            }

            ballposX += ballXdir;
            ballposY += ballYdir;
            if(ballposX < 0) { // if ball hits the left wall then it bounces back
                ballXdir = -ballXdir;
            }
            if(ballposY < 0) {  // if ball hits the top wall then it bounces back
                ballYdir = -ballYdir;
            }
            if(ballposX > 670) { // if ball hits the right wall then it bounces back
                ballXdir = -ballXdir;
            }

        }
        repaint();
    }
    @Override
    public void keyTyped(KeyEvent arg0) {

    }

    @Override
    public void keyPressed(KeyEvent arg0) {
        if(arg0.getKeyCode() == KeyEvent.VK_RIGHT) { // if right arrow key is pressed then paddle moves right
            if(playerX >= 600) {
                playerX = 600;
            } else {
                moveRight();

            }
        }
        if(arg0.getKeyCode() == KeyEvent.VK_LEFT) { // if left arrow key is pressed then paddle moves left
            if(playerX < 10) {
                playerX = 10;
            } else {
                moveLeft();

            }
        }

        if(arg0.getKeyCode() == KeyEvent.VK_ENTER) { // if enter key is pressed then game restarts
            if(!play) {
                play = true;
                ballposX = 120;
                ballposY = 350;
                ballXdir = -1;
                ballYdir = -2;
                score = 0;
                totalbricks = 21;
                map = new MapGenerator(3,7);

                repaint();
            }
        }

    }
    public void moveRight() { // paddle moves right by 50 pixels
        play = true;
        playerX += 50;
    }
    public void moveLeft() { // paddle moves left by 50 pixels
        play = true;
        playerX -= 50;
    }



    @Override
    public void keyReleased(KeyEvent arg0) {

    }
}