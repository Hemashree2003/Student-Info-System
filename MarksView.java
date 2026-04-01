import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class MarksView {

    public static void show(Student student) {
        JFrame frame = new JFrame("Marks & Grades for " + student.name);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(600, 400);
        frame.setLocationRelativeTo(null);

        String[] columnNames = {"Subject", "Score (out of 100)", "Grade"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        JTable table = new JTable(model);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.setRowHeight(25);

        if (student.marks.isEmpty()) {
            model.addRow(new Object[]{"No marks entered yet.", "", ""});
        } else {
            for (String subject : student.marks.keySet()) {
                int score = student.marks.get(subject);
                String grade = calculateGrade(score);
                model.addRow(new Object[]{subject, score, grade});
            }
        }
        
        frame.add(new JScrollPane(table));
        frame.setVisible(true);
    }

    private static String calculateGrade(int score) {
        if (score >= 85) return "A+";
        if (score >= 75) return "A";
        if (score >= 60) return "B+";
        if (score >= 36) return "C";
        if (score == 35) return "Just Pass";
        return "Fail";
    }
}