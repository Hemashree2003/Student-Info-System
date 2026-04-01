// Correct code for StudentListView.java
import java.awt.*;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class StudentListView {
    public static void show() {
        JFrame frame = new JFrame("List of All Students");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(800, 500);
        frame.setLocationRelativeTo(null);
        String[] columnNames = {"Student ID", "Name", "Username", "Date of Birth", "Contact"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        JTable table = new JTable(model);
        table.setFillsViewportHeight(true);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.setRowHeight(25);

        List<Student> students = DatabaseManager.getAllStudents();

        for (Student student : students) {
            Object[] row = {
                student.studentId, student.name, student.username,
                student.dob, student.contact
            };
            model.addRow(row);
        }
        
        frame.add(new JScrollPane(table));
        frame.setVisible(true);
    }
}