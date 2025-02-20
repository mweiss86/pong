package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Random;

public class GamePanel extends JPanel implements Runnable {

    static final int GAME_WIDTH = 1000;
    static final int GAME_HEIGHT = (int) (GAME_WIDTH * (0.5555));
    static final Dimension SCREEN_SIZE = new Dimension(GAME_WIDTH, GAME_HEIGHT);
    static final int BALL_DIAMETER = 20;
    static final int PADDLE_WIDTH = 25;
    static final int PADDLE_HEIGHT = 100;
    static final int UPS = 10; //Updates per second (game logic speed) - higher = faster | lower = slower
    static final int FPS = 60; // Frames per second (rendering speed)
    static final long UPDATE_INTERVAL = 1000 / UPS;        // Time per update in ms
    static final long FRAME_TIME = 1000 / FPS;
    int fps;
    int frameCount;
    long lastFpsUpdate = System.currentTimeMillis();// Time per frame in ms
    int detectCollideY = -1;
    boolean goTo = false;
    Thread gameThread;
    Image image;
    Graphics graphics;
    Random random;
    Paddle paddle1;
    Paddle paddle2;
    Ball ball;
    Ball futureball;
    Score score;


    public GamePanel() {
        this.setPreferredSize(SCREEN_SIZE);
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdapter());
        this.setDoubleBuffered(true);
        this.setBackground(Color.BLACK);
        newPaddles();
        newBall();
        score = new Score(GAME_WIDTH, GAME_HEIGHT);

