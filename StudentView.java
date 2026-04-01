// Save this as StudentView.java
import javax.swing.*;
import java.awt.*;

public class StudentView {

    public static void show(Student student) {
        JFrame frame = new JFrame("Student Details: " + student.name);
        JTextArea area = new JTextArea();
        area.setEditable(false);
        area.setFont(new Font("Monospaced", Font.PLAIN, 14));

        StringBuilder details = new StringBuilder();
        details.append(String.format("%-18s: %s\n", "Student ID", student.studentId));
        details.append(String.format("%-18s: %s\n", "Username", student.username));
        details.append(String.format("%-18s: %s\n", "Full Name", student.name));
        details.append(String.format("%-18s: %s\n", "Date of Birth", student.dob));
        details.append(String.format("%-18s: %s\n", "Address", student.address));
        details.append(String.format("%-18s: %s\n", "Contact", student.contact));
        details.append(String.format("%-18s: %s\n", "Email", student.email));
        details.append(String.format("%-18s: %s (%s)\n\n", "Parent", student.parentName, student.parentContact));

        details.append("--- Attendance ---\n");
        if (student.attendance.isEmpty()) {
            details.append("No attendance records found.\n");
        } else {
            student.attendance.forEach((subject, percent) -> 
                details.append(String.format("- %-15s: %d%%\n", subject, percent)));
        }

        details.append("\n--- Marks ---\n");
        if (student.marks.isEmpty()) {
            details.append("No mark records found.\n");
        } else {
            student.marks.forEach((subject, score) -> 
                details.append(String.format("- %-15s: %d\n", subject, score)));
        }
        
        area.setText(details.toString());
        frame.add(new JScrollPane(area));
        frame.setSize(550, 500);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}