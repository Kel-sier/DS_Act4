import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;
import java.util.ArrayList;

public class PassesManagementSystemGUI extends JFrame {
    private PassesManagementSystem system;
    private JTextField customerNameField;
    private JTextArea reportArea;
    private JPanel vipListPanel;
    private JPanel generalListPanel;

    // Simplified color scheme
    private static final Color CREAM_COLOR = new Color(255, 248, 231);
    private static final Color LIGHT_BLUE = new Color(235, 245, 251);
    private static final Color BUTTON_COLOR = new Color(70, 130, 180); // Steel blue for better contrast
    private static final Color TEXT_COLOR = Color.WHITE;
    private static final Color BORDER_COLOR = new Color(25, 25, 112); // Dark blue

    public PassesManagementSystemGUI() {
        system = new PassesManagementSystem();
        setupUI();
    }

    private void setupUI() {
        setTitle("Passes Management System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 700);
        setLocationRelativeTo(null);
        getContentPane().setBackground(CREAM_COLOR);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        mainPanel.setBackground(CREAM_COLOR);

        // Add components to main panel
        mainPanel.add(createInputPanel(), BorderLayout.NORTH);

        // Create split pane with lists and report
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        splitPane.setResizeWeight(0.5);
        splitPane.setBackground(CREAM_COLOR);
        splitPane.setBorder(null);

        // Top section with lists and action buttons
        JPanel topSection = new JPanel(new BorderLayout());
        topSection.setBackground(CREAM_COLOR);
        topSection.add(createListsPanel(), BorderLayout.CENTER);
        topSection.add(createActionPanel(), BorderLayout.EAST);

        // Create report area
        reportArea = new JTextArea();
        reportArea.setEditable(false);
        reportArea.setFont(new Font("Monospaced", Font.PLAIN, 16));
        reportArea.setBackground(LIGHT_BLUE);
        reportArea.setForeground(BORDER_COLOR);

        JScrollPane reportScrollPane = new JScrollPane(reportArea);
        reportScrollPane.setBorder(createTitledBorder("Service Report"));
        reportScrollPane.setPreferredSize(new Dimension(800, 250));
        reportScrollPane.getViewport().setBackground(LIGHT_BLUE);

        // Add components to split pane
        splitPane.setTopComponent(topSection);
        splitPane.setBottomComponent(reportScrollPane);
        mainPanel.add(splitPane, BorderLayout.CENTER);

        add(mainPanel);
        updateCustomerLists();

        SwingUtilities.invokeLater(() -> splitPane.setDividerLocation(0.6));
    }

    private JPanel createInputPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.setBorder(createTitledBorder("Add Customer"));
        panel.setBackground(CREAM_COLOR);

        JLabel nameLabel = new JLabel("Customer Name:");
        nameLabel.setForeground(BORDER_COLOR);
        nameLabel.setFont(new Font("Arial", Font.BOLD, 12));

        customerNameField = new JTextField(15);
        customerNameField.setBackground(LIGHT_BLUE);
        customerNameField.setForeground(BORDER_COLOR);
        customerNameField.setBorder(BorderFactory.createLineBorder(BORDER_COLOR));

        JButton addGeneralButton = createStyledButton("Add General Customer");
        addGeneralButton.addActionListener(e -> addGeneralCustomer());

        JButton addVipButton = createStyledButton("Add VIP Customer");
        addVipButton.addActionListener(e -> addVipCustomer());

        panel.add(nameLabel);
        panel.add(customerNameField);
        panel.add(addGeneralButton);
        panel.add(addVipButton);

        return panel;
    }

