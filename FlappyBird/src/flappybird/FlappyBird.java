/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package flappybird;

import javax.swing.JFrame;
import java.awt.Graphics;
import javax.swing.Timer;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;
import java.awt.event.ActionEvent;
import java.awt.Rectangle;
import java.awt.Color;
import java.util.ArrayList; 
import java.util.Random;
import java.awt.Font;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

/**
 *
 * @author dylan
 */
public class FlappyBird implements ActionListener, MouseListener, KeyListener{

    /**
     * @param args the command line arguments
     */
    
    public static FlappyBird flappyBird;
    
    public final int WIDTH = 1200, HEIGHT = 800;
    
    public Renderer renderer;    
    public Rectangle bird;
    public boolean gameOver, started = false;
    public ArrayList<Rectangle>  columns;
    public Random rand;
    public int ticks, yMotion, score;
    private BufferedImage myImage;
    public int space = 350;
    public int speed = 5;
    
    public FlappyBird(){
        JFrame jframe = new JFrame();
        Timer timer = new Timer(20, this);
        
        renderer = new Renderer();
        
        jframe.add(renderer);
        jframe.setTitle("Flappy Bird");
        jframe.setDefaultCloseOperation(jframe.EXIT_ON_CLOSE);
        jframe.setSize(WIDTH, HEIGHT);
        jframe.addMouseListener(this);
        jframe.addKeyListener(this);
        jframe.setResizable(false);
        jframe.setVisible(true);
        
        rand = new Random();
        columns = new ArrayList<Rectangle>();
        
        
        addColumns(true);
        addColumns(true);
        addColumns(true);
        addColumns(true);
        
        try {
            myImage = ImageIO.read(new File("F:\\JavaProject\\FlappyBird\\src\\flappybird\\bird.png"));
            } catch (Exception ex) {
            ex.printStackTrace();
        }
        bird = new Rectangle(WIDTH/4 - 10, HEIGHT/2 - 10, myImage.getWidth(), myImage.getHeight());
        timer.start();
    }
    
    public void jump(){
        if(gameOver){
            bird = new Rectangle(WIDTH/4 - 10, HEIGHT/2 - 10, myImage.getWidth(), myImage.getHeight());
            columns.clear();
            score = 0;
            yMotion = 0;
            
            addColumns(true);
            addColumns(true);
            addColumns(true);
            addColumns(true);
            
            gameOver = false;
        }
        if(!started){
            started = true;
        }else if(!gameOver){
            if(yMotion > 0){
                yMotion = 0;
            }
            yMotion -= 10;
        }
    }
    
    @Override
    public void actionPerformed(ActionEvent e){
        //int speed = 10;
        
        ticks++;  
        
        if(started){
            for (int i = 0; i < columns.size(); i++){
                Rectangle column = columns.get(i);
                column.x -= speed;
            }

            if(ticks % 2 == 0 && yMotion < 15){
                yMotion += 2;
            }

            for (int i = 0; i < columns.size(); i++){
                Rectangle column = columns.get(i);
                if(column.x + column.width < 0){
                    columns.remove(column);
                    if(column.y == 0){
                        addColumns(false);
                    }
                }
            }

            bird.y += yMotion;

            for (int i = 0; i < columns.size(); i++){
                Rectangle column = columns.get(i);
                
                if(column.y == 0 && bird.x + bird.width /2 > column.x + column.width/2 - 3 && bird.x + bird.width /2 < column.x + column.width/2 + 3){
                    score++;
                    if(score > 1 && score %10 == 0) {
                        if(space > 200) space -= 5;
                        speed++;
                    }
                }
                
                
                if(column.intersects(bird)){
                    gameOver = true;
                    bird.x = column.x - bird.width;
                }
            }
            if(bird.y < 0 || bird.y > HEIGHT - 120){
                gameOver = true;
            }
            if(bird.y + yMotion >= HEIGHT - 100) {
                bird.y = HEIGHT - 120 - bird.height;
            }
        }

        renderer.repaint();
    }
    
    public void addColumns(boolean start){
        
        int width = 100;
        int height = 50 +  rand.nextInt(300);
        
        if(start){
            columns.add(new Rectangle(WIDTH + width + columns.size() * 300, HEIGHT - height - 120, width, height));
            columns.add(new Rectangle(WIDTH + width + (columns.size() - 1) * 300, 0, width, HEIGHT - height - space));
        }else{
            columns.add(new Rectangle(columns.get(columns.size()-1).x + 600, HEIGHT - height - 120, width, height));
            columns.add(new Rectangle(columns.get(columns.size()-1).x, 0, width, HEIGHT - height - space));        
        } 
    }
    
    public void paintColumn(Graphics g, Rectangle column){
        g.setColor(Color.green.darker());
        g.fillRect(column.x, column.y, column.width, column.height);
    
    }
    
    public void repaint(Graphics g){
        g.setColor(Color.cyan);
        g.fillRect(0, 0, WIDTH, HEIGHT);
        
        g.setColor(Color.orange);
        g.fillRect(0, HEIGHT-120, WIDTH, 120);
         g.setColor(Color.green);
        g.fillRect(0, HEIGHT-120, WIDTH, 20);
        
        g.drawImage(myImage, bird.x, bird.y, renderer);
        //g.setColor(Color.red);
        //g.fillRect(bird.x, bird.y, bird.width, bird.height);
        
        
        for (Rectangle column : columns){
            paintColumn(g, column);
        }
        
        g.setColor(Color.white);
        g.setFont(new Font("Arial", 1, 100));
        if(!started){
            g.drawString("Click to Start!", 175, HEIGHT/2-50);
        }
        if(gameOver){
            g.drawString("GAME OVER!", 250, HEIGHT/2 - 50);
        }
        if(!gameOver && started){
            g.drawString(String.valueOf(score), WIDTH - 200, 100);
        }
    }
    
    public static void main(String[] args) {
        // TODO code application logic here
        flappyBird = new FlappyBird();
    }
    
    @Override
    public void mouseClicked(MouseEvent e){
        jump();
    }
    
    @Override
    public void mousePressed(MouseEvent e){
    
    }
    @Override
    public void mouseReleased(MouseEvent e){
    
    }
    @Override
    public void mouseEntered(MouseEvent e){
    
    }
    @Override
    public void mouseExited(MouseEvent e){
    
    }
    @Override
    public void keyTyped(KeyEvent e){
    
    }
    @Override
    public void keyPressed(KeyEvent e){
    
    }
    @Override
    public void keyReleased(KeyEvent e){
        if(e.getKeyCode() == KeyEvent.VK_SPACE){
            jump();
        }
    }
}
