package org.example;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;

public class Ball extends Rectangle{

    Random random;
    int xVelocity;
    int yVelocity;
    int initialSpeed = 2;

    Ball(int x, int y, int width, int height){
        super(x,y,width,height);
        random = new Random();
        int randomXDireciton = random.nextInt(2);
        if (randomXDireciton == 0) {
            randomXDireciton--;
        }
        setXDirection(randomXDireciton*initialSpeed);

        int randomYDirection = random.nextInt(2);
        if(randomYDirection==0) {
            randomYDirection--;
        }
        setYDirection(randomYDirection*initialSpeed);
    }
    Ball (Ball b){
        super(b.x,b.y,b.width,b.height);
        xVelocity = b.xVelocity;
        yVelocity = b.yVelocity;
    }

    public void setXDirection(int randomXDirection){
        xVelocity = randomXDirection;
    }
    public void setYDirection(int randomYDirection){
        yVelocity = randomYDirection;
    }
    public void move(){
        x+=xVelocity;
        y+=yVelocity;
    }
    public void draw(Graphics g){
        g.setColor(Color.white);
        g.fillOval(x,y,width,height);
    }
}
