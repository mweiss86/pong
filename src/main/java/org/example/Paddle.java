package org.example;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;

public class Paddle extends Rectangle{

    int id;
    int yVelocity;
    int speed = 10;
    int paddle_height;

    Paddle(int x, int y, int PADDLE_WIDTH, int PADDLE_HEIGHT, int ID){
        super(x,y,PADDLE_WIDTH,PADDLE_HEIGHT);
        this.id = ID ;
        this.paddle_height = PADDLE_HEIGHT;
    }

    public void keyPressed(KeyEvent e){
        switch (id) {
            case 1:
                if (e.getKeyCode() == KeyEvent.VK_W) {
                    setYDirection(-speed);
                }
                if (e.getKeyCode() == KeyEvent.VK_S) {
                    setYDirection(speed);
                }
                break;
            case 2:
                if (e.getKeyCode() == KeyEvent.VK_UP) {
                    setYDirection(-speed);
                }
                if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                    setYDirection(speed);
                }
                break;
        }
    }
    public void keyReleased(KeyEvent e){
        switch (id){
            case 1:
                if (e.getKeyCode() == KeyEvent.VK_W) {
                    setYDirection(0);
                }
                if (e.getKeyCode() == KeyEvent.VK_S) {
                    setYDirection(0);
                }
                break;
            case 2:
                if (e.getKeyCode() == KeyEvent.VK_UP) {
                    setYDirection(0);
                }
                if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                    setYDirection(0);
                }
                break;
        }
    }
    public void setYDirection(int yDirection){
        yVelocity = yDirection;
    }
    public void move(){
        y = y + yVelocity;
    }
    public void pcMove(int moveToY){
        int center = y + paddle_height/2;
        if(Math.abs(center-moveToY) > speed) {
            if (center > moveToY) {
                y -= speed;
            }
            if (center < moveToY) {
                y += speed;
            }
        }
    }
    public void draw (Graphics g){
        if(id==1)
        {
            g.setColor(Color.blue);
        }
        else {
            g.setColor(Color.red);
        }
        g.fillRect(x,y,width,height);
    }
}
