
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.Timer;

public class MysteryCalculator {
    public int boardWidth = 600;
    public int boardHeight = 700;

    public Color customLightGray = new Color(230, 230, 230);
    public Color customDarkGray = new Color(60, 60, 60);
    public Color customBlack = new Color(20, 20, 20);
    public Color customOrange = new Color(255, 149, 0);

    public String[] buttonValues = { "AC", "+/-", "%", "÷", "7", "8", "9", "×", "4", "5", "6", "-", "1", "2", "3", "+", "0", ".", "←", "=" };
    public Set<String> validKeys = new HashSet<>(Arrays.asList("0","1","2","3","4","5","6","7","8","9","+","-","*","/","×","÷",".","=","←","AC"));
    public String[] rightSymbols = {"÷", "×", "-", "+", "="};
    public String[] topSymbols = {"AC", "+/-", "%"};

    public JFrame frame = new JFrame("Calculator With Pong");
    public JLabel displayLabel = new JLabel();
    public JPanel displayPanel = new JPanel();
    public JPanel buttonsPanel = new JPanel();
    public Map<String, JButton> buttonMap = new HashMap<>();
    public boolean justShowedScore = false;

    public String A = "0";
    public String operator = null;
    public String B = null;
    public boolean enteringSecondNumber = false;

    public MysteryCalculator() {
        frame.setSize(boardWidth, boardHeight);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        displayLabel.setBackground(customBlack);
        displayLabel.setForeground(Color.white);
        displayLabel.setFont(new Font("Segoe UI", Font.BOLD, 50));
        displayLabel.setHorizontalAlignment(JLabel.RIGHT);
        displayLabel.setText("0");
        displayLabel.setOpaque(true);
        displayLabel.setBorder(new EmptyBorder(20, 20, 20, 20));

        displayPanel.setLayout(new BorderLayout());
        displayPanel.add(displayLabel);
        frame.add(displayPanel, BorderLayout.NORTH);

        buttonsPanel.setLayout(new GridLayout(5, 4, 8, 8));
        buttonsPanel.setBackground(customBlack);
        buttonsPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        frame.add(buttonsPanel);

        for (String value : buttonValues) {
            JButton button = new RoundedButton(value);
            button.setFont(new Font("Segoe UI", Font.BOLD, 24));
            button.setFocusable(false);

            if (Arrays.asList(topSymbols).contains(value)) {
                button.setBackground(customLightGray);
                button.setForeground(customBlack);
            } else if (Arrays.asList(rightSymbols).contains(value)) {
                button.setBackground(customOrange);
                button.setForeground(Color.white);
            } else {
                button.setBackground(customDarkGray);
                button.setForeground(Color.white);
            }

            buttonMap.put(value, button);
            buttonsPanel.add(button);
            button.addActionListener(e -> {
                if (justShowedScore) {
                    displayLabel.setText("0");
                    justShowedScore = false;
                }
                handleButtonPress(button.getText());
            });
        }

        frame.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                String key = KeyEvent.getKeyText(e.getKeyCode());
                char c = e.getKeyChar();
                String label = null;

                if (Character.isDigit(c)) label = String.valueOf(c);
                else if (c == '+') label = "+";
                else if (c == '-') label = "-";
                else if (c == '*') label = "×";
                else if (c == '/') label = "÷";
                else if (c == '.') label = ".";
                else if (e.getKeyCode() == KeyEvent.VK_ENTER) label = "=";
                else if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE) label = "←";
                else if (key.equalsIgnoreCase("C")) label = "AC";

