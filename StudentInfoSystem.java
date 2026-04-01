import javax.swing.*;

public class StudentInfoSystem {

    public static void main(String[] args) {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            System.err.println("FATAL ERROR: SQLite JDBC driver not found.");
            System.err.println("Please ensure the sqlite-jdbc-....jar file is in the same folder and you are using the correct -cp command.");
            JOptionPane.showMessageDialog(null, "Database driver not found! Check the console for details.", "Driver Error", JOptionPane.ERROR_MESSAGE);
            System.exit(1); 
        }

        DatabaseManager.setupDatabase();
        showMainMenu();
    }
    
    static void showMainMenu() {
        JFrame frame = new JFrame("Student Info System - Main Menu");
        frame.setSize(450, 400);
        frame.setLayout(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        JButton adminBtn = new JButton("Admin Login");
        adminBtn.setBounds(100, 50, 250, 50);
        adminBtn.addActionListener(_ -> showLogin("Admin"));
        JButton studentBtn = new JButton("Student Login");
        studentBtn.setBounds(100, 130, 250, 50);
        studentBtn.addActionListener(_ -> showLogin("Student"));
        JButton teacherBtn = new JButton("Teacher Login");
        teacherBtn.setBounds(100, 210, 250, 50);
        teacherBtn.addActionListener(_ -> showLogin("Teacher"));
        frame.add(adminBtn);
        frame.add(studentBtn);
        frame.add(teacherBtn);
        frame.setVisible(true);
    }

    static void showLogin(String userType) {
        JFrame loginFrame = new JFrame(userType + " Login");
        loginFrame.setSize(400, 250);
        loginFrame.setLayout(null);
        loginFrame.setLocationRelativeTo(null);
        JLabel userLabel = new JLabel("Username:");
        userLabel.setBounds(50, 40, 80, 25);
        loginFrame.add(userLabel);
        JTextField userText = new JTextField(20);
        userText.setBounds(150, 40, 165, 25);
        loginFrame.add(userText);
        JLabel passLabel = new JLabel("Password:");
        passLabel.setBounds(50, 80, 80, 25);
        loginFrame.add(passLabel);
        JPasswordField passText = new JPasswordField(20);
        passText.setBounds(150, 80, 165, 25);
        loginFrame.add(passText);
        JButton loginButton = new JButton("Login");
        loginButton.setBounds(150, 130, 80, 25);
        loginFrame.add(loginButton);
        
        loginButton.addActionListener(_ -> {
            String username = userText.getText();
            String password = new String(passText.getPassword());

            if (DatabaseManager.validateLogin(userType, username, password)) {
                loginFrame.dispose();
                switch (userType) {
                    case "Admin":
                        AdminDashboard.show(new Admin(username, ""));
                        break;
                    case "Student":
                        // This now shows the dashboard with two buttons again
                        StudentDashboard.show(username);
                        break;
                    case "Teacher":
                        TeacherView.show(new Teacher("Teacher", username, ""));
                        break;
                }
            } else {
                JOptionPane.showMessageDialog(loginFrame, "Invalid username or password.");
            }
        });
        loginFrame.setVisible(true);
    }
}