package yuparking.gui.Manager;

import yuparking.models.User;
import yuparking.services.ManagementService;

import javax.swing.*;
import java.awt.*;

public class AutoAccountGUI {
    private JFrame frame;
    private User user;
    private ManagementService managementService;

    public AutoAccountGUI(User user) {
        // Check if user is super_manager
        if (!user.getUserType().equals("super_manager")) {
            JOptionPane.showMessageDialog(null,
                    "Access Denied: Only Super Managers can generate new manager accounts.",
                    "Access Denied",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        this.user = user;
        this.managementService = new ManagementService();
        initializeUI();
    }

    private void initializeUI() {
        frame = new JFrame("Auto Generate Manager Account");
        frame.setSize(800, 800);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Title Panel
        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        JLabel titleLabel = new JLabel("Generate New Manager Account");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        titlePanel.add(titleLabel);
        mainPanel.add(titlePanel, BorderLayout.NORTH);

        // Content Panel
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        // Generate Button
        JButton generateButton = new JButton("Generate New Manager Account");
        generateButton.setBackground(new Color(0, 120, 215));
        generateButton.setForeground(Color.WHITE);
        generateButton.setFocusPainted(false);
        generateButton.setFont(new Font("Arial", Font.BOLD, 12));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(0, 0, 20, 0);
        contentPanel.add(generateButton, gbc);

        // Warning Label
        JLabel warningLabel = new JLabel("<html><center>Warning: This will generate a new manager account<br>with auto-generated credentials.<br>Please save the credentials securely.</center></html>");
        warningLabel.setForeground(Color.RED);
        warningLabel.setFont(new Font("Arial", Font.BOLD, 14));
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(0, 0, 20, 0);
        contentPanel.add(warningLabel, gbc);

        // Result Text Area
        JTextArea resultArea = new JTextArea(20, 40);
        resultArea.setEditable(false);
        resultArea.setLineWrap(true);
        resultArea.setWrapStyleWord(true);
        resultArea.setFont(new Font("Monospaced", Font.PLAIN, 18));
        resultArea.setMargin(new Insets(20, 20, 20, 20));
        JScrollPane scrollPane = new JScrollPane(resultArea);
        scrollPane.setPreferredSize(new Dimension(700, 500));
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(0, 0, 0, 0);
        contentPanel.add(scrollPane, gbc);

        mainPanel.add(contentPanel, BorderLayout.CENTER);

        // Add action listener to generate button
        generateButton.addActionListener(e -> {
            managementService.generateManagerAccount();
            resultArea.setText("New manager account generated successfully!\n\n" +
                    "Please save these credentials securely:\n\n" +
                    "Username: manager_auto_" + (managementService.getNextUserId() - 1) + "@yorku.ca\n\n" +
                    "Password: " + managementService.getLastGeneratedPassword());
        });

        frame.add(mainPanel);
        frame.setVisible(true);
    }
} 