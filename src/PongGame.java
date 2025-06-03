import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class PongGame extends JPanel implements ActionListener, KeyListener {
    Timer timer;
    int ballX = 180, ballY = 250;
    double ballDX = 3, ballDY = 3;
    int playerX = 160, aiX = 160;
    int paddleWidth = 80, paddleHeight = 10;
    static int lastScore = 0;
    int score = 0;
    boolean running = true;
    boolean ballHitPlayer = false;
    boolean ballHitAI = false;
    boolean leftPressed = false;
    boolean rightPressed = false;
    JFrame parentFrame;
    Runnable onGameOver;

    public PongGame(JFrame parentFrame, Runnable onGameOver) {
        this.parentFrame = parentFrame;
        this.onGameOver = onGameOver;
        setBackground(Color.BLACK);
        setFocusable(true);
        addKeyListener(this);
        timer = new Timer(5, this);
        timer.start();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.WHITE);
        g.fillRect(playerX, getHeight() - 20, paddleWidth, paddleHeight);
        g.fillRect(aiX, 10, paddleWidth, paddleHeight);
        g.fillOval((int) ballX, (int) ballY, 15, 15);
        g.drawString("Score: " + score, 160, 20);
    }

    public void actionPerformed(ActionEvent e) {
        if (!running) return;
        if (leftPressed && playerX > 0) playerX -= 5;
        if (rightPressed && playerX < getWidth() - paddleWidth) playerX += 5;

        ballX += ballDX;
        ballY += ballDY;

        if (ballX <= 0 || ballX >= getWidth() - 15) ballDX *= -1;

        if (!ballHitPlayer && ballY + 15 >= getHeight() - 20 &&
            ballX + 15 >= playerX && ballX <= playerX + paddleWidth) {
            ballDY *= -1;
            score++;
            ballHitPlayer = true;
            ballHitAI = false;
        }

        aiX = (int) ballX - paddleWidth / 2;
        if (aiX < 0) aiX = 0;
        if (aiX > getWidth() - paddleWidth) aiX = getWidth() - paddleWidth;

        if (!ballHitAI && ballY <= 20 &&
            ballX + 15 >= aiX && ballX <= aiX + paddleWidth) {
            ballDY *= -1;
            ballHitAI = true;
            ballHitPlayer = false;
            ballDX *= 1.05;
            ballDY *= 1.05;
        }

        if (ballY < 0 || ballY > getHeight()) {
            running = false;
            timer.stop();
            lastScore = score;
            parentFrame.dispose();
            onGameOver.run();
        }

        repaint();
    }

    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_LEFT) leftPressed = true;
        if (e.getKeyCode() == KeyEvent.VK_RIGHT) rightPressed = true;
    }

    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_LEFT) leftPressed = false;
        if (e.getKeyCode() == KeyEvent.VK_RIGHT) rightPressed = false;
    }

    public void keyTyped(KeyEvent e) {}

    public static void launch(JFrame calculatorFrame, Runnable onGameOver, int width, int height) {
        JFrame gameFrame = new JFrame("Pong AI Mode (Vertical)");
        PongGame pong = new PongGame(gameFrame, onGameOver);
        gameFrame.setSize(width, height);
        gameFrame.setUndecorated(true);
        gameFrame.setLocation(calculatorFrame.getX(), calculatorFrame.getY());
        gameFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        gameFrame.setContentPane(pong);
        gameFrame.setVisible(true);
        pong.requestFocusInWindow();
    }
}