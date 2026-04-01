
import javax.swing.*;

public class AdminDashboard {

    public static void show(Admin admin) {
        JFrame frame = new JFrame("Admin Dashboard - " + admin.username);
        frame.setSize(500, 300);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Close only this window
        frame.setLayout(null);
        frame.setLocationRelativeTo(null);

        JButton addStudentBtn = new JButton("Add New Student");
        addStudentBtn.setBounds(100, 60, 300, 50);
        addStudentBtn.addActionListener(_ -> AdminView.show(admin));

        JButton viewStudentsBtn = new JButton("View Student List");
        viewStudentsBtn.setBounds(100, 140, 300, 50);
        viewStudentsBtn.addActionListener(_ -> StudentListView.show());

        frame.add(addStudentBtn);
        frame.add(viewStudentsBtn);
        frame.setVisible(true);
    }
}