                if (label != null && validKeys.contains(label)) {
                    if (justShowedScore) {
                        displayLabel.setText("0");
                        justShowedScore = false;
                    }
                    pressFromKey(label);
                }
            }
        });

        frame.setFocusable(true);
        frame.requestFocusInWindow();
        frame.setVisible(true);
    }

    public void pressFromKey(String label) {
        if (buttonMap.containsKey(label)) {
            buttonMap.get(label).doClick();
        }
    }

    public void handleButtonPress(String buttonValue) {
        if (Arrays.asList(rightSymbols).contains(buttonValue)) {
            if (buttonValue.equals("=")) {
                if (A != null && operator != null) {
                    B = displayLabel.getText();
                    double numA = Double.parseDouble(A);
                    double numB = Double.parseDouble(B);

                    switch (operator) {
                        case "+": displayLabel.setText(removeZeroDecimal(numA + numB)); break;
                        case "-": displayLabel.setText(removeZeroDecimal(numA - numB)); break;
                        case "×": displayLabel.setText(removeZeroDecimal(numA * numB)); break;
                        case "÷":
                            if (numB == 0) displayLabel.setText("Error");
                            else displayLabel.setText(removeZeroDecimal(numA / numB));
                            break;
                    }
                    clearAll();
                }
            } else {
                if (operator == null) A = displayLabel.getText();
                operator = buttonValue;
                displayLabel.setText(operator);
                enteringSecondNumber = true;
            }
        } else if (Arrays.asList(topSymbols).contains(buttonValue)) {
            switch (buttonValue) {
                case "AC": clearAll(); displayLabel.setText("0"); break;
                case "+/-":
                    double neg = Double.parseDouble(displayLabel.getText()) * -1;
                    displayLabel.setText(removeZeroDecimal(neg)); break;
                case "%":
                    double percent = Double.parseDouble(displayLabel.getText()) / 100;
                    displayLabel.setText(removeZeroDecimal(percent)); break;
            }
        } else if (buttonValue.equals(".")) {
            String current = displayLabel.getText();
            if (current.equals("123456789")) {
                displayLabel.setFont(new Font("Segoe UI", Font.BOLD, 30));
                displayLabel.setText("Launching PONG...");
                PongGame.launch(frame, () -> {
                    displayLabel.setFont(new Font("Segoe UI", Font.BOLD, 50));
                    displayLabel.setText("SCORE : " + PongGame.lastScore);
                    justShowedScore = true;
                }, boardWidth, boardHeight);
            } else if (current.equals("123789456")) {
                displayLabel.setFont(new Font("Segoe UI", Font.PLAIN, 18));
                displayLabel.setText(FortuneEngine.getAccessMessage());
                justShowedScore = true;

                Timer t1 = new Timer(1000, e -> displayLabel.setText(FortuneEngine.getWelcomeMessage()));
                t1.setRepeats(false); t1.start();

                Timer t2 = new Timer(3000, e -> displayLabel.setText("> " + FortuneEngine.getMission()));
                t2.setRepeats(false); t2.start();

                Timer t3 = new Timer(6000, e -> displayLabel.setText(FortuneEngine.getGoodbyeMessage()));
                t3.setRepeats(false); t3.start();
            } else if (!current.contains(".")) {
                displayLabel.setText(current + ".");
            }
        } else if (buttonValue.equals("←")) {
            String current = displayLabel.getText();
            if (current.length() > 1) displayLabel.setText(current.substring(0, current.length() - 1));
            else displayLabel.setText("0");
        } else {
            if (displayLabel.getText().equals("0") || displayLabel.getText().equals(operator)) {
                displayLabel.setText(buttonValue);
                enteringSecondNumber = false;
            } else {
                displayLabel.setText(displayLabel.getText() + buttonValue);
            }
        }
    }

    public void clearAll() {
        A = "0";
        operator = null;
        B = null;
        enteringSecondNumber = false;
        displayLabel.setFont(new Font("Segoe UI", Font.BOLD, 50));
    }

    public String removeZeroDecimal(double number) {
        if (number % 1 == 0) return String.valueOf((int) number);
        return String.valueOf(number);
    }

    public static void main(String[] args) {
        new MysteryCalculator();
    }
}

class RoundedButton extends JButton {
    public RoundedButton(String text) {
        super(text);
        setFocusPainted(false);
        setBorderPainted(false);
        setContentAreaFilled(false);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        Color base = getBackground();
        if (getModel().isPressed()) g2.setColor(base.darker());
        else {
            GradientPaint gradient = new GradientPaint(0, 0, base.brighter(), 0, getHeight(), base.darker());
            g2.setPaint(gradient);
        }

        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
        g2.dispose();
        super.paintComponent(g);
    }

    @Override protected void paintBorder(Graphics g) {}
    @Override public void setContentAreaFilled(boolean b) {}
}
