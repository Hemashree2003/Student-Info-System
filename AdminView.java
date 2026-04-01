
import javax.swing.*;

public class AdminView {
    public static void show(Admin admin) {
        // ... UI Setup is unchanged ...
        JFrame frame = new JFrame("Admin: " + admin.username);
        frame.setSize(500, 600);
        frame.setLayout(null);
        frame.setLocationRelativeTo(null);
        String[] labels = {"Student ID (e.g., S001):", "Full Name:", "DOB (YYYY-MM-DD):", "Password:", "Address:", "Contact:", "Email:", "Parent's Name:", "Parent's Contact:"};
        JTextField[] fields = new JTextField[labels.length];
        int y_pos = 30;
        for (int i = 0; i < labels.length; i++) {
            JLabel label = new JLabel(labels[i]);
            label.setBounds(30, y_pos, 150, 30);
            frame.add(label);
            fields[i] = new JTextField();
            fields[i].setBounds(200, y_pos, 250, 30);
            frame.add(fields[i]);
            y_pos += 40;
        }
        JButton createBtn = new JButton("Create Student");
        createBtn.setBounds(200, y_pos + 20, 150, 40);
        frame.add(createBtn);

        createBtn.addActionListener(_ -> {
            // ... Validation is unchanged ...
            String id = fields[0].getText();
            String name = fields[1].getText();
            String dob = fields[2].getText();
            String password = fields[3].getText();
            if (id.isEmpty() || name.isEmpty() || dob.isEmpty() || password.isEmpty() || !dob.matches("\\d{4}-\\d{2}-\\d{2}")) {
                JOptionPane.showMessageDialog(frame, "ID, Name, Password, and a valid DOB (YYYY-MM-DD) are required.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            String year = dob.substring(0, 4);
            String username = name.replaceAll("\\s+", "").toLowerCase() + year;

            Student newStudent = new Student(id, username, password, name, dob,
                fields[4].getText(), fields[5].getText(), fields[6].getText(), fields[7].getText(), fields[8].getText());
            
            // Call the DatabaseManager to add the student
            if (DatabaseManager.addStudent(newStudent)) {
                JOptionPane.showMessageDialog(frame, "Student '" + name + "' created successfully!\nUsername: " + username);
                for (JTextField field : fields) {
                    field.setText("");
                }
            } else {
                JOptionPane.showMessageDialog(frame, "Failed to create student. The ID or Username may already exist.", "Database Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        frame.setVisible(true);
    }
}