    private JPanel createListsPanel() {
        JPanel panel = new JPanel(new GridLayout(1, 2, 10, 0));
        panel.setBackground(CREAM_COLOR);

        // VIP customers panel
        vipListPanel = createCustomerListPanel();
        JScrollPane vipScrollPane = new JScrollPane(vipListPanel);
        vipScrollPane.getViewport().setBackground(LIGHT_BLUE);
        vipScrollPane.setBorder(BorderFactory.createEmptyBorder());

        JPanel vipPanel = new JPanel(new BorderLayout());
        vipPanel.setBorder(createTitledBorder("VIP Customers"));
        vipPanel.setBackground(CREAM_COLOR);
        vipPanel.add(vipScrollPane, BorderLayout.CENTER);

        // General customers panel
        generalListPanel = createCustomerListPanel();
        JScrollPane generalScrollPane = new JScrollPane(generalListPanel);
        generalScrollPane.getViewport().setBackground(LIGHT_BLUE);
        generalScrollPane.setBorder(BorderFactory.createEmptyBorder());

        JPanel generalPanel = new JPanel(new BorderLayout());
        generalPanel.setBorder(createTitledBorder("General Customers"));
        generalPanel.setBackground(CREAM_COLOR);
        generalPanel.add(generalScrollPane, BorderLayout.CENTER);

        panel.add(vipPanel);
        panel.add(generalPanel);

        return panel;
    }

