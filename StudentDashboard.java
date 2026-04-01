import javax.swing.*;

public class StudentDashboard {
    public static void show(String studentUsername) {
        JFrame frame = new JFrame("Student Dashboard");
        frame.setSize(500, 300);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLayout(null);
        frame.setLocationRelativeTo(null);

        JButton viewMarksBtn = new JButton("View Marks & Grades");
        viewMarksBtn.setBounds(100, 60, 300, 50);
        viewMarksBtn.addActionListener(_ -> {
            Student student = DatabaseManager.getStudent(studentUsername);
            if (student != null) {
                MarksView.show(student);
            }
        });

        JButton viewAttendanceBtn = new JButton("View Attendance");
        viewAttendanceBtn.setBounds(100, 140, 300, 50);
        viewAttendanceBtn.addActionListener(_ -> {
            Student student = DatabaseManager.getStudent(studentUsername);
            if (student != null) {
                AttendanceView.show(student);
            }
        });

        frame.add(viewMarksBtn);
        frame.add(viewAttendanceBtn);
        frame.setVisible(true);
    }
}