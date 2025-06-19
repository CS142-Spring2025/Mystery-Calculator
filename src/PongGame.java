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
    boolean running = false;
    boolean ballHitPlayer = false;
    boolean ballHitAI = false;
    boolean leftPressed = false;
    boolean rightPressed = false;
    JFrame parentFrame;
    Runnable onGameOver;

    JLabel scoreLabel;
    JSlider difficultySlider;
    JPanel controlPanel;

    public PongGame(JFrame parentFrame, Runnable onGameOver) {
        this.parentFrame = parentFrame;
        this.onGameOver = onGameOver;
        setBackground(Color.BLACK);
        setFocusable(true);
        addKeyListener(this);

        timer = new Timer(5, this);
    }

    public JPanel buildContainer() {
        JPanel container = new JPanel(new BorderLayout());
        controlPanel = new JPanel();
        controlPanel.setBackground(new Color(20, 20, 20));
        controlPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 5));

        JButton startButton = createStyledButton("Start", new Color(255, 149, 0), Color.WHITE);
        JButton pauseButton = createStyledButton("Pause", new Color(60, 60, 60), Color.WHITE);
        JButton resumeButton = createStyledButton("Resume", new Color(60, 60, 60), Color.WHITE);

        startButton.addActionListener(e -> {
            running = true;
            score = 0;
            scoreLabel.setText("Score: 0");
            timer.start();
            requestFocusInWindow();
        });

        pauseButton.addActionListener(e -> running = false);
        resumeButton.addActionListener(e -> {
            running = true;
            timer.start();
            requestFocusInWindow();
        });

        controlPanel.add(startButton);
        controlPanel.add(pauseButton);
        controlPanel.add(resumeButton);

        scoreLabel = new JLabel("Score: 0");
        scoreLabel.setForeground(Color.WHITE);
        controlPanel.add(scoreLabel);

        JLabel difficultyLabel = new JLabel("Difficulty:");
        difficultyLabel.setForeground(Color.WHITE);
        controlPanel.add(difficultyLabel);

        difficultySlider = new JSlider(1, 3, 2);
        difficultySlider.setMajorTickSpacing(1);
        difficultySlider.setPaintTicks(true);
        difficultySlider.setPaintLabels(true);
        difficultySlider.setForeground(Color.WHITE);
        difficultySlider.setBackground(new Color(20, 20, 20));
        difficultySlider.setLabelTable(difficultySlider.createStandardLabels(1));
        controlPanel.add(difficultySlider);

        container.add(controlPanel, BorderLayout.NORTH);
        container.add(this, BorderLayout.CENTER);
        return container;
    }

    private JButton createStyledButton(String text, Color bg, Color fg) {
        JButton button = new JButton(text);
        button.setBackground(bg);
        button.setForeground(fg);
        button.setFocusPainted(false);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        return button;
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.WHITE);
        g.fillRect(playerX, getHeight() - 20, paddleWidth, paddleHeight);
        g.fillRect(aiX, 10, paddleWidth, paddleHeight);
        g.fillOval((int) ballX, (int) ballY, 15, 15);
    }

    public void actionPerformed(ActionEvent e) {
        if (!running) return;

        double difficultyMultiplier = switch (difficultySlider.getValue()) {
            case 1 -> 0.5;
            case 3 -> 1.5;
            default -> 1.0;
        };

        if (leftPressed && playerX > 0) playerX -= 5;
        if (rightPressed && playerX < getWidth() - paddleWidth) playerX += 5;

        ballX += ballDX * difficultyMultiplier;
        ballY += ballDY * difficultyMultiplier;

        if (ballX <= 0 || ballX >= getWidth() - 15) ballDX *= -1;

        if (!ballHitPlayer && ballY + 15 >= getHeight() - 20 &&
                ballX + 15 >= playerX && ballX <= playerX + paddleWidth) {
            ballDY *= -1;
            score++;
            scoreLabel.setText("Score: " + score);
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
        JPanel container = pong.buildContainer();

        gameFrame.setSize(width, height);
        gameFrame.setLocation(calculatorFrame.getX(), calculatorFrame.getY());
        gameFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        gameFrame.setContentPane(container);
        gameFrame.setVisible(true);
        pong.requestFocusInWindow();
    }
}
