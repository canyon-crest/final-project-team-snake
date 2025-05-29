import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 * HowToPlayScreen displays user instructions for Hills of Fire,
 * with a professional dark blue hills themed background and modern layout.
 */
public class HowToPlayScreen extends JPanel {
    private JButton backButton;
    private JTextArea instructionsArea;

    /**
     * Constructs the HowToPlayScreen with themed background, instructions, and a back button.
     * @param backAction the ActionListener for the back button
     */
    public HowToPlayScreen(ActionListener backAction) {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(18, 0, 10, 0);
        gbc.gridx = 0;

        // Title label
        JLabel titleLabel = new JLabel("How to Play");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 36));
        titleLabel.setForeground(new Color(210, 225, 255)); // Lighter blue for contrast
        gbc.gridy = 0;
        add(titleLabel, gbc);

        // Instructions area inside a card-like panel
        JPanel instructionCard = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                // Drop shadow
                Graphics2D g2 = (Graphics2D) g;
                g2.setColor(new Color(15, 30, 80, 70));
                g2.fillRoundRect(5, 5, getWidth()-10, getHeight()-10, 26, 26);
                // Card background
                g2.setColor(new Color(24, 39, 77, 220));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 26, 26);
                super.paintComponent(g);
            }
        };
        instructionCard.setOpaque(false);
        instructionCard.setLayout(new BorderLayout());
        instructionCard.setPreferredSize(new Dimension(480, 180));

        instructionsArea = new JTextArea(
            "• Move your tank using the arrow keys (or A/D).\n\n" +
            "• Adjust your shot angle and power with the sliders below the game.\n\n" +
            "• Click 'Fire!' to shoot. The wind changes each turn and affects your shot curve.\n\n" +
            "• Each tank has 3 lives. The last tank remaining wins!"
        );
        instructionsArea.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        instructionsArea.setForeground(new Color(210, 225, 255));
        instructionsArea.setOpaque(false);
        instructionsArea.setEditable(false);
        instructionsArea.setLineWrap(true);
        instructionsArea.setWrapStyleWord(true);
        instructionsArea.setBorder(BorderFactory.createEmptyBorder(14, 20, 14, 18));

        instructionCard.add(instructionsArea, BorderLayout.CENTER);

        gbc.gridy = 1;
        add(instructionCard, gbc);

        // Back button, styled
        backButton = new JButton("Back to Main Menu");
        backButton.setFont(new Font("Segoe UI", Font.BOLD, 20));
        backButton.setForeground(new Color(220, 233, 255));
        backButton.setBackground(new Color(25, 56, 112));
        backButton.setFocusPainted(false);
        backButton.setBorder(BorderFactory.createEmptyBorder(8, 30, 8, 30));
        backButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        backButton.addActionListener(backAction);
        gbc.gridy = 2;
        gbc.insets = new Insets(20, 0, 0, 0);
        add(backButton, gbc);
    }

    /**
     * Paints the dark blue hills themed background, with a gradient sky, clouds, and rolling hills.
     * @param g the Graphics object for drawing
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Dark blue gradient sky
        Graphics2D g2 = (Graphics2D) g;
        GradientPaint skyGrad = new GradientPaint(
            0, 0, new Color(20, 37, 70),         // Darkest blue at top
            0, getHeight(), new Color(46, 83, 144) // Deep blue at bottom
        );
        g2.setPaint(skyGrad);
        g2.fillRect(0, 0, getWidth(), getHeight());

        // Soft clouds (subtle, whiter at night)
        g.setColor(new Color(220,230,255,110));
        g.fillOval(80, 38, 110, 34);
        g.fillOval(280, 25, 150, 38);
        g.fillOval(480, 48, 120, 32);

        // Distant hill (deepest blue)
        g.setColor(new Color(22, 44, 97));
        int[] x1 = {0, 140, 270, 440, getWidth()};
        int[] y1 = {getHeight(), 185, 225, 180, getHeight()};
        g.fillPolygon(x1, y1, x1.length);

        // Foreground hill (slightly lighter)
        g.setColor(new Color(52, 104, 176));
        int[] x2 = {0, 120, 260, 420, 620, getWidth()};
        int[] y2 = {getHeight(), 250, 205, 285, 230, getHeight()};
        g.fillPolygon(x2, y2, x2.length);

        // Power-Up Legend (drawn on HowToPlayScreen)
        int legendX = getWidth() - 170, legendY = 45;
        g.setColor(new Color(255,255,255,230));
        g.fillRoundRect(legendX - 9, legendY - 5, 125, 70, 14, 14);
        g.setColor(Color.BLACK);
        g.setFont(new Font("Arial", Font.BOLD, 14));
        g.drawString("Power-Up Legend:", legendX, legendY + 10);

        // Health
        g.setColor(Color.RED);
        g.fillOval(legendX, legendY + 18, 16, 16);
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 13));
        g.drawString("+", legendX + 5, legendY + 30);
        g.setColor(Color.BLACK);
        g.setFont(new Font("Arial", Font.PLAIN, 12));
        g.drawString("Health", legendX + 22, legendY + 30);

        // Fuel
        g.setColor(new Color(40, 180, 60));
        g.fillOval(legendX, legendY + 36, 16, 16);
        g.setColor(Color.BLACK);
        g.setFont(new Font("Arial", Font.BOLD, 12));
        g.drawString("F", legendX + 5, legendY + 48);
        g.setFont(new Font("Arial", Font.PLAIN, 11));
        g.drawString("Fuel", legendX + 22, legendY + 48);

        // Double Damage (if used)
        g.setColor(Color.YELLOW.darker());
        g.fillOval(legendX, legendY + 54, 16, 16);
        g.setColor(Color.BLACK);
        g.setFont(new Font("Arial", Font.BOLD, 12));
        g.drawString("D", legendX + 5, legendY + 66);
        g.setFont(new Font("Arial", Font.PLAIN, 11));
        g.drawString("Double", legendX + 22, legendY + 66);

    }

    /**
     * Returns the back button for navigation.
     * @return the back button JButton
     */
    public JButton getBackButton() {
        return backButton;
    }
}