        gameThread = new Thread(this);
        gameThread.start();
    }

    public void paint(Graphics g) {
        image = createImage(getWidth(), getHeight());
        graphics = image.getGraphics();
        draw(graphics);
        g.drawImage(image, 0, 0, this);
    }

    public void draw(Graphics g) {
        paddle1.draw(g);
        paddle2.draw(g);
        ball.draw(g);
        score.draw(g);
       // futureball.draw(g);
        Toolkit.getDefaultToolkit().sync();

        // Draw FPS counter
        g.setColor(Color.white);
        g.setFont(new Font("Arial", Font.PLAIN, 20));
        g.drawString("FPS: " + fps, 10, 20);

        // Update FPS
        frameCount++;
        if (System.currentTimeMillis() - lastFpsUpdate >= 1000) {
            fps = frameCount;
            frameCount = 0;
            lastFpsUpdate = System.currentTimeMillis();
        }
    }

    public void newPaddles() {
        paddle1 = new Paddle(0, (GAME_HEIGHT / 2) - (PADDLE_HEIGHT / 2), PADDLE_WIDTH, PADDLE_HEIGHT, 1);
        paddle2 = new Paddle(GAME_WIDTH - PADDLE_WIDTH, (GAME_HEIGHT / 2) - (PADDLE_HEIGHT / 2), PADDLE_WIDTH, PADDLE_HEIGHT, 2);

    }

    public void newBall() {
        random = new Random();
        ball = new Ball((GAME_WIDTH / 2) - (BALL_DIAMETER / 2), random.nextInt(GAME_HEIGHT - BALL_DIAMETER), BALL_DIAMETER, BALL_DIAMETER);
        futureball = new Ball(ball);
    }

    public void move() {
        paddle1.move();
        //paddle2.move();
        if(Math.abs((paddle2.height/2)-detectCollideY) < 3 && !goTo){
            goTo = true;
        }
        if (!goTo) {
            paddle2.pcMove(detectCollideY);
        }
        ball.move();
        futureball.move();
    }

    public void checkCollision() {
        //Ball hits Top or Bottom border
        if (ball.y <= 0) {
            ball.setYDirection(-ball.yVelocity);
        }
        if (ball.y >= GAME_HEIGHT - BALL_DIAMETER) {
            ball.setYDirection(-ball.yVelocity);
        }
        //Ball hits Paddles
        if (ball.intersects(paddle1)) {
            ball.xVelocity = Math.abs(ball.xVelocity);
            ball.xVelocity++;
            // if (ball.yVelocity > 0) {
            //     ball.yVelocity++;
            // }
            ball.yVelocity--;
            ball.setXDirection(ball.xVelocity);
            ball.setYDirection(ball.yVelocity);
            futureball = new Ball(ball);
            detectCollideY = -1;
        }
        if (ball.intersects(paddle2)) {
            ball.xVelocity = Math.abs(ball.xVelocity);
            ball.xVelocity++;
            // if (ball.yVelocity > 0) {
            //     ball.yVelocity++;
            // }
            ball.yVelocity--;
            ball.setXDirection(-ball.xVelocity);
            ball.setYDirection(ball.yVelocity);
        }
        // Ball Collision prediction
        if (detectCollideY == -1) {

            for (int i = 0; i < 10; i++) {
                futureball.move();
                if (futureball.intersects(paddle1)) {
                    futureball.xVelocity = Math.abs(futureball.xVelocity);
                    futureball.xVelocity++;
                    // if (ball.yVelocity > 0) {
                    //     ball.yVelocity++;
                    // }
                    futureball.yVelocity--;
                    futureball.setXDirection(futureball.xVelocity);
                    futureball.setYDirection(futureball.yVelocity);
                }
                if (futureball.y <= 0) {
                    futureball.setYDirection(-futureball.yVelocity);
                }
                if (futureball.y >= GAME_HEIGHT - BALL_DIAMETER) {
                    futureball.setYDirection(-futureball.yVelocity);
                }
                if (futureball.intersects(paddle2)) {
                    futureball.xVelocity = Math.abs(futureball.xVelocity);
                    futureball.xVelocity++;
                    // if (futureball.yVelocity > 0) {
                    //     futureball.yVelocity++;
                    // }
                    futureball.yVelocity--;
                    futureball.setXDirection(-futureball.xVelocity);
                    futureball.setYDirection(futureball.yVelocity);
                }
                if (futureball.x >= GAME_WIDTH - BALL_DIAMETER) {
                    detectCollideY = futureball.y;
                    break;
                }

            }

        }
        //Ball scores
        if (ball.x >= GAME_WIDTH - BALL_DIAMETER) {
            score.p1++;
            newPaddles();
            newBall();
        }
        if (ball.x <= 0) {
            score.p2++;
            newPaddles();
            newBall();
        }
        //Paddle hit top or bottom border
        if (paddle1.y <= 0) {
            paddle1.y = 0;
        }
        if (paddle1.y >= GAME_HEIGHT - PADDLE_HEIGHT) {
            paddle1.y = GAME_HEIGHT - PADDLE_HEIGHT;
        }
        if (paddle2.y <= 0) {
            paddle2.y = 0;
        }
        if (paddle2.y >= GAME_HEIGHT - PADDLE_HEIGHT) {
            paddle2.y = GAME_HEIGHT - PADDLE_HEIGHT;
        }
    }


    public void run() {
        //gameloop
        long lastTime = System.nanoTime();
        double amountOfTicks = 60.0;
        double ns = 1000000000 / amountOfTicks;
        double delta = 0;

        while (true) {
            long now = System.nanoTime();
            delta += (now - lastTime) / ns;
            lastTime = now;
            if (delta >= 1) {
                move();
                checkCollision();
                repaint();
                delta--;
            }
        }

        /*
        long lastUpdate = System.currentTimeMillis();
        long lastFrame = System.currentTimeMillis();

        while (true) {
            long currentTime = System.currentTimeMillis();

            // Update Game Logic at UPS rate
            if (currentTime - lastUpdate >= UPDATE_INTERVAL) {
                updateGame();
                lastUpdate = currentTime;
            }

            // Render the Game at FPS rate
            if (currentTime - lastFrame >= FRAME_TIME) {
                repaint();
                lastFrame = currentTime;
            }
        }

         */
    }

    private class MyKeyAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            paddle1.keyPressed(e);
            //paddle2.keyPressed(e);
        }

        public void keyReleased(KeyEvent e) {
            paddle1.keyReleased(e);
            // paddle2.keyReleased(e);
        }
    }
}