    private JPanel createCustomerListPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(LIGHT_BLUE);
        return panel;
    }

    private JPanel createActionPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(createTitledBorder("Actions"));
        panel.setBackground(CREAM_COLOR);

        JButton serveVipButton = createStyledButton("Serve VIP Customer");
        serveVipButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        serveVipButton.setMaximumSize(new Dimension(150, 30));
        serveVipButton.addActionListener(e -> serveVipCustomer());

        JButton serveGeneralButton = createStyledButton("Serve General Customer");
        serveGeneralButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        serveGeneralButton.setMaximumSize(new Dimension(150, 30));
        serveGeneralButton.addActionListener(e -> serveGeneralCustomer());

        JButton displayReportButton = createStyledButton("Display Report");
        displayReportButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        displayReportButton.setMaximumSize(new Dimension(150, 30));
        displayReportButton.addActionListener(e -> displayReport());

        panel.add(Box.createVerticalGlue());
        panel.add(serveVipButton);
        panel.add(Box.createVerticalStrut(10));
        panel.add(serveGeneralButton);
        panel.add(Box.createVerticalStrut(10));
        panel.add(displayReportButton);
        panel.add(Box.createVerticalGlue());

        return panel;
    }

    private TitledBorder createTitledBorder(String title) {
        return BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(BORDER_COLOR, 2), title,
                TitledBorder.LEFT, TitledBorder.TOP,
                new Font("Arial", Font.BOLD, 14), BORDER_COLOR);
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setBackground(BUTTON_COLOR);
        button.setForeground(Color.BLACK); // White text for better contrast
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        button.setFont(new Font("Arial", Font.BOLD, 12));

        // Add hover effect
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(BORDER_COLOR);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(BUTTON_COLOR);
            }
        });

        return button;
    }

    /**
     * Validates the customer name to ensure it doesn't contain numbers
     * @param name The customer name to validate
     * @return true if the name is valid (contains no numbers), false otherwise
     */
    private boolean isValidName(String name) {
        if (name.isEmpty()) {
            return false;
        }

        // Check if the name contains any digits
        for (char c : name.toCharArray()) {
            if (Character.isDigit(c)) {
                return false;
            }
        }

        return true;
    }

    private void addGeneralCustomer() {
        String name = customerNameField.getText().trim();

        try {
            // Validate the customer name
            if (!isValidName(name)) {
                if (name.isEmpty()) {
                    throw new IllegalArgumentException("Please enter a customer name");
                } else {
                    throw new IllegalArgumentException("Customer name cannot contain numbers");
                }
            }

            system.addGeneralCustomer(name);
            customerNameField.setText("");
            updateCustomerLists();
            appendToReport("Added general customer: " + name);

        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Input Error", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void addVipCustomer() {
        String name = customerNameField.getText().trim();

        try {
            // Validate the customer name
            if (!isValidName(name)) {
                if (name.isEmpty()) {
                    throw new IllegalArgumentException("Please enter a customer name");
                } else {
                    throw new IllegalArgumentException("Customer name cannot contain numbers");
                }
            }

            system.addVipCustomer(name);
            customerNameField.setText("");
            updateCustomerLists();
            appendToReport("Added VIP customer: " + name);

        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Input Error", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void serveVipCustomer() {
        if (!system.vipCustomer.isEmpty()) {
            String customer = system.vipCustomer.pop();
            system.totalPrivateServed++;
            updateCustomerLists();
            appendToReport("Serving VIP customer: " + customer);
        } else {
            appendToReport("No VIP customers to serve.");
        }
    }

    private void serveGeneralCustomer() {
        if (!system.generalCustomer.isEmpty()) {
            String customer = system.generalCustomer.poll();
            system.totalGeneralServed++;
            updateCustomerLists();
            appendToReport("Serving general customer: " + customer);
        } else {
            appendToReport("No general customers to serve.");
        }
    }

    private void displayReport() {
        StringBuilder report = new StringBuilder();
        report.append("\n=== Customer Service Report ===\n");
        report.append("Total VIP customers served: ").append(system.totalPrivateServed).append("\n");
        report.append("Total general customers served: ").append(system.totalGeneralServed).append("\n");
        report.append("Remaining VIP customers: ").append(system.vipCustomer.size()).append("\n");
        report.append("Remaining general customers: ").append(system.generalCustomer.size()).append("\n");

        report.append("\nRemaining VIP customers:\n");
        if (!system.vipCustomer.isEmpty()) {
            Stack<String> tempStack = new Stack<>();
            tempStack.addAll(system.vipCustomer);

            while (!tempStack.isEmpty()) {
                report.append("- ").append(tempStack.pop()).append("\n");
            }
        } else {
            report.append("None\n");
        }

        report.append("\nRemaining general customers:\n");
        if (!system.generalCustomer.isEmpty()) {
            for (String customer : system.generalCustomer) {
                report.append("- ").append(customer).append("\n");
            }
        } else {
            report.append("None\n");
        }

        reportArea.setText(report.toString());
    }

    private void updateCustomerLists() {
        // Update VIP list
        vipListPanel.removeAll();
        if (!system.vipCustomer.isEmpty()) {
            ArrayList<String> vipList = new ArrayList<>(system.vipCustomer);
            for (int i = vipList.size() - 1; i >= 0; i--) {
                JLabel customerLabel = new JLabel(vipList.get(i));
                customerLabel.setForeground(BORDER_COLOR);
                customerLabel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
                customerLabel.setFont(new Font("Arial", Font.BOLD, 12));
                vipListPanel.add(customerLabel);
            }
        }

        // Update General list
        generalListPanel.removeAll();
        for (String customer : system.generalCustomer) {
            JLabel customerLabel = new JLabel(customer);
            customerLabel.setForeground(BORDER_COLOR);
            customerLabel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
            customerLabel.setFont(new Font("Arial", Font.BOLD, 12));
            generalListPanel.add(customerLabel);
        }

        // Refresh UI
        vipListPanel.revalidate();
        vipListPanel.repaint();
        generalListPanel.revalidate();
        generalListPanel.repaint();
    }

    private void appendToReport(String text) {
        reportArea.append(text + "\n");
        // Scroll to the bottom
        reportArea.setCaretPosition(reportArea.getDocument().getLength());
    }

    // Simple internal data management class
    public class PassesManagementSystem {
        public Queue<String> generalCustomer;
        public Stack<String> vipCustomer;
        public int totalGeneralServed;
        public int totalPrivateServed;

        public PassesManagementSystem() {
            generalCustomer = new LinkedList<>();
            vipCustomer = new Stack<>();
            totalGeneralServed = 0;
            totalPrivateServed = 0;
        }

        public void addGeneralCustomer(String name) {
            generalCustomer.add(name);
        }

        public void addVipCustomer(String name) {
            vipCustomer.push(name);
        }
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            UIManager.put("OptionPane.background", new Color(255, 248, 231));
            UIManager.put("Panel.background", new Color(255, 248, 231));
            UIManager.put("OptionPane.messageForeground", new Color(25, 25, 112));
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> {
            new PassesManagementSystemGUI().setVisible(true);
        });
    }
}