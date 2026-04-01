import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class AttendanceView {

    public static void show(Student student) {
        JFrame frame = new JFrame("Attendance for " + student.name);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(500, 400);
        frame.setLocationRelativeTo(null);

        String[] columnNames = {"Subject", "Attendance Percentage"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        JTable table = new JTable(model);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.setRowHeight(25);

        if (student.attendance.isEmpty()) {
            model.addRow(new Object[]{"No attendance entered yet.", ""});
        } else {
            for (String subject : student.attendance.keySet()) {
                int percentage = student.attendance.get(subject);
                model.addRow(new Object[]{subject, percentage + "%"});
            }
        }
        
        frame.add(new JScrollPane(table));
        frame.setVisible(true);
    }
}